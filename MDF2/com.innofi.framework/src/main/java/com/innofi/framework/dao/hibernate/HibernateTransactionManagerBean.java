package com.innofi.framework.dao.hibernate;

import com.innofi.framework.datasource.DataSourceRegister;
import com.innofi.framework.spring.context.ContextHolder;
import com.innofi.framework.utils.console.ConsoleUtil;
import org.hibernate.SessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Map;

/**
 * 扩展HibernateTransactionManager，用于实现HibernateSessionFactory数据源动态切换
 *
 * @author jack.liu@innofi.com
 * @version 2.0
 */
public class HibernateTransactionManagerBean extends HibernateTransactionManager implements ApplicationContextAware {

    private static final long serialVersionUID = 2369126687238353561L;
    private Collection<HibernateSessionFactoryBean> sessionFactoryBeanList;
    private SessionFactory defaultSessionFactory;
    private ApplicationContext applicationContext;

    @Override
    public SessionFactory getSessionFactory() {
        return getCurrentSessionFactory();
    }

    private SessionFactory getCurrentSessionFactory() {
        String currentDataSourceName = ContextHolder.getCurrentDataSourceName();
        SessionFactory sessionFactory = null;
        if (StringUtils.hasText(currentDataSourceName)) {
            for (HibernateSessionFactoryBean bean : sessionFactoryBeanList) {
                if (bean.getDataSourceName().equals(currentDataSourceName)) {
                    sessionFactory = bean.getObject();
                    break;
                }
            }
        }
        if (sessionFactory != null) {
            return sessionFactory;
        } else {
            return defaultSessionFactory;
        }
    }

    @Override
    public void afterPropertiesSet() {
        sessionFactoryBeanList = applicationContext.getBeansOfType(HibernateSessionFactoryBean.class).values();
        Map<String, DataSourceRegister> dataSourceRegisters = applicationContext.getBeansOfType(DataSourceRegister.class);
        for (HibernateSessionFactoryBean bean : sessionFactoryBeanList) {
             /*
              * 获得所有DataSourceRegister，建立数据源名称与DataSource映射
              */
            for (DataSourceRegister dataSourceRegister : dataSourceRegisters.values()) {
                if (dataSourceRegister.getName().equals(bean.getDataSourceName()) && dataSourceRegister.isAsDefault()) {
                    defaultSessionFactory = bean.getObject();
                    ConsoleUtil.info("default session factory [" + bean.getDataSourceName() + "]");
                }
            }
        }
    }

    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.applicationContext = applicationContext;
    }

}
