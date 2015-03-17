/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.component.metadata.entityobject.service;

import com.innofi.framework.pojo.metadata.MdEntityObject;
import com.innofi.framework.service.IBaseService;

/**
 * 功能/ 模块：元数据模块
 *
 * @author  张磊 Alex.zhang@innofi.com.cn
 * @version 2.0.0 12-11-30
 *          测试Service接口
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
public interface IMdEntityObjectService extends IBaseService<MdEntityObject, String> {

    /**
     * 刷新程序元数据
     *
     * @param codePath 源代码存储路径
     */
    public String refreshProgramMetadata(String codePath);


}