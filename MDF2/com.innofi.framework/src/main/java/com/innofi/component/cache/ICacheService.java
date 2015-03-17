/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.component.cache;

import com.innofi.framework.pojo.BasePojo;

import java.io.Serializable;
import java.util.List;

/**
 * 功能/ 模块：缓存接口
 *
 * @author 刘名寓 liumy2009@126.com
 * @version 2.0.0 12-11-30
 *          缓存接口
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
public interface ICacheService<T extends BasePojo, PK extends Serializable> {

    /**
     * 加载缓存对象方法
     *
     * @return 缓存对象列表
     */
    public List<T> loadCacheObjects();

    /**
     * 获取缓存对象代码字段名称
     *
     * @return 代码字段名称
     */
    public String getCodeFieldName();

    /**
     * 获取缓存对象中文字段名称
     *
     * @return 中文字段名称
     */
    public String getCnFieldName();

    /**
     * 缓存类型:temporary缓存半小时，如半小时缓存数据未被使用，将从缓存中移除数据，forever永久不会从缓存中移除
     * @return 缓存类型
     */
    public String getCacheType();

}
