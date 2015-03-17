
package com.innofi.component.rbac.dict.service;

import com.innofi.component.rbac.dict.pojo.SysDict;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.pagination.Page;
import com.innofi.framework.service.IBaseService;

import java.util.List;

/**
 * 功能/ 模块：数据字典Service
 *
 * @author 张恩维 eric.zhang@innofi.com.cn
 * @version 2.0.0 12-11-30
 *          用户服务接口
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
public interface ISysDictService  extends IBaseService<SysDict,String> {

    /**
	 * 
	 * @param searchType
	 * @param parentDictId
	 * @param propertyFilters
	 * @return
	 */
	void findSysDictsList(String searchType, String parentDictId,
			List<PropertyFilter> propertyFilters,Page innofiPage);

	/**
	 * 判断相同字典类型的字典编码是否重复
	 * @param dictCode
	 * @param dictType
	 * @return
	 */
	boolean checkDictCode(String dictCode, String dictType);
	
}