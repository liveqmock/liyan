package com.innofi.component.metadata.table.service.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.innofi.component.metadata.datatitletablemapping.service.IMdDataTitleTableMappingService;
import com.innofi.component.metadata.dimenfield.service.IMdDimenFieldService;
import com.innofi.component.metadata.dimension.service.IMdDimensionService;
import com.innofi.component.metadata.field.service.IMdFieldService;
import com.innofi.component.metadata.mddepend.service.IMdMdDependService;
import com.innofi.component.metadata.table.service.IMdTableService;
import com.innofi.component.metadata.tablerelate.service.IMdTableRelateService;
import com.innofi.framework.FrameworkConstants;
import com.innofi.component.codetransfer.IdfCodeTransfer;
import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.PropertyOrder;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.framework.pojo.BasePojo;
import com.innofi.framework.pojo.metadata.MdField;
import com.innofi.framework.pojo.metadata.MdTable;
import com.innofi.framework.pojo.metadata.MdTableRelate;
import com.innofi.framework.service.IBaseService;
import com.innofi.framework.service.impl.BaseServiceImpl;
import com.innofi.framework.spring.context.ContextHolder;
import com.innofi.framework.utils.reflect.ReflectionUtil;
import com.innofi.framework.utils.string.StringUtil;

@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
@Service(value = "mdTableService")
public class MdTableServiceImpl extends BaseServiceImpl<MdTable, String> implements IMdTableService {

    @Resource
    private IMdFieldService mdFieldServcie;
    @Resource
    private IMdTableRelateService mdTableRelateService;
    @Resource
    private IMdDimensionService mdDimensionService;
    @Resource
    private IMdDimenFieldService mdDimenFieldService;
    @Resource
    private IMdMdDependService mdMdDependService;
    @Resource
    private IMdDataTitleTableMappingService mdDataTitleTableMapping;

    @Resource(name="mdDataTitleTableMappingDaoSupport")
    protected DaoSupport daoSupport;

    public DaoSupport getDaoSupport(){
        return daoSupport;
    }

    /**
     * 覆盖父类方法，缓存状态为有效的MdTable对象
     *
     * @return 当前有效的MdTable列表
     */
    public List loadCacheObjects() {
        return findByProperty("status", FrameworkConstants.STATUS_EFFECTIVE, QueryConstants.EQUAL);
    }

