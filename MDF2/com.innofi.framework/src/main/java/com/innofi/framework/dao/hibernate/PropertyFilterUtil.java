/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.framework.dao.hibernate;

import com.innofi.framework.utils.date.DateUtil;
import com.innofi.framework.utils.validate.Validator;

import java.util.*;

/**
 * 功能/ 模块：框架基础模块
 *
 * @author 刘名寓 liumy2009@126.com
 * @version 2.0.0 12-11-30
 *          属性过滤器工具类，提供添加、修改、删除等功能
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
public class PropertyFilterUtil {

    /**
     * 根据传入属性名称，属性值，匹配方式属性过滤器
     *
     * @param propertyName  属性名称
     * @param propertyValue 属性值
     * @param matchType     匹配类型
     * @return 属性过滤器列表
     */
    public static List<PropertyFilter> buildPropertyFilter(String propertyName, Object propertyValue, String matchType) {
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        addPropertyFilter(filters, QueryConstants.RESTRICTION_TYPE_AND,propertyName, propertyValue, matchType, false);
        return filters;
    }

    /**
     * 根据传入参数Map，创建等于属性过滤器
     *
     * @param parameters 前台传入的查询参数,key属性名,value属性值
     * @return 等于数属性器列表
     */
    public static List<PropertyFilter> buildEqPropertyFilters(Map<String, Object> parameters) {
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        if (parameters != null) {
            Set entries = parameters.entrySet();
            Iterator<Map.Entry<String, String>> iEntries = entries.iterator();
            while (iEntries.hasNext()) {
                Map.Entry<String, String> entry = iEntries.next();
                addPropertyFilter(filters, QueryConstants.RESTRICTION_TYPE_AND,entry.getKey(), entry.getValue(), QueryConstants.EQUAL, false);
            }
        }
        return filters;
    }

    /**
     * 添加属性过滤器，
     *
     * @param propertyFilters 属性过滤器列表
     * @param restrictionType 限制方式
     * @param propertyName    属性名称
     * @param propertyValue   属性值
     * @param matchType       匹配方式
     * @param overwrite       是否重写，true覆盖第一次添加属性名相同的条件,fasle重复添加该属性条件执行and查询
     */
    public static void addPropertyFilter(List<PropertyFilter> propertyFilters,String restrictionType ,String propertyName, Object propertyValue, String matchType, Boolean overwrite) {
        if (Validator.isNull(propertyName)) return;

        if (!QueryConstants.IS_NULL.equals(matchType)
                && !QueryConstants.IS_NOT_NULL.equals(matchType)
                && !QueryConstants.IS_EMPTY.equals(matchType)
                && !QueryConstants.IS_NOT_EMPTY.equals(matchType)) {//不是isNull、isNotNull、isEmpty、isNotEmpty匹配
            //判断是否为空值，是直接返回，不进行添加属性过滤器添加动作
            if (propertyValue == null) return;
            else if (propertyValue instanceof Collection && ((Collection) propertyValue).size() == 0) return;
            else if (propertyValue instanceof Object[] && ((Object[]) propertyValue).length == 0) return;
            else if (propertyValue instanceof String && Validator.isNull((String) propertyValue)) return;
        }

        if (overwrite) {//删除已添加属性条件
            removePropertyFilter(propertyFilters, propertyName);
        }

        PropertyFilter propertyFilter = new PropertyFilter(restrictionType,propertyName, propertyValue, matchType);
        propertyFilters.add(propertyFilter);
    }

    /**
     * 根据传入的propertyName从parameters参数Map中取得开始日期值，根据约定propertyName+"End从parameters参数Map中取得结束日期值<br/>
     * 按照"yyyy-MM-dd HH:mm:ss"对日期值进行格式化，格式化后值为：开始日期值+" 00:00:00"、结束日期值+" 23:59:59"<br/>
     * 如果propertyName取不到日期值,propertyName+End能够取到日期值，那么格式化后值为:结束日期值+" 00:00:00"、结束日期值+" 23:59:59"<br/>
     * 如果propertyName能够取到日期值,propertyName+End取不到日期值，那么格式化后值为:开始日期值+" 00:00:00"、开始日期值+" 23:59:59"<br/>
     * 如果propertyName、propertyName+"End"均取不到日期值，那么将不添加该属性过滤器
     * 该属性过滤的匹配方式为between
     *
     * @param propertyName 属性名称
     * @param parameters   参数Map
     */
    public static void addDateRangePropertyFilter(List<PropertyFilter> propertyFilters, String propertyName, Map<String, Object> parameters) {
        Date startDate = (Date) parameters.get(propertyName);
        Date endDate = (Date) parameters.get(propertyName + "End");

        //如果都为空直接返回不进行属性过滤器添加操作
        if (startDate == null && endDate == null) return;
        if (startDate != null && endDate != null) {
            startDate = DateUtil.getDateFromDateTime(DateUtil.formatDateWithDASH(startDate) + " 00:00:00");
            endDate = DateUtil.getDateFromDateTime(DateUtil.formatDateWithDASH(endDate) + " 23:59:59");
        } else if (startDate != null && endDate == null) {
            endDate = DateUtil.getDateFromDateTime("2300-01-01 23:59:59");
            startDate = DateUtil.getDateFromDateTime(DateUtil.formatDateWithDASH(startDate) + " 00:00:00");
        } else if (startDate == null && endDate != null) {
            startDate = DateUtil.getDateFromDateTime("1900-01-01 00:00:00");
            endDate = DateUtil.getDateFromDateTime(DateUtil.formatDateWithDASH(endDate) + " 23:59:59");
        }

        Date[] dateRange = new Date[]{startDate, endDate};
        PropertyFilter propertyFilter = new PropertyFilter(QueryConstants.RESTRICTION_TYPE_AND,propertyName, dateRange, QueryConstants.BETWEEN);
        propertyFilters.add(propertyFilter);
    }


    /**
     * 删除属性过滤器，
     *
     * @param propertyFilters 属性过滤器列表
     * @param propertyName    属性名称
     */
    public static void removePropertyFilter(List<PropertyFilter> propertyFilters, String propertyName) {
        PropertyFilter removedPropertyFilter = null;
        for (PropertyFilter propertyFilter : propertyFilters) {
            if (propertyFilter.getPropertyName().equals(propertyName)) {
                removedPropertyFilter = propertyFilter;
            }
        }
        if (removedPropertyFilter != null) propertyFilters.remove(removedPropertyFilter);
    }
}
