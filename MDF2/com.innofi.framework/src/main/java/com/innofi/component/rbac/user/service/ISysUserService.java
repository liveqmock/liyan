
package com.innofi.component.rbac.user.service;

import com.innofi.framework.service.IBaseService;
import com.innofi.component.rbac.user.pojo.SysUser;

import java.util.List;
import java.util.Map;

/**
 * 功能/ 模块：权限模块
 *
 * @author 张恩维 eric.zhang@innofi.com.cn
 * @version 2.0.0 12-11-30
 *          用户服务接口
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
public interface ISysUserService extends IBaseService<SysUser, String> {

    /**
     * 根据userCode获取用户对象
     *
     * @param userCode 员工编号
     * @return SysUser对象
     */
    public SysUser getSysUserByUserCode(String userCode);

    /**
     * 用户登录验证
     *
     * @param userCode 员工编号
     * @param password 密码
     * @param ip       登录用户ip地址
     * @param macIp    登录用户mac地址
     * @return 登录状态:'0':登录成功,'1':用户不存在,'2':密码错误,'3':用户停用,'4':用户未分配功能角色,'5':用户未分配岗位
     */
    public char login(String userCode, String password, String ip, String macIp);

    /**
     * 用户签退，清空session
     */
    public void logout();

    public Map<String, SysUser> getSysUserMaps();

    /**
     * 更新用户密码 当password为空时修改为默认密码
     *
     * @param userId   用户id
     * @param password 密码
     */
    public void updateUserPassword(String userId, String password);

    /**
     * 找到岗位下的所有用户信息
     * findAllSysUsersByPostId
     *
     * @param postId 岗位编号
     * @since CodingExample　Ver(编码范例查看) 1.1
     */
    public List<SysUser> findAllSysUsersByPostId(String postId);

    public List<SysUser> findSysUserNoSetByRoleId(String roleId);

    public List<SysUser> findSysUserSetByRoleId(String roleId);


}