    /**
     * 更新表结构创建新的版本
     *
     * @param newVerMdTable 更新的表对象
     */
    public void updateForNewVersion(MdTable newVerMdTable) {
        Map<String, MdField> mdFieldsMapping = new HashMap<String, MdField>();
        MdTable preVerMdTable = findLastVersionMdTableByTableName(newVerMdTable.getTableName());    //查找表当前有效版本
        preVerMdTable.setStatus(FrameworkConstants.STATUS_INVALID);
        super.update(preVerMdTable);
        newVerMdTable.setId(null);                                                                  //保存新版本表
        newVerMdTable.setVerNo(new BigDecimal(preVerMdTable.getVerNo().intValue() + 1));              //版本+1
        save(newVerMdTable);

        List<MdField> preVerMdFields = mdFieldServcie.findMdFieldsByTableId(preVerMdTable.getId());//拷贝字段信息
        for (MdField preVerFieldPojo : preVerMdFields) {
            MdField newVerField = new MdField();
            BeanUtils.copyProperties(preVerFieldPojo, newVerField);
            preVerFieldPojo.setStatus(FrameworkConstants.STATUS_INVALID);
            mdFieldServcie.update(preVerFieldPojo);
            newVerField.setId(null);
            newVerField.setTableId(newVerMdTable.getId());
            mdFieldServcie.save(newVerField);
            mdFieldsMapping.put(newVerField.getFieldName(), newVerField);
        }


        mdDimensionService.updateMdDimensionTableId(newVerMdTable.getId(), newVerMdTable.getTableName(), mdFieldsMapping);//更新维度中表的id
        mdMdDependService.updateMdDependMdIdAndDmdId(newVerMdTable.getId(), preVerMdTable.getId());  //通过旧ID更新新ID
        mdDataTitleTableMapping.updateMdDataTitleMappingTableId(newVerMdTable.getId(), newVerMdTable.getTableName()); //通过表明修改表ID


        List<MdTableRelate> tableRelates = mdTableRelateService.findLastVersionMdTableRelatePojosByTableName(newVerMdTable.getTableName());

        for (MdTableRelate tableRelate : tableRelates) {
            if (tableRelate.getMainTableName().equals(newVerMdTable.getTableName())) {
                tableRelate.setMainTableId(newVerMdTable.getId());
            } else {
                tableRelate.setForeignField(newVerMdTable.getId());
            }
            mdTableRelateService.update(tableRelate);
        }
    }


    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.metadata.table.service.IMdTableService#findLastVersionMdTableByTableName(String)
     */
    public MdTable findLastVersionMdTableByTableName(String tableName) {

        IdfCodeTransfer idfCodeTransfer = getIdfCodeTransfer();
        ListOrderedMap mdTables = idfCodeTransfer.getCacheObjects("mdTableService");
        ListOrderedMap effectiveTables = idfCodeTransfer.listDistinctFilter(mdTables, "tableName", tableName, null);
        
        MdTable tablePojo = null;

        if(effectiveTables.size()==0){
            String sql = "select * from md_table where status= ?  and table_name= ? ";
            tablePojo = queryBeanForUnique(sql, FrameworkConstants.STATUS_EFFECTIVE, tableName);
        }else{
        	tablePojo = (MdTable) effectiveTables.getValue(0); 
        }

        return tablePojo;
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.metadata.table.service.IMdTableService#findLastVersionTableNameByUiName(String)
     */
    public String findLastVersionTableNameByUiName(String uiName) {
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
        addPropertyFilter(filters, "uiName", uiName, QueryConstants.EQUAL, true);
        addPropertyFilter(filters, "status", FrameworkConstants.STATUS_EFFECTIVE, QueryConstants.EQUAL, true);
        List<MdTable> list = findByPropertyFilters(filters, orders);
        if (list == null || list.size() == 0) {
            return "";
        } else {
            return list.get(0).getTableName();
        }
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.metadata.table.service.IMdTableService#openPersistenceSwitch(String)
     */
    public boolean openPersistenceSwitch(String tableName) {
        MdTable mdTable = findLastVersionMdTableByTableName(tableName);
        if(null!=mdTable){
            mdTable.setIsCloseSaved(FrameworkConstants.COMM_N);
            getDaoSupport().getHibernateDao().update(mdTable);
            return true;
        }
        return false;
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.metadata.table.service.IMdTableService#closePersistenceSwitch(String)
     */
    public boolean closePersistenceSwitch(String tableName) {
        MdTable mdTable = findLastVersionMdTableByTableName(tableName);
        if(null!=mdTable){
            mdTable.setIsCloseSaved(FrameworkConstants.COMM_Y);
            getDaoSupport().getHibernateDao().update(mdTable);
            return true;
        }
        return false;
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.metadata.table.service.IMdTableService#findLastVersionMdTableByEntityName(String)
     */
    public MdTable findLastVersionMdTableByEntityName(String entityName) {
        IdfCodeTransfer idfCodeTransfer = getIdfCodeTransfer();
        ListOrderedMap cacheTables = idfCodeTransfer.getCacheObjects("mdTableService");
        cacheTables = idfCodeTransfer.listDistinctFilter(cacheTables, "entityName", entityName, null);
        MdTable tablePojo = null;
        if (cacheTables.size() > 0) {
            cacheTables = idfCodeTransfer.listDistinctFilter(cacheTables, "status", FrameworkConstants.STATUS_EFFECTIVE, null);
            if (cacheTables.size() > 0) {
                tablePojo = (MdTable) cacheTables.getValue(0);
            }
        }
        return tablePojo;
    }

    /**
     * 通过状态获取最新的数据
     *
     * @param status 对象状态
     * @return List<MdTable> 状态对应的结果集
     */
    public List<MdTable> findMdTableByStatus(String status) {
    	 IdfCodeTransfer idfCodeTransfer = getIdfCodeTransfer();
         ListOrderedMap cacheTables = idfCodeTransfer.getCacheObjects("mdTableService");
         
         List<MdTable> tables = cacheTables.valueList();
         if(tables==null||tables.size()==0){
        	 tables = findByProperty("status", FrameworkConstants.STATUS_EFFECTIVE, QueryConstants.EQUAL);
         }
         
        return tables; 
    }


    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.metadata.table.service.IMdTableService#refreshTableMetaData(String,List<MdTable>)
     */
    public char refreshTableMetaData(String tableName , List<MdTable> dbAllTables) {
        char result = '0';                                                                                              //刷新结果
        MdTable preVerMdTable = findLastVersionMdTableByTableName(tableName);                                           //表当前有效版本
        Map<String, MdTable> dbTablesMapping = new HashMap<String, MdTable>();
        if(preVerMdTable==null){
            if(dbAllTables==null||dbAllTables.size()==0){
                dbAllTables = dynamicQueryBeanForList("metadata-dynamic-statement", "findTableInfo");                   //获取数据库中最新的数据。目标数据
            }

            for (MdTable dbTable : dbAllTables) {
                dbTablesMapping.put(dbTable.getTableName(), dbTable);
            }
            
            MdTable mdTable = dbTablesMapping.get(tableName);                                                           //元数据表信息
            mdTable.setId(null);
            save(mdTable);

            List<MdField> dbMdFields = mdFieldServcie.findMdFieldsByTableNameFromDb(tableName);                         //数据库当前版本字段
            for (MdField dbField : dbMdFields) {
            	dbField.setId(null);
                dbField.setEntityAttriName(StringUtil.convertPropertyName(dbField.getFieldName()));                     //设置字段对应实体对象属性名
                dbField.setTableId(mdTable.getId());
                dbField.setTableName(mdTable.getTableName());
                mdFieldServcie.save(dbField);
            }
            result = '1';
        }


        /*List<MdTable> mdTables = findMdTableByStatus(FrameworkConstants.STATUS_EFFECTIVE);                               //所有有效表


        Map<String, MdTable> dbTablesMapping = new HashMap<String, MdTable>();                                            //保存数据库中最新数据表的Map结构减少后续遍历

        for (MdTable dbTable : dbAllTables) {
            dbTablesMapping.put(dbTable.getTableName(), dbTable);
        }

        Map<String, MdTable> mdTablesMapping = new HashMap<String, MdTable>();                                                         //保存当前版本数据表的Map结构减少后续遍历

        for (MdTable mdTable : mdTables) {
            mdTablesMapping.put(mdTable.getTableName(), mdTable);
        }


        if (preVerMdTable == null) {                                                                                     //没有历史版本
            generatorVersion(mdTablesMapping, preVerMdTable, dbTablesMapping, dbTablesMapping.get(tableName), new ArrayList<MdField>(), dbMdFields);
        } else {
            String preVerMdTableId = preVerMdTable.getId();
            List<MdField> preVerFields = mdFieldServcie.findMdFieldsByTableId(preVerMdTableId);                         //字段当前有效版本
            MdTable newVerMdTable = new MdTable();
            BeanUtils.copyProperties(preVerMdTable, newVerMdTable);                                                     //复制当前有效版本属性到新版本
            ReflectionUtil.copyProperties(dbTablesMapping.get(tableName), newVerMdTable);                               //复制数据信息到新版本
            boolean tableEq = preVerMdTable.equals(newVerMdTable);                                                      //表是否相同
            List<MdField> newVerFields = mdFieldServcie.refreshMdFieldMetaData(tableName, preVerFields, dbMdFields);    //刷新字段列表
            if (tableEq && newVerFields.size() == 0) {//表和字段信息都相同删除保存的表和字段信息
                result = '1';
                return result;
            } else {
    			if(!tableEq&&newVerFields.size()==0){//表有变化，字段无变化
                	for(MdField oldMdField : preVerFields){
                		MdField newField = new MdField();
                		BeanUtils.copyProperties(oldMdField, newField);
                		newField.setId(null);
                		newVerFields.add(newField);
                	}
                }
                generatorVersion(mdTablesMapping, preVerMdTable, dbTablesMapping, newVerMdTable, preVerFields, newVerFields);
            }
        }*/
        return result;
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.metadata.table.service.IMdTableService#findMdTableFromDb(String)
     */
    public MdTable findMdTableFromDb(String tableName) {
        String lowerName = tableName.toLowerCase();
        String upperName = tableName.toUpperCase();
        Map<String, String> params = new HashMap<String, String>();
        params.put("lowerName", lowerName);
        params.put("upperName", upperName);
        params.put("tableName", tableName);

        return dynamicQueryBeanForUnique("metadata-dynamic-statement", "findTableInfo", params);
    }

    /**
     * 生成新版MD数据表相关元数据
     *
     * @param mdTablesMapping      当前有效表的映射 key tableName ， value MdTable对象
     * @param preVerTable          当前有效版本表结构对象
     * @param dbTablesMapping      数据库中数据表的映射 key tableName ， value MdTable对象
     * @param newMdTable           新版本对象
     * @param preVerFields         当前有效字段列表
     * @param newVerMdFields       新版本字段列表
     * @param preVerMdTableRelates --todo
     * @param dbMdTableRelates
     */
    private void generatorVersion(Map<String, MdTable> mdTablesMapping, MdTable preVerTable, Map<String, MdTable> dbTablesMapping, MdTable newMdTable, List<MdField> preVerFields, List<MdField> newVerMdFields) {
        String tableName = newMdTable.getTableName();                                             //表名
        Map<String, MdField> mdFieldsMapping = new HashMap<String, MdField>();

        if (preVerTable != null) {                                                                //失效老版本表信息
            preVerTable.setStatus(FrameworkConstants.STATUS_INVALID);
            getDaoSupport().getJdbcDao().execute("update md_table set status = '0' where table_id='"+preVerTable.getId()+"'");

            StringBuffer fieldIds = new StringBuffer();
            
            for (MdField invalidField : preVerFields) {                                           //失效老版本字段信息
            	fieldIds.append("'"+invalidField.getId()+"',");
            }
            
            if(fieldIds.length()>0){
            	getDaoSupport().getJdbcDao().execute("update md_field set status = '0' where FIELD_ID in ("+fieldIds.substring(0,fieldIds.length()-1)+")");
            }
            newMdTable.setId(null);
            newMdTable.setVerNo(new BigDecimal(preVerTable.getVerNo().intValue() + 1));           //版本+1
        }

        if (mdTablesMapping.get(newMdTable.getTableName()) == null || mdTablesMapping.get(newMdTable.getTableName()).getVerNo().intValue() != newMdTable.getVerNo().intValue()) {
            save(newMdTable);                                                                     //保存新版本数据表
            mdTablesMapping.put(newMdTable.getTableName(), newMdTable);                           //保存后将MdTable添加到映射中
        } else {
            newMdTable = mdTablesMapping.get(newMdTable.getTableName());
        }

        String tableId = newMdTable.getId();

        for (MdField newVerMdField : newVerMdFields) {                                            //保存字段信息
            newVerMdField.setId(null);
            newVerMdField.setTableId(tableId);
            mdFieldServcie.save(newVerMdField);
            mdFieldsMapping.put(newVerMdField.getFieldName(), newVerMdField);
        }

        
        mdDimensionService.updateMdDimensionTableId(tableId, tableName, mdFieldsMapping);//更新维度中表的id
        if (preVerTable != null) {
            mdMdDependService.updateMdDependMdIdAndDmdId(tableId, preVerTable.getId());  //通过旧ID更新新ID
        }
        mdDataTitleTableMapping.updateMdDataTitleMappingTableId(tableId, tableName);     //通过表名修改表ID
    }

    /**
     * @param @param  tableName
     * @param @return id
     * @return String  返回类型
     * @throws
     * @Title: getIdByTableCode
     * @Description: 通过表名获取表id
     */
    private String getIdByTableCode(String tableName) {
        //String sql = "SELECT T.TABLE_ID TABLE_ID FROM MD_TABLE T WHERE T.VER_NO = ( SELECT MAX(VER_NO) FROM MD_TABLE WHERE TABLE_NAME = '"+tableName+"') AND T.TABLE_NAME = '"+tableName+"'";
        String sql = "SELECT T.TABLE_ID TABLE_ID FROM MD_TABLE T WHERE T.STATUS = '" + FrameworkConstants.COMM_Y + "' AND TABLE_NAME = '" + tableName + "'";
        List<Map<String, Object>> list = getDaoSupport().getJdbcDao().queryForList(sql);
        if (list.size() == 0)
            return "";
        return (String) list.get(0).get("TABLE_ID");
    }

    /**
     * @param @param  tableName
     * @param @param  columnName
     * @param @return 设定文件
     * @return String    返回类型
     * @throws
     * @Title: getEntityName
     * @Description: 将表名MD_TABLE 转换实体对象名称 MdTable
     */
    private String getEntityName(String tableName, String columnName) {
        String entityName = "";
        //String fieldCnName = "";
        //tableName = tableName.toLowerCase();
        columnName = columnName.toLowerCase();
        //String[] stringArray1 = tableName.split("_");
        String[] stringArray2 = columnName.split("_");
        for (int i = 0; i < stringArray2.length; i++) {
            if (i == 0) {
                entityName = stringArray2[0].toString();
            } else {
                entityName += stringArray2[i].substring(0, 1).toUpperCase() + stringArray2[i].substring(1, stringArray2[i].length());
            }
        }
        //for(int i = 0;i<stringArray2.length;i++){
        //	entityName += stringArray2[i].substring(0,1).toUpperCase()+stringArray2[i].substring(1,stringArray2[i].length());
        //}
        //return entityName+"."+fieldCnName;
        return entityName;
    }


    public boolean saveExcelData(String filepath) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {

		InputStream is = new FileInputStream("F:/SYS_ORG_INFO.xls");
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		HSSFSheet sheet = hssfWorkbook.getSheetAt(0);
		String tableName = sheet.getSheetName().substring(sheet.getSheetName().indexOf("(") + 1,sheet.getSheetName().lastIndexOf(")"));
		List<MdTable> mdTablesList = this.findByProperty("tableName",tableName, QueryConstants.EQUAL);
		Class clazzInterface = Class.forName(mdTablesList.get(0).getServiceName());

		IBaseService baseService = (IBaseService) ContextHolder.getSpringBeanFactory().getBean(tnToEn(tableName).get(0) + "Service");
		String clazzName = mdTablesList.get(0).getEntityName();

		HSSFRow titleRow = sheet.getRow(0);
		Map mapKey = new HashMap();
		for (Iterator<Cell> cellIter = (Iterator<Cell>) titleRow.cellIterator(); cellIter.hasNext();) {
			Cell cell = cellIter.next();

			String field = cell.getStringCellValue().substring(cell.getStringCellValue().indexOf("(") + 1,cell.getStringCellValue().lastIndexOf(")"));
			mapKey.put(cell.getColumnIndex(), this.convertFieldName(field));
		}
		
		ArrayList basePojoList = new ArrayList();
		// 循环行Row
		for (int rowIndex = 1; rowIndex < sheet.getLastRowNum() + 1; rowIndex++) {
			Row row = sheet.getRow(rowIndex);
			Map map = new HashMap();
			
			// 循环列Cell
			for (Iterator<Cell> cellIter = (Iterator<Cell>) row.cellIterator(); cellIter.hasNext();) {
				Cell cell = cellIter.next();

				String content = cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC ? cell.getNumericCellValue() + "": cell.getStringCellValue();
				map.put(mapKey.get(cell.getColumnIndex()), content);
			}
			
			BasePojo basePojo = (BasePojo) Class.forName(clazzName).newInstance();
			ReflectionUtil.copyMapToObject(map, basePojo);
			basePojoList.add(basePojo);
		}
		
		if (basePojoList != null && basePojoList.size() > 0) {
			baseService.saveForBranch(basePojoList);
		}

        return true;
    }

    /**
     * ************************************************************************
     * 将s_name 变为 sName;
     *
     * @param sName
     * @return
     */
    public static String convertFieldName(String sName) {

        String[] str = sName.toLowerCase().split("_");

        StringBuffer beanS = new StringBuffer(str[0]);

        for (int i = 1; i < str.length; i++) {
            if ("".equals(str) || str == null) {
                continue;
            }
            String info = str[i];
            beanS.append(info.substring(0, 1).toUpperCase()
                    + info.substring(1, info.length()));
        }
        return beanS.toString();
    }

    @Override
    public String getCnFieldName() {
        return "tableCnName";
    }

    /**
     * 把表名变成类名和对象名
     *
     * @param tableName
     * @return
     */
    public List<String> tnToEn(String tableName) {
        //定义实体对象名和类名
        String entityName = "";
        String className = "";
        tableName = tableName.toLowerCase();
        String[] stringArray = tableName.split("_");
        for (int i = 0; i < stringArray.length; i++) {
            entityName += stringArray[i].substring(0, 1).toUpperCase() + stringArray[i].substring(1, stringArray[i].length());
        }
        for (int i = 0; i < stringArray.length; i++) {
            if (i == 0) {
                className = stringArray[i].toString();
            } else {
                className += stringArray[i].substring(0, 1).toUpperCase() + stringArray[i].substring(1, stringArray[i].length());
            }
        }
        List<String> list = new ArrayList();
        list.add(className);
        list.add(entityName);
        return list;
    }
}