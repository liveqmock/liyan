/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.component.metadata.metamodel.service.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.innofi.component.metadata.commons.MetadataConstants;
import com.innofi.framework.pojo.metadata.MdDataView;
import com.innofi.framework.pojo.metadata.MdMetaModelAndDataViewTree;
import com.innofi.component.metadata.dataview.service.IMdDataViewService;
import com.innofi.framework.pojo.metadata.MdMetaModel;
import com.innofi.component.metadata.metamodel.service.IMdMetaModelService;
import com.innofi.framework.FrameworkConstants;
import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.PropertyOrder;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.framework.exception.BusinessRuntimeException;
import com.innofi.framework.service.impl.BaseServiceImpl;

import javax.annotation.Resource;

import com.innofi.framework.utils.validate.Validator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 功能/ 模块：元模型模块
 *
 * @author 张磊 Alex.zhang@innofi.com.cn
 * @version 2.0.0 12-11-30
 *          元模型Service接口
 *          修订历史：第1次修订
 *          日期          作者     参考    描述
 *          2013年5月30日 刘名寓   删除重复的方法  findMetaModelsById 用 BaseService中的 get替换
 *          北京名晟信息技术有限公司版权所有.
 */
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
@Service(value = "mdMetaModelService")
public class MdMetaModelServiceImpl extends BaseServiceImpl<MdMetaModel, String> implements IMdMetaModelService {

    @Resource(name="mdDataTitleTableMappingDaoSupport")
    protected DaoSupport daoSupport;
    @Resource
    private IMdDataViewService mdDataViewService;

    public DaoSupport getDaoSupport(){
        return daoSupport;
    }


    /**
     * 保存元模型时，添加对UI界面参数处理逻辑
     *
     * @param entity 具体实体对象
     */
    public void save(MdMetaModel entity) {
        super.save(entity);
        if (Validator.isNotEmpty(entity.getUiName())) {
            String addParamUiName = "";
            if (entity.getUiName().indexOf("?") > -1) {
                addParamUiName = entity.getUiName() + "&mdmId=" + entity.getId();
            } else {
                addParamUiName = entity.getUiName() + "?mdmId=" + entity.getId();
            }
            entity.setUiName(addParamUiName);
            getDaoSupport().getHibernateDao().update(entity);
        }
    }

    /**
     * 修改元模型时，添加对UI界面参数处理逻辑
     *
     * @param entity 具体实体对象
     */
    public void update(MdMetaModel entity) {
        if (Validator.isNotEmpty(entity.getUiName())) {
            String addParamUiName = "";
            if (entity.getUiName().indexOf("mdmId") == -1) {
                if (entity.getUiName().indexOf("?") > -1) {
                    addParamUiName = entity.getUiName() + "&mdmId=" + entity.getId();
                } else {
                    addParamUiName = entity.getUiName() + "?mdmId=" + entity.getId();
                }
            }
            entity.setUiName(addParamUiName);
            getDaoSupport().getHibernateDao().update(entity);
        }
        super.update(entity);
    }

    public List<MdMetaModel> findMetaModelsByParentId(String parentId) {
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
        addPropertyFilter(filters, "parentId", parentId, QueryConstants.EQUAL, true);
        addPropertyFilter(filters, "viewType", MetadataConstants.VIEW_TYPE_BUSINESS, QueryConstants.NOT_EQUAL, true);
        addPropertyFilter(filters, "status", FrameworkConstants.STATUS_EFFECTIVE, QueryConstants.EQUAL, true);
        orders.add(new PropertyOrder("mdmSeq", QueryConstants.ORDER_ASC));
        return findByPropertyFilters(filters, orders);
    }

