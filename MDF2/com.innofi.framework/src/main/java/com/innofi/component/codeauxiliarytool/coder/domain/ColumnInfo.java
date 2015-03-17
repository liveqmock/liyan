package com.innofi.component.codeauxiliarytool.coder.domain;

public class ColumnInfo {

    // 表名

    private String tableName;
    // 数据库对应的字段名称

    private String oldColumnName;
    // pojo对象对应的字段名称；

    private String columnName;
    // pojo对应的set get 名称

    private String fieldName;

    // pojo的数据类型
    private String javaType;

    // dorado数据类型
    private String doradoType;

    // 数据库字段类型
    private String columnType;

    // 10,2 10

    private String columnSize;

    // 默认值
    private String defaultValue;

    // 是否为空
    private String isnullAble;

    //是否存储中文
    private String isCN;

    public String getIsCN() {
        return isCN;
    }

    public void setIsCN(String CN) {
        isCN = CN;
    }

    public String getIsnullAble() {
        return isnullAble;
    }

    public void setIsnullAble(String isnullAble) {
        this.isnullAble = isnullAble;
    }

    public String getIsprimaryKey() {
        return isprimaryKey;
    }

    public void setIsprimaryKey(String isprimaryKey) {
        this.isprimaryKey = isprimaryKey;
    }

    public String getIsautoincrement() {
        return isautoincrement;
    }

    // 是否主见
    private String isprimaryKey;

    // 是否自动创建
    private String isautoincrement;
    // 组件类型。


    private String componentType;
    // 是否查询
    private String searchFlag;
    // 查询类型
    private String searchType;
    // 列表是否显示
    private String listFlag;

    // 修改是否显示
    private String modifyFlag;

    // 修改是否只读
    private String modifyReadyFlag;

    // 新增是否显示
    private String addFlag;

    // 新增是否之读
    private String addReadyFlag;


    //下拉框字典类型DictType
    private String dictType;

    //标签
    private String propertyLabel;

    //默认显示信息
    private String propertyBlankText;

    //提示信息
    private String propertyTip;

    // 验证

    private String validators;

    // 正则表达式
    private String regexp;
    //下拉框选择样式
    private String selectType;


    private String orgCategory;

    private String rootCode;

    public String getRootCode() {
        return rootCode;
    }

    public void setRootCode(String rootCode) {
        this.rootCode = rootCode;
    }

    public String getSelectType() {
        return selectType;
    }

    public void setSelectType(String selectType) {
        this.selectType = selectType;
    }

    public String getOrgCategory() {
        return orgCategory;
    }

    public void setOrgCategory(String orgCategory) {
        this.orgCategory = orgCategory;
    }

    public String getOrgExpandLevel() {
        return orgExpandLevel;
    }

    public void setOrgExpandLevel(String orgExpandLevel) {
        this.orgExpandLevel = orgExpandLevel;
    }

    private String orgExpandLevel;

    public String getRegexp() {
        return regexp;
    }

    public void setRegexp(String regexp) {
        this.regexp = regexp;
    }

    private String dataRange;
    //注释
    private String remarks;

    public String getPropertyLabel() {
        return propertyLabel;
    }

    public void setPropertyLabel(String propertyLabel) {
        this.propertyLabel = propertyLabel;
    }

    public String getPropertyBlankText() {
        return propertyBlankText;
    }

    public void setPropertyBlankText(String propertyBlankText) {
        this.propertyBlankText = propertyBlankText;
    }

    public String getPropertyTip() {
        return propertyTip;
    }

    public void setPropertyTip(String propertyTip) {
        this.propertyTip = propertyTip;
    }


    public String getSearchFlag() {
        return searchFlag;
    }

    public void setSearchFlag(String searchFlag) {
        this.searchFlag = searchFlag;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getListFlag() {
        return listFlag;
    }

    public void setListFlag(String listFlag) {
        this.listFlag = listFlag;
    }

    public String getModifyFlag() {
        return modifyFlag;
    }

    public void setModifyFlag(String modifyFlag) {
        this.modifyFlag = modifyFlag;
    }


    public String getAddFlag() {
        return addFlag;
    }

    public void setAddFlag(String addFlag) {
        this.addFlag = addFlag;
    }


    public String getModifyReadyFlag() {
        return modifyReadyFlag;
    }

    public void setModifyReadyFlag(String modifyReadyFlag) {
        this.modifyReadyFlag = modifyReadyFlag;
    }

    public String getAddReadyFlag() {
        return addReadyFlag;
    }

    public void setAddReadyFlag(String addReadyFlag) {
        this.addReadyFlag = addReadyFlag;
    }

    public String getDictType() {
        return dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    public String getValidators() {
        return validators;
    }

    public void setValidators(String validators) {
        this.validators = validators;
    }

    public String getDataRange() {
        return dataRange;
    }

    public void setDataRange(String dataRange) {
        this.dataRange = dataRange;
    }

    public String getOldColumnName() {
        return oldColumnName;
    }

    public void setOldColumnName(String oldColumnName) {
        this.oldColumnName = oldColumnName;
    }

    public String isIsautoincrement() {
        return isautoincrement;
    }

    public void setIsautoincrement(String isautoincrement) {
        this.isautoincrement = isautoincrement;
    }


    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setColumnSize(String columnSize) {
        this.columnSize = columnSize;
    }

    public String getColumnSize() {
        return columnSize;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public String getDoradoType() {
        return doradoType;
    }

    public void setDoradoType(String doradoType) {
        this.doradoType = doradoType;
    }

}
