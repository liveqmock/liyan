package com.innofi.framework.datasource;

import com.innofi.framework.exception.FrameworkException;
import com.innofi.framework.spring.context.ContextHolder;
import com.innofi.framework.utils.console.ConsoleUtil;
import com.innofi.framework.utils.validate.Validator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.sql.DataSource;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @author liumy2009@126.com
 * @version 2.0
 * @found date: 2010-11-23
 * @found time: 20:40:56
 * 扫描系统添加所有DataSource
 */
public class DataSourceRepository implements InitializingBean, ApplicationContextAware {

    private ApplicationContext applicationContext;
    private Map<String, DataSource> dataSources = new HashMap<String, DataSource>();

    private String defaultDataSourceName;

    /**
     * 从数据源仓库中取得接下来线程要用到的数据源.
     *
     * @return 真实的数据源
     */
    public DataSource getRealDataSource() {
        String dataSource = getRealDataSourceName();                                                                    //获取当前数据源名称

        if (Validator.isNotNull(dataSource)) {
            return dataSources.get(dataSource);
        }

        if (1 == dataSources.size()) {                                                                                  //如果当前线程中没有明确指定使用的数据源名称,且没有默认名称,若仅有一个数据源,使用它
            return dataSources.values().iterator().next();
        }

        throw new IllegalStateException("当前系统配置了多个数据源,您必须指定其一为默认.");
    }

    public String getRealDataSourceName() {
        String dataSource = ContextHolder.getCurrentDataSourceName();                                                   // 如果明确指定了当前线程使用的数据源的名称,则使用它
        if (Validator.isNotNull(dataSource) && dataSources.containsKey(dataSource)) {
            return dataSource;
        }
        if (Validator.isNotNull(defaultDataSourceName) && dataSources.containsKey(defaultDataSourceName)) {             // 如果当前线程中没有明确指定使用的数据源名称,使用默认
            return defaultDataSourceName;
        }
        return null;
    }


    public synchronized void afterPropertiesSet() throws Exception {
        Map<String, DataSourceRegister> dataSourceRegisters = applicationContext.getBeansOfType(DataSourceRegister.class);
        /*
         * 获得所有DataSourceRegister，建立数据源名称与DataSource映射
         */
        for (DataSourceRegister dataSourceRegister : dataSourceRegisters.values()) {
        	dataSources.put(dataSourceRegister.getName(), dataSourceRegister.getDataSource());
            if (dataSourceRegister.isAsDefault()) {
        		if(Validator.isNotEmpty(defaultDataSourceName)){
                    throw new FrameworkException("已经存在默认的数据源["+defaultDataSourceName+"],不能同时将两个数据源同时设置为默认数据源["+dataSourceRegister.getName()+"],请检查META-INF[datasources.xml]!");
                }
                ConsoleUtil.info(MessageFormat.format("project default datasource [{0}]", dataSourceRegister.getName()));
                defaultDataSourceName = dataSourceRegister.getName();
            }
        }
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public String getDefaultDataSourceName() {
        return defaultDataSourceName;
    }

    public Map<String, DataSource> getDataSources() {
        return dataSources;
    }

}
