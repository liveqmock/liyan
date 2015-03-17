
package com.innofi.component.rbac.post.service;

import java.util.List;
import java.util.Map;

import com.innofi.component.rbac.post.pojo.SysUserPost;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.pagination.Page;
import com.innofi.framework.service.IBaseService;

/**
 * 功能/ 模块：todo 模块中文名称
 *
 * @author  todo 开发人员姓名 邮箱
 * @version todo 当前版本 日期
 *          todo 类描述
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
public interface ISysUserPostService  extends IBaseService<SysUserPost,String> {

	/**
	 * 根据用户ID查询主岗位
	 * @param userId
	 * @return
	 */
	SysUserPost findUserMainPostByUserId(String userId);

	/**
	 * 根据用户ID查询兼岗位
	 * @param userId
	 * @return
	 */
	List<SysUserPost> findUserSecondaryPostByUserId(String userId);

	/**
	 * 根据用户ID查询所有岗位
	 * @param userId
	 * @return
	 */
	List<SysUserPost> findUserPostByUserId(String userId);
	
	/**
	 * 根据postId查询所有的信息
	 * @param userId
	 * @return
	 */
	List<SysUserPost> findUserPostByPostId(String postId);

	/**
	 * 根据用户ID查询所有兼岗位 并返回map
	 * @param userId
	 * @return
	 */
	Map<String,SysUserPost> findUserSecondaryPostMapByUserId(String userId);
	
	/**
	 * 根据岗位ID查询所有主岗用户
	 * @param postId
	 */
	List<SysUserPost> findUsersByMainPostId(String postId);

	public void findSysUserByPostId(String postId,List<PropertyFilter> propertyFilters,Page innofiPage);
	
}