package com.innofi.framework.dao.jdbc;

import com.innofi.component.dbconsole.pojo.*;
import com.innofi.framework.dao.jdbc.sqldialect.DBDialect;
import com.innofi.framework.dao.pagination.Page;
import com.innofi.framework.exception.FrameworkJdbcRuntimeException;

import freemarker.template.TemplateException;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import javax.sql.DataSource;

import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @author liumy2009@126.com
 * @version 2.0
 * @found date: 2010-11-23
 * @found time: 20:40:56
 * Jdbc Dao数据库访问接口，屏蔽数据库之间差异
 */
public interface IJdbcDao extends NamedParameterJdbcOperations, JdbcOperations {

    public static final String OBJECT_TYPE_SELECTABLE_KEY = "objecttype.selectable";

    /**
     * 获取当前数据库连接
     *
     * @return 数据库连接对象
     */
    public Connection getConnection();

    /**
     * 获得当前数据库类型
     *
     * @return 数据库类型
     */
    public String getCurrentDbType();

    /**
     * 加载当前数据库信息
     *
     * @return
     */
    public DbConsoleDbInfo loadDbInfo();

    /**
     * 获得当前数据源
     *
     * @return
     */
    public DataSource getDataSource();

    /**
     * 获得当前数据库方言
     *
     * @return
     */
    public DBDialect getDBDialect();

    /**
     * 加载当前数据库所有schema模式
     *
     * @return 返回模式列表
     */
    public List<String> loadSchemas();

    /**
     * 加载数据库对象类型
     *
     * @return 返回对象类型列表
     */
    public List<String> loadObjectTypes();

    /**
     * 加载指定模式下，指定表名视图及数据表
     *
     * @param schema 可为null，null加载所有模式
     * @param table  可为null，null加载所有，非null like查询
     * @param types  类型
     * @param lazy   是否懒加载数据表对象其它复合属性
     * @return 符合条件的视图、数据表
     */
    public List<DbConsoleTable> loadTableList(String schema, String table, String types, boolean lazy);


    /**
     * 查询表的所有主键
     *
     * @param schema    模式
     * @param tableName 表名
     * @return 返回表主键
     * @throws Exception
     */
    public DbConsolePk loadTablePrimaryKeys(String schema, String tableName);


    /**
     * 查询表的所有外键
     *
     * @param schema    模式
     * @param tableName 表名
     * @return 返回表主键
     * @throws Exception
     */
    public List<DbConsoleFk> loadTableForeignKeys(String schema, String tableName);

    /**
     * 根据指定表名加载表中所有列
     *
     * @param schema    schema名称
     * @param tableName 数据表名称
     * @return 该表中所有列对象
     */
    public List<DbConsoleColumn> loadColumns(String schema, String tableName);

    /**
     * 查找默认配置的数据库类型
     *
     * @return 返回默认配置的数据库类型的集合
     * @throws Exception
     */
    public List<String> loadColumnType();

    /**
     * 加载指定模式下，指定Sequence
     *
     * @param schema 不可为null
     * @param name   不可为null
     * @return 符合条件的sequence
     */
    public DbConsoleSequence loadSequence(String schema, String name);

    /**
     * 加载指定模式下，指定Sequence
     *
     * @param schema 可未null,null加载所有模式
     * @param name   可为null，null加载所有，非null like查询
     * @return 符合条件的sequence列表
     */
    public List<DbConsoleSequence> loadSequences(String schema, String name);


    /**
     * 插入表新列
     *
     * @param schema     模式
     * @param tableName  表名
     * @param columnInfo 列对象
     */
    public void insertColumn(String schema, String tableName, DbConsoleColumn columnInfo);


    /**
     * 更新表列
     *
     * @param schema        模式
     * @param tableName     表名
     * @param oldColumnInfo 修改之前列对象
     * @param newColumnInfo 新列对象
     */
    public void updateColumn(String schema, String tableName, DbConsoleColumn oldColumnInfo, DbConsoleColumn newColumnInfo);

