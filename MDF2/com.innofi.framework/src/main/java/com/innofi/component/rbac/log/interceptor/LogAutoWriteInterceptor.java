/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.component.rbac.log.interceptor;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import com.innofi.framework.utils.reflect.ReflectionUtil;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.aop.AfterReturningAdvice;

import com.innofi.component.rbac.log.pojo.SysLog;
import com.innofi.component.rbac.log.pojo.SysLogCfg;
import com.innofi.component.rbac.log.service.ISysLogService;
import com.innofi.component.cache.service.ISysCacheConfigService;
import com.innofi.component.codetransfer.IdfCodeTransfer;
import com.innofi.component.rbac.user.pojo.SysUser;
import com.innofi.framework.spring.context.ContextHolder;
import com.innofi.framework.utils.string.StringUtil;

/**
 * 功能/ 模块：权限模块
 *
 * @author 刘名寓 liumy2009@126.com
 * @version 2.0.0 12-11-30
 *          日志自动记录拦截器：根据PCC中操作日志配置模块配置的规则，在业务服务中的方法执行时，自动记录日志。
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
public class LogAutoWriteInterceptor implements AfterReturningAdvice {

    @Resource
    private ISysLogService sysLogService;
    
    @Resource
    private ISysCacheConfigService sysCacheConfigService;

    /**
     * (non-Javadoc)
     *
     * @see org.aopalliance.intercept.MethodInterceptor#invoke(MethodInvocation)
     */
    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {

        String targetName = target.getClass().getSimpleName();

        if (!(target instanceof java.lang.reflect.Proxy) && !(target instanceof net.sf.cglib.proxy.Proxy) && sysCacheConfigService!=null && !targetName.equals("SysCacheConfigServiceImpl") &&
                !targetName.equals("SysLogServiceImpl")) {
            IdfCodeTransfer idfCodeTransfer = ContextHolder.getSpringBean("mdf.codeTransfer");
            ListOrderedMap allSysLogCfgs = idfCodeTransfer.getCacheObjects("sysLogCfgService");
            if (allSysLogCfgs != null) {
                for (int i = 0; i < allSysLogCfgs.size(); i++) {
                    SysLogCfg sysLogCfg = (SysLogCfg) allSysLogCfgs.getValue(i);
                    if (targetName.startsWith(StringUtil.upperFirst(sysLogCfg.getObjName())) && method.getName().equals(sysLogCfg.getMethodName())) {
                        SysUser user = null;
                        Object[] arguments = args;
                        int argumentsLength = arguments.length;

                        try {
                            user = ContextHolder.getContext().getLoginUser();
                        } catch (Exception e) {
                            //如果出现Exception说明当前登录用户为空，则不记录日志。
                        }
                        if (user != null) {//用户不为空时记录操作日志
                            SysLog syslog = new SysLog();
                            syslog.setUserCode(user.getUserCode());
                            syslog.setUserName(user.getUserName());
                            syslog.setOrgCode(user.getOrgCode());
                            syslog.setOrgName(user.getSysOrgInfo().getOrgName());
                            syslog.setIpAddr(user.getIpAddress());
                            syslog.setOperType(sysLogCfg.getOperType());
                            syslog.setResourceName(sysLogCfg.getResourceName());
                            syslog.setResourceCode(sysLogCfg.getResourceCode());
                            syslog.setLogType("1");
                            syslog.setOperateDate(new Date());
                            if (StringUtils.isNotBlank(sysLogCfg.getLogRule()) && sysLogCfg.getLogRule().indexOf("[") != -1 && sysLogCfg.getLogRule().indexOf("]") != -1) {
                                String[] elements = sysLogCfg.getLogRule().split(",");//处理输出格式
                                StringBuffer logTemplae = new StringBuffer();
                                for (int j = 0; j < elements.length; j++) {
                                    logTemplae.append(elements[j].substring(0, elements[j].indexOf("|")) + "],");
                                }
                                logTemplae.delete(logTemplae.length() - 1, logTemplae.length());

                                //输出参数跟值
                                Pattern p = Pattern.compile("\\[([^\\]]+])");
                                Matcher m = p.matcher(sysLogCfg.getLogRule());
                                List<String> argsList = new ArrayList<String>();
                                while (m.find()) {
                                    String arg = m.group();
                                    argsList.add(arg);
                                }

                                String[] formatParameters = new String[argsList.size()];
                                for (int k = 0; k < argsList.size(); k++) {
                                    String arg = argsList.get(k);
                                    int argIndex = -1;
                                    if (arg.indexOf(".") > -1) {
                                        argIndex = Integer.parseInt(arg.substring(arg.indexOf("|arg") + 4, arg.indexOf(".")));
                                        String property = arg.substring(arg.indexOf(".") + 1, arg.length() - 1);
                                        Object argument = arguments[argIndex - 1];
                                        formatParameters[k] = ReflectionUtil.getFieldValue(argument, property).toString();
                                    } else {
                                        argIndex = Integer.parseInt(arg.substring(arg.indexOf("|arg") + 4, arg.length() - 1));
                                        formatParameters[k] = arguments[argIndex - 1].toString();
                                    }
                                }
                                syslog.setLogContent(MessageFormat.format(logTemplae.toString(), formatParameters));
                                sysLogService.save(syslog);
                            } else {
                                syslog.setLogContent(sysLogCfg.getLogRule());
                                sysLogService.save(syslog);
                            }
                            break;
                        }
                    }
                }
            }
        }
    }
}
