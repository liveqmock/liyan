package com.innofi.component.dbconsole.pojo;

import com.innofi.framework.utils.sql.SqlUtil;
import com.innofi.framework.utils.string.StringUtil;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @author liumy2009@126.com
 * @version 2.0
 *          <p/>
 *          序列定义对象
 * @found date: 2012-5-20
 * @found time: 下午3:47:07
 */
public class DbConsoleSequence {
    private String sequenceName;
    private String schema;
    private String catalog;
    private CharSequence source;
    private String comment;
    private String startValue;
    private String minValue;
    private String maxValue;
    private String increment;
    private String cycle;
    private String currentValue;
    private String dataType;


    public DbConsoleSequence() {

    }

    public DbConsoleSequence(String seqSchema, String seqName) {
        sequenceName = seqName;
        schema = seqSchema;
    }

    public DbConsoleSequence(String seqCatalog, String seqSchema, String seqName) {
        catalog = seqCatalog;
        sequenceName = seqName;
        schema = seqSchema;
    }

    public String getComment() {
        return comment;
    }

    public String getSchema() {
        return schema;
    }

    public String getCatalog() {
        return catalog;
    }


    public String getObjectType() {
        return "SEQUENCE";
    }


    public String getObjectName() {
        return getSequenceName();
    }

    public String getSequenceName() {
        return this.sequenceName;
    }

    public CharSequence getSource() {
        if (StringUtil.hasText(sequenceName)) {
            StringBuilder ddl = new StringBuilder("CREATE SEQUENCE " + this.sequenceName);

            if (StringUtil.hasText(dataType)) {
                ddl.append(" DATATYPE " + dataType);
            }
            
            if(StringUtil.hasText(startValue)){
            	ddl.append(" START WITH " + startValue);
            }

            ddl.append(" INCREMENT BY " + increment);

            if (StringUtil.hasText(this.minValue)) {
                ddl.append(" MINVALUE " + minValue);
            }

            if (StringUtil.hasText(maxValue)) {
                ddl.append(" MAXVALUE " + maxValue);
            }

/*            if (StringUtil.hasText(cycle) && cycle.equalsIgnoreCase("Y")) {
                ddl.append(" CYCLE ");
            } else {
                ddl.append(" NO CYCLE ");
            }*/
            
            source = SqlUtil.formatDDLSql(ddl.toString());
        }
        return source;
    }

    public void setSource(CharSequence src) {
        source = src;
    }

    public String getStartValue() {
        return startValue;
    }

    public String getMinValue() {
        return minValue;
    }

    public String getMaxValue() {
        return maxValue;
    }

    public String getIncrement() {
        return increment;
    }

    public String getCycle() {
        return cycle;
    }

    public String getCurrentValue() {
        return currentValue;
    }

    public String getDataType() {
        return dataType;
    }

    public void setSequenceName(String sequenceName) {
        this.sequenceName = sequenceName;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setStartValue(String startValue) {
        this.startValue = startValue;
    }

    public void setMinValue(String minValue) {
        this.minValue = minValue;
    }

    public void setMaxValue(String maxValue) {
        this.maxValue = maxValue;
    }

    public void setIncrement(String increment) {
        this.increment = increment;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DbConsoleSequence sequence = (DbConsoleSequence) o;

        if (catalog != null ? !catalog.equals(sequence.catalog) : sequence.catalog != null) return false;
        if (comment != null ? !comment.equals(sequence.comment) : sequence.comment != null) return false;
        if (currentValue != null ? !currentValue.equals(sequence.currentValue) : sequence.currentValue != null)
            return false;
        if (cycle != null ? !cycle.equals(sequence.cycle) : sequence.cycle != null) return false;
        if (dataType != null ? !dataType.equals(sequence.dataType) : sequence.dataType != null) return false;
        if (increment != null ? !increment.equals(sequence.increment) : sequence.increment != null) return false;
        if (maxValue != null ? !maxValue.equals(sequence.maxValue) : sequence.maxValue != null) return false;
        if (minValue != null ? !minValue.equals(sequence.minValue) : sequence.minValue != null) return false;
        if (schema != null ? !schema.equals(sequence.schema) : sequence.schema != null) return false;
        if (sequenceName != null ? !sequenceName.equals(sequence.sequenceName) : sequence.sequenceName != null)
            return false;
        if (source != null ? !source.equals(sequence.source) : sequence.source != null) return false;
        if (startValue != null ? !startValue.equals(sequence.startValue) : sequence.startValue != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = sequenceName != null ? sequenceName.hashCode() : 0;
        result = 31 * result + (schema != null ? schema.hashCode() : 0);
        result = 31 * result + (catalog != null ? catalog.hashCode() : 0);
        result = 31 * result + (source != null ? source.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (startValue != null ? startValue.hashCode() : 0);
        result = 31 * result + (minValue != null ? minValue.hashCode() : 0);
        result = 31 * result + (maxValue != null ? maxValue.hashCode() : 0);
        result = 31 * result + (increment != null ? increment.hashCode() : 0);
        result = 31 * result + (cycle != null ? cycle.hashCode() : 0);
        result = 31 * result + (currentValue != null ? currentValue.hashCode() : 0);
        result = 31 * result + (dataType != null ? dataType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DbConsoleSequence{" +
                "sequenceName='" + sequenceName + '\'' +
                ", schema='" + schema + '\'' +
                ", catalog='" + catalog + '\'' +
                ", source=" + source +
                ", comment='" + comment + '\'' +
                ", startValue='" + startValue + '\'' +
                ", minValue='" + minValue + '\'' +
                ", maxValue='" + maxValue + '\'' +
                ", increment='" + increment + '\'' +
                ", cycle='" + cycle + '\'' +
                ", currentValue='" + currentValue + '\'' +
                ", dataType='" + dataType + '\'' +
                '}';
    }
}
