package com.innofi.component.rbac.privilage.service.impl;

import java.text.SimpleDateFormat;
import com.innofi.framework.service.impl.BaseServiceImpl;
import java.util.*;

import javax.annotation.Resource;

import com.innofi.component.rbac.privilage.pojo.DaDimenDataRestrict;
import com.innofi.component.rbac.privilage.pojo.DaDimenDataRestrictRelated;
import com.innofi.component.rbac.privilage.service.*;
import com.innofi.framework.utils.validate.Validator;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.innofi.component.rbac.RBACConstants;
import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.framework.pojo.metadata.MdDimenField;
import com.innofi.framework.pojo.metadata.MdDimension;
import com.innofi.component.rbac.privilage.pojo.DaDimenControl;
import com.innofi.component.rbac.privilage.pojo.DaDimenData;
import com.innofi.component.rbac.privilage.pojo.DaTablePolicy;
import com.innofi.component.rbac.privilage.pojo.SysAuthorize;
import com.innofi.component.rbac.role.pojo.SysRole;
import com.innofi.framework.spring.context.ContextHolder;

/**
 * 功能/ 模块：权限模块
 *
 * @author @author liumy2009@126.com
 * @version 2.0
 *          数据访问策略服务实现类
 * @found date: 2010-11-23
 * @found time: 20:40:56
 */
@Component(value = "daTablePolicyService")
public class DaTablePolicyServiceImpl extends BaseServiceImpl<DaTablePolicy, String> implements IDaTablePolicyService {

    @Resource(name="mdDataTitleTableMappingDaoSupport")
    protected DaoSupport daoSupport;

