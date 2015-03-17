package com.innofi.component.sie.action;

import com.innofi.component.codetransfer.IdfCodeTransfer;
import com.innofi.framework.service.IBaseService;
import com.innofi.component.sie.pojo.WfProcessCfg;
import com.innofi.component.sie.service.IWfProcessCfgService;

import com.innofi.framework.action.impl.BaseActionImpl;
import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.PropertyOrder;
import com.innofi.framework.dao.hibernate.QueryConstants;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
 * @author todo 开发人员姓名 邮箱
 * @version todo 当前版本 日期
 *          todo 为dorado7界面维护操作的提供支持，实现WfProcessCfg对象信息的加载与保存操作
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
@Component
public class WfProcessCfgAction extends BaseActionImpl {
    @Resource
    private IWfProcessCfgService wfProcessCfgSevice;

    public IBaseService getBusinessService() {
        return wfProcessCfgSevice;
    }

    @DataProvider
    public void findWfProcessCfgs(Page page, Map<String, Object> parameter) throws Exception {

        List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();

        if (parameter != null) {
            addPropertyFilter(propertyFilters, "isEnd", parameter.get("isEnd"), (String) parameter.get("qMisEnd"), true);
            addPropertyFilter(propertyFilters, "isUserOrg", parameter.get("isUserOrg"), (String) parameter.get("qMisUserOrg"), true);
            addPropertyFilter(propertyFilters, "moduleCode", parameter.get("moduleCode"), (String) parameter.get("qMmoduleCode"), true);
            addPropertyFilter(propertyFilters, "moduleName", parameter.get("moduleName"), (String) parameter.get("qMmoduleName"), true);
            addPropertyFilter(propertyFilters, "id", parameter.get("id"), (String) parameter.get("qMid"), true);
            addPropertyFilter(propertyFilters, "operOrgCode", parameter.get("operOrgCode"), (String) parameter.get("qMoperOrgCode"), true);
            addPropertyFilter(propertyFilters, "operRole", parameter.get("operRole"), (String) parameter.get("qMoperRole"), true);
            addPropertyFilter(propertyFilters, "operUserCode", parameter.get("operUserCode"), (String) parameter.get("qMoperUserCode"), true);
            addPropertyFilter(propertyFilters, "prevNodeId", parameter.get("prevNodeId"), (String) parameter.get("qMprevNodeId"), true);
            addPropertyFilter(propertyFilters, "status", parameter.get("status"), (String) parameter.get("qMstatus"), true);
            addPropertyFilter(propertyFilters, "orgLevel", parameter.get("orgLevel"), (String) parameter.get("qMorgLevel"), true);
        }

        com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);

        List<PropertyOrder> proertyOrders = new ArrayList();
        proertyOrders.add(new PropertyOrder("moduleCode", QueryConstants.ORDER_ASC));
        proertyOrders.add(new PropertyOrder("orgLevel", QueryConstants.ORDER_ASC));
        proertyOrders.add(new PropertyOrder("crtDate", QueryConstants.ORDER_ASC));
        proertyOrders.add(new PropertyOrder("prevNodeId", QueryConstants.ORDER_ASC));

        wfProcessCfgSevice.findByPage_Filters(propertyFilters, null, innofiPage);

        IdfCodeTransfer codeTransfer = wfProcessCfgSevice.getIdfCodeTransfer();
        Map map = new HashMap();
        map.put("operOrgCode", "operOrgCodeName");
        codeTransfer.transferResult(page.getEntities(), "sysOrgInfoService", map);
        map = new HashMap();
        map.put("operUserCode", "operUserCodeName");
        codeTransfer.transferResult(page.getEntities(), "sysUserService", map);
        map = new HashMap();
        map.put("operRoleCode", "operRoleCodeName");
        codeTransfer.transferResult(page.getEntities(), "sysRoleService", map);
    }
    
    
	@DataProvider
	public List<Map<String, Object>> findWfProcessCfgMapping() throws Exception {
		String sql = "select NODE_ID key, NODE_NAME value from WF_PROCESS_CFG t ";
		DaoSupport daoSupport = this.getSpringBean("daoSupport");
		return daoSupport.getJdbcDao().queryForList(sql);
	}
    

    @DataResolver
    public void saveWfProcessCfgs(Collection<WfProcessCfg> objs) {
		for (Iterator iterator = objs.iterator(); iterator.hasNext();) {
			WfProcessCfg wfProcessCfg = (WfProcessCfg) iterator.next();
			EntityState state = EntityUtils.getState(wfProcessCfg);
			if (EntityState.DELETED.equals(state)) {
				wfProcessCfgSevice.delete(wfProcessCfg);
			} else if (EntityState.MODIFIED.equals(state) || EntityState.MOVED.equals(state)) {
				wfProcessCfgSevice.update(wfProcessCfg);
			} else if (EntityState.NEW.equals(state)) {
				wfProcessCfgSevice.save(wfProcessCfg);
			}
		}
    }

}
