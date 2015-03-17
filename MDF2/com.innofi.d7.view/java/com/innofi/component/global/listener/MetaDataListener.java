/** Copyright © 2012-2016 Ming Sheng information technology cod., LTD.
 * All rights reserved.*/
package com.innofi.component.global.listener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.bstek.dorado.common.event.DefaultClientEvent;
import com.bstek.dorado.data.listener.GenericObjectListener;
import com.bstek.dorado.data.provider.DataProvider;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.data.type.EntityDataType;
import com.bstek.dorado.data.type.property.BasePropertyDef;
import com.bstek.dorado.data.type.property.PropertyDef;
import com.bstek.dorado.view.View;
import com.bstek.dorado.view.manager.ViewConfig;
import com.bstek.dorado.view.widget.action.AjaxAction;
import com.bstek.dorado.view.widget.base.Dialog;
import com.bstek.dorado.view.widget.base.IFrame;
import com.bstek.dorado.view.widget.base.tab.IFrameTab;
import com.bstek.dorado.view.widget.base.tab.TabControl;
import com.bstek.dorado.view.widget.base.toolbar.Button;
import com.bstek.dorado.view.widget.base.toolbar.ToolBar;
import com.bstek.dorado.view.widget.data.DataSet;
import com.bstek.dorado.view.widget.layout.AnchorLayoutConstraint;
import com.bstek.dorado.view.widget.layout.AnchorMode;
import com.innofi.component.metadata.dataview.service.IMdDataViewService;
import com.innofi.component.metadata.field.service.IMdFieldService;
import com.innofi.component.metadata.mdmdepend.service.IMdMdmDependService;
import com.innofi.component.metadata.metamodel.service.IMdMetaModelService;
import com.innofi.component.metadata.table.service.IMdTableService;
import com.innofi.framework.FrameworkConstants;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.framework.exception.BusinessRuntimeException;
import com.innofi.framework.pojo.metadata.MdDataView;
import com.innofi.framework.pojo.metadata.MdField;
import com.innofi.framework.pojo.metadata.MdMetaModel;
import com.innofi.framework.pojo.metadata.MdTable;
import com.innofi.framework.spring.context.ContextHolder;
import com.innofi.framework.utils.validate.Validator;


/**
 * 功能/ 模块：元数据模块
 *
 * @author 张磊 alex.zhang@innofi.com.cn
 * @version 2.0.0 12-11-30
 *          类描述
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */

@Component
public class MetaDataListener extends GenericObjectListener<ViewConfig> {
    @Resource
    private IMdMdmDependService mdMdmDependService;
    @Resource
    private IMdMetaModelService mdMetaModelServcie;
    @Resource
    private IMdFieldService mdFieldService;
    @Resource
    private IMdTableService mdTableService;
    @Resource
    private IMdMetaModelService mdMetaModelService;
    @Resource
    private IMdDataViewService mdDataViewService;