    public DaoSupport getDaoSupport() {
        return daoSupport;
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.rbac.privilage.service.IDaTablePolicyService#findPoliciesByUserRoles(java.util.List)
     */
    public List<DaTablePolicy> findPoliciesByUserRoles(List<SysRole> userRoles) {
        if (userRoles != null && userRoles.size() > 0) {
            List<String> roleIds = new ArrayList<String>();
            Map<String, List> paramMap = new HashMap<String, List>();
            for (SysRole role : userRoles) {
                roleIds.add(role.getId());
            }
            paramMap.put("roleIds", roleIds);
            String hql = "select distinct f from SysAuthorize a,DaTablePolicy f where a.resourceId=f.id and a.resourceType='"
                    + RBACConstants.RESOURCE_TYPE_TABLE
                    + "' and a.roleId in (:roleIds)";

            return this.findByNamedHql(hql, paramMap);
        } else {
            return new ArrayList();
        }
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.rbac.privilage.service.IDaTablePolicyService#findPoliciesByUserRolesAndTable(java.util.List, java.util.Set)
     */
    public List<DaTablePolicy> findPoliciesByUserRolesAndTable(List<SysRole> userRoles, Set<String> tableNames) {
        if (userRoles != null && userRoles.size() > 0) {
            Set<String> roleIds = new HashSet<String>();
            Map<String, Set> paramMap = new HashMap<String, Set>();
            for (SysRole role : userRoles) {
                roleIds.add(role.getId());
            }
            paramMap.put("roleIds", roleIds);

            paramMap.put("tableName", tableNames);
            String hql = "select distinct f from SysAuthorize a,DaTablePolicy f where a.resourceId=f.id and a.resourceType='"
                    + RBACConstants.RESOURCE_TYPE_TABLE
                    + "' and a.roleId in (:roleIds) and f.tableName in (:tableName)";
            return getDaoSupport().getHibernateDao().findByNamedHql(DaTablePolicy.class, hql, paramMap);//这里不要调用父类方法，防止策略过滤
        } else {
            return new ArrayList();
        }
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.rbac.privilage.service.IDaTablePolicyService#findPoliciesByUserRolesAndTable(java.util.List, java.util.Set)
     */
    public List<DaTablePolicy> findPoliciesByUserRolesOpenTypeAndTable(List<SysRole> userRoles, String operType, Set<String> tableNames) {
        if (userRoles != null && userRoles.size() > 0) {
            Set<String> roleIds = new HashSet<String>();
            Map<String, Set> paramMap = new HashMap<String, Set>();
            for (SysRole role : userRoles) {
                roleIds.add(role.getId());
            }
            paramMap.put("roleIds", roleIds);

            paramMap.put("tableName", tableNames);
            String hql = "select distinct f from SysAuthorize a,DaTablePolicy f where a.resourceId=f.id and a.resourceType='"
                    + RBACConstants.RESOURCE_TYPE_TABLE
                    + "' and a.roleId in (:roleIds) and f.tableName in (:tableName) and f.operType in (" + operType + ")";
            return getDaoSupport().getHibernateDao().findByNamedHql(DaTablePolicy.class, hql, paramMap);//这里不要调用父类方法，防止策略过滤
        } else {
            return new ArrayList();
        }
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.rbac.privilage.service.IDaTablePolicyService#findDaTablePoliciesForPrivilege(String)
     */
    public List<DaTablePolicy> findDaTablePoliciesForPrivilege(String roleId) {
        String sql = "select distinct T2.table_desc ,t1.* from DA_TABLE_POLICY t1,MD_TABLE T2 WHERE T1.TABLE_NAME = T2.TABLE_NAME and t2.status = '1' and t2.is_da_control = '1' order by t1.table_name";
        List<DaTablePolicy> policys = this.queryBeanForList(sql, null);

        ISysAuthorizeService sysAuthorizeService = ContextHolder
                .getSpringBean("sysAuthorizeService");
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        this.addPropertyFilter(filters, "roleId", roleId, QueryConstants.EQUAL,
                true);
        this.addPropertyFilter(filters, "resourceType",
                RBACConstants.RESOURCE_TYPE_TABLE, QueryConstants.EQUAL, true);
        List<SysAuthorize> authorizes = sysAuthorizeService
                .findByPropertyFilters(filters, null);
        if (authorizes.size() > 0) {
            Set<String> set = new HashSet<String>();
            for (SysAuthorize authorize : authorizes) {
                set.add(authorize.getResourceId());
            }
            for (DaTablePolicy policy : policys) {
                if (set.contains(policy.getId())) {
                    policy.setSelectFlag(true);
                }
            }
        }
        return policys;
    }


    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.rbac.privilage.service.IDaTablePolicyService#deleteDaTablePolicy(DaTablePolicy)
     */
    public void deleteDaTablePolicy(DaTablePolicy policy) {
        ISysAuthorizeService sysAuthorizeService = ContextHolder
                .getSpringBean("sysAuthorizeService");
        IDaDimenControlService daDimenControlService = ContextHolder
                .getSpringBean("daDimenControlService");
        IDaDimenDataService daDimenDataService = ContextHolder
                .getSpringBean("daDimenDataService");
        // 删除已分配角色的策略数据
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        this.addPropertyFilter(filters, "resourceId", policy.getId(),
                QueryConstants.EQUAL, true);
        this.addPropertyFilter(filters, "resourceType",
                RBACConstants.RESOURCE_TYPE_TABLE, QueryConstants.EQUAL, true);
        sysAuthorizeService.deleteByPropertyFilters(filters);
        // 删除维度控制 维度数据
        filters.clear();
        this.addPropertyFilter(filters, "tableAuthId", policy.getId(),
                QueryConstants.EQUAL, true);
        List<DaDimenControl> controls = daDimenControlService
                .findByPropertyFilters(filters, null);
        for (DaDimenControl control : controls) {
            filters.clear();
            this.addPropertyFilter(filters, "dimenControlId", control.getId(),
                    QueryConstants.EQUAL, true);
            daDimenDataService.deleteByPropertyFilters(filters);
            daDimenControlService.delete(control);
        }
        // 删除控制策略
        this.delete(policy);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.rbac.privilage.service.IDaTablePolicyService#saveDaTablePoliciesWithDimens(com.innofi.component.rbac.privilage.pojo.DaTablePolicy, java.util.List)
     */
    public String saveDaTablePoliciesWithDimens(DaTablePolicy policy, List<MdDimension> dimens) {
        IDaDimenControlService daDimenControlService = ContextHolder.getSpringBean("daDimenControlService");
        IDaDimenDataService daDimenDataService = ContextHolder.getSpringBean("daDimenDataService");


        EntityState state = EntityUtils.getState(policy);
        if (EntityState.DELETED.equals(state)) {
            delete(policy);
        } else if (EntityState.MODIFIED.equals(state) || EntityState.MOVED.equals(state)) {
            update(policy);
        } else if (EntityState.NEW.equals(state)) {
            save(policy);
        }
        //List<DaTablePolicy> policyList = this.setFbPoliceFromzbPolice(policy, dimens);

        for (MdDimension dimen : dimens) {
            EntityState dimenState = EntityUtils.getState(dimen);
            if (EntityState.MODIFIED.equals(dimenState) && dimen.getSelectFlag() == false) {//取消维度
                //删除维度数据
                daDimenControlService.deleteControlData(policy.getId(), dimen.getId());

            } else {
                Collection<MdDimenField> fields = EntityUtils.getValue(dimen,
                        "fields");
                if (fields != null) {
                    String dimenControlId = null;
                    for (MdDimenField field : fields) {
                        // EntityState fieldState = EntityUtils.getState(field);
                        if (dimenControlId == null) {
                            dimenControlId = field.getDimenControlId();
                        }
                        Collection<DaDimenData> dimenDatas = EntityUtils.getValue(field, "dimenData");
                        if (!StringUtils.isBlank(dimenControlId)
                                && field.getSelectFlag() == false) {// 取消维度字段
                            // 删除维度数据
                            daDimenDataService.deleteDimenData(dimenControlId, field.getId());
                        } else {
                            if (field.getSelectFlag() == true
                                    && dimenDatas != null) {
                                if (StringUtils.isBlank(dimenControlId)) {// 添加维度控制数据

                                    DaDimenControl control = new DaDimenControl();
                                    control.setDimenId(field.getDimenId());
                                    control.setTableAuthId(policy.getId());
                                    daDimenControlService.save(control);
                                    dimenControlId = control.getId();

                                }
                                for (DaDimenData dimenData : dimenDatas) {
                                    EntityState dataState = EntityUtils
                                            .getState(dimenData);
                                    if (EntityState.NEW.equals(dataState)) {
                                        dimenData.setDimenControlId(dimenControlId);
                                        dimenData.setDimenFieldId(field.getId());
                                        dimenData.setDimenType(dimen.getDimenType());
                                    }
                                    daDimenDataService.saveDaDimenData(dimenData);
                                }
                            }
                        }
                    }
                    if (dimenControlId != null) {
                        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
                        this.addPropertyFilter(filters, "dimenControlId",
                                dimenControlId, QueryConstants.EQUAL, true);
                        List<DaDimenData> datas = daDimenDataService
                                .findByPropertyFilters(filters, null);
                        if (datas.size() == 0) {
                            daDimenControlService.delete(dimenControlId);
                        }
                    }
                }
            }
        }
        return policy.getId();

    }


    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.rbac.privilage.service.IDaTablePolicyService#saveDaTablePolicyWithRestricts(com.innofi.component.rbac.privilage.pojo.DaTablePolicy, java.util.List)
     */
    public void saveDaTablePolicyWithRestricts(DaTablePolicy policy, List<DaDimenDataRestrict> dataRestricts) {
        IDaDimenDataRestrictService daDimenDataRestrictService = ContextHolder.getSpringBean("daDimenDataRestrictService");
        IDaDimenDataRestrictRelatedService daDimenDataRestrictRelatedService = ContextHolder.getSpringBean("daDimenDataRestrictRelatedService");
        IDaDimenDataService daDimenDataService = ContextHolder.getSpringBean("daDimenDataService");
        IDaDimenControlService daDimenControlService = ContextHolder.getSpringBean("daDimenControlService");

        //首先保存方案
        EntityState state = EntityUtils.getState(policy);
        if (EntityState.DELETED.equals(state)) {
            delete(policy);
        } else if (EntityState.MODIFIED.equals(state) || EntityState.MOVED.equals(state)) {
            update(policy);
        } else if (EntityState.NEW.equals(state)) {
            save(policy);
        }
        String tableAuthId = policy.getId();


        for (DaDimenDataRestrict daDimenDataRestrict : dataRestricts) {
            daDimenDataRestrict.setTableAuthId(tableAuthId);
            List<DaDimenData> datas = EntityUtils.getValue(daDimenDataRestrict, "dimenData");
            DaDimenControl daDimenControl = null;

            if (RBACConstants.RESTRICT_TYPE_CONDITION.equals(daDimenDataRestrict.getRestrictType())) {
                //保存维度控制信息
                String dimenId = daDimenDataRestrict.getDimenId();

                daDimenControl = daDimenControlService.findUniqueByHql("from DaDimenControl where dimenId=? and tableAuthId = ?", dimenId, tableAuthId);
                if (daDimenControl == null) {
                    daDimenControl = new DaDimenControl();
                    daDimenControl.setDimenId(dimenId);
                    daDimenControl.setTableAuthId(policy.getId());
                    daDimenControlService.save(daDimenControl);
                }
            }

            state = EntityUtils.getState(daDimenDataRestrict);
            if (EntityState.DELETED.equals(state)) {
                if (datas != null) {
                    saveDaDimenData(datas, daDimenControl, daDimenDataRestrict);
                } else {
                    List<Map<String, Object>> dataIds = getDaoSupport().getJdbcDao().queryForList("select dimen_data_Id from DA_DIMEN_DATA_RESTRICT_RELATED where restrict_Id = '" + daDimenDataRestrict.getId() + "'");
                    StringBuffer dataIdBuffer = new StringBuffer();
                    for (int i = 0; i < dataIds.size(); i++) {
                        Map<String, Object> idRecord = dataIds.get(i);
                        dataIdBuffer.append("'" + idRecord.get("DIMEN_DATA_ID").toString() + "',");
                    }
                    getDaoSupport().getJdbcDao().execute("delete from DA_DIMEN_DATA_RESTRICT_RELATED where restrict_Id = '" + daDimenDataRestrict.getId() + "'");
                    if (dataIdBuffer.length() > 0) {
                        getDaoSupport().getJdbcDao().execute("delete from DA_DIMEN_DATA where DIMEN_DATA_ID in (" + dataIdBuffer.toString().substring(0, dataIdBuffer.length() - 1) + ")");
                    }

                }
                daDimenDataRestrictService.delete(daDimenDataRestrict);
            } else if (EntityState.MODIFIED.equals(state) || EntityState.MOVED.equals(state)) {
                String sql = "select distinct * from (select t1.* , tt.DIMEN_ID,tt.DIMEN_CN_NAME,tt.DIMEN_TYPE," +
                        "                tt.DIMEN_FIELD_ID," +
                        "                tt.FIELD_CN_NAME," +
                        "                tt.DIMEN_CONTROL_ID  from DA_DIMEN_DATA_RESTRICT t1  " +
                        "                ,DA_DIMEN_DATA_RESTRICT_RELATED t2 " +
                        "                ,(select t3.dimen_data_id , t6.DIMEN_ID,t6.DIMEN_NAME DIMEN_CN_NAME,t6.DIMEN_TYPE," +
                        "                t5.DIMEN_FIELD_ID," +
                        "                t7.FIELD_CN_NAME," +
                        "                t4.DIMEN_CONTROL_ID from DA_DIMEN_DATA t3 ,DA_DIMEN_CONTROL t4 ,MD_DIMEN_FIELD t5 ,MD_DIMENSION t6 , MD_FIELD t7" +
                        "                where  t4.dimen_control_id = t3.dimen_control_id" +
                        "                and t3.dimen_field_id = t5.dimen_field_id " +
                        "                and t6.dimen_id = t4.dimen_id " +
                        "                and t7.field_id = t5.field_id) tt  " +
                        "                where" +
                        "                t1.table_auth_id=? " +
                        "                and t1.restrict_Id = ?" +
                        "                and t1.restrict_id = t2.restrict_id" +
                        "                and t2.dimen_data_id = tt.dimen_data_id" +
                        "                and t1.restrict_type='3') union select distinct * from (select t1.* , tt.DIMEN_ID,tt.DIMEN_CN_NAME,tt.DIMEN_TYPE," +
                        "                tt.DIMEN_FIELD_ID," +
                        "                tt.FIELD_CN_NAME," +
                        "                tt.DIMEN_CONTROL_ID  from DA_DIMEN_DATA_RESTRICT t1  " +
                        "                left join DA_DIMEN_DATA_RESTRICT_RELATED t2 on t1.restrict_id = t2.restrict_id " +
                        "                left join (select t3.dimen_data_id , t6.DIMEN_ID,t6.DIMEN_NAME DIMEN_CN_NAME,t6.DIMEN_TYPE," +
                        "                t5.DIMEN_FIELD_ID," +
                        "                t7.FIELD_CN_NAME," +
                        "                t4.DIMEN_CONTROL_ID from DA_DIMEN_DATA t3 ,DA_DIMEN_CONTROL t4 ,MD_DIMEN_FIELD t5 ,MD_DIMENSION t6 , MD_FIELD t7" +
                        "                where  t4.dimen_control_id = t3.dimen_control_id" +
                        "                and t3.dimen_field_id = t5.dimen_field_id " +
                        "                and t6.dimen_id = t4.dimen_id " +
                        "                and t7.field_id = t5.field_id) tt  on t2.dimen_data_id = tt.dimen_data_id" +
                        "                where" +
                        "                t1.table_auth_id=? " +
                        "                and t1.restrict_Id = ?" +
                        "                and (t1.restrict_type='1' or t1.restrict_type='2'))";

                DaDimenDataRestrict oldDaDimenDataRestrict = daDimenDataRestrictService.queryBeanForUnique(sql, tableAuthId, daDimenDataRestrict.getId(), tableAuthId, daDimenDataRestrict.getId());
                if (oldDaDimenDataRestrict.getDimenType() != null && oldDaDimenDataRestrict.getDimenFieldId() != null && (!oldDaDimenDataRestrict.getDimenType().equals(daDimenDataRestrict.getDimenType())
                        || !oldDaDimenDataRestrict.getDimenFieldId().equals(daDimenDataRestrict.getDimenFieldId()))) {
                    List<DaDimenDataRestrictRelated> relateds = daDimenDataRestrictRelatedService.findByProperty("restrictId", daDimenDataRestrict.getId(), QueryConstants.EQUAL);
                    List<Object> dataIds = daDimenDataRestrictRelatedService.getPropertyValueList(relateds, "dimenDataId");
                    daDimenDataService.deleteByPropertyFilters(buildPropertyFilter("id", dataIds, QueryConstants.IN));
                    List<DaDimenData> otherDatas = daDimenDataService.findByProperty("dimenControlId", oldDaDimenDataRestrict.getDimenContorlId(), QueryConstants.EQUAL);
                    if (null == otherDatas || otherDatas.size() == 0) {
                        daDimenControlService.deleteByPropertyFilters(buildPropertyFilter("id", oldDaDimenDataRestrict.getDimenContorlId(), QueryConstants.EQUAL));
                    }
                }
                daDimenDataRestrictService.update(daDimenDataRestrict);
                saveDaDimenData(datas, daDimenControl, daDimenDataRestrict);
            } else if (EntityState.NEW.equals(state)) {
                daDimenDataRestrictService.save(daDimenDataRestrict);
                saveDaDimenData(datas, daDimenControl, daDimenDataRestrict);
            } else {
                saveDaDimenData(datas, daDimenControl, daDimenDataRestrict);
            }


            List<DaDimenDataRestrict> children = EntityUtils.getValue(daDimenDataRestrict, "children");
            if (children != null) {
                saveDaTablePolicyWithRestricts(policy, children);
            }
        }
    }


    private void saveDaDimenData(List<DaDimenData> datas, DaDimenControl daDimenControl, DaDimenDataRestrict daDimenDataRestrict) {
        if (datas != null) {
            IDaDimenDataRestrictRelatedService daDimenDataRestrictRelatedService = ContextHolder.getSpringBean("daDimenDataRestrictRelatedService");
            IDaDimenDataService daDimenDataService = ContextHolder.getSpringBean("daDimenDataService");
            IDaDimenControlService daDimenControlService = ContextHolder.getSpringBean("daDimenControlService");
            //保存授权数据
            for (DaDimenData daDimenData : datas) {

                EntityState state = EntityUtils.getState(daDimenData);
                if (EntityState.DELETED.equals(state)) {
                    daDimenDataRestrictRelatedService.deleteByPropertyFilters(buildPropertyFilter("dimenDataId", daDimenData.getId(), QueryConstants.EQUAL));
                    daDimenDataService.delete(daDimenData);
                } else if (EntityState.MODIFIED.equals(state) || EntityState.MOVED.equals(state)) {
                    daDimenData.setDimenControlId(daDimenControl.getId());
                    daDimenData.setDimenFieldId(daDimenDataRestrict.getDimenFieldId());
                    daDimenData.setDimenType(daDimenDataRestrict.getDimenType());
                    if ("2".equals(daDimenData.getDimenType())) {//时间
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        daDimenData.setDimenValue(df.format(daDimenData.getStartDate()) + "|" + df.format(daDimenData.getEndDate()));
                    }
                    daDimenDataService.update(daDimenData);
                } else if (EntityState.NEW.equals(state)) {
                    daDimenData.setDimenControlId(daDimenControl.getId());
                    daDimenData.setDimenFieldId(daDimenDataRestrict.getDimenFieldId());
                    daDimenData.setDimenType(daDimenDataRestrict.getDimenType());
                    if ("2".equals(daDimenData.getDimenType())) {//时间
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        if (Validator.isNotNull(daDimenData.getStartDate()) && Validator.isNotNull(daDimenData.getEndDate())) {
                            daDimenData.setDimenValue(df.format(daDimenData.getStartDate()) + "|" + df.format(daDimenData.getEndDate()));
                        }
                    }
                    daDimenDataService.save(daDimenData);
                    DaDimenDataRestrictRelated daDimenDataRestrictRelated = new DaDimenDataRestrictRelated();
                    daDimenDataRestrictRelated.setDimenDataId(daDimenData.getId());
                    daDimenDataRestrictRelated.setRestrictId(daDimenDataRestrict.getId());
                    daDimenDataRestrictRelatedService.save(daDimenDataRestrictRelated);
                }
            }
        }
    }

}
