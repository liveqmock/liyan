package com.innofi.framework.dao.hibernate;

import com.innofi.framework.datasource.DataSourceRepository;
import com.innofi.framework.exception.FrameworkJdbcRuntimeException;
import com.innofi.framework.utils.validate.Validator;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.util.Map;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司 all rights reserved.
 *
 * @author liumy2009@126.com
 * @version 2.0
 * @found date: 2010-11-23
 * @found time: 20:40:56
 * 扩展Hibernate4 LocalSessionFactoryBean，实现对Hibernate配置文件的动态加载与解析
 */
public class HibernateSessionFactoryBean extends LocalSessionFactoryBean implements ApplicationContextAware {

    private String dataSourceName;
    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() {
        DataSourceRepository dataSourceRepository = applicationContext.getBean(DataSourceRepository.class);
        if (Validator.isNotNull(this.dataSourceName)) {
            Map<String, DataSource> dsMap = dataSourceRepository.getDataSources();
            for (String name : dsMap.keySet()) {
                if (name.equals(this.dataSourceName)) {
                    this.setDataSource(dsMap.get(name));
                    break;
                }
            }
        }
        try {
            super.afterPropertiesSet();
        } catch (Exception e) {
            e.printStackTrace();
            throw new FrameworkJdbcRuntimeException(e);
        }
    }


    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.applicationContext = applicationContext;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

}
