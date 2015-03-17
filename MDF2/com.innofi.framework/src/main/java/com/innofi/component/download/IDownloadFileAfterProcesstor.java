package com.innofi.component.download;

import java.util.Date;

/**
 * 文件下载的后续处理接口
 * Copyright (c) 2010 正信嘉华管理顾问有限公司
 * all rights reserved.
 * author: liumy
 * found date: 2010-11-21
 * found time: 11:34:50
 */
public interface IDownloadFileAfterProcesstor {
    /**
     * 文件下载后续处理
     *
     * @param fileName     下载文件名称
     * @param fileIds      文件唯一标识列表
     * @param fileCount    下载文件数量
     * @param downloadDate 下载日期
     * @param extFix       下载文件扩展名
     */
    public void afterProcessing(String fileName, String[] fileIds, int fileCount, Date downloadDate, String extFix, long size);
}
