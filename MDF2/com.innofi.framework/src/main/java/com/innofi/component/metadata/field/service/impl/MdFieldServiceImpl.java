/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.component.metadata.field.service.impl;

import com.innofi.component.metadata.commons.MetadataConstants;
import com.innofi.component.codetransfer.IdfCodeTransfer;
import com.innofi.component.dbconsole.pojo.DbConsoleColumn;
import com.innofi.framework.pojo.metadata.MdField;
import com.innofi.component.metadata.field.service.IMdFieldService;
import com.innofi.framework.pojo.metadata.MdTable;
import com.innofi.component.metadata.table.service.IMdTableService;
import com.innofi.framework.FrameworkConstants;
import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.PropertyMapping;
import com.innofi.framework.dao.hibernate.PropertyOrder;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.framework.service.impl.BaseServiceImpl;

import javax.annotation.Resource;

import com.innofi.framework.spring.context.ContextHolder;
import com.innofi.framework.utils.reflect.ReflectionUtil;
import com.innofi.framework.utils.string.StringUtil;
import com.innofi.framework.utils.xml.XmlBuilder;
import com.innofi.framework.utils.xml.XmlDocument;
import com.innofi.framework.utils.xml.XmlNode;
import com.innofi.framework.utils.xml.XmlParseException;
import com.innofi.framework.utils.xml.dom4j.Dom4jXmlBuilder;
import com.innofi.framework.utils.xml.dom4j.Dom4jXmlOutputter;

import org.apache.commons.collections.map.ListOrderedMap;
import org.hibernate.mapping.PersistentClass;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * 功能/ 模块：元数据模块
 *
 * @author 张磊 Alex.zhang@innofi.com.cn
 * @version 2.0.0 12-11-30
 *          元数据字段服务实现类
 *          修订历史：2013年1月20
 *          日期  作者  参考  描述
 *          2013年1月20  刘名寓 添加刷新字段元数据方法，删除多余方法 findMdFieldByTableId
 *          北京名晟信息技术有限公司版权所有.
 */
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
@Service(value = "mdFieldService")
public class MdFieldServiceImpl extends BaseServiceImpl<MdField, String> implements IMdFieldService {

    static XmlBuilder xmlBuilder = new Dom4jXmlBuilder();
    static Dom4jXmlOutputter dom4jXmlOutputter = new Dom4jXmlOutputter();
    static List listViewFiles = new ArrayList();
    static Map<String, String> dataTypeMaps = new HashMap<String, String>();

    @Resource(name="mdDataTitleTableMappingDaoSupport")
    protected DaoSupport daoSupport;

    public DaoSupport getDaoSupport(){
        return daoSupport;
    }

