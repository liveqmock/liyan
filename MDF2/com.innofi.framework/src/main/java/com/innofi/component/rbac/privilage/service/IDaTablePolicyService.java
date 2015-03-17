package com.innofi.component.rbac.privilage.service;

import java.util.List;
import java.util.Set;

import com.innofi.framework.service.IBaseService;
import com.innofi.component.rbac.privilage.pojo.DaDimenDataRestrict;
import com.innofi.framework.pojo.metadata.MdDimension;
import com.innofi.component.rbac.privilage.pojo.DaTablePolicy;
import com.innofi.component.rbac.role.pojo.SysRole;

/**
 * 功能/ 模块：权限模块
 *
 * @author @author liumy2009@126.com
 * @version 2.0
 *          数据访问策略服务接口
 * @found date: 2010-11-23
 * @found time: 20:40:56 
 */
public interface IDaTablePolicyService extends IBaseService<DaTablePolicy, String> {

    /**
     * 根据角色ID查询数据控制策略
     *
     * @param roleId
     * @return
     */
    public List<DaTablePolicy> findDaTablePoliciesForPrivilege(String roleId);

    /**
     * 删除数据策略级联删除相关表
     *
     * @param policy
     */
    public void deleteDaTablePolicy(DaTablePolicy policy);

    /**
     * 保存数据访问策略
     *
     * @param policy 数据访问策略
     * @param dimens 数据访问策略中的维度
     */
    public String saveDaTablePoliciesWithDimens(DaTablePolicy policy, List<MdDimension> dimens);

    /**
     * 保存数据访问策略
     *
     * @param policy        数据访问策略
     * @param dataRestricts 约束条件集合
     */
    public void saveDaTablePolicyWithRestricts(DaTablePolicy policy, List<DaDimenDataRestrict> dataRestricts);

    /**
     * 根据角色对象列表查询角色拥有的数据策略
     *
     * @param userRoles 角色对象列表
     * @return 返回符合条件的数据策略列表
     */
    public List<DaTablePolicy> findPoliciesByUserRoles(List<SysRole> userRoles);

    /**
     * 根据角色对象列表，表名列表查询对应的数据策略
     *
     * @param userRoles  角色对象列表
     * @param tableNames 本次数据表列表
     * @return 返回符合条件的数据策略列表
     */
    public List<DaTablePolicy> findPoliciesByUserRolesAndTable(List<SysRole> userRoles, Set<String> tableNames);

    /**
     * 根据角色对象列表，表名列表查询对应的数据策略
     *
     * @param userRoles  角色对象列表
     * @param tableNames 本次数据表列表
     * @param operType   ，串的形式
     * @return 返回符合条件的数据策略列表
     */
    public List<DaTablePolicy> findPoliciesByUserRolesOpenTypeAndTable(List<SysRole> userRoles, String operType, Set<String> tableNames);

}