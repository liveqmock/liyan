package com.innofi.component.report.excel.domain;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;


public class ExcelModelRowMapper implements RowMapper<ExcelModel>{

	private ExcelModelRowMapper(){
		
	}
	
	@Override
	public ExcelModel mapRow(ResultSet rs, int rowNum) throws SQLException {
		ExcelModel excelModel = new ExcelModel();
		excelModel.setId(rs.getString(1));
		excelModel.setName(rs.getString(2));
		excelModel.setSheet(rs.getString(3));
		excelModel.setTableName(rs.getString(4));

		excelModel.setTablePrimaryKey(rs.getString(5));
		excelModel.setPrimaryKeyType(rs.getString(6));
		excelModel.setSequenceName(rs.getString(7));
		excelModel.setDbType(rs.getString(8));
		excelModel.setStartRow(rs.getInt(9));
		excelModel.setEndRow(rs.getInt(10));
		excelModel.setStartColumn(rs.getInt(11));
		excelModel.setEndColumn(rs.getInt(12));

		excelModel.setProcessor(rs.getString(13));
		excelModel.setCmnt(rs.getString(14));
		excelModel.setHelpDoc(rs.getString(15));
		excelModel.setCrtDate(rs.getDate(16));
		excelModel.setUpdDate(rs.getDate(17));
		excelModel.setCrtUserCode(rs.getString(18));
		excelModel.setUpdUserCode(rs.getString(19));
		excelModel.setCrtOrgCode(rs.getString(20));
		excelModel.setUpdOrgCode(rs.getString(21));
		excelModel.setDatasourceName(rs.getString(22));
		return excelModel;
	}
	
	private static final ExcelModelRowMapper INSTANCE = new ExcelModelRowMapper();

	/**
	 * 取得实例.
	 * 
	 * @return 单例JobHistoryRowMapper.
	 */
	public static final ExcelModelRowMapper getInstance() {
		return INSTANCE;
	}
}
