/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.component.metadata.mddepend.service;

import com.innofi.framework.pojo.metadata.MdMdDepend;
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
public interface IMdMdDependService extends IBaseService<MdMdDepend, String> {
    /**
     * 通过旧的元数据ID更新新的表ID
     *
     * @param newID
     * @param oldId
     */
    public void updateMdDependMdIdAndDmdId(String newID, String oldId);
}