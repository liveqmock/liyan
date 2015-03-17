package com.innofi.framework.utils.variable;

import org.apache.commons.lang.ObjectUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 支持多种数据类型的变量对象
 *
 * @author liumy
 * @date 2010-3-12上午09:17:37
 */
public class Variable implements Serializable, Cloneable {

    private int dataType;    //数据类型
    private Object value;   //数据值

    public Variable() {
        this.dataType = 0;
    }

    public Variable(int dataType) {
        this.dataType = dataType;
    }

    /**
     * 返回变量对象数据类型
     *
     * @return
     */
    public int getDataType() {
        return this.dataType;
    }

    /**
     * 设置数据类型
     *
     * @param dataType 数据类型int类型常量
     */
    public void setDataType(int dataType) {
        if (this.dataType == dataType) {
            return;
        }

        this.dataType = dataType;
    }

    /**
     * 设置数据类型
     *
     * @param dataType 数据类型名称
     */
    public void setDataType(String dataType) {
        setDataType(DataType.nameToType(dataType));
    }

    /**
     * 获得变量对象值
     *
     * @return Object类型变量对象值
     */
    public Object getValue() {
        return this.value;
    }

    /**
     * 设置变量对象值
     *
     * @param value Object类型变量对象值
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * 获得变量对象值，以String类型
     *
     * @return String类型的变量对象值
     */
    public String getString() {
        return VariableHelper.parseString(this.value);
    }

    /**
     * 以String类型设置变量对象值
     *
     * @param value String类型的变量对象值
     * @throws java.text.ParseException
     */
    public void setString(String value) {
        if (this.dataType == 0) {
            this.dataType = 1;
        }

        Object o = VariableHelper.toObject(value);
        if (this.dataType == 1) {
            this.value = o;
        } else
            this.value = VariableHelper.translate(this.dataType, o);
    }

    /**
     * 以byte类型返回变量对象值
     *
     * @return byte类型的变量值
     */
    public byte getByte() {
        return VariableHelper.parseByte(this.value);
    }

    /**
     * 以byte类型设置变量对象值
     *
     * @param value byte类型值
     * @throws java.text.ParseException
     */
    public void setByte(byte value) {
        if (this.dataType == 0) {
            this.dataType = 2;
        }

        Object o = VariableHelper.toObject(value);
        if (this.dataType == 2) {
            this.value = o;
        } else
            this.value = VariableHelper.translate(this.dataType, o);
    }

    /**
     * 以short类型返回变量对象值
     *
     * @return short类型值
     */
    public short getShort() {
        return VariableHelper.parseShort(this.value);
    }

    /**
     * 以short类型设置变量对象值
     *
     * @param value short类型值
     * @throws java.text.ParseException
     */
    public void setShort(short value) {
        if (this.dataType == 0) {
            this.dataType = 3;
        }

        Object o = VariableHelper.toObject(value);
        if (this.dataType == 3) {
            this.value = o;
        } else
            this.value = VariableHelper.translate(this.dataType, o);
    }

    /**
     * 以int类型获得变量对象值
     *
     * @return int类型值
     */
    public int getInt() {
        return VariableHelper.parseInt(this.value);
    }

    /**
     * 以int类型设置变量对象值
     *
     * @param value int类型值
     * @throws java.text.ParseException
     */
    public void setInt(int value) {
        if (this.dataType == 0) {
            this.dataType = 4;
        }

        Object o = VariableHelper.toObject(value);
        if (this.dataType == 4) {
            this.value = o;
        } else
            this.value = VariableHelper.translate(this.dataType, o);
    }

    /**
     * 获得long类型变量对象值
     *
     * @return long类型值
     */
    public long getLong() {
        return VariableHelper.parseLong(this.value);
    }

