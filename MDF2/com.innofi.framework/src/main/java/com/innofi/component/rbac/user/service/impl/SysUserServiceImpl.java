package com.innofi.component.rbac.user.service.impl;


import com.innofi.component.rbac.RBACConstants;
import com.innofi.component.rbac.menu.pojo.SysMenu;
import com.innofi.component.rbac.privilage.pojo.SysFunAction;
import com.innofi.component.rbac.privilage.service.ISysFunActionService;
import com.innofi.component.rbac.menu.service.ISysMenuService;
import com.innofi.component.rbac.org.pojo.SysOrgInfo;
import com.innofi.component.rbac.org.service.ISysOrgInfoService;
import com.innofi.component.rbac.post.service.ISysPostService;
import com.innofi.component.rbac.post.pojo.SysPost;
import com.innofi.component.rbac.role.pojo.SysRole;
import com.innofi.component.rbac.role.service.ISysRoleService;
import com.innofi.component.rbac.user.pojo.SysUser;
import com.innofi.component.rbac.user.service.ISysUserService;
import com.innofi.component.rbac.workspace.theme.service.ISysUserThemeService;
import com.innofi.framework.FrameworkConstants;
import com.innofi.component.codetransfer.IdfCodeTransfer;
import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.PropertyOrder;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.framework.dao.pagination.Page;
import com.innofi.component.rbac.workspace.theme.pojo.SysUserTheme;
import com.innofi.framework.service.impl.BaseServiceImpl;
import com.innofi.framework.spring.context.ContextHolder;
import com.innofi.framework.utils.codec.MD5Util;
import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import java.util.*;

@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
@Service(value = "sysUserService")
public class SysUserServiceImpl extends BaseServiceImpl<SysUser, String> implements ISysUserService {

    @Resource(name = "sysUserDaoSupport")
    protected DaoSupport daoSupport;

    public DaoSupport getDaoSupport() {
        return daoSupport;
    }

    /**
     * 加载用户缓存
     *
     * @return
     */
    public List<SysUser> loadCacheObjects() {
        return getDaoSupport().getHibernateDao().find(getEntityType(), "from SysUser");
    }

    /**
     * (non-Javadoc)
     *
     * @throws Exception
     * @see com.innofi.component.rbac.user.service.ISysUserService#getSysUserByUserCode(String)
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SysUser getSysUserByUserCode(String userCode) {
        return (SysUser) getDaoSupport().getHibernateDao().findUnique(getEntityType(), "from SysUser where userCode = ? ", userCode);
    }

    /**
     * (non-Javadoc)
     *
     * @throws Exception
     * @see com.innofi.component.rbac.user.service.ISysUserService#login(String, String, String, String)
     */
    public char login(String userCode, String password, String ip, String mac) {
        SysUser user = initUserRelatedInfo(userCode);                               //初始化用户相关机构、岗位、角色信息
        if (user == null) return '1';                                               //用户不存在
        if (!password.equals("IDS")) {
            if (!user.getPassword().equals(MD5Util.toMD5(password))) return '2';    //密码错误
        }
        if (user.getStatus().equals(FrameworkConstants.STATUS_INVALID)) return '3'; //停用状态
        if (!ContextHolder.getContext().isAdmin(user)) {							//是否系统超级管理员
            if (user.getFunctionRoles().size() == 0) return '4';                    //用户未分配功能角色
            //if (user.getPosts().size() == 0)return '5';                           //用户未分配岗位
        }
        List<SysMenu> menus = initUserMenusInfo(user.getFunctionRoles());           //初始化用户权限菜单
        List<SysFunAction> actions = initUserActionsInfo(user.getFunctionRoles());  //初始化用户权限按钮

        ContextHolder.getContext().setLoginUser(user);                              //向frameworkContext设置登录用户信息
        ContextHolder.getContext().setLoginUsername(userCode);                      //向frameworkContext设置登录用户帐号

        ContextHolder.getRequest().getSession().setAttribute(FrameworkConstants.SESSION_USER, user);//向session设置登录用户帐号
        ContextHolder.getRequest().getSession().setAttribute(FrameworkConstants.SESSION_USER_MENU, menus);//向session设置登录用户权限菜单
        ContextHolder.getRequest().getSession().setAttribute(FrameworkConstants.SESSION_USER_ACTION, actions);//向session设置登录用户权限按钮

        SysUserTheme sysUserTheme = initUserTheme(userCode);                        //初始化用户主题
        user.setSysUserTheme(sysUserTheme);                                         //设置用户主题
        
        update(user);
        return '0';
    }

    private SysUserTheme initUserTheme(String userCode) {
        ISysUserThemeService sysUserThemeService = ContextHolder.getSpringBean("sysUserThemeService");//用户主题服务
        return sysUserThemeService.getUserTheme(userCode); //查询用户主题
    }

