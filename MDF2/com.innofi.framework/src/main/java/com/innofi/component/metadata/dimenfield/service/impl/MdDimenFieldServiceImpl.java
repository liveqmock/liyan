/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.component.metadata.dimenfield.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.innofi.component.metadata.dimenfield.service.IMdDimenFieldService;
import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.pojo.metadata.MdDimenField;
import com.innofi.framework.pojo.metadata.MdField;
import com.innofi.framework.pojo.metadata.MdTable;
import com.innofi.framework.service.impl.BaseServiceImpl;

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
@Component(value="mdDimenFieldService")
public class MdDimenFieldServiceImpl extends BaseServiceImpl<MdDimenField,String> implements IMdDimenFieldService{
	
	@Resource(name="mdDataTitleTableMappingDaoSupport")
	protected DaoSupport daoSupport;
	
    public DaoSupport getDaoSupport(){
		return daoSupport;
	}
    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.metadata.dimenfield.service.IMdDimenFieldService#updateDimenFieldFieldId(String dimenId,Map<String, MdTable> mdFieldsMapping)
     */
    public void updateDimenFieldFieldId(String dimenId,Map<String, MdField> mdFieldsMapping){
    	String hql = "from MdDimenField mdf where mdf.dimenId = ?";
    	List<MdDimenField> mdDimenFields = findByHql(hql, dimenId);//作为主表查找逻辑外键
    	for(MdDimenField mdDimenField:mdDimenFields){
            if(mdFieldsMapping.size()>0){
                mdDimenField.setFieldId(mdFieldsMapping.get(mdDimenField.getFieldName()).getId());//获取最新的字段id更新维度表中的数据
            }
            update(mdDimenField);
    	}
    }
}









































































































