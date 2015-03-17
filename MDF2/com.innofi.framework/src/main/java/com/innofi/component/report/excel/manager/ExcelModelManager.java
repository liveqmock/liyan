/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.excel.manager;

import java.rmi.dgc.VMID;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.dao.pagination.Page;

import com.innofi.component.report.excel.domain.ExcelModel;
import com.innofi.component.report.excel.domain.ExcelModelDetail;
import com.innofi.component.report.excel.domain.ExcelModelRowMapper;
import com.innofi.component.report.excel.domain.ExportDataWrapper;
import com.innofi.component.report.excel.service.ExportExcelGridService;
import com.innofi.component.report.model.ReportTitleModel;
import com.innofi.framework.spring.context.ContextHolder;

/**
 * Excel导入数据库表维护
 * 
 * @author matt.yao@bstek.com
 * @since 1.0
 */
public class ExcelModelManager extends DaoSupport {

	/**
	 * 分页查询excel表定义
	 * 
	 * @param map
	 * @param pageIndex
	 * @param pageSize
	 * @return 返回存有ExcelModel的分页对象
	 * @throws Exception
	 */
	public Page<ExcelModel> getPaginationExcelModels(
			Map<String, Object> map, int pageIndex, int pageSize)
			throws Exception {
		StringBuffer sql = new StringBuffer(
				"select id_,name_,sheet_,table_name_,table_primary_key_,primary_key_type_,sequence_name_,db_type_,start_row_,end_row_,start_column_,end_column_,");
		sql.append(" processor_,cmnt_,help_doc_,create_date_,update_date_,create_user_,update_user_,create_org_,update_org_,datasource_name_ from sys_excel_models  where 1=1 ");
		String countSql = " select count(*)  from sys_excel_models where 1=1 ";
		StringBuffer sb = new StringBuffer("");
		List<Object> params = new ArrayList<Object>();
		if (map != null) {
			String name = (String) map.get("name");
			String tableName = (String) map.get("tableName");
			if (StringUtils.hasText(name)) {
				sb.append(" and name_ like ?");
				params.add("%" + name + "%");
			}
			if (StringUtils.hasText(tableName)) {
				sb.append(" and table_name_ like ?");
				params.add("%" + tableName + "%");
			}
		}
		String orderBy = " order by create_date_ desc  ";
		Page<ExcelModel> page = new Page<ExcelModel>(pageSize, pageIndex);
		this.getJdbcDao().paginationQuery(sql + sb.toString() + orderBy,
				countSql + sb.toString(), params.toArray(), page,
				ExcelModelRowMapper.getInstance());
		return page;
	}

