
package com.innofi.component.metadata.dataview.service.impl;


import java.util.List;

import com.innofi.framework.pojo.metadata.MdDataView;
import com.innofi.component.metadata.dataview.service.IMdDataViewService;

import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.framework.service.impl.BaseServiceImpl;


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
@Component(value="mdDataViewService")
public class MdDataViewServiceImpl  extends BaseServiceImpl<MdDataView,String> implements IMdDataViewService {
	
	@Resource(name="mdDataTitleTableMappingDaoSupport")
	protected DaoSupport daoSupport;

	public DaoSupport getDaoSupport(){
		return daoSupport;
	}
	
	public List<MdDataView> findDataViewsByParentId(String parentId){
	    	return findByProperty("parentId", parentId, QueryConstants.EQUAL);
	}

}

