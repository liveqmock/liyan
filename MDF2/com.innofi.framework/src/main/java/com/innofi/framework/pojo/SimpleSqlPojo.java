/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.framework.pojo;

/**
 * 功能/ 模块：框架基础模块
 *
 * @author 刘名寓 liumy2009@126.com
 * @version 2.0.0 12-11-30
 *          简单SqlPojo
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
public class SimpleSqlPojo extends BasePojo{

    /**
     * SQL中选择的列
     */
    private String cols;

    /**
     * SQL中查找的表
     */
    private String tables;

    /**
     * 查询限制条件
     */
    private String restrictions;

    /**
     * Group By的字段
     */
    private String groupCols;

    /**
     * Order by的字段
     */
    private String orderCols;


    public String getCols() {
        return cols;
    }

    public void setCols(String cols) {
        this.cols = cols;
    }

    public String getTables() {
        return tables;
    }

    public void setTables(String tables) {
        this.tables = tables;
    }

    public String getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(String restrictions) {
        this.restrictions = restrictions;
    }

    public String getGroupCols() {
        return groupCols;
    }

    public void setGroupCols(String groupCols) {
        this.groupCols = groupCols;
    }

    public String getOrderCols() {
        return orderCols;
    }

    public void setOrderCols(String orderCols) {
        this.orderCols = orderCols;
    }

}