	/**
	 * 插入excel表定义
	 * 
	 * @param excelModel
	 * @throws Exception
	 */
	public void insertExcelModel(ExcelModel excelModel) throws Exception {
		String sql = "insert into sys_excel_models (id_,name_,sheet_,table_name_,table_primary_key_,primary_key_type_,sequence_name_,db_type_,start_row_,end_row_,start_column_,end_column_,"
				+ " processor_,cmnt_,help_doc_,create_date_,create_user_,create_org_,datasource_name_) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		if (!StringUtils.hasText(excelModel.getId())) {
			String id = new VMID().toString();
			excelModel.setId(id);
		}
		excelModel.setCrtDate(new Date());
		excelModel.setCrtUserCode(ContextHolder.getContext().getLoginUsername());
		excelModel.setCrtOrgCode(ContextHolder.getContext().getLoginUser().getCrtOrgCode());
		if (excelModel.getDatasourceName() == null) {
			excelModel.setDatasourceName("");
		}
		Object[] args = new Object[] { excelModel.getId(),
				excelModel.getName(), excelModel.getSheet(),
				excelModel.getTableName(), excelModel.getTablePrimaryKey(),
				excelModel.getPrimaryKeyType(), excelModel.getSequenceName(),
				excelModel.getDbType(), excelModel.getStartRow(),
				excelModel.getEndRow(), excelModel.getStartColumn(),
				excelModel.getEndColumn(), excelModel.getProcessor(),
				excelModel.getCmnt(), excelModel.getHelpDoc(),
				excelModel.getCrtDate(), excelModel.getCrtUserCode(),
				excelModel.getCrtOrgCode(), excelModel.getDatasourceName() };
		this.getJdbcDao().update(sql, args);
	}

	/**
	 * 根据id删除表定义
	 * 
	 * @param excelModelId
	 * @throws Exception
	 */
	public void deleteExcelModelById(String excelModelId) throws Exception {
		String sql = "delete from sys_excel_models where id_=? ";
		this.getJdbcDao().update(sql, new Object[] { excelModelId });

	}

	/**
	 * 更新表定义
	 * 
	 * @param excelModel
	 * @throws Exception
	 */
	public void updateExcelModel(ExcelModel excelModel) throws Exception {
		String sql = "update sys_excel_models set name_=?,sheet_=?,table_name_=?,table_primary_key_=?,primary_key_type_=?,sequence_name_=?,db_type_=?,start_row_=?,end_row_=?,start_column_=?,end_column_=?,"
				+ " processor_=?,cmnt_=?,help_doc_=?,update_date_=?,update_user_=?,update_org_=?,datasource_name_=? where id_=?";
		excelModel.setUpdDate(new Date());
		excelModel.setUpdUserCode(ContextHolder.getContext().getLoginUsername());
		excelModel.setUpdOrgCode(ContextHolder.getContext().getLoginUser().getCrtOrgCode());
		if (excelModel.getDatasourceName() == null) {
			excelModel.setDatasourceName("");
		}
		Object[] args = new Object[] { excelModel.getName(),
				excelModel.getSheet(), excelModel.getTableName(),
				excelModel.getTablePrimaryKey(),
				excelModel.getPrimaryKeyType(), excelModel.getSequenceName(),
				excelModel.getDbType(), excelModel.getStartRow(),
				excelModel.getEndRow(), excelModel.getStartColumn(),
				excelModel.getEndColumn(), excelModel.getProcessor(),
				excelModel.getCmnt(), excelModel.getHelpDoc(),
				excelModel.getUpdDate(), excelModel.getUpdUserCode(),
				excelModel.getUpdOrgCode(),
				excelModel.getDatasourceName(), excelModel.getId() };
		this.getJdbcDao().update(sql, args);
	}
	
	/**
	 * 添加帮助文档文件Id
	 * 
	 * @param helpDocId
	 */
	public void updateExcelModelHelpDoc(String helpDocId, String id) {
		String sql = "update sys_excel_models set help_doc_=? where id_=?";
		this.getJdbcDao().update(sql, new Object[] { helpDocId, id });
	}

	/**
	 * 根据excel表模型id 查询excel表模型
	 * 
	 * @param excelModelId
	 *            excel表模型id
	 * @return excel表模型集合
	 * @throws Exception
	 */
	public ExcelModel findExcelModelById(String excelModelId)
			throws SQLException {
		Assert.notNull(excelModelId, "excelModelId 不能为空！");
		StringBuffer sb = new StringBuffer(
				"select id_,name_,sheet_,table_name_,table_primary_key_,primary_key_type_,sequence_name_,db_type_,start_row_,end_row_,start_column_,end_column_,");
		sb.append(" processor_,cmnt_,help_doc_,create_date_,update_date_,create_user_,update_user_,create_org_,update_org_,datasource_name_ from sys_excel_models  where id_=? ");
		List<ExcelModel> list = this.getJdbcDao().query(sb.toString(),
				new Object[] { excelModelId }, ExcelModelRowMapper.getInstance());
		return list.size() > 0 ? list.get(0) : null;
	}

	/**
	 * 根据excel表模型id查询 excel表模型详细信息
	 * 
	 * @param modelId
	 *            excel表模型id
	 * @return excel表模型详细信息集合
	 * @throws Exception
	 */
	public List<ExcelModelDetail> findExcelModelDetailByModelId(String modelId)
			throws Exception {
		Assert.notNull(modelId, "modelId 不能为空！");
		StringBuffer sb = new StringBuffer(
				"select id_,model_id_,excel_column_,table_column_,interceptor_ ");
		sb.append("from sys_excel_model_details  where model_id_=? order by excel_column_  ");
		List<ExcelModelDetail> list = this.getJdbcDao().query(
				sb.toString(), new Object[] { modelId },
				new ExcelModelDetailRowMapper());
		return list;
	}

	/**
	 * 插入列定义信息
	 * 
	 * @param excelModelDetail
	 * @throws Exception
	 */
	public void insertExcelModelDetail(ExcelModelDetail excelModelDetail)
			throws Exception {
		String sql = "insert into sys_excel_model_details (id_,model_id_,excel_column_,table_column_,interceptor_) values(?,?,?,?,?)";
		String id = new VMID().toString();
		excelModelDetail.setId(id);
		if (excelModelDetail.getTableColumn() == null) {
			excelModelDetail.setTableColumn("");
		}
		Object[] args = new Object[] { excelModelDetail.getId(),
				excelModelDetail.getExcelModel().getId(),
				excelModelDetail.getExcelColumn(),
				excelModelDetail.getTableColumn(),
				excelModelDetail.getInterceptor() };
		this.getJdbcDao().update(sql, args);
	}

	/**
	 * 根据id删除列定义信息
	 * 
	 * @param excelModelDetailId
	 * @throws Exception
	 */
	public void deleteExcelModelDetailById(String excelModelDetailId)
			throws Exception {
		String sql = "delete from sys_excel_model_details where id_=? ";
		this.getJdbcDao().update(sql, new Object[] { excelModelDetailId });

	}

	/**
	 * 根据表定义id删除列定义信息
	 * 
	 * @param excelModelId
	 * @throws Exception
	 */
	public void deleteExcelModelDetailByModelId(String excelModelId)
			throws Exception {
		String sql = "delete from sys_excel_model_details where model_id_=? ";
		this.getJdbcDao().update(sql, new Object[] { excelModelId });

	}

	/**
	 * 更新列定义信息
	 * 
	 * @param excelModelDetail
	 * @throws Exception
	 */
	public void updateExcelModelDetail(ExcelModelDetail excelModelDetail)
			throws Exception {
		String sql = "update sys_excel_model_details set model_id_=?,excel_column_=?,table_column_=?,interceptor_=? where id_=?";
		if (excelModelDetail.getTableColumn() == null) {
			excelModelDetail.setTableColumn("");
		}
		Object[] args = new Object[] {
				excelModelDetail.getExcelModel().getId(),
				excelModelDetail.getExcelColumn(),
				excelModelDetail.getTableColumn(),
				excelModelDetail.getInterceptor(), excelModelDetail.getId() };
		this.getJdbcDao().update(sql, args);
	}

	/**
	 * 根据excel表模型id和行号查询 excel表模型详细信息
	 * 
	 * @param modelId
	 *            excel表模型id
	 * @param columnIndex
	 *            行号
	 * @return excel表模型详细信息
	 * @throws Exception
	 */
	public ExcelModelDetail findExcelModelDetailByModelIdAndColumnIndex(
			String modelId, int columnIndex) throws Exception {
		Assert.notNull(modelId, "modelId 不能为空！");
		Assert.notNull(columnIndex, "columnIndex 不能为空！");
		StringBuffer sb = new StringBuffer(
				"select id_,model_id_,excel_column_,table_column_,interceptor_ ");
		sb.append("from sys_excel_model_details  where model_id_=? and excel_column_=?");
		List<ExcelModelDetail> list = this.getJdbcDao().query(
				sb.toString(), new Object[] { modelId, columnIndex },
				new ExcelModelDetailRowMapper());
		return list.size() > 0 ? list.get(0) : null;
	}

	/**
	 * 根据excel表模型id和定义主键查询 excel表模型详细信息
	 * 
	 * @param modelId
	 *            excel表模型id
	 * @param primaryKey
	 *            自定义主键
	 * @return excel表模型详细信息
	 * @throws Exception
	 */
	public ExcelModelDetail findExcelModelDetailByModelIdAndPrimaryKey(
			String modelId, String primaryKey) throws Exception {
		Assert.notNull(modelId, "modelId 不能为空！");
		Assert.notNull(primaryKey, "primaryKey 不能为空！");
		StringBuffer sb = new StringBuffer(
				"select id_,model_id_,excel_column_,table_column_,interceptor_ ");
		sb.append("from sys_excel_model_details  where model_id_=? and table_column_=? ");
		List<ExcelModelDetail> list = this.getJdbcDao().query(
				sb.toString(), new Object[] { modelId, primaryKey },
				new ExcelModelDetailRowMapper());
		return list.size() > 0 ? list.get(0) : null;
	}

	/**
	 * 查询数据库中所有的表
	 * 
	 * @return 返回表名的List
	 * @throws SQLException
	 * @throws Exception
	 */
	public List<String> findAllTables(String dataSourceName)
			throws SQLException {
		List<String> tablesList = new ArrayList<String>();
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = this.getJdbcDao().getDataSource().getConnection();
			DatabaseMetaData metaData = conn.getMetaData();
			String url = metaData.getURL();
			String schema = null;
			if (url.toLowerCase().contains("oracle")) {
				schema = metaData.getUserName();
			}
			rs = metaData
					.getTables(null, schema, "%", new String[] { "TABLE" });
			while (rs.next()) {
				tablesList.add(rs.getString("TABLE_NAME").toLowerCase());
			}
			return tablesList;
		} finally {
			if (conn != null) {
				conn.close();
			}
			if (rs != null) {
				rs.close();
			}
		}
	}

	/**
	 * 查询某张表的主键
	 * 
	 * @param tableName
	 *            表名称
	 * @return 主键集合对象
	 * @throws SQLException
	 */
	public List<String> findTablePrimaryKeys(String dataSourceName,
			String tableName) throws SQLException {
		List<String> primaryKeys = new ArrayList<String>();
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = this.getJdbcDao().getConnection();
			DatabaseMetaData metaData = conn.getMetaData();
			rs = metaData.getPrimaryKeys(null, null, tableName.toUpperCase());
			while (rs.next()) {
				primaryKeys.add(rs.getString("COLUMN_NAME").toLowerCase());
			}
			return primaryKeys;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (conn != null) {
				DataSourceUtils.releaseConnection(conn, this.getJdbcDao().getDataSource());
			}

		}
	}

	/**
	 * 查询某张表的所有列名
	 * 
	 * @param tableName
	 *            表名称
	 * @return	表的所有列名的List
	 */
	public List<String> findTableColumnNames(String dataSourceName,
			String tableName) throws SQLException {
		List<String> list = new ArrayList<String>();
		Set<String> set = new HashSet<String>();
		if (StringUtils.hasText(tableName)) {
			Connection conn = null;
			ResultSet rs = null;
			try {
				conn = this.getJdbcDao().getConnection();
				DatabaseMetaData metaData = conn.getMetaData();
				rs = metaData.getColumns(null, null, tableName.toUpperCase(),
						"%");
				while (rs.next()) {
					set.add(rs.getString("COLUMN_NAME").toLowerCase());
				}
				if (null != set && set.size() > 0) {
					for (String column : set) {
						list.add(column);
					}
				}
				return list;
			} finally {
				if (conn != null) {
					conn.close();
				}
				if (rs != null) {
					rs.close();
				}
			}
		}
		return list;
	}



 class ExcelModelDetailRowMapper implements
			RowMapper<ExcelModelDetail> {
		public ExcelModelDetail mapRow(ResultSet rs, int arg1)
				throws SQLException {
			ExcelModelDetail ed = new ExcelModelDetail();
			ed.setId(rs.getString(1));
			ed.setModelId(rs.getString(2));
			if (StringUtils.hasText(rs.getString(2))) {
				ed.setExcelModel(findExcelModelById(rs.getString(2)));
			}
			ed.setExcelColumn(rs.getInt(3));
			ed.setTableColumn(rs.getString(4));
			ed.setInterceptor(rs.getString(5));
			return ed;
		}

	}

	@SuppressWarnings("unchecked")
	public Collection<ExcelModelDetail> notNullFirst(Map<String, Object> map)
			throws Exception {

		String tableName = (String) map.get("tablename");
		String sql = "SELECT COLUMN_NAME,  COLUMN_COMMENT, IS_NULLABLE  FROM   INFORMATION_SCHEMA.COLUMNS where table_name = ?";
		List<Map<String, Object>> list = this.getJdbcDao().queryForList(
				sql, tableName);
		Map<String, String> columnComment = new HashMap<String, String>();
		Set<String> notNullSet = new HashSet<String>();
		for (Map<String, Object> item : list) {
			String key = toLowerCase((String) item.get("COLUMN_NAME"));
			String value = toLowerCase((String) item.get("COLUMN_COMMENT"));
			String isNullAble = (String) item.get("IS_NULLABLE");
			if (isNullAble != null && key != null
					&& isNullAble.equalsIgnoreCase("NO")) {
				notNullSet.add(key);
			}
			columnComment.put(key.toLowerCase(), value.toLowerCase());
		}

		Collection<ExcelModelDetail> details = (Collection<ExcelModelDetail>) map
				.get("excel");

		List<ExcelModelDetail> notNullDetails = new ArrayList<ExcelModelDetail>();

		for (Iterator<ExcelModelDetail> iterator = details.iterator(); iterator
				.hasNext();) {
			ExcelModelDetail excelModelDetail = iterator.next();
			if (notNullSet.contains(excelModelDetail.getTableColumn())) {
				notNullDetails.add(excelModelDetail);
				iterator.remove();
			}
		}
		return notNullDetails;
	}

	private String toLowerCase(String str) {
		return str == null ? null : str.toLowerCase();
	}

	public void createWorkSheet(Map<String, Object> map) throws Exception {

		if (map == null) {
			throw new RuntimeException("必须传入一个map");
		}

		// 提取列注释
		String tableName = (String) map.get("tablename");
		// String tableSchema = (String) map.get("tableSchema");
		String sql = "SELECT COLUMN_NAME,  COLUMN_COMMENT, IS_NULLABLE  FROM   INFORMATION_SCHEMA.COLUMNS where table_name = ?";
		List<Map<String, Object>> list = this.getJdbcDao().queryForList(
				sql, tableName);
		Map<String, String> columnComment = new HashMap<String, String>();
		Set<String> notNullSet = new HashSet<String>();
		for (Map<String, Object> item : list) {
			String key = toLowerCase((String) item.get("COLUMN_NAME"));
			String value = toLowerCase((String) item.get("COLUMN_COMMENT"));
			String isNullAble = (String) item.get("IS_NULLABLE");
			if (isNullAble != null && key != null
					&& isNullAble.equalsIgnoreCase("NO")) {
				notNullSet.add(key);
			}
			columnComment.put(key.toLowerCase(), value.toLowerCase());
		}

		createWorkSheet(columnComment, notNullSet, map);
	}

	private void createWorkSheet(Map<String, String> columnComment,
			Set<String> notNullSet, Map<String, Object> map) throws Exception {
		ExportExcelGridService service = ContextHolder
				.getSpringBean("bdf.exportExcelGridService");
		ExportDataWrapper dataWrapper = new ExportDataWrapper();

		List<String> titles = new ArrayList<String>();

		@SuppressWarnings("unchecked")
		Collection<ExcelModelDetail> details = (Collection<ExcelModelDetail>) map
				.get("excel");

		List<ExcelModelDetail> listDetails = new ArrayList<ExcelModelDetail>();
		listDetails.addAll(details);

		if (details != null) {
			for (ExcelModelDetail detail : listDetails) {
				String column = detail.getTableColumn().toLowerCase();
				String comment = columnComment.get(column);

				Assert.notNull(comment);

				String[] cmts = comment.split(":");

				String title = "";
				if (cmts != null && cmts.length > 0) {
					title = cmts[0];
				} else {
					title = column;
				}

				if (notNullSet.contains(column)) {
					title += " (必填)";
				}
				titles.add(title);
			}
		}

		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		Map<String, Object> data = new HashMap<String, Object>();
		datas.add(data);
		dataWrapper.setExcelData(datas);
		dataWrapper.setExcelTitle(titles);

		String tableName = (String) map.get("tablename");
		String fileName = tableName + ".xls";

		ReportTitleModel model = new ReportTitleModel();
		service.execute(model, dataWrapper, fileName, "Sheet1");

	}

}
