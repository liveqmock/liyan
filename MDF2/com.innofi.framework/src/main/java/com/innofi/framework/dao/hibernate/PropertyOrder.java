package com.innofi.framework.dao.hibernate;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @author liumy
 * @version 2.0
 *          对象属性排序对象
 * @found date: 2012-5-20
 * @found time: 下午3:47:07
 */
public class PropertyOrder {

    //属性名称
    private String propertyName;

    //排序规则
    private String orderRule;

    public PropertyOrder(String propertyName, String orderRule) {
        this.propertyName = propertyName;
        this.orderRule = orderRule;
    }

    /**
     * 获得排序属性名称
     *
     * @return
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * 获得排序规则
     *
     * @return
     */
    public String getOrderRule() {
        return orderRule;
    }

    /**
     * 设置排序属性名称
     *
     * @param propertyName 排序属性名称
     */
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    /**
     * 设置排序规则  @see com.innofi.framework.dao.hibernate.QueryConstants
     *
     * @param orderRule
     */
    public void setOrderRule(String orderRule) {
        this.orderRule = orderRule;
    }

}
