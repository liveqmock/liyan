package com.innofi.framework.exception;

import com.innofi.framework.exception.FrameworkException;

/**
 * gl company copyright 2010
 * User: liumy
 * Date: 2010-2-16
 * Time: 11:23:30
 */
public class FileDownloadException extends FrameworkException {

    public FileDownloadException(String message) {
        super(message);
    }

    public FileDownloadException() {

    }

    public FileDownloadException(Exception e) {
        super(e);
    }

}