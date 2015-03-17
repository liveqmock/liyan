package com.innofi.component.rbac.notice.action;

import com.innofi.component.rbac.notice.pojo.SysNoticeContent;
import com.innofi.component.rbac.notice.pojo.SysNoticeContentRole;
import com.innofi.component.rbac.notice.service.ISysNoticeContentRoleService;
import com.innofi.component.rbac.notice.service.ISysNoticeContentService;

import com.innofi.framework.action.impl.BaseActionImpl;
import com.innofi.framework.dao.hibernate.PropertyFilter;


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
 * @author todo 开发人员姓名 邮箱
 * @version todo 当前版本 日期
 *          todo 为dorado7界面维护操作的提供支持，实现SysNoticeContent对象信息的加载与保存操作
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
@Component
public class SysNoticeContentAction extends BaseActionImpl {
    @Resource
    private ISysNoticeContentService sysNoticeContentService;
    @Resource
    private ISysNoticeContentRoleService sysNoticeContentRolesevice;

    @DataProvider
    public void findSysNoticeContents(Page page, Map<String, Object> parameter) throws Exception {
        List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
        if (parameter != null) {
            addPropertyFilter(propertyFilters, "noticeNo", parameter.get("noticeNo"), parameter.get("qMnoticeNo").toString(), true);
            addPropertyFilter(propertyFilters, "title", parameter.get("title"), parameter.get("qMtitle").toString(), true);
            addPropertyFilter(propertyFilters, "noticeType", parameter.get("noticeType"), parameter.get("qMnoticeType").toString(), true);
            addDateRangePropertyFilter(propertyFilters, "completeDate", parameter);
            addPropertyFilter(propertyFilters, "busiStatus", parameter.get("busiStatus"), parameter.get("qMbusiStatus").toString(), true);
            addPropertyFilter(propertyFilters, "status", parameter.get("status"), parameter.get("qMstatus").toString(), true);
            addDateRangePropertyFilter(propertyFilters, "releaseDate", parameter);
        }
        com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
        sysNoticeContentService.findByPage_Filters(propertyFilters, null, innofiPage);
    }

    @DataProvider
    public void findSysNoticeContentsByUserRole(Page page) throws Exception {
        com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
        sysNoticeContentService.findSysNoticeContentsByUserRole(innofiPage);
    }

    @DataProvider
    public SysNoticeContent findSysNoticeContentDisp(String id) throws Exception {
        return sysNoticeContentService.get(id);
    }

    @DataResolver
    public void saveSysNoticeContents(Collection<SysNoticeContent> objs) {
        for (Iterator iterator = objs.iterator(); iterator.hasNext(); ) {
            SysNoticeContent notice = (SysNoticeContent) iterator.next();
            Collection<SysNoticeContentRole> noticeRoles = EntityUtils.getValue(notice, "noticeRole");
            EntityState state = EntityUtils.getState(notice);
            if (EntityState.DELETED.equals(state)) {
                sysNoticeContentService.delete(notice);
            } else if (EntityState.MODIFIED.equals(state) || EntityState.MOVED.equals(state)) {
                sysNoticeContentService.update(notice);
            } else if (EntityState.NEW.equals(state)) {
                sysNoticeContentService.save(notice);
            }
            if (noticeRoles != null && noticeRoles.size() > 0) {
                if (EntityState.NEW.equals(EntityUtils.getState(notice))) {
                    for (SysNoticeContentRole noticeRole : noticeRoles) {
                        state = EntityUtils.getState(noticeRole);
                        if (EntityState.DELETED.equals(state)) {
                            sysNoticeContentRolesevice.delete(noticeRole);
                        } else if (EntityState.MODIFIED.equals(state) || EntityState.MOVED.equals(state)) {
                            sysNoticeContentRolesevice.update(noticeRole);
                        } else if (EntityState.NEW.equals(state)) {
                            sysNoticeContentRolesevice.save(noticeRole);
                        }
                    }
                }
            }
        }
    }
}