    /**
     *
     */
    public void onCreateMetaDataInfoInit(ViewConfig viewConfig) throws Exception {
        HttpServletRequest request = ContextHolder.getContext().getDoradoContext().getRequest(); //获取路径信息
        String mdmId = request.getParameter("mdmId");                                            //获取路径中传来的参数  mdmId 元模型ID,mdId元数据ID
        String mdId = request.getParameter("mdId");
        String savePath = mdMetaModelServcie.get(mdmId).getSavePath();     //获取元模型的表名
        PropertyDef propertyDef;
        MdTable tablePojo = mdTableService.findLastVersionMdTableByTableName(savePath);
        /**
         *获取字段信息，通过savePath获取字段信息，获取字段信息。
         *通过字段信息的是否查询和是否列表显示获取列表显示选项
         */
        List<MdField> mdFieldForEntity = new ArrayList<MdField>();
        String sqlForEntity = "SELECT * FROM MD_FIELD T WHERE T.TABLE_ID = ? AND T.IS_TABLE_VIEW = '" + FrameworkConstants.COMM_Y + "'";
        mdFieldForEntity = mdFieldService.queryBeanForList(sqlForEntity, new Object[]{tablePojo.getId()});
        if (mdFieldForEntity.size() == 0) {
            sqlForEntity = "SELECT * FROM MD_FIELD T WHERE T.TABLE_ID = ? AND T.STATUS = '" + FrameworkConstants.COMM_Y + "'";
            mdFieldForEntity = mdFieldService.queryBeanForList(sqlForEntity, new Object[]{tablePojo.getId()});
        }
        EntityDataType entityDataType = (EntityDataType) viewConfig.getDataType("entityDataType");
        //todo 后续逻辑需要添加判断 propertyString 是否为空
        for (int i = 0; i < mdFieldForEntity.size(); i++) {
            String propertyString = mdFieldForEntity.get(i).getEntityAttriName();
            if (Validator.isEmpty(propertyString)) {
                continue;
            } else {
                //冗余实体对象名称
                String entityAttriNameString = tnToEn(mdFieldForEntity.get(i).getFieldName()).get(1);
                if (propertyString == null || "".equals(propertyString)) {
                    throw new BusinessRuntimeException("在MD_FIELD中未找EntityAttriName为" + tnToEn(mdFieldForEntity.get(i).getFieldName()).get(1) + "的数据表，表字段数据不完整，请补充相应表字段元数据!");
                }
                propertyDef = new BasePropertyDef(propertyString);
                if (mdFieldForEntity.get(i).getFieldType().equals("VARCHAR2") || mdFieldForEntity.get(i).getFieldType().equals("VARCHAR")) {
                    propertyDef.setDataType(viewConfig.getDataType("String"));
                } else {
                    propertyDef.setDataType(viewConfig.getDataType(mdFieldForEntity.get(i).getFieldType()));
                }
                if (!mdFieldForEntity.get(i).getIsPk().equals("1")) {
                    propertyDef.setLabel(mdFieldForEntity.get(i).getLabelInf());
                    propertyDef.setReadOnly(true);
                    entityDataType.addPropertyDef(propertyDef);
                }
            }
        }
        View view = viewConfig.getView();
        DataSet entityDataSet = (DataSet) view.getComponent("entityDataSet");
        DataType dateType = entityDataSet.getDataType();
        DataProvider dataProvider = entityDataSet.getDataProvider();
        dataProvider.setId(tnToEn(savePath).get(0) + "Action#find" + tnToEn(savePath).get(1) + "s");
        dataProvider.setParameter(mdId);
        entityDataSet.setReadOnly(true);
    }

