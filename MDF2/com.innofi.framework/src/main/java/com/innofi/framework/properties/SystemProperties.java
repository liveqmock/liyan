package com.innofi.framework.properties;

import EDU.oswego.cs.dl.util.concurrent.SyncMap;
import EDU.oswego.cs.dl.util.concurrent.WriterPreferenceReadWriteLock;
import com.innofi.component.rbac.parameter.service.ISysParameterService;
import com.innofi.framework.spring.context.ContextHolder;
import com.innofi.framework.utils.console.ConsoleUtil;
import com.innofi.framework.utils.properties.PropertiesUtil;
import com.innofi.framework.utils.string.StringUtil;
import com.innofi.framework.utils.variable.VariableHelper;

import java.io.*;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 系统参数配置文件操作工具类 （这里系统配置级的文件是指system-ext.properties文件，
 * 它们存放在类路径下的META-INF文件夹中）
 * Copyright (c) 2010 正信嘉华管理顾问有限公司
 * all rights reserved.
 * author: liumy
 * found date: 2010-1-8
 * found time: 21:25:38
 */
public class SystemProperties {

    public String encoding = "UTF-8";
    private URL url = null;
    private Map _props = new SyncMap(new HashMap(), new WriterPreferenceReadWriteLock());
    private static SystemProperties sysProperties;

    /**
     * 获得system-ext.roperties文件中获取传入Key对应等号右边的值，以String类型返回
     *
     * @param key 参数key
     * @return Key对应等号右边的值
     */
    private String get(String key) {
        String value = null;
        try{
            ISysParameterService sysParameterService = ContextHolder.getSpringBean("sysParameterService");
            value = sysParameterService.getParamValue(key);
        }catch (Exception e){
        }

        if (value == null) {
            value = (String) _props.get(key);
        }

        if (value == null) {
            value = System.getProperty(key);
        }

        return value;
    }

    /**
     * 获得system-ext.roperties文件中获取传入Key对应等号右边的值，以String类型返回
     *
     * @param key 参数key
     * @return Key对应等号右边的值
     */
    public String getString(String key) {
        return VariableHelper.parseString(get(key));
    }


    /**
     * 根据key获取system-ext.properties属性
     * @param key
     * @param defaultValue
     * @return
     */
	public String getDefaultString(String key, String defaultValue) {
		String value = (String) this._props.get(key);
		if (null == value) {
			return defaultValue;
		} else {
			return value;
		}
	}
	
    /**
     * 获得system-ext.roperties文件中获取传入Key对应等号右边的值，以String类型返回，如果对应的key在文件当中不存在，则返回默认值
     *
     * @param key          参数key
     * @param defaultValue 默认值
     * @return Key对应等号右边的值
     */
    public String getString(String key, String defaultValue) {
        return VariableHelper.parseString(get(key, defaultValue));
    }

    /**
     * 获得system-ext.roperties文件中获取传入Key对应等号右边的值，以int类型返回，如果对应的key在文件当中不存在，则返回默认值
     *
     * @param key          参数key
     * @param defaultValue 默认值
     * @return Key对应等号右边的值
     */
    public int getInteger(String key, int defaultValue) {
        return VariableHelper.parseInt(get(key, defaultValue + ""));
    }


    /**
     * 获得system-ext.roperties文件中获取传入Key对应等号右边的值，以int类型返回
     *
     * @param key 参数key
     * @return Key对应等号右边的值
     */
    public int getInteger(String key) {
        return VariableHelper.parseInt(get(key));
    }

    /**
     * 获得system-ext.roperties文件中获取传入Key对应等号右边的值，以double类型返回
     *
     * @param key 参数key
     * @return Key对应等号右边的值
     */
    public double getDouble(String key) {
        return VariableHelper.parseDouble(get(key));
    }

    /**
     * 获得system-ext.roperties文件中获取传入Key对应等号右边的值，以double类型返回，如果对应的key在文件当中不存在，则返回默认值
     *
     * @param key          参数key
     * @param defaultValue 默认值
     * @return Key对应等号右边的值
     */
    public double getDouble(String key, double defaultValue) {
        return VariableHelper.parseDouble(get(key, defaultValue + ""));
    }

    /**
     * 获得system-ext.roperties文件中获取传入Key对应等号右边的值，以Date类型返回，如果对应的key在文件当中不存在，则返回默认值
     *
     * @param key 参数key
     * @param df  给定的时间字符串的格式
     * @return Key对应等号右边的值
     */
    public Date getDate(String key, DateFormat df) {
        return VariableHelper.parseDate(get(key));
    }

    /**
     * 获得system-ext.roperties文件中获取传入Key对应等号右边的值，以Date类型返回，如果对应的key在文件当中不存在，则返回默认值
     *
     * @param key          参数key
     * @param defaultValue 默认值
     * @return Key对应等号右边的值
     */
    public Date getDate(String key, Date defaultValue) {
        if (!StringUtil.hasText(get(key))) {
            return defaultValue;
        }
        return VariableHelper.parseDate(get(key));
    }

