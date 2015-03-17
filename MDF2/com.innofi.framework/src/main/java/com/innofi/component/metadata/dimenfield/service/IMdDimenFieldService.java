/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.component.metadata.dimenfield.service;

import java.util.Map;

import com.innofi.framework.pojo.metadata.MdDimenField;
import com.innofi.framework.pojo.metadata.MdField;
import com.innofi.framework.service.IBaseService;

/**
 * 功能/ 模块：测试模块
 *
 * @author 张磊 Alex.zhang@innofi.com.cn
 * @version 2.0.0 12-11-30
 *          测试Service接口
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
public interface IMdDimenFieldService extends IBaseService<MdDimenField,String>{
	/**
	 * 通过维度id和字段的mapping更新维度字段的信息。
	 * @param dimenId
	 * @param mdFieldsMapping
	 */
	
	public void updateDimenFieldFieldId(String dimenId,Map<String, MdField> mdFieldsMapping);
}