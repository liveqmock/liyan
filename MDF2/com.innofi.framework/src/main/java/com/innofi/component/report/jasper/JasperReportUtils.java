/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.jasper;

import java.lang.reflect.Modifier;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

/**
 * JasperReport数据加载辅助类，提供对SpringBean方式加载数据时方法的动态匹配功能
 * 
 * @author tode.yu@bstek.com
 * @version 1.0
 */
public class JasperReportUtils {

	/**
	 * 根据指定的类及其方法，获取方法定义的参数类型和参数名称
	 * 
	 * @param targetClassName 类名
	 * @param targetMethodName 方法名
	 * @return 返回JasperReportAnalzyAssistBean对象
	 */
	public static JasperReportAnalzyAssistBean getMethodVariableNames(String targetClassName, String targetMethodName) {  
	    int index = targetClassName.indexOf("$$");
	    String className = targetClassName;
	    if(index != -1)
	    	className = targetClassName.substring(0,index);
	    Class<?> clazz = null;  
	    try {  
	    	clazz = Class.forName(className);  
	    } catch (ClassNotFoundException e) {  
	        e.printStackTrace();  
	    }  
	    ClassPool pool = ClassPool.getDefault();  

	    pool.insertClassPath(new ClassClassPath(clazz));  
	    CtClass cc;  
	    CtMethod cm = null;
	    try {  
	        cc = pool.get(clazz.getName());  
	        cm = cc.getDeclaredMethod(targetMethodName);  
	    } catch (NotFoundException e) {  
	        e.printStackTrace();  
	    }
	  
	    MethodInfo methodInfo = cm.getMethodInfo();  
	    CodeAttribute codeAttribute = methodInfo.getCodeAttribute();  
	    LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);  
	    int length = 0;
	    CtClass[] cs = null;
	    try {  
	    	length = cm.getParameterTypes().length; 
	    	
	  	   cs = cm.getParameterTypes();
	    } catch (NotFoundException e) {  
	        e.printStackTrace();  
	    }  
	    int staticIndex = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;  
	    JasperReportAnalzyAssistBean assist = new JasperReportAnalzyAssistBean();
	    for (int i = 0; i < length; i++)  {
	    	try {
	    		if(cs[i].isPrimitive()){
	    			assist.add(attr.variableName(i+staticIndex), getPrimitiveType(cs[i]));
	    		}else{
	    			assist.add(attr.variableName(i+staticIndex), Class.forName(cs[i].getName()));
	    		}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
	    }
	    return assist;  
	} 
	
	/**
	 * 如果参数类型是基本类型，需要做特殊处理
	 * 
	 * @param typeName
	 * @return 返回基本类型的class
	 */
	@SuppressWarnings("rawtypes")
	private static Class getPrimitiveType(CtClass typeName){
		if(typeName == CtClass.booleanType){
			return boolean.class;
		}else if(typeName == CtClass.byteType){
			return byte.class;
		}else if(typeName == CtClass.charType){
			return char.class;
		}else if(typeName == CtClass.doubleType){
			return double.class;
		}else if(typeName == CtClass.floatType){
			return float.class;
		}else if(typeName == CtClass.intType){
			return int.class;
		}else if(typeName ==CtClass.longType){
			return long.class;
		}else {
			return short.class;
		}
	}
}
