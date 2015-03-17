package com.innofi.component.metadata.table.service;

import java.io.IOException;
import java.util.List;

import com.innofi.framework.pojo.metadata.MdTable;
import com.innofi.framework.service.IBaseService;

public interface IMdTableService extends IBaseService<MdTable, String> {

    /**
     * 根据表名获取最新版本MdTable对象
     *
     * @param tableName 表名
     * @return mdTable对象
     */
    public MdTable findLastVersionMdTableByTableName(String tableName);

    /**
     * 根据实体对象名获取最新版本MdTable对象
     *
     * @param entityName 实体对象名，包含类路径
     * @return mdTable对象
     */
    public MdTable findLastVersionMdTableByEntityName(String entityName);

    /**
     * 通过表名从数据库中读取表信息
     *
     * @param tableName 表名
     * @return MdTable 表对象
     */
    public MdTable findMdTableFromDb(String tableName);


    /**
     * 更新表结构创建新的版本
     *
     * @param newVerMdTable 更新的表对象
     */
    public void updateForNewVersion(MdTable newVerMdTable);

    /**
     * 刷新表结构元数据，包括表及对应字段元数据
     *
     * @param tableName 表名称
     * @return 0-刷新成功差生新版本,1-无变化
     */
    public char refreshTableMetaData(String tableName,List<MdTable> allTables);

    /**
     * 通过状态获取表最新的数据
     *
     * @param status 对象状态
     * @return List<MdTable> 状态对应的结果集
     */
    public List<MdTable> findMdTableByStatus(String status);

    /**
     * @param @param  filepath 文件路径
     * @param @return 设定文件
     * @return boolean    返回类型
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IOException
     * @throws
     * @Title: saveExcelData
     * @Description: 解析excel数据导入
     */
    public boolean saveExcelData(String filepath) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException;

    /**
     * 通过uiName得到对应的表名.
     *
     * @param uiName
     * @return
     */
    public String findLastVersionTableNameByUiName(String uiName);

    /**
     * 打开表数据持久化开关，支持对该表的新增、修改、删除操作。
     *
     * @param tableName 数据表名
     * @return 成功 true 失败 false
     */
    public boolean openPersistenceSwitch(String tableName);

    /**
     * 打开表数据持久化开关，支持对该表的新增、修改、删除操作。
     *
     * @param tableName 数据表名
     * @return 成功 true 失败 false
     */
    public boolean closePersistenceSwitch(String tableName);

}