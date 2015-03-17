
package com.innofi.component.cache.service.impl;


import com.innofi.framework.FrameworkConstants;
import com.innofi.component.cache.ICacheService;
import com.innofi.component.cache.pojo.SysCacheConfig;
import com.innofi.component.cache.service.ISysCacheConfigService;
import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.pojo.BasePojo;
import com.innofi.framework.service.impl.BaseServiceImpl;
import com.innofi.framework.spring.context.ContextHolder;
import com.innofi.framework.utils.console.ConsoleUtil;
import com.innofi.framework.utils.reflect.ReflectionUtil;
import com.innofi.framework.utils.resource.ResourceUtil;
import com.innofi.framework.utils.xml.XMLResourceBundleControl;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.*;
import org.apache.commons.collections.map.ListOrderedMap;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.*;


/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司<p/>
 * all rights reserved.
 *
 * @author 刘名寓 liumy2009@126.com
 * @version V2.0.0
 *          一个扩展自Spring EhCacheManagerFactoryBean的缓存工具类，这里使用的是EHCache为缓存实现，可以实现向缓存当中添加对象、更新对象、删除对象
 *          <p/> 缓存配置服务实现类
 *          日期 2014-09-05 22:07:59
 *          <p/>
 *          修订历史： 日期 作者 参考 描述 北京名晟信息技术有限公司版权所有.
 * @since JDK1.6
 */
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = {Exception.class,RuntimeException.class})
@Service(value = "sysCacheConfigService")
public class SysCacheConfigServiceImpl extends BaseServiceImpl<SysCacheConfig, String> implements ISysCacheConfigService, InitializingBean {

    @Resource(name = "sysCacheConfigDaoSupport")
    protected DaoSupport daoSupport;

    @Override
    public DaoSupport getDaoSupport() {
        return daoSupport;
    }

    /**
     * IDF框架缓存
     */
    private Cache idfCache;

    /**
     * IDF框架临时缓存
     */
    private Cache idfTemporaryCache;

    /**
     * IDF框架缓存管理器
     */
    private CacheManager cacheManager;

    /**
     * IDF框架缓存名称
     */
    private String IDF_CACHE_NAME = "mdf.application.cache";


    /**
     * IDF框架临时缓存名称
     */
    private String IDF_TEMPORARY_CACHE_NAME = "mdf.temporary.cache";

    /**
     * IDF框架临时文件存放路径，默认java.io.tmpdir/idf_tempdir
     */
    private String storePath = ContextHolder.getIdfTempFileStorePath();

    /**
     * 当前应用所有缓存注册器列表
     */
    private Set<String> cacheServiceBeanNames = new HashSet<String>();

    /**
     * IDF框架缓存配置文件路径
     */
    private String configLocation = "classpath:com/innofi/framework/cache/ehcache-configs.xml";

    /**
     * terracotta缓存服务器地址
     */
    private String terracottaServer = "${mdf.cache.terracottaServer}";

    /**
     * 当前应用名称
     */
    private String applicationName = "${mdf.applicationName}";


    private ISysCacheConfigService sysCacheConfigSevice;

    /**
     * 缓存类型集合
     */
    public static ListOrderedMap cacheTypeMap = new ListOrderedMap();

    /**
     * 缓存策略集合
     */
    public static ListOrderedMap cachePolicyMap = new ListOrderedMap();


    /**
     * 获取缓存配置文件路径
     *
     * @return 缓存文件路径
     */
    public String getConfigLocation() {
        return configLocation;
    }

    /**
     * 设置缓存配置文件路径
     *
     * @param configLocation 缓存文件配置路径
     */
    public void setConfigLocation(String configLocation) {
        this.configLocation = configLocation;
    }

    /**
     * 获取缓存服务器地址
     *
     * @return 缓存服务器地址
     */
    public String getTerracottaServer() {
        return terracottaServer;
    }

    /**
     * 设置缓存服务器地址
     *
     * @param terracottaServer 缓存服务器地址
     */
    public void setTerracottaServer(String terracottaServer) {
        this.terracottaServer = terracottaServer;
    }

