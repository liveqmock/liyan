/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.component.metadata.commons;

/**
 * 功能/ 模块：元数据基础模块
 *
 * @author 刘名寓 liumy2009@126.com
 * @version 2.0.0 12-11-30
 *          元数据常量类
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
public class MetadataConstants {

    /**
     * 元数据版本启始号
     */
    public static final int VERSION_START = 1;

    /**
     * 对象类型 1-entity实体对象
     */
    public static final String OBJECT_TYPE_ENTITY = "1";

    /**
     * 对象类型 2-serviceimpl服务类
     */
    public static final String OBJECT_TYPE_SERVICE = "2";

    /**
     * 对象类型3-action类
     */
    public static final String OBJECT_TYPE_ACTION = "3";
    /**
     * 对象类型4-UI界面
     */
    public static final String OBJECT_TYPE_UI = "4";
    /**
     * 对象类型5-service服务接口
     */
    public static final String OBJECT_TYPE_INTERFACE = "5";
    /**
     * 对象类型6-dao服务接口
     */
    public static final String OBJECT_TYPE_DAO = "6";

    /**
     * 表类型 1-系统表
     */
    public static final String TABLE_TYPE_SYS = "1";

    /**
     * 表类型 2-业务表
     */
    public static final String TABLE_TYPE_BUS = "2";

    /**
     * 表类型 3-平台表
     */
    public static final String TABLE_TYPE_PT = "3";

    /**
     * 表类型 4-源数据表
     */
    public static final String TABLE_TYPE_SS = "4";

    /**
     * 外键类型 0-逻辑
     */
    public static final String IS_CRT_L = "0";

    /**
     * 外键类型 1-物理
     */
    public static final String IS_CRT_P = "1";

    /**
     * 返回 0-成功
     */
    public static final String SUCCESS = "0";

    /**
     * 返回  1-失败
     */
    public static final String FAILED = "1";


    /**
     * 字段修改类型:添加
     */
    public static final int FIELD_ADD = 1;

    /**
     * 字段修改类型:修改
     */
    public static final int FIELD_UPDATE = 2;

    /**
     * 字段修改类型:删除
     */
    public static final int FIELD_DEL = 3;

    /**
     * 字段修改类型:未改变
     */
    public static final int FIELD_UN_CHANGE = 4;

    /**
     * 添加  1
     */
    public static final String METHOD_ADD = "1";

    /**
     * 修改  2
     */
    public static final String METHOD_UPDATE = "2";

    /**
     * 删除  3
     */
    public static final String METHOD_DEL = "3";

    /**
     * 浏览 4
     */
    public static final String METHOD_LOOK = "4";

    /**
     * 用户权限按钮
     */
    public static final String SESSION_USER_ACTION = "userAction";

    /**
     * 树根 0-根节点
     */
    public static final String TREE_ROOT = "0";

    /**
     * 视图类型 1-元数据标准（CWM）视图
     */
    public static final String VIEW_TYPE_CWM = "1";

    /**
     * 2-业务视图
     */
    public static final String VIEW_TYPE_BUSINESS = "2";

    /**
     * 3-元数据标准（CWM）视图 ，加下级节点不需要作依赖关系校验
     */
    public static final String VIEW_TYPE_OTHER = "3";

    /**
     * 元数据 数据状态：只保存最新数据
     */
    public static final String DATA_STATUS_NEW = "1";

    /**
     * 元数据 数据状态：保存历史数据
     */
    public static final String DATA_STATUS_VERSION = "2";

    /**
     * 元数据 数据状态：历史数据保存历史表
     */
    public static final String DATA_STATUS_HISTORY = "3";


}
