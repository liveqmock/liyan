
package com.innofi.component.sie.service.impl;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.innofi.component.rbac.RBACConstants;
import com.innofi.framework.service.impl.BaseServiceImpl;
import com.innofi.component.sie.pojo.WfProcessCfg;
import com.innofi.component.sie.service.IWfProcessCfgService;
import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.QueryConstants;


/**
 * 功能/ 模块：todo 模块中文名称
 *
 * @author  todo 开发人员姓名 邮箱
 * @version todo 当前版本 日期
 *          todo 类描述
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
@Component(value="wfProcessCfgService")
public class WfProcessCfgServiceImpl  extends BaseServiceImpl<WfProcessCfg,String> implements IWfProcessCfgService {
	
	@Resource(name="mdDataTitleTableMappingDaoSupport")
	protected DaoSupport daoSupport;

	public DaoSupport getDaoSupport(){
		return daoSupport;
	}
	
	/*
	 * 通过模块编码、当前业务状态查询业务处理配置
	 */
	public WfProcessCfg findWfProcessCfg(String moduleCode, List orgLevelList,String status) {
		WfProcessCfg wfProcessCfg = null;
		
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		addPropertyFilter(filters, "moduleCode", moduleCode,QueryConstants.EQUAL, true);
		addPropertyFilter(filters, "orgLevel", orgLevelList,QueryConstants.IN, true);
		addPropertyFilter(filters, "status", status, QueryConstants.EQUAL, true);
		List<WfProcessCfg> wfProcessCfgList = this.findByPropertyFilters(filters, null);
		
		if (wfProcessCfgList == null || wfProcessCfgList.size() <= 0) {
			//没有业务处理配置
		} else if (wfProcessCfgList.size() > 1) {
			for (WfProcessCfg wfProcessCfgTemp : wfProcessCfgList) {
				String orgLevel = wfProcessCfgTemp.getOrgLevel();
				if (orgLevel.equals((String) orgLevelList.get(0))) {
					wfProcessCfg = wfProcessCfgTemp;
					break;
				}else if (orgLevel.equals(RBACConstants.ORG_LEVEL_ALL)) {
					wfProcessCfg = wfProcessCfgTemp;
				}
			}			
		} else {
			wfProcessCfg = (WfProcessCfg) wfProcessCfgList.get(0);
		}

		return wfProcessCfg;
	}
	
	/*
	 * 通过当前业务处理配置ID、查询下一步业务处理配置，得到下一步状态。
	 */
	public WfProcessCfg findWfProcessCfg(String prevNodeId) {
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		addPropertyFilter(filters, "prevNodeId", prevNodeId,QueryConstants.EQUAL, true);
		List<WfProcessCfg> wfProcessCfgList = this.findByPropertyFilters(filters, null);
		WfProcessCfg wfProcessCfg = (WfProcessCfg) wfProcessCfgList.get(0);

		return wfProcessCfg;
	}
}

