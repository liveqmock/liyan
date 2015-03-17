
package com.innofi.component.rbac.post.service;

import java.util.List;
import java.util.Map;

import com.innofi.framework.service.IBaseService;
import com.innofi.component.rbac.post.pojo.SysPost;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.pagination.Page;

public interface ISysPostService  extends IBaseService<SysPost,String> {

	/**
	 * 根据用户ID查询兼岗位列表，已拥有的岗位选择状态为true
	 * @param userId
	 * @param propertyFilters
	 * @param innofiPage
	 */
	public void findSysPostForUserSecondaryPost(String userId,List<PropertyFilter> propertyFilters,Page innofiPage);

	/**
	 * 查询所有岗位 返回Map
	 * @return
	 */
	public Map<String, SysPost> getSysPostMaps();

    /**
     * 查询用户所有岗位 有效状态
     */
    public List<SysPost> findAllSysPostsByUserId(String userId);

    /**
     * 岗位停用校验 是否存在有效的用户
     * @param postId
     * @return
     */
	public String invalidPost(String postId);
	/**
	 * 根据部门代码获取所有有效岗位
	 * findAllSysPostsByDeptCode(这里用一句话描述这个方法的作用)    
	 * @param   name    
	 * @param  @return    设定文件    
	 * @return String    DOM对象    
	 * @Exception 异常对象    
	 * @since  CodingExample　Ver(编码范例查看) 1.1
	 */
	public List<SysPost> findAllSysPostsByDeptCode(String deptcode);
	/**
	 * 根据机构获取所有有效岗位
	 * findAllSysPostsByOrgCode(这里用一句话描述这个方法的作用)    
	 * @param   name    
	 * @param  @return    设定文件    
	 * @return String    DOM对象    
	 * @Exception 异常对象    
	 * @since  CodingExample　Ver(编码范例查看) 1.1
	 */
    public List<SysPost> findAllSysPostsByOrgCode(String orgCode);
    /**
     * 根据角色ID查询未设置的所有岗位
     *
     * @param roleId
     */
    List<SysPost> findSysPostNoSetByRoleId(String roleId);

    /**
     * 根据角色ID查询已设置的所有岗位
     *
     * @param roleId
     */
    List<SysPost> findSysPostSetByRoleId(String roleId);
	
}