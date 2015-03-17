package com.innofi.framework.service;

import com.innofi.framework.utils.console.ConsoleUtil;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.WebApplicationContext;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

/**
 * {@link com.innofi.framework.service.InstanceService}接口的实现类.
 * <p>
 * 定义了取得InstanceName的策略:<br/>
 * <ol>
 * <li>要求在mdf.properties中设置了从系统属性中取得InstanceName的Key,并据此将属性得的属性值作为InstanceName;</li>
 * <li>如果未设置,且strict属性被设置为true,则抛出异常;</li>
 * <li>如果未设置,且strict属性被设置为false,则以机器名+时分秒作为InstanceName;</li>
 * <li>如果取得机器名时出现异常,则以IDF+时分作为InstanceName.</li>
 * </ol>
 * </p>
 *
 * @author liumy2009@126.com
 * @version 2.0
 */
public class InstanceServiceImpl implements InstanceService, InitializingBean,
        ApplicationContextAware {

    private static final Logger log = LoggerFactory.getLogger(InstanceServiceImpl.class.getName());
    private static final boolean isDebugEnabled = log.isDebugEnabled();

    /*
      * (non-Javadoc)
      *
      * @see
      * com.innofi.framework.service.InstanceNameProvider#getInstanceName()
      */
    public String getInstanceName() {
        return this.instanceName;
    }

    /*
      * (non-Javadoc)
      *
      * @see com.innofi.framework.service.InstanceService#getAppRootPath()
      */
    public String getAppRootPath() {
        if (WebApplicationContext.class
                .isAssignableFrom(this.applicationContext.getClass())) {
            WebApplicationContext wac = (WebApplicationContext) this.applicationContext;
            String appRootPath = wac.getServletContext().getRealPath("/");
            if (appRootPath.endsWith("/") || appRootPath.endsWith("\\")) {
                appRootPath = appRootPath
                        .substring(0, appRootPath.length() - 1);
            }
            return appRootPath;
        } else {
            return null;
        }
    }

    /*
      * (non-Javadoc)
      *
      * @see
      * org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
      */
    public void afterPropertiesSet() throws Exception {
        this.innerGetInstanceName();
        ConsoleUtil.info(String.format("******** Using [%s] as instance name", this.instanceName)+ " ************");
    }

    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 按System.getProperty(instanceNameKey)>generate的顺序产生一个实例名
     */
    synchronized protected void innerGetInstanceName() {
        if (null != this.instanceName) {
            if (isDebugEnabled) {
                log.debug("InstanceServiceImpl.instanceName was setted in spring,using it as instanceName");
            }
            return;
        }
        instanceName = System.getProperty(this.instanceNameKey);
        if (null != instanceName) {
            if (isDebugEnabled) {
                log.debug(String
                        .format("Found system property with key [%s],using it as instanceName.",
                                this.instanceName));
            }
            return;
        }
        if (restrict) {
            throw new RuntimeException(String.format(
                    "Cann't find system property with key [%s]",
                    this.instanceNameKey));

        }
        instanceName = generateInstanceName();
    }

    /**
     * 根据HostName和当前时间产生一个实例名.
     *
     * @return 生成的实例名
     */
    protected String generateInstanceName() {
        if (isDebugEnabled) {
            log.debug("Generating instance name, try to use hostname + HHmmss ...");
        }
        String instanceName = null;
        String hourMinAndSec = DateFormatUtils.format(new Date(), "HHmmss");
        try {
            instanceName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            instanceName = DEFAULT_INSTANCE_NAME;
        }
        return instanceName;
    }

    private String instanceNameKey = null;
    private String instanceName = null;
    private boolean restrict = false;
    private ApplicationContext applicationContext = null;

    private static final String DEFAULT_INSTANCE_NAME = "IDF"+ System.currentTimeMillis();

    public String getInstanceNameKey() {
        return instanceNameKey;
    }

    public void setInstanceNameKey(String instanceNameKey) {
        this.instanceNameKey = instanceNameKey;
    }

    public boolean isRestrict() {
        return restrict;
    }

    public void setRestrict(boolean restrict) {
        this.restrict = restrict;
    }

}