    /**
     * 获取当前应用名称
     *
     * @return 当前应用程序名称
     */
    public String getApplicationName() {
        return applicationName;
    }

    /**
     * 设置当前应用名称
     *
     * @param applicationName 当前应用程序名称
     */
    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws IOException, CacheException {
        ConsoleUtil.info("Init ApplicationCache start...");

        IDF_CACHE_NAME = buildKey(IDF_CACHE_NAME);
        IDF_TEMPORARY_CACHE_NAME = buildKey(IDF_TEMPORARY_CACHE_NAME);
        File f = new File(storePath);
        if (!f.exists()) {
            f.mkdirs();
        }
        Configuration config = null;
        String configTerracottaServer = ContextHolder.getSystemProperties().getString("mdf.cache.terracottaServer");
        ConsoleUtil.info("terracottaServer is " + configTerracottaServer);
        //如果terracottaServer不为空，那么就采用terracotta server提供的集群式缓存策略，否则就采用单机缓存策略
        if (StringUtils.hasText(configTerracottaServer)) {
            config = new Configuration();
            TerracottaClientConfiguration terracottaConfig = new TerracottaClientConfiguration();
            terracottaConfig.setUrl(configTerracottaServer);
            config.addTerracottaConfig(terracottaConfig);

            CacheConfiguration cacheConfig = new CacheConfiguration();
            cacheConfig.setEternal(true);
            TerracottaConfiguration terracottaConfiguration = new TerracottaConfiguration();
            terracottaConfiguration.setSynchronousWrites(false);
            cacheConfig.addTerracotta(terracottaConfiguration);
            cacheConfig.setName(IDF_CACHE_NAME);
            cacheConfig.setMaxEntriesLocalHeap(5000);
            cacheConfig.setMaxElementsOnDisk(0);
            cacheConfig.setMemoryStoreEvictionPolicy("LFU");
            cacheConfig.setOverflowToDisk(false);
            config.addCache(cacheConfig);

            CacheConfiguration temporaryCacheConfig = new CacheConfiguration();
            temporaryCacheConfig.addTerracotta(terracottaConfiguration);
            temporaryCacheConfig.setEternal(false);
            temporaryCacheConfig.overflowToDisk(false);
            temporaryCacheConfig.setTimeToIdleSeconds(1800);
            temporaryCacheConfig.setTimeToLiveSeconds(1800);
            temporaryCacheConfig.setDiskPersistent(false);
            temporaryCacheConfig.setDiskExpiryThreadIntervalSeconds(1000);
            temporaryCacheConfig.setName(IDF_TEMPORARY_CACHE_NAME);
            temporaryCacheConfig.setMaxEntriesLocalHeap(5000);
            temporaryCacheConfig.setMaxElementsOnDisk(0);
            temporaryCacheConfig.setMemoryStoreEvictionPolicy("LFU");
            config.addCache(temporaryCacheConfig);
        } else {

            try {
                URL configLocationURL = ResourceUtil.getURL(configLocation);
                config = ConfigurationFactory.parseConfiguration(configLocationURL);
            } catch (FileNotFoundException fnfe) {
                ConsoleUtil.warn("not found ehcache-configs.xml");
                config = new Configuration();
            }

            CacheConfiguration cacheConfig = new CacheConfiguration();
            cacheConfig.setName(IDF_CACHE_NAME);                //设置缓存名称
            cacheConfig.setMaxEntriesLocalHeap(5000);           //内存中最大缓存对象数
            cacheConfig.setEternal(true);                       //Element是否永久有效，设置了该属性为true，timeout将不起作用。
            cacheConfig.setOverflowToDisk(true);                //配置此属性，当内存中Element数量达到maxEntriesLocalHeap时，Ehcache将会Element写到磁盘中。
            cacheConfig.setDiskSpoolBufferSizeMB(300);          //这个参数设置DiskStore（磁盘缓存）的缓存区大小。默认是300MB。每个Cache都应该有自己的一个缓冲区
            cacheConfig.setMaxElementsOnDisk(0);                //设置硬盘存储Element个数限制，0代表无限
            cacheConfig.setMemoryStoreEvictionPolicy("LRU");    //默认策略是LRU（最近最少使用）。你可以设置为FIFO（先进先出）或是LFU（较少使用）。这里比较遗憾，Ehcache并没有提供一个用户定制策略的接口，仅仅支持三种指定策略。
            config.addCache(cacheConfig);

            CacheConfiguration temporaryCacheConfig = new CacheConfiguration();
            temporaryCacheConfig.setEternal(false);
            temporaryCacheConfig.setName(IDF_TEMPORARY_CACHE_NAME);
            temporaryCacheConfig.setMaxEntriesLocalHeap(5000);
            temporaryCacheConfig.setMaxElementsOnDisk(0);
            temporaryCacheConfig.setMemoryStoreEvictionPolicy("LRU");
            temporaryCacheConfig.setTimeToIdleSeconds(1800);
            temporaryCacheConfig.setDiskExpiryThreadIntervalSeconds(1000);
            temporaryCacheConfig.setDiskSpoolBufferSizeMB(300);

            config.addCache(temporaryCacheConfig);

        }
        config.setName("cache_manager_" + applicationName);
        this.cacheManager = new CacheManager(config);
        idfCache = cacheManager.getCache(IDF_CACHE_NAME);
        idfTemporaryCache = cacheManager.getCache(IDF_TEMPORARY_CACHE_NAME);
/*        // 获取数据库配置的缓存数据
        List<SysCacheConfig> sysCacheConfigList = this.getDaoSupport().getHibernateDao().findByHql(this.getEntityType(), "from SysCacheConfig");
        if (null != sysCacheConfigList && sysCacheConfigList.size() > 0) {
            for (SysCacheConfig sysCacheConfig : sysCacheConfigList) {
                // 获取springbeanId
                String beanName = sysCacheConfig.getObjName();
                Assert.notNull(ContextHolder.getSpringBean(beanName), MessageFormat.format("无法找到beanName为[{0}]的缓存Service，请检查配置!", beanName));
                Assert.isInstanceOf(ICacheService.class, ContextHolder.getSpringBean(beanName), MessageFormat.format("beanName[{0}]未实现ICache接口，不能进行缓存!", beanName));
                ICacheService cacheService = ContextHolder.getSpringBean(beanName);
                String cnFieldName = cacheService.getCnFieldName();
                String codeFieldName = cacheService.getCodeFieldName();
                String cacheType = sysCacheConfig.getCacheType();
                String cachePolicy = sysCacheConfig.getCachePolicy();
                cacheTypeMap.put(beanName, cacheType);
                cachePolicyMap.put(beanName, cachePolicy);
                Assert.notNull(cnFieldName, "CodeFieldName属性不能为空");
                Assert.notNull(codeFieldName, "CnFieldName属性不能为空");
                ConsoleUtil.info(MessageFormat.format("Loading CacheRegister CacheServiceBeanName[{0}] CacheType[{1}] ", beanName, cacheType));
                cacheServiceBeanNames.add(beanName);
            }
        }*/

        //加载缓存
        loading(cacheServiceBeanNames);

        // 加载各种国际化资源文件到缓存 中文信息
        loadResourceBundles(Locale.CHINA);

        // 加载各种国际化资源文件到缓存 英文信息
        loadResourceBundles(Locale.US);

        ConsoleUtil.info("Init ApplicationCache end...");
    }

