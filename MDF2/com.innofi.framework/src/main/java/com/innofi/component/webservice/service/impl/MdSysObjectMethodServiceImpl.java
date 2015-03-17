package com.innofi.component.webservice.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.pojo.metadata.MdObjectMethod;
import com.innofi.framework.service.impl.BaseServiceImpl;
import com.innofi.component.webservice.service.IMdSysObjectMethodService;

@Component(value = "mdSysObjectMethodService")
public class MdSysObjectMethodServiceImpl extends BaseServiceImpl<MdObjectMethod, String> implements IMdSysObjectMethodService {
	@Resource(name="mdDataTitleTableMappingDaoSupport")
	protected DaoSupport daoSupport;

	public DaoSupport getDaoSupport(){
		return daoSupport;
	}
}
