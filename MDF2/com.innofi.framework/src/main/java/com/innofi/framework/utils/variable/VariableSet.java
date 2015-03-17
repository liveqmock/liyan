package com.innofi.framework.utils.variable;

import com.innofi.framework.utils.collection.ObjectCollection;
import com.innofi.framework.utils.collection.ObjectList;
import org.apache.commons.lang.ClassUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

/**
 * 可支持多种类型的对象集合。其功能类似于一个功能增强了的Map
 * Copyright (c) 2010 北京名晟信息技术有限公司
 * all rights reserved.
 * author: liumy
 * found date: 2010-11-23
 * found time: 20:40:56
 */
public class VariableSet implements Serializable, Cloneable {

    private ObjectCollection values;

    public VariableSet() {
        this.values = new ObjectList();
    }

    /**
     * 从对象集合中获得对象,如果指定key对象不存在则添加指定key对象
     *
     * @param name 对象key
     * @return 变量对象
     */
    private Variable ensureVariableExist(String name) {
        Variable v = getVariable(name);
        if (v == null) {
            v = new Variable();
            setVariable(name, v);
        }
        return v;
    }

    /**
     * 测试指定index是否超出对象集合范围
     *
     * @param index 索引
     */
    private void varifyIndex(int index) {
        if ((index < 0) || (index >= this.values.size())) throw new ArrayIndexOutOfBoundsException(index);
    }

    /**
     * 测试指定名称key的对象是否存在
     *
     * @param name key名称
     */
    private void varifyName(String name) {
        if (!(exists(name)))
            throw new RuntimeException("VariablePojo '" + name + "' does not exist!");
    }

    /**
     * 判断指定key名称的对象是否存在，存在返回true，否则false
     *
     * @param name 判断指定对象在集合中是否存在
     * @return 存在返回true，不存在返回false
     */
    public boolean exists(String name) {
        return (getVariable(name) != null);
    }

    /**
     * 将本集合中的所有数据全部指派到给定的另一个集合中
     *
     * @param variables
     */
    public void assign(VariableSet variables) {
        int count = variables.count();
        for (int i = 0; i < count; ++i) {
            String name = variables.indexToName(i);
            if (!(exists(name))) {
                setDataType(name, variables.getDataType(i));
            }
            Object value = variables.getValue(i);
            if ((getDataType(i) == 0) && ("".equals(value))) {
                value = null;
            }
            setValue(name, value);
        }
    }

    /**
     * 清除本集合中所有的数据.
     */
    public void clear() {
        this.values.removeAll();
    }

    /**
     * 根据索引号并按Variable类型读取数据
     *
     * @param index
     * @return
     */
    public Variable getVariable(int index) {
        return ((Variable) this.values.get(index));
    }

    /**
     * 根据键值并按Object类型读取数据
     *
     * @param name
     * @return
     */
    public Variable getVariable(String name) {
        return ((Variable) this.values.get(name));
    }

    /**
     * 根据索引号返回默认数据类型
     *
     * @param index
     * @return
     */
    public int getDataType(int index) {
        varifyIndex(index);
        return getVariable(index).getDataType();
    }

    /**
     * 根据索引号设置默认数据类型
     *
     * @param index
     * @param dataType
     */
    public void setDataType(int index, int dataType) {
        varifyIndex(index);
        getVariable(index).setDataType(dataType);
    }

    /**
     * 根据索引号设置默认数据类型
     *
     * @param index
     * @param dataType
     */
    public void setDataType(int index, String dataType) {
        varifyIndex(index);
        getVariable(index).setDataType(dataType);
    }

    /**
     * 根据键值返回默认数据类型
     *
     * @param name
     * @return
     */
    public int getDataType(String name) {
        varifyName(name);
        return getVariable(name).getDataType();
    }

    /**
     * 根据键值设置默认数据类型
     *
     * @param name
     * @param dataType
     */
    public void setDataType(String name, int dataType) {
        Variable v = ensureVariableExist(name);
        v.setDataType(dataType);
    }

    /**
     * 根据键值设置默认数据类型
     *
     * @param name
     * @param dataType
     */
    public void setDataType(String name, String dataType) {
        Variable v = ensureVariableExist(name);
        v.setDataType(dataType);
    }

    /**
     * 根据索引号并按Object类型读取数据
     *
     * @param index
     * @return
     */
    public Object getValue(int index) {
        Variable Variable = getVariable(index);
        if (Variable != null) {
            return Variable.getValue();
        }
        return null;
    }

