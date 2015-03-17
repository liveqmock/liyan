package com.innofi.component.webservice.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.innofi.framework.dao.DaoSupport;
import com.innofi.component.webservice.service.IManageWebservice;

@Component(value = "managerWebServiceService")
public class ManageWebserviceImpl implements IManageWebservice {

	public static final String VERSION_XML_ID ="1";
	
	@Resource(name="mdDataTitleTableMappingDaoSupport")
	protected DaoSupport daoSupport;

	public DaoSupport getDaoSupport(){
		return daoSupport;
	}

	@Override
	public String getWebserviceXml() {
		String retXml = "";
		int count = this.getDaoSupport().getJdbcDao().queryForObject("select count(*) from sys_version_xml",Integer.class);
		if (count == 0) {
			return retXml;
		}
		retXml =  this.getDaoSupport().getJdbcDao().queryForObject(
						"select version_xml from sys_version_xml where version_id=1",
						String.class);
		return retXml;
	}

	@Override
	public void saveWebserviceXml(String xml) {
		this.getDaoSupport().getJdbcDao().update("insert into sys_version_xml(version_id,version_xml) values(?,?)",
						new Object[] { ManageWebserviceImpl.VERSION_XML_ID, xml });
	}

	@Override
	public void deleteWebserviceXml() {
		this.getDaoSupport().getJdbcDao().update("delete from sys_version_xml");
	}
}
