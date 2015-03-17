package com.innofi.framework.dao.jdbc.sqldialect;

import com.innofi.framework.dao.jdbc.IJdbcDao;
import com.innofi.framework.dao.pagination.Page;
import com.innofi.component.dbconsole.pojo.DbConsoleColumn;
import com.innofi.component.dbconsole.pojo.DbConsoleTable;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Connection;
import java.util.List;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @version 2.0
 * @author liumy2009@126.com
 * @found date: 2012-06-10
 * @found time: 23:55:59
 * <p/>
 * 数据库方言对象，实现不同数据库对应的分页实现
 */
public interface IDBDialect {

    /**
     * 判断数据库类型
     *
     * @param connection 当前数据库连接
     * @return 返回是否支持当前连接 支持返回true 不支持返回false
     */
    public boolean support(Connection connection);

    /**
     * 修改表名称sql
     *
     * @param schema       模式 可选项
     * @param tableName    原表名
     * @param newTableName 修改后的表名
     * @return 返回修改表名Sql
     */
    public String getTableRenameSql(String schema, String tableName, String newTableName);

    /**
     * 默认创建表sql
     *
     * @param tableName 表名
     * @return 返回 建表语句
     */
    public String getCreateDefaultTableSql(String tableName);

    /**
     * 添加列sql
     *
     * @param dbConsoleColumn dbColumnInfo对象
     * @return 返回添加列Sql
     */
    public String getNewColumnSql(DbConsoleColumn dbConsoleColumn);

    /**
     * 更新列sql
     *
     * @param oldDbConsoleColumn 原有的dbColumnInfo对象
     * @param newDbConsoleColumn 新的dbColumnInfo对象
     * @return 返回更新列Sql
     */
    public String getUpdateColumnSql(DbConsoleColumn oldDbConsoleColumn, DbConsoleColumn newDbConsoleColumn);


    /**
     * 删除序列Sql
     *
     * @param schema       模式 可选项
     * @param sequenceName 序列名称
     * @return 返回删除序列Sql
     */
    public String getDropSequenceSql(String schema, String sequenceName);

    /**
     * 获取分页SQL
     *
     * @param sql  sql语句
     * @param page 分页对象
     * @return 返回分页sql
     */
    public String getLimitSql(String sql, Page page);

    /**
     * 获取分页SQL
     *
     * @param sql       sql语句
     * @param pageIndex pageIndex 当前页面索引
     * @param limitSize
     * @return 返回分页sql
     */
    public String getLimitSql(String sql, int pageIndex, int limitSize);

    /**
     * 获取分页SQL
     *
     * @param table 表格对象
     * @return 返回分页sql
     */
    public String generatorDDL(DbConsoleTable table);

    /**
     * 分页查询
     *
     * @param <T>
     * @param sql
     * @param parameters
     * @param pageNo
     * @param pageSize
     * @param rowMapper
     * @return 返回查询结果集合
     */
    public <T> List<T> query(IJdbcDao jdbcDao, String sql, Object[] parameters, int pageNo, int pageSize, RowMapper<T> rowMapper);


}