    /**
     * 根据索引号并按BigDecimel类型读取数据
     *
     * @param index
     * @return
     */
    public BigDecimal getBigDecimal(int index) {
        Variable Variable = getVariable(index);
        if (Variable != null) {
            return Variable.getBigDecimal();
        }
        return null;
    }

    /**
     * 根据索引号并按boolean类型读取数据
     *
     * @param index
     * @return
     */
    public boolean getBoolean(int index) {
        Variable variables = getVariable(index);
        if (variables != null) {
            return variables.getBoolean();
        }
        return false;
    }

    /**
     * 根据索引号并按byte类型读取数据
     *
     * @param index
     * @return
     */
    public byte getByte(int index) {
        Variable Variable = getVariable(index);
        if (Variable != null) {
            return Variable.getByte();
        }
        return 0;
    }

    /**
     * 根据索引号并按Date类型读取数据
     *
     * @param index
     * @return
     * @throws java.text.ParseException
     */
    public Date getDate(int index) throws ParseException {
        Variable Variable = getVariable(index);
        if (Variable != null) {
            return Variable.getDate();
        }
        return null;
    }

    /**
     * 根据索引号并按double类型读取数据
     *
     * @param index
     * @return
     */
    public double getDouble(int index) {
        Variable Variable = getVariable(index);
        if (Variable != null) {
            return Variable.getDouble();
        }
        return 0.0D;
    }

    /**
     * 根据索引号并按float类型读取数据
     *
     * @param index
     * @return
     */
    public float getFloat(int index) {
        Variable Variable = getVariable(index);
        if (Variable != null) {
            return Variable.getFloat();
        }
        return 0.0F;
    }

    /**
     * 根据索引号并按int类型读取数据
     *
     * @param index
     * @return
     */
    public int getInteger(int index) {
        Variable Variable = getVariable(index);
        if (Variable != null) {
            return Variable.getInt();
        }
        return 0;
    }

    /**
     * 根据索引号并按long类型读取数据
     *
     * @param index
     * @return
     */
    public long getLong(int index) {
        Variable Variable = getVariable(index);
        if (Variable != null) {
            return Variable.getLong();
        }
        return 0L;
    }


    /**
     * 根据索引号并按short类型读取数据
     *
     * @param index
     * @return
     */
    public short getShort(int index) {
        Variable Variable = getVariable(index);
        if (Variable != null) {
            return Variable.getShort();
        }
        return 0;
    }

    /**
     * 根据索引号并按String类型读取数据
     *
     * @param index
     * @return
     */
    public String getString(int index) {
        Variable Variable = getVariable(index);
        if (Variable != null) {
            return Variable.getString();
        }
        return null;
    }

    /**
     * 根据索引号设置Object类型的数据
     *
     * @param index
     * @return
     */
    public void setValue(int index, Object value) {
        varifyIndex(index);
        getVariable(index).setValue(value);
    }

    /**
     * 根据索引号设置BigDecimal类型的数据
     *
     * @param index
     * @param value
     * @throws java.text.ParseException
     */
    public void setBigDecimal(int index, BigDecimal value) throws ParseException {
        varifyIndex(index);
        getVariable(index).setBigDecimal(value);
    }

    /**
     * 根据索引号设置boolean类型的数据
     *
     * @param index
     * @param value
     * @throws java.text.ParseException
     */
    public void setBoolean(int index, boolean value) throws ParseException {
        varifyIndex(index);
        getVariable(index).setBoolean(value);
    }

    /**
     * 根据索引号设置byte类型的数据
     *
     * @param index
     * @param value
     * @throws java.text.ParseException
     */
    public void setByte(int index, byte value) throws ParseException {
        varifyIndex(index);
        getVariable(index).setByte(value);
    }

    /**
     * 根据索引号设置Date类型的数据
     *
     * @param index
     * @param value
     * @throws java.text.ParseException
     */
    public void setDate(int index, Date value) throws ParseException {
        varifyIndex(index);
        getVariable(index).setDate(value);
    }

    /**
     * 根据索引号设置double类型的数据
     *
     * @param index
     * @param value
     * @throws java.text.ParseException
     */
    public void setDouble(int index, double value) throws ParseException {
        varifyIndex(index);
        getVariable(index).setDouble(value);
    }

