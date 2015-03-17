/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.component.metadata.dimension.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.innofi.framework.pojo.metadata.MdDimenField;
import com.innofi.framework.pojo.metadata.MdDimension;
import com.innofi.framework.pojo.metadata.MdField;
import com.innofi.component.metadata.dimenfield.service.IMdDimenFieldService;
import com.innofi.component.metadata.dimension.service.IMdDimensionService;
import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.PropertyOrder;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.framework.service.impl.BaseServiceImpl;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

/**
 * 功能/ 模块：测试模块
 * 
 * @author 张磊 Alex.zhang@innofi.com.cn
 * @version 2.0.0 12-11-30 类描述 修订历史： 日期 作者 参考 描述 北京名晟信息技术有限公司版权所有.
 */
@Component(value = "mdDimensionService")
public class MdDimensionServiceImpl extends
		BaseServiceImpl<MdDimension, String> implements IMdDimensionService {

	@Resource(name="mdDataTitleTableMappingDaoSupport")
	protected DaoSupport daoSupport;
	@Resource
	private IMdDimenFieldService mdDimenFieldService;

	public DaoSupport getDaoSupport(){
		return daoSupport;
	}

	/**
	 * 表权限控制策略ID查询维度信息。
	 * 
	 * findMdDimensionByPolicyId(这里用一句话描述这个方法的作用)
	 */
	public List<MdDimension> findMdDimensionByPolicyId(String DaTablePolicyId) {
		List args = new ArrayList();
		args.add(DaTablePolicyId);
		String sql = "select t3.* from DA_TABLE_POLICY t1, DA_DIMEN_CONTROL t2,MD_DIMENSION t3,MD_TABLE t4 where t1.table_auth_id = t2.table_auth_id and t2.dimen_id = t3.dimen_id and t3.status = '1' and t1.table_name = t4.table_name and t4.is_da_control = '1' and  t1.table_auth_id = ?";
		List<MdDimension> list = getDaoSupport().queryBeanForList(
				MdDimension.class, sql, args);
		return list;

	}

	@Override
	public String getCnFieldName() {
		return "tableCnName";
	}

	/**
	 * 通过表ID和维度类型获取维度ID的拼串
	 * 
	 * @param tableId
	 * @param dimenType
	 * @return
	 */
	public String findIdByTableIdAndDimenType(String tableId, String dimenType) {
		String dimenIdsString = "";
		String sql = "SELECT * FROM MD_DIMENSION MD WHERE MD.TABLE_ID = ? AND MD.DIMEN_TYPE = ?";
		List<MdDimension> list = getDaoSupport().queryBeanForList(
				MdDimension.class, sql, tableId, dimenType);
		if (list.size() > 0) {
			for (MdDimension mdDimension : list) {
				dimenIdsString += mdDimension.getId() + ",";
			}
			dimenIdsString = dimenIdsString.substring(0,
					dimenIdsString.length() - 1);
			return dimenIdsString;
		} else {
			return dimenIdsString;
		}
	}

	/**
	 * 通过表ID和维度类型获取维度ID的拼串
	 * 
	 * @param tableId
	 * @param dimenType
	 * @return
	 */
	public String findIdByTableIdAndDimenType(String tableId) {
		String dimenIdsString = "";
		String sql = "SELECT * FROM MD_DIMENSION MD WHERE MD.TABLE_ID = ?";
		List<MdDimension> list = getDaoSupport().queryBeanForList(
				MdDimension.class, sql, tableId);
		if (list.size() > 0) {
			for (MdDimension mdDimension : list) {
				dimenIdsString += mdDimension.getId() + ",";
			}
			dimenIdsString = dimenIdsString.substring(0,
					dimenIdsString.length() - 1);
			return dimenIdsString;
		} else {
			return dimenIdsString;
		}
	}

	/**
	 * 通过维度ID或维度ID字符串，获取维度字段的id字符串
	 * 
	 * @param dimenIds
	 * @return
	 */
	public String findDimenFieldIdsByDimenId(String dimenIds) {
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		List<PropertyOrder> orders = new ArrayList<PropertyOrder>();

		String mdFieldIdsString = "";
		List<String> dimenIdList = java.util.Arrays.asList(dimenIds.split(","));
		addPropertyFilter(filters, "dimenId", dimenIdList, QueryConstants.IN,
				true);
		List<MdDimenField> dimenFieldList = mdDimenFieldService
				.findByPropertyFilters(filters, orders);
		if (dimenFieldList.size() > 0) {
			for (MdDimenField mdDimenField : dimenFieldList) {
				mdFieldIdsString += mdDimenField.getFieldId() + ",";
			}
			mdFieldIdsString = mdFieldIdsString.substring(0,
					mdFieldIdsString.length() - 1);
			return mdFieldIdsString;
		} else {
			return mdFieldIdsString;
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.innofi.component.metadata.dimension.service.IMdDimensionService#
	 *      updateMdDimensionTableId(String tableId,String tableName,Map<String,
	 *      MdTable> mdFieldsMapping)
	 */
	public void updateMdDimensionTableId(String tableId, String tableName,
			Map<String, MdField> mdFieldsMapping) {
		String hql = "from MdDimension mds where mds.tableName = ?";
		List<MdDimension> mdDimensions = findByHql(hql, tableName);// 作为主表查找逻辑外键
		for (MdDimension mdDimension : mdDimensions) {
			mdDimension.setTableId(tableId);
			update(mdDimension);
			mdDimenFieldService.updateDimenFieldFieldId(mdDimension.getId(),
					mdFieldsMapping);
		}
	}
}