    /**
     * 覆盖父类方法，缓存状态为有效的MdField对象
     *
     * @return 当前有效的MdField列表
     */
    public List loadCacheObjects() {
        return findByProperty("status", FrameworkConstants.STATUS_EFFECTIVE, QueryConstants.EQUAL);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.metadata.field.service.IMdFieldService#findMdFieldsByTableId(String)
     */
    public List<MdField> findMdFieldsByTableId(String tableId) {
        IdfCodeTransfer idfCodeTransfer = getIdfCodeTransfer();
        ListOrderedMap allFields = idfCodeTransfer.getCacheObjects("mdFieldService");
        List<MdField> tableFields = idfCodeTransfer.listDistinctFilter(allFields, "tableId", tableId, null).valueList();
        if (tableFields == null || tableFields.size() == 0) {
            tableFields = findByProperty("tableId", tableId, QueryConstants.EQUAL);
        }
        return tableFields;
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.metadata.field.service.IMdFieldService#findLastVersionMdFieldsByTableName(String)
     */
    public List<MdField> findLastVersionMdFieldsByTableName(String tableName) {
        IMdTableService mdTableService = getSpringBean("mdTableService");
        MdTable mdTable = mdTableService.findLastVersionMdTableByTableName(tableName);
        if (mdTable == null) {
            return new ArrayList();
        } else {
            return findMdFieldsByTableId(mdTable.getId());
        }
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.metadata.field.service.IMdFieldService#compareFields(String, BigDecimal, BigDecimal)
     */
    public Map<BigDecimal, List<MdField>> compareFields(String tableName, BigDecimal verNo1, BigDecimal verNo2) {
        IMdTableService mdTableService = ContextHolder.getSpringBean("mdTableService");

        MdTable verNo1Table = mdTableService.findUniqueByHql("from MdTable mt where mt.verNo = ? and tableName = ?", verNo1, tableName);
        MdTable verNo2Table = mdTableService.findUniqueByHql("from MdTable mt where mt.verNo = ? and tableName = ?", verNo2, tableName);
        List<MdField> verNo1Fields = findMdFieldsByTableId(verNo1Table.getId());
        List<MdField> verNo2Fields = findMdFieldsByTableId(verNo2Table.getId());

        List<MdField> comparedFields1 = new ArrayList<MdField>();
        List<MdField> comparedFields2 = new ArrayList<MdField>();


        /**
         * 较早版本
         */
        Map<String, MdField> verNo1FieldsMapping = new HashMap<String, MdField>(); //保存较早版本字段到Map中，减少后续遍历
        for (MdField verNo1Field : verNo1Fields) {
            verNo1FieldsMapping.put(verNo1Field.getFieldName(), verNo1Field);
        }

        for (MdField verNo2Field : verNo2Fields) {
            String fieldName = verNo2Field.getFieldName();             //字段名称
            MdField verNo1Field = null;                            //当前有效版本字段对象
            if (verNo1FieldsMapping.keySet().contains(fieldName)) {
                verNo1Field = verNo1FieldsMapping.get(fieldName);      //取出当前有效版本字段
                if (verNo1Field.equals(verNo2Field)) {
                    verNo1Field.setUpdateFlg(MetadataConstants.FIELD_UN_CHANGE);
                    verNo2Field.setUpdateFlg(MetadataConstants.FIELD_UN_CHANGE);
                    comparedFields1.add(verNo1Field);
                    comparedFields2.add(verNo2Field);
                } else {
                    verNo1Field.setUpdateFlg(MetadataConstants.FIELD_UPDATE);
                    verNo2Field.setUpdateFlg(MetadataConstants.FIELD_UPDATE);
                    comparedFields1.add(verNo1Field);
                    comparedFields2.add(verNo2Field);
                }
            } else {
                verNo2Field.setUpdateFlg(MetadataConstants.FIELD_ADD);
                MdField newVer1Field = new MdField();
                newVer1Field.setStatus(FrameworkConstants.STATUS_INVALID);
                newVer1Field.setUpdateFlg(MetadataConstants.FIELD_DEL);
                comparedFields1.add(newVer1Field);
                comparedFields2.add(verNo2Field);
            }
        }

        int index = 0;
        for (MdField verNo1Field : verNo1Fields) {
            if (verNo1Field.getUpdateFlg() == 0) {
                verNo1Field.setUpdateFlg(MetadataConstants.FIELD_ADD);
                MdField newVer2Field = new MdField();
                newVer2Field.setUpdateFlg(MetadataConstants.FIELD_DEL);
                comparedFields1.add(index, verNo1Field);
                comparedFields2.add(index, newVer2Field);
            }
            index++;
        }

        Map result = new HashMap();
        result.put(verNo1, comparedFields1);
        result.put(verNo2, comparedFields2);
        return result;
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.metadata.field.service.IMdFieldService#refreshMdFieldMetaData(String, List, List)
     */
    public List<MdField> refreshMdFieldMetaData(String tableName, List<MdField> preVerFields, List<MdField> dbMdFields) {
        List<MdField> changeFields = new ArrayList<MdField>();

        if (preVerFields.size() == 0) {//没有之前版本
            return dbMdFields;
        } else {
            /**
             * 之前有版本
             */
            Map<String, MdField> preVerFieldsMapping = new HashMap<String, MdField>(); //保存当前有效版本字段Map结构减少后续遍历
            for (MdField preVerField : preVerFields) {
                preVerFieldsMapping.put(preVerField.getFieldName(), preVerField);
            }

            List<MdField> newVerMdFields = new ArrayList<MdField>();                //保存新版本字段
            boolean allEq = true;                                                           //是否所有属性都相同，默认true

            /**
             * 判断如果上一版本字段在数据库字段中存在，则复制上一版本字段的属性
             */
            for (MdField dbMdField : dbMdFields) {
                MdField newVerMdField = new MdField();                              //创建新版本字段对象
                String fieldName = dbMdField.getFieldName();                                //字段名称
                MdField preVerMdField = null;                                           //当前有效版本字段对象
                if (preVerFieldsMapping.keySet().contains(fieldName)) {
                    preVerMdField = preVerFieldsMapping.get(fieldName);                     //取出当前有效版本字段
                    BeanUtils.copyProperties(preVerMdField, newVerMdField);                  //复制当前有效版本字段信息到新版本
                }
                ReflectionUtil.copyProperties(dbMdField, newVerMdField);                                    //复制数据字段信息到新版本
                if (preVerMdField == null || !preVerMdField.equals(newVerMdField)) allEq = false; //当有不同时标识为false
                newVerMdFields.add(newVerMdField);                                          //添加到新版本字段列表中
            }
            if (newVerMdFields.size() == preVerFields.size() && allEq) return changeFields;       //返回无变化
            changeFields = newVerMdFields;
        }

        return changeFields;
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.metadata.field.service.IMdFieldService#findMdFieldsByTableNameFromDb(String)
     */
    public List<MdField> findMdFieldsByTableNameFromDb(String tableName) {
        String lowerName = tableName.toLowerCase();
        String upperName = tableName.toUpperCase();
        Map<String,String> params = new HashMap<String,String>();
        params.put("lowerName", lowerName);
        params.put("upperName", upperName);
        params.put("tableName", tableName);
        return dynamicQueryBeanForList("metadata-dynamic-statement", "findFieldInfo", params);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.metadata.field.service.IMdFieldService#createNewObject(Collection, String, List)
     */
    public void createNewObject(Collection<MdField> mdFields, String tableId, List<String> fieldIds) {
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
        //获取到table的service.
        IMdTableService mdTableService = getSpringBean("mdTableService");
        //更新表结构版本信息.
        MdTable mdTable = mdTableService.get(tableId);
        MdTable mdTableNew = new MdTable();
        BeanUtils.copyProperties(mdTable, mdTableNew);
        mdTableNew.setId("");
        Long verNo = mdTable.getVerNo().longValue();
        mdTableNew.setVerNo(BigDecimal.valueOf(verNo + 1));
        mdTableService.save(mdTableNew);
        mdTable.setStatus(FrameworkConstants.STATUS_INVALID);
        mdTableService.update(mdTable);
        //开始处理表字段新版本的数据.
        List<MdField> mdFieldList = new ArrayList<MdField>();
        //mdFieldList = findMdFieldByTableId(tableId);
        mdFieldList = findByIds(fieldIds);
        //迭代参数传过来的对象集合.
        for (Iterator<MdField> mdFieldsiterator = mdFields.iterator(); mdFieldsiterator.hasNext(); ) {
            MdField mdFieldNew = (MdField) mdFieldsiterator.next();
            //迭代通过tableId传过来的集合.
            for (Iterator<MdField> iterator = mdFieldList.iterator(); iterator.hasNext(); ) {
                MdField mdField = (MdField) iterator.next();
                //如果两个迭代的id相同,就将参数传过来的对象作为新的对象保存.
                if (mdFieldNew.getId().equals(mdField.getId())) {
                    MdField newVersionField = new MdField();
                    BeanUtils.copyProperties(mdFieldNew, newVersionField);
                    newVersionField.setId("");
                    newVersionField.setStatus(FrameworkConstants.STATUS_EFFECTIVE);
                    newVersionField.setTableId(mdTableNew.getId());
                    save(newVersionField);
                }
            }
        }
        //创建的是没有参数传来的ID的数据.
        List<MdField> mdFieldList2 = new ArrayList<MdField>();
        addPropertyFilter(filters, "id", fieldIds, QueryConstants.NOT_IN, true);
        addPropertyFilter(filters, "tableId", tableId, QueryConstants.EQUAL, true);
        addPropertyFilter(filters, "status", FrameworkConstants.STATUS_EFFECTIVE, QueryConstants.EQUAL, true);
        mdFieldList2 = findByPropertyFilters(filters, orders);
        for (Iterator<MdField> iterator = mdFieldList2.iterator(); iterator.hasNext(); ) {
            MdField mdField2 = (MdField) iterator.next();
            MdField newVersionField2 = new MdField();
            BeanUtils.copyProperties(mdField2, newVersionField2);
            newVersionField2.setId("");
            newVersionField2.setStatus(FrameworkConstants.STATUS_EFFECTIVE);
            newVersionField2.setTableId(mdTableNew.getId());
            save(newVersionField2);
        }
        //修改原来数据的状态为失效.
        List<MdField> mdFieldListOld = new ArrayList<MdField>();
        mdFieldListOld = findMdFieldsByTableId(tableId);
        for (Iterator<MdField> iterator = mdFieldListOld.iterator(); iterator.hasNext(); ) {
            MdField mdFieldOld = (MdField) iterator.next();
            mdFieldOld.setStatus(FrameworkConstants.STATUS_INVALID);
            update(mdFieldOld);
        }
    }

    @Override
    public void refreshMdFieldLabel() throws IOException, XmlParseException, ClassNotFoundException {
/*        List a = new ArrayList();
        List b = new ArrayList();
        String rootPath = ContextHolder.getSystemProperties().getString("coder.path", "D:/coder");
*//*		String rootPath = "D://doworkspace//develop-platform";*//*
        this.findAllXml(rootPath, a, b);
        listViewFiles.addAll(a);
        listViewFiles.addAll(b);
//		String xmLPath="D:/doworkspace/develop-platform/com.innofi.component.rbac/src/main/java/com/innofi/component/rbac/index/Setting.view.xml";
//		String modelpath = "D:/doworkspace/develop-platform/com.innofi.component.rbac/src/main/java/models/Rbac.model.xml";
//		listViewFiles.add(modelpath);
//		listViewFiles.add(xmLPath);
//		this.readXml(xmLPath);
        for (int i = 0; i < listViewFiles.size(); i++) {
            //System.out.println("view ==="+listViewFiles.get(i) );
            this.readXml(listViewFiles.get(i).toString());
        }*/
    }

    @Override
    public void saveMdFieldsforDynamictPojoManage(MdField mdField) {
        if (getDaoSupport().getJdbcDao().getCurrentDbType().equalsIgnoreCase("oracle")) {
            mdField.setFieldType("VARCHAR2");
        } else {
            mdField.setFieldType("VARCHAR");
        }

        mdField.setIsTableView(FrameworkConstants.COMM_Y);
        mdField.setIsAddView(FrameworkConstants.COMM_Y);
        mdField.setIsUpdView(FrameworkConstants.COMM_Y);
        mdField.setIsExtend(FrameworkConstants.COMM_Y);
        mdField.setEntityAttriName(StringUtil.convertPropertyName(mdField.getFieldName()));

        mdField.setEleType("1");
        mdField.setIsQueryCond(FrameworkConstants.COMM_N);
        mdField.setIsPk(FrameworkConstants.COMM_N);
        mdField.setIsPkIndex(FrameworkConstants.COMM_N);
        mdField.setIsStati(FrameworkConstants.COMM_N);
        mdField.setIsRedundant(FrameworkConstants.COMM_N);
        mdField.setIsAddRead(FrameworkConstants.COMM_N);
        mdField.setIsUpdRead(FrameworkConstants.COMM_N);
        mdField.setFieldLen(new BigDecimal(400));
        mdField.setFieldCnName(mdField.getLabelInf());
        mdField.setStatus(FrameworkConstants.STATUS_INVALID);
        save(mdField);
    }

    public void updateMdFieldsforDynamictPojoManage(MdField mdField, Boolean active) {
        mdField.setEntityAttriName(StringUtil.convertPropertyName(mdField.getFieldName()));
        mdField.setFieldCnName(mdField.getLabelInf());
        update(mdField);
        if (active != null && active == true) {
            boolean exist = getDaoSupport().getJdbcDao().ifTableColumnExist("", mdField.getTableName(), mdField.getFieldName());
            if (!exist) {
                DbConsoleColumn dbConsoleColumn = new DbConsoleColumn();
                dbConsoleColumn.setTableName(mdField.getTableName());
                dbConsoleColumn.setColumnName(mdField.getFieldName());
                dbConsoleColumn.setColumnType(mdField.getFieldType());
                dbConsoleColumn.setColumnSize(mdField.getFieldLen() + "");
                dbConsoleColumn.setIsprimaryKey(false);
                dbConsoleColumn.setIsnullAble(true);
                getDaoSupport().getJdbcDao().insertColumn(null, mdField.getTableName(), dbConsoleColumn);
            }
        }
    }

    public void findAllXml(String rootPath, List a, List b) {
        File[] files = new File(rootPath).listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                findAllXml(file.toString(), a, b);
            } else {
                if (file.getName().endsWith("model.xml")) {
                    a.add(file.getPath());
                }
                if (file.getName().endsWith("view.xml")) {
                    b.add(file.getPath());
                }
            }
        }


    }

    public void readXml(String xmLPath) throws IOException, XmlParseException, ClassNotFoundException {
        IMdTableService mdTableService = ContextHolder.getSpringBean("mdTableService");

        //替换label
        XmlDocument xmlDocument = xmlBuilder.buildDocument(new File(xmLPath));
        XmlNode[] dataTypes = xmlDocument.getNodesByXPath("//DataType");
        for (XmlNode dataTypeNode : dataTypes) {
            String dataTypeName = dataTypeNode.getAttributeString("name");
            XmlNode[] creationTypeNodes = dataTypeNode.getNodesByXPath("./Property[@name='creationType']");
            //System.out.println("dataTypeName   "+dataTypeName);
            if (creationTypeNodes == null || creationTypeNodes.length == 0) {
                for (Map.Entry<String, String> entry : dataTypeMaps.entrySet()) {
                    // System.out.println("key[" + entry.getKey() + "] creationType[" + entry.getValue()+ "]");
                    if (dataTypeName.indexOf("query") != -1) {
                        if (dataTypeName.substring(5).equals(entry.getKey())) {
                            XmlNode[] propertiesDefNodes = dataTypeNode.getNodesByXPath("./PropertyDef");
                            for (XmlNode propertyDef : propertiesDefNodes) {
                                String propertyName = propertyDef.getAttribute("name");
                                XmlNode labelNode = propertyDef.getNodeByXPath("./Property[@name='label']");
                                if (labelNode != null) {
                                    String label = labelNode.getContent();
                                    if (label != null && !"".equals(label)) {
                                        if (label.indexOf("[\"com") == -1) {
                                            labelNode.setContent(label.replace("[\"", "[\"" + entry.getValue().toString() + "."));
                                        }
                                    } else {
                                        Class clasEntity = Class.forName(entry.getValue().toString());
                                        PersistentClass persistentClass = getDaoSupport().getHibernateDao().getPersistenceClass(clasEntity);

                                        if (persistentClass != null) {
                                            String tableName = persistentClass.getTable().getName();
                                            HashMap<String, PropertyMapping> propertyMappings = getDaoSupport().getHibernateDao().getEntityHbmMapping(clasEntity);
                                            for (Map.Entry<String, PropertyMapping> entry1 : propertyMappings.entrySet()) {
                                                if (propertyName.equals(entry1.getValue().getPropertyName())) {
                                                    String labelString = "" + entry.getValue().toString() + "." + entry1.getValue().getPropertyName() + "";
                                                    labelNode.setContent("${req[\"" + labelString + "\"].propertyLabel}");
                                                }
                                            }
                                        }
                                    }

                                }
                            }

                        }
                    }
                }
            } else {
                String creationType = creationTypeNodes[0].getContent();
                //System.out.println("creationType  "+creationType);
                //插入md_table表
                Class claEntity = Class.forName(creationType);
                PersistentClass persisClass = getDaoSupport().getHibernateDao().getPersistenceClass(claEntity);

                if (persisClass != null) {
                    //刷新表数据  entityName。serviceName。daoName。actionName。uiName
                    String tableName = persisClass.getTable().getName();
                    String pojoName = creationType.substring(creationType.lastIndexOf(".") + 1, creationType.length());
                    if (pojoName.indexOf("Pojo") != -1) {
                        pojoName = pojoName.substring(0, pojoName.length() - 4);
                    }
                    String entityName = creationType;
                    String packge = creationType.substring(0, creationType.indexOf(".pojo."));
                    String serviceName = packge + ".service.impl." + pojoName + "ServiceImpl";
                    String daoName = packge + ".dao." + pojoName + "DaoSupport";
                    String actionName = packge + ".action." + pojoName + "Action";
                    String uiName = packge + ".view." + pojoName + "Manage.d";
                    MdTable mdTable = mdTableService.findLastVersionMdTableByTableName(tableName.toUpperCase());
                    if (mdTable == null) {
                        mdTableService.refreshTableMetaData(tableName.toUpperCase(), null);
                        mdTable = mdTableService.findLastVersionMdTableByTableName(tableName.toUpperCase());
                    }
                    mdTable.setEntityName(entityName);
                    mdTable.setServiceName(serviceName);
                    mdTable.setDaoName(daoName);
                    mdTable.setUiName(uiName);
                    mdTable.setActionName(actionName);
                    mdTableService.update(mdTable);
                    //刷新字段数据    entity_attri_name
                    HashMap<String, PropertyMapping> propertyMappings = getDaoSupport().getHibernateDao().getEntityHbmMapping(claEntity);
                    for (Map.Entry<String, PropertyMapping> entry : propertyMappings.entrySet()) {
                    	getDaoSupport().getJdbcDao().execute("update md_field t set t.entity_attri_name ='" + entry.getValue().getPropertyName() + "' where t.table_name='" + tableName.toUpperCase() + "' and t.field_name='" + entry.getValue().getColumnName().toUpperCase() + "'");
                    }
                }
                dataTypeMaps.put(dataTypeName, creationType);
                XmlNode[] propertiesDefNodes = dataTypeNode.getNodesByXPath("./PropertyDef");


                for (XmlNode propertyDef : propertiesDefNodes) {
                    String propertyName = propertyDef.getAttribute("name");
                    XmlNode labelNode = propertyDef.getNodeByXPath("./Property[@name='label']");
                    if (labelNode != null) {
                        String label = labelNode.getContent();
                        if (label.indexOf("${req[") != -1) {
                            if (label.indexOf("[\"com") == -1) {
                                labelNode.setContent(label.replace("[\"", "[\"" + creationType + "."));
                            }
                        } else {
                            Class clasEntity = Class.forName(creationType);
                            PersistentClass persistentClass = getDaoSupport().getHibernateDao().getPersistenceClass(clasEntity);

                            if (persistentClass != null) {
                                String tableName = persistentClass.getTable().getName();
                                HashMap<String, PropertyMapping> propertyMappings = getDaoSupport().getHibernateDao().getEntityHbmMapping(claEntity);
                                for (Map.Entry<String, PropertyMapping> entry : propertyMappings.entrySet()) {
                                    if (propertyName.equals(entry.getValue().getPropertyName())) {
                                    	getDaoSupport().getJdbcDao().execute("update md_field t set t.label_inf = '" + label + "' where t.table_name='" + tableName.toUpperCase() + "' and t.field_name='" + entry.getValue().getColumnName().toUpperCase() + "'");
                                        String labelString = "" + creationType + "." + entry.getValue().getPropertyName() + "";
                                        labelNode.setContent("${req[\"" + labelString + "\"].propertyLabel}");
                                    }
                                }
                            }
                        }
                    } else {
                        XmlNode propertyNode = propertyDef.addChild("Property");
                        propertyNode.setAttribute("name", "label");
                        Class clasEntity = Class.forName(creationType);
                        PersistentClass persistentClass = getDaoSupport().getHibernateDao().getPersistenceClass(clasEntity);
                        if (persistentClass != null) {

                            HashMap<String, PropertyMapping> propertyMappings = getDaoSupport().getHibernateDao().getEntityHbmMapping(claEntity);
                            for (Map.Entry<String, PropertyMapping> entry : propertyMappings.entrySet()) {
                                if (propertyName.equals(entry.getValue().getPropertyName())) {
                                    String labelString = "" + creationType + "." + entry.getValue().getPropertyName() + "";
                                    propertyNode.setContent("${req[\"" + labelString + "\"].propertyLabel}");
                                }
                            }
                        }

                    }
                }
            }

        }
        //替换tip ,bankText
        XmlNode[] autoformNodes = xmlDocument.getNodesByXPath("//AutoForm");
        for (XmlNode dataTypeNode : autoformNodes) {
            XmlNode[] dataTypeName = dataTypeNode.getNodesByXPath("./Property[@name='dataType']");
            if (dataTypeName != null && dataTypeName.length != 0) {
                String dataType = dataTypeName[0].getContent();
                if (dataType.indexOf("query") != -1) {
                    dataType = dataType.substring(5);
                }
                String creationType = dataTypeMaps.get(dataType);
                XmlNode[] queryblankTexts = dataTypeNode.getNodesByXPath("./AutoFormElement/Editor/Container/TextEditor/Property[@name='blankText']");
                XmlNode[] querytips = dataTypeNode.getNodesByXPath("./AutoFormElement/Editor/Container/TextEditor/Property[@name='tip']");

                XmlNode[] blankTexts = dataTypeNode.getNodesByXPath("./AutoFormElement/Editor/TextEditor/Property[@name='blankText']");
                XmlNode[] tips = dataTypeNode.getNodesByXPath("./AutoFormElement/Editor/TextEditor/Property[@name='tip']");
                for (XmlNode blankText : blankTexts) {
                    String str = blankText.getContent();
                    if (str.indexOf("${req[") != -1) {
                        if (str.indexOf("[\"com") == -1) {
                            blankText.setContent(str.replace("[\"", "[\"" + creationType + "."));
                        }
                    }
                }

                for (XmlNode tip : tips) {
                    String str = tip.getContent();
                    if (str.indexOf("${req[") != -1) {
                        if (str.indexOf("[\"com") == -1) {
                            tip.setContent(str.replace("[\"", "[\"" + creationType + "."));
                        }
                    }
                }

                for (XmlNode blankText : queryblankTexts) {
                    String str = blankText.getContent();
                    if (str.indexOf("${req[") != -1) {
                        if (str.indexOf("[\"com") == -1) {
                            blankText.setContent(str.replace("[\"", "[\"" + creationType + "."));
                        }
                    }
                }

                for (XmlNode tip : querytips) {
                    String str = tip.getContent();
                    if (str.indexOf("${req[") != -1) {
                        if (str.indexOf("[\"com") == -1) {
                            tip.setContent(str.replace("[\"", "[\"" + creationType + "."));
                        }
                    }
                }
            }
        }
        dom4jXmlOutputter.output(xmlDocument, new File(xmLPath));


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

}


