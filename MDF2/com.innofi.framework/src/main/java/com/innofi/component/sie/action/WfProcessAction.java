package com.innofi.component.sie.action;

import java.lang.Exception;
import java.lang.String;
import java.util.*;

import javax.annotation.Resource;

import com.bstek.dorado.annotation.Expose;
import com.innofi.component.sie.SieConstants;
import com.innofi.framework.FrameworkConstants;
import com.innofi.framework.dao.hibernate.PropertyOrder;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.component.rbac.user.pojo.SysUser;
import com.innofi.framework.spring.context.ContextHolder;
import com.innofi.framework.utils.string.StringUtil;

import org.springframework.stereotype.Component;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.innofi.component.codetransfer.IdfCodeTransfer;
import com.innofi.framework.exception.BaseException;
import com.innofi.framework.action.impl.BaseActionImpl;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.pojo.BasePojo;
import com.innofi.framework.service.IBaseService;
import com.innofi.component.sie.pojo.WfOperateStatus;
import com.innofi.component.sie.pojo.WfProcess;
import com.innofi.component.sie.service.IWfProcessService;


/**
 * 功能/ 模块：todo
 *
 * @author todo 开发人员姓名 邮箱
 * @version todo 当前版本 日期
 *          todo 为dorado7界面维护操作的提供支持，实现WfProcess对象信息的加载与保存操作
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
@Component
public class WfProcessAction extends BaseActionImpl {
    @Resource
    private IWfProcessService wfProcessSevice;

    public IBaseService getBusinessService() {
        return wfProcessSevice;
    }

    @DataProvider
    public void findWfProcesss(Page page, Map<String, Object> parameter) throws Exception {

        List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();

        if (parameter != null) {
        	String businessId = ((String)parameter.get("businessId"));
        	if(((String)parameter.get("businessId")).indexOf(",")!=-1){
        		String[] businessIds = StringUtil.split(businessId);
        		addPropertyFilter(propertyFilters, "businessId", businessIds, QueryConstants.IN,true);
        	}else{
        		addPropertyFilter(propertyFilters, "businessId", parameter.get("businessId"), QueryConstants.EQUAL,true);
        	}
            addDateRangePropertyFilter(propertyFilters, "crtDate", parameter);
            addPropertyFilter(propertyFilters, "crtOrgCode", parameter.get("crtOrgCode"), (String) parameter.get("qMcrtOrgCode"), true);
            addPropertyFilter(propertyFilters, "crtUserCode", parameter.get("crtUserCode"), (String) parameter.get("qMcrtUserCode"), true);
            addPropertyFilter(propertyFilters, "dealIdea", parameter.get("dealIdea"), (String) parameter.get("qMdealIdea"), true);
            addPropertyFilter(propertyFilters, "isEnd", parameter.get("isEnd"), (String) parameter.get("qMisEnd"), true);
            addPropertyFilter(propertyFilters, "isNew", parameter.get("isNew"), (String) parameter.get("qMisNew"), true);
            addPropertyFilter(propertyFilters, "moduleCode", parameter.get("moduleCode"), (String) parameter.get("qMmoduleCode"), true);
            addPropertyFilter(propertyFilters, "moduleName", parameter.get("moduleName"), (String) parameter.get("qMmoduleName"), true);
            addPropertyFilter(propertyFilters, "nodeId", parameter.get("nodeId"), (String) parameter.get("qMnodeId"), true);
            addDateRangePropertyFilter(propertyFilters, "operDate", parameter);
            addPropertyFilter(propertyFilters, "operDesc", parameter.get("operDesc"), (String) parameter.get("qMoperDesc"), true);
            addPropertyFilter(propertyFilters, "operOrgCode", parameter.get("operOrgCode"), (String) parameter.get("qMoperOrgCode"), true);
            addPropertyFilter(propertyFilters, "operPostId", parameter.get("operPostId"), (String) parameter.get("qMoperPostId"), true);
            addPropertyFilter(propertyFilters, "operRoleCode", parameter.get("operRoleCode"), (String) parameter.get("qMoperRoleCode"), true);
            addPropertyFilter(propertyFilters, "operType", parameter.get("operType"), (String) parameter.get("qMoperType"), true);
            addPropertyFilter(propertyFilters, "operUserCode", parameter.get("operUserCode"), (String) parameter.get("qMoperUserCode"), true);
            addPropertyFilter(propertyFilters, "id", parameter.get("id"), (String) parameter.get("qMid"), true);
            addPropertyFilter(propertyFilters, "status", parameter.get("status"), (String) parameter.get("qMstatus"), true);
            addDateRangePropertyFilter(propertyFilters, "updDate", parameter);
            addPropertyFilter(propertyFilters, "updOrgCode", parameter.get("updOrgCode"), (String) parameter.get("qMupdOrgCode"), true);
            addPropertyFilter(propertyFilters, "updUserCode", parameter.get("updUserCode"), (String) parameter.get("qMupdUserCode"), true);
        }

        com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);

        List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
        orders.add(new PropertyOrder("operDate", QueryConstants.ORDER_DESC));

        wfProcessSevice.findByPage_Filters(propertyFilters, orders, innofiPage);

        if (parameter.get("isNew") != null && page.getEntities().size() > 0) {
            WfProcess wfProcess = (WfProcess) page.getEntities().iterator().next();
            wfProcess.setDealIdea("");
        }


        Map map = new HashMap();
        map.put("crtOrgCode", "crtOrgCodeName");
        IdfCodeTransfer codeTransfer = wfProcessSevice.getIdfCodeTransfer();
        codeTransfer.transferResult(page.getEntities(), "sysOrgInfoService", map);

        map = new HashMap();
        map.put("crtUserCode", "crtUserCodeName");
        codeTransfer.transferResult(page.getEntities(), "sysUserService", map);

    }

    /**
     * 状态转换
     *
     * @param wfProcess
     * @param wfOperateStatus
     * @param businessObject
     * @return
     */
    @DataResolver
    public List saveWfProcesss(WfProcess wfProcess, WfOperateStatus wfOperateStatus, BasePojo businessObject) {
        WfProcess wfProcessNew = wfProcessSevice.saveWfProcess(wfProcess, wfOperateStatus, businessObject);
        List result = new ArrayList();
        result.add("1");
        result.add(wfProcessNew.getStatus());
        result.add(wfProcessNew.getNodeName());
        return result;
    }

    /**
     * 状态转换
     *
     * @param wfProcess
     * @param wfOperateStatus
     * @param businessObject
     * @return
     */
    @DataResolver
    public List saveWfProcesss(List<WfProcess> wfProcess, WfOperateStatus wfOperateStatus) {
    	List result = new ArrayList();
    	for(int i = 0 ; i < wfProcess.size() ; i++){
    		WfProcess wf = wfProcess.get(i);

    		WfProcess wfProcessNew = wfProcessSevice.saveWfProcess(wf, wfOperateStatus);
    		result = new ArrayList(); 
            result.add("1");
            result.add(wfProcessNew.getStatus());
            result.add(wfProcessNew.getNodeName());
    	}
        return result;
    }
    

    /**
     * 检查用户是否有权处理当前业务记录
     *
     * @return '0' 无  '1' 有  '2' 已不需要处理
     */
    @Expose
    public String checkOperPermission(String moduleCode, String businessObjectId) {
        SysUser sysUser = ContextHolder.getContext().getLoginUser();
        String result = "0";
        
        if(businessObjectId.indexOf(",")==-1){
	        Map queryParameters = new HashMap();
	        queryParameters.put("moduleCode", moduleCode);
	        queryParameters.put("businessObjectId", businessObjectId);
	        queryParameters.put("isNew", FrameworkConstants.COMM_Y);
	        queryParameters.put("operOrgCode", sysUser.getOrgCode());
	        queryParameters.put("roleList", Arrays.asList(sysUser.getFunctionRoleIds().split(",")));
	        queryParameters.put("operUserCode", sysUser.getUserCode());
	
	        WfProcess wfProcess = wfProcessSevice.findUniqueByHql(" from WfProcess w where w.moduleCode=? and w.businessId=? and w.isNew=? ", moduleCode, businessObjectId, FrameworkConstants.COMM_Y);
	        
	        if(wfProcess==null)throw new BaseException("状态引擎数据异常!");
	        
	        //没有权限处理
	        if (null != wfProcess.getIsEnd() && FrameworkConstants.COMM_Y.equals(wfProcess.getIsEnd())) {
	        	result =  "2";
	        } else if (SieConstants.BUSI_STATUS_WAIT_ADUIT.equals(wfProcess.getStatus())) {
	            wfProcess = null;
	            queryParameters.remove("isNew");
	            queryParameters.put("status", SieConstants.BUSI_STATUS_EDIT);
	            List<WfProcess> wfProceses = wfProcessSevice.findByNamedHql(" from WfProcess w where w.moduleCode=:moduleCode and w.businessId=:businessObjectId and (w.operOrgCode=:operOrgCode or w.operOrgCode is null ) and (w.operRoleCode in (:roleList) or w.operRoleCode is null) and (w.operUserCode = :operUserCode or w.operUserCode is null) and status=:status", queryParameters);
	            if (wfProceses.size() > 0) {
	            	result =  "1";
	            }
	        }
	        
	        if(result.equals("0")){
	        	queryParameters.remove("status");
		        queryParameters.put("isNew", FrameworkConstants.COMM_Y);
		        wfProcess = (WfProcess)wfProcessSevice.findUniqueByNamedHql(" from WfProcess w where w.moduleCode=:moduleCode and w.businessId=:businessObjectId and w.isNew=:isNew and (w.operOrgCode=:operOrgCode or w.operOrgCode is null ) and (w.operRoleCode in (:roleList) or w.operRoleCode is null) and (w.operUserCode = :operUserCode or w.operUserCode is null) ", queryParameters);
		        if (wfProcess != null) {
		        	result =  "1";
		        }
	        }
	        
        }else{
        	String[] businessOjbectIds = businessObjectId.split(",");
        	for(int i = 0 ; i < businessOjbectIds.length ; i++){
        		String bId = businessOjbectIds[i];
    	        Map queryParameters = new HashMap();
    	        queryParameters.put("moduleCode", moduleCode);
    	        queryParameters.put("businessObjectId", bId);
    	        queryParameters.put("isNew", FrameworkConstants.COMM_Y);
    	        queryParameters.put("operOrgCode", sysUser.getOrgCode());
    	        queryParameters.put("roleList", Arrays.asList(sysUser.getFunctionRoleIds().split(",")));
    	        queryParameters.put("operUserCode", sysUser.getUserCode());
    	
    	        WfProcess wfProcess = wfProcessSevice.findUniqueByHql(" from WfProcess w where w.moduleCode=? and w.businessId=? and w.isNew=? ", moduleCode, bId, FrameworkConstants.COMM_Y);
    	        
    	        if(wfProcess==null)throw new BaseException("状态引擎数据异常!");
    	        
    	        //没有权限处理
    	        if (null != wfProcess.getIsEnd() && FrameworkConstants.COMM_Y.equals(wfProcess.getIsEnd())) {
    	        	result =  "2";
    	        	break;
    	        } else if (SieConstants.BUSI_STATUS_WAIT_ADUIT.equals(wfProcess.getStatus())) {
    	            wfProcess = null;
    	            queryParameters.remove("isNew");
    	            queryParameters.put("status", SieConstants.BUSI_STATUS_EDIT);
    	            List<WfProcess> wfProceses = wfProcessSevice.findByNamedHql(" from WfProcess w where w.moduleCode=:moduleCode and w.businessId=:businessObjectId and (w.operOrgCode=:operOrgCode or w.operOrgCode is null ) and (w.operRoleCode in (:roleList) or w.operRoleCode is null) and (w.operUserCode = :operUserCode or w.operUserCode is null) and status=:status", queryParameters);
    	            if (wfProceses.size() > 0) {
    	            	result =  "1";
    	            }
    	        }
    	        queryParameters.remove("status");
    	        queryParameters.put("isNew", FrameworkConstants.COMM_Y);
    	        wfProcess = (WfProcess)wfProcessSevice.findUniqueByNamedHql(" from WfProcess w where w.moduleCode=:moduleCode and w.businessId=:businessObjectId and w.isNew=:isNew and (w.operOrgCode=:operOrgCode or w.operOrgCode is null ) and (w.operRoleCode in (:roleList) or w.operRoleCode is null) and (w.operUserCode = :operUserCode or w.operUserCode is null) ", queryParameters);
    	        if (wfProcess != null) {
    	        	result =  "1";
    	        }
        	}
        }
        
        return result;
    }


}
