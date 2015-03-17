package com.innofi.framework.utils.xml;

import org.hibernate.internal.util.ConfigHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import java.io.InputStream;
import java.io.Serializable;


/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @author liumy2009@126.com
 * @version 2.0
 *          动态hql、sql的dtd解析器
 * @found date: 2010-11-23
 * @found time: 20:40:56
 */
public class DynamicStatementDTDEntityResolver implements org.xml.sax.EntityResolver, Serializable {
    private static final long serialVersionUID = 8123799007554762965L;
    private static final Logger logger = LoggerFactory.getLogger(DynamicStatementDTDEntityResolver.class);
    private static final String HOP_DYNAMIC_STATEMENT = "http://www.mingsheng.com.cn/dtd";

    public InputSource resolveEntity(String publicId, String systemId) {
        InputSource source = null; // returning null triggers default behavior
        if (systemId != null) {
            logger.debug("trying to resolve system-id [" + systemId + "]");
            if (systemId.startsWith(HOP_DYNAMIC_STATEMENT)) {
                logger.debug("recognized hop dyanmic statement namespace; attempting to resolve on classpath under META-INF/dynamic-statement");
                source = resolveOnClassPath(publicId, systemId, HOP_DYNAMIC_STATEMENT);
            }
        }
        return source;
    }

    private InputSource resolveOnClassPath(String publicId, String systemId, String namespace) {
        InputSource source = null;
        String path = "/META-INF/dynamic-statement/" + systemId.substring(namespace.length());
        InputStream dtdStream = resolveInHibernateNamespace(path);
        if (dtdStream == null) {
            logger.debug("unable to locate [" + systemId + "] on classpath");
            if (systemId.substring(namespace.length()).indexOf("2.0") > -1) {
                logger.error("Don't use old DTDs, read the Hibernate 3.x Migration Guide!");
            }
        } else {
            logger.debug("located [" + systemId + "] in classpath");
            source = new InputSource(dtdStream);
            source.setPublicId(publicId);
            source.setSystemId(systemId);
        }
        return source;
    }

    protected InputStream resolveInHibernateNamespace(String path) {
        return this.getClass().getClassLoader().getResourceAsStream(path);
    }

    protected InputStream resolveInLocalNamespace(String path) {
        try {
            return ConfigHelper.getUserResourceAsStream(path);
        } catch (Throwable t) {
            return null;
        }
    }
}