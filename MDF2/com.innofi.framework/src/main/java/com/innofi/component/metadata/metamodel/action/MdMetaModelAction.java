/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.component.metadata.metamodel.action;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;
import com.innofi.component.metadata.commons.MetadataConstants;
import com.innofi.component.metadata.dataview.service.IMdDataViewService;
import com.innofi.framework.pojo.metadata.MdDataView;
import com.innofi.framework.pojo.metadata.MdMetaModel;
import com.innofi.framework.pojo.metadata.MdMetaModelAndDataViewTree;
import com.innofi.component.metadata.metamodel.service.IMdMetaModelService;
import com.innofi.component.metadata.tablerelate.service.IMdTableRelateService;
import com.innofi.framework.FrameworkConstants;
import com.innofi.component.codetransfer.IdfCodeTransfer;
import com.innofi.framework.action.impl.BaseActionImpl;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.PropertyOrder;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.framework.spring.context.ContextHolder;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 * 功能/ 模块：元模型模块
 *
 * @author 张磊  Alex.zhang@innofi.com.cn
 * @version 2.0.0 12-11-30
 *          元模型Action实现类
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
@Component
public class MdMetaModelAction extends BaseActionImpl {
    @Resource
    private IMdMetaModelService mdMetaModelServcie;
    @Resource
    private IMdTableRelateService iMdTableRelateService;
    @Resource
    private IMdDataViewService mdDataViewService;

    /**
     * @param page
     * @param parameter
     * @throws Exception
     */
    @DataProvider
    public void findMdMetaModels(Page page, Map<String, Object> parameter) throws Exception {


        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
        List<String> mdmIdsList = new ArrayList<String>();
        if (parameter != null) {
            addPropertyFilter(filters, "parentId", parameter.get("parentId"), QueryConstants.EQUAL, true);
            addPropertyFilter(filters, "mdmCode", parameter.get("mdmCode"), QueryConstants.LIKE, true);
            addPropertyFilter(filters, "mdmName", parameter.get("mdmName"), QueryConstants.LIKE, true);
            addPropertyFilter(filters, "status", FrameworkConstants.STATUS_EFFECTIVE, QueryConstants.EQUAL, true);
            //维护元模型依赖维护的方法.
            if (parameter.get("mdmIds") != null) {
                mdmIdsList = java.util.Arrays.asList(parameter.get("mdmIds").toString().split(","));
                if (mdmIdsList.size() > 0) {
                    List<String> list = new ArrayList<String>();
                    list.add(MetadataConstants.VIEW_TYPE_BUSINESS);
                    list.add(MetadataConstants.VIEW_TYPE_OTHER);
                    addPropertyFilter(filters, "id", mdmIdsList, QueryConstants.NOT_IN, true);
                    addPropertyFilter(filters, "viewType", list, QueryConstants.NOT_IN, true);
                }
            }

            if (parameter.get("busiManage") != null) { //业务视图维护需要的查询.查询有依赖元模型的.并且元模型类型为1的.
                addPropertyFilter(filters, "viewType", FrameworkConstants.STATUS_EFFECTIVE, QueryConstants.EQUAL, true);
            } else if (parameter.get("savePaths") != null) {
                if (!"undefined".equals(parameter.get("savePaths"))) {
                    List<String> savePathList = new ArrayList<String>();
                    savePathList = java.util.Arrays.asList(parameter.get("savePaths").toString().split(","));
                    if (savePathList.size() > 0) {
                        addPropertyFilter(filters, "savePath", savePathList, QueryConstants.IN, true);
                        addPropertyFilter(filters, "viewType", MetadataConstants.VIEW_TYPE_CWM, QueryConstants.EQUAL, true);
                    }
                } else {
                    addPropertyFilter(filters, "viewType", MetadataConstants.VIEW_TYPE_CWM, QueryConstants.EQUAL, true);
                }
            } else {
                addPropertyFilter(filters, "viewType", MetadataConstants.VIEW_TYPE_BUSINESS, QueryConstants.NOT_EQUAL, true);
            }

        }

        orders.add(new PropertyOrder("mdmSeq", QueryConstants.ORDER_ASC));
        com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
        mdMetaModelServcie.findByPage_Filters(filters, orders, innofiPage);
        transferCn(innofiPage);
    }

    /**
     * 通过父ID查询数据信息
     *
     * @param parentId
     * @return
     * @throws Exception
     */
    @DataProvider
    public List<MdMetaModel> findMetaModelsByParentId(String parentId) throws Exception {
        if (StringUtils.isBlank(parentId)) {
            parentId = MetadataConstants.TREE_ROOT;
        }
        List<MdMetaModel> metaModels = mdMetaModelServcie.findMetaModelsByParentId(parentId);
        return metaModels;
    }


    /**
     * 通过id查询对应的记录信息
     *
     * @param id
     * @return
     * @throws Exception
     */
    @DataProvider
    public List<MdMetaModel> findMetaModelsById(String id) throws Exception {
        List<MdMetaModel> metaModels = new ArrayList<MdMetaModel>();
        metaModels.add(mdMetaModelServcie.get(id));
        return metaModels;
    }

