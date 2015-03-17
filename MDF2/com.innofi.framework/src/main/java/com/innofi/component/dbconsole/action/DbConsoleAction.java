package com.innofi.component.dbconsole.action;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.entity.EntityEnhancer;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.entity.FilterType;
import com.bstek.dorado.data.provider.Page;
import com.innofi.component.dbconsole.pojo.*;
import com.innofi.component.dbconsole.service.IDbConsoleService;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司 all rights reserved.
 *
 * @author liumy2009@126.com
 * @version 2.0
 * @found date: 12-8-16
 * @found time: 下午5:46
 * <p/>
 * 数据库管理控制台控制器,与Dorado7粘合接口
 */
@Component("dbConsoleAction")
public class DbConsoleAction {

    @Resource(name = "dbConsoleService", type = IDbConsoleService.class)
    private IDbConsoleService dbConsoleService;

    /**
     * 加载数据库信息
     *
     * @param groupId  数据组编号
     * @param connName 数据库连接名称
     * @return 返回数据库信息
     */
    @DataProvider
    public DbConsoleDbInfo loadDbInfo(String groupId, String connName) {
        return dbConsoleService.loadDbInfo(groupId, connName);
    }

    /**
     * 加载工作组
     *
     * @return 工作组列表
     */
    @DataProvider
    public List<DbConsoleGroup> loadGroups() {
        return dbConsoleService.loadGroups();
    }

    /**
     * 加载连接列表
     *
     * @param groupId 工作组编号
     * @return 工作组下所有数据库连接对象列表
     */
    @DataProvider
    public List<DbConsoleConnection> loadConnections(String groupId) {
        return dbConsoleService.loadConnections(groupId);
    }

    /**
     * 加载数据库连接模板列表
     *
     * @return 数据模板列表
     */
    @DataProvider
    public List<DbConsoleTemplate> loadDbTemplates() {
        return dbConsoleService.loadDbTemplates();
    }

    /**
     * 加载数据库对象类型
     *
     * @param groupId  工作组编号
     * @param connName 连接名称
     * @return 返回对象类型列表
     */
    @DataProvider
    public List<DbConsoleObjectType> loadObjectTypes(String groupId,
                                                     String connName) {
        return dbConsoleService.loadObjectTypes(groupId, connName);
    }

    /**
     * 加载数据库当中Table、View、Sequence对象
     *
     * @param groupId    工作组编号
     * @param connName   连接名称
     * @param schema     schema名称
     * @param objectName 对象名称
     * @param types      对象类型
     * @return 符合条件的对象列表
     */
    @DataProvider
    public List<DbConsoleTable> loadObjects(String groupId, String connName,String schema, String objectName, String types) {
        return dbConsoleService.loadObjects(groupId, connName, schema, objectName, types);
    }

    /**
     * 加载数据库当指定 schema 下的 Table
     *
     * @param groupId  工作组编号
     * @param connName 连接名称
     * @param schema   schema名称
     * @return 符合条件的对象列表
     */
    @DataProvider
    public List<DbConsoleTable> loadTablesBySchema(String groupId,
                                                   String connName, String schema) {
        return dbConsoleService.loadTablesBySchema(groupId, connName, schema);
    }

    /**
     * 加载具体某个sequence
     *
     * @param groupId  工作组编号
     * @param connName 连接名称
     * @param schema   schema名称
     * @param seqName  sequence名称
     * @return 加载指定名称Sequence对象
     */
    @DataProvider
    public DbConsoleSequence loadSequence(String groupId, String connName,
                                          String schema, String seqName) {
        return dbConsoleService
                .loadSequence(groupId, connName, schema, seqName);
    }

    /**
     * 根据指定表名加载表中所有列
     *
     * @param groupId   工作组编号
     * @param connName  连接名称
     * @param schema    schema名称
     * @param tableName 数据表名称
     * @return 该表中所有列对象
     */
    @DataProvider
    public List<DbConsoleColumn> loadColumns(String groupId, String connName,
                                             String schema, String tableName) {
        return dbConsoleService.loadColumns(groupId, connName, schema,
                tableName);
    }