    /**
     * 获得system-ext.roperties文件中获取传入Key对应等号右边的值，以long类型返回
     *
     * @param key 参数key
     * @return Key对应等号右边的值
     */
    public long getLong(String key) {
        return VariableHelper.parseLong(get(key));
    }

    /**
     * 获得system-ext.roperties文件中获取传入Key对应等号右边的值，以long类型返回，如果对应的key在文件当中不存在，则返回默认值
     *
     * @param key          参数key
     * @param defaultValue 默认值
     * @return Key对应等号右边的值
     */
    public long getLong(String key, long defaultValue) {
        return VariableHelper.parseLong(get(key, defaultValue + ""));
    }

    /**
     * 获得system-ext.roperties文件中获取传入Key对应等号右边的值，以float类型返回
     *
     * @param key 参数key
     * @return Key对应等号右边的值
     */
    public float getFloat(String key) {
        return VariableHelper.parseFloat(get(key));
    }

    /**
     * 获得system-ext.roperties文件中获取传入Key对应等号右边的值，以float类型返回，如果对应的key在文件当中不存在，则返回默认值
     *
     * @param key          参数key
     * @param defaultValue 默认值
     * @return Key对应等号右边的值
     */
    public float getFloat(String key, float defaultValue) {
        return VariableHelper.parseFloat(get(key, defaultValue + ""));
    }

    /**
     * 获得system-ext.roperties文件中获取传入Key对应等号右边的值，以boolean类型返回
     *
     * @param key 参数key
     * @return Key对应等号右边的值
     */
    public boolean getBoolean(String key) {
        return VariableHelper.parseBoolean(get(key));
    }

    /**
     * 获得system-ext.roperties文件中获取传入Key对应等号右边的值，以boolean类型返回，如果对应的key在文件当中不存在，则返回默认值
     *
     * @param key          参数key
     * @param defaultValue 默认值
     * @return Key对应等号右边的值
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        return VariableHelper.parseBoolean(get(key, defaultValue + ""));
    }

    /**
     * 修改系统参数值
     *
     * @param key
     * @param value
     */
    public synchronized void set(String key, String value) {
        _props.put(key, value);
    }

    /**
     * 将参数同步到文件
     */
    public synchronized void update() throws IOException {
        Properties p = PropertiesUtil.fromMap(_props);
        Writer w = new PrintWriter(new File(url.toString()));
        w.flush();
        w.close();
    }

    /**
     * 获得system-ext.roperties文件中获取传入Key对应等号右边的值，并以，拆分，以String类型数组形式返回，如果对应的key在文件当中不存在，则返回一个空数组
     *
     * @param key 参数key
     * @return key对应等号右边的值以，进行拆分的数组
     */
    public String[] getArray(String key) {
        String value = get(key);

        if (value == null) {
            return new String[0];
        }

        return StringUtil.split(value);
    }

    /**
     * 获得system-ext.roperties文件中获取传入Key对应等号右边的值，并以，拆分，以String类型数组形式返回，如果对应的key在文件当中不存在，则返回一个空数组
     *
     * @param key
     * @return key对应等号右边的值以splitSeparator进行拆分的数组
     */
    public String[] getArray(String key, String splitSeparator) {
        String value = get(key);

        if (value == null) {
            return new String[0];
        }

        return StringUtil.split(value, splitSeparator);
    }


    public Map getProperties() {
        return _props;
    }

    public static SystemProperties getInstance() {
        if (sysProperties != null) return sysProperties;
        return initSystemProperties();
    }

    /**
     * 将给定字符串，转换为相应int型
     *
     * @param value        给定字符串
     * @param defaultValue 默认值
     * @return key对应的值
     */
    public String get(String value, String defaultValue) {
        value = get(value);
        if (StringUtil.hasText(value)) {
            return value;
        } else {
            return defaultValue;
        }
    }

    private static SystemProperties initSystemProperties() {
        sysProperties = new SystemProperties();

        Properties p = new Properties();
        try {
            ClassLoader classLoader = SystemProperties.class.getClassLoader();
            URL url = classLoader.getResource("META-INF/system-ext.properties");
            ConsoleUtil.info("system-ext.properties loading url is[" + url + "]");
            if (url != null) {
                InputStream is = url.openStream();
                p.load(is);
                is.close();
                ConsoleUtil.info("the system-ext.properties load succeed load property size [" + p.size() + "]");
            }

            String pFileNames = (String) p.get("mdf.properties.filenames");
            String[] pFiles = StringUtil.split(pFileNames);
            for (int i = 0; i < pFiles.length; i++) {
                String fileName = pFiles[i];
                url = classLoader.getResource("META-INF/" + fileName);
                ConsoleUtil.info(fileName + " loading url is[" + url + "]");
                if (url != null) {
                    InputStream is = url.openStream();
                    p.load(is);
                    is.close();
                    ConsoleUtil.info("the " + fileName + " load succeed load property size [" + p.size() + "]");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        PropertiesUtil.fromProperties(p, sysProperties.getProperties());
        return sysProperties;
    }


}