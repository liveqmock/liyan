package com.innofi.component.rbac.org.service.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import com.innofi.component.rbac.RBACConstants;
import com.innofi.component.rbac.org.service.ISysOrgInfoService;
import com.innofi.component.rbac.post.service.ISysPostService;
import com.innofi.framework.service.impl.BaseServiceImpl;
import com.innofi.component.rbac.role.service.ISysRoleService;
import com.innofi.component.rbac.user.service.ISysUserService;
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
import com.innofi.component.rbac.user.pojo.SysUser;
import com.innofi.framework.spring.context.ContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @author liumy2009@126.com
 * @version 2.0
 *          组织机构服务实现类
 * @found date: 2010-11-23
 * @found time: 20:40:56
 */
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
@Service(value = "sysOrgInfoService")
public class SysOrgInfoServiceImpl extends BaseServiceImpl<SysOrgInfo, String> implements ISysOrgInfoService {

    @Resource(name="mdDataTitleTableMappingDaoSupport")
    protected DaoSupport daoSupport;

    public DaoSupport getDaoSupport() {
        return daoSupport;
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.cache.ICacheService#getCodeFieldName()
     */
    public String getCodeFieldName() {
        return "orgCode";
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.cache.ICacheService#getCnFieldName()
     */
    public String getCnFieldName() {
        return "orgName";
    }

    /**
     * 缓存机构整表数据
     *
     * @return
     */
    public List<SysOrgInfo> loadCacheObjects() {
        return getDaoSupport().getHibernateDao().find(getEntityType(), "from SysOrgInfo");
    }


    /**
     * @see com.innofi.component.rbac.org.service.ISysOrgInfoService#findSysOrgInfoList(String, String, java.util.List, com.innofi.framework.dao.pagination.Page)
     */
    public void findSysOrgInfoList(String searchType, String parentOrgCode, List<PropertyFilter> filters, Page innofiPage) {
        List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
        orders.add(new PropertyOrder("category", QueryConstants.ORDER_ASC));
        orders.add(new PropertyOrder("seq", QueryConstants.ORDER_ASC));

        if (!StringUtils.isBlank(searchType) && "tree".equals(searchType) && !StringUtils.isBlank(parentOrgCode)) {
            this.removePropertyFilter(filters, "parentOrgCode");
            this.addPropertyFilter(filters, "orgCode", parentOrgCode, QueryConstants.NOT_EQUAL, true);
            this.addPropertyFilter(filters, "orgSeq", "." + parentOrgCode + ".", QueryConstants.LIKE, true);
            findByPage_Filters(filters, orders, innofiPage);
        } else {
            findByPage_Filters(filters, orders, innofiPage);
        }
    }

    /**
     * @see com.innofi.component.rbac.org.service.ISysOrgInfoService#getSysOrgInfoMaps()
     */
    public Map<String, SysOrgInfo> getSysOrgInfoMaps() {
        Map<String, SysOrgInfo> orgInfoMap = new HashMap<String, SysOrgInfo>();
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        this.addPropertyFilter(filters, "status", FrameworkConstants.STATUS_EFFECTIVE, QueryConstants.EQUAL, true);
        List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
        PropertyOrder order = new PropertyOrder("category", QueryConstants.ORDER_ASC);
        orders.add(order);
        List<SysOrgInfo> orgInfos = findByPropertyFilters(filters, orders);
        for (SysOrgInfo orgInfo : orgInfos) {
            orgInfoMap.put(orgInfo.getOrgCode(), orgInfo);
        }
        return orgInfoMap;
    }

    /**
     * @see com.innofi.component.rbac.org.service.ISysOrgInfoService#getSysOrgInfoByOrgCode(String)
     */
    public SysOrgInfo getSysOrgInfoByOrgCode(String orgCode) {
        return (SysOrgInfo) this.getDaoSupport().getHibernateDao().findUnique(getEntityType(), "from SysOrgInfo where orgCode = ?", orgCode);
    }

    /**
     * @see com.innofi.component.rbac.org.service.ISysOrgInfoService#checkObjectExist(String)
     */
    public boolean checkObjectExist(String orgCode) {
        List<SysOrgInfo> orgs = this.getDaoSupport().getHibernateDao().findByProperty(getEntityType(), "orgCode", orgCode, QueryConstants.EQUAL);
        if (orgs.size() != 0) return true;
        return false;
    }

    /**
     * @see com.innofi.component.rbac.org.service.ISysOrgInfoService#checkObjectExist(String)
     */
    public boolean invalidOrg(String orgCode) {
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        this.addPropertyFilter(filters, "orgSeq", "." + orgCode + ".", QueryConstants.LIKE, true);
        this.addPropertyFilter(filters, "status", FrameworkConstants.STATUS_EFFECTIVE, QueryConstants.EQUAL, true);
        this.addPropertyFilter(filters, "orgCode", orgCode, QueryConstants.NOT_EQUAL, true);
        List<SysOrgInfo> list = this.findByPropertyFilters(filters, null);
        if (list.size() > 0) {
            return false;
        }
        filters.clear();
        this.addPropertyFilter(filters, "parentOrgCode", orgCode, QueryConstants.EQUAL, true);
        this.addPropertyFilter(filters, "status", FrameworkConstants.STATUS_EFFECTIVE, QueryConstants.EQUAL, true);
        list = this.findByPropertyFilters(filters, null);
        if (list.size() > 0) {
            return false;
        }
        SysOrgInfo org = this.findUniqueByProperty("orgCode", orgCode, QueryConstants.EQUAL);
        org.setStatus(FrameworkConstants.STATUS_INVALID);
        this.update(org);
        return true;
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.rbac.org.service.ISysOrgInfoService#findChildrenByOrgCode(String)
     */
    public List<SysOrgInfo> findChildrenByOrgCode(String orgCode) {
        List<SysOrgInfo> childrenList = new ArrayList<SysOrgInfo>();
        SysOrgInfo sysOrgInfo = findUniqueByProperty("orgCode", orgCode, QueryConstants.EQUAL);
        if (sysOrgInfo.getCategory().equals(RBACConstants.ORG_CATEGORY_ORG)) {     //如果为机构，先找下级部门
            List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
            this.addPropertyFilter(filters, "status", FrameworkConstants.STATUS_EFFECTIVE, QueryConstants.EQUAL, true);
            this.addPropertyFilter(filters, "parentOrgCode", orgCode, QueryConstants.EQUAL, true);
            List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
            PropertyOrder order = new PropertyOrder("category", QueryConstants.ORDER_ASC);
            orders.add(order);
            List<SysOrgInfo> depts = this.findByPropertyFilters(filters, orders);
            for (SysOrgInfo dept : depts) {
                List<SysOrgInfo> branchDepts = getDownLevelOrgsByOrgCode(dept.getOrgCode());
                childrenList.addAll(branchDepts);
            }
        }
        List<SysOrgInfo> subOrgs = getDownLevelOrgsByOrgCode(orgCode);

        childrenList.addAll(subOrgs);

        return childrenList;
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.rbac.org.service.ISysOrgInfoService#invalidDep(String)
     */
    public String invalidDep(String orgCode) {
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        this.addPropertyFilter(filters, "status", FrameworkConstants.STATUS_EFFECTIVE, QueryConstants.EQUAL, true);
        //判断部门
        this.addPropertyFilter(filters, "orgSeq", "." + orgCode + ".", QueryConstants.LIKE, true);
        this.addPropertyFilter(filters, "orgCode", orgCode, QueryConstants.NOT_EQUAL, true);
        this.addPropertyFilter(filters, "status", FrameworkConstants.STATUS_EFFECTIVE, QueryConstants.EQUAL, true);
        List<SysOrgInfo> orgs = this.findByPropertyFilters(filters, null);
        if (orgs.size() > 0) {
            return "org";
        }
        //判断岗位
        this.removePropertyFilter(filters, "orgSeq");
        this.addPropertyFilter(filters, "orgCode", orgCode, QueryConstants.EQUAL, true);
        this.addPropertyFilter(filters, "status", FrameworkConstants.STATUS_EFFECTIVE, QueryConstants.EQUAL, true);
        ISysPostService sysPostService = getSpringBean("sysPostService");
        List<SysPost> posts = sysPostService.findByPropertyFilters(filters, null);
        if (posts.size() > 0) {
            return "post";
        }
        //判断用户
        ISysUserService sysUserService = getSpringBean("sysUserService");
        List<SysUser> users = sysUserService.findByPropertyFilters(filters, null);
        if (users.size() > 0) {
            return "user";
        }
        return "ok";
    }

    public List<SysOrgInfo> findByPropertyFilters(List<PropertyFilter> filters, List<PropertyOrder> orders) {
        List orgs = super.findByPropertyFilters(filters, orders);
        Map map = new HashMap();
        map.put("parentOrgCode", "parentOrgName");
        IdfCodeTransfer codeTransfer = ContextHolder.getSpringBean("mdf.codeTransfer");
        codeTransfer.transferResult(orgs, "sysOrgInfoService", map);
        return orgs;
    }

    /**
     * 通过机构节点找到节点信息
     */
    public SysOrgInfo getOrgInfoByOrgCode(String orgCode) {
        return (SysOrgInfo) this.getDaoSupport().getHibernateDao().findUnique(getEntityType(), " from SysOrgInfo where orgCode=?", orgCode);
    }

    /**
     * 通过父节点找到子节点信息
     */
    public List<SysOrgInfo> getOrgInfosByParentOrgCode(String parentOrgCode) {
        List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
        orders.add(new PropertyOrder("orgSeq", QueryConstants.ORDER_ASC));
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        this.addPropertyFilter(filters, "status", FrameworkConstants.STATUS_EFFECTIVE, QueryConstants.EQUAL, true);
        this.addPropertyFilter(filters, "parentOrgCode", parentOrgCode, QueryConstants.EQUAL, true);
        return this.getDaoSupport().getHibernateDao().findByFilters(getEntityType(), filters, orders);
    }

    /**
     * U：所有本上级节点*
     */
    public List<SysOrgInfo> getUpLevelOrgsByOrgCode(String orgCode) {
        List<SysOrgInfo> list = new ArrayList();
        //满足ORG_SEQ .a.b.c. 的规则。
        SysOrgInfo org = getOrgInfoByOrgCode(orgCode);
        if (null != org) {
            //获取Seq
            String orgSeq = org.getOrgSeq();
            orgSeq = orgSeq.substring(1, orgSeq.length() - 1);//去除首尾字符
            if (orgSeq.indexOf(".") > 0) {
                String orgs[] = StringUtils.split(orgSeq, ".");
                for (int i = 0; i < orgs.length; i++) {
                    list.add(getOrgInfoByOrgCode(orgs[i]));
                }
            } else {
                //表示为顶点
                list.add(getOrgInfoByOrgCode(orgSeq));
            }
        }
        return list;
    }

    /**
     * D：所有本下级节点 *
     */
    public List<SysOrgInfo> getDownLevelOrgsByOrgCode(String orgCode) {
        List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
        orders.add(new PropertyOrder("category", QueryConstants.ORDER_ASC));
        orders.add(new PropertyOrder("seq", QueryConstants.ORDER_ASC));
        return this.getDaoSupport().getHibernateDao().findByProperty(getEntityType(), "orgSeq", "%." + orgCode + ".%", QueryConstants.LIKE, orders);
    }


    /**
     * V：垂直所有节点 *
     */
    public List<SysOrgInfo> getVerticalLevelOrgsByOrgCode(String orgCode) {
        List<SysOrgInfo> list = new ArrayList();
        //这里 直接取上一级节点开始
        list.addAll(getUpLevelOrgsByOrgCode(orgCode));//本上级所有节点
        list.remove(getUpLevelOrgsByOrgCode(orgCode).size() - 1);//移除本上级节点最后一个节点 即本节点
        list.addAll(getDownLevelOrgsByOrgCode(orgCode));//本下级所有节点
        return list;
    }

    /**
     * H: 水平所有节点 *
     */
    public List<SysOrgInfo> getSameLevelOrgsOrgCode(String orgCode) {
        SysOrgInfo org = getOrgInfoByOrgCode(orgCode);
        return getOrgInfosByParentOrgCode(org.getParentOrgCode());
    }
    /**O: 其它实际值**/


    /**
     * 受权限控制的迭代展开
     */
    public List<SysOrgInfo> findSysOrgInfoTreeForPolicy(List<PropertyFilter> filters) {
        List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
        orders.add(new PropertyOrder("category", QueryConstants.ORDER_ASC));
        orders.add(new PropertyOrder("seq", QueryConstants.ORDER_ASC));
        //1不受权限控制的 树的迭代展开。去标记通过策略展开的树
        //this.addPropertyFilter(filters, "status", FrameworkConstants.STATUS_EFFECTIVE, QueryConstants.EQUAL, true);
        List<SysOrgInfo> allTreeList = this.getDaoSupport().getHibernateDao().findByFilters(SysOrgInfo.class, filters, orders);

        //2按树权限控制的 树的迭代展开。
        List<SysOrgInfo> policyTreeList = this.findByPropertyFilters(filters, orders);
        //设置策略中的键值对
        Map policyOrgMap = new HashMap();
        for (SysOrgInfo org : policyTreeList) {
            policyOrgMap.put(org.getOrgCode(), "true");
        }
        for (SysOrgInfo org : allTreeList) {
            if ("true".equals(policyOrgMap.get(org.getOrgCode()))) {
                org.setPolicyFlag(true);
            }
        }
        return allTreeList;
    }

    /**
     * 无权限全树展开
     *
     * @param filters
     * @return
     */
    public List<SysOrgInfo> findSysOrgInfoAllTree(List<PropertyFilter> filters) {
        List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
        orders.add(new PropertyOrder("category", QueryConstants.ORDER_ASC));
        orders.add(new PropertyOrder("seq", QueryConstants.ORDER_ASC));
        List<SysOrgInfo> allTreeList = this.getDaoSupport().getHibernateDao().findByFilters(SysOrgInfo.class, filters, orders);
        return allTreeList;
    }


    /**
     * 根据机构ID获取对应的角色字符串
     *
     * @param name
     * @param @return 设定文件
     * @return String    DOM对象
     * @Exception 异常对象
     * @since CodingExample　Ver(编码范例查看) 1.1
     */
    public Map<String, StringBuffer> findRolesStringByOrgCodes(List<String> orgCodeList) {
        Map<String, StringBuffer> orgCodeToRolesMap_over = new HashMap();
        if (orgCodeList.size() > 0) {
            String orgCodes = "";
            for (int i = 0; i < orgCodeList.size(); i++) {
                if (StringUtils.isBlank(orgCodes)) {
                    orgCodes = "'" + orgCodeList.get(i) + "'";
                } else {
                    orgCodes += ",'" + orgCodeList.get(i) + "'";
                }
            }
            StringBuffer rolesSqlBuffer = new StringBuffer();
            rolesSqlBuffer.append("SELECT T1.ORG_CODE, t3.dict_code as ROLE_TYPE,T3.Dict_Name as ROLE_TYPE_NAME, T2.ROLE_NAME");
            rolesSqlBuffer.append(" FROM SYS_ORG_ROLE T1,");
            rolesSqlBuffer.append(" SYS_ROLE T2,");
            rolesSqlBuffer.append(" (SELECT DICT_CODE,DICT_NAME");
            rolesSqlBuffer.append(" FROM SYS_DICT");
            rolesSqlBuffer.append(" WHERE Parent_Dict_Id = '*ROLE_TYPE') T3");
            rolesSqlBuffer.append(" WHERE T1.ROLE_ID = T2.ROLE_ID and t3.dict_code = t2.role_type");
            rolesSqlBuffer.append(" and t1.org_code in (" + orgCodes + ")");
            rolesSqlBuffer.append(" order by t1.org_id,t3.dict_code");

            List rolesPerOrgList = this.getDaoSupport().getJdbcDao().queryForList(rolesSqlBuffer.toString());
            Map orgCodeToRolesMap = new HashMap();

            StringBuffer roleBuffer = new StringBuffer();
            for (int i = 0; i < rolesPerOrgList.size(); i++) {
                Map mapRole = (Map) rolesPerOrgList.get(i);
                String ORG_CODE = (String) mapRole.get("ORG_CODE");
                String ORG_TYPE = (String) mapRole.get("ROLE_TYPE");
                String ROLE_TYPE_NAME = (String) mapRole.get("ROLE_TYPE_NAME");
                String ROLE_NAME = (String) mapRole.get("ROLE_NAME");
                String orgAroleType_Key = ORG_CODE + "#" + ORG_TYPE;

                if (null == orgCodeToRolesMap.get(orgAroleType_Key)) {
                    orgCodeToRolesMap.put(orgAroleType_Key, new StringBuffer(ROLE_TYPE_NAME + "-【"));
                }
                roleBuffer = (StringBuffer) orgCodeToRolesMap.get(orgAroleType_Key);
                roleBuffer.append(ROLE_NAME + " ");
            }
            Iterator it = orgCodeToRolesMap.keySet().iterator();
            StringBuffer orgToroleBuffer = new StringBuffer();
            while (it.hasNext()) {
                String key = (String) it.next();
                StringBuffer value = (StringBuffer) orgCodeToRolesMap.get(key);
                String orgCode = key.split("#")[0];
                if (null == orgCodeToRolesMap_over.get(orgCode)) {
                    orgCodeToRolesMap_over.put(orgCode, new StringBuffer());
                }
                orgToroleBuffer = orgCodeToRolesMap_over.get(orgCode);
                orgToroleBuffer.append(value.toString());
                orgToroleBuffer.append("】");
            }
        }
        return orgCodeToRolesMap_over;
    }

    /**
     * 根据当前部门CODE值获取所属机构信息
     * getSysOwnOrgInfoByDeptCode(这里用一句话描述这个方法的作用)
     *
     * @param deptCode
     * @param @return  设定文件
     * @return String    DOM对象
     * @Exception 异常对象
     * @since CodingExample　Ver(编码范例查看) 1.1
     */
    public SysOrgInfo getSysOwnOrgInfoByDeptCode(String deptCode) {
        SysOrgInfo ownOrg = this.getOrgInfoByOrgCode(deptCode);//默认当前部门的信息为所属机构
        String category = ownOrg.getCategory();
        while (RBACConstants.ORG_CATEGORY_DEPT.equals(category)) {//如果当前机构为部门 需要想上去机构直到是机构信息
            ownOrg = this.getOrgInfoByOrgCode(ownOrg.getParentOrgCode());
            category = ownOrg.getCategory();
        }
        return ownOrg;
    }

    /**
     * NEW 1
     * 根据节点获取本级的机构以及机构的所属部门
     * getOwnDeptInfoByOrgCode(这里用一句话描述这个方法的作用)
     *
     * @param orgCode
     * @return String    DOM对象
     * @since CodingExample　Ver(编码范例查看) 1.1
     */
    public List<SysOrgInfo> getOwnDeptInfoByOrgCode(String orgCode) {
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        SysOrgInfo org = this.getOrgInfoByOrgCode(orgCode);
        if (null != org) {
            this.addPropertyFilter(filters, "orgSeq", "%." + orgCode + ".%", QueryConstants.LIKE, true);
            this.addPropertyFilter(filters, "category", RBACConstants.ORG_CATEGORY_DEPT, QueryConstants.EQUAL, true);
            this.addPropertyFilter(filters, "orgLevel", org.getOrgLevel(), QueryConstants.EQUAL, true);
            List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
            orders.add(new PropertyOrder("category", QueryConstants.ORDER_ASC));
            orders.add(new PropertyOrder("seq", QueryConstants.ORDER_ASC));
            return this.findByPropertyFilters(filters, orders);
        } else {
            return null;
        }
    }

    /**
     * NEW2
     * 根据节点去本下级机构节点
     * getDownLevelOrgsByCategoryOrgCode(这里用一句话描述这个方法的作用)
     *
     * @param orgCode
     * @param @return 设定文件
     * @return String    DOM对象
     * @Exception 异常对象
     * @since CodingExample　Ver(编码范例查看) 1.1
     */
    public List<SysOrgInfo> getDownLevelCategoryOrgsByOrgCode(String orgCode) {
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
        this.addPropertyFilter(filters, "orgSeq", "%." + orgCode + ".%", QueryConstants.LIKE, true);
        this.addPropertyFilter(filters, "category", RBACConstants.ORG_CATEGORY_ORG, QueryConstants.EQUAL, true);
        this.addPropertyFilter(filters, "status", FrameworkConstants.STATUS_EFFECTIVE, QueryConstants.EQUAL, true);
        orders.add(new PropertyOrder("category", QueryConstants.ORDER_ASC));
        orders.add(new PropertyOrder("seq", QueryConstants.ORDER_ASC));
        return this.findByPropertyFilters(filters, orders);
    }

    public List<SysOrgInfo> findSysOrgInfoNoSetByRoleId(String roleId) {
        ISysRoleService sysRoleService = (ISysRoleService) ContextHolder.getSpringBean("sysRoleService");
        SysRole sysrole = sysRoleService.findUniqueByProperty("id", roleId, QueryConstants.EQUAL);

        if (sysrole.getRoleLevel().equals("0")) {
            String hql = " from SysOrgInfo p where p.status=? and " +
                    "not exists (select 1 from SysOrgRole u where p.id=u.orgId and u.roleId=?) and p.category=0 order by p.seq,p.orgName";
            return this.findByHql(hql, new String[]{FrameworkConstants.STATUS_EFFECTIVE, roleId});

        } else {
            String hql = " from SysOrgInfo p where p.status=? and p.orgLevel=(select roleLevel from SysRole where id=?) and " +
                    "not exists (select 1 from SysOrgRole u where p.id=u.orgId and u.roleId=?) and p.category=0 order by p.seq,p.orgName";
            return this.findByHql(hql, new String[]{FrameworkConstants.STATUS_EFFECTIVE, roleId, roleId});
        }
    }

    public List<SysOrgInfo> findSysOrgInfoSetByRoleId(String roleId) {
        String hql = " from SysOrgInfo p where p.status=? and exists (select 1 from SysOrgRole u where p.id=u.orgId and u.roleId=? and p.category=0)";
        return this.findByHql(hql, new String[]{FrameworkConstants.STATUS_EFFECTIVE, roleId});
    }

    public SysOrgInfo findUpDeptInfo(String deptCode) {
        SysOrgInfo currentDept = this.getOrgInfoByOrgCode(deptCode);//默认当前部门的信息为所属机构
        String parentDeptCode = currentDept.getParentDeptCode();
        if (StringUtils.isBlank(parentDeptCode)) {
            return currentDept;
        }
        return findUpDeptInfo(parentDeptCode);
    }
}