    /**
     * 查找默认配置的数据库类型
     *
     * @param groupId  工作组编号
     * @param connName 连接名称
     * @return 返回默认配置的数据库类型的集合
     */
    @DataProvider
    public List<DbConsoleColumn> loadColumnType(String groupId, String connName) {
        return dbConsoleService.loadColumnType(groupId, connName);
    }

    /**
     * 加载对象DDL
     *
     * @param groupId    工作组编号
     * @param connName   连接名称
     * @param type       类型 TABLE,VIEW,SEQUENCE
     * @param schema     模式名称
     * @param objectName 对象名称
     * @return 返回对象DDL语句
     */
    @Expose
    public String loadObjectDDL(String groupId, String connName, String type,
                                String schema, String objectName) {
        return dbConsoleService.loadObjectDDL(groupId, connName, type, schema,
                objectName);
    }

    /**
     * 加载数据库模式,加载*
     *
     * @param groupId  工作组编号
     * @param connName 数据库连接名称
     * @return 当前数据库中的Schemes
     */
    @DataProvider
    public List<DbConsoleSchema> loadSchemas(String groupId, String connName) {
        return dbConsoleService.loadSchemas(groupId, connName);
    }

    /**
     * 加载数据库模式，不加载*
     *
     * @param groupId  工作组编号
     * @param connName 数据库连接名称
     * @return 当前数据库中的Schemes
     */
    @DataProvider
    public List<DbConsoleSchema> loadSchemasForSqlEditor(String groupId,
                                                         String connName) {
        return dbConsoleService.loadSchemasForSqlEditor(groupId, connName);
    }

    /**
     * 打开数据连接，会将连接对应的数据源缓存起来
     *
     * @param groupId  工作组编号
     * @param connName 数据库连接名称
     * @return 不成功返回错误信息，成功返回null
     */
    @Expose
    public String openConnection(String groupId, String connName) {
        return dbConsoleService.openConnection(groupId, connName);
    }

    /**
     * 修改表名称
     *
     * @param groupId      工作组
     * @param connName     连接名称
     * @param schema       模式 可选参数
     * @param tableName    原表名
     * @param newTableName 新表名
     */
    @Expose
    public void renameTableName(String groupId, String connName, String schema,
                                String tableName, String newTableName) {
        dbConsoleService.renameTableName(groupId, connName, schema, tableName,
                newTableName);
    }

    /**
     * 修改SeqName
     *
     * @param groupId    工作组
     * @param connName   连接名称
     * @param schema     模式 可选参数
     * @param seqName    原序列名
     * @param newSeqName 新序列名
     */
    @Expose
    public void renameSequence(String groupId, String connName, String schema,
                               String seqName, String newSeqName) {
        dbConsoleService.renameSequence(groupId, connName, schema, seqName,
                newSeqName);
    }

    /**
     * 重置sequence值
     *
     * @param groupId  工作组
     * @param connName 连接名称
     * @param schema   模式 可选参数
     * @param seqName  原序列名
     */
    @Expose
    public void resetSequence(String groupId, String connName, String schema,
                              String seqName, String newSeqValue) {
        dbConsoleService.resetSequence(groupId, connName, schema, seqName,
                newSeqValue);
    }

    /**
     * 删除指定表
     *
     * @param groupId   工作组
     * @param connName  连接名称
     * @param schema    模式 可选参数
     * @param tableName 指定表名
     */
    @Expose
    public boolean dropTable(String groupId, String connName, String schema,
                             String tableName) {
        return dbConsoleService.dropTable(groupId, connName, schema, tableName);
    }

