package com.innofi.component.codeauxiliarytool.coder.util;

import java.util.List;

import com.innofi.component.codeauxiliarytool.coder.domain.ColumnInfo;


public class Model {

    private String tableName;
    private String packageName;
    private String modelName;
    private String modelCnName;
    private List<ColumnInfo> fields;
    private String beanName;
    
    private String discPathName;

    public String getDiscPathName() {
		return discPathName;
	}

	public void setDiscPathName(String discPathName) {
		this.discPathName = discPathName;
	}

	public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List getFields() {
        return this.fields;
    }

    public void setFields(List fields) {
        this.fields = fields;
    }

    public String getModelCnName() {
        return this.modelCnName;
    }

    public void setModelCnName(String modelCnName) {
        this.modelCnName = modelCnName;
    }

    public String getModelName() {
        return this.modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

}
