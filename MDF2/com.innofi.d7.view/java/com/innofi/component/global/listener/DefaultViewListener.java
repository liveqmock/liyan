/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.component.global.listener;

import com.bstek.dorado.config.definition.Definition;
import com.bstek.dorado.core.Context;
import com.bstek.dorado.core.el.Expression;
import com.bstek.dorado.core.el.ExpressionHandler;
import com.bstek.dorado.data.config.definition.DataTypeDefinition;
import com.bstek.dorado.data.config.definition.GlobalDataTypeDefinitionManager;
import com.bstek.dorado.data.config.definition.PropertyDefDefinition;
import com.bstek.dorado.data.listener.GenericObjectListener;
import com.bstek.dorado.data.type.EntityDataType;
import com.bstek.dorado.data.type.manager.DataTypeManager;
import com.bstek.dorado.data.type.property.BasePropertyDef;
import com.bstek.dorado.data.type.property.PropertyDef;
import com.bstek.dorado.view.View;
import com.bstek.dorado.view.ViewState;
import com.bstek.dorado.view.manager.ViewConfig;
import com.bstek.dorado.view.widget.Component;
import com.bstek.dorado.view.widget.Control;
import com.bstek.dorado.view.widget.HideMode;
import com.bstek.dorado.view.widget.base.toolbar.ToolBar;
import com.bstek.dorado.view.widget.form.autoform.AutoForm;
import com.bstek.dorado.view.widget.form.autoform.AutoFormElement;
import com.bstek.dorado.view.widget.grid.Column;
import com.bstek.dorado.view.widget.grid.DataGrid;
import com.bstek.dorado.web.DoradoContext;
import com.innofi.component.metadata.commons.MetadataConstants;
import com.innofi.component.metadata.field.service.IMdFieldService;
import com.innofi.component.metadata.table.service.IMdTableService;
import com.innofi.component.rbac.login.action.LoginAction;
import com.innofi.component.rbac.userview.pojo.SysUserViewCriteria;
import com.innofi.component.rbac.userview.service.ISysUserViewCriteriaService;
import com.innofi.framework.FrameworkConstants;
import com.innofi.component.codetransfer.IdfCodeTransfer;
import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.framework.exception.BusinessRuntimeException;
import com.innofi.framework.pojo.UIFieldPojo;
import com.innofi.framework.pojo.metadata.MdField;
import com.innofi.framework.pojo.metadata.MdTable;
import com.innofi.component.rbac.privilage.pojo.SysFunAction;
import com.innofi.component.rbac.menu.pojo.SysMenu;
import com.innofi.framework.spring.context.ContextHolder;
import com.innofi.framework.utils.Pair;
import com.innofi.framework.utils.sql.SqlUtil;
import com.innofi.framework.utils.string.StringUtil;
import com.innofi.framework.utils.validate.Validator;
import com.innofi.framework.utils.xml.XmlBuilder;
import com.innofi.framework.utils.xml.XmlDocument;
import com.innofi.framework.utils.xml.XmlNode;
import com.innofi.framework.utils.xml.XmlParseException;
import com.innofi.framework.utils.xml.dom4j.Dom4jXmlBuilder;
import org.apache.commons.collections.map.ListOrderedMap;
import org.hibernate.mapping.PersistentClass;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;

/**
 * 功能/ 模块：框架基础模块
 *
 * @author 刘名寓 liumy2009@126.com
 * @version 2.0.0 12-11-30 IDF框架默认视图监听器，实现控件Label动态翻译 修订历史： 日期 作者 参考 描述
 *          北京名晟信息技术有限公司版权所有.
 */
public class DefaultViewListener extends GenericObjectListener<ViewConfig> {

    private XmlBuilder xmlBuilder = new Dom4jXmlBuilder();

    public final String loginUrl = LoginAction.LOGIN_VIEW_URL;

    @Resource(name = "mdDataTitleTableMappingDaoSupport")
    protected DaoSupport daoSupport;

    public DaoSupport getDaoSupport() {
        return daoSupport;
    }

    /**
     * @see com.bstek.dorado.data.listener.GenericObjectListener#beforeInit(Object)
     */
    public boolean beforeInit(ViewConfig viewConfig) throws Exception {
        loadingReferenceDataType(viewConfig);//加载view中引用的dataType，将propertyDef对应的Label放置request.attribute当中
        return true;
    }

    

