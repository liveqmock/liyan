package com.innofi.component.rbac.privilage.action;

import com.innofi.component.rbac.privilage.pojo.SysFunAction;
import com.innofi.component.rbac.privilage.service.ISysFunActionService;

import com.innofi.framework.action.impl.BaseActionImpl;
import com.innofi.component.cache.service.ISysCacheConfigService;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.QueryConstants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import java.util.Map;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.view.config.ViewConfigDefinitionFactory;
import com.bstek.dorado.view.config.definition.ComponentDefinition;
import com.bstek.dorado.view.config.definition.ControlDefinition;
import com.bstek.dorado.view.config.definition.ViewConfigDefinition;
import com.bstek.dorado.view.config.definition.ViewDefinition;


/**
 * 为dorado7界面维护操作的提供支持，实现SysFunAction对象信息的加载与保存操作
 */

@Component
public class SysFunActionAction extends BaseActionImpl {
    @Resource
    private ISysFunActionService sysFunActionService;

    @DataProvider
    public void findSysFunActions(Page page, Map<String, Object> parameter) throws Exception {

        List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();

        if (parameter != null) {
        }

        com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);

        sysFunActionService.findByPage_Filters(propertyFilters, null, innofiPage);

    }

    @DataProvider
    public List<SysFunAction> findActionByMenuId(String menuId) throws Exception {
    	if(menuId==null)return new ArrayList();
        return sysFunActionService.findByProperty("menuId", menuId, QueryConstants.EQUAL);

    }

    @DataProvider
    public List<SysFunAction> findActionByMenuUrl(Map<String, String> parameters) {
        List<SysFunAction> actionInfos = new ArrayList<SysFunAction>();
        List<SysFunAction> actionInfos1 = new ArrayList<SysFunAction>();
        try {
            String menuUrl2 = parameters.get("menuUrl");
            String menuUrl = menuUrl2.substring(0, menuUrl2.lastIndexOf(".d"));
            String menuId = parameters.get("menuId");
            Map params = new HashMap();
            params.put("menuId", menuId);
            List<SysFunAction> entities = sysFunActionService.findByProperty("menuId", menuId, QueryConstants.EQUAL);

            ViewConfigDefinitionFactory viewConfigDefinitionFactory = getSpringBean("dorado.xmlViewConfigDefinitionFactory");

            ViewConfigDefinition viewConfigDefinition = viewConfigDefinitionFactory.create(menuUrl);
            ViewDefinition viewDefinition = viewConfigDefinition.getViewDefinition();
            Map<String, ComponentDefinition> components = viewDefinition.getComponents();

            for (Iterator<String> iterator = components.keySet().iterator(); iterator.hasNext(); ) {
                String id = iterator.next();
                ComponentDefinition componentDefinition = components.get(id);

                if (componentDefinition != null && componentDefinition.getComponentType().equals("ToolBar")) {
                    if (componentDefinition instanceof ControlDefinition) {
                        ControlDefinition toolbar1 = (ControlDefinition) componentDefinition;
                        if (!StringUtils.isBlank((String) toolbar1.getProperty("tags")) && toolbar1.getProperty("tags").equals("tags_toolbar")) {
                            List<ControlDefinition> items = (List<ControlDefinition>) toolbar1.getProperty("items");
                            if (items != null) {
                                for (int i = 0; i < items.size(); i++) {
                                    ControlDefinition button = items.get(i);
                                    String buttonId = (String) button.getId();
                                    String caption = (String) button.getProperty("caption");

                                    if (StringUtils.isNotBlank(buttonId)) {
                                        SysFunAction actionInfo = new SysFunAction();
                                        actionInfo.setActionCode(buttonId);
                                        actionInfo.setActionName(caption);
                                        actionInfos.add(actionInfo);
                                    }

                                }
                            }
                        }
                    }
                }

            }
            for (SysFunAction actionInfo : actionInfos) {
                for (SysFunAction action : entities) {
                    if (actionInfo.getActionCode().equals(action.getActionCode())) {
                        actionInfo.setSelectFlag(true);
                        break;
                    }
                }
            }
            for (SysFunAction actionInfo : actionInfos) {

                if (!actionInfo.getSelectFlag()) {
                    actionInfos1.add(actionInfo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return actionInfos1;
    }

    @DataResolver
    public void saveSysFunActions(Collection<SysFunAction> objs) {
		for (Iterator iterator = objs.iterator(); iterator.hasNext(); ) {
			SysFunAction sysFunAction = (SysFunAction) iterator.next();
            EntityState state = EntityUtils.getState(sysFunAction);
            if (EntityState.DELETED.equals(state)) {
            	sysFunActionService.delete(sysFunAction);
            } else if (EntityState.MODIFIED.equals(state) || EntityState.MOVED.equals(state)) {
            	sysFunActionService.update(sysFunAction);
            } else if (EntityState.NEW.equals(state)) {
            	sysFunActionService.save(sysFunAction);
            }
		}
    	ISysCacheConfigService sysCacheConfigService = getSpringBean("sysCacheConfigService");
    	sysCacheConfigService.reloadCacheByCacheServiceBeanName("sysFunActionService");
    }
}
