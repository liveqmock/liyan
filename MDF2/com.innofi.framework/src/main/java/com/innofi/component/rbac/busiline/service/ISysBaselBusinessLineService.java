
package com.innofi.component.rbac.busiline.service;

import java.util.List;

import com.innofi.component.rbac.busiline.pojo.SysBaselBusinessLine;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.pagination.Page;
import com.innofi.framework.service.IBaseService;

public interface ISysBaselBusinessLineService  extends IBaseService<SysBaselBusinessLine,String> {

	/**
	 * @param searchType
	 * @param parentId 
	 * @param propertyFilters
	 * @param innofiPage
	 */
	void findSysBaselBusinessLineList(String searchType, String parentId,List<PropertyFilter> propertyFilters,Page innofiPage);

	/**
	 * 停用业务线
	 * @param code
	 * @return
	 */
	String invalidBizLine(String code);
	
}