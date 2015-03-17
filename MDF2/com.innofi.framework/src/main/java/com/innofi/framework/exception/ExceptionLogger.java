package com.innofi.framework.exception;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 一个用于将Exception信息记录到文件的Logger
 *
 * @author liumy2009@126.com
 * @version  1.0-SNAPSHOT
 */
public class ExceptionLogger extends Logger {
    private static ExceptionLoggerFactory loggerFactory = ExceptionLoggerFactory.getInstance();
    protected static final SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMdd");

    protected ExceptionLogger(String name) {
        super(name);
    }

    public static Logger getExceptionLogger(String name, String logDir) throws IOException {
        Logger logger = Logger.getLogger(name, loggerFactory);
        synchronized (logger) {
            String currentDateStr = dataFormat.format(new Date());
            File sqlLogDir = new File(logDir + File.separator + currentDateStr);//e.g.d:/logs/sqldialect/20110420/mid/
            if (!sqlLogDir.exists()) {
                sqlLogDir.mkdirs();
            }
            FileAppender fileAppender = (FileAppender) logger.getAppender(name);
            String logFilePath = sqlLogDir.getAbsolutePath() + File.separator + name + ".log";
            if (fileAppender != null) {
                fileAppender.close();
                logger.removeAppender(fileAppender);
            }
            fileAppender = new FileAppender(new PatternLayout("%m%n"), logFilePath);
            fileAppender.setName(name);
            fileAppender.setThreshold(Level.ERROR);
            logger.addAppender(fileAppender);
        }
        return logger;
    }
}