    @DataResolver
    public void saveMdMetaModels(Collection<MdMetaModel> objs) {

        for (Iterator iterator = objs.iterator(); iterator.hasNext(); ) {
            MdMetaModel mdMetaModel = (MdMetaModel) iterator.next();
            EntityState state = EntityUtils.getState(mdMetaModel);
            if (EntityState.NEW.equals(state)) {
                mdMetaModelServcie.save(mdMetaModel);
            } else if (EntityState.MODIFIED.equals(state)) {
                mdMetaModelServcie.update(mdMetaModel);
            } else if (EntityState.DELETED.equals(state)) {
                mdMetaModelServcie.delete(mdMetaModel);
            }
        }
    }

    /**
     * 查找是否存在上下级关系
     *
     * @param parameter 元模型上级表名(表关系表中的主表)和新增下级表名(表关系表中的从表)
     * @return
     */
    @Expose
    public String checkMetaModelRelate(Map<String, Object> parameter) {
        String upMdmId = parameter.get("upMdmId").toString();
        String foreignTableName = parameter.get("foreignTableName").toString();
        MdMetaModel mdMetaModel = null;
        try {
            mdMetaModel = findMetaModelsById(upMdmId).get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mdMetaModel.getViewType() == "3") {
            //如果是中间节点数据,直接返回成功.
            return MetadataConstants.SUCCESS;
        } else {
            return iMdTableRelateService.checkMetaModelRelate(mdMetaModel.getSavePath(), foreignTableName);
        }
    }

    /**
     * 组装树
     *
     * @param parameter
     * @return
     */
    @DataProvider
    public Collection<MdMetaModelAndDataViewTree> findRoots(Map parameter) {
        return mdMetaModelServcie.findRoots(parameter);
    }

    /**
     * 通过父ID查询数据信息
     *
     * @param parameter
     * @return
     * @throws Exception
     */
    @DataProvider
    public List<MdMetaModelAndDataViewTree> findMetaModelAdnDataViewByParentId(Map<String, Object> parameter) {
        String parentId = "";
        String viewType = "";
        if (parameter != null) {
            if (parameter.get("parentId") != null)
                parentId = parameter.get("parentId").toString();
            if (parameter.get("viewType") != null)
                viewType = parameter.get("viewType").toString();
        }

        List<MdMetaModelAndDataViewTree> roots = new ArrayList<MdMetaModelAndDataViewTree>();
        if (StringUtils.isBlank(parentId)) {
            parentId = MetadataConstants.TREE_ROOT;
        }
        if (viewType.equals("2")) {
            List<MdDataView> entities = mdDataViewService.findDataViewsByParentId(parentId);
            if (entities.size() > 0) {
                for (MdDataView mdDataView : entities) {
                    //创建一个MdMetaModelPojo对象.
                    MdMetaModel mdMetaModel = new MdMetaModel();
                    //通过业务视图中的mdmId查找到MdMetaModelPojo中的模型对象.
                    try {
                        mdMetaModel = this.findMetaModelsById(mdDataView.getMdmId()).get(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    MdMetaModelAndDataViewTree rootTree = new MdMetaModelAndDataViewTree();
                    rootTree.setMdmId(mdMetaModel.getId());
                    rootTree.setMdmName(mdMetaModel.getMdmName());
                    rootTree.setSavePath(mdMetaModel.getSavePath());
                    rootTree.setViewType(viewType);
                    rootTree.setNodePic(mdMetaModel.getNodePic());
                    rootTree.setUiName(mdMetaModel.getUiName());
                    //创建虚拟根节点.
                    roots.add(rootTree);
                }
            }
        } else {
            List<MdMetaModel> metaModels = mdMetaModelServcie.findMetaModelsByParentId(parentId);
            for (MdMetaModel mdMetaModel : metaModels) {
                MdMetaModelAndDataViewTree rootTree = new MdMetaModelAndDataViewTree();
                rootTree.setMdmId(mdMetaModel.getId());
                rootTree.setMdmName(mdMetaModel.getMdmName());
                rootTree.setSavePath(mdMetaModel.getSavePath());
                rootTree.setViewType(viewType);
                rootTree.setNodePic(mdMetaModel.getNodePic());
                rootTree.setUiName(mdMetaModel.getUiName());
                //创建虚拟根节点.
                roots.add(rootTree);
            }
        }
        return roots;
    }

    /**
     * 查找表关系,专属查看按钮.
     *
     * @return
     */
    @Expose
    public String findRelateForLookButton(Map parameter) {
        String mdmId = parameter.get("mdmId").toString();
        return mdMetaModelServcie.findRelateForLookButton(mdmId);
    }

    /**
     * @param innofiPage
     */
    private void transferCn(com.innofi.framework.dao.pagination.Page innofiPage) {
        Map map = new HashMap();
        map.put("parentId", "parentMdmName");
        IdfCodeTransfer codeTransfer = ContextHolder.getSpringBean("mdf.codeTransfer");
        codeTransfer.transferResult(innofiPage.getEntities(), "mdMetaModelService", map);
    }
}
