package com.innofi.component.rbac.org.action;

import java.util.*;

import javax.annotation.Resource;

import com.innofi.framework.utils.string.StringUtil;
import com.innofi.framework.utils.validate.Validator;
import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;
import com.innofi.component.rbac.RBACConstants;
import com.innofi.component.rbac.org.service.ISysOrgInfoService;
import com.innofi.framework.FrameworkConstants;
import com.innofi.component.codetransfer.IdfCodeTransfer;
import com.innofi.framework.action.impl.BaseActionImpl;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.PropertyOrder;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.component.rbac.org.pojo.SysOrgInfo;
import com.innofi.framework.spring.context.ContextHolder;


/**
 * 功能/ 模块：
 *
 * @author 张恩维  eric.zhang@innofi.com.cn
 * @version 2.0.0 2012-12-11
 *          类描述
 *          修订历史：
 *          日期  作者  参考  描述
 * @see 相关类连接
 *      北京名晟信息技术有限公司版权所 有.
 */
@Component
public class SysOrgInfoAction extends BaseActionImpl {

    @Resource
    private ISysOrgInfoService sysOrgInfoService;
    private final static String ORG_PARENT_ROOT = ContextHolder.getSystemProperties().getString("mdf.orgRoot");

    @DataProvider
    public void findSysOrgInfos(Page page, Map<String, Object> parameter) throws Exception {
        List<PropertyFilter> propertyFilters = super.buildEqPropertyFilters(parameter);

        processOrgCodes("orgCode",parameter, propertyFilters);
        processOrgCodes("parentOrgCode",parameter, propertyFilters);

        com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);

