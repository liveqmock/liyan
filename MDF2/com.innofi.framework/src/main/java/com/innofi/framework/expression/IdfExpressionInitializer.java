/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.framework.expression;

import java.util.Map;

import com.bstek.dorado.core.el.ContextVarsInitializer;
import com.innofi.framework.exception.MessageUtil;
import com.innofi.framework.spring.context.ContextHolder;
import com.innofi.framework.utils.date.DateUtil;
import com.innofi.framework.utils.string.StringUtil;
import com.innofi.framework.utils.validate.Validator;

/**
 * 功能/ 模块：框架基础模块
 *
 * @author 刘名寓 liumy2009@126.com
 * @version 2.0.0 12-11-30
 *          EL表达式初始化类，
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
public class IdfExpressionInitializer implements ContextVarsInitializer {

    public void initializeContext(Map<String, Object> vars) throws Exception {
        vars.put("sys", ContextHolder.getSystemProperties());
        vars.put("validator", new Validator());
        vars.put("dateUtil", new DateUtil());
        vars.put("stringUtil",new StringUtil());
        vars.put("messageUtil", new MessageUtil());
        vars.put("fctx",ContextHolder.getContext());
    }

}
