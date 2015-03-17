package com.innofi.framework.utils.reflect;

import com.bstek.dorado.data.entity.EntityUtils;
import com.innofi.framework.exception.BusinessRuntimeException;
import com.innofi.framework.utils.validate.Validator;
import com.innofi.framework.utils.variable.DataType;
import com.innofi.framework.utils.variable.VariableHelper;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @author liumy2009@126.com
 * @version 2.0
 * @found date: 12-6-11
 * @found time: 下午11:31
 * <p/>
 * 反射工具类.
 * <p/>
 * 提供访问私有变量,获取泛型类型Class,提取集合中元素的属性,转换字符串到对象等Util函数.
 * <p/>
 */
public class ReflectionUtil {

    private static Logger logger = LoggerFactory.getLogger(ReflectionUtil.class);

    static {
        DateConverter dc = new DateConverter();
        dc.setUseLocaleFormat(true);
        dc.setPatterns(new String[]{"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"});
        ConvertUtils.register(dc, Date.class);
    }

    /**
     * 调用Getter方法.
     */
    public static Object invokeGetterMethod(Object target, String propertyName) {
        String getterMethodName = "get" + StringUtils.capitalize(propertyName);
        return invokeMethod(target, getterMethodName, new Class[]{}, new Object[]{});
    }

    /**
     * 调用Setter方法.使用value的Class来查找Setter方法.
     */
    public static void invokeSetterMethod(Object target, String propertyName, Object value) {
        invokeSetterMethod(target, propertyName, value, null);
    }

    /**
     * 调用Setter方法.
     *
     * @propertyType 用于查找Setter方法, 为空时使用value的Class替代.
     */
    public static void invokeSetterMethod(Object target, String propertyName, Object value, Class<?> propertyType) {
        if(value!=null){

            Class<?> type = propertyType != null ? propertyType : value.getClass();

            String setterMethodName = "set" + StringUtils.capitalize(propertyName);
            invokeMethod(target, setterMethodName,new Class[]{type},new Object[]{value});
        }
    }

    /**
     * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数.
     */
    public static Object getFieldValue(final Object object,
                                       final String fieldName) {
        Field field = getDeclaredField(object, fieldName);

        if (field == null) {
            throw new IllegalArgumentException("Could not find field ["
                    + fieldName + "] on target [" + object + "]");
        }

        makeAccessible(field);

        Object result = null;
        try {
            result = field.get(object);
        } catch (IllegalAccessException e) {
            logger.error("不可能抛出的异常{}", e.getMessage());
        }
        return result;
    }

    /**
     * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
     */
    public static void setFieldValue(Object object, String fieldName, Object value) {
    	fieldName = fieldName.substring(0,1).toLowerCase()+fieldName.substring(1);
        Field field = getDeclaredField(object, fieldName);

        if (field != null) {
            makeAccessible(field);
            try {
                field.set(object, value);
            } catch (Exception e) {
                value = VariableHelper.translate(field.getType().getName(),value);
                try {
                    field.set(object, value);
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                }
                //logger.error("不可能抛出的异常:{}", e.getMessage());
            }
        }
    }

    /**
     * 直接调用对象方法, 无视private/protected修饰符.
     */
    public static Object invokeMethod(final Object object, final String methodName, final Class<?>[] parameterTypes, final Object[] parameters) {
        Method method = getDeclaredMethod(object, methodName, parameterTypes);
        if (method==null&&parameterTypes!=null) {
            Class type = parameterTypes[0];
            if(type!=null&&type.getName().startsWith("java.sql")){
                method= getDeclaredMethod(object,methodName,new Class[]{Date.class});
            }else if(type!=null&&type.getName().startsWith("java.math")){
            	method= getDeclaredMethod(object,methodName,new Class[]{BigDecimal.class});
            }
        }

        if(method==null){
            throw new BusinessRuntimeException("未在对象中找到名称为["+methodName+"]的方法");
        }

        method.setAccessible(true);

        try {
            return method.invoke(object, parameters);
        } catch (Exception e) {
            throw convertReflectionExceptionToUnchecked(e);
        }
    }

