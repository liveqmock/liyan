package com.innofi.component.rbac.menu.action;

import com.innofi.component.cache.service.ISysCacheConfigService;
import com.innofi.component.rbac.menu.pojo.SysMenu;
import com.innofi.component.rbac.menu.service.ISysMenuService;

import com.innofi.framework.action.impl.BaseActionImpl;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.PropertyOrder;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.framework.spring.context.ContextHolder;



import java.io.File;
import java.util.*;

import javax.annotation.Resource;

import com.innofi.framework.utils.validate.Validator;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;


/**
 * 为dorado7界面维护操作的提供支持，实现SysMenu对象信息的加载与保存操作
 */

@Component
public class SysMenuAction extends BaseActionImpl {	
    @Resource
	private   ISysMenuService sysMenuService;

    @DataProvider
    public void findSysMenus(Page page, Map<String, Object> parameter) throws Exception {

        List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
	 
    	if(parameter != null){
    																																																																																																																					}
	 
        com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);

        sysMenuService.findByPage_Filters(propertyFilters, null, innofiPage);

    }

    @DataProvider
    public List<Map> findAppsByRoles()throws Exception{
        List<Map> apps = new ArrayList<Map>();
        List<SysMenu> sysMenuList = sysMenuService.findApps();
        for(SysMenu sysMenu : sysMenuList){
            Map app = new HashMap();
            app.put("name", sysMenu.getMenuLabel());
            app.put("image", sysMenu.getMenuPict());
            apps.add(app);
        }
        return apps;
    }


    @DataProvider
    public List<SysMenu> findSysMenusByParentId(String parentMenuId) throws Exception {
        List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
        PropertyOrder order = new PropertyOrder("menuSeq", QueryConstants.ORDER_ASC);
        orders.add(order);
        return sysMenuService.findByProperty("parentMenuId", parentMenuId, QueryConstants.EQUAL,orders);
    }

    @DataProvider
    public List<SysMenu> findSysMenusByParameter(Map parameter) throws Exception {
        List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
        List sysMenus = new ArrayList();
        if(Validator.isNotNull(parameter.get("menuName"))){
            sysMenus = sysMenuService.findByProperty("menuName", parameter.get("menuName"), QueryConstants.LIKE,orders);
        }else if(Validator.isNotNull(parameter.get("parentMenuId"))){
            sysMenus = findSysMenusByParentId((String) parameter.get("parentMenuId"));
        }
        return sysMenus;
    }
    
    @DataProvider
    public List<SysMenu> findSysMenusByRoles(String parentMenuId) throws Exception {
        return sysMenuService.findModules(parentMenuId);
    }

    @DataProvider
    public List<SysMenu> findImagesByPath(String imagePath) throws Exception {
    	
    	String rootPath=ContextHolder.getContext().getDoradoContext().getServletContext().getRealPath("/");
    	String iconPath="";
    	if(StringUtils.isBlank(imagePath) || imagePath.equals("null")){
    		iconPath=rootPath+"/icons/32";
    		imagePath="icons/32";
    	}else{
    		while(imagePath.startsWith("/") || imagePath.startsWith("\\")){
    			imagePath=imagePath.substring(1);
        	}
        	while(imagePath.endsWith("/") || imagePath.endsWith("\\")){
        		imagePath=imagePath.substring(0,imagePath.length()-1);
        	}
        	iconPath=rootPath+File.separator+imagePath;
    	}
    	
    	File file=new File(iconPath);
    	String[] fileNames=file.list();
    	List<SysMenu> menus=new ArrayList<SysMenu>();
    	for(String fileName:fileNames){
    		if(fileName.endsWith(".png")){
    			SysMenu menu=new SysMenu();
        		menu.setMenuName(fileName);
        		menu.setMenuCode(fileName.substring(0,fileName.indexOf(".png")));
            	menu.setMenuPict(">"+imagePath+"/"+fileName);
            	menus.add(menu);
    		}
    	}
        return menus;
    }

    @DataResolver
    public void saveSysMenus(Collection<SysMenu> objs) {
    	for (Iterator iterator = objs.iterator(); iterator.hasNext(); ) {
    		SysMenu sysMenu = (SysMenu) iterator.next();
            Collection<SysMenu> children = EntityUtils.getValue(sysMenu, "children");
    		if (children != null && children.size() > 0) {
	        	saveSysMenus(children);
	        }
	        EntityState state = EntityUtils.getState(sysMenu);
	        if (EntityState.NEW.equals(state)) {
	        	sysMenuService.save(sysMenu);
	        } else if (EntityState.MODIFIED.equals(state)) {
	        	sysMenuService.update(sysMenu);
	        } else if (EntityState.DELETED.equals(state)) {
	        	sysMenuService.delete(sysMenu);
	        }
    	}
    	ISysCacheConfigService sysCacheConfigService = getSpringBean("sysCacheConfigService");
    	sysCacheConfigService.reloadCacheByCacheServiceBeanName("sysMenuService");
    }

}
