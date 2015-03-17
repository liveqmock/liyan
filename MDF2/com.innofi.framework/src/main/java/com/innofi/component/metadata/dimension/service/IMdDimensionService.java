/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.component.metadata.dimension.service;

import java.util.List;
import java.util.Map;

import com.innofi.framework.pojo.metadata.MdDimension;
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
public interface IMdDimensionService extends IBaseService<MdDimension,String>{
	public List<MdDimension> findMdDimensionByPolicyId(String DaTablePolicyId);
	/**
	 * 通过表ID和维度类型获取维度ID的拼串
	 * @param tableId
	 * @param dimenType
	 * @return
	 */
	public String findIdByTableIdAndDimenType(String tableId,String dimenType);
	/**
	 * 通过表ID获取维度ID的拼串
	 * @param tableId
	 * @return
	 */
	public String findIdByTableIdAndDimenType(String tableId);
	/**
	 * 通过维度ID或维度ID字符串，获取维度字段的id字符串
	 * @param dimenIds
	 * @return
	 */
	public String findDimenFieldIdsByDimenId(String dimenIds);
	/**
	 * 通过表明更新表ID信息
	 * @param tableId
	 * @param tableName
	 * @param mdFieldsMapping
	 */
	
	public void updateMdDimensionTableId(String dimenId,String tableName,Map<String, MdField> mdFieldsMapping);
}