    /**
     * 设置查询DataType、主DataType的属性
     *
     * @param viewCofig
     * @throws Exception
     */
    public void onCreateViewInit(ViewConfig viewCofig) throws Exception {

        HttpServletRequest request = ContextHolder.getContext().getDoradoContext().getRequest();
        // ConsoleUtil.test("元模型ID["+request.getParameter("mId")+"]");
        String savePath = request.getParameter("savePath");
        //String mdmId = request.getParameter("mdmId");
        String mdIds = request.getParameter("mdIds");
        String entityName = "";
        String className = "";
        //savePath = metaModel.get(0).getSavePath().toLowerCase();
        String tableName = savePath.toLowerCase();
        String[] stringArray = tableName.split("_");
        for (int i = 0; i < stringArray.length; i++) {
            entityName += stringArray[i].substring(0, 1).toUpperCase() + stringArray[i].substring(1, stringArray[i].length());
        }
        for (int i = 0; i < stringArray.length; i++) {
            if (i == 0) {
                className = stringArray[i].toString();
            } else {
                className += stringArray[i].substring(0, 1).toUpperCase() + stringArray[i].substring(1, stringArray[i].length());
            }
        }
        PropertyDef propertyDef;
        /**
         *获取字段信息，通过savePath获取字段信息，获取字段信息。
         *通过字段信息的是否查询和是否列表显示获取查询选项
         */
        List<MdField> mdFieldForQuery = new ArrayList<MdField>();
        MdTable tablePojo = mdTableService.findLastVersionMdTableByTableName(savePath);
        String sqlForQuery = "SELECT * FROM MD_FIELD T WHERE T.TABLE_ID = ? AND T.IS_QUERY_COND = '" + FrameworkConstants.COMM_Y + "'";
        mdFieldForQuery = mdFieldService.queryBeanForList(sqlForQuery, new Object[]{tablePojo.getId()});

        // 按照我们昨天说的查库动态创建DataType的PropertyDef，可不考虑效率问题，会统一设置缓存
        EntityDataType queryDataType = (EntityDataType) viewCofig.getDataType("queryDataType");
        for (int i = 0; i < mdFieldForQuery.size(); i++) {
            String propertyString = mdFieldForQuery.get(i).getEntityAttriName();
            if (Validator.isEmpty(propertyString)) {
                continue;
            } else {
                propertyDef = new BasePropertyDef(propertyString);
                if (mdFieldForQuery.get(i).getFieldType().equals("VARCHAR2") || mdFieldForQuery.get(i).getFieldType().equals("VARCHAR")) {
                    propertyDef.setDataType(viewCofig.getDataType("String"));
                } else {
                    propertyDef.setDataType(viewCofig.getDataType(mdFieldForQuery.get(i).getFieldType()));
                }
                propertyDef.setLabel(mdFieldForQuery.get(i).getLabelInf());
                propertyDef.setReadOnly(false);
                queryDataType.addPropertyDef(propertyDef);
            }
        }
        /**
         *获取字段信息，通过savePath获取字段信息，获取字段信息。
         *通过字段信息的是否查询和是否列表显示获取列表显示选项
         */
        List<MdField> mdFieldForEntity = new ArrayList<MdField>();
        String sqlForEntity = "SELECT * FROM MD_FIELD T WHERE T.TABLE_ID = ? AND T.IS_TABLE_VIEW = '" + FrameworkConstants.COMM_Y + "'";
        mdFieldForEntity = mdFieldService.queryBeanForList(sqlForEntity, new Object[]{tablePojo.getId()});
        EntityDataType entityDataType = (EntityDataType) viewCofig.getDataType("entityDataType");
        for (int i = 0; i < mdFieldForEntity.size(); i++) {
            String propertyString = mdFieldForEntity.get(i).getEntityAttriName();
            if (Validator.isEmpty(propertyString)) {
                continue;
            } else {
                propertyDef = new BasePropertyDef(propertyString);
                if (mdFieldForEntity.get(i).getFieldType().equals("VARCHAR2") || mdFieldForEntity.get(i).getFieldType().equals("VARCHAR")) {
                    propertyDef.setDataType(viewCofig.getDataType("String"));
                } else {
                    propertyDef.setDataType(viewCofig.getDataType(mdFieldForEntity.get(i).getFieldType()));
                }
                propertyDef.setLabel(mdFieldForEntity.get(i).getLabelInf());
                propertyDef.setReadOnly(true);
                entityDataType.addPropertyDef(propertyDef);
            }
        }
        View view = viewCofig.getView();
        DataSet entityDataSet = (DataSet) view.getComponent("entityDataSet");
        DataProvider dataProvider = entityDataSet.getDataProvider();
        //dataProvider.setId("mdMdmDependAction#findMdMdmDepends");
        dataProvider.setId(className + "Action#find" + entityName + "s");
        //dataProvider.setId(tnToEn(savePath).get(0)+"Action#find"+tnToEn(savePath).get(1)+"s");
        dataProvider.setParameter(mdIds);

    }

