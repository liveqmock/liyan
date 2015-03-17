
package com.innofi.component.rbac.privilage.service;

import java.util.List;

import com.innofi.framework.pojo.metadata.MdDimenField;
import com.innofi.component.rbac.privilage.pojo.DaDimenData;
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
public interface IDaDimenDataService  extends IBaseService<DaDimenData,String> {

	/**
	 * 查询策略字段
	 * @param dimenControlId
	 * @param dimenId
	 * @return
	 */
	List<MdDimenField> findDimenFieldForDimenControl(String dimenControlId,String dimenId);


	void deleteDimenData(String dimenControlId, String dimenFieldId);

	/**
	 * 查询维度信息
	 * @param dimenControlId
	 * @param dimenFieldId
	 * @return
	 */
	List<DaDimenData> findDimenDatasForDimenControl(String dimenControlId,String dimenFieldId);
	
	/**
	 * 查询维度数据
	 * @param dimenControlId
	 * @param dimenFieldId
	 * @return
	 */
	List<DaDimenData> findDimenDatasByDimenFieldId(String dimenFieldId);

	/**
	 * 保存数据 根据类型做不同转换
	 * @param dimenData
	 */
	void saveDaDimenData(DaDimenData dimenData);
	
	/**
	 * findMdDimensionByDimenControlId(这里用一句话描述这个方法的作用)    
	 * @param   name    
	 * @param  @return    设定文件    
	 * @return String    DOM对象    
	 * @Exception 异常对象    
	 * @since  CodingExample　Ver(编码范例查看) 1.1
	 */
	public List<DaDimenData> findMdDimensionByDimenControlId(String DimenControlId);

	public List<DaDimenData> findDimenDatasForRestrict(String DimenControlId);
	
	
	/**
	 * 根据策略ID取维度数据
	 * findMdDimensionByPolicyIds(这里用一句话描述这个方法的作用)    
	 * @param   name    
	 * @param  @return    设定文件    
	 * @return String    DOM对象    
	 * @Exception 异常对象    
	 * @since  CodingExample　Ver(编码范例查看) 1.1
	 */
	public List<DaDimenData> findMdDimensionByPolicyIds(List<String> policyIds,String FieldName,boolean treeFlag);
	
}