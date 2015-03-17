package com.innofi.component.rbac.log.action;

import com.innofi.component.metadata.entityobject.service.IMdEntityObjectService;
import com.innofi.component.metadata.table.service.IMdTableService;
import com.innofi.component.rbac.log.pojo.SysLogCfg;
import com.innofi.component.rbac.log.service.ISysLogCfgService;
import com.innofi.framework.action.impl.BaseActionImpl;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.framework.pojo.metadata.MdTable;
import com.innofi.framework.pojo.metadata.MdEntityObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import java.util.Map;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;

/**
 * 功能/ 模块：todo
 * 
 * @author todo 开发人员姓名 邮箱
 * @version todo 当前版本 日期 todo 为dorado7界面维护操作的提供支持，实现SysLogCfg对象信息的加载与保存操作 修订历史：
 *          日期 作者 参考 描述 北京名晟信息技术有限公司版权所有.
 */
@Component
public class SysLogCfgAction extends BaseActionImpl {
	@Resource
	private ISysLogCfgService sysLogCfgSevice;
	@Resource
	private IMdTableService mdTableService;
	@Resource
	private IMdEntityObjectService mdEntityObjectService;

	@DataProvider
	public void findSysLogCfgs(Page page, Map<String, Object> parameter)
			throws Exception {

		List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();

		if (parameter != null) {
			addDateRangePropertyFilter(propertyFilters, "crtDate", parameter);
			addPropertyFilter(propertyFilters, "crtOrgCode",
					parameter.get("crtOrgCode"), parameter.get("qMcrtOrgCode")
							.toString(), true);
			addPropertyFilter(propertyFilters, "crtUserCode",
					parameter.get("crtUserCode"), parameter
							.get("qMcrtUserCode").toString(), true);
			addPropertyFilter(propertyFilters, "id", parameter.get("id"),
					parameter.get("qMid").toString(), true);
			addPropertyFilter(propertyFilters, "logRule",
					parameter.get("logRule"), parameter.get("qMlogRule")
							.toString(), true);
			addPropertyFilter(propertyFilters, "methodId",
					parameter.get("methodId"), parameter.get("qMmethodId")
							.toString(), true);
			addPropertyFilter(propertyFilters, "methodName",
					parameter.get("methodName"), parameter.get("qMmethodName")
							.toString(), true);
			addPropertyFilter(propertyFilters, "objName",
					parameter.get("objName"), parameter.get("qMobjName")
							.toString(), true);
			addPropertyFilter(propertyFilters, "operType",
					parameter.get("operType"), parameter.get("qMoperType")
							.toString(), true);
			addPropertyFilter(propertyFilters, "resourceCode",
					parameter.get("resourceCode"),
					parameter.get("qMresourceCode").toString(), true);
			addPropertyFilter(propertyFilters, "resourceName",
					parameter.get("resourceName"),
					parameter.get("qMresourceName").toString(), true);
			addDateRangePropertyFilter(propertyFilters, "updDate", parameter);
			addPropertyFilter(propertyFilters, "updOrgCode",
					parameter.get("updOrgCode"), parameter.get("qMupdOrgCode")
							.toString(), true);
			addPropertyFilter(propertyFilters, "updUserCode",
					parameter.get("updUserCode"), parameter
							.get("qMupdUserCode").toString(), true);
		}

		com.innofi.framework.dao.pagination.Page innofiPage = super
				.translateDoradoPageToInnofiPage(page);

		sysLogCfgSevice.findByPage_Filters(propertyFilters, null, innofiPage);

	}

	@DataResolver
	public void saveSysLogCfgs(Collection<SysLogCfg> objs) {
		for (Iterator iterator = objs.iterator(); iterator.hasNext();) {
			SysLogCfg sysLogCfg = (SysLogCfg) iterator.next();
			EntityState state = EntityUtils.getState(sysLogCfg);
			if (EntityState.DELETED.equals(state)) {
				sysLogCfgSevice.delete(sysLogCfg);
			} else if (EntityState.MODIFIED.equals(state)
					|| EntityState.MOVED.equals(state)) {
				sysLogCfgSevice.update(sysLogCfg);
			} else if (EntityState.NEW.equals(state)) {
				sysLogCfgSevice.save(sysLogCfg);
			}
		}
	}

	@Expose
	public String findServiceByMenuUrl(String menuUrl) {
		String serviceName = "";
		List<MdTable> mdTableList = mdTableService.findByProperty("uiName",
				menuUrl, QueryConstants.EQUAL);
		if (mdTableList.size() > 0) {
			MdTable a = mdTableList.get(0);
			serviceName = a.getServiceName();
		}

		if (StringUtils.isNotBlank(serviceName)) {
			serviceName = serviceName
					.substring(serviceName.lastIndexOf(".") + 1);
			serviceName = serviceName.substring(0, 1).toLowerCase()
					+ serviceName.substring(1, serviceName.length());
			List<MdEntityObject> objectList = mdEntityObjectService
					.findByProperty("objName", serviceName,
							QueryConstants.EQUAL);

			String objId = "";
			if (mdTableList.size() > 0) {
				MdEntityObject b = objectList.get(0);
				objId = b.getId();
			}
			serviceName = serviceName + "|" + objId;
		}

		return serviceName;

	}

}