    /**
     * 主View监听方法
     *
     * @param view
     * @throws Exception
     */
    public void onMainViewInit(View view) throws Exception {
        //从请求中获取参数
        HttpServletRequest request = ContextHolder.getContext().getDoradoContext().getRequest();
        //最开始的元模型id
        String mdmId = request.getParameter("mdmId");
        //依赖元模型id集合
        String dmdIds = request.getParameter("dmdIds");
        //元数据id
        String mdId = request.getParameter("mdId");
        //区分创建的类型
        String lookStyle = request.getParameter("lookStyle");
        //获取对象
        String pkString = request.getParameter("pkString");
        if (lookStyle == null) {
            lookStyle = "null";
        }
        //获取TabControl组件
        TabControl mainTabControl = (TabControl) view.getComponent("mainTabControl");
        //判断是否是查看类型的数据
        if ("look".endsWith(lookStyle)) {
            //创建一个基本信息的iframe。
            IFrameTab metaDataIFrameTab = new IFrameTab();
            metaDataIFrameTab.setPath("com.innofi.component.metadata.commons.view.MetaDataInfo.d?mdId=" + mdId + "&mdmId=" + mdmId + "&randomId=" + Math.random());
            metaDataIFrameTab.setCaption("基本信息");
            mainTabControl.addTab(metaDataIFrameTab);
            //通过元模型ID获得元模型的数据.
            MdMetaModel mdMetaModel = mdMetaModelServcie.get(mdmId);
            //--------------------------------------------------------------------------------------------
            //通过关联关系查找对应的元模型中的数据. 
            List<Map<String, Object>> mdMetaModelLists = new ArrayList<Map<String, Object>>();
            mdMetaModelLists = mdMetaModelService.findMetaModelAndTableRelate(mdMetaModel.getId(), mdMetaModel.getSavePath());
            if (mdMetaModelLists != null && mdMetaModelLists.size() != 0) {
                for (Map<String, Object> mdMetaModelPojo : mdMetaModelLists) {
                    IFrameTab iFrameTab = new IFrameTab();
                    String uiName = mdMetaModelPojo.get("UI_NAME").toString();
                    // String savePath = mdMetaModelPojo.get("SAVE_PATH").toString();
                    String tableName = mdTableService.get(mdId).getTableName().toString();
                    iFrameTab.setPath(uiName + "?mdId=" + mdId + "&tableId=" + mdId + "&tableName=" + tableName + "&lookStyle=" + lookStyle + "&mainFieldValue=" + pkString + "&foreignField=" + mdMetaModelPojo.get("FOREIGN_FIELD") + "&randomId=" + Math.random());
                    iFrameTab.setCaption(mdMetaModelPojo.get("MDM_NAME").toString());
                    mainTabControl.addTab(iFrameTab);
                }
            }
        } else {
            //关系元模型ID列表 这里应该是查询方法，这里通过new ArrayList 添加点假数据，道理是一样的
            List<String> modelIdList = new ArrayList<String>();
            //获取元模型关系表中的元模型ID
            modelIdList = mdMdmDependService.findMdMdmDependByMdmId(mdmId);
            for (int i = 0; i < modelIdList.size(); i++) {

                MdMetaModel mdMetaModel = mdMetaModelServcie.get(modelIdList.get(i));
                String savePath = mdMetaModel.getSavePath();

                //新获得的元模型ID，用于查询
                IFrameTab iFrameTab = new IFrameTab();
                iFrameTab.setPath("com.innofi.component.metadata.commons.view.Entity.d?dmdIds=" + dmdIds + "&mdmId=" + mdmId + "&mdId=" + mdId + "&savePath=" + savePath);
                iFrameTab.setCaption(mdMetaModel.getMdmName());
                mainTabControl.addTab(iFrameTab);
            }
        }
    }

    @Override
    public boolean beforeInit(ViewConfig arg0) throws Exception {
        return true;
    }


