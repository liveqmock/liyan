
package com.innofi.component.rbac.post.service.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.innofi.component.rbac.org.service.ISysOrgInfoService;
import com.innofi.component.rbac.post.service.ISysPostService;
import com.innofi.component.rbac.post.service.ISysUserPostService;
import com.innofi.framework.service.impl.BaseServiceImpl;
import com.innofi.component.rbac.role.service.ISysRoleService;
import com.innofi.framework.FrameworkConstants;
import com.innofi.component.codetransfer.IdfCodeTransfer;
import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.PropertyOrder;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.framework.dao.pagination.Page;
import com.innofi.component.rbac.org.pojo.SysOrgInfo;
import com.innofi.component.rbac.post.pojo.SysPost;
import com.innofi.component.rbac.role.pojo.SysRole;
import com.innofi.component.rbac.post.pojo.SysUserPost;
import com.innofi.framework.spring.context.ContextHolder;




@Component(value="sysPostService")
public class SysPostServiceImpl  extends BaseServiceImpl<SysPost,String> implements ISysPostService {
	@Resource
	private ISysUserPostService sysUserPostService;
	
	@Resource
    private ISysOrgInfoService sysOrgInfoService;
	
	@Resource(name="mdDataTitleTableMappingDaoSupport")
	protected DaoSupport daoSupport;

	public DaoSupport getDaoSupport(){
		return daoSupport;
	}
	
	public String getCodeFieldName(){
		return "postCode";
	}
	
	public String getCnFieldName(){
		return "postName";
	}
	
    @Override
    public List<SysPost> loadCacheObjects() {
        return getDaoSupport().getHibernateDao().find(getEntityType(),"from SysPost");
    }
	public void findByPage_Filters(List<PropertyFilter> filters,
			List<PropertyOrder> orders, Page page) {
		super.findByPage_Filters(filters, orders, page);
		List posts=page.getEntities();
		Map map=new HashMap();
		map.put("orgCode", "orgName");
		IdfCodeTransfer codeTransfer = ContextHolder.getSpringBean("mdf.codeTransfer");
		codeTransfer.transferResult(posts, "sysOrgInfoService", map);
	}

	public void findSysPostForUserSecondaryPost(String userId,List<PropertyFilter> filters,Page page){
		//主岗判断情况改为页面判断 否则存在主岗调整 而兼岗查不出来

		/*SysUserPost userPost=sysUserPostService.findUserMainPostByUserId(userId);
		if(userPost!=null){
			String mainPostId=userPost.getPostId();
			this.addPropertyFilter(filters, "id", mainPostId, QueryConstants.NOT_EQUAL, true);
		}*/
		
		Map secondaryPostMap=sysUserPostService.findUserSecondaryPostMapByUserId(userId);
		this.findByPage_Filters(filters, null, page);
		List posts=page.getEntities();
		Map<String,SysOrgInfo> orgInfoMap= sysOrgInfoService.getSysOrgInfoMaps();
		for(int i=0;i<posts.size();i++){
			SysPost post=(SysPost) posts.get(i);
			if(secondaryPostMap.containsKey(post.getId())){
				post.setSelectFlag(true);
			}
			if(null!=orgInfoMap.get(post.getOrgCode())){
				post.setOrgName(orgInfoMap.get(post.getOrgCode()).getOrgName());
			}
		}
	}
	
	public Map<String,SysPost> getSysPostMaps(){
		List<SysPost> posts=this.findByPropertyFilters(null, null);
		Map<String,SysPost> postMap=new HashMap<String,SysPost>();
		for(SysPost post:posts){
			postMap.put(post.getId(), post);
		}
		return postMap;
	}