    /**
     * 根据指定的key，从缓存当中获取一个对象
     *
     * @param key 缓存当中对象的key值
     * @return 返回缓存当中与给定key对应的对象值，如果对象不存在，就返回null
     */
    public Object getCacheObject(String key) {
        Element objElement = idfCache.get(buildKey(key));
        if (objElement != null) {
            return objElement.getValue();
        }
        return null;
    }

    /**
     * 从临时缓存当中获取一个被临时缓存对象，<br>
     * 默认情况下，位于临时缓存中对象生命周期为1800秒，也就是半小时
     *
     * @param key 缓存当中对象的key值
     * @return 返回缓存当中与给定key对应的对象值，如果对象不存在，就返回null
     */
    public Object getTemporaryCacheObject(String key) {
        Element objElement = idfTemporaryCache.get(buildKey(key));
        if (objElement != null) {
            return objElement.getValue();
        }
        return null;
    }


    /**
     * 将一个对象放入缓存当中，同时如果缓存当中有存在相同key的对象，则进行覆盖
     *
     * @param key 对象的key
     * @param obj 具体对象
     */
    public synchronized void putCacheObject(String key, Object obj) {
        ConsoleUtil.info(MessageFormat.format("cacheType forever put object cache key [{0}]", key));
        idfCache.put(new Element(buildKey(key), obj));
    }