    /**
     * 以long类型设置变量对象值
     *
     * @param value long类型值
     * @throws java.text.ParseException
     */
    public void setLong(long value) {
        if (this.dataType == 0) {
            this.dataType = 5;
        }

        Object o = VariableHelper.toObject(value);
        if (this.dataType == 5) {
            this.value = o;
        } else
            this.value = VariableHelper.translate(this.dataType, o);
    }

    /**
     * 获得float类型的变量对象值
     *
     * @return float类型值
     */
    public float getFloat() {
        return VariableHelper.parseFloat(this.value);
    }

    public void setFloat(float value) {
        if (this.dataType == 0) {
            this.dataType = 6;
        }

        Object o = VariableHelper.toObject(value);
        if (this.dataType == 6) {
            this.value = o;
        } else
            this.value = VariableHelper.translate(this.dataType, o);
    }

    /**
     * 获得double类型的变量对象值
     *
     * @return double类型值
     */
    public double getDouble() {
        return VariableHelper.parseDouble(this.value);
    }

    /**
     * 以double类型值设置变量对象值
     *
     * @param value double类型值
     * @throws java.text.ParseException
     */
    public void setDouble(double value) {
        if (this.dataType == 0) {
            this.dataType = 7;
        }

        Object o = VariableHelper.toObject(value);
        if (this.dataType == 7) {
            this.value = o;
        } else
            this.value = VariableHelper.translate(this.dataType, o);
    }

    /**
     * 以BigDecimal类型获得变量对象值
     *
     * @return BigDecimal类型值
     */
    public BigDecimal getBigDecimal() {
        return VariableHelper.parseBigDecimal(this.value);
    }

    /**
     * 以BigDecimal类型值设置变量对象值
     *
     * @param value BigDecimal类型值
     * @throws java.text.ParseException
     */
    public void setBigDecimal(BigDecimal value) {
        if (this.dataType == 0) {
            this.dataType = 8;
        }

        Object o = VariableHelper.toObject(value);
        if (this.dataType == 8) {
            this.value = o;
        } else
            this.value = VariableHelper.translate(this.dataType, o);
    }

    /**
     * 以boolean类型获得变量对象值
     *
     * @return boolean类型值
     */
    public boolean getBoolean() {
        return VariableHelper.parseBoolean(this.value);
    }

    /**
     * 以boolean类型值设置变量对象值
     *
     * @param value boolean类型值
     * @throws java.text.ParseException
     */
    public void setBoolean(boolean value) {
        if (this.dataType == 0) {
            this.dataType = 9;
        }

        Object o = VariableHelper.toObject(value);
        if (this.dataType == 8) {
            this.value = o;
        } else
            this.value = VariableHelper.translate(this.dataType, o);
    }

    /**
     * 返回日期类型值
     *
     * @return 返回当前变量的值
     * @throws java.text.ParseException
     */
    public Date getDate() {
        return VariableHelper.parseDate(this.value);
    }

    /**
     * 为变量设置Date类型值
     *
     * @param value 日期类型值
     * @throws java.text.ParseException
     */
    public void setDate(Date value) {
        if (this.dataType == 0) {
            this.dataType = 10;
        }

        Object o = VariableHelper.toObject(value);
        if ((this.dataType == 10) || (this.dataType == 11) || (this.dataType == 12)) {
            this.value = o;
        } else
            this.value = VariableHelper.translate(this.dataType, o);
    }

    /**
     * 判断变量值是否为null
     *
     * @return 是true 不是false
     */
    public boolean isNull() {
        return (this.value == null);
    }

    /**
     * 将变量值设置为null
     */
    public void setNull() {
        this.value = null;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Variable) {
            return ObjectUtils.equals(this.value, ((Variable) obj).getValue());
        }
        return false;
    }

    public int hashCode() {
        if (this.value != null) {
            return this.value.hashCode();
        }
        return 0;
    }

    protected Object clone() throws CloneNotSupportedException {
        Variable o = (Variable) super.clone();
        o.setValue(getValue());
        return o;
    }

    public String toString() {
        return ((this.value == null) ? "Variable {null}" : this.value.toString());
    }

}