      	List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
 	    PropertyOrder order = new PropertyOrder("category", QueryConstants.ORDER_ASC);
 	    orders.add(order);
        sysOrgInfoService.findByPage_Filters(propertyFilters, orders, innofiPage);

    }

    /**
     * 无权限树的展开
     * @param parameter
     * @return
     * @throws Exception
     */
    @DataProvider
    public List<SysOrgInfo> findSysOrgInfoAllTree(Map<String, Object> parameter) throws Exception {
    	List<PropertyFilter> propertyFilters = super.buildEqPropertyFilters(parameter);

        processOrgCodes("orgCode",parameter, propertyFilters);
        processOrgCodes("parentOrgCode",parameter, propertyFilters);

    	return sysOrgInfoService.findSysOrgInfoAllTree(propertyFilters);
    }
    /**
     * 根据策略来展开树，整个机构书全部展开，但是通过策略查到的树的信息，为有效可选信息。
     * findSysOrgInfoTreeByParentCodeForPolicy(这里用一句话描述这个方法的作用)
     * @param  @return    设定文件    
     * @return String    DOM对象    
     * @Exception 异常对象    
     * @since  CodingExample　Ver(编码范例查看) 1.1
     */
    @DataProvider
    public List<SysOrgInfo> findSysOrgInfoTreeForPolicy(Map<String, Object> parameter) throws Exception{
    	List<PropertyFilter> propertyFilters = super.buildEqPropertyFilters(parameter);
        processOrgCodes("orgCode",parameter, propertyFilters);
        processOrgCodes("parentOrgCode",parameter, propertyFilters);
        addPropertyFilter(propertyFilters, "status", FrameworkConstants.STATUS_EFFECTIVE, QueryConstants.EQUAL, true);//状态有效
    	return sysOrgInfoService.findSysOrgInfoTreeForPolicy(propertyFilters);
    }
    
    /**
     * 
     * findSysOrgInfoTreeGridForPolicy(这里用一句话描述这个方法的作用)
     * @param  @return    设定文件    
     * @return String    DOM对象    
     * @Exception 异常对象    
     * @since  CodingExample　Ver(编码范例查看) 1.1
     */
    @DataProvider
    public List<SysOrgInfo> findSysOrgInfoTreeGridForPolicy(Map<String, Object> parameter) throws Exception{
    	List<PropertyFilter> propertyFilters = super.buildEqPropertyFilters(parameter);
        processOrgCodes("orgCode",parameter, propertyFilters);
        processOrgCodes("parentOrgCode",parameter, propertyFilters);
    	List<SysOrgInfo> orgslist = sysOrgInfoService.findSysOrgInfoTreeForPolicy(propertyFilters);
    	List codesList = new ArrayList();
		for(SysOrgInfo org:orgslist){
			codesList.add(org.getOrgCode());
		}
		Map orgIdToRolesMap_over = sysOrgInfoService.findRolesStringByOrgCodes(codesList);
		IdfCodeTransfer codeTransfer = ContextHolder.getSpringBean("mdf.codeTransfer");
		ListOrderedMap cacheObjects = codeTransfer.getCacheObjects("sysOrgInfoService");
		for(int i=0;i<orgslist.size();i++){
			SysOrgInfo orgInfo=orgslist.get(i);
			if(RBACConstants.ORG_CATEGORY_DEPT.equals(orgInfo.getCategory())){//部门
				SysOrgInfo realDept=(SysOrgInfo) cacheObjects.get(orgInfo.getOrgCode());
				SysOrgInfo parentOrg=(SysOrgInfo) cacheObjects.get(realDept.getParentOrgCode());
				if(parentOrg!=null){
					orgInfo.setParentOrgName(parentOrg.getOrgName());
				}
				SysOrgInfo parentDept=(SysOrgInfo)cacheObjects.get(realDept.getParentDeptCode());
				if(parentDept!=null){
					orgInfo.setParentDeptName(parentDept.getOrgName());
				}
				StringBuffer deptRoles = (StringBuffer)orgIdToRolesMap_over.get(orgInfo.getOrgCode());
			    if(null!=deptRoles){
				orgInfo.setDeptRoles(deptRoles.toString());
			    }
			}
		}
    	return orgslist;
    }
    
    @DataProvider
    public void findSysOrgInfoList(Page page, Map<String, Object> parameter) throws Exception {
        List<PropertyFilter> propertyFilters = super.buildEqPropertyFilters(parameter);
        String depFlag=(String) parameter.get("depFlag");//查询所属机构为orgCode的部门列表
        addPropertyFilter(propertyFilters, "orgName", parameter.get("orgName"), QueryConstants.LIKE, true);
        addPropertyFilter(propertyFilters, "status", FrameworkConstants.STATUS_EFFECTIVE, QueryConstants.EQUAL, true);//状态有效
        removePropertyFilter(propertyFilters, "searchType");
        removePropertyFilter(propertyFilters, "depFlag");
        processOrgCodes("orgCode",parameter, propertyFilters);
        processOrgCodes("parentOrgCode",parameter, propertyFilters);
        com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
        if(FrameworkConstants.COMM_Y.equals(depFlag)){
        	sysOrgInfoService.findByPage_Filters(propertyFilters, null,innofiPage);
        }else{
        	sysOrgInfoService.findSysOrgInfoList((String) parameter.get("searchType"), (String) parameter.get("parentOrgCode"), propertyFilters, innofiPage);
        }
    }

    @DataResolver
    public void saveSysOrgInfos(Collection<SysOrgInfo> objs) {
        for (Iterator iterator = objs.iterator(); iterator.hasNext(); ) {
            SysOrgInfo sysOrgInfo = (SysOrgInfo) iterator.next();
            Collection<SysOrgInfo> children = EntityUtils.getValue(sysOrgInfo, "children");
            if (children != null && children.size() > 0) {
                saveSysOrgInfos(children);
            }
            EntityState state = EntityUtils.getState(sysOrgInfo);
            if (EntityState.NEW.equals(state)) {
            	sysOrgInfo.setOrgSeq(sysOrgInfo.getOrgSeq()+sysOrgInfo.getOrgCode()+".");
            	
            	if(RBACConstants.ORG_CATEGORY_DEPT.equals(sysOrgInfo.getCategory())){
            		//获取上级部门信息 
            		String parentDeptCode = sysOrgInfo.getParentDeptCode();
            		String deptSeq = "";
            		//存在上级部门，获取DEPTSEQ
            		if(StringUtils.isNotBlank(parentDeptCode)){
            			SysOrgInfo parentDeptInfo = sysOrgInfoService.getOrgInfoByOrgCode(parentDeptCode);
            			deptSeq = parentDeptInfo.getDeptSeq()+sysOrgInfo.getOrgCode()+".";
            		}else{
            			deptSeq = "."+sysOrgInfo.getOrgCode()+".";
            		}
            		sysOrgInfo.setDeptSeq(deptSeq);
            	
            	}
                sysOrgInfoService.save(sysOrgInfo);
            } else if (EntityState.MODIFIED.equals(state)) {
            	String category=sysOrgInfo.getCategory();
            	if(RBACConstants.ORG_CATEGORY_DEPT.equals(category)){
            		SysOrgInfo org = sysOrgInfoService.get(sysOrgInfo.getParentOrgId());
            		sysOrgInfo.setOrgSeq(org.getOrgSeq()+sysOrgInfo.getOrgCode()+".");
             		String deptSeq = "";
            		//存在上级部门，获取DEPTSEQ
            		if(StringUtils.isNotBlank(sysOrgInfo.getParentDeptCode())){
            			SysOrgInfo parentDeptInfo = sysOrgInfoService.getOrgInfoByOrgCode(sysOrgInfo.getParentDeptCode());
            			deptSeq = parentDeptInfo.getDeptSeq()+sysOrgInfo.getOrgCode()+".";
            		}else{
            			deptSeq = "."+sysOrgInfo.getOrgCode()+".";
            		}
            		sysOrgInfo.setDeptSeq(deptSeq);

            	}
                sysOrgInfoService.update(sysOrgInfo);
            } else if (EntityState.DELETED.equals(state)) {
                sysOrgInfoService.delete(sysOrgInfo);
            }
        }
    }

    /**
     * 检查编号
     */
    @Expose
    public String checkOrgCode(String code) {
        return sysOrgInfoService.checkObjectExist(code) + "";
    }
    
    /**
     * 机构停用校验
     */
    @Expose
    public String invalidOrg(String orgCode) {
        return sysOrgInfoService.invalidOrg(orgCode)+"";
    }
    
    /**
     * 部门停用校验
     */
    @Expose
    public String invalidDep(String orgCode) {
        return sysOrgInfoService.invalidDep(orgCode);
    }

    /**
     * 取当前节点 ROLES
     */
    @Expose
    public String getRolesByDeptCode(String orgCode) {
    	List codeLsit = new ArrayList(1);
    	codeLsit.add(orgCode);
    	Map<String,StringBuffer> map= sysOrgInfoService.findRolesStringByOrgCodes(codeLsit);
    	StringBuffer deptRoles = (StringBuffer)map.get(orgCode);
    	if(null!=deptRoles){
    		return deptRoles.toString();
    	}else{
    		return "";
    	}

    }

    private void processOrgCodes(String fieldName,Map<String, Object> parameter, List<PropertyFilter> propertyFilters) {
        String orgCode = (String) parameter.get(fieldName);
        if(Validator.isNotEmpty(orgCode)&&orgCode.indexOf(",")>-1){
            String orgCodes[] = StringUtil.split(orgCode);
            List<String> orgCodeList = Arrays.asList(orgCodes);
            addPropertyFilter(propertyFilters,fieldName,orgCodeList, QueryConstants.IN,true);
        }else{
        	addPropertyFilter(propertyFilters,fieldName,orgCode, QueryConstants.EQUAL,true);
        }
    }
    
}
