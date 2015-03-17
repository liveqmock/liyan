package com.innofi.component.webservice.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.pojo.metadata.MdTable;
import com.innofi.framework.service.impl.BaseServiceImpl;
import com.innofi.component.webservice.service.IMdSysTableService;
@Component(value="mdSysTableService")
public class MdSysTableServiceImpl extends BaseServiceImpl<MdTable, String> implements IMdSysTableService{
	@Resource(name="mdDataTitleTableMappingDaoSupport")
	protected DaoSupport daoSupport;
	@Override
	public DaoSupport getDaoSupport(){
		return daoSupport;
	}

}
