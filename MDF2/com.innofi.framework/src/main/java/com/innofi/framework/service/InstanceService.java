package com.innofi.framework.service;

/**
 * 集群服务.
 * <p>
 * 此类提供给框架使用者感知集群环境的能力.
 * </p>
 *
 * @author jack.liu@innofi.com
 * @version 2.0
 */
public interface InstanceService {

    /**
     * 返回一个实例名.
     * <p>
     * 接口的实现者需要保证每次对此函数的调用,都返回同一个字符串.
     * </p>
     *
     * @return 实例名
     */
    public String getInstanceName();

    /**
     * 取得当前应用程序根目录.
     * <p>
     * 接口的实现类保证代表应用程序根目录的字符串符合如下约定:<br/>
     * <ol>
     * <li>如为非web环境,返回<codebuilder>null</codebuilder></li>
     * <li>如为web环境,返回应用程序安装路径,如C:\tomcat\webapps\MyApp</li>
     * <li>返回的路径中不包含末尾的路径分隔符</li>
     * </ol>
     * </p>
     *
     * @return 当前应用程序根目录
     */
    public String getAppRootPath();

    /**
     * 服务ID,代表了其在spring中的beanName.
     */
    public static final String SERVICE_ID = "mdf.instanceService";

}
