/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.component.cache.aop;

import org.springframework.aop.support.NameMatchMethodPointcut;

import java.lang.reflect.Method;


/**
 * 功能/ 模块：框架基础模块
 *
 * @author 刘名寓 liumy2009@126.com
 * @version 2.0.0 12-11-30
 *          缓存自动刷新切入点
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
public class LogAutoWriteInterceptorPoint extends NameMatchMethodPointcut {

    @Override
    public void setMappedNames(String[] mappedNames) {
        super.setMappedNames(mappedNames);
    }

    @Override
    public boolean matches(Method method, Class targetClass) {
        return super.matches(method, targetClass);
    }

}
