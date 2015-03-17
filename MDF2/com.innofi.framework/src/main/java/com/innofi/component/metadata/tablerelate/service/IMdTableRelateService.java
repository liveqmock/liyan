/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.component.metadata.tablerelate.service;

import java.util.List;

import com.innofi.framework.pojo.metadata.MdTableRelate;
import com.innofi.framework.service.IBaseService;

/**
 * 功能/ 模块：测试模块
 *
 * @author 张磊 Alex.zhang@innofi.com.cn
 * @version 2.0.0 12-11-30
 *          测试Service接口
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
public interface IMdTableRelateService extends IBaseService<MdTableRelate,String>{


    /**
     * 根据表名获取最新版本MdTableRelatePojo列表
     * @param tableName 表名
     * @return List<MdTableRelate> 对象列表
     */
    public List<MdTableRelate> findLastVersionMdTableRelatePojosByTableName(String tableName);

    /**
     * 通过状态获取最新的数据
     * @param status 对象状态
     * @return List<MdTable> 状态对应的结果集
     */
	public List<MdTableRelate> findMdTableRelateByStatus(String status);

    /**
     * 通过表名从数据库中读取关联关系
     * @param tableName 表名
     * @return 字段列表
     */
    public List<MdTableRelate> findMdTableRelateByTableNameFormDb(String tableName);
    
    /**
     * 查询数据库表关联关系
     */    
    public List<MdTableRelate> findAllMdTableRelateFormDb();

    /**
     * 刷新表关联关系元数据
     * @param tableId 表ID
     * @param tableName 表名称
     * @param preVerMdTableRelates 当前表关联关系
     */
    public void refreshTableRelateMdMeta(String tableId , String tableName , List<MdTableRelate> preVerMdTableRelates);

    /**
     * 根据外键类型获取表关联关系
     * @param isCrt 0逻辑 1物理
     * @return List<MdTableRelate> 列表
     */
    public List<MdTableRelate> findMdTableRelatePojosByIsCrt(String isCrt);

    /**
     * 检查是否存在关联关系
     * @param mainTableName 主表名称
     * @param foreignTableName 从表名称
     * @return 成功,代表有记录.失败代表没有记录.
     */
    public String checkMetaModelRelate(String mainTableName,String foreignTableName);
    
    /**
     * 删除物理关系
     * @param tableId
     * @return
     */
    public void removeRelatePMdMeta(String tableId);

    /**
     * 根据主表名称查找从表关联关系
     * @param mainTableName 主表名称
     * @return 对应的关联关系信息
     */
    public List<MdTableRelate> findMdTableRelateByMainTable(String mainTableName);
}