    /**
     * @see com.bstek.dorado.data.listener.GenericObjectListener#onInit(Object)
     */
    @Override
    public void onInit(ViewConfig viewConfig) throws Exception {
        List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
        HttpServletRequest request = ContextHolder.getRequest();
        String url = request.getRequestURL().toString();
        if (url.indexOf(loginUrl) == -1) {//不是登录页面执行后面逻辑
            setUserUseableOperation(viewConfig, request, url);//设置用户页面可用操作
            View view = viewConfig.getView();
            if(Validator.isNotNull(view)){
                ISysUserViewCriteriaService sysUserViewCriteriaService = ContextHolder.getSpringBean("sysUserViewCriteriaService");
                String viewPath = viewConfig.getName()+".d";
                try{
                	String userId = ContextHolder.getContext().getLoginUser().getId();
                    propertyFilters.add(new PropertyFilter(QueryConstants.RESTRICTION_TYPE_AND,"viewPath", viewPath, QueryConstants.EQUAL));
                    propertyFilters.add(new PropertyFilter(QueryConstants.RESTRICTION_TYPE_AND,"userId", userId, QueryConstants.EQUAL));
                    propertyFilters.add(new PropertyFilter(QueryConstants.RESTRICTION_TYPE_AND,"cfgType", FrameworkConstants.USER_CFG_TYPE_VIEW, QueryConstants.EQUAL));
                    List<SysUserViewCriteria> sysUserViewCriterias = sysUserViewCriteriaService.findByPropertyFilters(propertyFilters,null);
                    if(sysUserViewCriterias.size()>0){
                        for(SysUserViewCriteria sysUserViewCriteria : sysUserViewCriterias){
                            String dataGridId = sysUserViewCriteria.getSchemaName();//dataGridId
                            String tableColumns = sysUserViewCriteria.getCfgContent().toLowerCase();
                            DataGrid dataGrid = (DataGrid) view.getViewElement(dataGridId);
                            if(Validator.isNotNull(dataGrid)) {
                                List<Column> columns = dataGrid.getColumns();
                                for(int i = 2 ; i < columns.size() ; i++){
                                    Column col = columns.get(i);
                                    if(Validator.isNotNull(col.getName())&&tableColumns.indexOf(col.getName().toLowerCase())==-1){
                                        col.setVisible(false);
                                    }else{
                                        col.setVisible(true);
                                    }
                                }
                            }
                        }
                    }
                }catch (Exception e) {
                	e.printStackTrace();
				}
            }
        }


    }

