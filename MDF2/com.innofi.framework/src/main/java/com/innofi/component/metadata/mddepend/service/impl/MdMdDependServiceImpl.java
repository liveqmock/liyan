/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.component.metadata.mddepend.service.impl;

import java.util.List;

import com.innofi.framework.pojo.metadata.MdMdDepend;
import com.innofi.component.metadata.mddepend.service.IMdMdDependService;
import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.service.impl.BaseServiceImpl;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

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
@Component(value="mdMdDependService")
public class MdMdDependServiceImpl extends BaseServiceImpl<MdMdDepend,String> implements IMdMdDependService{
	
	@Resource(name="mdDataTitleTableMappingDaoSupport")
	protected DaoSupport daoSupport;
	
    public DaoSupport getDaoSupport(){
		return daoSupport;
	}
    /**
	 * 通过旧的元数据ID更新新的表ID
	 * @param newID
	 * @param oldId
	 */
    public void updateMdDependMdIdAndDmdId(String newID,String oldId){
    	String hqlMdId = "from MdMdDepend mmd where mmd.mdId = ?";
    	List<MdMdDepend> mdMdDependForMdIds = findByHql(hqlMdId,oldId);//作为主表查找逻辑外键
    	for (MdMdDepend mdMdDepend : mdMdDependForMdIds) {
    		mdMdDepend.setMdId(newID);
            update(mdMdDepend);
        }
    	
    	String hql = "from MdMdDepend mmd where mmd.dMdId = ?";
    	List<MdMdDepend> mdMdDependFordmdIds = findByHql(hql, oldId);//作为主表查找逻辑外键
    	for (MdMdDepend mdMdDepend : mdMdDependFordmdIds) {
    		mdMdDepend.setdMdId(newID);
            update(mdMdDepend);
        }
    }
}
