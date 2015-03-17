package com.innofi.component.rbac.privilage.service;

import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.pagination.Page;
import com.innofi.component.rbac.post.pojo.SysPost;
import com.innofi.component.rbac.privilage.pojo.SysPostRole;
import com.innofi.framework.service.IBaseService;

import java.util.List;

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
public interface ISysPostRoleService extends IBaseService<SysPostRole, String> {

    /**
     * 根据用户所拥有的岗位查询所分配的角色 岗位状态为有效
     *
     * @param userId
     * @return
     */
    List<SysPostRole> findSysPostRoleByUserId(String userId);


    /**
     * 保存岗位角色设置
     *
     * @param roleId
     * @param added
     * @param removed
     */
    void saveSysPostRole(String roleId, List<SysPost> added,
                         List<SysPost> removed);
    
    /**
     * 通过POSTID找到匹配的分页角色
       
     * findSysRoleByPostId(这里用一句话描述这个方法的作用)    
       
     * @param   name    
       
     * @param  @return    设定文件    
       
     * @return String    DOM对象    
       
     * @Exception 异常对象    
       
     * @since  CodingExample　Ver(编码范例查看) 1.1
     */
	void findSelectedSysRoleByPostId(String postId, List<PropertyFilter> filter, Page page);
	
	List<SysPostRole> findSysPostRoleByPostId(String postId);
}