package com.innofi.component.rbac.privilage.action;

import com.innofi.framework.service.IBaseService;
import com.innofi.component.rbac.privilage.pojo.DaDimenDataRestrictRelated;
import com.innofi.component.rbac.privilage.service.IDaDimenDataRestrictRelatedService;

import com.innofi.framework.action.impl.BaseActionImpl;
import com.innofi.framework.dao.hibernate.PropertyFilter;

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
 * @author todo 开发人员姓名 邮箱
 * @version todo 当前版本 日期 todo
 *          为dorado7界面维护操作的提供支持，实现DaDimenDataRestrictRelated对象信息的加载与保存操作 修订历史：
 *          日期 作者 参考 描述 北京名晟信息技术有限公司版权所有.
 */
@Component
public class DaDimenDataRestrictRelatedAction extends BaseActionImpl {
	@Resource
	private IDaDimenDataRestrictRelatedService daDimenDataRestrictRelatedSevice;

	public IBaseService getBusinessService() {
		return daDimenDataRestrictRelatedSevice;
	}

	@DataProvider
	public void findDaDimenDataRestrictRelateds(Page page,
			Map<String, Object> parameter) throws Exception {

		List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();

		if (parameter != null) {
			addPropertyFilter(propertyFilters, "restrictId",
					parameter.get("restrictId"),
					(String) parameter.get("qMrestrictId"), true);
			addPropertyFilter(propertyFilters, "id", parameter.get("id"),
					(String) parameter.get("qMid"), true);
			addPropertyFilter(propertyFilters, "dimenDataId",
					parameter.get("dimenDataId"),
					(String) parameter.get("qMdimenDataId"), true);
		}

		com.innofi.framework.dao.pagination.Page innofiPage = super
				.translateDoradoPageToInnofiPage(page);

		daDimenDataRestrictRelatedSevice.findByPage_Filters(propertyFilters,
				null, innofiPage);

	}

	@DataResolver
	public void saveDaDimenDataRestrictRelateds(
			Collection<DaDimenDataRestrictRelated> objs) {
		for (Iterator iterator = objs.iterator(); iterator.hasNext();) {
			DaDimenDataRestrictRelated daDimenDataRestrictRelated = (DaDimenDataRestrictRelated) iterator
					.next();
			EntityState state = EntityUtils
					.getState(daDimenDataRestrictRelated);
			if (EntityState.DELETED.equals(state)) {
				daDimenDataRestrictRelatedSevice
						.delete(daDimenDataRestrictRelated);
			} else if (EntityState.MODIFIED.equals(state)
					|| EntityState.MOVED.equals(state)) {
				daDimenDataRestrictRelatedSevice
						.update(daDimenDataRestrictRelated);
			} else if (EntityState.NEW.equals(state)) {
				daDimenDataRestrictRelatedSevice
						.save(daDimenDataRestrictRelated);
			}
		}
	}

}