    /**
     * 设置用户可用
     * @param viewConfig
     * @param request
     * @param url
     * @throws Exception
     */
    private void setUserUseableOperation(ViewConfig viewConfig, HttpServletRequest request, String url) throws Exception {
        View view = viewConfig.getView();

        Set<DataTypeDefinition> dataTypeDefinitions = (Set<DataTypeDefinition>) DoradoContext.getCurrent().getAttribute(DoradoContext.SESSION, "dataTypeDefinitions");
        if (dataTypeDefinitions != null) {
            for (DataTypeDefinition dataTypeDefinition : dataTypeDefinitions) {
                EntityDataType entityDataType = (EntityDataType) viewConfig.getDataType(dataTypeDefinition.getName());
                if (entityDataType != null && Validator.isNotEmpty(entityDataType.getTags()) && entityDataType.getTags().indexOf("=") != -1) {
                    addEntityDataTypePropertyDef(view, entityDataType, null);
                }
                while (dataTypeDefinition != null && dataTypeDefinition.getParents() != null && dataTypeDefinition.getParents().length > 0) {
                    Definition[] parents = dataTypeDefinition.getParents();
                    dataTypeDefinition = (DataTypeDefinition) parents[0];
                    addEntityDataTypePropertyDef(view, entityDataType, dataTypeDefinition);
                }
            }
        }

        if (view != null && ContextHolder.getSystemProperties().getBoolean("mdf.system.permission.switch")) { // 是否进行权限控制
            Collection<Component> components = view.getAllComponents();
            List<SysFunAction> userActions = (List<SysFunAction>) ContextHolder.getRequest().getSession().getAttribute(MetadataConstants.SESSION_USER_ACTION);// 权限按钮
            HashSet userActionIds = new HashSet();
            if (userActions != null) {
                for (SysFunAction funAction : userActions) {
                    userActionIds.add(funAction.getId());
                }
            }


            Map parameter = request.getParameterMap();
            String menuUrl = getUrlPath(url);

            int index = 0;
            for (Iterator it = parameter.keySet().iterator(); it.hasNext(); ) {
                String key = (String) it.next();
                String[] values = (String[]) parameter.get(key);
                String value = "";
                if (values != null && values.length >= 1) {
                    for (int i = 0; i < values.length; i++) {
                        value += values[i];
                    }
                }
                if (index == 0) {
                    menuUrl = menuUrl + "?" + key + "=" + value;
                } else {
                    menuUrl = menuUrl + "&" + key + "=" + value;
                }

                index++;
            }

            IdfCodeTransfer codeTransfer = ContextHolder.getSpringBean("mdf.codeTransfer");
            ListOrderedMap sysFunActions = codeTransfer.getCacheObjects("sysFunActionService");// 所有权限控制按钮
            ListOrderedMap sysMenus = codeTransfer.getCacheObjects("sysMenuService");
            
            if(Validator.isNotNull(menuUrl)){
	            ListOrderedMap menus = IdfCodeTransfer.listDistinctFilter(sysMenus, "menuUrl", menuUrl, null);
	            if (menus.size() > 1) {
	                throw new RuntimeException("url [" + menuUrl + "]重复，请检查sys_menu表数据!");
	            }
	            
	            if (menus.size() > 0) {// 功能权限控制
	                SysMenu menu = (SysMenu) menus.getValue(0);
	                sysFunActions = IdfCodeTransfer.listDistinctFilter(sysFunActions, "menuId", menu.getId(), null);
	                for (Component component : components) {
	                    String tagToolBar = component.getTags();
	                    if (component instanceof ToolBar && "tags_toolbar".equals(tagToolBar)) {
	                        ToolBar toolBar = (ToolBar) component;
	                        List<Control> controls = toolBar.getItems();
	                        for (Control control : controls) {// 遍历按钮
	                            String id = control.getId();
	                            for (int i = 0; i < sysFunActions.size(); i++) {
	                                SysFunAction funAction = (SysFunAction) sysFunActions.getValue(i);
	                                if (id.equals(funAction.getActionCode())) {// 存在权限控制
	                                    boolean visibleFlag = false;
	                                    if (userActionIds.contains(funAction.getId())) {
	                                        visibleFlag = true;
	                                    }
	                                    if (!visibleFlag) {
	                                        control.setHideMode(HideMode.display);
	                                        control.setVisible(false);
	                                    }
	                                    break;
	                                }
	                            }
	                        }
	                    }
	                }
	            }
            }
        }
    }

