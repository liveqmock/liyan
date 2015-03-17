package com.innofi.component.dbconsole.view.interceptor;


import com.bstek.dorado.view.View;
import com.bstek.dorado.view.widget.HtmlContainer;
import com.bstek.dorado.web.DoradoContext;
import com.innofi.component.dbconsole.service.IDbConsoleService;
import com.innofi.component.dbconsole.pojo.ProcessResult;
import com.innofi.framework.utils.string.StringUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.StringReader;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @author liumy2009@126.com
 * @version 2.0
 * @found date 12-7-12
 * @found time 上午11:23
 * <p/>
 * SQL语句执行信息视图拦截器
 */
@Component(value = "dbConsoleExecuteMessageInterceptor")
public class DbConsoleExecuteMessageInterceptor {

    @Resource(name = "dbConsoleService", type = IDbConsoleService.class)
    private IDbConsoleService dbConsoleService;

    public void onInit(View view) throws Exception {
        HtmlContainer htmlContainer = (HtmlContainer) view.getComponent("htmlContainerMessage");
        String groupId = DoradoContext.getAttachedRequest().getParameter("groupId");
        String connName = DoradoContext.getAttachedRequest().getParameter("connName");
        String sql = DoradoContext.getAttachedRequest().getParameter("sql");
        sql = StringUtil.decode(sql);

        if (StringUtil.hasText(sql)) {

            StringReader reader = new StringReader(sql);

            ProcessResult<String[]> result = dbConsoleService.execute(groupId, connName, reader, false, true);

            String succeedMessage = result.getData()[0];

            String failedMessage = result.getData()[1];

            String commentMessage = result.getData()[2];

            StringBuffer allMessage = new StringBuffer();

            if (StringUtil.hasText(commentMessage)) {
                String[] fms = commentMessage.split("\n");
                for (int i = 0; i < fms.length; i++) {
                    String line = fms[i];
                    warning(allMessage, line);
                }
            }

            if (StringUtil.hasText(succeedMessage)) {
                String[] sms = succeedMessage.split("\n");

                for (int i = 0; i < sms.length; i++) {
                    String line = sms[i];
                    info(allMessage, line);
                }
            }

            if (StringUtil.hasText(failedMessage)) {
                String[] fms = failedMessage.split("\n");
                for (int i = 0; i < fms.length; i++) {
                    String line = fms[i];
                    error(allMessage, line);
                }
            }

            summary(allMessage, result.getData()[3]);


            htmlContainer.setContent(allMessage.toString());
        }
    }

    private void error(StringBuffer outMessage, String s) {
        outMessage.append("<span style=\"font-family: Comic Sans MS,Times, TimesNR, 'New Century Schoolbook', Georgia, 'New York', serif;\n" +
                "            font-size: 10px;\n" +
                "            height: 10px;\n" +
                "            filter: blur(strength = 2, direction = 135);\n" +
                "            color: #FF0000;\n" +
                "            \"><p>" + s + "</p></span>");
    }

    private void info(StringBuffer outMessage, String s) {
        outMessage.append("<span style=\"font-family: Comic Sans MS,Times, TimesNR, 'New Century Schoolbook', Georgia, 'New York', serif;\n" +
                "            font-size: 10px;\n" +
                "            height: 10px;\n" +
                "            filter: blur(strength = 2, direction = 135);\n" +
                "            color:#008000\"><p>" + s + "</p></span>");
    }

    private void warning(StringBuffer outMessage, String s) {
        outMessage.append("<span style=\"font-family: Comic Sans MS,Times, TimesNR, 'New Century Schoolbook', Georgia, 'New York', serif;\n" +
                "            font-size: 10px;\n" +
                "            height: 10px;\n" +
                "            filter: blur(strength = 2, direction = 135);\n" +
                "            color: #ff5500\"><p>" + s + "</p></span>");
    }

    private void summary(StringBuffer outMessage, String s) {
        outMessage.append("<span style=\"font-family: Comic Sans MS,Times, TimesNR, 'New Century Schoolbook', Georgia, 'New York', serif;\n" +
                "            font-size: 10px;\n" +
                "            height: 10px;\n" +
                "            filter: blur(strength = 2, direction = 135);\n" +
                "            color: #203eff\"><p>" + s + "</p></span>");
    }

}
