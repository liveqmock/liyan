package com.innofi.component.dbconsole.pojo;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @version 2.0
 * @author liumy2009@126.com
 * @found date: 12-7-16
 * @found time: 上午9:29
 * <p/>
 * 任务处理结果描述对象
 */
public class ProcessResult<T> {

    /*
     *是否执行成功
     */
    private boolean success = false;
    /*
     *执行消息
     */
    private String message = null;
    /*
     *当前页数
     */
    private int pageNo;
    /*
     *提取数
     */
    private int limitSize;
    /*
     *总页数
     */
    private int pageCount;
    /*
     *总记录数
     */
    private int totalCount;

    /*
     *查询SQL语句
     */
    private String querySql;

    /*
     *超时时间
     */
    private long timeOut;

    /*
     *操作用时
     */
    private double consumeTime;

    private T data = null;

    public ProcessResult() {

    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T result) {
        this.data = result;
    }

    public boolean isFailing() {
        return !success;
    }

    public void setFailing(boolean failing) {
        success = !failing;
    }

    public int getPageNo() {
        if (this.pageNo <= 0) {
            this.pageNo = 1;
        }
        return pageNo;
    }

    public int getLimitSize() {
        if (this.limitSize <= 0) {
            this.limitSize = 200;
        }
        return limitSize;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public void setLimitSize(int limitSize) {
        this.limitSize = limitSize;
    }

    public String getQuerySql() {
        return querySql;
    }

    public long getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(long timeOut) {
        this.timeOut = timeOut;
    }

    public void setQuerySql(String querySql) {
        this.querySql = querySql;
    }

    /**
     * 返回总记录数。
     * <p>
     * 此处的总记录数并不是指当页数据的总数，而是指整个结果的总数。即每一页数据累计的总数。
     * </p>
     */
    public void setTotalCount(int totalCount) {
        if (totalCount < 0) {
            throw new IllegalArgumentException(
                    "Illegal entityCount arguments. [entityCount="
                            + totalCount + "]");
        }

        this.totalCount = totalCount;
        pageCount = ((totalCount - 1) / limitSize) + 1;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public double getConsumeTime() {
        return consumeTime;
    }

    public void setConsumeTime(double consumeTime) {
        this.consumeTime = consumeTime;
    }
}
