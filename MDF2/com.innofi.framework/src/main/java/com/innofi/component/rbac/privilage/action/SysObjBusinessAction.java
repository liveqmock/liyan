package com.innofi.component.rbac.privilage.action;

import com.innofi.component.codetransfer.IdfCodeTransfer;
import com.innofi.framework.service.IBaseService;
import com.innofi.component.rbac.privilage.pojo.SysObjBusiness;
import com.innofi.component.rbac.privilage.service.ISysObjBusinessService;

import com.innofi.framework.action.impl.BaseActionImpl;
import com.innofi.framework.dao.hibernate.PropertyFilter;


import java.util.*;

import javax.annotation.Resource;

import com.innofi.framework.spring.context.ContextHolder;
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
 *          todo 为dorado7界面维护操作的提供支持，实现SysObjBusiness对象信息的加载与保存操作
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
@Component
public class SysObjBusinessAction extends BaseActionImpl {
    @Resource
    private ISysObjBusinessService sysObjBusinessSevice;

    public IBaseService getBusinessService() {
        return sysObjBusinessSevice;
    }

    @DataProvider
    public void findSysObjBusinesss(Page page, Map<String, Object> parameter) throws Exception {

        List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();

        if (parameter != null) {
            addPropertyFilter(propertyFilters, "busilineId", parameter.get("busilineId"), (String) parameter.get("qMbusilineId"), true);
            addPropertyFilter(propertyFilters, "id", parameter.get("id"), (String) parameter.get("qMid"), true);
            addPropertyFilter(propertyFilters, "objBusilineType", parameter.get("objBusilineType"), (String) parameter.get("qMobjBusilineType"), true);
            addPropertyFilter(propertyFilters, "objCode", parameter.get("objCode"), (String) parameter.get("qMobjCode"), true);
            addPropertyFilter(propertyFilters, "objId", parameter.get("objId"), (String) parameter.get("qMobjId"), true);
        }

        com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);

        sysObjBusinessSevice.findByPage_Filters(propertyFilters, null, innofiPage);

        IdfCodeTransfer codeTransfer = ContextHolder.getSpringBean("mdf.codeTransfer");
        Map map = new HashMap();
        map.put("busilineId", "busilineName");

        codeTransfer.transferResult(page.getEntities(), "sysBusinessLineService", map);

    }

    @DataResolver
    public void saveSysObjBusinesss(Collection<SysObjBusiness> objs) {
		for (Iterator iterator = objs.iterator(); iterator.hasNext();) {
			SysObjBusiness sysObjBusiness = (SysObjBusiness) iterator.next();
			EntityState state = EntityUtils.getState(sysObjBusiness);
			if (EntityState.DELETED.equals(state)) {
				sysObjBusinessSevice.delete(sysObjBusiness);
			} else if (EntityState.MODIFIED.equals(state)
					|| EntityState.MOVED.equals(state)) {
				sysObjBusinessSevice.update(sysObjBusiness);
			} else if (EntityState.NEW.equals(state)) {
				sysObjBusinessSevice.save(sysObjBusiness);
			}
		}
    }

}
