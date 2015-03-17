package com.innofi.component.rbac.job.action;
import com.innofi.component.rbac.job.pojo.SysJobTodo;
import com.innofi.component.rbac.job.service.ISysJobTodoService;

import com.innofi.framework.action.impl.BaseActionImpl;
import com.innofi.framework.dao.hibernate.PropertyFilter;


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
 *          todo 为dorado7界面维护操作的提供支持，实现SysJobTodo对象信息的加载与保存操作
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
@Component
public class SysJobTodoAction extends BaseActionImpl {
    @Resource
	private   ISysJobTodoService sysJobTodoService;

    @DataProvider
    public void findSysJobTodos(Page page, Map<String, Object> parameter) throws Exception {
    	List<PropertyFilter> propertyFilters = this.buildEqPropertyFilters(parameter);
        com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
        sysJobTodoService.findByPage_Filters(propertyFilters, null, innofiPage);
    }
    
    @DataProvider
    public void findSysJobTodoByUserRole(Page page,Map<String, Object> parameter) throws Exception {
        com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
        sysJobTodoService.findSysJobTodoByUserRole((String) parameter.get("id"),innofiPage);
    }
    
    @DataResolver
    public void saveSysJobTodos(Collection<SysJobTodo> objs) {
		for (Iterator iterator = objs.iterator(); iterator.hasNext(); ) {
			SysJobTodo sysJobTodo = (SysJobTodo) iterator.next();
            EntityState state = EntityUtils.getState(sysJobTodo);
            if (EntityState.DELETED.equals(state)) {
            	sysJobTodoService.delete(sysJobTodo);
            } else if (EntityState.MODIFIED.equals(state) || EntityState.MOVED.equals(state)) {
            	sysJobTodoService.update(sysJobTodo);
            } else if (EntityState.NEW.equals(state)) {
            	sysJobTodoService.save(sysJobTodo);
            }
		}
    }
}
