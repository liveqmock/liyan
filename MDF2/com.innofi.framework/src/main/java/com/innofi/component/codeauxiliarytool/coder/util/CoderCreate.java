package com.innofi.component.codeauxiliarytool.coder.util;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.Expose;
import com.innofi.component.codeauxiliarytool.coder.domain.ColumnInfo;
import com.innofi.component.codeauxiliarytool.coder.domain.TableInfo;
import com.innofi.component.metadata.commons.MetadataConstants;
import com.innofi.component.rbac.dict.pojo.SysDict;
import com.innofi.component.codetransfer.IdfCodeTransfer;
import com.innofi.framework.pojo.metadata.MdEntityObject;
import com.innofi.component.metadata.entityobject.service.IMdEntityObjectService;
import com.innofi.framework.pojo.metadata.MdField;
import com.innofi.component.metadata.field.service.IMdFieldService;
import com.innofi.framework.pojo.metadata.MdTable;
import com.innofi.component.metadata.table.service.IMdTableService;
import com.innofi.framework.FrameworkConstants;
import com.innofi.framework.action.impl.BaseActionImpl;
import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.framework.pojo.BasePojo;
import com.innofi.framework.service.IBaseService;
import com.innofi.framework.service.impl.BaseServiceImpl;
import com.innofi.framework.spring.context.ContextHolder;
import com.innofi.framework.utils.string.StringUtil;
import org.apache.commons.collections.map.ListOrderedMap;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

@Component
public class CoderCreate {

    @Resource
    private IMdFieldService mdFieldServcie;
    @Resource
    private IMdTableService mdTableServcie;
    @Resource
    private IMdEntityObjectService mdEntityObjectService;
    @Resource(name="mdDataTitleTableMappingDaoSupport")
    DaoSupport daoSupport;
    public DaoSupport getDaoSupport(){
    	return daoSupport;
    }
    
    /*
     * 加载表信息
     */
    @DataProvider
    public Collection<TableInfo> loadTableInfos(Map parameters)
            throws Exception {
        String queryTableName = null;
        if (parameters != null) {
            queryTableName = (String) parameters.get("tableName");
        }

        List<TableInfo> tableInfoList = new ArrayList<TableInfo>();

        List<MdTable> mdTables = mdTableServcie
                .findMdTableByStatus(FrameworkConstants.STATUS_EFFECTIVE);
        Iterator it = mdTables.iterator();
        while (it.hasNext()) {
            MdTable mdTable = (MdTable) it.next();
            String mdTableName = mdTable.getTableName();
            TableInfo tableInfo = new TableInfo();
            if (null != mdTable.getTableType()
                    && mdTable.getTableType().equals(
                    MetadataConstants.TABLE_TYPE_BUS)) {
                if (com.innofi.framework.utils.validate.Validator
                        .isEmpty(queryTableName)
                        || (com.innofi.framework.utils.validate.Validator
                        .isNotEmpty(queryTableName))
                        && mdTableName.toUpperCase().indexOf(
                        queryTableName.toUpperCase()) > -1) {
                    tableInfo.setTableName(mdTable.getTableName());
                    tableInfoList.add(tableInfo);
                }
            }
        }

        return tableInfoList;
    }

    /*
     * 加载列信息
     */
    @DataProvider
    public Collection<MdField> loadColumnInfos(String tableName)
            throws Exception {

        List<MdField> list = mdFieldServcie
                .findLastVersionMdFieldsByTableName(tableName);

        for (MdField field : list) {

            if (!StringUtil.hasText(field.getEleType())) {
                field.setEleType(FrameworkConstants.COMM_Y);
            }
            field.setIsQueryCond(FrameworkConstants.COMM_Y);
            field.setIsAddRead(FrameworkConstants.COMM_N);
            field.setIsAddView(FrameworkConstants.COMM_Y);
            field.setIsUpdRead(FrameworkConstants.COMM_N);
            field.setIsUpdView(FrameworkConstants.COMM_Y);
            field.setIsTableView(FrameworkConstants.COMM_Y);
            field.setLabelInf(field.getFieldDesc());
        }

        return list;

    }

