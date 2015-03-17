package com.innofi.component.rbac.post.action;
import com.innofi.framework.FrameworkConstants;
import com.innofi.component.rbac.post.pojo.SysUserPost;
import com.innofi.component.rbac.post.service.ISysUserPostService;

import com.innofi.framework.action.impl.BaseActionImpl;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.QueryConstants;



import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import java.util.Map;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;


/**
 * 为dorado7界面维护操作的提供支持，实现SysUserPost对象信息的加载与保存操作
 */

@Component
public class SysUserPostAction extends BaseActionImpl {	
    @Resource
	private   ISysUserPostService sysUserPostService;

    @DataProvider
    public void findSysUserPosts(Page page, Map<String, Object> parameter) throws Exception {
    	List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
        com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
        sysUserPostService.findByPage_Filters(propertyFilters, null, innofiPage);
    }

    @DataProvider
    public SysUserPost findUserMainPostByUserId(String userId) throws Exception {
        return sysUserPostService.findUserMainPostByUserId(userId);
    }
    
    @DataProvider
    public List<SysUserPost> findUserSecondaryPostByUserId(String userId) throws Exception {
    	return sysUserPostService.findUserSecondaryPostByUserId(userId);
    }
    
    @DataResolver
    public void saveSysUserPosts(Collection<SysUserPost> objs) {
		for (Iterator iterator = objs.iterator(); iterator.hasNext();) {
			SysUserPost sysUserPost = (SysUserPost) iterator.next();
			EntityState state = EntityUtils.getState(sysUserPost);
			if (EntityState.DELETED.equals(state)) {
				sysUserPostService.delete(sysUserPost);
			} else if (EntityState.MODIFIED.equals(state) || EntityState.MOVED.equals(state)) {
				sysUserPostService.update(sysUserPost);
			} else if (EntityState.NEW.equals(state)) {
				sysUserPostService.save(sysUserPost);
			}
		}
    }
    
    @DataProvider
    public List<SysUserPost> findUsersByMainPostId(String postId) throws Exception {
    	return sysUserPostService.findUsersByMainPostId(postId);
    }
    
    @DataProvider
    public void findSysUserByPostId(Page page, Map<String, Object> parameter) throws Exception {
    	String postId=(String) parameter.get("postId");
    	List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
    	addPropertyFilter(propertyFilters,"status", FrameworkConstants.STATUS_EFFECTIVE,QueryConstants.EQUAL,true);//查询有效
    	if(parameter != null){
			addPropertyFilter(propertyFilters,"userName",parameter.get("userName"),QueryConstants.LIKE,true); 
			addPropertyFilter(propertyFilters,"orgCode",parameter.get("orgCode"),QueryConstants.EQUAL,true);
    	}
    	this.removePropertyFilter(propertyFilters, "postId");
        com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
        sysUserPostService.findSysUserByPostId(postId,propertyFilters,innofiPage);
    }
    
    @DataResolver
    public void saveSysUserMainPosts(SysUserPost userPost) {
    	if(StringUtils.isNotBlank(userPost.getId())){
    		sysUserPostService.update(userPost);
    	}else{
    		sysUserPostService.save(userPost);
    	}
    
    }
}
