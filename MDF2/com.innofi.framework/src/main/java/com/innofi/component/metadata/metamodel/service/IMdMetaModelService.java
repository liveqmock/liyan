/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.component.metadata.metamodel.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.innofi.framework.pojo.metadata.MdMetaModel;
import com.innofi.framework.pojo.metadata.MdMetaModelAndDataViewTree;
import com.innofi.framework.service.IBaseService;

/**
 * 功能/ 模块：元模型模块
 *
 * @author 张磊 Alex.zhang@innofi.com.cn
 * @version 2.0.0 12-11-30
 *          元模型Service接口
 *          修订历史：第1次修订
 *          日期          作者     参考    描述
 *          2013年5月30日 刘名寓   删除重复的方法  findMetaModelsById() 用 BaseServic中的get()方法替换
 *          findMetaModelsByIdList() 用BaseService中的findByIds()方法替换
 *          北京名晟信息技术有限公司版权所有.
 */
public interface IMdMetaModelService extends IBaseService<MdMetaModel, String> {

    public List<MdMetaModel> findMetaModelsByParentId(String parentId);

    /**
     * 查询树并组装
     *
     * @param parameter
     * @return
     */
    public Collection<MdMetaModelAndDataViewTree> findRoots(Map parameter);

    /**
     * 为查看按钮查找表关系.
     *
     * @return
     */
    public String findRelateForLookButton(String mdmId);

    /**
     * 通过ID和savePath 查询元模型和关联关系的方法.
     *
     * @param id
     * @param savePath
     * @return
     */
    public List<Map<String, Object>> findMetaModelAndTableRelate(String id, String savePath);

}