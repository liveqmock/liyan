package com.innofi.component.webservice.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.pojo.metadata.MdEntityObject;
import com.innofi.framework.service.impl.BaseServiceImpl;
import com.innofi.component.webservice.service.IMdSysEntityObjectService;

@Component(value="mdSysEntityObjectService")
public class MdSysEntityObjectServiceImpl extends BaseServiceImpl<MdEntityObject,String>  implements IMdSysEntityObjectService{

	@Resource(name="mdDataTitleTableMappingDaoSupport")
	protected DaoSupport daoSupport;
	
    public DaoSupport getDaoSupport(){
		return daoSupport;
	}
}
