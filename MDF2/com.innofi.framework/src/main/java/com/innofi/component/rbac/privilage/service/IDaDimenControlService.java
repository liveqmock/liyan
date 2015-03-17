
package com.innofi.component.rbac.privilage.service;

import java.util.List;

import com.innofi.framework.pojo.metadata.MdDimension;
import com.innofi.component.rbac.privilage.pojo.DaDimenControl;
import com.innofi.framework.service.IBaseService;

/**
 * 功能/ 模块：todo 模块中文名称
 *
 * @author  todo 开发人员姓名 邮箱
 * @version todo 当前版本 日期
 *          todo 类描述
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
public interface IDaDimenControlService  extends IBaseService<DaDimenControl,String> {

	/**
	 * 根据策略ID，表ID查询控制维度
	 * @param tableAuthId
	 * @param tableId 
	 * @param searchFlag 
	 * @return
	 */
	public List<MdDimension> findMdDimensionForControls(String tableAuthId, String tableId, String searchFlag);
	/**
	 * 根据策略ID，表ID以及子表查询控制维度
	 * @param tableAuthId
	 * @param tableId 
	 * @param searchFlag 
	 * @return
	 */
	public List<MdDimension> findMdDimensionForTableAndRelate(String tableAuthId,String mainTableId,String searchFlag);

	void deleteControlData(String tableAuthId, String dimenId);
	
}