    /**
     * 清空表内数据
     *
     * @param groupId   工作组
     * @param connName  连接名称
     * @param schema    模式 可选参数
     * @param tableName 指定表名
     */
    @Expose
    public void clearTableData(String groupId, String connName, String schema,
                               String tableName) {
        dbConsoleService.clearTableData(groupId, connName, schema, tableName);
    }

    /**
     * 删除指定表
     *
     * @param groupId      工作组
     * @param connName     连接名称
     * @param schema       模式 可选参数
     * @param sequenceName 指定Sequence名称
     */
    @Expose
    public boolean dropSequence(String groupId, String connName, String schema,
                                String sequenceName) {
        return dbConsoleService.dropSequence(groupId, connName, schema,
                sequenceName);
    }

    /**
     * 执行查询，异步处理方法
     *
     * @param groupId   工作组
     * @param connName  连接名称
     * @param querySql  查询sql语句
     * @param pageNo    当前页面编号
     * @param limitSize 提取页数
     */
    @Expose
    public String executeQuery(String groupId, String connName,
                               String querySql, int pageNo, int limitSize) {
        return dbConsoleService.executeQuery(groupId, connName, querySql,
                pageNo, limitSize);
    }

    /**
     * 获取当前查询状态
     *
     * @return 任务的token
     */
    @Expose
    public String getQueryStatus(String token) {
        return dbConsoleService.getQueryStatus(token);
    }

    /**
     * 加载查询结果
     *
     * @param token 任务的token
     * @return 返回任务查询结果
     */
    @DataProvider
    public List<Map<String, Object>> loadTaskQueryData(String token) {
        return dbConsoleService.loadTaskQueryData(token);
    }

    /**
     * 停止任务执行
     *
     * @param token 任务的token
     */
    @Expose
    public void stopQuery(String token) {
        dbConsoleService.stopQuery(token);
    }

    /**
     * 创建新的数据表
     *
     * @param groupId   工作组编号
     * @param connName  连接名称
     * @param tableName 新表名称
     * @return
     */
    @Expose
    public int createNewTable(String groupId, String connName, String tableName) {
        return dbConsoleService.createNewTable(groupId, connName, tableName);
    }

    /**
     * 添加修改删除数据表列
     *
     * @param coll 列对象
     * @param map  查询参数
     * @throws java.lang.reflect.InvocationTargetException
     *
     * @throws IllegalAccessException
     */
    @DataResolver
    public void saveTableColumn(Collection<DbConsoleColumn> coll,Map<String, Object> map) throws InvocationTargetException,
            IllegalAccessException {
        String groupId = (String) map.get("groupId");
        String connName = (String) map.get("connName");
        String schema = (String) map.get("schema");
        String tableName = (String) map.get("tableName");
        for (Iterator<DbConsoleColumn> iter = EntityUtils.getIterator(coll,
                FilterType.ALL); iter.hasNext(); ) {
            DbConsoleColumn columnInfo = iter.next();
            columnInfo.setTableName(tableName);
            EntityState state = EntityUtils.getState(columnInfo);
            if (state.equals(EntityState.NEW)) {
                dbConsoleService.insertColumn(groupId, connName, schema,
                        tableName, columnInfo);
            }
            if (state.equals(EntityState.MODIFIED)) {
                EntityEnhancer entityEnhancer = EntityUtils
                        .getEntityEnhancer(columnInfo);
                String oldDefaultValue = null;
                if (entityEnhancer != null) {
                    Map<String, Object> oldValues = entityEnhancer
                            .getOldValues();
                    if (oldValues.get("defaultValue") != null) {
                        oldDefaultValue = (String) oldValues
                                .get("defaultValue");
                    }
                }

                DbConsoleColumn oldColumnInfo = new DbConsoleColumn();
                BeanUtils.copyProperties(oldColumnInfo, columnInfo);
                oldColumnInfo.setColumnName(EntityUtils.getOldString(
                        columnInfo, "columnName"));
                oldColumnInfo.setIsnullAble(EntityUtils.getOldBoolean(
                        columnInfo, "isnullAble"));
                oldColumnInfo.setColumnSize(EntityUtils.getOldString(
                        columnInfo, "columnSize"));
                oldColumnInfo.setIsprimaryKey(EntityUtils.getOldBoolean(
                        columnInfo, "isprimaryKey"));
                oldColumnInfo.setDefaultValue(oldDefaultValue);
                dbConsoleService.updateColumn(groupId, connName, schema,
                        tableName, oldColumnInfo, columnInfo);

            }
            if (state.equals(EntityState.DELETED)) {
                dbConsoleService.dropColumn(groupId, connName, schema,
                        tableName, columnInfo.getColumnName());
            }
        }
    }