    public List<MdMetaModel> findMetaModelsByIdList(List idList) {

        return findByProperty("id", idList, QueryConstants.IN);
    }


    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.metadata.metamodel.service.IMdMetaModelService#findRoots(Map)
     */
    public Collection<MdMetaModelAndDataViewTree> findRoots(Map parameter) {
        Collection<MdMetaModelAndDataViewTree> roots = new ArrayList<MdMetaModelAndDataViewTree>();
        if (parameter == null) {//如果参数为空.
            String hql = "from MdMetaModel mdMetaModel WHERE mdMetaModel.parentId = ? order by mdmSeq";
            List<MdMetaModel> rootsList = findByHql(hql, MetadataConstants.TREE_ROOT);//查询模型中的根节点.
            for (int i = 0; i < rootsList.size(); i++) {
                MdMetaModelAndDataViewTree rootTree = new MdMetaModelAndDataViewTree();
                rootTree.setMdmId(rootsList.get(i).getId());
                rootTree.setMdmName(rootsList.get(i).getMdmName());
                rootTree.setSavePath(rootsList.get(i).getSavePath());
                rootTree.setViewType(rootsList.get(i).getViewType());
                rootTree.setNodePic(rootsList.get(i).getNodePic());
                rootTree.setUiName(rootsList.get(i).getUiName());
                roots.add(rootTree); //创建虚拟根节点.
            }
        } else {
            String type = (String) parameter.get("type");
            String pid = (String) parameter.get("pid");
            if (!MetadataConstants.VIEW_TYPE_BUSINESS.equals(type)) {//如果是类型1.
                List<MdMetaModel> metaModels = findMetaModelsByParentId(pid);//通过父ID查找数据.
                for (MdMetaModel mdMetaModel : metaModels) {
                    MdMetaModelAndDataViewTree rootTree = new MdMetaModelAndDataViewTree();
                    rootTree.setMdmId(mdMetaModel.getId());
                    rootTree.setMdmName(mdMetaModel.getMdmName());
                    rootTree.setSavePath(mdMetaModel.getSavePath());
                    rootTree.setViewType(type);
                    rootTree.setNodePic(mdMetaModel.getNodePic());
                    rootTree.setUiName(mdMetaModel.getUiName());
                    //创建虚拟根节点.
                    roots.add(rootTree);
                }
            } else if (MetadataConstants.VIEW_TYPE_BUSINESS.equals(type)) {
                List<MdDataView> entities = mdDataViewService.findDataViewsByParentId(pid); //如果是业务视图,通过父ID查找数据.
                for (MdDataView mdDataView : entities) {
                    //创建一个MdMetaModelPojo对象.
                    MdMetaModel mdMetaModel = new MdMetaModel();
                    //通过业务视图中的mdmId查找到MdMetaModelPojo中的模型对象.
                    mdMetaModel = get(mdDataView.getMdmId());
                    MdMetaModelAndDataViewTree rootTree = new MdMetaModelAndDataViewTree();
                    rootTree.setMdmId(mdDataView.getId());                                    //保留业务试图的ID
                    rootTree.setMdmName(mdMetaModel.getMdmName());
                    rootTree.setSavePath(mdMetaModel.getSavePath());
                    rootTree.setViewType(type);
                    rootTree.setNodePic(mdMetaModel.getNodePic());
                    rootTree.setUiName(mdMetaModel.getUiName());
                    //创建虚拟根节点.
                    roots.add(rootTree);
                }
            }
        }
        return roots;
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.metadata.metamodel.service.IMdMetaModelService#findRelateForLookButton(String)
     */
    public String findRelateForLookButton(String mdmId) {
        String entityName = "";
        List<MdMetaModel> mdMetaModelList = new ArrayList<MdMetaModel>();
        //通过元模型ID获得元模型的数据.
        MdMetaModel mdMetaModel = get(mdmId);
        List<Map<String, Object>> mdMetaModelLists = getDaoSupport().getJdbcDao().queryForList("SELECT * FROM MD_METAMODEL MT,MD_TABLE_RELATE MTR WHERE  MT.SAVE_PATH = MTR.FOREIGN_TABLE_NAME AND  MT.PARENT_ID = '" + mdmId + "' AND MTR.MAIN_TABLE_NAME = '" + mdMetaModel.getSavePath() + "'");
        //String entity = ((String) mdMetaModelLists.get(0).get("MAIN_FIELD")).toLowerCase();
        if (mdMetaModelLists.size() == 0) {
            throw new BusinessRuntimeException(MessageFormat.format("在MD_TABLE_RELATE中未找主表名为{0}的主外键关系，请添加关联关系!", mdMetaModel.getSavePath()));
        }
        String fieldName = ((String) mdMetaModelLists.get(0).get("MAIN_FIELD"));
        String tableName = ((String) mdMetaModelLists.get(0).get("MAIN_TABLE_NAME"));

        String querySql = "SELECT ENTITY_ATTRI_NAME FROM MD_FIELD WHERE TABLE_NAME = '" + tableName + "' AND FIELD_NAME = '" + fieldName + "' AND STATUS = '" + FrameworkConstants.STATUS_EFFECTIVE + "'";

        List<Map<String, Object>> result = getDaoSupport().getJdbcDao().queryForList(querySql);
        String entity = (String) result.get(0).get("ENTITY_ATTRI_NAME");

        return entity;
    }

    @Override
    public String getCnFieldName() {
        return "mdmName";
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.metadata.metamodel.service.IMdMetaModelService#findMetaModelAndTableRelate(String, String)
     */
    public List<Map<String, Object>> findMetaModelAndTableRelate(String id, String savePath) {
    	String strSql = "SELECT DISTINCT MT.UI_NAME, MT.MDM_ID,MT.MDM_NAME,MTR.MAIN_TABLE_NAME,MTR.MAIN_FIELD,MTR.FOREIGN_TABLE_NAME,MTR.FOREIGN_FIELD FROM MD_METAMODEL MT,MD_TABLE_RELATE MTR " +
    			"WHERE  MT.SAVE_PATH = MTR.FOREIGN_TABLE_NAME AND  MT.PARENT_ID = '" + id + "' AND MTR.MAIN_TABLE_NAME = '" + savePath + "'";
        return getDaoSupport().getJdbcDao().queryForList(strSql);
    }
}
