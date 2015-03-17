/**
 * 
 */
package com.innofi.component.report.d7;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.core.resource.ResourceManager;
import com.bstek.dorado.core.resource.ResourceManagerUtils;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.entity.FilterType;
import com.bstek.dorado.data.provider.Page;
import com.innofi.framework.datasource.DataSourceRepository;
import com.innofi.framework.exception.SysMessage;
import com.innofi.component.report.d7.domain.TableInfo;
import com.innofi.component.report.excel.ExcelParser;
import com.innofi.component.report.excel.domain.ExcelDataWrapper;
import com.innofi.component.report.excel.domain.ExcelModel;
import com.innofi.component.report.excel.domain.ExcelModelDetail;
import com.innofi.component.report.excel.domain.RowWrapper;
import com.innofi.component.report.excel.manager.ExcelModelManager;
import com.innofi.framework.spring.context.ContextHolder;
import com.innofi.component.uploader.pojo.SysUploadFile;
import com.innofi.component.uploader.service.ISysUploadFileService;

/**
 * excel表维护定义、解析入库
 * 
 * @author matt.yao@bstek.com
 * @since 1.0
 */
public class ExcelManagerPR implements InitializingBean {
	private ExcelModelManager excelModelManager;
	private ExcelParser excelParser;
	private DataSourceRepository dataSourceRepository;
	
	@Resource(name = "sysUploadFileService")
	private ISysUploadFileService sysUploadFileService;

	private static ResourceManager res;
	
	//页面预览记录条数,默认100条
	private String rowDataCount = "100";
	
	/**
	 * 分页查询excel表定义信息
	 * 
	 * @param page
	 * @param map
	 * @throws Exception
	 */
	@DataProvider
	public void loadExcelModels(Page<ExcelModel> page, Map<String, Object> map) throws Exception {
		Page<ExcelModel> p = excelModelManager.getPaginationExcelModels(map, page.getPageNo(), page.getPageSize());
		page.setEntities(p.getEntities());
		page.setEntityCount(p.getEntityCount());
	}

	/**
	 * 根据表定义信息id查询excel的单元格定义信息
	 * 
	 * @param excelModelId
	 * @return 返回ExcelModelDetail集合
	 * @throws Exception
	 */
	@DataProvider
	public Collection<ExcelModelDetail> loadExcelModelDetails(String excelModelId) throws Exception {
		if (StringUtils.hasText(excelModelId)) {
			Collection<ExcelModelDetail> ret =  excelModelManager.findExcelModelDetailByModelId(excelModelId);
			this.editInterceptorType(ret);
			return ret;
		}
		return null;
	}

	/**
	 * 处理页面显示虚拟属性值，拦截器的类型，1，不使用拦截器  2，使用用户自定义拦截器
	 * @param excelModelDetail
	 */
	private void editInterceptorType(Collection<ExcelModelDetail> excelModelDetail) {
		if(null !=excelModelDetail && excelModelDetail.size() >0){
			for(ExcelModelDetail excelModel : excelModelDetail) {
				if(null != excelModel.getInterceptor() && !"".equals(excelModel.getInterceptor())){
					excelModel.setInterceptorType("2");
				}else {
					excelModel.setInterceptorType("1");
				}
			}
		}
	}
	
	/**
	 * 完成excel表定义信息的增删改功能
	 * 
	 * @param coll
	 * @throws Exception
	 */
	@DataResolver
	public void saveExcelModel(Collection<ExcelModel> coll) throws Exception {
		for (@SuppressWarnings("unchecked")
		Iterator<ExcelModel> iter = EntityUtils.getIterator(coll, FilterType.DIRTY); iter.hasNext();) {
			ExcelModel excelModel = iter.next();
			EntityState state = EntityUtils.getState(excelModel);
			if (state.equals(EntityState.NEW)) {
				excelModelManager.insertExcelModel(excelModel);
			}
			if (state.equals(EntityState.MODIFIED)) {
				excelModelManager.updateExcelModel(excelModel);
			}
			if (state.equals(EntityState.DELETED)) {
				excelModelManager.deleteExcelModelDetailByModelId(excelModel.getId());
				excelModelManager.deleteExcelModelById(excelModel.getId());
				deleteFileByID(excelModel.getHelpDoc());
			}
		}
		for (ExcelModel e : coll) {
			if (e.getListExcelModelDetail() != null) {
				saveExcelModelDetail(e.getListExcelModelDetail());
			}
		}
	}