    /**
     * 删除表列
     *
     * @param schema     模式
     * @param tableName  表名
     * @param columnName 列名
     * @throws Exception
     */
    public void dropColumn(String schema, String tableName, String columnName);

    /**
     * 判断指定表是否存在
     *
     * @param schema    模式  可选参数
     * @param tableName 指定表名
     * @return 存在则返回true；不存在则返回false
     * @throws FrameworkJdbcRuntimeException 框架Jdbc运行时异常
     */
    public boolean ifTableExist(String schema, String tableName) throws FrameworkJdbcRuntimeException;

    /**
     * 判断表中指定列是否存在
     *
     * @param schema     模式  可选参数
     * @param tableName  指定表名
     * @param columnName 列名
     * @return 存在则返回true；不存在则返回false
     * @throws FrameworkJdbcRuntimeException 框架Jdbc运行时异常
     */
    public boolean ifTableColumnExist(String schema, String tableName, String columnName) throws FrameworkJdbcRuntimeException;

    /**
     * 判断满足条件的索引是否存在
     *
     * @param schema    模式  可选参数
     * @param tableName 指定表名
     * @param cols      指定表上的索引字段
     * @return 存在则返回true；不存在则返回false
     * @throws FrameworkJdbcRuntimeException 框架Jdbc运行时异常
     */
    public boolean ifIndexExist(String schema, String tableName, String... cols) throws FrameworkJdbcRuntimeException;

    /**
     * 判断指定名称的索引是否存在
     *
     * @param schema    模式  可选参数
     * @param indexName 索引名称
     * @return 存在则返回true；不存在则返回false
     * @throws FrameworkJdbcRuntimeException 框架Jdbc运行时异常
     */
    public boolean ifIndexExist(String schema, String indexName) throws FrameworkJdbcRuntimeException;

    /**
     * 获取数据表记录数
     *
     * @param schema 模式  可选参数
     * @param table  指定表名
     * @return 表中字段名数组
     * @throws FrameworkJdbcRuntimeException
     */
    public int getTableRecordCount(String schema, String table) throws FrameworkJdbcRuntimeException;

    /**
     * 获取数据表字段名,按字段名进行排序
     *
     * @param schema 模式  可选参数
     * @param table  指定表名
     * @return 表中字段名数组
     * @throws FrameworkJdbcRuntimeException
     */
    public List<String> getOrderColumnsByTable(String schema, String table) throws FrameworkJdbcRuntimeException;

    /**
     * 获取数据表字段名，此方法不进行排序
     *
     * @param schema 模式 可选参数
     * @param table  指定表名
     * @return 表中字段名数组
     * @throws FrameworkJdbcRuntimeException
     */
    public List<String> getColumnsByTable(String schema, String table) throws FrameworkJdbcRuntimeException;

    /**
     * 删除指定索引
     *
     * @param schema 模式 可选参数
     * @param index  指定索引名
     * @throws FrameworkJdbcRuntimeException
     */
    public boolean dropIndex(String schema, String index) throws FrameworkJdbcRuntimeException;

    /**
     * 修改表名称
     *
     * @param schema       模式 可选参数
     * @param tableName    原表名
     * @param newTableName 新表名
     * @throws Exception
     */
    public void renameTableName(String schema, String tableName, String newTableName);

    /**
     * 删除指定表
     *
     * @param schema 模式 可选参数
     * @param table  指定表名
     * @throws FrameworkJdbcRuntimeException
     */
    public boolean dropTable(String schema, String table);

    /**
     * 清空表内数据
     *
     * @param schema    模式 可选参数
     * @param tableName 指定表名
     */
    public void clearTableData(String schema, String tableName);

    /**
     * 删除指定表
     *
     * @param schema   模式 可选参数
     * @param sequence 指定Sequence名称
     * @throws FrameworkJdbcRuntimeException
     */
    public boolean dropSequence(String schema, String sequence);

    /**
     * 获取字符串类型，数据库定义
     *
     * @param length 长度
     * @return 返回string类型当前数据库对应定义字符串
     */
    public String getDefStringType(int length);

