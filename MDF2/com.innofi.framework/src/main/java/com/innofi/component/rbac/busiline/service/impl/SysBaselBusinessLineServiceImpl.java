
package com.innofi.component.rbac.busiline.service.impl;


import java.util.ArrayList;
import java.util.List;

import com.innofi.component.rbac.busiline.pojo.SysBaselBusinessLine;
import com.innofi.component.rbac.busiline.service.ISysBaselBusinessLineService;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.innofi.framework.FrameworkConstants;
import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.framework.dao.pagination.Page;
import com.innofi.framework.service.impl.BaseServiceImpl;



@Component(value="sysBaselBusinessLineService")
public class SysBaselBusinessLineServiceImpl  extends BaseServiceImpl<SysBaselBusinessLine,String> implements ISysBaselBusinessLineService {
	
	@Resource(name="mdDataTitleTableMappingDaoSupport")
	protected DaoSupport daoSupport;

	public DaoSupport getDaoSupport(){
		return daoSupport;
	}

	public String getCnFieldName() {
		return "busilineBaselName";
	}

	public void findSysBaselBusinessLineList(String searchType,
			String parentId, List<PropertyFilter> filters,
			Page innofiPage) {
		if ("tree".equals(searchType) && !StringUtils.isBlank(parentId)) {
			String baselCode="0";
			if(!"0".equals(parentId)){
				SysBaselBusinessLine line=this.get(parentId);
				baselCode=line.getBusilineBaselCode();
			}
            this.removePropertyFilter(filters, "parentId");
            this.addPropertyFilter(filters, "id", parentId, QueryConstants.NOT_EQUAL, true);
            this.addPropertyFilter(filters, "treeSeq", "." + baselCode + ".", QueryConstants.LIKE, true);
            findByPage_Filters(filters, null, innofiPage);
        } else {
            findByPage_Filters(filters, null, innofiPage);
        }
	}
	
	public String invalidBizLine(String code) {
		List<PropertyFilter> filters=new ArrayList<PropertyFilter>();
		this.addPropertyFilter(filters, "treeSeq", "." + code + ".", QueryConstants.LIKE, true);
		List<SysBaselBusinessLine> lines=this.findByPropertyFilters(filters, null);
		for(SysBaselBusinessLine line:lines){
			line.setStatus(FrameworkConstants.STATUS_INVALID);
			this.update(line);
		}
		return "ok";
	}
}