    @Override
    public void onInit(ViewConfig viewConfig) throws Exception {
        long startTime = new Date().getTime();

        View view = viewConfig.getView();
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        //获取view
        String viewPath = "";
        String entityName = "";
        HttpServletRequest request = ContextHolder.getContext().getDoradoContext().getRequest();
        String mdmId = request.getParameter("mdmId");
        String uri = request.getRequestURI();
        if (Validator.isNotNull(mdmId)) { //add by jack.liu 避免不必要的查询，当mdId不为null时再执行
            //if(uri.indexOf("com.innofi.component.metadata.") > -1){			//add by alex.zhang 通过路径去判断是否执行下面的操作.
            MdMetaModel metaModel = mdMetaModelServcie.get(mdmId);
            if(metaModel==null){
            	MdDataView mdDataView = mdDataViewService.get(mdmId);
            	metaModel = mdMetaModelServcie.get(mdDataView.getMdmId());
            }
            if (metaModel != null) {
                Dialog dialogCheck = (Dialog) view.getComponent("dependDialog");
                if (dialogCheck == null) {
                    //String uri = request.getRequestURI();
                    //获取url中的com.innofi.....的路径。
                    if (uri.indexOf(".") > -1) {
                        viewPath = uri.substring(uri.lastIndexOf("/") + 1, uri.lastIndexOf(".d") + 2);
                    }
                    if ("com.innofi.component.metadata.commons.view.Entity.d".equals(viewPath)) {
                        this.onCreateViewInit(viewConfig);
                    }
                    if ("com.innofi.component.metadata.commons.view.TabControlMain.d".equals(viewPath)) {
                        this.onMainViewInit(viewConfig.getView());
                    }
                    if ("com.innofi.component.metadata.commons.view.MetaDataInfo.d".equals(viewPath)) {
                        this.onCreateMetaDataInfoInit(viewConfig);
                    }
                    //管理关系中用到次创建。创建对话框对象。创建iframe对象。动态生成。
                    Dialog dialog = new Dialog();
                    IFrame iFrame = new IFrame();
                    dialog.setId("dependDialog");
                    dialog.setCaption("依赖关系维护");
                    dialog.setMaximizeable(true);
                    dialog.setMinimizeable(true);
                    dialog.setCenter(true);
                    dialog.setModal(true);
                    dialog.setHeight("100%");
                    dialog.setWidth("100%");
                    iFrame.setId("dependIFrame");
                    dialog.addChild(iFrame);
                    view.addChild(dialog);
                    //创建一个查看的对话框，该对话框只存在元模型中。
                    Dialog lookDialog = new Dialog();
                    IFrame lookIFrame = new IFrame();
                    lookDialog.setId("lookDialog");
                    lookDialog.setCaption("元数据查看");
                    lookDialog.setCenter(true);
                    lookDialog.setModal(true);
                    lookDialog.setHeight("100%");
                    lookDialog.setWidth("100%");
                    lookDialog.setMaximizeable(true);
                    lookDialog.setMinimizeable(true);
                    lookIFrame.setId("lookIframe");
                    lookDialog.addChild(lookIFrame);
                    Button closeButton = new Button();
                    closeButton.setId("closeButton");
                    closeButton.setCaption("关闭");
                    closeButton.setIcon("url(>skin>common/icons.gif) -40px -0px");
                    closeButton.addClientEventListener(
                            "onClick",
                            new DefaultClientEvent(
                                    "view.get(\"#lookDialog\").hide();"
                            ));
                    lookDialog.addButton(closeButton);
                    view.addChild(lookDialog);
                    //定义一个ajaxAction.目的是获取元数据中的关联关系.
                    AjaxAction ajaxAction = new AjaxAction();
                    ajaxAction.setId("findRelateForLookButton");
                    ajaxAction.setService("mdMetaModelAction#findRelateForLookButton");
                    view.addChild(ajaxAction);
//		        }
//		        ToolBar buttonCheck = (ToolBar) view.getComponent("dependButton");
//		        if(buttonCheck == null){
                    String entity = metaModel.getSavePath().toLowerCase();
                    String[] stringArray = entity.split("_");
                    for (int i = 0; i < stringArray.length; i++) {
                        entityName += stringArray[i].substring(0, 1).toUpperCase() + stringArray[i].substring(1, stringArray[i].length());
                    }
                    mdMetaModelServcie.addPropertyFilter(filters, "uiName", viewPath+"%", QueryConstants.LIKE, true);
                    Long num = mdMetaModelServcie.countByPropertyFilters(filters);
                    if (num > 0) {
                        ToolBar toolBar = (ToolBar) view.getComponent("toolBar" + entityName);

                        //Button lookButton = (Button) view.getComponent("lookButton"); add by zhanglei
                        Button lookButton = new Button();
                        lookButton.setId("lookMetaDataRelateButton");
                        lookButton.setCaption("关联查询");
                        lookButton.setIcon("url(>skin>common/icons.gif) -120px -120px");
                        AnchorLayoutConstraint lookMDRLayout = new AnchorLayoutConstraint();
                        lookMDRLayout.setAnchorLeft(AnchorMode.previous);
                        lookMDRLayout.setLeft("5");
                        lookMDRLayout.setTop("10");
                        lookButton.setLayoutConstraint(lookMDRLayout);

                        //给按钮添加单击事件
                        lookButton.addClientEventListener(
                                "onClick",
                                new DefaultClientEvent(
                                        "var mdmId=\"" + mdmId + "\";" +
                                                "var dataSet = view.get(\"#dataSet" + entityName + "\");" +
                                                "var ajaxAction = view.get(\"#findRelateForLookButton\");" +
                                                "var selection = view.get(\"#dataGrid" + entityName + "\").get(\"selection\");" +
                                                "var dialog = view.get(\"#lookDialog\");" +
                                                "if(selection.length==0){dorado.MessageBox.alert(\"请选择记录！\");}" +
                                                "else if(selection.length>1){" +
                                                "dorado.MessageBox.alert(\"只能选择一条记录！\");}" +
                                                "else if (selection.length == 1){" +
                                                "dataSet.getData().setCurrent(selection[0]);" +
                                                "var id = dataSet.getData(\"#\").get(\"id\");" +
                                                "var iframe = view.get(\"#lookIframe\");" +
                                                "ajaxAction.set(\"parameter\",{mdmId:mdmId}).execute(function(result){" +
                                                //应该取pk字段值，而不能约定主键为id moidfy by yanli
                                                "var pkString = dataSet.getData(\"#\").get('id');" +
                                                "" +
                                                "var path=\"com.innofi.component.metadata.commons.view.TabControlMain.d?mdmId=\"+mdmId+\"&mdId=\"+id+\"&lookStyle=look&pkString=\"+pkString+\"&randomId=\"+Math.random();" +
                                                "iframe.set(\"path\",path);" +
                                                "dialog.show();" +
                                                "});" +
                                                "}"));

                        //add end by zhangl


                        //创建关联关系button对象。
                        Button button = new Button();
                        button.setId("dependButton");
                        button.setCaption("关系维护");
                        button.setIcon("url(>skin>common/icons.gif) -240px -160px");
                        AnchorLayoutConstraint layoutConstraint = new AnchorLayoutConstraint();
                        layoutConstraint.setAnchorLeft(AnchorMode.previous);
                        layoutConstraint.setLeft("5");
                        layoutConstraint.setTop("10");
                        button.setLayoutConstraint(layoutConstraint);

                        //给按钮添加单击事件
                        button.addClientEventListener(
                                "onClick",
                                new DefaultClientEvent(
                                        "var mdmId=\"" + mdmId + "\";" +
                                                "var dataSet = view.get(\"#dataSet" + entityName + "\");" +
                                                "var selection = view.get(\"#dataGrid" + entityName + "\").get(\"selection\");" +
                                                "var dialog = view.get(\"#dependDialog\");" +
                                                "if(selection.length==0){dorado.MessageBox.alert(\"请选择记录！\");}else if(selection.length>1){" +
                                                "dorado.MessageBox.alert(\"只能选择一条记录！\");}else if (selection.length == 1){" +
                                                "dataSet.getData().setCurrent(selection[0]);" +
                                                "var id = dataSet.getData(\"#\").get(\"id\");" +
                                                "var path=\"com.innofi.component.metadata.mddepend.view.MdMdDependManage.d?mdmId=\"+mdmId+\"&mdId=\"+id+\"&randomId=\"+Math.random();" +
                                                "view.get(\"#dependIFrame\").set(\"path\",path);" +
                                                "dialog.show();" +
                                                "}"));
                        if (toolBar != null) {
                            toolBar.addItem(lookButton);
                            toolBar.addItem(button);
                        }
                    }
                }
            }
        }
        long endTime = new Date().getTime();

        //ConsoleUtil.test("MetaDataListener execute time ["+((endTime-startTime)/1000)+"]");
    }

    /**
     * 把表名变成类名和对象名
     *
     * @param tableName
     * @return
     */
    public List<String> tnToEn(String tableName) {
        //定义实体对象名和类名
        String entityName = "";
        String className = "";
        tableName = tableName.toLowerCase();
        String[] stringArray = tableName.split("_");
        for (int i = 0; i < stringArray.length; i++) {
            entityName += stringArray[i].substring(0, 1).toUpperCase() + stringArray[i].substring(1, stringArray[i].length());
        }
        for (int i = 0; i < stringArray.length; i++) {
            if (i == 0) {
                className = stringArray[i].toString();
            } else {
                className += stringArray[i].substring(0, 1).toUpperCase() + stringArray[i].substring(1, stringArray[i].length());
            }
        }
        List<String> list = new ArrayList();
        list.add(className);
        list.add(entityName);
        return list;
    }
}
