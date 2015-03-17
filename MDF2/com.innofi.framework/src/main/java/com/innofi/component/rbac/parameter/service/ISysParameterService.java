package com.innofi.component.rbac.parameter.service;

import java.util.Map;

import com.innofi.component.rbac.parameter.pojo.SysParameter;
import com.innofi.framework.service.IBaseService;

public interface ISysParameterService extends IBaseService<SysParameter, String> {

    /**
     * 判断编号参数是否存在
     *
     * @param paraCode
     * @return
     */
    boolean checkCode(String paraCode);

    /**
     * 判断编号
     *
     * @param paraCode
     * @return
     */
    String getParamValue(String paraCode);
    
    /**
     * 修改系统参数值
     * @param input
     */
    public String updateParamValue(Map<String,String> input);

    /**
     * 加载配置文件中新增的参数
     */
    public void loadNewParameter();

}