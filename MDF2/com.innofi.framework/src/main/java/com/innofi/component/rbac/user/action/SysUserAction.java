package com.innofi.component.rbac.user.action;

import com.innofi.component.rbac.org.service.ISysOrgInfoService;
import com.innofi.framework.FrameworkConstants;
import com.innofi.component.rbac.user.pojo.SysUser;
import com.innofi.component.rbac.user.service.ISysUserService;

import com.innofi.framework.action.impl.BaseActionImpl;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.PropertyOrder;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.framework.spring.context.ContextHolder;
import com.innofi.framework.utils.codec.MD5Util;
import com.innofi.framework.utils.string.StringUtil;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import java.util.Map;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;


/**
 * 为dorado7界面维护操作的提供支持，实现SysUser对象信息的加载与保存操作
 */

@Component
public class SysUserAction extends BaseActionImpl {
    @Resource
    private ISysUserService sysUserService;

    /**
     * 用户维护列表
     *
     * @param page
     * @param parameter
     * @throws Exception
     */
    @DataProvider
    public void findSysUsers(Page page, Map<String, Object> parameter) throws Exception {

        List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();

        if (parameter != null) {
            addPropertyFilter(propertyFilters, "userCode", parameter.get("userCode"), parameter.get("qMuserCode").toString(), true);
            addPropertyFilter(propertyFilters, "userName", parameter.get("userName"), parameter.get("qMuserName").toString(), true);
            ISysOrgInfoService sysOrgInfoService = getSpringBean("sysOrgInfoService");
            if(null!=parameter.get("orgCode")){
                if(!ContextHolder.getSystemProperties().getString("mdf.cbRoot").equals(parameter.get("orgCode"))){
                	List<Object> orgCodes = sysOrgInfoService.getPropertyValueList(sysOrgInfoService.getDownLevelOrgsByOrgCode((String) parameter.get("orgCode")), "orgCode");
                	if(orgCodes.size()>500){
                		int sumCount = orgCodes.size()%500;
                		for(int i = 0 ; i <=sumCount ; i++){
                			int startCount = i*500;
                            if(startCount>orgCodes.size())break;
                			int endCount = (i+1)*500;
                			if(endCount>orgCodes.size())endCount = orgCodes.size();
                			addOrPropertyFilter(propertyFilters, "orgCode", orgCodes.subList(startCount, endCount), QueryConstants.IN, false);
                		}
                	}else if(orgCodes.size()>0){
                		addOrPropertyFilter(propertyFilters, "orgCode", orgCodes, QueryConstants.IN, false);
                	}
                    
                }
            }
            addPropertyFilter(propertyFilters, "status", parameter.get("status"), parameter.get("qMstatus").toString(), true);
        }

        com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
        List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
    	orders.add(new PropertyOrder("orgCode",QueryConstants.ORDER_ASC));
    	
        sysUserService.findByPage_Filters(propertyFilters, orders, innofiPage);

    }
    
	/**
	 * 根据岗位Id查询其所属用户
	 * 
	 * @param page
	 * @param parameter
	 * @throws Exception
	 */
	@DataProvider
	public void findSysUserListByPostId(Page page, Map<String, Object> parameter) throws Exception {
		if (null != parameter) {
			String postId = (String) parameter.get("postId");
			String orgCode = (String) parameter.get("orgCode");
			com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
			// 先根据岗位id查询用户，如果没有，则根据部门查询用户
			if (StringUtil.hasText(postId)) {
				String hql = "select u from SysUserPost p,SysUser u where p.postId= ? and p.userId = u.id and u.status = ?";
				sysUserService.findByHql_Page(hql, innofiPage, new Object[] { postId, FrameworkConstants.STATUS_EFFECTIVE });
			}
			if (innofiPage.getEntityCount() == 0) {
				if (StringUtil.hasText(orgCode)) {
					String hql = "from SysUser where orgCode= ? and status = ?";
					sysUserService.findByHql_Page(hql, innofiPage, new Object[] { orgCode, FrameworkConstants.STATUS_EFFECTIVE });
				}
			}
		}
	}

