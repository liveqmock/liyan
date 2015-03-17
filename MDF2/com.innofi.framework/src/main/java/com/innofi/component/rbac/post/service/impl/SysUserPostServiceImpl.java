
package com.innofi.component.rbac.post.service.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.innofi.component.rbac.org.pojo.SysOrgInfo;
import com.innofi.component.rbac.org.service.ISysOrgInfoService;
import com.innofi.component.rbac.post.pojo.SysPost;
import com.innofi.component.rbac.post.pojo.SysUserPost;
import com.innofi.component.rbac.post.service.ISysPostService;
import com.innofi.component.rbac.post.service.ISysUserPostService;
import com.innofi.component.rbac.user.pojo.SysUser;
import com.innofi.component.rbac.user.service.ISysUserService;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.framework.dao.pagination.Page;
import com.innofi.framework.service.impl.BaseServiceImpl;


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
@Component(value="sysUserPostService")
public class SysUserPostServiceImpl  extends BaseServiceImpl<SysUserPost,String> implements ISysUserPostService {
	
	@Resource(name="mdDataTitleTableMappingDaoSupport")
	protected DaoSupport daoSupport;

	public DaoSupport getDaoSupport(){
		return daoSupport;
	}

	public SysUserPost findUserMainPostByUserId(String userId) {
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		this.addPropertyFilter(filters, "userId", userId, QueryConstants.EQUAL, true);
		this.addPropertyFilter(filters, "postType", "1", QueryConstants.EQUAL, true);
		List<SysUserPost> posts= this.findByPropertyFilters(filters, null);
		if(posts.size()>0){
			SysUserPost userPost=posts.get(0);
            ISysPostService sysPostService = getSpringBean("sysPostService");
			SysPost post = sysPostService.get(userPost.getPostId());
			userPost.setPostName(post.getPostName());
			return userPost;
		}
		return null;
	}

	public List<SysUserPost> findUserSecondaryPostByUserId(String userId) {
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		this.addPropertyFilter(filters, "userId", userId, QueryConstants.EQUAL, true);
		this.addPropertyFilter(filters, "postType", "2", QueryConstants.EQUAL, true);
		List<SysUserPost> userPosts=this.findByPropertyFilters(filters, null);
        ISysOrgInfoService sysOrgInfoService = getSpringBean("sysOrgInfoService");
        ISysPostService sysPostService = getSpringBean("sysPostService");

		if(userPosts.size()>0){
			Map<String,SysOrgInfo> orgInfoMap = sysOrgInfoService.getSysOrgInfoMaps();
			Map<String,SysPost> postMap = sysPostService.getSysPostMaps();
			for(int i=0;i<userPosts.size();i++){
				SysUserPost userPost=(SysUserPost) userPosts.get(i);
				SysPost post=postMap.get(userPost.getPostId());
				userPost.setPostName(post.getPostName());
				if(null!=orgInfoMap.get(post.getOrgCode())){
				userPost.setOrgName(orgInfoMap.get(post.getOrgCode()).getOrgName());
				}
			}
		}
		return userPosts;
	}
	
	public Map<String,SysUserPost> findUserSecondaryPostMapByUserId(String userId) {
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		this.addPropertyFilter(filters, "userId", userId, QueryConstants.EQUAL, true);
		this.addPropertyFilter(filters, "postType", "2", QueryConstants.EQUAL, true);
		List<SysUserPost> userPosts=this.findByPropertyFilters(filters, null);
		HashMap<String,SysUserPost> postMap=new HashMap<String,SysUserPost>();
		for(SysUserPost userPost:userPosts){
			postMap.put(userPost.getPostId(), userPost);
		}
		return postMap;
	}
	
	public Map<String,SysUserPost> findMainPostMapByPostId(String postId) {
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		this.addPropertyFilter(filters, "postId", postId, QueryConstants.EQUAL, true);
		this.addPropertyFilter(filters, "postType", "1", QueryConstants.EQUAL, true);
		List<SysUserPost> userPosts=this.findByPropertyFilters(filters, null);
		HashMap<String,SysUserPost> postMap=new HashMap<String,SysUserPost>();
		for(SysUserPost userPost:userPosts){
			postMap.put(userPost.getUserId(), userPost);
		}
		return postMap;
	}
	
	public List<SysUserPost> findUserPostByUserId(String userId){
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		this.addPropertyFilter(filters, "userId", userId, QueryConstants.EQUAL, true);
		return this.findByPropertyFilters(filters, null);
	}
	public List<SysUserPost> findUserPostByPostId(String postId){
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		this.addPropertyFilter(filters, "postId", postId, QueryConstants.EQUAL, true);
		return this.findByPropertyFilters(filters, null);
	}
	
	

	public List<SysUserPost> findUsersByMainPostId(String postId) {
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		this.addPropertyFilter(filters, "postId", postId, QueryConstants.EQUAL, true);
		this.addPropertyFilter(filters, "postType", "1", QueryConstants.EQUAL, true);
		List<SysUserPost> userPosts=this.findByPropertyFilters(filters, null);
        ISysOrgInfoService sysOrgInfoService = getSpringBean("sysOrgInfoService");
        ISysUserService sysUserService = getSpringBean("sysUserService");

		if(userPosts.size()>0){
			Map<String,SysOrgInfo> orgInfoMap = sysOrgInfoService.getSysOrgInfoMaps();
			Map<String,SysUser> userMap = sysUserService.getSysUserMaps();
			for(int i=0;i<userPosts.size();i++){
				SysUserPost userPost=(SysUserPost) userPosts.get(i);
				SysUser user=userMap.get(userPost.getUserId());
				userPost.setUserName(user.getUserName());
				userPost.setOrgName(orgInfoMap.get(user.getOrgCode()).getOrgName());
			}
		}
		return userPosts;
	}

	public void findSysUserByPostId(String postId,List<PropertyFilter> filters, Page page) {
		ISysUserService sysUserService = getSpringBean("sysUserService");
		ISysOrgInfoService sysOrgInfoService = getSpringBean("sysOrgInfoService");
		sysUserService.findByPage_Filters(filters, null, page);
		List<SysUser> sysUsers=page.getEntities();
		Map<String,SysUserPost> postMap=findMainPostMapByPostId(postId);
		if(sysUsers.size()>0){
			Map<String,SysOrgInfo> orgInfoMap = sysOrgInfoService.getSysOrgInfoMaps();
			for(SysUser user:sysUsers){
				if(postMap.containsKey(user.getId())){
					user.setSelectFlag(true);
				}
				user.setOrgName(orgInfoMap.get(user.getOrgCode()).getOrgName());
			}
		}
	}

	
}

