/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.component.rbac.login.listener;

import com.bstek.dorado.view.View;
import com.bstek.dorado.view.widget.form.TextEditor;
import com.innofi.component.rbac.RBACConstants;
import com.innofi.framework.FrameworkConstants;
import com.innofi.framework.spring.context.ContextHolder;
import com.innofi.framework.utils.validate.Validator;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * 功能/ 模块：系统登录
 *
 * @author 刘名寓 liumy2009@126.com
 * @version 2.0.0 12-11-30
 *          登录视图监听器
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
@Component("loginViewListener")
public class LoginViewListener{

    /**
     * 初始化Login View
     * @param view 视图对象
     * @throws Exception
     */
    public void onInit(View view) throws Exception {
       
    }
}