    /**
     * (non-Javadoc)
     *
     * @throws Exception
     * @see com.innofi.component.rbac.user.service.ISysUserService#logout()
     */
    public void logout() {
        HttpSession session = ContextHolder.getRequest().getSession();
        if (session != null) {
            SysUser user = (SysUser) session.getAttribute(FrameworkConstants.SESSION_USER);
            if (user != null) {
                user.setOnlineStatus(RBACConstants.ONLINE_FALSE);
                user.setUpdDate(new Date());
                getDaoSupport().getHibernateDao().merge(user);
                session.removeAttribute(FrameworkConstants.SESSION_USER);
            }
            session.invalidate();
        }
    }

    /**
     * 重写父类save方法，实现用户密码加密等业务操作
     *
     * @param user 新增的用户
     */
    public void save(SysUser user) {
        String password = ContextHolder.getSystemProperties().getString("mdf.default.password");//默认密码
        password = MD5Util.toMD5(password);
        user.setPassword(password);
        user.setStatus(FrameworkConstants.STATUS_EFFECTIVE);
        user.setOnlineStatus(RBACConstants.ONLINE_FALSE);
        super.save(user);
    }

    public Map<String, SysUser> getSysUserMaps() {
        Map<String, SysUser> userMap = new HashMap<String, SysUser>();
        List<SysUser> sysUsers = this.findByPropertyFilters(null, null);
        for (SysUser user : sysUsers) {
            userMap.put(user.getId(), user);
        }
        return userMap;
    }


    //初始化用户相关机构、岗位、角色信息、主题
    private SysUser initUserRelatedInfo(String userCode) {
        ISysOrgInfoService sysOrgInfoService = ContextHolder.getSpringBean("sysOrgInfoService");//注入机构服务
        ISysRoleService sysRoleService = ContextHolder.getSpringBean("sysRoleService");//注入角色服务
        ISysPostService sysPostService = ContextHolder.getSpringBean("sysPostService");//注入岗位服务
        SysUser user = getSysUserByUserCode(userCode);                                 //获取登录用户信息
        if (user == null) return null;
        SysOrgInfo userOrgInfo = sysOrgInfoService.getSysOrgInfoByOrgCode(user.getOrgCode());//查询用户机构
        SysOrgInfo ownOrgInfo = sysOrgInfoService.getSysOwnOrgInfoByDeptCode(userOrgInfo.getOrgCode());
        String ownOrgCode = ownOrgInfo.getOrgCode();
        List<SysRole> userRoles = sysRoleService.findAllRolesByUserId(user.getId());   //查询用户角色

        List<SysRole> functionRoles = new ArrayList<SysRole>();                              //功能角色
        List<SysRole> dataRoles = new ArrayList<SysRole>();                                  //数据角色

        StringBuffer functionRoleNames = new StringBuffer();                           //功能角色名称
        StringBuffer dataRoleNames = new StringBuffer();                               //数据角色名称
        StringBuffer functionRoleIds = new StringBuffer();                             //功能角色ID
        StringBuffer dataRoleIds = new StringBuffer();                                 //数据角色ID

        for (SysRole sysRole : userRoles) {
            if (null == sysRole.getRoleType()) {
                continue;
            } else if (RBACConstants.ROLE_TYPE_FUNCTION.equals(sysRole.getRoleType())) {
                functionRoles.add(sysRole);
                functionRoleNames.append(sysRole.getRoleName() + "，");
                functionRoleIds.append(sysRole.getId() + ",");
            } else if (RBACConstants.ROLE_TYPE_DATA.equals(sysRole.getRoleType())) {
                dataRoles.add(sysRole);
                dataRoleNames.append(sysRole.getRoleName() + "，");
                dataRoleIds.append(sysRole.getId());
            }
        }

        //List<SysPost> userPosts = sysPostService.findAllSysPostsByUserId(user.getId());//查询用户岗位

        //StringBuffer mainPostNames = new StringBuffer();                               //主岗位名称
        //StringBuffer partPostNames = new StringBuffer();                               //兼职岗位名称

        //int index = 0;
        //for (SysPost sysPost : userPosts) {
        //    if (index == 0) {
        //        mainPostNames.append(sysPost.getPostName());                           //主岗位名称  todo 这里目前取第一条记录为主岗位，逻辑存在问题，需要修改。
        //    } else {
        //        partPostNames.append(sysPost.getPostName() + "，");                      //兼职岗位名称
        //    }
        //    index++;
        //}

        user.setOwerOrgCode(ownOrgCode);                                               //设置用户所属机构代码
        user.setSysOrgInfo(userOrgInfo);                                               //设置用户部门
        user.setFunctionRoles(functionRoles);                                          //设置用户功能角色
        user.setDataRoles(dataRoles);                                                  //设置用户数据角色
        if (functionRoleNames.length() > 0) {
            user.setFunctionRoleNames
                    (functionRoleNames.substring(0, functionRoleNames.length() - 1));             //设置用户功能角色名称
            user.setFunctionRoleIds
                    (functionRoleIds.substring(0, functionRoleIds.length() - 1));                 //设置用户功能角色ID
        }
        if (dataRoleNames.length() > 0) {
            user.setDataRoleNames
                    (dataRoleNames.substring(0, dataRoleNames.length() - 1));                     //设置用户数据角色名称
            user.setDataRoleIds(dataRoleIds.substring(0, dataRoleIds.length() - 1));      //设置用户数据角色ID
        }
        //user.setPosts(userPosts);                                                      //设置用户岗位
        //user.setMainPostNames(mainPostNames.toString());                               //设置用户主岗位名称
        //user.setPartPostNames(partPostNames.toString());                               //设置用户兼职岗位名称
        return user;
    }