    public List<SysPost> findAllSysPostsByUserId(String userId) {
        String hql="select u from SysUserPost p,SysPost u where p.userId=? and p.postId=u.id and u.status=?";
        return getDaoSupport().getHibernateDao().findByHql(getEntityType(),hql, new Object[]{userId, FrameworkConstants.STATUS_EFFECTIVE});
    }
    /**
     * 获取该部门的所有岗位信息
     */
    public List<SysPost> findAllSysPostsByDeptCode(String deptCode) {
		List<PropertyFilter> filters=new ArrayList<PropertyFilter>();
		this.addPropertyFilter(filters, "orgCode", deptCode, QueryConstants.EQUAL, true);
		this.addPropertyFilter(filters, "status", FrameworkConstants.STATUS_EFFECTIVE, QueryConstants.EQUAL, true);
     	List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
    	orders.add( new PropertyOrder("postLevel",QueryConstants.ORDER_ASC));
    	return this.findByPropertyFilters(filters,  orders);	
    }
    /**
     * 获取该机构下的所有岗位信息，指的是该机构的部门对应的所有岗位，不包括分支行
     */
    public List<SysPost> findAllSysPostsByOrgCode(String orgCode){
    	
    	ISysOrgInfoService sysOrgInfoService = (ISysOrgInfoService) getSpringBean("sysOrgInfoService");//注入机构服务
    	List<SysOrgInfo> deptList = sysOrgInfoService.getOwnDeptInfoByOrgCode(orgCode);
    	if(0==deptList.size()){
    		return null;
    	}
    	List<String> depts = new ArrayList();
    	for(SysOrgInfo dept:deptList){
    		depts.add(dept.getOrgCode());
    	}
    	List<PropertyFilter> filters=new ArrayList<PropertyFilter>();
		this.addPropertyFilter(filters, "orgCode", depts, QueryConstants.IN, true);
		this.addPropertyFilter(filters, "status", FrameworkConstants.STATUS_EFFECTIVE, QueryConstants.EQUAL, true);
     	List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
    	orders.add(new PropertyOrder("postLevel",QueryConstants.ORDER_ASC));
    	return  this.findByPropertyFilters(filters,  orders);
    
    }
	public String invalidPost(String postId) {
		ISysUserPostService sysUserPostService=getSpringBean("sysUserPostService");
		String hql="select p from SysUserPost p,SysUser u where p.userId=u.id and p.postId=? and u.status=?";
		List<SysUserPost> userPost=sysUserPostService.findByHql(hql, new String[]{postId,FrameworkConstants.STATUS_EFFECTIVE});
		if(userPost.size()>0){
			return "user";
		}
		return "ok";
	}
	
    
	
	public List<SysPost> findSysPostNoSetByRoleId(String roleId){
		List posts = new ArrayList(1);
		ISysRoleService sysRoleService = (ISysRoleService)ContextHolder.getSpringBean("sysRoleService");
		SysRole sysrole = sysRoleService.findUniqueByProperty("id", roleId, QueryConstants.EQUAL);
		
		if(sysrole.getRoleLevel().equals("0")){
			String hql="select p from SysPost p,SysOrgInfo o where  p.orgCode = o.orgCode and p.status=?  and " +
					"not exists (select 1 from SysPostRole u where p.id=u.postId and u.roleId=?) order by p.postName";
				//String hql=" from SysPost p where p.status=? and p.postLevel=(select roleLevel from SysRole where id=?) and " +
					//"not exists (select 1 from SysPostRole u where p.id=u.postId and u.roleId=?)";
	        posts= this.findByHql(hql, new String[]{FrameworkConstants.STATUS_EFFECTIVE,roleId});
			
		}else{
			String hql="select p from SysPost p,SysOrgInfo o where  p.orgCode = o.orgCode and p.status=? and p.postLevel=(select roleLevel from SysRole where id=?) and " +
				"not exists (select 1 from SysPostRole u where p.id=u.postId and u.roleId=?) order by p.postName";
			//String hql=" from SysPost p where p.status=? and p.postLevel=(select roleLevel from SysRole where id=?) and " +
				//"not exists (select 1 from SysPostRole u where p.id=u.postId and u.roleId=?)";
        	posts= this.findByHql(hql, new String[]{FrameworkConstants.STATUS_EFFECTIVE,roleId,roleId});
		}

        Map map=new HashMap();
		map.put("orgCode", "orgName");
		IdfCodeTransfer codeTransfer = ContextHolder.getSpringBean("mdf.codeTransfer");
		codeTransfer.transferResult(posts, "sysOrgInfoService", map);
        return posts;
	}
	
	public List<SysPost> findSysPostSetByRoleId(String roleId){
		String hql="select p from SysPost p,SysOrgInfo o where p.orgCode = o.orgCode and p.status=? and exists (select 1 from SysPostRole u where p.id=u.postId and u.roleId=?)";
		//String hql=" from SysPost p where p.status=? and exists (select 1 from SysPostRole u where p.id=u.postId and u.roleId=?)";
		List posts= this.findByHql(hql, new String[]{FrameworkConstants.STATUS_EFFECTIVE,roleId});
	     Map map=new HashMap();
			map.put("orgCode", "orgName");
			IdfCodeTransfer codeTransfer = ContextHolder.getSpringBean("mdf.codeTransfer");
			codeTransfer.transferResult(posts, "sysOrgInfoService", map);
		return posts;
	}
	

	

}

