/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.component.metadata.field.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.innofi.framework.pojo.metadata.MdField;
import com.innofi.framework.service.IBaseService;
import com.innofi.framework.utils.xml.XmlParseException;

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
public interface IMdFieldService extends IBaseService<MdField, String> {

    /**
     * 根据表物理名称获取最新版本MdField列表
     * @param tableId 表ID
     * @return List<MdField> mdFields
     */
    public List<MdField> findMdFieldsByTableId(String tableId);

    /**
     * 根据表物理名称获取最新版本MdField列表
     *
     * @param tableName 表物理名称
     * @return List<MdField> mdFields
     */
    public List<MdField> findLastVersionMdFieldsByTableName(String tableName);

    /**
     * 比较表字段版本差异
     * @param tableName 表名
     * @param verNo1    较早版本
     * @param verNo2    较晚版本
     * @return key 版本号 value List<MdField> 通过对象getUpdateFlg获取变化情况 1.新增 2.修改 3.删除 4.无变化
     */
    public Map<BigDecimal,List<MdField>> compareFields(String tableName, BigDecimal verNo1, BigDecimal verNo2);

    /**
     * 根据表物理名称刷新字段元数据
     * @param  tableName 表物理名称
     * @param  tableName 当前有效版本字段列表
     * @return 返回变化的字段列表,无变化时列表size为0
     */
    public List<MdField> refreshMdFieldMetaData(String tableName , List<MdField> preVerFields , List<MdField> dbMdFields);

    /**
     * 根据表名从数据库中读取字段列表
     * @param tableName 表名
     * @return 字段列表
     */
    public List<MdField> findMdFieldsByTableNameFromDb(String tableName);


    /**
     * 通过对象.表ID,和修改后的字段ID来更新表字段信息.
     *
     * @param mdFields
     * @param tableId
     * @param fieldIds
     */
    public void createNewObject(Collection<MdField> mdFields, String tableId, List<String> fieldIds);
    
    /**
     * 
    * @Title: refreshMdFieldLabel 
    * @Description: 刷新表label 
    * @param @throws IOException
    * @param @throws XmlParseException
    * @param @throws ClassNotFoundException    设定文件 
    * @return void    返回类型 
    * @throws
     */
    public void refreshMdFieldLabel() throws IOException, XmlParseException, ClassNotFoundException;


    public void saveMdFieldsforDynamictPojoManage(MdField mdField);

    public void updateMdFieldsforDynamictPojoManage(MdField mdField,Boolean active);
}