package com.innofi.component.fileloader;


import com.innofi.framework.spring.context.ContextHolder;

import java.lang.reflect.Constructor;

/**
 * 文件加载器工厂类，提供对各种文件加载器创建功能
 */
public final class FileLoaderFactory {

    /**
     * 默认文件加载器
     */
    private static String autoFileLoader = "com.innofi.component.fileloader.AutoFileLoader";
    /**
     * 配置文件路径
     */
    private static String configRoot = "classpath:META-INF";
    /**
     * 源文件路径
     */
    private static String sourceRoot = "";

    /**
     * 初始化文件加载器工厂方法
     */
    public static void init() {
        configRoot = ContextHolder.getSystemProperties().getString("configFileLoader.root");
        sourceRoot = ContextHolder.getSystemProperties().getString("sourceFileLoader.root");
    }


    /**
     * 创建文件加载器方法
     *
     * @param clazz    文件加载器类的全命名
     * @param rootPath 该文件加载器加载的文件目录路径
     * @return 文件加载器
     */
    private static FileLoader createLoader(String clazz, String rootPath) {
        FileLoader fileLoader = null;
        try {
            Class cl = Class.forName(clazz);
            Constructor constr = cl.getConstructor(new Class[]{String.class});
            fileLoader = (FileLoader) constr.newInstance(new Object[]{rootPath});
            return fileLoader;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return fileLoader;
    }

    /**
     * 创建文件加载器方法,文件系统文件加载器在路径前添加file:前缀，
     * classpath文件加载器在路径前添加classpath:前缀，
     * database文件加载器在路径前添加database:前缀
     *
     * @return 文件加载器对象
     * @rootPath 文件路径
     */
    public static FileLoader createFileLoader(String rootPath) {
        return createLoader(autoFileLoader, rootPath);
    }

    /**
     * 创建文件加载器方法,文件系统加载器在路径前添加file:前缀，
     * classpath文件加载器在路径前添加classpath:前缀，
     * database文件加载器在路径前添加database:前缀
     * 如果不加前缀默认为文件系统加载器，即file:
     * @return 文件加载器对象
     * @rootPath 文件路径
     */
    public static FileLoader createFileLoader() {
        return createLoader(autoFileLoader, null);
    }

    /**
     * 创建配置文件加载器
     * @return
     */
    public static FileLoader createConfigFileLoader(){
        return createLoader(autoFileLoader,configRoot);
    }

    /**
     * 创建源文件加载器
     * @return
     */
    public static FileLoader createResourceFileLoader(){
        return createLoader(autoFileLoader,sourceRoot);
    }

}