    /**
     * 循环向上转型, 获取对象的DeclaredField.
     * <p/>
     * 如向上转型到Object仍无法找到, 返回null.
     */
    public static Field getDeclaredField(final Object object,
                                            final String fieldName) {
        Assert.notNull(object, "object不能为空");
        Assert.hasText(fieldName, "fieldName");
        for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                // Field不在当前类定义,继续向上转型
            }
        }
        return null;
    }

    /**
     * 强行设置Field可访问.
     */
    protected static void makeAccessible(final Field field) {
        if (!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
            field.setAccessible(true);
        }
    }

    /**
     * 循环向上转型,获取对象的DeclaredMethod.
     * <p/>
     * 如向上转型到Object仍无法找到, 返回null.
     */
    protected static Method getDeclaredMethod(Object object, String methodName,Class<?>[] parameterTypes) {
        Assert.notNull(object, "object不能为空");

        for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException e) {
                // Method不在当前类定义,继续向上转型
            }
        }
        return null;
    }

    /**
     * 通过反射,获得Class定义中声明的父类的泛型参数的类型. 如无法找到, 返回Object.class. eg. public UserDao
     * extends IHibernateDao<User>
     *
     * @param clazz The class to introspect
     * @return the first generic declaration, or Object.class if cannot be
     *         determined
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getSuperClassGenricType(final Class clazz) {
        return getSuperClassGenricType(clazz, 0);
    }

    /**
     * 通过反射,获得Class定义中声明的泛型参数的类型,如无法找到, 返回Object.class.
     * extends IHibernateDao<User>
     *
     * @param clazz The class to introspect
     * @return the first generic declaration, or Object.class if cannot be
     *         determined
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getClassGenricType(final Class clazz) {
        return getClassGenricType(clazz, 0);
    }

    /**
     * 通过反射,获得定义Class时声明的泛型参数的类型. 如无法找到, 返回Object.class.
     * <p/>
     * 如public UserDao extends IHibernateDao<User,Long>
     *
     * @param clazz clazz The class to introspect
     * @param index the Index of the generic ddeclaration,start from 0.
     * @return the index generic declaration, or Object.class if cannot be determined
     */
    @SuppressWarnings("unchecked")
    public static Class getClassGenricType(final Class clazz, final int index) {

        Type genType = clazz;

        if (!(genType instanceof ParameterizedType)) {
            logger.warn(clazz.getSimpleName() + "'s not ParameterizedType");
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            logger.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: " + params.length);
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            logger.warn(clazz.getSimpleName() + " not set the actual class on generic parameter");
            return Object.class;
        }

        return (Class) params[index];
    }

    /**
     * 通过反射,获得定义Class时声明的父类的泛型参数的类型. 如无法找到, 返回Object.class.
     * <p/>
     * 如public UserDao extends IHibernateDao<User,Long>
     *
     * @param clazz clazz The class to introspect
     * @param index the Index of the generic ddeclaration,start from 0.
     * @return the index generic declaration, or Object.class if cannot be
     *         determined
     */
    @SuppressWarnings("unchecked")
    public static Class getSuperClassGenricType(final Class clazz, final int index) {

        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            logger.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            logger.warn("Index: " + index + ", Size of "
                    + clazz.getSimpleName() + "'s Parameterized Type: "
                    + params.length);
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            logger.warn(clazz.getSimpleName()
                    + " not set the actual class on superclass generic parameter");
            return Object.class;
        }

        return (Class) params[index];
    }

    /**
     * 提取集合中的对象的属性(通过getter函数), 组合成List.
     *
     * @param collection   来源集合.
     * @param propertyName 要提取的属性名.
     */
    @SuppressWarnings("unchecked")
    public static List convertElementPropertyToList(
            final Collection collection, final String propertyName) {
        List list = new ArrayList();

        try {
            for (Object obj : collection) {
                list.add(PropertyUtils.getProperty(obj, propertyName));
            }
        } catch (Exception e) {
            throw convertReflectionExceptionToUnchecked(e);
        }

        return list;
    }

    /**
     * 提取集合中的对象的属性(通过getter函数), 组合成由分割符分隔的字符串.
     *
     * @param collection   来源集合.
     * @param propertyName 要提取的属性名.
     * @param separator    分隔符.
     */
    @SuppressWarnings("unchecked")
    public static String convertElementPropertyToString(
            final Collection collection, final String propertyName,
            final String separator) {
        List list = convertElementPropertyToList(collection, propertyName);
        return StringUtils.join(list, separator);
    }

    /**
     * 转换字符串到相应类型.
     *
     * @param value  待转换的字符串
     * @param toType 转换目标类型
     */
    public static Object convertStringToObject(String value, Class<?> toType) {
        try {
            return ConvertUtils.convert(value, toType);
        } catch (Exception e) {
            throw convertReflectionExceptionToUnchecked(e);
        }
    }

    /**
     * 将反射时的checked exception转换为unchecked exception.
     */
    public static RuntimeException convertReflectionExceptionToUnchecked(
            Exception e) {
        if (e instanceof IllegalAccessException
                || e instanceof IllegalArgumentException
                || e instanceof NoSuchMethodException) {
            return new IllegalArgumentException("Reflection Exception.", e);
        } else if (e instanceof InvocationTargetException) {
            return new RuntimeException("Reflection Exception.",
                    ((InvocationTargetException) e).getTargetException());
        } else if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        }
        return new RuntimeException("Unexpected Checked Exception.", e);
    }

    /**
     * 将Pojo对象转换为Map对象
     *
     * @param obj
     * @param map
     */
    public static void copyObjectToMap(Object obj, Map map) {

        BeanInfo bi = null;
        try {
            bi = Introspector.getBeanInfo(obj.getClass());
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        PropertyDescriptor[] pd = null;
        if (bi != null) {
            pd = bi.getPropertyDescriptors();
        }
        for (int i = 0; i < pd.length; i++) {

            if (pd[i].getName().equals("class"))
                continue;

            Method readMethod = pd[i].getReadMethod();
            Object value = null;
            try {
                value = readMethod.invoke(obj, new Object[]{});
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            map.put(pd[i].getName(), value);
        }
    }

    /**
     * 将pojo对象复制的属性复制到另一个实体对象
     *
     * @param src
     * @param traget
     */
    public static void copyObjectToObject(Object src, Object traget) {

        BeanInfo bi = null;
        try {
            bi = Introspector.getBeanInfo(traget.getClass());
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        PropertyDescriptor[] pd = null;
        if (bi != null) {
            pd = bi.getPropertyDescriptors();
        }
        for (int i = 0; i < pd.length; i++) {
            String name = pd[i].getName();
            if (name.equals("class"))
                continue;
            String readMethodName = pd[i].getReadMethod().getName();
            Object value = null;
            try {
                value = src.getClass()
                        .getMethod(readMethodName, new Class[]{})
                        .invoke(src, new Object[]{});
            } catch (IllegalArgumentException e1) {
            } catch (SecurityException e1) {
            } catch (IllegalAccessException e1) {
            } catch (InvocationTargetException e1) {
            } catch (NoSuchMethodException e1) {
            } catch (Exception e) {
            }

            if (value != null) {

                Method method = pd[i].getWriteMethod();
                try {

                    value = changeObjectType(value,
                            method.getParameterTypes()[0]);
                    if (value.getClass() == method.getParameterTypes()[0]) {
                        method.invoke(traget, new Object[]{value});
                    } else {
                        method.invoke(traget, new Object[]{value});
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

            }

        }
    }


    /**
     * 将map对象转换为相应的实体对象
     *
     * @param map
     * @param obj
     */
    public static void copyMapToObject(Map map, Object obj) {

        BeanInfo bi = null;
        try {
            bi = Introspector.getBeanInfo(obj.getClass());
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        PropertyDescriptor[] pd = null;
        if (bi != null) {
            pd = bi.getPropertyDescriptors();
        }
        for (int i = 0; i < pd.length; i++) {
            String name = pd[i].getName();
            Object value = map.get(name);
            if (value == null)
                value = map.get(name.toLowerCase());
            if (value == null)
                value = map.get(name.toUpperCase());
            if (value != null) {
                Method method = pd[i].getWriteMethod();
                try {
                    value = changeObjectType(value,
                            method.getParameterTypes()[0]);
                    if (value.getClass() == method.getParameterTypes()[0])
                        method.invoke(obj, new Object[]{value});
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

            }

        }
    }


    /**
     * 将Map复制到另一个Map中
     *
     * @param map1
     * @param map2
     */
    public static void copyMapToMap(Map map1, Map map2) {
        for (Iterator it = map1.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry entry = (Map.Entry) it.next();
            map2.put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 从源复制到目标对象不为空或者""的属性
     * @param source 源对象
     * @param target 目标对象
     */
    public static void copyProperties(Object source, Object target) {
        Class clazz = source.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field filed : fields) {
            Object value = ReflectionUtil.getFieldValue(source, filed.getName());
            if (value != null && !"".equals(value.toString())) {
                ReflectionUtil.setFieldValue(target, filed.getName(), value);
            }
        }
    }

    /**
     * 将List中的对象转换为Object[]数据组
     *
     * @param list
     * @return
     */
    public static Object[] getObjectArrayFromList(List list) {
        Object[] objs = null;
        if (list == null)
            objs = new Object[]{};
        else {
            objs = new Object[list.size()];
            for (int i = 0; i < list.size(); i++) {
                objs[i] = list.get(i);
            }
        }
        return objs;
    }

    private static Object changeObjectType(Object object, Class clazz) {
        if (object.getClass() == clazz)
            return object;

        if (!object.getClass().getName().equals("java.lang.String"))
            return object;

        String str = (String) object;

        if (clazz.getName().equals("java.lang.Integer")) {
            try {
                return new Integer(str);
            } catch (Exception e) {
                return object;
            }
        }
        if (clazz.getName().equals("java.lang.Long")) {
            try {
                return new Long(str);
            } catch (Exception e) {
                return object;
            }
        }
        if (clazz.getName().equals("java.math.BigDecimal")) {
            try {
                return new java.math.BigDecimal(str);
            } catch (Exception e) {
                return object;
            }
        }
        if (clazz.getName().equals("java.lang.Double")) {
            try {
                return new Double(str);
            } catch (Exception e) {
                return object;
            }
        }
        if (clazz.getName().equals("java.util.Date")) {
            try {
                java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat(
                        "yyyy-MM-dd");
                return formater.parse(str);
            } catch (Exception e) {
                return object;
            }
        }
        if (clazz.getName().equals("java.sqldialect.Date")) {
            try {
                java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat(
                        "yyyy-MM-dd");
                return new java.sql.Date(formater.parse(str).getTime());
            } catch (Exception e) {
                return object;
            }
        }

        return object;
    }
}
