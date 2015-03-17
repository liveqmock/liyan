package com.innofi.framework.exception;


import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 用于生成ExceptionLogger的工厂类
 *
 * @author jack.liu@innofi.com
 * @version 2.0
 */
public class ExceptionLoggerFactory implements LoggerFactory {
    Map<String, ExceptionLogger> loggerMap = Collections.synchronizedMap(new HashMap<String, ExceptionLogger>());
    private static ExceptionLoggerFactory instance = new ExceptionLoggerFactory();

    public static ExceptionLoggerFactory getInstance() {
        if (instance == null) {
            instance = new ExceptionLoggerFactory();
        }
        return instance;
    }

    public Logger makeNewLoggerInstance(String loggerName) {
        if (loggerMap.containsKey(loggerName)) {
            return loggerMap.get(loggerName);
        } else {
            ExceptionLogger logger = new ExceptionLogger(loggerName);
            loggerMap.put(loggerName, logger);
            return logger;
        }
    }
}