    /**
     * 加载视图中的DataType，将propertyDef的中文显示信息放置request.attribute中
     * @param  viewConfig   视图配置对象
     * @throws IOException
     * @throws XmlParseException
     * @throws ClassNotFoundException
     */
    private void loadingReferenceDataType(ViewConfig viewConfig) throws IOException, XmlParseException, ClassNotFoundException {
        /**
         * View在初始化时，获取当前View定义的DataType，
         * 根据DataType获取creationType，根据creationType去MD_FIELD加载BANK_TEXT_INF;LABEL_INF;TIP_INF;
         * 用于前端显示，ENTITY_ATTRI_NAME作为${req["propertyName"]}中的KEY
         */
        String viewPath = viewConfig.getName();
        Set<String> viewPaths = new HashSet<String>();                              // 保存所有View路径，包括View中的SubView
        Set<String> creationTypes = new HashSet<String>();                          // 保存View中所有dataType绑定的creationType
        Set<DataTypeDefinition> dataTypeDefinitions = new HashSet<DataTypeDefinition>();
        GlobalDataTypeDefinitionManager globalDataTypeDefinitionManager = ContextHolder.getSpringBean("dorado.dataTypeDefinitionManager");// dataTypeDefinitionManager
        Map<String, DataTypeDefinition> dataTypeDefinitionMap = globalDataTypeDefinitionManager.getDefinitions();// 获取所有全局的dataType定义
        Context context = Context.getCurrent(); // 上下文对象

        if (viewConfig.getState().equals(ViewState.rendering)) {
            /**
             * 解析View，将所有view路径保存至viewPaths集合中
             */
            loadSubViewAndImportViewPath(viewPath, viewPaths);
            for (Iterator it = viewPaths.iterator(); it.hasNext(); ) {
                String viewPathElement = it.next().toString();
                com.bstek.dorado.core.io.Resource resource = context.getResource(viewPathElement);
                XmlDocument document = xmlBuilder.buildDocument(resource.getInputStream());

                /**
                 * DataType节点
                 */
                XmlNode[] dataTypes = document.getNodesByXPath("//DataType");
                for (XmlNode dataTypeNode : dataTypes) {
                    String dataTypeName = dataTypeNode.getAttribute("name");
                    String parentTypeName = dataTypeNode.getAttributeString("parent");

                    XmlNode creationTypeNode = dataTypeNode.getNodeByXPath("./Property[@name='creationType']");
                    if (creationTypeNode != null) {
                        String creationType = creationTypeNode.getContent();
                        creationTypes.add(creationType);
                    }
                    XmlNode tagNode = dataTypeNode.getNodeByXPath("./Property[@name='tags']");
                    DataTypeDefinition dataTypeDefinition = null;
                    if (tagNode != null) {
                        String tag = tagNode.getContent();
                        dataTypeDefinition = new DataTypeDefinition();
                        dataTypeDefinition.setName(dataTypeName);
                        dataTypeDefinition.setProperty("tags", tag);
                        dataTypeDefinitions.add(dataTypeDefinition);
                    }
                    if (parentTypeName.indexOf("global:") != -1) {
                        parentTypeName = parentTypeName.substring(7);
                    }
                    if (Validator.isNotEmpty(parentTypeName)) {
                        DataTypeDefinition pdtd = dataTypeDefinitionMap.get(parentTypeName);
                        if (dataTypeDefinition != null) {
                            dataTypeDefinition.setParent(pdtd);
                        }
                        recursiveLoadCreationType(creationTypes, pdtd);
                    }
                }

                /**
                 * Reference节点
                 */
                XmlNode[] references = document.getNodesByXPath("//Reference");
                for (XmlNode referenceNode : references) {
                    XmlNode[] datasetTypeNodes = referenceNode.getNodesByXPath("./Property[@name='dataType']");
                    if (datasetTypeNodes != null && datasetTypeNodes.length != 0) {
                        String dataSetDataType = datasetTypeNodes[0].getContent();
                        if (dataSetDataType.indexOf("[") != -1) {
                            dataSetDataType = dataSetDataType.substring(1, dataSetDataType.length() - 1);
                        }
                        DataTypeDefinition dataTypeDefinition = dataTypeDefinitionMap.get(dataSetDataType);
                        recursiveLoadCreationType(creationTypes, dataTypeDefinition);
                    }
                }

                /**
                 * DataSet节点
                 */
                XmlNode[] dataSets = document.getNodesByXPath("//DataSet");
                for (XmlNode dataSetNode : dataSets) {
                    XmlNode[] datasetTypeNodes = dataSetNode.getNodesByXPath("./Property[@name='dataType']");
                    if (datasetTypeNodes != null && datasetTypeNodes.length != 0) {
                        String dataSetDataType = datasetTypeNodes[0].getContent();
                        if (dataSetDataType.indexOf("[") != -1) {
                            dataSetDataType = dataSetDataType.substring(1, dataSetDataType.length() - 1);
                        }

                        DataTypeDefinition dataTypeDefinition = dataTypeDefinitionMap.get(dataSetDataType);
                        if (dataTypeDefinition != null) {
                            dataTypeDefinitions.add(dataTypeDefinition);
                            recursiveLoadCreationType(creationTypes, dataTypeDefinition);
                        }
                    }
                }

            }

            translateFieldLabel(creationTypes);
            //DoradoContext.getCurrent().setAttribute(DoradoContext.SESSION, "dataTypeDefinitions", dataTypeDefinitions);
        }
    }

    /**
     * 递归加载dataType的creationType
     *
     * @param creationTypes      保存creationType的集合
     * @param dataTypeDefinition 数据类型定义uixiang
     */
    private void recursiveLoadCreationType(Set<String> creationTypes, DataTypeDefinition dataTypeDefinition) {
        if (null != dataTypeDefinition) {
            if (Validator.isNotEmpty((String) dataTypeDefinition.getProperty("tags"))) {
                Definition[] pdtd = dataTypeDefinition.getParents();
                if (pdtd != null && pdtd.length > 0) {
                    recursiveLoadCreationType(creationTypes, (DataTypeDefinition) pdtd[0]);
                }
            }
            // 添加creationType
            if (null != dataTypeDefinition.getCreationType()) {
                String creationType = dataTypeDefinition.getCreationType().getName();
                creationTypes.add(creationType);
            }
        }
    }

