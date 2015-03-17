package com.innofi.component.sie.service;

import com.innofi.framework.pojo.BasePojo;
import com.innofi.component.sie.pojo.WfProcess;

/*
 * 状态引擎拦截器，需要进行状态处理的业务类需要实现该接口
 */
public interface IWfProcessInterceptor {

	/*
	 * 判断该业务类是否支持状态处理
	 */
	boolean support(String moduleCode);
	
	/*
	 * 在进行业务处理过程前需要实现的业务逻辑处理。
	 */
	void before(WfProcess wfProcess,BasePojo businessObject);

	/*
	 * 进行业务处理过程后需要实现的业务逻辑处理，比如修改业务数据状态等。
	 */
	void after(WfProcess wfProcess,BasePojo businessObject);
	
    /**
     * 状态字段名称当不为status时，需要重写该方法，返回实际状态字段名，如：busiStatus
     *
     * @return
     */
    public String getStatusFieldName();

    /**
     * 状态字段名称当不为nodeName时，需要重写该方法，返回实际状态字段名，如：myNodeName
     *
     * @return
     */
    public String getNodeFieldName();

	
}
