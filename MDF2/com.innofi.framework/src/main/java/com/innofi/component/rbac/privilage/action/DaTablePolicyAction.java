package com.innofi.component.rbac.privilage.action;
import com.innofi.framework.pojo.metadata.MdDimension;
import com.innofi.component.rbac.privilage.pojo.DaTablePolicy;
import com.innofi.component.rbac.privilage.pojo.DaDimenDataRestrict;
import com.innofi.component.rbac.privilage.service.IDaDimenControlService;
import com.innofi.component.rbac.privilage.service.IDaDimenDataRestrictRelatedService;
import com.innofi.component.rbac.privilage.service.IDaDimenDataRestrictService;
import com.innofi.component.rbac.privilage.service.IDaTablePolicyService;

import com.innofi.framework.action.impl.BaseActionImpl;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.QueryConstants;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import java.util.Map;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;


/**
 * 功能/ 模块：todo
 *
 * @author  todo 开发人员姓名 邮箱
 * @version todo 当前版本 日期
 *          todo 为dorado7界面维护操作的提供支持，实现DaTablePolicy对象信息的加载与保存操作
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
@Component
public class DaTablePolicyAction extends BaseActionImpl {
    @Resource
	private   IDaTablePolicyService daTablePolicySevice;
    @Resource
    private IDaDimenDataRestrictRelatedService daDimenDataRestrictRelatedService;
    
    @Resource
	private IDaDimenDataRestrictService daDimenDataRestrictSevice;
    
    @Resource
	private   IDaDimenControlService daDimenControlSevice;
    
    @DataProvider
    public void findDaTablePolicys(Page page, Map<String, Object> parameter) throws Exception {
    	List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
    	if(parameter != null){
    		addPropertyFilter(propertyFilters,"tableName",parameter.get("tableName"),parameter.get("qMtableName").toString(),true);
			addPropertyFilter(propertyFilters,"operType",parameter.get("operType"),parameter.get("qMoperType").toString(),true);
    	}
        com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
        daTablePolicySevice.findByPage_Filters(propertyFilters, null, innofiPage);
    }
    
    @DataProvider
    public List<DaTablePolicy> findDaTablePolicysForPrivilege(String roleId) throws Exception {
        return daTablePolicySevice.findDaTablePoliciesForPrivilege(roleId);
    }

    @DataResolver
    public String saveDaTablePolicysWithDimens(DaTablePolicy policy,List<MdDimension> dimens) {
    	return daTablePolicySevice.saveDaTablePoliciesWithDimens(policy, dimens);
    }

    /**
     * 保存数据访问策略
     *
     * @param policy 数据访问策略
     * @param dataRestricts 约束条件集合
     */
    @DataResolver
    public void saveDaTablePolicyWithRestricts(DaTablePolicy policy,List<DaDimenDataRestrict> dataRestricts){
        daTablePolicySevice.saveDaTablePolicyWithRestricts(policy,dataRestricts);
        daDimenDataRestrictSevice.removeNotJoinData(policy.getId());
    }
    
    @DataProvider
    public DaTablePolicy findDaTablePolicyById(String id) throws Exception {
        return daTablePolicySevice.get(id);
    }
    
    @DataResolver
    public void deleteDaTablePolicyWithDimens(Collection<DaTablePolicy> objs) {
    	for(DaTablePolicy policy:objs){
    		EntityState state = EntityUtils.getState(policy);
        	if(EntityState.DELETED.equals(state)){//删除策略
        		List<DaDimenDataRestrict> restricts = daDimenDataRestrictSevice.findByProperty("tableAuthId", policy.getId(), QueryConstants.EQUAL);
        		List restrictIds = daDimenDataRestrictSevice.getPropertyValueList(restricts,daDimenDataRestrictSevice.getDaoSupport().getHibernateDao().getIdPropertyName(daDimenDataRestrictSevice.getEntityType()));
        		daDimenDataRestrictSevice.removeNotJoinData(policy.getId());
        		daDimenDataRestrictRelatedService.deleteByPropertyFilters(buildPropertyFilter("restrictId", restrictIds, QueryConstants.IN));
        		daDimenControlSevice.deleteControlData(policy.getId(), null);
        		daTablePolicySevice.deleteDaTablePolicy(policy);
        		daDimenDataRestrictSevice.deleteByPropertyFilters(buildPropertyFilter("id", restrictIds, QueryConstants.IN));
        	}
    	}
    }
    
    @DataResolver
    public void saveDaTablePolicys(Collection<DaTablePolicy> objs) {
		for (Iterator iterator = objs.iterator(); iterator.hasNext();) {
			DaTablePolicy daTablePolicy = (DaTablePolicy) iterator.next();
			EntityState state = EntityUtils
					.getState(daTablePolicy);
			if (EntityState.DELETED.equals(state)) {
				daTablePolicySevice.delete(daTablePolicy);
			} else if (EntityState.MODIFIED.equals(state)
					|| EntityState.MOVED.equals(state)) {
				daTablePolicySevice.update(daTablePolicy);
			} else if (EntityState.NEW.equals(state)) {
				daTablePolicySevice.save(daTablePolicy);
			}
		}
    }
}
