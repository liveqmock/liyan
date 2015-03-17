package com.innofi.component.sie.action;

import com.innofi.component.sie.SieConstants;
import com.innofi.component.sie.pojo.WfOperateStatus;
import com.innofi.component.sie.pojo.WfProcess;
import com.innofi.component.sie.service.IWfOperateStatusService;
import com.innofi.component.sie.service.IWfProcessService;
import com.innofi.framework.FrameworkConstants;
import com.innofi.component.rbac.user.pojo.SysUser;
import com.innofi.framework.service.IBaseService;

import com.innofi.framework.action.impl.BaseActionImpl;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.spring.context.ContextHolder;
import com.innofi.framework.utils.string.StringUtil;


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
 *          todo 为dorado7界面维护操作的提供支持，实现WfOperateStatus对象信息的加载与保存操作
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
@Component
public class WfOperateStatusAction extends BaseActionImpl {
    @Resource
    private IWfOperateStatusService wfOperateStatusSevice;

    public IBaseService getBusinessService() {
        return wfOperateStatusSevice;
    }

    @DataProvider
    public void findWfOperateStatuss(Page page, Map<String, Object> parameter) throws Exception {

        List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();

        if (parameter != null) {
            addPropertyFilter(propertyFilters, "operDesc", parameter.get("operDesc"), (String) parameter.get("qMoperDesc"), true);
            addPropertyFilter(propertyFilters, "operStatus", parameter.get("operStatus"), (String) parameter.get("qMoperStatus"), true);
            addPropertyFilter(propertyFilters, "id", parameter.get("id"), (String) parameter.get("qMid"), true);
            addPropertyFilter(propertyFilters, "operType", parameter.get("operType"), (String) parameter.get("qMoperType"), true);
            addPropertyFilter(propertyFilters, "status", parameter.get("status"), (String) parameter.get("qMstatus"), true);
        }

        com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);

        wfOperateStatusSevice.findByPage_Filters(propertyFilters, null, innofiPage);

        if (parameter.get("operStatus") != null && parameter.get("operStatus").equals(SieConstants.BUSI_STATUS_WAIT_ADUIT)) {
            IWfProcessService wfProcessSevice = this.getSpringBean("wfProcessService");
            SysUser sysUser = ContextHolder.getContext().getLoginUser();
            Map queryParameters = new HashMap();
            String moduleCode = (String) parameter.get("moduleCode");
            String businessObjectId = (String) parameter.get("businessObjectId");
            if(businessObjectId.indexOf(",")!=-1){
        		String[] businessIds = StringUtil.split(businessObjectId);
        		queryParameters.put("businessObjectId", businessIds[0]);
        	}else{
        		queryParameters.put("businessObjectId", businessObjectId);
        	}
            
            queryParameters.put("moduleCode", moduleCode);
            
            queryParameters.put("isNew", FrameworkConstants.COMM_Y);
            queryParameters.put("operOrgCode", sysUser.getOrgCode());
            queryParameters.put("roleList", sysUser.getFunctionRoleIds().split(","));
            queryParameters.put("operUserCode", sysUser.getUserCode());
            WfProcess wfProcess = (WfProcess)wfProcessSevice.findUniqueByNamedHql(" from WfProcess w where w.moduleCode=:moduleCode and w.businessId=:businessObjectId and w.isNew=:isNew and (w.operOrgCode=:operOrgCode or w.operOrgCode is null ) and (w.operRoleCode in (:roleList) or w.operRoleCode is null) and (w.operUserCode = :operUserCode or w.operUserCode is null)", queryParameters);
            WfOperateStatus wfCancelOperate = null;
            for (Iterator it = page.getEntities().iterator(); it.hasNext(); ) {
                WfOperateStatus wfOperateStatus = (WfOperateStatus) it.next();
                if (wfOperateStatus.getOperType().equals(SieConstants.OPER_TYPE_CANCEL)) {
                    wfCancelOperate = wfOperateStatus;
                }
            }
            if (wfProcess == null) {
                if (wfCancelOperate != null) {
                    page.getEntities().clear();
                    page.getEntities().add(wfCancelOperate);
                }
            } else {
                if (wfCancelOperate != null) {
                    page.getEntities().remove(wfCancelOperate);
                }
            }
        }


    }

    @DataResolver
    public void saveWfOperateStatuss(Collection<WfOperateStatus> objs) {
		for (Iterator iterator = objs.iterator(); iterator.hasNext();) {
			WfOperateStatus wfOperateStatus = (WfOperateStatus) iterator.next();
			EntityState state = EntityUtils.getState(wfOperateStatus);
			if (EntityState.DELETED.equals(state)) {
				wfOperateStatusSevice.delete(wfOperateStatus);
			} else if (EntityState.MODIFIED.equals(state) || EntityState.MOVED.equals(state)) {
				wfOperateStatusSevice.update(wfOperateStatus);
			} else if (EntityState.NEW.equals(state)) {
				wfOperateStatusSevice.save(wfOperateStatus);
			}
		}
    }

}
