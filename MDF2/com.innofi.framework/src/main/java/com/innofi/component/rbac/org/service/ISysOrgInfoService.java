package com.innofi.component.rbac.org.service;

import java.util.List;
import java.util.Map;

import com.innofi.framework.service.IBaseService;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.pagination.Page;
import com.innofi.component.rbac.org.pojo.SysOrgInfo;

public interface ISysOrgInfoService extends IBaseService<SysOrgInfo, String> {


    /**
     * 根据机构编号获取机构对象，不加权限控制
     *
     *
     * @param orgCode 机构编号
     * @return 机构编号对应的对象
     */
    public SysOrgInfo getSysOrgInfoByOrgCode(String orgCode);

    /**
     * 根据指定机构编号检查机构是否存在 true存在，false不存在
     *
     * @param orgCode 机构编号
     * @return true存在，false不存在
     */
    public boolean checkObjectExist(String orgCode);

    /**
     * 根据指定查询条件查找机构列表
     *
     * @param searchType 类型为tree时 根据orgSeq查询Like
     * @param orgCode    机构编号
     * @param filters    查询条件
     * @param page       分页
     */
    public void findSysOrgInfoList(String searchType, String orgCode, List<PropertyFilter> filters, Page page);

    /**
     * 查询所有机构信息 返回Map
     *
     * @return key orgCode , value SysOrgInfo
     */
    public Map<String, SysOrgInfo> getSysOrgInfoMaps();

    /**
     * 机构停用校验
     *
     * @param orgCode
     * @return
     */
    public boolean invalidOrg(String orgCode);

    /**
     * 根据机构Code递归查找该机构下设的所有机构及部门
     *
     * @param orgCode 机构编号
     * @return 该机构下设的所有机构及部门
     */
    public List<SysOrgInfo> findChildrenByOrgCode(String orgCode);

    /**
     * 部门停用校验
     *
     * @param orgCode
     * @return
     */
    public String invalidDep(String orgCode);

    /**
     * 通过机构节点找到节点信息
     */
    public SysOrgInfo getOrgInfoByOrgCode(String orgCode);

    /**
     * 通过父节点找到子节点信息
     */
    public List<SysOrgInfo> getOrgInfosByParentOrgCode(String parentOrgCode);

    /**
     * U：所有上级节点*
     */
    public List<SysOrgInfo> getUpLevelOrgsByOrgCode(String orgCode);

    /**
     * D：所有下级节点 *
     */
    public List<SysOrgInfo> getDownLevelOrgsByOrgCode(String orgCode);

    /**
     * V：垂直所有节点 *
     */
    public List<SysOrgInfo> getVerticalLevelOrgsByOrgCode(String orgCode);

    /**
     * H: 水平所有节点 *
     */
    public List<SysOrgInfo> getSameLevelOrgsOrgCode(String orgCode);
    /**O: 其它实际值
     * @param orgName2 **/


    //public List<SysOrgInfo> findSysVOrgInfos(String parentOrgId,String orgCode, String orgName);

    /**
     * getSysOwnOrgInfoByDeptCode(这里用一句话描述这个方法的作用)
     *
     * @param name
     * @param @return 设定文件
     * @Exception 异常对象
     * @since CodingExample　Ver(编码范例查看) 1.1
     */
    public SysOrgInfo getSysOwnOrgInfoByDeptCode(String deptCode);

    /**
     * NEW 1
     * 根据节点获取本级的机构以及机构的所属部门
     * getOwnDeptInfoByOrgCode(这里用一句话描述这个方法的作用)
     *
     * @return String    DOM对象
     * @since CodingExample　Ver(编码范例查看) 1.1
     */
    public List<SysOrgInfo> getOwnDeptInfoByOrgCode(String orgCode);

    /**
     * NEW2
     * 根据节点去本下级机构节点
     * getDownLevelOrgsByCategoryOrgCode(这里用一句话描述这个方法的作用)
     *
     * @param name
     * @param @return 设定文件
     * @return String    DOM对象
     * @Exception 异常对象
     * @since CodingExample　Ver(编码范例查看) 1.1
     */
    public List<SysOrgInfo> getDownLevelCategoryOrgsByOrgCode(String orgCode);

    /**
     * 受权限控制的迭代展开
     * findSysOrgInfoTreeByParentCodeForPolicy(这里用一句话描述这个方法的作用)
     *
     * @param name
     * @param @return 设定文件
     * @return String    DOM对象
     * @Exception 异常对象
     * @since CodingExample　Ver(编码范例查看) 1.1
     */
    public List<SysOrgInfo> findSysOrgInfoTreeForPolicy(List<PropertyFilter> filters);

    /**
     * 不受权限所有树展开
     * findSysOrgInfoAllTree(这里用一句话描述这个方法的作用)
     *
     * @param name
     * @param @return 设定文件
     * @return String    DOM对象
     * @Exception 异常对象
     * @since CodingExample　Ver(编码范例查看) 1.1
     */
    public List<SysOrgInfo> findSysOrgInfoAllTree(List<PropertyFilter> filters);

    /**
     * findRolesStringByOrgCodes(这里用一句话描述这个方法的作用)
     *
     * @param name
     * @param @return 设定文件
     * @return String    DOM对象
     * @since CodingExample　Ver(编码范例查看) 1.1
     */
    public Map<String, StringBuffer> findRolesStringByOrgCodes(List<String> orgCodeList);

    /**
     * 根据角色ID查询未设置的部门
     *
     * @param roleId
     * @return
     */
    List<SysOrgInfo> findSysOrgInfoNoSetByRoleId(String roleId);

    /**
     * 根据角色ID查询已设置的部门
     *
     * @param roleId
     * @return
     */
    List<SysOrgInfo> findSysOrgInfoSetByRoleId(String roleId);

    /**
     * 查找顶级部门信息。。
     *
     * @param deptCode
     * @return
     */
    public SysOrgInfo findUpDeptInfo(String deptCode);
}