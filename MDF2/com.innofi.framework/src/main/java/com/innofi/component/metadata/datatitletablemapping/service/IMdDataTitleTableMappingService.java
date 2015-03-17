/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.component.metadata.datatitletablemapping.service;

import com.innofi.framework.pojo.metadata.MdDataTitleTableMapping;
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
public interface IMdDataTitleTableMappingService extends IBaseService<MdDataTitleTableMapping,String>{
	/**
	 * 通过表名更新表ID信息
	 * @param tableId
	 * @param tableName
	 */
	public void updateMdDataTitleMappingTableId(String tableId,String tableName);
}