    /**
     * 根据索引号设置float类型的数据
     *
     * @param index
     * @param value
     * @throws java.text.ParseException
     */
    public void setFloat(int index, float value) throws ParseException {
        varifyIndex(index);
        getVariable(index).setFloat(value);
    }

    /**
     * 根据索引号设置int类型的数据
     *
     * @param index
     * @param value
     * @throws java.text.ParseException
     */
    public void setInteger(int index, int value) throws ParseException {
        varifyIndex(index);
        getVariable(index).setInt(value);
    }

    /**
     * 根据索引号设置long类型的数据
     *
     * @param index
     * @param value
     * @throws java.text.ParseException
     */
    public void setLong(int index, long value) throws ParseException {
        varifyIndex(index);
        getVariable(index).setLong(value);
    }

    /**
     * 根据索引号设置short类型的数据
     *
     * @param index
     * @param value
     * @throws java.text.ParseException
     */
    public void setShort(int index, short value) throws ParseException {
        varifyIndex(index);
        getVariable(index).setShort(value);
    }

    /**
     * 根据索引号设置String类型的数据
     *
     * @param index
     * @param value
     * @throws java.text.ParseException
     */
    public void setString(int index, String value) throws ParseException {
        varifyIndex(index);
        getVariable(index).setString(value);
    }

    /**
     * 根据键值并按Object类型读取数据
     *
     * @param name
     * @return
     */
    public Object getValue(String name) {
        Variable Variable = getVariable(name);
        if (Variable != null) {
            return Variable.getValue();
        }
        return null;
    }

    /**
     * 根据键值并按BigDecimal类型读取数据
     *
     * @param name
     * @return
     */
    public BigDecimal getBigDecimal(String name) {
        Variable Variable = getVariable(name);
        if (Variable != null) {
            return Variable.getBigDecimal();
        }
        return null;
    }

    /**
     * 根据键值并按boolean类型读取数据
     *
     * @param name
     * @return
     */
    public boolean getBoolean(String name) {
        Variable Variable = getVariable(name);
        if (Variable != null) {
            return Variable.getBoolean();
        }
        return false;
    }

    /**
     * 根据键值并按byte类型读取数据
     *
     * @param name
     * @return
     */
    public byte getByte(String name) {
        Variable Variable = getVariable(name);
        if (Variable != null) {
            return Variable.getByte();
        }
        return 0;
    }

    /**
     * 根据键值并按Date类型读取数据
     *
     * @param name
     * @return
     * @throws java.text.ParseException
     */
    public Date getDate(String name) throws ParseException {
        Variable Variable = getVariable(name);
        if (Variable != null) {
            return Variable.getDate();
        }
        return null;
    }

    /**
     * 根据键值并按double类型读取数据
     *
     * @param name
     * @return
     */
    public double getDouble(String name) {
        Variable Variable = getVariable(name);
        if (Variable != null) {
            return Variable.getDouble();
        }
        return 0.0D;
    }

    /**
     * 根据键值并按float类型读取数据
     *
     * @param name
     * @return
     */
    public float getFloat(String name) {
        Variable Variable = getVariable(name);
        if (Variable != null) {
            return Variable.getFloat();
        }
        return 0.0F;
    }

    /**
     * 根据键值并按int类型读取数据
     *
     * @param name
     * @return
     */
    public int getInteger(String name) {
        Variable Variable = getVariable(name);
        if (Variable != null) {
            return Variable.getInt();
        }
        return 0;
    }

    /**
     * 根据键值并按long类型读取数据
     *
     * @param name
     * @return
     */
    public long getLong(String name) {
        Variable Variable = getVariable(name);
        if (Variable != null) {
            return Variable.getLong();
        }
        return 0L;
    }

    /**
     * 根据键值并按short类型读取数据
     *
     * @param name
     * @return
     */
    public short getShort(String name) {
        Variable Variable = getVariable(name);
        if (Variable != null) {
            return Variable.getShort();
        }
        return 0;
    }

    /**
     * 根据键值并按String类型读取数据
     *
     * @param name
     * @return
     */
    public String getString(String name) {
        Variable Variable = getVariable(name);
        if (Variable != null) {
            return Variable.getString();
        }
        return null;
    }

    /**
     * 根据键值设置Variable类型的数据
     *
     * @param name
     * @param v
     */
    public void setVariable(String name, Variable v) {
        this.values.forceAdd(name, v);
    }

    /**
     * 根据键值设置Object类型的数据
     *
     * @param name
     * @param value
     */
    public void setValue(String name, Object value) {
        Variable v = ensureVariableExist(name);
        v.setValue(value);
    }

