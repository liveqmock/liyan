package com.innofi.component.rbac.privilage.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import com.innofi.component.rbac.privilage.pojo.DaTablePolicy;
import org.apache.commons.collections.map.ListOrderedMap;
import org.springframework.stereotype.Component;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.provider.Page;
import com.innofi.component.metadata.dimenfield.service.IMdDimenFieldService;
import com.innofi.component.rbac.privilage.pojo.DaDimenDataRestrict;
import com.innofi.component.rbac.privilage.service.IDaDimenDataRestrictService;
import com.innofi.component.rbac.privilage.service.IDaDimenDataService;
import com.innofi.component.codetransfer.IdfCodeTransfer;
import com.innofi.framework.action.impl.BaseActionImpl;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.framework.pojo.metadata.MdDimenField;
import com.innofi.framework.pojo.metadata.MdField;
import com.innofi.component.rbac.privilage.pojo.DaDimenData;
import com.innofi.framework.service.IBaseService;
import com.innofi.framework.spring.context.ContextHolder;
import com.innofi.framework.utils.string.StringUtil;
import com.innofi.framework.utils.validate.Validator;

/**
 * 功能/ 模块：todo
 *
 * @author todo 开发人员姓名 邮箱
 * @version todo 当前版本 日期 todo
 *          为dorado7界面维护操作的提供支持，实现DaDimenDataRestrict对象信息的加载与保存操作 修订历史： 日期 作者 参考
 *          描述 北京名晟信息技术有限公司版权所有.
 */
@Component
public class DaDimenDataRestrictAction extends BaseActionImpl {
    @Resource
    private IDaDimenDataRestrictService daDimenDataRestrictSevice;

    @Resource
    private IDaDimenDataService daDimenDataService;

    @Resource(name = "mdDimenFieldService")
    private IMdDimenFieldService mdDimenFieldService;

    public IBaseService getBusinessService() {
        return daDimenDataRestrictSevice;
    }

    @DataProvider
    public void findDaDimenDataRestricts(Page page,
                                         Map<String, Object> parameter) throws Exception {
        List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
        if (parameter != null) {
            addPropertyFilter(propertyFilters, "dimenDataId", parameter.get("dimenDataId"), (String) parameter.get("qMdimenDataId"), true);
            addPropertyFilter(propertyFilters, "id", parameter.get("id"), (String) parameter.get("qMid"), true);
            addPropertyFilter(propertyFilters, "restrictType", parameter.get("restrictType"), (String) parameter.get("qMrestrictType"), true);
            addPropertyFilter(propertyFilters, "tableAuthId", parameter.get("tableAuthId"), (String) parameter.get("qMtableAuthId"), true);
        }
        addPropertyFilter(propertyFilters, "parentRestrictId", 0, QueryConstants.EQUAL, true);
        com.innofi.framework.dao.pagination.Page innofiPage = super
                .translateDoradoPageToInnofiPage(page);
        daDimenDataRestrictSevice.findByPage_Filters(propertyFilters, null, innofiPage);
    }

    @DataProvider
    public Collection<DaDimenDataRestrict> findDaDimenDataRestrictsByTabelAuthId(Map<String, String> parameters) throws Exception {
        String tableAuthId = "";
        String parentRestrictId = "0";
        if (null != parameters) {
            tableAuthId = parameters.get("tableAuthId");
            String parentId = parameters.get("parentRestrictId");
            if (StringUtil.hasText(parentId)) {
                parentRestrictId = parentId;
            }
            if (Validator.isEmpty(tableAuthId)) {
                DaDimenDataRestrict daDimenDataRestrict = daDimenDataRestrictSevice.get(parentId);
                if (daDimenDataRestrict == null) {
                    return new ArrayList();
                } else {
                    tableAuthId = daDimenDataRestrict.getTableAuthId();
                }
            }
        }
        
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
                "                and t1.parent_Restrict_Id = ?" +
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
                "                and t1.parent_Restrict_Id = ?" +
                "                and (t1.restrict_type='1' or t1.restrict_type='2'))";



        return daDimenDataRestrictSevice.queryBeanForList(sql,tableAuthId,parentRestrictId,tableAuthId,parentRestrictId);
    }


    @DataResolver
    public void saveDaDimenDataRestricts(DaTablePolicy policy , Collection<DaDimenDataRestrict> objs , Map<String, String> parameter) {
        daDimenDataRestrictSevice.saveDaDimenDataRestricts(policy,objs,parameter);
        daDimenDataRestrictSevice.removeNotJoinData(policy.getId());
    }

    private void editDadimenDataFileName(
            Collection<DaDimenDataRestrict> daDimenDataRestrictList) {
        if (null != daDimenDataRestrictList
                && daDimenDataRestrictList.size() > 0) {
            for (DaDimenDataRestrict daDimenDataRestrict : daDimenDataRestrictList) {
                String typeString = daDimenDataRestrict.getRestrictType();
                if ("1".equals(typeString)) {
                    daDimenDataRestrict.setTreeIdFiledName("并且");
                } else if ("2".equals(typeString)) {
                    daDimenDataRestrict.setTreeIdFiledName("或者");
                } else {

                    //List<DaDimenData> daDimenDatas = daDimenDataService.findDimenDatasByDimenFieldId(daDimenDataRestrict.getDimenFieldId());
                    List<DaDimenData> daDimenDatas = null;
                    DaDimenData daDimenData = daDimenDatas.get(0);

                    List<MdDimenField> dimenFields = mdDimenFieldService.findByProperty("id", daDimenData.getDimenFieldId(), QueryConstants.EQUAL);

                    String field = dimenFields.get(0).getFieldId();

                    IdfCodeTransfer idfCodeTransfer = ContextHolder.getSpringBean("idfCodeTransfer");
                    ListOrderedMap fieldListOrderedMap = idfCodeTransfer.getCacheObjects("mdFieldService");
                    StringBuffer string = new StringBuffer();
                    MdField mdField = (MdField) fieldListOrderedMap.get(field);
                    string.append(mdField.getFieldCnName() + "[");

                    for (DaDimenData daDimenDataTemp : daDimenDatas) {
                        if (Validator.isEmpty(daDimenDataTemp.getDimenAuthType())) {
                            string.append(daDimenDataTemp.getDimenValue() + ",");
                        } else {
                            string.append(daDimenDataTemp.getDimenValue() + "|" + daDimenDataTemp.getDimenAuthType() + ",");
                        }
                    }
                    String treeIdFieldName = string.substring(0, string.length() - 1) + "]";
                    daDimenDataRestrict.setTreeIdFiledName(treeIdFieldName);
                }
            }
        }
    }

    @Expose
    public String getPrimaryKeyByUUID() {
        return UUID.randomUUID().toString();
    }
}