    /**
     * 添加修改删除表数据
     *
     * @param coll 数据要更新的数据
     * @param map  参数
     * @throws Exception
     */
    @DataResolver
    public void saveTableData(Collection<Map<String, Object>> coll,
                              Map<String, Object> map) throws Exception {
        String groupId = (String) map.get("groupId");
        String connName = (String) map.get("connName");
        String tableName = (String) map.get("tableName");
        List<SqlWrapper> listSqlWrapper = new ArrayList<SqlWrapper>();
        for (Iterator<Map<String, Object>> iter = EntityUtils.getIterator(coll,
                FilterType.ALL); iter.hasNext(); ) {
            Map<String, Object> mapValues = iter.next();
            EntityState state = EntityUtils.getState(mapValues);

            if (state.equals(EntityState.NEW)) {
                listSqlWrapper.add(dbConsoleService.getInsertDataSql(tableName,
                        mapValues));
            }
            if (state.equals(EntityState.MODIFIED)) {
                Map<String, Object> oldMapValues = this
                        .getTableOldMapValues(mapValues);
                listSqlWrapper.add(dbConsoleService.getUpdateDataSql(tableName,
                        mapValues, oldMapValues));
            }
            if (state.equals(EntityState.DELETED)) {
                Map<String, Object> oldMapValues = this
                        .getTableOldMapValues(mapValues);
                listSqlWrapper.add(dbConsoleService.getDeleteDataSql(tableName,
                        oldMapValues));
            }
        }
        if (listSqlWrapper.size() > 0) {
            dbConsoleService.updateSql(groupId, connName, listSqlWrapper);// 事务操作
        }
    }

    /**
     * 加载数据表数据
     *
     * @param page       分页对象
     * @param parameters 查询参数
     * @throws Exception
     */
    @DataProvider
    public void loadQueryTableData(Page page, Map<String, Object> parameters)
            throws Exception {
        com.innofi.framework.dao.pagination.Page<Map<String, Object>> innofiPage = new com.innofi.framework.dao.pagination.Page<Map<String, Object>>(
                page.getPageSize(), page.getPageNo());
        dbConsoleService.loadQueryTableData(innofiPage, parameters);
        page.setEntities(innofiPage.getEntities());
        page.setEntityCount(innofiPage.getEntityCount());
    }

    /**
     * 格式化给定SQL
     *
     * @param sql 需要格式化的SQL语句
     * @return 格式化后的SQL语句
     */
    @Expose
    public String formatSql(String sql) {
        return dbConsoleService.formatSql(sql);
    }

    /**
     * 测试数据库连接是否可以连通
     *
     * @param groupId  工作组编号
     * @param connName 数据库连接名称
     * @return 不成功返回错误信息，成功返回null
     */
    @Expose
    public String testConnection(String groupId, String connName) {
        return dbConsoleService.testConnection(groupId, connName);
    }

    private Map<String, Object> getTableOldMapValues(
            Map<String, Object> mapValues) {
        EntityEnhancer entityEnhancer = EntityUtils
                .getEntityEnhancer(mapValues);
        if (entityEnhancer != null) {
            Map<String, Object> oldValues = entityEnhancer.getOldValues();
            if (oldValues != null) {
                return oldValues;
            } else {
                return mapValues;
            }
        }
        return null;
    }

}
