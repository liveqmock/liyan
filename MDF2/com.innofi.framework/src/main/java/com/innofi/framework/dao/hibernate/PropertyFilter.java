package com.innofi.framework.dao.hibernate;

import com.innofi.framework.utils.validate.Validator;



/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @author liumy2009@126.com
 * @version 2.0
 *          对象属性过滤器
 * @found date: 2012-5-20
 * @found time: 下午3:47:07
 */
public class PropertyFilter {

	//约束条件 and和or
	private String restrictionType;
	
    //查询名称
    private String propertyName;

    //查询值
    private Object propertyValue;

    //匹配条件
    private String matchType;

    public String getRestrictionType() {
        return restrictionType;
    }

    public void setRestrictionType(String restrictionType) {
        this.restrictionType = restrictionType;
    }

    //可以扩展其他属性
    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public Object getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(Object propertyValue) {
        this.propertyValue = propertyValue;
    }

    public String getMatchType() {
        return matchType;
    }

    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }

    public PropertyFilter(String restrictionType , String propertyName, Object propertyValue ,String matchType){
        if(Validator.isEmpty(restrictionType)){
        	this.restrictionType = QueryConstants.RESTRICTION_TYPE_AND;
        }else{
        	this.restrictionType = restrictionType;
        }
    	if(Validator.isEmpty(matchType)){
            this.matchType = QueryConstants.EQUAL;
        }else{
        	this.matchType = matchType;
        }
        this.propertyName= propertyName;
        this.propertyValue = propertyValue;
    }

}