    /**
     * 用户下拉框(状态为有效)
     *
     * @param page
     * @param parameter
     * @throws Exception
     */
    @DataProvider
    public void findSysUserList(Page page, Map<String, Object> parameter) throws Exception {

        List<PropertyFilter> propertyFilters = super.buildEqPropertyFilters(parameter);
        addPropertyFilter(propertyFilters, "status", FrameworkConstants.STATUS_EFFECTIVE, QueryConstants.EQUAL, true);//状态有效
        if (parameter != null) {
            addPropertyFilter(propertyFilters, "userName", parameter.get("userName"), QueryConstants.LIKE, true);
            ISysOrgInfoService sysOrgInfoService = getSpringBean("sysOrgInfoService");

            if(null!=parameter.get("orgCode")){
                removePropertyFilter(propertyFilters,"orgCode");
                if(!ContextHolder.getSystemProperties().getString("mdf.cbRoot").equals(parameter.get("orgCode"))){
                	List<Object> orgCodes = sysOrgInfoService.getPropertyValueList(sysOrgInfoService.getDownLevelOrgsByOrgCode((String) parameter.get("orgCode")), "orgCode");
                	if(orgCodes.size()>500){
                		int sumCount = orgCodes.size()%500;
                		for(int i = 0 ; i <=sumCount ; i++){
                			int startCount = i*500;
                			int endCount = (i+1)*500;
                			if(endCount>orgCodes.size())endCount = orgCodes.size();
                			addOrPropertyFilter(propertyFilters, "orgCode", orgCodes.subList(startCount, endCount), QueryConstants.IN, false);
                		}
                	}else if(orgCodes.size()>0){
                		addOrPropertyFilter(propertyFilters, "orgCode", orgCodes, QueryConstants.IN, false);
                	}
                }
            }
        }

        com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
        List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
    	orders.add(new PropertyOrder("orgCode",QueryConstants.ORDER_ASC));
        sysUserService.findByPage_Filters(propertyFilters, orders, innofiPage);

    }

    @DataResolver
    public void saveSysUsers(Collection<SysUser> objs) {
        for (Iterator iterator = objs.iterator(); iterator.hasNext(); ) {
            SysUser sysUser = (SysUser) iterator.next();
            EntityState state = EntityUtils.getState(sysUser);
            if (EntityState.NEW.equals(state)) {
                sysUserService.save(sysUser);
            } else if (EntityState.MODIFIED.equals(state)) {
                sysUserService.update(sysUser);
            } else if (EntityState.DELETED.equals(state)) {
                sysUserService.delete(sysUser);
            }
        }
    }

    @DataResolver
    public void saveSysUser(SysUser sysUser) {

        sysUserService.update(sysUser);

    }

    @Expose
    public String resetUserPassword(Map<String, Object> parameter) {
        List<String> userIds = (List<String>) parameter.get("userId");
        for (String userId : userIds) {
            sysUserService.updateUserPassword(userId, null);
        }
        return "1";
    }

    @Expose
    public void modifyPassword(Map<String, Object> parameter) {
        String userId = (String) parameter.get("userId");
        String password = (String) parameter.get("password");
        sysUserService.updateUserPassword(userId, password);
    }

    @Expose
    public String checkPassword(Map<String, String> parameter) {
        String flag = "0";
        String oldPassword = parameter.get("oldPassword");
        SysUser user = (SysUser) ContextHolder.getContext().getLoginUser();
        if (user.getPassword().equals(MD5Util.toMD5(oldPassword))) {
            flag = "1";
        }
        return flag;

    }

    @Expose
    public String checkCode(String userCode) {
        return sysUserService.checkEntityExist("userCode", userCode) + "";
    }

    @DataProvider
    public SysUser findUser() {
        SysUser user = ContextHolder.getContext().getLoginUser();
        return user;
    }
}