    /*
     * 配置列信息
     */
    @Expose
    public void getColumnInfos(Map<String, Object> parameter) {
        Collection<MdField> entityList = (Collection<MdField>) parameter
                .get("ds");

        String packageName = (String) parameter.get("srcPath");
        String discPathName = (String) parameter.get("discPathName");

        String tableName = "";

        Model model = new Model();
        List<ColumnInfo> fields = new ArrayList<ColumnInfo>();
        for (MdField field : entityList) {

            ColumnInfo col = new ColumnInfo();
            tableName = field.getTableName();
            col.setTableName(tableName);
            col.setOldColumnName(field.getFieldName());
            if (field.getIsPk().equals(FrameworkConstants.COMM_Y)) {
                col.setColumnName("id");
            } else {
                col.setColumnName(StringUtil.convertPropertyName(field.getFieldName()));
            }
            String fieldName = col.getColumnName().substring(0, 1)
                    .toUpperCase()
                    + col.getColumnName().substring(1,
                    col.getColumnName().length());
            col.setFieldName(fieldName);
            col.setJavaType(CoderCreate.getPojoType(field.getFieldType()));
            col.setComponentType(field.getEleType());
            col.setColumnSize(String.valueOf(field.getFieldLen()));
            col.setPropertyLabel(field.getLabelInf());
            col.setPropertyBlankText(field.getBankTextInf());
            col.setPropertyTip(field.getTipInf());
            col.setDictType(field.getFieldDict());
            col.setIsnullAble(field.getIsNull());
            col.setIsprimaryKey(field.getIsPk());
            col.setValidators(field.getConsCond());
            col.setRegexp(field.getRegExp());
            col.setSearchFlag(field.getIsQueryCond());
            col.setSearchType(field.getQueryMethod() == null ? "eq" : field
                    .getQueryMethod());
            col.setListFlag(field.getIsTableView());
            col.setAddFlag(field.getIsAddView());
            col.setAddReadyFlag(field.getIsAddRead());
            col.setModifyFlag(field.getIsUpdView());
            col.setModifyReadyFlag(field.getIsUpdRead());
            col.setRemarks(field.getFieldDesc());
            col.setSelectType(field.getSelectType());
            col.setOrgCategory(field.getOrgCategory() == null ? "" : field
                    .getOrgCategory());
            col.setOrgExpandLevel(field.getExpandLevel() == null ? "" : field
                    .getExpandLevel());
            col.setRootCode(field.getRootCode() == null ? "" : field
                    .getRootCode());
            field.setEntityAttriName(col.getColumnName());
            fields.add(col);
            mdFieldServcie.update(field);
        }

        String entityName = CoderCreate.generateClassName(StringUtil
                .convertPropertyName(tableName));

        model.setFields(fields);
        model.setTableName(tableName);
        model.setBeanName(StringUtil.convertPropertyName(tableName));
        model.setModelName(entityName);
        model.setPackageName(packageName);
        model.setDiscPathName(discPathName);
        Map map = toMap(model);
        CodeGenerator.generate(map, model);

        // 创建对象元数据
        createMdEntityMetadata(packageName, tableName, model);

    }

