package com.innofi.framework.dao.hibernate.interceptor;

import com.bstek.dorado.util.proxy.ProxyBeanUtils;
import org.hibernate.EmptyInterceptor;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @author liumy2009@126.com
 * @version 2.0
 *          动态为实体类创建代理
 * @found date: 2010-11-23
 * @found time: 20:40:56
 */
public class UnByteCodeProxyInterceptor extends EmptyInterceptor {

    private static final long serialVersionUID = -6422637558312349795L;

    @Override
    public String getEntityName(Object object) {
        if (object != null) {
            Class<?> cl = ProxyBeanUtils.getProxyTargetType(object);
            return cl.getName();
        } else {
            return null;
        }
    }

}
