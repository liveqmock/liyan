package com.innofi.component.rbac.busiline.action;
import com.bstek.dorado.data.entity.EntityState;
import com.innofi.component.rbac.busiline.pojo.SysBusinessLine;
import com.innofi.component.rbac.busiline.service.ISysBusinessLineService;
import com.innofi.framework.FrameworkConstants;
import com.innofi.framework.action.impl.BaseActionImpl;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.PropertyOrder;
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
 * 为dorado7界面维护操作的提供支持，实现SysBusinessLine对象信息的加载与保存操作
 */

@Component
public class SysBusinessLineAction extends BaseActionImpl {	
    @Resource
	private   ISysBusinessLineService sysBusinessLineService;

    /**
     * 业务线管理列表
     * @param page
     * @param parameter
     * @throws Exception
     */
    @DataProvider
    public void findSysBusinessLines(Page page, Map<String, Object> parameter) throws Exception {
    	List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
    	if(parameter != null){
			addPropertyFilter(propertyFilters,"parentBusilineId",parameter.get("parentBusilineId"),QueryConstants.EQUAL,true);
			addPropertyFilter(propertyFilters,"busilineCode",parameter.get("busilineCode"),QueryConstants.EQUAL,true);
			addPropertyFilter(propertyFilters,"busilineName",parameter.get("busilineName"),QueryConstants.EQUAL,true);
			addPropertyFilter(propertyFilters,"belongOrgCode",parameter.get("belongOrgCode"),QueryConstants.EQUAL,true);
			addPropertyFilter(propertyFilters,"busilineBaselId",parameter.get("busilineBaselId"),QueryConstants.EQUAL,true);
		}
        com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
        List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
        orders.add(new PropertyOrder("seq",QueryConstants.ORDER_ASC));
        sysBusinessLineService.findByPage_Filters(propertyFilters, orders, innofiPage);
    }

    /**
     * 业务线管理树子节点
     * @param parentBusilineId
     * @return
     * @throws Exception
     */
    @DataProvider
    public List<SysBusinessLine> findSysBusinessLineByParentId(String parentBusilineId) throws Exception {
        List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
        addPropertyFilter(propertyFilters,"parentBusilineId",parentBusilineId,QueryConstants.EQUAL,true);
        List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
        orders.add(new PropertyOrder("seq",QueryConstants.ORDER_ASC));
        return sysBusinessLineService.findByPropertyFilters(propertyFilters,orders);
    }
    
    /**
     * 业务线下拉框树结构
     * @param parameter
     * @return
     * @throws Exception
     */
    @DataProvider
    public List<SysBusinessLine> findSysBusinessLines(Map<String, Object> parameter) throws Exception {
        List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
        orders.add(new PropertyOrder("seq",QueryConstants.ORDER_ASC));
    	List<PropertyFilter> filters=this.buildEqPropertyFilters(parameter);
        return sysBusinessLineService.findByPropertyFilters(filters, orders);
    }
    
    /**
     * 业务线下拉框列表
     * @param page
     * @param parameter
     * @throws Exception
     */
    @DataProvider
    public void findSysBusinessLineList(Page page,Map<String, Object> parameter) throws Exception {
    	List<PropertyFilter> propertyFilters = super.buildEqPropertyFilters(parameter);
    	this.removePropertyFilter(propertyFilters, "searchType");
    	addPropertyFilter(propertyFilters,"busilineName",parameter.get("busilineName"),QueryConstants.LIKE,true);
    	addPropertyFilter(propertyFilters, "status", FrameworkConstants.STATUS_EFFECTIVE, QueryConstants.EQUAL, true);//状态有效
    	com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
    	sysBusinessLineService.findSysBusinessLineList((String) parameter.get("searchType"), (String) parameter.get("parentBusilineId"),propertyFilters, innofiPage);
    }
    
    @DataResolver
    public void saveSysBusinessLines(Collection<SysBusinessLine> objs) {
    	for(SysBusinessLine line:objs){
    		Collection<SysBusinessLine> children=EntityUtils.getValue(line, "children");
    		if(children!=null && children.size()>0){
    			saveSysBusinessLines(children);
    		}
            EntityState state = EntityUtils.getState(line);
            if (EntityState.NEW.equals(state)) {
                sysBusinessLineService.save(line);
            } else if (EntityState.MODIFIED.equals(state)) {
                sysBusinessLineService.update(line);
            } else if (EntityState.DELETED.equals(state)) {
                sysBusinessLineService.delete(line);
            }
    	}
    }
    
    /**
     * 检查编号
     */
    @Expose
    public String checkBusilineCode(String code) {
        return sysBusinessLineService.checkEntityExist("busilineCode", code)+"";
    }
    
    /**
     * 停用业务线
     */
    @Expose
    public String invalidBizLine(String code) {
        return sysBusinessLineService.invalidBizLine(code);
    }
    
}