	@SuppressWarnings("unchecked")
	@DataResolver
	public void saveExcelModelDetail(Collection<ExcelModelDetail> coll) throws Exception {
		for (Iterator<ExcelModelDetail> it = EntityUtils.getIterator(coll, FilterType.DIRTY); it.hasNext();) {
			ExcelModelDetail entity = it.next();
			EntityState state = EntityUtils.getState(entity);
			if (state.equals(EntityState.NEW)) {
				excelModelManager.insertExcelModelDetail(entity);
			}
			if (state.equals(EntityState.MODIFIED)) {
				excelModelManager.updateExcelModelDetail(entity);
			}
			if (state.equals(EntityState.DELETED)) {
				excelModelManager.deleteExcelModelDetailById(entity.getId());
			}
		}

	}

	/**
	 * 加载解析的excel数据
	 * 
	 * @param excelModelId
	 * @param uploadFileId
	 * @return 返回ExcelDataWrapper对象
	 * @throws Exception
	 */
	@DataProvider
	public ExcelDataWrapper loadParsedExcelWrapper(String excelModelId, String uploadFileId) throws Exception {
		InputStream in = this.loadFileInputStream(uploadFileId);
		ExcelDataWrapper excelDataWrapper = excelParser.parser(excelModelId, in);
		ExcelDataWrapper excelDataWrapperTmp = (ExcelDataWrapper)BeanUtils.cloneBean(excelDataWrapper);
		if (null != excelDataWrapperTmp) {
			// 校验没通过，此文件存在问题 ,显示错误信息到页面
			if (!excelDataWrapperTmp.isValidate()) {
				Collection<RowWrapper> coll = excelDataWrapperTmp.getRowWrappers();
				Collection<RowWrapper> collNotValid = new ArrayList<RowWrapper>();
				for (RowWrapper rowWrapper : coll) {
					// 校验没通过的数据项
					if (!rowWrapper.isValid()) {
						collNotValid.add(rowWrapper);
					}
				}
				excelDataWrapperTmp.setRowWrappers(collNotValid);
			} else {
				// 所有数据都校验通过，页面显示前100条数据以供预览
				Collection<RowWrapper> rowsPreview = new ArrayList<RowWrapper>();
				Collection<RowWrapper> rows = excelDataWrapperTmp.getRowWrappers();
				if (null != rows && rows.size() > Integer.parseInt(rowDataCount)) {
					int i = 0;
					for (RowWrapper rowWrapper : rows) {
						if (i >= Integer.parseInt(rowDataCount)) {
							break;
						}
						rowsPreview.add(rowWrapper);
						i++;
					}
					excelDataWrapperTmp.setRowWrappers(rowsPreview);
				}
			}
		}
		return excelDataWrapperTmp;
	}
	
	/**
	 * 根据文件id获取文件流
	 * @param uploadFileId
	 * @return
	 * @throws SQLException
	 */
	private InputStream loadFileInputStream(String uploadFileId)
			throws SQLException {
		InputStream input = null;
		List<String> list = new ArrayList<String>();
		list.add(uploadFileId);
		List<SysUploadFile> sysupload = sysUploadFileService.findByIds(list);
		if (null != sysupload && sysupload.size() > 0) {
			SysUploadFile s = sysupload.get(0);
			Blob ss = s.getFileContentB();
			input = ss.getBinaryStream();
		}
		return input;
	}

	@Expose
	public String checkId(String id) throws Exception {
		ExcelModel model = excelModelManager.findExcelModelById(id);
		if (model != null) {
			SysMessage sysMessage = new SysMessage("excelModel.checkId");
			return sysMessage.getMsgInfo();
		} else {
			return null;
		}
	}

	/**
	 * 保存解析的excel数据
	 * 
	 * @throws Exception
	 */
	@Expose
	public void saveParserExcelData(Map<String, Object> parameters) throws Exception {
		if (parameters != null) {
			String excelModelId = (String) parameters.get("excelModelId");
			Integer startRow = (Integer) parameters.get("startRow");
			Integer endRow = (Integer) parameters.get("endRow");
			if (StringUtils.hasText(excelModelId) && startRow != null && endRow != null) {
				ExcelModel em = excelModelManager.findExcelModelById(excelModelId);
				em.setStartRow(startRow);
				em.setEndRow(endRow);
				excelModelManager.updateExcelModel(em);
			}

			String uploadFileId = (String) parameters.get("uploadFileId");
			if (StringUtils.hasText(uploadFileId)) {
				InputStream in = this.loadFileInputStream(uploadFileId);
				excelParser.parser(excelModelId, in);
			}
		}
		excelParser.saveParserExcelData();
	}

