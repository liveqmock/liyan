package com.innofi.component.metadata.adapter;

import java.util.Map;

/**
 * 功能/ 模块：元数据适配器
 *
 * @author 张磊  Alex.zhang@innofi.com.cn
 * @version 2.0.0 12-11-30
 *          元数据适配器接口
 *          修订历史：
 *          日期:2012-12-24
 *          作者:张磊  Alex.zhang@innofi.com.cn
 *          北京名晟信息技术有限公司版权所有.
 */
public interface IMdMetaAdapter {

    /**
     * 刷新元数据，目前只针对数据库进行了实现
     *
     * @param parameter
     * @return
     */
    public String refreshMetaData(Map<String, Object> parameter);

}
