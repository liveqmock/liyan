package com.innofi.component.cache.service;

import java.util.*;

import com.innofi.component.cache.pojo.SysCacheConfig;
import com.innofi.framework.service.IBaseService;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司<p/>
 * all rights reserved.
 *
 * @author 刘名寓 liumy2009@126.com
 * @author liuhuaiyang
 * @version V2.0.0
 *          一个扩展自Spring EhCacheManagerFactoryBean的缓存工具类，这里使用的是EHCache为缓存实现，可以实现向缓存当中添加对象、更新对象、删除对象
 *          <p/> 缓存配置服务接口类
 *          日期 2014-09-05 22:07:59
 *          <p/>
 *          修订历史： 日期 作者 参考 描述 北京名晟信息技术有限公司版权所有.
 * @since JDK1.6
 */
public interface ISysCacheConfigService extends IBaseService<SysCacheConfig, String> {

    /**
     * 获取缓存配置文件路径
     *
     * @return 缓存文件路径
     */
    public String getConfigLocation();
    /**
     * 设置缓存配置文件路径
     *
     * @param configLocation 缓存文件配置路径
     */
    public void setConfigLocation(String configLocation);
    /**
     * 获取缓存服务器地址
     *
     * @return 缓存服务器地址
     */
    public String getTerracottaServer();

    /**
     * 设置缓存服务器地址
     *
     * @param terracottaServer 缓存服务器地址
     */
    public void setTerracottaServer(String terracottaServer);

    /**
     * 获取当前应用名称
     *
     * @return 当前应用程序名称
     */
    public String getApplicationName();

    /**
     * 设置当前应用名称
     *
     * @param applicationName 当前应用程序名称
     */
    public void setApplicationName(String applicationName);

    /**
     * 根据指定的key，从缓存当中获取一个对象
     *
     * @param key 缓存当中对象的key值
     * @return 返回缓存当中与给定key对应的对象值，如果对象不存在，就返回null
     */
    public Object getCacheObject(String key);

    /**
     * 从临时缓存当中获取一个被临时缓存对象，<br>
     * 默认情况下，位于临时缓存中对象生命周期为1800秒，也就是半小时
     *
     * @param key 缓存当中对象的key值
     * @return 返回缓存当中与给定key对应的对象值，如果对象不存在，就返回null
     */
    public Object getTemporaryCacheObject(String key);

    /**
     * 将一个对象放入缓存当中，同时如果缓存当中有存在相同key的对象，则进行覆盖
     *
     * @param key 对象的key
     * @param obj 具体对象
     */
    public void putCacheObject(String key, Object obj);
    /**
     * 将一个对象放入临时缓存当中，同时如果缓存当中有存在相同key的对象，则进行覆盖，<br>
     * 默认情况下，位于临时缓存中对象生命周期为1800秒，也就是半小时
     *
     * @param key 对象的key
     * @param obj 具体对象
     */
    public void putTemporaryCacheObject(String key, Object obj);

    /**
     * 从缓存当中移除一个对象
     *
     * @param key 要移除的对象的key值
     */
    public void removeCacheObject(String key);

    /**
     * 从临时缓存当中移除一个对象
     *
     * @param key 要移除的对象的key值
     */
    public void removeTemporaryCacheObject(String key);


    /**
     * 获取当前应用所有缓存注册器
     *
     * @return Set<String> cacheServiceBeanNames
     */
    public Set<String> getCacheServiceBeanNames();

    /**
     * @param cacheServiceBeanNames
     */
    public void loading(Set<String> cacheServiceBeanNames);

    /**
     * 根据缓存服务名称加载重新加载缓存
     *
     * @param cacheServiceBeanName 缓存服务名称
     */
    public void reloadCacheByCacheServiceBeanName(String cacheServiceBeanName);


    /**
     * 加载各种不同的国际化资源文件到缓存
     */
    public ArrayList<ResourceBundle> loadResourceBundles(Locale locale);

    /**
     * 根据缓存服务名称名称刷新缓存
     * @param cacheServiceBeanNames 服务名称集合
     */
    public void refreshCache(Set<String> cacheServiceBeanNames);

    /**
     * 根据服务名称获取缓存类型
     * @param cacheServiceBeanName 服务名称
     * @return 缓存类型
     */
    public String getSysCacheType(String cacheServiceBeanName);

    public String getSysCachePolicy(String cacheServiceBeanName);

    public SysCacheConfig getSysCacheConfigList(String cacheServiceBeanName);

    public Collection<SysCacheConfig> getSysCacheCofingListByPolicy(String cachePolicy);
}