    /**
     * 获得日期类型，数据库定义
     *
     * @return 返回Date类型当前数据库对应定义字符串
     */
    public String getDefDateType();

    /**
     * 获得数字类型，数据库定义
     *
     * @return 返回Nuber类型当前数据库对应定义字符串
     */
    public String getDefNumberType();

    /**
     * 执行moduleId、sqlId对应sql返回Map结果
     *
     * @param moduleId 模块唯一标识
     * @param sqlId    sql唯一标识
     * @param params   参数列表 paramName 对应sql中定义的#paramName# value实际参数值
     * @return 返回结果列表 key 字段名称大写 字段对应类型对象
     */
    public Map<String, Object> queryMapForUnique(String moduleId, String sqlId, Map<String,String> params);

    /**
     * 执行moduleId、sqlId对应sql返回Map列表
     *
     * @param moduleId 模块唯一标识
     * @param sqlId    sql唯一标识
     * @return 返回结果列表 key 字段名称大写 字段对应类型对象
     */
    public List<Map<String, Object>> queryMapForList(String moduleId, String sqlId);

    /**
     * 执行moduleId、sqlId对应sql返回Map列表
     *
     * @param moduleId 模块唯一标识
     * @param sqlId    sql唯一标识
     * @param params   参数列表 paramName 对应sql中定义的#paramName# value实际参数值
     * @return 返回结果列表 key 字段名称大写 字段对应类型对象
     */
    public List<Map<String, Object>> queryMapForList(String moduleId, String sqlId, Map<String,String> params);


    /**
     * 此方法支持分页，执行moduleId、sqlId对应sql返回Map列表
     *
     * @param querySql 查询sql
     * @param page     分页对象
     * @return 返回结果Page对象，每个元素为一个Map key 字段名称大写 字段对应类型对象
     */
    public void queryMapForList(String querySql, Page<Map<String, Object>> page);

    /**
     * 此方法支持分页，执行moduleId、sqlId对应sql返回Map列表
     *
     * @param moduleId 模块唯一标识
     * @param sqlId    sql唯一标识
     * @param params   参数列表 paramName 对应sql中定义的#paramName# value实际参数值
     * @param page     分页对象
     */
    public void queryMapForList(String moduleId, String sqlId, Map<String,String> params, Page<Map<String, Object>> page);

    /**
     * 运行sql脚本
     *
     * @param reader      sql脚本的reader
     * @param stopOnError 在有错误时是否停止 true 停止 false继续执行 默认false
     * @param autoCommit  是否自动提交 true自动提交 false最终提交 默认false
     */
    public ProcessResult<String[]> runScript(Reader reader, boolean stopOnError, boolean autoCommit);

    /**
     * 创建新表
     *
     * @param tableName 表名称
     * @return 失败返回错误信息
     */
    public int createNewTable(String tableName);

    /**
     * 批处理更新操作
     *
     * @param sqls
     * @return 返回更新的列的数量
     * @throws Exception
     */
    public int[] executeUpdateSql(String[] sqls);

    /**
     * 批处理更新操作
     *
     * @param sqlWrapper
     * @return 返回更新的列的数量
     * @throws Exception
     */
    public int[] executeUpdateSql(SqlWrapper sqlWrapper);

    /**
     * 基于Jdbc进行分页，可以添加查询条件，可以自定义结果包装器
     *
     * @param sql       用来查询具体记录信息的sql
     * @param countSql  用来查询记录总数的sql
     * @param params    参数值，如果有条件参数的话
     * @param page      Pagination对象，结果信息就在这个Pagination对象当中
     * @param rowMapper 返回结果的包装器
     */
    public <T> void paginationQuery(String sql, String countSql, Object params[], Page<T> page, final RowMapper<T> rowMapper);

    /**
     * 替换参数后的sql语句
     *
     * @param moduleId 模块唯一标识
     * @param sqlName  sql名称
     * @param params   sql语句中的实参
     * @return
     * @throws TemplateException 
     * @throws IOException 
     * @throws Exception
     */
    public String getFormatSql(String moduleId, String sqlName, Map<String, String> params);


}
