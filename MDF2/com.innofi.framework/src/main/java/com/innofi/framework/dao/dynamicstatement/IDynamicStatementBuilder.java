package com.innofi.framework.dao.dynamicstatement;

import java.io.IOException;
import java.util.Map;

/**
 * 动态sql/hql语句组装器接口
 *
 * @author liumy2009@126.com
 * @version 2.0.0 12-11-30
 *          类描述
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
public interface IDynamicStatementBuilder {

    /**
     * 获取动态sql/hql文件
     * @return
     */
    public String[] getFileNames();

    /**
     * 初始化动态sql/hql上下文
     *
     * @throws IOException
     */
    public void loadingDynamicStatement(String dbType,String fileName) throws IOException;
}