/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.component.sie;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @author liumy2009@126.com
 * @version 2.0
 *         状态引擎常量
 * @found date: 13-10-25
 * @found time: 下午4:53
 */
public class SieConstants {

    public static final String BUSI_STATUS_EDIT = "1";                // 编辑中
    public static final String BUSI_STATUS_WAIT_ADUIT = "2";          // 待审核
    public static final String BUSI_STATUS_WAIT_APPROVAL = "3";       // 待审批
    public static final String BUSI_STATUS_AL_ADUIT = "4";            // 已审核
    public static final String BUSI_STATUS_AL_APPROVAL = "5";         // 已审批
    public static final String BUSI_STATUS_RELEASE = "6";             // 已发布
    public static final String BUSI_STATUS_FILE = "7";                // 已归档

    public static final String OPER_TYPE_NEWADD = "1";                // 新增
    public static final String OPER_TYPE_MODIFY = "2";                // 修改
    public static final String OPER_TYPE_DELETE = "3";                // 删除
    public static final String OPER_TYPE_SUBMIT = "4";                // 提交
    public static final String OPER_TYPE_ADUIT = "5";                 // 审核
    public static final String OPER_TYPE_RETURN = "6";                // 退回
    public static final String OPER_TYPE_VIEW = "7";                  // 浏览
    public static final String OPER_TYPE_APPROVAL = "8";              // 审批
    public static final String OPER_TYPE_RETURN_EDIT = "9";           // 退回到编辑中
    public static final String OPER_TYPE_CANCEL = "10";               // 撤消
    public static final String OPER_TYPE_FILE = "11";                 // 归档

    public static final String VARIABLE_USER_KEY = "${userCode}";     //用户变量
    public static final String VARIABLE_ORG_KEY = "${orgCode}";       //机构变量
    public static final String VARIABLE_ROLE_KEY = "${roleCode}";     //角色变量

    public static final String NODE_TYPE_MANUAL = "NODE_TYPE1";                //人工节点
    public static final String NODE_TYPE_IF = "NODE_TYPE2";                    //人工节点
    public static final String NODE_TYPE_AUTO = "NODE_TYPE3";                  //自动节点



}