    /**
     * 创建对象元数据
     *
     * @param packageName 包路径
     * @param tableName   表名
     * @param model       模型对象
     */
    public void createMdEntityMetadata(String packageName, String tableName,
                                       Model model) {

        // 删除对象元数据信息
        // daoSupport.getJdbcDao().execute("delete from md_entity_object where OBJ_PATH LIKE '%"+getEntityPath(packageName,
        // model.getModelName())+"%'");

        String objName = generateMdEntityName(model.getModelName());// 对md_entity_object中
        // OBJame 使用
        // 写
        String entityName = model.getModelName();// 拼写使用 实际类名

        getDaoSupport()
                .getJdbcDao()
                .execute(
                        "delete from md_entity_object where OBJ_NAME LIKE '%"
                                + objName + "%'");

        // 创建实体对象元数据信息
        MdEntityObject entityObjectPojo = new MdEntityObject();

        entityObjectPojo.setObjName(objName);

        entityObjectPojo.setObjPath(getEntityPath(packageName, entityName));
        entityObjectPojo.setObjType(MetadataConstants.OBJECT_TYPE_ENTITY);
        String basePojoId = mdEntityObjectService.findUniqueByProperty(
                "objPath", BasePojo.class.getName(), QueryConstants.EQUAL)
                .getId();
        entityObjectPojo.setInterFace(basePojoId);
        mdEntityObjectService.save(entityObjectPojo);

        // 创建DaoSuport元数据信息
        entityObjectPojo = new MdEntityObject();
        entityObjectPojo.setObjName(objName + "DaoSupport");
        entityObjectPojo.setObjPath(getDaoPath(packageName, entityName));
        entityObjectPojo.setObjType(MetadataConstants.OBJECT_TYPE_DAO);
        String baseDaoId = mdEntityObjectService.findUniqueByProperty(
                "objPath", DaoSupport.class.getName(),
                QueryConstants.EQUAL).getId();
        entityObjectPojo.setInterFace(baseDaoId);
        mdEntityObjectService.save(entityObjectPojo);

        // 创建元数据Service接口对象信息元数据信息
        entityObjectPojo = new MdEntityObject();
        entityObjectPojo.setObjName(objName + "Service");
        entityObjectPojo.setObjPath(getServicePath(packageName, entityName));
        entityObjectPojo.setObjType(MetadataConstants.OBJECT_TYPE_INTERFACE);
        String baseServiceId = mdEntityObjectService.findUniqueByProperty(
                "objPath", IBaseService.class.getName(), QueryConstants.EQUAL)
                .getId();
        entityObjectPojo.setInterFace(baseServiceId);
        mdEntityObjectService.save(entityObjectPojo);

        // 创建Service实现对象信息元数据信息
        entityObjectPojo = new MdEntityObject();
        entityObjectPojo.setObjName(objName + "ServiceImpl");
        entityObjectPojo
                .setObjPath(getServiceImplPath(packageName, entityName));
        entityObjectPojo.setObjType(MetadataConstants.OBJECT_TYPE_SERVICE);
        String baseServiceImplId = mdEntityObjectService.findUniqueByProperty(
                "objPath", BaseServiceImpl.class.getName(),
                QueryConstants.EQUAL).getId();
        entityObjectPojo.setInterFace(baseServiceImplId);
        mdEntityObjectService.save(entityObjectPojo);

        // 创建元数据Action对象信息元数据信息
        entityObjectPojo = new MdEntityObject();
        entityObjectPojo.setObjName(objName + "Action");
        entityObjectPojo.setObjPath(getActionPath(packageName, entityName));
        entityObjectPojo.setObjType(MetadataConstants.OBJECT_TYPE_ACTION);
        String baseActionId = mdEntityObjectService
                .findUniqueByProperty("objPath",
                        BaseActionImpl.class.getName(), QueryConstants.EQUAL)
                .getId();
        entityObjectPojo.setInterFace(baseActionId);
        mdEntityObjectService.save(entityObjectPojo);

        // 创建UI对象信息元数据信息
        entityObjectPojo = new MdEntityObject();
        entityObjectPojo.setObjName(objName + "Manage");
        entityObjectPojo.setObjPath(getViewPath(packageName, entityName));
        entityObjectPojo.setObjType(MetadataConstants.OBJECT_TYPE_UI);
        mdEntityObjectService.save(entityObjectPojo);

        // 更新元数据表信息
        MdTable tablePojo = mdTableServcie
                .findLastVersionMdTableByTableName(tableName);
        tablePojo.setTableName(tableName);
        tablePojo.setEntityName(getEntityPath(packageName, entityName));
        tablePojo.setDaoName(getDaoPath(packageName, entityName));
        tablePojo.setServiceName(getServiceImplPath(packageName, entityName));
        tablePojo.setActionName(getActionPath(packageName, entityName));
        tablePojo.setUiName(getViewPath(packageName, entityName));
        mdTableServcie.update(tablePojo);
    }

    /*
     * 获取视图名包含类路径
     */
    private String getViewPath(String packageName, String modelName) {
        return packageName + ".view." + modelName + "Manage.d";
    }

    /*
     * 获取Action类名包含类路径
     */
    private String getActionPath(String packageName, String modelName) {
        return packageName + ".action." + modelName + "Action";
    }

    /*
     * 获取业务服务实现类名包含类路径
     */
    private String getServiceImplPath(String packageName, String modelName) {
        return packageName + ".service.impl." + modelName + "ServiceImpl";
    }

    /*
     * 获取业务服务类名包含类路径
     */
    private String getServicePath(String packageName, String modelName) {
        return packageName + ".service.I" + modelName + "Service";
    }

    /*
     * 获取DAO对象类名包含类路径
     */
    private String getDaoPath(String packageName, String modelName) {
        return packageName + ".dao." + modelName + "DaoSupport";
    }

    /*
     * 获取实体对象类名包含类路径
     */
    private String getEntityPath(String packageName, String modelName) {
        return packageName + ".pojo." + modelName;
    }

