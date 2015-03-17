
package com.innofi.component.metadata.storedproc.service;

import com.innofi.framework.pojo.metadata.MdStoredProc;
import com.innofi.framework.service.IBaseService;

import java.util.List;
import java.util.Map;

/**
 * 功能/ 模块：元数据模块
 *
 * @author 张磊 Alex.zhang@innofi.com.cn
 * @version 2.0.0 12-11-30
 *          测试Service接口
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
public interface IMdStoredProcService  extends IBaseService<MdStoredProc,String> {


    /**
     * 刷新存储过程元数据
     * @param parameter
     * @return
     */
    public String refreshMetaData(Map<String, Object> parameter);
    
    /*
     * 
     */
    public List<MdStoredProc> findMdStoredProcByStatus(String status);
    
    /*
     * 
     */
    public int refreshProcMetaData(String procName);

}