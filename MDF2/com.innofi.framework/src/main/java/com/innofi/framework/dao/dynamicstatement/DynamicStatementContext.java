package com.innofi.framework.dao.dynamicstatement;

import com.innofi.framework.exception.FrameworkRuntimeException;
import com.innofi.framework.spring.context.ContextHolder;
import com.innofi.framework.utils.console.ConsoleUtil;
import com.innofi.framework.utils.resource.ResourceUtil;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @author liumy2009@126.com
 * @version 1.0-SNAPSHOT
 * @found date: 2012-06-10
 * @found time: 23:55:59
 * <p/>
 * SQL定义上下文
 */
public class DynamicStatementContext {

    private static final Logger _log = LoggerFactory.getLogger(DynamicStatementContext.class);

    private static Map<String, Template> sqlCtx = new HashMap<String, Template>();
    private static Map<String, Template> hqlCtx = new HashMap<String, Template>();
    private static Configuration configuration = new Configuration();


    private static final String SQL_DEFINE_FILE_DIR = "classpath:META-INF/dynamic-statement";

    /**
     * SqlDefineContext是否初始化标识
     */

    private static boolean initialized = false;


    private static String getKey(String dbType, String moduleId, String sqlId) {
        return "$" + dbType + "_" + moduleId + "_" + sqlId;
    }

    public static void put(String dbType, String moduleId, String sqlName, String type, String dynamicStatement) throws DynamicStatementNameExistedException, IOException {

        String key = getKey(dbType, moduleId, sqlName);

        Template template = new Template(sqlName, new StringReader(dynamicStatement), configuration);

        if ("sql-query".equals(type) && sqlCtx.get(key) == null) {
            sqlCtx.put(key, template);
        } else if ("hql-query".equals(type)) {
            hqlCtx.put(key, template);
        } else {
            throw new DynamicStatementNameExistedException(moduleId, sqlName);
        }
    }

    /**
     * 获得格式以后的sql
     *
     * @param dbType   数据库类型
     * @param moduleId 模块ID即动态sql/hql定义文件名称
     * @param sqlName  sql名称
     * @param params   参数列表
     * @return 格式化后的sql语句
     */
    public static String getSql(String dbType, String moduleId, String sqlName, Map<String, String> params){
        if (!initialized) {
            initCtx();
            initialized = true;
        }

        String key = getKey(dbType, moduleId, sqlName);
        Template template = sqlCtx.get(key);
        if (template == null) {
            throw new FrameworkRuntimeException(MessageFormat.format("在目录[{0}]文件[{1}]中未找到sqlname/hqlname为[{2}的动态语句定义,请检查!]", dbType, moduleId, sqlName));
        }

        StringWriter stringWriter = new StringWriter();
        try {
			template.process(params, stringWriter);
		} catch (TemplateException e) {
			e.printStackTrace();
			throw new FrameworkRuntimeException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkRuntimeException(e);
		}
        return stringWriter.toString();
    }

    /**
     * 获得格式以后的hql
     *
     * @param dbType   数据库类型
     * @param moduleId 模块ID即动态sql/hql定义文件名称
     * @param sqlName  sql名称
     * @param params   参数列表
     * @return 格式化后的sql语句
     */
    public static String getHql(String dbType, String moduleId, String sqlName, Map<String, String> params){
        if (!initialized) {
            initCtx();
            initialized = true;
        }

        String key = getKey(dbType, moduleId, sqlName);
        Template template = hqlCtx.get(key);

        StringWriter stringWriter = new StringWriter();
        try {
			template.process(params, stringWriter);
		} catch (TemplateException e) {
			e.printStackTrace();
			throw new FrameworkRuntimeException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkRuntimeException(e);
		}
        return stringWriter.toString();
    }

    /**
     * 初始化SqlDefineContext
     *
     * @throws org.dom4j.DocumentException
     */
    public static void initCtx() {
        if (!initialized) {

            StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();

            IDynamicStatementBuilder dynamicHibernateStatementBuilder = ContextHolder.getSpringBeanFactory().getBean(IDynamicStatementBuilder.class);
            String[] fileNames = dynamicHibernateStatementBuilder.getFileNames();
            for (String fileName : fileNames) {
                if (fileName.indexOf("[") != -1 && fileName.indexOf("]") != -1) {
                    try {
                        File[] files = ResourceUtil.getFile(SQL_DEFINE_FILE_DIR).listFiles(new FileFilter() { //获得指定文件实例下非隐藏的目录和文件，；
                            public boolean accept(File f) {
                                if (f.isHidden()) return false;
                                if (f.getName().indexOf("svn") > -1) return false;
                                return true;
                            }
                        });//项目目录/META-INF下建立dynamic-statement目录
                        if (files == null) throw new FileNotFoundException();
                        for (File file : files) {
                            String dbType = file.getName();
                            if (file.isDirectory() && (file.listFiles() != null && file.listFiles().length > 0)) {
                                String realPath = fileName.replace("[$dbType]", dbType);
                                ConsoleUtil.info("loading sql/hql define file[" + realPath + "]");
                                dynamicHibernateStatementBuilder.loadingDynamicStatement(dbType, realPath);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new FrameworkRuntimeException("动态sql/sql上下文初始化失败，请在项目目录/META-INF下建立dynamic-statement/dbtype目录");
                    } catch (DynamicStatementNameExistedException e) {
                        e.printStackTrace();
                        throw e;
                    }
                } else {
                    throw new FrameworkRuntimeException("动态sql/sql定义文件路径配置格式不符合规范，请检查！正确配置如：META-INF/dynamic-statement/[$dbType]/****-dynamic-statement.xml");
                }
            }
            configuration.setTemplateLoader(stringTemplateLoader);
            initialized = true;
        }

    }
}
