
package com.innofi.component.rbac.privilage.service.impl;


import com.innofi.component.rbac.post.service.ISysUserPostService;
import com.innofi.component.rbac.privilage.service.ISysPostRoleService;
import com.innofi.component.rbac.role.service.ISysRoleService;
import com.innofi.framework.FrameworkConstants;
import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.PropertyOrder;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.framework.dao.pagination.Page;
import com.innofi.component.rbac.post.pojo.SysPost;
import com.innofi.component.rbac.privilage.pojo.SysPostRole;
import com.innofi.component.rbac.role.pojo.SysRole;
import com.innofi.component.rbac.post.pojo.SysUserPost;
import com.innofi.framework.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 功能/ 模块：权限管理
 *
 * @author  todo 开发人员姓名 邮箱
 * @version todo 当前版本 日期
 *          todo 类描述
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
@Component(value = "sysPostRoleService")
public class SysPostRoleServiceImpl extends BaseServiceImpl<SysPostRole, String> implements ISysPostRoleService {

    @Resource(name="mdDataTitleTableMappingDaoSupport")
    protected DaoSupport daoSupport;
    @Resource
    private ISysUserPostService sysUserPostService;

    public DaoSupport getDaoSupport(){
        return daoSupport;
    }


    public void saveSysPostRole(String roleId, List<SysPost> adds, List<SysPost> removes) {
        for (SysPost post : adds) {
            SysPostRole postRole = new SysPostRole();
            postRole.setPostId(post.getId());
            postRole.setRoleId(roleId);
            this.save(postRole);
        }
        for (SysPost post : removes) {
            List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
            this.addPropertyFilter(filters, "roleId", roleId, QueryConstants.EQUAL, true);
            this.addPropertyFilter(filters, "postId", post.getId(), QueryConstants.EQUAL, true);
            this.deleteByPropertyFilters(filters);
        }
    }

    public List<SysPostRole> findSysPostRoleByUserId(String userId) {
        String hql = "select u from SysUserPost u,SysPost p where u.postId=p.id and p.status=? and u.userId=?";//查询有效岗位
        List<SysUserPost> userPosts = sysUserPostService.findByHql(hql, new String[]{FrameworkConstants.STATUS_EFFECTIVE, userId});
        List postList = new ArrayList();
        for (SysUserPost userPost : userPosts) {
            postList.add(userPost.getPostId());
        }
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        this.addPropertyFilter(filters, "postId", postList, QueryConstants.IN, true);
        return this.findByPropertyFilters(filters, null);
    }


    /**
     * 通过岗位找到具备的角色信息
     */
    public List<SysPostRole> findSysPostRoleByPostId(String postId) {
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        this.addPropertyFilter(filters, "postId", postId, QueryConstants.EQUAL, true);
        List<SysPostRole> postRoles = this.findByPropertyFilters(filters, null);
        ISysRoleService sysRoleService = getSpringBean("sysRoleService");
        Map<String, SysRole> roleMap = sysRoleService.findAllRolesMap();
        for (SysPostRole postRole : postRoles) {
            postRole.setRoleName(roleMap.get(postRole.getRoleId()).getRoleName());
            postRole.setRoleType(roleMap.get(postRole.getRoleId()).getRoleType());//add by 毛小兵 增加角色类型
            postRole.setStatus(roleMap.get(postRole.getRoleId()).getStatus());
        }
        return postRoles;
    }


    /**
     * 通过岗位找到匹配的角色，并勾选在PAGE页中
     */
    public void findSelectedSysRoleByPostId(String postId, List<PropertyFilter> filters,
                                            Page page) {
        ISysRoleService sysRoleService = getSpringBean("sysRoleService");
	  	List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
 	    PropertyOrder order = new PropertyOrder("roleType", QueryConstants.ORDER_ASC);
    	PropertyOrder order2 = new PropertyOrder("roleName", QueryConstants.ORDER_ASC);
 	    orders.add(order);
 	    orders.add(order2);
        sysRoleService.findByPage_Filters(filters, orders, page);
        Map<String, SysPostRole> postMap = findSysPostRoleMapByPostId(postId);
        List<SysRole> roles = page.getEntities();
        for (SysRole role : roles) {
            if (postMap.containsKey(role.getId())) {
                role.setSelectFlag(true);
            }
        }
    }

    private Map<String, SysPostRole> findSysPostRoleMapByPostId(String postId) {
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        this.addPropertyFilter(filters, "postId", postId, QueryConstants.EQUAL, true);
        List<SysPostRole> postRoles = this.findByPropertyFilters(filters, null);
        Map<String, SysPostRole> postRoleMap = new HashMap<String, SysPostRole>();
        for (SysPostRole postRole : postRoles) {
            postRoleMap.put(postRole.getRoleId(), postRole);
        }
        return postRoleMap;
    }

}

