/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.component.metadata.tablespace.service;

import java.util.List;
import java.util.Map;

import com.innofi.framework.pojo.metadata.MdTableSpace;
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
public interface IMdTableSpaceService extends IBaseService<MdTableSpace, String> {
    /**
     * 根据状态查询表空间信息
     *
     * @param status 状态 1有效 0 无效
     * @return MdTableSpace对象List
     */
    public List<MdTableSpace> findMdTableSpacesByStatus(String status);

    /**
     * 根据参数刷新表空间
     *
     * @return 0无变化 1成功
     */
    public String refreshMetaData(Map<String, Object> parameter);


    /**
     * 刷新单个表空间
     *
     * @return 0无变化 1成功
     */
    public int refreshTbsMetaData(String tbsName);

    /**
     * 刷新所有表空间
     *
     * @return 0无变化 1成功
     */
    public int refreshAllTbsMetaData();


}