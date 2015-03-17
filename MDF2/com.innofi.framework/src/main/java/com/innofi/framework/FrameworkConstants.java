/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.framework;

/**
 * 功能/ 模块：框架基础模块
 *
 * @author 刘名寓 liumy2009@126.com
 * @version 2.0.0 12-11-30
 *          框架基础块常量
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
public class FrameworkConstants {
    /**
     * 当前用户在session中的key
     */
    public static final String SESSION_USER = "user";
    /**
     * 当前用户在cookie中的key
     */
    public static final String COOKIE_USER = "idfUser";

    /**
     * 用户权限菜单
     */
    public static final String SESSION_USER_MENU = "userMenu";

    /**
     * 用户权限按钮
     */
    public static final String SESSION_USER_ACTION = "userAction";

    /**
     * 权限类型:浏览
     */
    public final static String DA_TYPE_BROWSER = "1";

    /**
     * 权限类型:修改
     */
    public final static String DA_TYPE_MODIFY = "2";

    /**
     * 权限类型:删除
     */
    public final static String DA_TYPE_DELETE = "3";

    /**
     * 是否:1-是
     */
    public static final String COMM_Y ="1";

    /**
     * 是否:0-否
     */
    public static final String COMM_N ="0";

    /**
     * 有效状态 有效 1
     */
    public static final String STATUS_EFFECTIVE = "1";

    /**
     * 有效状态 无效 0
     */
    public static final String STATUS_INVALID = "0";

    /**
     * 资源文件缓存
     */
    public static final String RESOURCE_BUNDLE_CACHE ="resourceBundles";
    
	/**
	 * 缓存类型，临时缓存
	 */
	public static final String CACHE_TYPE_TEMPORARY = "temporary";
	
	/**
	 * 永久缓存
	 */
	public static final String CACHE_TYPE_FOREVER = "forever";
	
	/**
	 * 实时刷新缓存
	 */
	public static final String CACHE_POLICY_CURRENT = "2";
	
	/**
	 * 定时刷新缓存
	 */
	public static final String CACHE_POLICY_DEFIND = "1";
	
	/**
	 * 缓存类型key
	 */
	public static final String CACHE_TYPE_KEY = "sys.config.cacheType";
	
	/**
	 * 缓存策略key
	 */
	public static final String CACHE_POLICY_KEY = "sys.config.cachePolicy";

    /**
     * 系统参数key
     */
    public static final String PARAMETER_KEY = "sys.parameter";

    /**
     * tag参数key于value分隔符
     */
    public static final String TAG_PARAMETER_SEPARATE = "=";

    /**
     * tag参数连接符
     */
    public static final String TAG_JOIN_SEPARATE = "&";

    /**
     * 文件上传保存类型CLOB
     */
    public static final String FILE_UPLOAD_SAVE_TYPE_CLOB = "CLOB";

    /**
     * 文件上传保存类型BLOB
     */
    public static final String FILE_UPLOAD_SAVE_TYPE_BLOB = "BLOB";

    /**
     * 文件上传保存类型FILE
     */
    public static final String FILE_UPLOAD_SAVE_TYPE_FILE = "FILE";

    /**
     * 用户自定义配置 视图列表
     */
    public static final String USER_CFG_TYPE_VIEW = "1";

    /**
     * 用户自定义配置 查询方案
     */
    public static final String USER_CFG_TYPE_CRITERIA = "2";

}