    /**
     * 将一个对象放入临时缓存当中，同时如果缓存当中有存在相同key的对象，则进行覆盖，<br>
     * 默认情况下，位于临时缓存中对象生命周期为1800秒，也就是半小时
     *
     * @param key 对象的key
     * @param obj 具体对象
     */
    public synchronized void putTemporaryCacheObject(String key, Object obj) {
        ConsoleUtil.info(MessageFormat.format("cacheType temporary put object cache key [{0}]", key));
        idfTemporaryCache.put(new Element(buildKey(key), obj));
    }

    /**
     * 从缓存当中移除一个对象
     *
     * @param key 要移除的对象的key值
     */
    public synchronized void removeCacheObject(String key) {
        ConsoleUtil.info(MessageFormat.format("cacheType temporary remove object cache key [{0}]", key));
        idfCache.remove(buildKey(key));
    }

    /**
     * 从临时缓存当中移除一个对象
     *
     * @param key 要移除的对象的key值
     */
    public void removeTemporaryCacheObject(String key) {
        idfTemporaryCache.remove(buildKey(key));
    }

    /*
     * 获取当前缓存名称 key+applicationName
     * @param key mdf.application.cache
     * @return
     */
    private String buildKey(String key) {
        return this.applicationName + "_" + key;
    }

    /**
     * 获取当前应用所有缓存注册器
     *
     * @return Set<String> cacheServiceBeanNames
     */
    public Set<String> getCacheServiceBeanNames() {
        return cacheServiceBeanNames;
    }

    /**
     * @param cacheServiceBeanNames
     */
    public void loading(Set<String> cacheServiceBeanNames) {
        //加载缓存类型和缓存策略
        putCacheObject(FrameworkConstants.CACHE_TYPE_KEY, cacheTypeMap);
        putCacheObject(FrameworkConstants.CACHE_POLICY_KEY, cachePolicyMap);
        for (String cacheServiceBeanName : cacheServiceBeanNames) {
            reloadCacheByCacheServiceBeanName(cacheServiceBeanName);
        }
    }

    /**
     * 根据缓存服务名称加载重新加载缓存
     *
     * @param cacheServiceBeanName 缓存服务名称
     */
    public void reloadCacheByCacheServiceBeanName(String cacheServiceBeanName) {
        ICacheService cacheService = ContextHolder.getSpringBean(cacheServiceBeanName);
        List<BasePojo> cacheObjects = cacheService.loadCacheObjects();
        ListOrderedMap listOrderedMap = new ListOrderedMap();

        for (BasePojo basePojo : cacheObjects) {
            Object codeValue = ReflectionUtil.getFieldValue(basePojo, cacheService.getCodeFieldName());
            listOrderedMap.put(codeValue, basePojo);
        }

        //根据sys_cache_config的springbeanId获取缓存的类型
        String cacheType = getSysCacheType(cacheServiceBeanName);
        if (FrameworkConstants.CACHE_TYPE_TEMPORARY.equals(cacheType)) {
            putTemporaryCacheObject(cacheServiceBeanName, listOrderedMap);
        } else {
            putCacheObject(cacheServiceBeanName, listOrderedMap);
        }
    }