    /*
     * 获取字典表自动映射为autoMappingDropDown
     */
    @DataProvider
    public List<Map<String, Object>> getDict(String dictType) throws Exception {
        String[] values = StringUtil.split(dictType, "|");

        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

        List<String> ignoreValues = new ArrayList<String>();

        if (null != values && values.length > 1) {
            dictType = values[0];
            for (int i = 1; i < values.length; i++) {
                ignoreValues.add(values[i]);
            }
        }

        IdfCodeTransfer idfCodeTransfer = ContextHolder.getSpringBean("mdf.codeTransfer");
        ListOrderedMap sysDictOrderedMap = idfCodeTransfer.getCacheObjects("sysDictService");

        sysDictOrderedMap = IdfCodeTransfer.listDistinctFilter(sysDictOrderedMap, "dictType", dictType, null);
        sysDictOrderedMap = IdfCodeTransfer.listDistinctFilter(sysDictOrderedMap, "parentDictId", "0", null, QueryConstants.NOT_EQUAL);
        sysDictOrderedMap = IdfCodeTransfer.listDistinctFilter(sysDictOrderedMap, "status", FrameworkConstants.STATUS_EFFECTIVE, null);

        if (dictType.indexOf("$") > -1) {
            String level = StringUtil.split(dictType, "$")[1];
            if (null != values && values.length > 1) {
                dictType = values[0];
            } else {
                dictType = StringUtil.split(dictType, "$")[0];
            }

            sysDictOrderedMap = IdfCodeTransfer.listDistinctFilter(sysDictOrderedMap, "treeLevel", level, null);
        }

        for (int i = 0; i < sysDictOrderedMap.size(); i++) {
            SysDict sysDict = ((SysDict) sysDictOrderedMap.getValue(i));
            if (ignoreValues.contains(sysDict.getDictCode())) {
                continue;
            } else {
                Map<String, Object> dictMap = new HashMap<String, Object>();
                dictMap.put("key", sysDict.getDictCode());
                dictMap.put("value", sysDict.getDictName());
                result.add(dictMap);
            }
        }
        return result;
    }

    /*
     * 根据字典编码获取该节点的所有子节点，用于联动翻译
     */
    @DataProvider
    public List<Map<String, Object>> getDictTree(String dictCode) throws Exception {
        String sql = "select dict_code key, dict_code||':'||dict_name value from sys_dict t where t.parent_dict_id!='0' and t.tree_seq like '." + dictCode + "%' and t.status='"
                + FrameworkConstants.STATUS_EFFECTIVE + "'";
        sql += " order by t.view_seq asc";
        return getDaoSupport().getJdbcDao()
                .queryForList(sql);
    }

    /*
     * 获取字典表自动映射为autoMappingDropDown
     */
    @DataProvider
    public List<Map<String, String>> getQueryMethod() throws Exception {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", "eq");
        map.put("value", "等于");
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("key", "ne");
        map1.put("value", "不等于");
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("key", "gt");
        map2.put("value", "大于");
        Map<String, String> map3 = new HashMap<String, String>();
        map3.put("key", "ge");
        map3.put("value", "大于等于");
        Map<String, String> map4 = new HashMap<String, String>();
        map4.put("key", "lt");
        map4.put("value", "小于");

        Map<String, String> map5 = new HashMap<String, String>();
        map5.put("key", "le");
        map5.put("value", "小于等于");

        list.add(map);
        list.add(map1);
        list.add(map2);
        list.add(map3);
        list.add(map4);
        list.add(map5);
        return list;
    }

    private static Map toMap(Model model) {
        Map map = new HashMap();
        map.put("model", model);
        map.put("fields", model.getFields());
        return map;
    }

    private static Map<String, String> map;

    static {
        map = new HashMap<String, String>();
        map.put("varchar2", "String");
        map.put("varchar", "String");
        map.put("int", "Integer");
        map.put("integer", "Integer");
        map.put("datetime", "Date");
        map.put("date", "Date");
        map.put("timestmp", "Date");
        map.put("nvarchar2", "String");
        map.put("char", "String");
        map.put("uniqueidentifier", "String");
        map.put("number", "BigDecimal");
        map.put("decimal", "BigDecimal");
        map.put("bigint", "Long");
        map.put("tinyint", "Boolean");
        map.put("blob", "Blob");
        map.put("clob", "String");
    }

    public static String getPojoType(String dataType) {
        String tmp = dataType.toLowerCase();
        StringTokenizer st = new StringTokenizer(tmp);
        return map.get(st.nextToken());
    }

    /**
     * 生成类名
     *
     * @param className
     * @return
     */
    private static String generateClassName(String className) {
        String stra = className.toString().substring(0, 1).toUpperCase()
                + className.toString().substring(1,
                className.toString().length());
        return stra;
    }

    /**
     * 修改成 对象名称
     *
     * @param entityName
     * @return
     */
    private static String generateMdEntityName(String entityName) {
        String stra = entityName.toString().substring(0, 1).toLowerCase()
                + entityName.toString().substring(1,
                entityName.toString().length());
        return stra;
    }

}
