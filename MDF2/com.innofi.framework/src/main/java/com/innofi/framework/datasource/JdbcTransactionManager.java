package com.innofi.framework.datasource;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.Map;

/**
 * 扩展Spring的JDBC事务管理器，使其可以在多个数据源中找到合适的DataSource
 *
 * @author jack.liu@innofi.com
 * @version  1.0-SNAPSHOT
 */
public class JdbcTransactionManager extends DataSourceTransactionManager implements ApplicationContextAware {
    private static final long serialVersionUID = -2117944168347645059L;
    private String dataSourceName;

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    @Override
    public void afterPropertiesSet() {
        //do nothing...
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        DataSourceRepository res = (DataSourceRepository) applicationContext.getBean("mdf.dataSourceRepository");
        Map<String, DataSource> dsMap = res.getDataSources();
        if (dsMap == null || dsMap.size() == 0) {
            throw new RuntimeException("当前系统未定义数据源！");
        }
        DataSource ds = dsMap.get(res.getRealDataSourceName());
        if (!StringUtils.hasText(dataSourceName) && ds != null) {
            ds = dsMap.values().iterator().next();
        } else {
            ds = dsMap.get(dataSourceName);
        }
        if (ds == null) {
            throw new RuntimeException("当前系统上下文环境没有合适的DataSource，" + this.getClass().getName() + "初始化失败!");
        }
        super.setDataSource(ds);
    }

}