    /**
     * 加载子页面
     *
     * @param viewPath  视图路径
     * @param viewPaths 视图路径集合
     * @throws IOException
     * @throws XmlParseException
     */
    private void loadSubViewAndImportViewPath(String viewPath, Set<String> viewPaths) throws IOException, XmlParseException {
        viewPath = StringUtil.replace(viewPath, ".", "/");
        viewPath = viewPath + ".view.xml";
        viewPaths.add(viewPath);
        Context contextPath = Context.getCurrent();
        com.bstek.dorado.core.io.Resource resourcePath = contextPath.getResource(viewPath);
        XmlDocument documentPath = xmlBuilder.buildDocument(resourcePath.getInputStream());
        XmlNode[] subViewPaths = documentPath.getNodesByXPath("//SubViewHolder");
        for (XmlNode subViewNode : subViewPaths) {
            XmlNode[] pathNodes = subViewNode.getNodesByXPath("./Property[@name='subView']");
            for (XmlNode pathNode : pathNodes) {
                String path = pathNode.getContent();
                path = StringUtil.replace(path, ".", "/") + ".view.xml";
                viewPaths.add(path);
            }
        }

        XmlNode[] importPaths = documentPath.getNodesByXPath("//Import");

        for (XmlNode importPath : importPaths) {
            String src = importPath.getAttribute("src");
            DoradoContext doradoContext = DoradoContext.getCurrent();
            ExpressionHandler expressionHandler = null;
            try {
                expressionHandler = (ExpressionHandler) doradoContext.getServiceBean("expressionHandler");
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            Expression expression = expressionHandler.compile(src);

            if (expression != null) {
                src = (String) expression.evaluate();
            }
            String path = src.split("#")[0];
            path = StringUtil.replace(path, ".", "/") + ".view.xml";
            viewPaths.add(path);
        }

    }

    /**
     * 获取视图类路径
     *
     * @param url 请求路径
     * @return 视图类路径
     */
    private String getUrlPath(String url) {
        String path = "";
        if (url.indexOf("com.innofi") > -1) {
            path = url.substring(url.indexOf("com.innofi"));
        } else if (url.indexOf("com/innofi") > -1) {
            path = url.substring(url.indexOf("com/innofi"));
        }
        return path;
    }

    /**
     * 翻译页面中的字段中文信息，输入框提示、输入移入的tip提示信息
     *
     * @param creationTypes 当前页面用到的creationTypes集合
     * @throws ClassNotFoundException
     */
    private void translateFieldLabel(Set<String> creationTypes) throws ClassNotFoundException {
        IMdTableService mdTableService = ContextHolder.getSpringBean("mdTableService");
        IMdFieldService mdFieldService = ContextHolder.getSpringBean("mdFieldService");
        for (Iterator it = creationTypes.iterator(); it.hasNext(); ) {
            String creationType = it.next().toString();
            Class clazz = Class.forName(creationType);

            PersistentClass persistentClass = daoSupport.getHibernateDao().getPersistenceClass(clazz);
            if (persistentClass != null) {
                MdTable mdTable = mdTableService.findLastVersionMdTableByTableName(persistentClass.getTable().getName().toUpperCase());
                if (mdTable == null) {
                    //throw new BusinessRuntimeException(MessageFormat.format("在MD_TABLE数据表中未找EntityName为{0}的数据表，元数据数据不完整，请补充相应元数据!", creationType));
                } else {
                    String tableId = mdTable.getId();
                    List<MdField> fields = mdFieldService.findMdFieldsByTableId(tableId);
                    if (fields.size() == 0) {
                        throw new BusinessRuntimeException(MessageFormat.format("在MD_FIELD数据表中未找表名为{0}的字段数据，元数据数据不完整，请补充相应元数据!", mdTable.getTableName()));
                    } else {
                        for (MdField mdField : fields) {
                            UIFieldPojo uiFieldObject = new UIFieldPojo();
                            uiFieldObject.setPropertyBlankText(mdField.getBankTextInf() == null ? "" : mdField.getBankTextInf());
                            uiFieldObject.setPropertyLabel(mdField.getLabelInf() == null ? "" : mdField.getLabelInf());
                            uiFieldObject.setPropertyName(mdField.getEntityAttriName() == null ? "" : mdField.getEntityAttriName());
                            uiFieldObject.setPropertyTip(mdField.getTipInf() == null ? "" : mdField.getTipInf());
                            DoradoContext.getCurrent().setAttribute(DoradoContext.REQUEST, creationType + "." + uiFieldObject.getPropertyName(), uiFieldObject);
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取DataType对应的数据表名称
     *
     * @param keyValue tableName=value
     * @return Pair对象 L tableName R 表名
     */
    private Pair<String, String> getTableName(String keyValue) {
        String[] keyTableName = keyValue
                .split(FrameworkConstants.TAG_PARAMETER_SEPARATE);
        Assert.state(2 == keyTableName.length, "动态对象参数格式定义错误！");
        Pair<String, String> keyTableNamePair = new Pair<String, String>(
                keyTableName[0], keyTableName[1]);
        return keyTableNamePair;
    }

    /**
     * 添加EntityDataType的Property定义
     *
     * @param entityDataType 实体数据类型
     * @throws Exception
     */
    private void addEntityDataTypePropertyDef(View view, EntityDataType entityDataType, DataTypeDefinition dataTypeDefinition) throws Exception {
        IMdFieldService mdFieldService = ContextHolder.getSpringBean("mdFieldService");
        DataTypeManager dataTypeManager = ContextHolder.getSpringBean("dorado.dataTypeManager");
        if (entityDataType.getTags() != null) {
            String[] tags = entityDataType.getTags().split(FrameworkConstants.TAG_JOIN_SEPARATE);
            for (int i = 0; i < tags.length; i++) {
                String tag = tags[i];
                Pair<String, String> keyTalbe = getTableName(tag);
                List<MdField> fields = mdFieldService.findLastVersionMdFieldsByTableName(keyTalbe.getRightObject());
                String entityName = StringUtil.upperFirst(StringUtil.convertPropertyName(keyTalbe.getRightObject()));
                AutoForm addAutoForm = null;
                AutoForm updateAutoForm = null;
                AutoForm lookAutoForm = null;
                if (view != null) {
                    addAutoForm = (AutoForm) view.getComponent("addAutoForm" + entityName);
                    updateAutoForm = (AutoForm) view.getComponent("autoForm" + entityName);
                    lookAutoForm = (AutoForm) view.getComponent("lookForm" + entityName);
                }
                if (fields.size() > 0) {

                    Set fieldNameSet = new HashSet();

                    if (dataTypeDefinition != null && dataTypeDefinition.getPropertyDefs() != null) {
                        Map<String, PropertyDefDefinition> propertyDefs = dataTypeDefinition.getPropertyDefs();
                        fieldNameSet = propertyDefs.keySet();
                    } else if (entityDataType != null && entityDataType.getPropertyDefs() != null) {
                        Map<String, PropertyDef> propertyDefs = entityDataType.getPropertyDefs();
                        fieldNameSet = propertyDefs.keySet();
                    }
                    for (MdField mdField : fields) {
                        if (fieldNameSet != null && !fieldNameSet.contains(mdField.getEntityAttriName()) && mdField.getIsExtend() != null && mdField.getIsExtend().equals(FrameworkConstants.COMM_Y)) {
                            BasePropertyDef basePropertyDef = new BasePropertyDef();
                            basePropertyDef.setName(mdField.getEntityAttriName());
                            String doradoType = SqlUtil.getDroadoType(mdField.getFieldType());
                            if (Validator.isNotEmpty(doradoType)) {
                                basePropertyDef.setDataType(dataTypeManager.getDataType(doradoType));
                            }
                            if (Validator.isNotEmpty(mdField.getIsNull())) {
                                basePropertyDef.setRequired(false);
                            }

                            if (Validator.isNotEmpty(mdField.getLabelInf())) {
                                basePropertyDef.setLabel(mdField.getLabelInf());
                            }
                            entityDataType.addPropertyDef(basePropertyDef);
                            if (addAutoForm != null) {
                                AutoFormElement element = new AutoFormElement();
                                element.setProperty(mdField.getEntityAttriName());
                                addAutoForm.addElement(element);
                            }
                            if (updateAutoForm != null) {
                                AutoFormElement element = new AutoFormElement();
                                element.setProperty(mdField.getEntityAttriName());
                                updateAutoForm.addElement(element);
                            }
                            if (lookAutoForm != null) {
                                AutoFormElement element = new AutoFormElement();
                                element.setProperty(mdField.getEntityAttriName());
                                lookAutoForm.addElement(element);
                            }
                        }
                    }
                }
            }
        }
    }

}