    /**
     * 根据键值设置BigDecimal类型的数据
     *
     * @param name
     * @param value
     * @throws java.text.ParseException
     */
    public void setBigDecimal(String name, BigDecimal value) throws ParseException {
        Variable v = ensureVariableExist(name);
        v.setBigDecimal(value);
    }

    /**
     * 根据键值设置boolean类型的数据
     *
     * @param name
     * @param value
     */
    public void setBoolean(String name, boolean value) {
        Variable v = ensureVariableExist(name);
        v.setBoolean(value);
    }

    /**
     * 根据键值设置byte类型的数据
     *
     * @param name
     * @param value
     */
    public void setByte(String name, byte value) {
        Variable v = ensureVariableExist(name);
        v.setByte(value);
    }

    /**
     * 根据键值设置Date类型的数据
     *
     * @param name
     * @param value
     */
    public void setDate(String name, Date value) {
        Variable v = ensureVariableExist(name);
        v.setDate(value);
    }

    /**
     * 根据键值设置double类型的数据
     *
     * @param name
     * @param value
     */
    public void setDouble(String name, double value) {
        Variable v = ensureVariableExist(name);
        v.setDouble(value);
    }

    /**
     * 根据键值设置float类型的数据
     *
     * @param name
     * @param value
     */
    public void setFloat(String name, float value) {
        Variable v = ensureVariableExist(name);
        v.setFloat(value);
    }

    /**
     * 根据键值设置int类型的数据
     *
     * @param name
     * @param value
     */
    public void setInteger(String name, int value) {
        Variable v = ensureVariableExist(name);
        v.setInt(value);
    }

    /**
     * 根据键值设置long类型的数据
     *
     * @param name
     * @param value
     */
    public void setLong(String name, long value) {
        Variable v = ensureVariableExist(name);
        v.setLong(value);
    }

    /**
     * 根据键值设置String类型的数据
     *
     * @param name
     * @param value
     */
    public void setShort(String name, short value) {
        Variable v = ensureVariableExist(name);
        v.setShort(value);
    }

    /**
     * 根据键值设置String类型的数据
     *
     * @param name
     * @param value
     */
    public void setString(String name, String value) {
        Variable v = ensureVariableExist(name);
        v.setString(value);
    }

    /**
     * 判断给定索引号下的数值是否为空(null)
     *
     * @param index
     * @return
     */
    public boolean isNull(int index) {
        Variable v = getVariable(index);
        if (v != null) {
            return v.isNull();
        }
        return true;
    }

    /**
     * 设置给定索引号下的数值为空(null)
     *
     * @param index
     */
    public void setNull(int index) {
        varifyIndex(index);
        getVariable(index).setNull();
    }

    /**
     * 判断给定键值下的数值是否为空(null)
     *
     * @param name
     * @return
     */
    public boolean isNull(String name) {
        Variable v = getVariable(name);
        if (v != null) {
            return v.isNull();
        }
        return true;
    }


    /**
     * 设置给定键值下的数值为空(null)
     *
     * @param name
     */
    public void setNull(String name) {
        Variable v = ensureVariableExist(name);
        v.setNull();
    }

    /**
     * 删除给定索引号及其下的数值.
     *
     * @param index
     */
    public void remove(int index) {
        this.values.remove(index);
    }


    /**
     * 删除给定键值及其下的数值.
     *
     * @param name
     */
    public void remove(String name) {
        this.values.remove(name);
    }

    /**
     * 返回集合中数值的总数量.
     *
     * @return
     */
    public int count() {
        return this.values.size();
    }

    /**
     * 返回给定的索引号对应的键值.
     *
     * @param index
     * @return
     */
    public String indexToName(int index) {
        return ((String) this.values.getKey(index));
    }

    public Object clone() throws CloneNotSupportedException {
        VariableSet o = (VariableSet) super.clone();
        ObjectCollection vs = new ObjectList();
        int vcount = this.values.size();
        for (int i = 0; i < vcount; ++i) {
            vs.add(this.values.getKey(i), ((Variable) this.values.get(i)).clone());
        }
        o.values = vs;
        return o;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof VariableSet) {
            return this.values.equals(((VariableSet) obj).values);
        }

        return false;
    }

    public int hashCode() {
        return this.values.hashCode();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(ClassUtils.getShortClassName(super.getClass()));
        sb.append(":").append(this.values.toString());
        return sb.toString();
    }

}