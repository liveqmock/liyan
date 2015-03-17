/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.framework.pojo;

/**
 * 功能/ 模块：框架基础模块
 *
 * @author 刘名寓 liumy2009@126.com
 * @version 2.0.0 12-11-30
 *          Dorado前端UI字段展现属性
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
public class UIFieldPojo extends BasePojo{

    /**
     * 属性名称
     */
    private String propertyName;

    /**
     * 属性Form Label名称
     */
    private String propertyLabel;

    /**
     * 属性Form输入框默认提示信息
     */
    private String propertyBlankText;

    /**
     * 属性Form输入框鼠标移入后的提示信息
     */
    private String propertyTip;

    /**
     * 获得属性名称
     * @return
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * 设置属性名称
     * @return
     */
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    /**
     * 属性Form Label名称
     */
    public String getPropertyLabel() {
        return propertyLabel;
    }

    /**
     * 设置Form Label名称
     */
    public void setPropertyLabel(String propertyLabel) {
        this.propertyLabel = propertyLabel;
    }

    /**
     * 获取属性Form输入框默认提示信息
     */
    public String getPropertyBlankText() {
        return propertyBlankText;
    }

    /**
     * 设置属性Form输入框默认提示信息
     */
    public void setPropertyBlankText(String propertyBlankText) {
        this.propertyBlankText = propertyBlankText;
    }

    /**
     * 获取属性Form输入框鼠标移入后的提示信息
     */
    public String getPropertyTip() {
        return propertyTip;
    }

    /**
     * 设置属性Form输入框鼠标移入后的提示信息
     */
    public void setPropertyTip(String propertyTip) {
        this.propertyTip = propertyTip;
    }

}
