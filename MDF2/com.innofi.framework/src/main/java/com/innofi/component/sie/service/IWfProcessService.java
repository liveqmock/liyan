
package com.innofi.component.sie.service;


import com.innofi.component.sie.pojo.WfOperateStatus;
import com.innofi.component.sie.pojo.WfProcess;
import com.innofi.framework.service.IBaseService;
import com.innofi.framework.pojo.BasePojo;

/**
 * 功能/ 模块：todo 模块中文名称
 *
 * @author todo 开发人员姓名 邮箱
 * @version todo 当前版本 日期
 *          todo 类描述
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
public interface IWfProcessService extends IBaseService<WfProcess, String> {

    public WfProcess saveWfProcess(WfProcess wfProcess, WfOperateStatus wfOperateStatus, BasePojo businessObject);
    public WfProcess saveWfProcess(WfProcess wfProcess, WfOperateStatus wfOperateStatus);

    /**
     * 创建流程
     *
     * @param clazz      实体对象class
     * @param businessId 业务实体id
     */
    public void saveWfProcess(Class clazz, String businessId);
    /**
     * 创建流程
     *
     * @param className  类名
     * @param businessId 业务实体id
     */
    public void saveWfProcess(String className, String businessId);

    /**
     * 删除流程
     *
     * @param entityName 实体对象名称
     * @param businessId 业务实体id
     */
    public void deleteWfProcess(String entityName, String businessId);

}