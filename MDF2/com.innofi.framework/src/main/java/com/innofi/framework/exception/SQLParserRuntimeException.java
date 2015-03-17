/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.framework.exception;

/**
 * 功能/ 模块：框架基础模块
 *
 * @author 刘名寓 liumy2009@126.com
 * @version 2.0.0 12-11-30
 *          SQL解析异常类
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
public class SQLParserRuntimeException extends FrameworkRuntimeException {

    public SQLParserRuntimeException() {
        super();
    }

    public SQLParserRuntimeException(String message) {
        super(message);
    }

    public SQLParserRuntimeException(Throwable t) {
        super(t);
    }

}