    /*
     * 加载各种不同的国际化资源文件到缓存
     */
    public ArrayList<ResourceBundle> loadResourceBundles(Locale locale) {
        ArrayList bundleList = null;

        // 获取资源文件缓存
        HashMap bundleMap = (HashMap) getCacheObject(FrameworkConstants.RESOURCE_BUNDLE_CACHE);
        if (bundleMap == null) {
            // 读取资源文件
            bundleList = XMLResourceBundleControl.getResourceBundles(locale);
            if (bundleList != null && bundleList.size() > 0) {
                bundleMap = new HashMap();

                bundleMap.put(locale.toString(), bundleList);
                putCacheObject(FrameworkConstants.RESOURCE_BUNDLE_CACHE, bundleMap);
            }
        } else {
            bundleList = (ArrayList) bundleMap.get(locale.toString());
            if (bundleList == null || bundleList.size() <= 0) {
                // 读取资源文件
                bundleList = XMLResourceBundleControl.getResourceBundles(locale);
                if (bundleList != null && bundleList.size() > 0) {
                    bundleMap.put(locale.toString(), bundleList);
                    putCacheObject(FrameworkConstants.RESOURCE_BUNDLE_CACHE, bundleMap);
                }
            }
        }

        return bundleList;
    }


    /**
     * 刷新缓存
     */
    public void refreshCache(Set<String> cacheServiceBeanNames) {
        for (String cacheServiceBeanName : cacheServiceBeanNames) {
            reloadCacheByCacheServiceBeanName(cacheServiceBeanName);
        }
    }

    /**
     * 根据服务名称获取缓存类型
     */
    public String getSysCacheType(String cacheServiceBeanName) {

        String cacheType = "";
        ListOrderedMap listCacheMap = (ListOrderedMap) getCacheObject(FrameworkConstants.CACHE_TYPE_KEY);
        if (null != listCacheMap) {
            cacheType = (String) listCacheMap.get(cacheServiceBeanName);
        }
        if (null != cacheType && !"".equals(cacheType)) {
            return cacheType;
        }

        // 根据SYS_CACHE_CONFIG的SpringBeanId获取缓存的类型
        List<SysCacheConfig> sysCacheConfigList = getDaoSupport().getHibernateDao().findByHql(this.getEntityType(), "from SysCacheConfig where objName=?", new Object[]{cacheServiceBeanName});
        if (null != sysCacheConfigList && sysCacheConfigList.size() > 0) {
            SysCacheConfig cacheConfig = sysCacheConfigList.get(0);
            if (null != cacheConfig) {
                cacheType = cacheConfig.getCacheType();
            }
        }
        return cacheType;
    }

    /**
     * 根据服务名称获取缓存策略
     */
    public String getSysCachePolicy(String cacheServiceBeanName) {

        String cachePolicy = "";
        ListOrderedMap listCacheMap = (ListOrderedMap) getCacheObject(FrameworkConstants.CACHE_POLICY_KEY);
        if (null != listCacheMap) {
            cachePolicy = (String) listCacheMap.get(cacheServiceBeanName);
        }
        if (null != cachePolicy && !"".equals(cachePolicy)) {
            return cachePolicy;
        }
        // 根据sys_cache_config的springbeanId获取缓存的类型
        List<SysCacheConfig> sysCacheConfigList = this.findByHql(
                "from SysCacheConfig where objName=?",
                new Object[]{cacheServiceBeanName});
        if (null != sysCacheConfigList && sysCacheConfigList.size() > 0) {
            SysCacheConfig cachecofing = sysCacheConfigList.get(0);
            if (null != cachecofing) {
                cachePolicy = cachecofing.getCachePolicy();
            }
        }
        return cachePolicy;
    }

    public SysCacheConfig getSysCacheConfigList(String cacheServiceBeanName) {
        // 根据sys_cache_config的springbeanId获取缓存的类型
        List<SysCacheConfig> sysCacheConfigList = this.getDaoSupport().getHibernateDao().findByHql(this.getEntityType(),
                "from SysCacheConfig where objName=?",
                new Object[]{cacheServiceBeanName});
        SysCacheConfig cachecofing = null;
        if (null != sysCacheConfigList && sysCacheConfigList.size() > 0) {
            cachecofing = sysCacheConfigList.get(0);
        }
        return cachecofing;
    }

    public Collection<SysCacheConfig> getSysCacheCofingListByPolicy(String cachePolicy) {
        List<SysCacheConfig> sysCacheConfigList = this.getDaoSupport().getHibernateDao().findByHql(this.getEntityType(), "from SysCacheConfig where cachePolicy=?",
                new Object[]{cachePolicy});

        return sysCacheConfigList;
    }
}