	@Expose
	public boolean storeExcelToCache(String excelModelId, String uploadFileId) throws Exception {
		InputStream in = this.loadFileInputStream(uploadFileId);
		ExcelDataWrapper excelDataWrapper = excelParser.parser(excelModelId, in);
		return excelDataWrapper.isValidate();
	}

	/**
	 * 查询系统的所有数据库表
	 * 
	 * @return 表名称集合
	 * @throws Exception
	 */
	@Expose
	@DataProvider
	public Collection<TableInfo> loadAllTables(Map<String, Object> map) throws Exception {
		Collection<TableInfo> tableInfos = new ArrayList<TableInfo>();
		if (map != null && map.get("dataSourceName") != null) {
			String dataSourceName = (String) map.get("dataSourceName");
			List<String> list = excelModelManager.findAllTables(dataSourceName == null ? null : dataSourceName);
			TableInfo table = null;
			for (String s : list) {
				table = new TableInfo();
				table.setTableName(s);
				tableInfos.add(table);
			}
		}
		return tableInfos;
	}

	/**
	 * 查询表的所有字段
	 * 
	 * @param map
	 *            表名称
	 * @return 表自动名称集合
	 * @throws Exception
	 */
	@Expose
	@DataProvider
	public Collection<TableInfo> loadTableColumnNames(Map<String, Object> map) throws Exception {
		Collection<TableInfo> tableInfos = new ArrayList<TableInfo>();
		if (map != null && map.get("dataSourceName") != null && map.get("tableName") != null) {
			String dataSourceName = (String) map.get("dataSourceName");
			String tableName = (String) map.get("tableName");
			List<String> list = excelModelManager.findTableColumnNames(dataSourceName == null ? null : dataSourceName, tableName);
			TableInfo table = null;
			for (String s : list) {
				table = new TableInfo();
				table.setTableColumn(s);
				tableInfos.add(table);
			}
		}
		return tableInfos;
	}

	/**
	 * 查询表的主键
	 * 
	 * @param map
	 * @return 返回TableInfo对象
	 * @throws Exception
	 */
	@Expose
	@DataProvider
	public Collection<TableInfo> loadTablePrimaryKeys(Map<String, Object> map) throws Exception {
		List<String> list = new ArrayList<String>();
		if (map != null) {
			String dataSourceName = (String) map.get("dataSourceName");
			String tableName = (String) map.get("tableName");
			list = excelModelManager.findTablePrimaryKeys(dataSourceName == null ? null : dataSourceName, tableName);
		}
		Collection<TableInfo> tableInfos = new ArrayList<TableInfo>();
		TableInfo table = null;
		for (String s : list) {
			table = new TableInfo();
			table.setTablePrimaryKey(s);
			tableInfos.add(table);
		}
		return tableInfos;
	}

	/**
	 * 查询主键类型
	 * 
	 * @return 返回TableInfo对象
	 * @throws Exception
	 */
	@DataProvider
	public Collection<TableInfo> loadPrimaryKeyTypes() throws Exception {
		Collection<String> list = excelParser.getPrimaryTypes();
		Collection<TableInfo> tableInfos = new ArrayList<TableInfo>();
		TableInfo table = null;
		for (String s : list) {
			table = new TableInfo();
			table.setPrimaryKeyType(s);
			tableInfos.add(table);
		}
		return tableInfos;
	}

	/**
	 * 保存开始行和结束行信息
	 * 
	 * @param excelModelId
	 * @param startRow
	 * @param endRow
	 * @throws Exception
	 */
	@Expose
	@DataResolver
	public void saveExcelModelRow(String excelModelId, int startRow, int endRow) throws Exception {
		if (StringUtils.hasText(excelModelId)) {
			ExcelModel em = excelModelManager.findExcelModelById(excelModelId);
			em.setStartRow(startRow);
			em.setEndRow(endRow);
			excelModelManager.updateExcelModel(em);

		}
	}

