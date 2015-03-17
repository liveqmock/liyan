/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.component.metadata.mdmdepend.service.impl;

import com.innofi.framework.pojo.metadata.MdMdmDepend;
import com.innofi.component.metadata.mdmdepend.service.IMdMdmDependService;
import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.framework.service.impl.BaseServiceImpl;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 功能/ 模块：测试模块
 *
 * @author 张磊 Alex.zhang@innofi.com.cn
 * @version 2.0.0 12-11-30
 *          类描述
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
@Component(value="mdMdmDependService")
public class MdMdmDependServiceImpl extends BaseServiceImpl<MdMdmDepend,String> implements IMdMdmDependService{
	
	@Resource(name="mdDataTitleTableMappingDaoSupport")
	protected DaoSupport daoSupport;
	
    public DaoSupport getDaoSupport(){
		return daoSupport;
	}
    
    public List findMdMdmDependByMdmId(String mdmId){
    	return getPropertyValueList(findByProperty("mdmId", mdmId, QueryConstants.EQUAL), "dMdmId");
    }
}
