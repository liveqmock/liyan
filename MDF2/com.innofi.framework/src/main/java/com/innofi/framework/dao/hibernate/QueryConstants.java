package com.innofi.framework.dao.hibernate;

/**
 * Hibernate查询常量类
 */
public final class QueryConstants {

    /**
     * 等於比较符号
     */
    public static final String EQUAL = "eq";

    /**
     * 不等於比较符号
     */
    public static final String NOT_EQUAL = "ne";

    /**
     * 使用Map，使用key/value進行多個等於的比對
     */
    public static final String ALL_EQUAL = "allEq";

    /**
     * 大于比较符号
     */
    public static final String GREATER_THAN = "gt";

    /**
     * 大于等于比较符号
     */
    public static final String GREATER_OR_EQUAL = "ge";

    /**
     * 小于比较符号
     */
    public static final String LESS_THAN = "lt";

    /**
     * 小于等于比较符号
     */
    public static final String LESS_OR_EQUAL = "le";

    /**
     * between 對應SQL的BETWEEN
     */
    public static final String BETWEEN = "between";

    /**
     * like 對應SQL的LIKE
     */
    public static final String LIKE = "like";

    /**
     * in對應SQL的IN
     */
    public static final String IN = "in";

    /**
     * not in對應SQL的NOT IN
     */
    public static final String NOT_IN = "notIn";

    /**
     * isNull对应SQL的ISNULL
     */
    public static final String IS_NULL = "isNull";

    /**
     * isNotNull对应SQL的ISNOTNULL
     */
    public static final String IS_NOT_NULL = "isNotNull";

    /**
     * isEmpty对应SQL的等于空串
     */
    public static final String IS_EMPTY = "isEmpty";
    /**
     * isNotEmpty对应SQL的不等于空串
     */
    public static final String IS_NOT_EMPTY = "isNotEmpty";

    /**
     * 排序_降序
     */
    public static final String ORDER_DESC = "desc";

    /**
     * 排序_升序
     */
    public static final String ORDER_ASC = "asc";

    /**
     * 约束条件 and 条件
     */
    public static final String RESTRICTION_TYPE_AND = "and";

    /**
     * 约束条件 or 条件
     */
    public static final String RESTRICTION_TYPE_OR = "or";

}