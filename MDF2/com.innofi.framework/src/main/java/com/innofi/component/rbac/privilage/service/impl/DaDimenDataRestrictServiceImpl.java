
package com.innofi.component.rbac.privilage.service.impl;


import com.bstek.dorado.data.entity.EntityState;
import com.innofi.framework.service.impl.BaseServiceImpl;
import com.bstek.dorado.data.entity.EntityUtils;
import com.innofi.component.rbac.privilage.pojo.DaDimenDataRestrict;
import com.innofi.component.rbac.privilage.service.*;

import javax.annotation.Resource;

import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.PropertyOrder;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.component.rbac.privilage.pojo.DaTablePolicy;
import com.innofi.framework.utils.string.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import com.innofi.framework.dao.DaoSupport;

import java.util.*;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 * 功能/ 模块：权限管理模块，数据表约束信息服务
 * @author liumy2009@126.com
 * @version 2.0
 * @found date: 2010-11-23
 * @found time: 20:40:56
 * 修订历史：日期  作者  参考  描述
 */
@Component(value="daDimenDataRestrictService")
public class DaDimenDataRestrictServiceImpl  extends BaseServiceImpl<DaDimenDataRestrict,String> implements IDaDimenDataRestrictService {

	@Resource(name="mdDataTitleTableMappingDaoSupport")
	protected DaoSupport daoSupport;

	public void save(DaDimenDataRestrict entity) {
		DaDimenDataRestrict old = get(entity.getId());
		if(old==null)super.save(entity);
		else super.update(entity);
	}
	
	public DaoSupport getDaoSupport(){
		return daoSupport;
	}


    public void saveDaDimenDataRestricts(DaTablePolicy policy, Collection<DaDimenDataRestrict> objs, Map<String, String> parameter) {
        String tableAuthId = parameter.get("tableAuthId");
        IDaTablePolicyService daTablePolicyService = getSpringBean("daTablePolicyService");
        if(policy!=null){
            daTablePolicyService.update(policy);
        }

        for (Iterator iterator = objs.iterator(); iterator.hasNext(); ) {
            DaDimenDataRestrict daDimenRestrictInfo = (DaDimenDataRestrict) iterator.next();
            if (null != daDimenRestrictInfo) {
                if (StringUtil.hasText(tableAuthId)) {
                    this.editDaDimenDataRestrictInfo(daDimenRestrictInfo, tableAuthId);
                }
                EntityState state = EntityUtils.getState(daDimenRestrictInfo);
                if (EntityState.NEW.equals(state) || EntityState.MODIFIED.equals(state)) {
                    save(daDimenRestrictInfo);
                } else if (EntityState.DELETED.equals(state)) {
                	this.getDaoSupport().getJdbcDao().execute("delete from DA_DIMEN_DATA_RESTRICT_RELATED where RESTRICT_ID = '"+daDimenRestrictInfo.getId()+"'");
                	this.getDaoSupport().getJdbcDao().execute("delete from DA_DIMEN_DATA where DIMEN_DATA_ID not in (select DIMEN_DATA_ID from DA_DIMEN_DATA_RESTRICT_RELATED)");
                    delete(daDimenRestrictInfo);
                }
            }

            //保存当前记录的子记录
            Collection<DaDimenDataRestrict> children = EntityUtils.getValue(daDimenRestrictInfo, "children");
            if (null != children && children.size() > 0) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("tableAuthId", tableAuthId);
                saveDaDimenDataRestricts(null,children, map);
            }
        }
    }

    private void editDaDimenDataRestrictInfo(DaDimenDataRestrict daDimenRestrictInfo, String tableAuthId) {
        daDimenRestrictInfo.setTableAuthId(tableAuthId);
    }

	@Override
	public void removeNotJoinData(String tableAuthId) {
		this.getDaoSupport().getJdbcDao().execute("delete from DA_DIMEN_DATA_RESTRICT where table_auth_id='"+tableAuthId+"' and restrict_id not in (select restrict_id from DA_DIMEN_DATA_RESTRICT_RELATED) and restrict_type='3'");
	}

    @Override
    public List<DaDimenDataRestrict> findDaDimenRestrictByPolicyAndParentId(String policyId,String parentId) {
        List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
        addPropertyFilter(propertyFilters, "parentRestrictId", parentId, QueryConstants.EQUAL, true);
        if (StringUtils.isNotBlank(policyId)) {
            addPropertyFilter(propertyFilters,"tableAuthId", policyId, QueryConstants.EQUAL, true);
        }
        List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
        orders.add(new PropertyOrder("restrictType", QueryConstants.ORDER_ASC));

        return findByPropertyFilters(propertyFilters, orders);
    }


}

