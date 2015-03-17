package com.innofi.component.rbac.log.action;
import com.innofi.component.rbac.log.pojo.SysLog;
import com.innofi.component.rbac.log.service.ISysLogService;

import com.innofi.framework.action.impl.BaseActionImpl;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.PropertyOrder;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import java.util.Map;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;


/**
 * 功能/ 模块：todo
 *
 * @author  todo 开发人员姓名 邮箱
 * @version todo 当前版本 日期
 *          todo 为dorado7界面维护操作的提供支持，实现SysLog对象信息的加载与保存操作
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
@Component
public class SysLogAction extends BaseActionImpl {
    @Resource
	private   ISysLogService sysLogService;

    @DataProvider
    public void findSysLogs(Page page, Map<String, Object> parameter) throws Exception {

    List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
    List<PropertyOrder> orderProperty = new ArrayList<PropertyOrder>();

    	if(parameter != null){
    			    	addPropertyFilter(propertyFilters,"orgName",parameter.get("orgName"),parameter.get("qMorgName").toString(),true);
														    	addPropertyFilter(propertyFilters,"logType",parameter.get("logType"),parameter.get("qMlogType").toString(),true);
														    	addPropertyFilter(propertyFilters,"operType",parameter.get("operType"),parameter.get("qMoperType").toString(),true);
								    	addPropertyFilter(propertyFilters,"userName",parameter.get("userName"),parameter.get("qMuserName").toString(),true);
																		addDateRangePropertyFilter(propertyFilters, "operateDate", parameter);
																																																																		
    	}
    	
    	PropertyOrder order = new PropertyOrder("operateDate", "desc");
    	orderProperty.add(order);
        com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
        
        sysLogService.findByPage_Filters(propertyFilters, orderProperty, innofiPage);

    }

    @DataResolver
    public void saveSysLogs(Collection<SysLog> objs) {
		for (Iterator iterator = objs.iterator(); iterator.hasNext();) {
			SysLog sysLog = (SysLog) iterator.next();
			EntityState state = EntityUtils.getState(sysLog);
			if (EntityState.DELETED.equals(state)) {
				sysLogService.delete(sysLog);
			} else if (EntityState.MODIFIED.equals(state) || EntityState.MOVED.equals(state)) {
				sysLogService.update(sysLog);
			} else if (EntityState.NEW.equals(state)) {
				sysLogService.save(sysLog);
			}
		}
    }
}
