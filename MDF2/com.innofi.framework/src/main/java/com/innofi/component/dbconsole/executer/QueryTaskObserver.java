package com.innofi.component.dbconsole.executer;

import com.innofi.component.dbconsole.pojo.ProcessResult;
import com.innofi.framework.exception.FrameworkRuntimeException;
import com.innofi.component.dbconsole.pojo.DbConsoleTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Observable;
import java.util.Observer;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @version 2.0
 * @author liumy2009@126.com
 * @found date: 12-7-17
 * @found time: 上午10:59
 * <p/>
 * 查询任务的观察者实现，根据任务状态的改变，观察者做出相应的相应
 */
public class QueryTaskObserver implements Observer {

    private QueryTask queryTask;

    private ProcessResult<DbConsoleTable> pr;

    private Logger _log = LoggerFactory.getLogger(QueryTaskObserver.class);

    public QueryTaskObserver(ProcessResult<DbConsoleTable> pr) {
        this.pr = pr;
    }

    public void update(Observable observable, Object o) {
        Integer status = (Integer) o;
        switch (status) {
            case QueryTask.TASK_STATUS_NONE:
                if (_log.isDebugEnabled()) {
                    _log.debug("【" + queryTask.getName() + "】任务已创建...");
                }
                break;
            case QueryTask.TASK_STATUS_RUNNING:
                if (_log.isDebugEnabled()) {
                    _log.debug("【" + queryTask.getName() + "】任务已启动...");
                }
                break;
            case QueryTask.TASK_STATUS_STOPING:
                if (_log.isDebugEnabled()) {
                    _log.debug("-----【" + queryTask.getName() + "】任务收到终止命令,正在停止...");
                }
                break;
            case QueryTask.TASK_STATUS_STOP:
                if (_log.isDebugEnabled()) {
                    _log.debug("-----【" + queryTask.getName() + "】任务已退出,消息【" + pr.getMessage() + "】！");
                }
                break;
            case QueryTask.TASK_STATUS_FINISH:
                if (_log.isDebugEnabled()) {
                    _log.debug("-----【" + queryTask.getName() + "】任务已完成,消息【" + pr.getMessage() + "】！");
                }
                break;
            case QueryTask.TASK_STATUS_TIMEOUT:
                if (_log.isDebugEnabled()) {
                    _log.debug("-----【" + queryTask.getName() + "】任务执行超过规定超时时间：" + (queryTask.getTimeOut() / 1000) + "秒已退出，消息【" + pr.getMessage() + "】！");
                }
                break;
            case QueryTask.TASK_STATUS_ERROR:
                if (_log.isDebugEnabled()) {
                    _log.debug("-----【" + queryTask.getName() + "】任务执行出错已退出，消息【" + pr.getMessage() + "】！");
                }
                break;
        }
    }

    public void setQueryTask(QueryTask queryTask) {
        if (this.queryTask != null) {
            throw new FrameworkRuntimeException("不能重复设置监听任务！");
        }
        this.queryTask = queryTask;
    }

}
