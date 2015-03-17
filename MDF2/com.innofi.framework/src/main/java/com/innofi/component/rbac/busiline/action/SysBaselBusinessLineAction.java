package com.innofi.component.rbac.busiline.action;
import com.bstek.dorado.data.entity.EntityState;
import com.innofi.component.rbac.busiline.pojo.SysBaselBusinessLine;
import com.innofi.component.rbac.busiline.service.ISysBaselBusinessLineService;

import com.innofi.framework.FrameworkConstants;
import com.innofi.framework.action.impl.BaseActionImpl;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.QueryConstants;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import java.util.Map;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;


/**
 * 为dorado7界面维护操作的提供支持，实现SysBaselBusinessLine对象信息的加载与保存操作
 */

@Component
public class SysBaselBusinessLineAction extends BaseActionImpl {	
    @Resource
	private   ISysBaselBusinessLineService sysBaselBusinessLineService;

    /**
     * 业务线管理列表
     * @param page
     * @param parameter
     * @throws Exception
     */
    @DataProvider
    public void findSysBaselBusinessLines(Page page, Map<String, Object> parameter) throws Exception {
    	List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
    	if(parameter != null){
    		addPropertyFilter(propertyFilters,"parentId",parameter.get("parentId"),QueryConstants.EQUAL,true);  
		}
        com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
        sysBaselBusinessLineService.findByPage_Filters(propertyFilters, null, innofiPage);
    }

    /**
     * 业务线管理树子节点
     * @param parentId
     * @return
     * @throws Exception
     */
    @DataProvider
    public List<SysBaselBusinessLine> findSysBaselBusinessLineByParentId(String parentId) throws Exception {
        return sysBaselBusinessLineService.findByProperty("parentId", parentId, QueryConstants.EQUAL);
    }
    
    /**
     * 业务线下拉框树形结构
     * @param parameter
     * @return
     * @throws Exception
     */
    @DataProvider
    public List<SysBaselBusinessLine> findSysBaselBusinessLines(Map<String, Object> parameter) throws Exception {
    	List<PropertyFilter> filters=this.buildEqPropertyFilters(parameter);
        return sysBaselBusinessLineService.findByPropertyFilters(filters, null);
    }
    
    /**
     *下拉框列表 
     * @param page
     * @param parameter
     * @throws Exception
     */
    @DataProvider
    public void findSysBaselBusinessLineList(Page page,Map<String, Object> parameter) throws Exception {
    	List<PropertyFilter> propertyFilters = super.buildEqPropertyFilters(parameter);
    	this.removePropertyFilter(propertyFilters, "searchType");
    	addPropertyFilter(propertyFilters, "status", FrameworkConstants.STATUS_EFFECTIVE, QueryConstants.EQUAL, true);//状态有效
    	addPropertyFilter(propertyFilters,"busilineBaselName",parameter.get("busilineBaselName"),QueryConstants.LIKE,true);
    	com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
    	sysBaselBusinessLineService.findSysBaselBusinessLineList((String) parameter.get("searchType"), (String) parameter.get("parentId"),propertyFilters, innofiPage);
    }
    
    @DataResolver
    public void saveSysBaselBusinessLines(Collection<SysBaselBusinessLine> objs) {
    	for(SysBaselBusinessLine line:objs){
    		Collection<SysBaselBusinessLine> children=EntityUtils.getValue(line, "children");
    		if(children!=null && children.size()>0){
    			saveSysBaselBusinessLines(children);
    		}
            EntityState state = EntityUtils.getState(line);
            if (EntityState.NEW.equals(state)) {
                sysBaselBusinessLineService.save(line);
            } else if (EntityState.MODIFIED.equals(state)) {
                sysBaselBusinessLineService.update(line);
            } else if (EntityState.DELETED.equals(state)) {
                sysBaselBusinessLineService.delete(line);
            }
    	}
    }
    
    /**
     * 检查编号
     */
    @Expose
    public String checkBusilineBaselCode(String code) {
        return sysBaselBusinessLineService.checkEntityExist("busilineBaselCode", code)+"";
    }
    
    /**
     * 停用业务线
     */
    @Expose
    public String invalidBizLine(String code) {
        return sysBaselBusinessLineService.invalidBizLine(code);
    }
}