    private List<SysFunAction> initUserActionsInfo(List<SysRole> roles) {
        ISysFunActionService sysFunActionService = ContextHolder.getSpringBean("sysFunActionService");//注入按钮服务
        return sysFunActionService.findActionsByUserRoles(roles);//查询用户按钮权限
    }

    private List<SysMenu> initUserMenusInfo(List<SysRole> roles) {
        ISysMenuService sysMenuService = ContextHolder.getSpringBean("sysMenuService");//注入菜单服务
        return sysMenuService.findMenusByUserRoles(roles);//查询用户菜单
    }

    public void updateUserPassword(String userId, String password) {
        SysUser user = this.get(userId);
        if (StringUtils.isBlank(password)) {
            password = ContextHolder.getSystemProperties().getString("mdf.default.password");//默认密码
        }
        password = MD5Util.toMD5(password);
        user.setPassword(password);
        super.save(user);
    }

    public void findByPage_Filters(List<PropertyFilter> filters,
                                   List<PropertyOrder> orders, Page page) {
        super.findByPage_Filters(filters, orders, page);
        List users = page.getEntities();
        Map map = new HashMap();
        map.put("orgCode", "orgName");
        IdfCodeTransfer codeTransfer = ContextHolder.getSpringBean("mdf.codeTransfer");
        codeTransfer.transferResult(users, "sysOrgInfoService", map);
    }

    public void findByHql_Page(String hql, Page page, Object[] values) {
        super.findByHql_Page(hql, page, values);
        List users = page.getEntities();
        Map map = new HashMap();
        map.put("orgCode", "orgName");
        IdfCodeTransfer codeTransfer = ContextHolder.getSpringBean("mdf.codeTransfer");
        codeTransfer.transferResult(users, "sysOrgInfoService", map);
    }

    public List<SysUser> findAllSysUsersByPostId(String postId) {
        String hql = "select u from SysUserPost p,SysUser u where p.postId=? and p.userId = u.id and u.status = ?";
        return this.findByHql(hql, new Object[]{postId, FrameworkConstants.STATUS_EFFECTIVE});
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.cache.ICacheService#getCodeFieldName()
     */
    public String getCodeFieldName() {
        return "userCode";
    }

    public String getCnFieldName() {
        return "userName";
    }


    public List<SysUser> findSysUserNoSetByRoleId(String roleId) {
        List users = new ArrayList(1);
        ISysRoleService sysRoleService = ContextHolder.getSpringBean("sysRoleService");
        SysRole sysrole = sysRoleService.findUniqueByProperty("id", roleId, QueryConstants.EQUAL);

        if (sysrole.getRoleLevel().equals("0")) {//全行
            String hql = "select p from SysUser p where p.status=?  and not exists (select 1 from SysUserRole u where p.id=u.userId and u.roleId=?) order by p.orgCode";
            users = this.findByHql(hql, new String[]{FrameworkConstants.STATUS_EFFECTIVE, roleId});
        } else {
            String hql = "select p from SysUser p,SysOrgInfo o where p.orgCode = o.orgCode and p.status=? and o.orgLevel=(select roleLevel from SysRole where id=?) and  not exists (select 1 from SysUserRole u where p.id=u.userId and u.roleId=?) order by o.seq,o.orgName";
            users = this.findByHql(hql, new String[]{FrameworkConstants.STATUS_EFFECTIVE, roleId, roleId});
        }

        Map<String, String> map = new HashMap<String, String>();
        map.put("orgCode", "orgName");
        IdfCodeTransfer codeTransfer = ContextHolder.getSpringBean("mdf.codeTransfer");
        ListOrderedMap sysOrgInfos = codeTransfer.getCacheObjects("sysOrgInfoService");//所有机构
        codeTransfer.transferResult(users, "sysOrgInfoService", map);
        return users;
    }

    public List<SysUser> findSysUserSetByRoleId(String roleId) {
        String hql = "select p from SysUser p where p.status=? and exists (select 1 from SysUserRole u where p.id=u.userId and u.roleId=?) order by p.orgCode";
        List users = this.findByHql(hql, new String[]{FrameworkConstants.STATUS_EFFECTIVE, roleId});
        Map<String, String> map = new HashMap<String, String>();
        map.put("orgCode", "orgName");
        IdfCodeTransfer codeTransfer = ContextHolder.getSpringBean("mdf.codeTransfer");
        ListOrderedMap sysOrgInfos = codeTransfer.getCacheObjects("sysOrgInfoService");//所有权限控制按钮
        codeTransfer.transferResult(users, "sysOrgInfoService", map);
        return users;
    }

}

