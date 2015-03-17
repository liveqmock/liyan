package com.innofi.component.metadata.adapter.impl;

import com.innofi.component.metadata.adapter.IMdMetaAdapter;
import com.innofi.component.metadata.storedproc.service.IMdStoredProcService;
import com.innofi.framework.spring.context.ContextHolder;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 功能/ 模块：元数据模块
 *
 * @author 张磊  Alex.zhang@innofi.com.cn
 * @version 2.0.0 12-11-30
 *          测试Action实现类
 *          修订历史：
 *          日期:2012-12-24
 *          作者:张磊  Alex.zhang@innofi.com.cn
 *          参考:
 *          描述:获取存储过程信息
 *          北京名晟信息技术有限公司版权所有.
 */
@Component("mdStoredProcAdapter")
public class MdStoredProcAdapter implements IMdMetaAdapter {

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.metadata.adapter.IMdMetaAdapter#refreshMetaData(java.util.Map)
     */
    public String refreshMetaData(Map<String, Object> parameter) {
        IMdStoredProcService mdStoredProcService = ContextHolder.getSpringBean("mdStoredProcService");
        return mdStoredProcService.refreshMetaData(parameter);
    }

}
