/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.component.rbac;

/**
 * 功能/ 模块：权限控制模块
 *
 * @author 刘名寓 liumy2009@126.com
 * @version 2.0.0 12-11-30
 *          权限控制模块常量
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
public class RBACConstants {

    /**
     * 用户在线状态-在线-1
     */
    public static final String ONLINE_TRUE = "1";
    /**
     * 用户在线状态-离线-0
     */
    public static final String ONLINE_FALSE = "0";

    /**
     * 权限表达式:当前用户登录机构
     */
    public final static String CURRENT_USER_ORG_CODE = "curUserOrgCode";

    /**
     * 权限表达式:开始标识
     */
    public final static String START_FLAG = "[";

    /**
     * 权限表达式：参数分割符
     */
    public final static String DIVIDE = "|";

    /**
     * 权限表达式:结束标识
     */
    public final static String END_FLAG = "]";

    /**
     * 权限表达式:向上,所选机构所有节点
     */
    public final static String UP = "U";

    /**
     * 权限表达式:向下,所选机构所有节点
     */
    public final static String DOWN = "D";

    /**
     * 权限表达式:垂直,所选机构垂直所有节点
     */
    public final static String VERTICAL = "V";

    /**
     * 权限表达式:水平,所选机构水平所有节点
     */
    public final static String HORIZONTAL = "H";
    /**
     * 实际节点
     */
    public final static String ACTURE = "O";

    /**
     * 待办SQL 当前用户
     */
	public static final String JOB_CURR_USER = ":currUser";
  	
	/**
     * 待办SQL 当前机构
     */
	public static final String JOB_CURR_ORG = ":currOrg";
	
	/**
     * 待办SQL 当前角色
     */
	public static final String JOB_CURR_ROLE = ":currRole";

    /**
     * 待办SQL 当前岗位
     */
    public static final String JOB_CURR_POST = ":currPost";

    /**
     * 权限资源类型:菜单
     */
    public final static String RESOURCE_TYPE_MENU = "1";

    /**
     * 权限资源类型：按钮
     */
    public final static String RESOURCE_TYPE_ACTION = "2";

    /**
     * 权限资源类型:数据表
     */
    public final static String RESOURCE_TYPE_TABLE = "3";

    /**
     * 角色类型:功能角色
     */
    public final static String ROLE_TYPE_FUNCTION = "1";

    /**
     * 角色类型:数据角色
     */
    public final static String ROLE_TYPE_DATA = "2";

    /**
     * 机构类型:机构
     */
    public final static String ORG_CATEGORY_ORG = "1";

    /**
     * 机构类型:部门
     */
    public final static String ORG_CATEGORY_DEPT = "0";

    /**
     * 约束类型，并且
     */
    public final static String RESTRICT_TYPE_AND = "1";

    /**
     * 约束类型，或者
     */
    public final static String RESTRICT_TYPE_OR = "2";

    /**
     * 约束类型，约束条件
     */
    public final static String RESTRICT_TYPE_CONDITION = "3";
    
    /*
     * 机构级别、角色级别、岗位级别
     * 全行
     */
    public final static String ORG_LEVEL_ALL = "0";
    /*
     * 机构级别、角色级别、岗位级别
     * 总行
     */
    public final static String ORG_LEVEL_HEADOFFICE = "1";
    /*
     * 机构级别、角色级别、岗位级别
     * 分行
     */
    public final static String ORG_LEVEL_BRANCH = "2";
    /*
     * 机构级别、角色级别、岗位级别
     * 支行
     */
    public final static String ORG_LEVEL_SUBBRANCH = "3";




}