	/**
	 * 解析上传excel文件的开始行和结束行
	 * 
	 * @param excelModelId
	 * @param uploadFileId
	 * @return 返回ExcelModel对象
	 * @throws Exception
	 */
	@Expose
	@DataProvider
	public ExcelModel loadExcelRow(String excelModelId, String uploadFileId) throws Exception {
		InputStream input = this.loadFileInputStream(uploadFileId);
		Map<String, Integer> map = excelParser.parseExcelRow(excelModelId, input);
		int startRow = (Integer) map.get("startRow");
		int endRow = (Integer) map.get("endRow");
		ExcelModel em = excelModelManager.findExcelModelById(excelModelId);
		em.setStartRow(startRow);
		em.setEndRow(endRow);
		return em;
	}

	/**
	 * 加载系统所有的数据源
	 * 
	 * @return 返回ExcelModel集合
	 */
	@DataProvider
	public Collection<ExcelModel> findDatasourceNames() {
		ExcelModel e;
		List<ExcelModel> list = new ArrayList<ExcelModel>();
		Map<String, DataSource> map = dataSourceRepository.getDataSources();
		Set<String> set = map.keySet();
		for (String s : set) {
			e = new ExcelModel();;
			e.setDatasourceName(s);
			list.add(e);
		}
		return list;
	}

	/**
	 * 删除帮助文档
	 * @param id
	 * @throws Exception
	 */
	@Expose
	public void deleteFileByID(String id) throws Exception {
		ISysUploadFileService sysUploadFileService = ContextHolder.getSpringBean("sysUploadFileService");
		sysUploadFileService.unBdUploadFile(id);
	}

	/**
	 * 添加帮助文档
	 * @param id
	 * @throws Exception
	 */
	@Expose
	public void addFileById(String id,String fileId) throws Exception {
		List<String> idsList = new ArrayList<String>();
		idsList.add(fileId);
		ISysUploadFileService sysUploadFileService = ContextHolder.getSpringBean("sysUploadFileService");
		sysUploadFileService.bdUploadFiles(fileId);
		
		//添加文档id关联到excel导入定义表
		excelModelManager.updateExcelModelHelpDoc(fileId,id);
	}
	
	@Expose
	public Collection<ExcelModelDetail> notNullFirst(Map<String, Object> map) throws Exception {
		return excelModelManager.notNullFirst(map);
	}

	@Expose
	public void createWorkSheet(Map<String, Object> map) throws Exception {
		excelModelManager.createWorkSheet(map);
	}

	/**
	 * 获取Excel上传模版的帮助文档名称
	 * 
	 * @param excelModelId
	 * @return 返回帮助文档名称，若没有返回null
	 * @throws Exception
	 */
	@Expose
	public String loadHelpDocName(String excelModelId) throws Exception {
		ExcelModel excelModel = this.excelModelManager.findExcelModelById(excelModelId);
		if (excelModel != null && StringUtils.hasText(excelModel.getHelpDoc())) {
			return excelModel.getHelpDoc();
		}
		return null;
	}

	@Expose
	public void changeCurrentDataSource(String dataSourceName) {
		ContextHolder.setCurrentDataSourceName(dataSourceName);
	}

	@Expose
	public String getDefaultDataSourceName() {
		return dataSourceRepository.getDefaultDataSourceName();
	}

	public ExcelModelManager getExcelModelManager() {
		return excelModelManager;
	}

	public void setExcelModelManager(ExcelModelManager excelModelManager) {
		this.excelModelManager = excelModelManager;
	}

	public ExcelParser getExcelParser() {
		return excelParser;
	}

	public void setExcelParser(ExcelParser excelParser) {
		this.excelParser = excelParser;
	}

	public DataSourceRepository getDataSourceRepository() {
		return dataSourceRepository;
	}

	public void setDataSourceRepository(DataSourceRepository dataSourceRepository) {
		this.dataSourceRepository = dataSourceRepository;
	}
	public void afterPropertiesSet() throws Exception {
		res = ResourceManagerUtils.get(ExcelManagerPR.class);

	}

	public ISysUploadFileService getSysUploadFileService() {
		return sysUploadFileService;
	}

	public void setSysUploadFileService(ISysUploadFileService sysUploadFileService) {
		this.sysUploadFileService = sysUploadFileService;
	}

	public static ResourceManager getRes() {
		return res;
	}

	public static void setRes(ResourceManager res) {
		ExcelManagerPR.res = res;
	}

	public String getRowDataCount() {
		return rowDataCount;
	}

	public void setRowDataCount(String rowDataCount) {
		this.rowDataCount = rowDataCount;
	}
}
