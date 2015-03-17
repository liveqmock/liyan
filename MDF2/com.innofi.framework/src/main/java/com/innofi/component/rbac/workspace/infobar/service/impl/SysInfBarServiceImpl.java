package com.innofi.component.rbac.workspace.infobar.service.impl;


import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.innofi.component.rbac.workspace.infobar.pojo.SysInfBar;
import com.innofi.component.rbac.workspace.infobar.pojo.SysUserInfBar;
import com.innofi.component.rbac.workspace.infobar.service.ISysInfBarService;
import com.innofi.component.rbac.workspace.infobar.service.ISysUserInfBarService;
import com.innofi.framework.FrameworkConstants;
import com.innofi.component.rbac.workspace.theme.pojo.SysUserTheme;
import com.innofi.component.rbac.workspace.theme.service.ISysUserThemeService;

import javax.annotation.Resource;

import com.innofi.framework.dao.hibernate.QueryConstants;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.innofi.framework.dao.DaoSupport;
import com.innofi.component.rbac.user.pojo.SysUser;
import com.innofi.framework.service.impl.BaseServiceImpl;
import com.innofi.framework.spring.context.ContextHolder;


/**
 * 功能/ 模块：todo 模块中文名称
 *
 * @author todo 开发人员姓名 邮箱
 * @version todo 当前版本 日期
 *          todo 类描述
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
@Service(value = "sysInfBarService")
public class SysInfBarServiceImpl extends BaseServiceImpl<SysInfBar, String> implements ISysInfBarService {
    @Resource
    private ISysUserThemeService sysUserThemeSevice;
    @Resource
    private ISysUserInfBarService sysUserInfBarSevice;

    @Resource(name="mdDataTitleTableMappingDaoSupport")
    protected DaoSupport daoSupport;

    public DaoSupport getDaoSupport(){
        return daoSupport;
    }

    @Override
    public void saveSysInfBars(Collection<SysInfBar> objs) {
          /*
           * 添加默认的主题版式
           */
        List<SysUserTheme> list = sysUserThemeSevice.findByHql(" from SysUserTheme where userCode =? ", "sysdefault");
        String userThemeId = "";
        String infBarId = "";
        String infBarCol = "";
        if (list.size() == 0) {
            SysUserTheme sysUserTheme = new SysUserTheme();
            sysUserTheme.setUserCode("sysdefault");
            sysUserTheme.setUserThemeName("默认版式");
            sysUserTheme.setInfBarFrame("0");
            sysUserTheme.setIsSysDefault(FrameworkConstants.COMM_Y);
            sysUserTheme.setMainFrame("classURL");
            sysUserTheme.setIsFullScreen(FrameworkConstants.COMM_Y);
            sysUserTheme.setCrtDate(new Date());
            sysUserThemeSevice.save(sysUserTheme);
            userThemeId = sysUserTheme.getId();
        }
        /*
            * 添加信息栏
            */
        for (Iterator iterator = objs.iterator(); iterator.hasNext(); ) {
            SysInfBar sysInfBar = (SysInfBar) iterator.next();
            EntityState state = EntityUtils.getState(sysInfBar);
            if (EntityState.NEW.equals(state)) {
                save(sysInfBar);
                /*
                 * 添加信息栏与主题版式关系
                 */
                SysUserInfBar sysUserInfBar = new SysUserInfBar();
                sysUserInfBar.setInfBarId(sysInfBar.getId());
                sysUserInfBar.setSeq(0);
                if (list.size() == 0) {
                    sysUserInfBar.setUserThemeId(userThemeId);
                } else {
                    sysUserInfBar.setUserThemeId(list.get(0).getId());
                }
                sysUserInfBarSevice.save(sysUserInfBar);
            } else if (EntityState.MODIFIED.equals(state)) {
                this.update(sysInfBar);
            } else if (EntityState.DELETED.equals(state)) {
                sysUserInfBarSevice.deleteByPropertyFilters(buildPropertyFilter("infBarId",sysInfBar.getId(),QueryConstants.EQUAL));//删除信息栏用户关系
                this.delete(sysInfBar);
            }
        }
    }


    @Override
    public void saveTheme(Map parameter) {
        SysUser user = ContextHolder.getContext().getLoginUser();
        String portals = (String) parameter.get("portal");
        String[] portalsColumn = portals.split("#");
        SysUserTheme sysUserTheme = sysUserThemeSevice.getCurrentUserTheme();
        String themeId = sysUserTheme.getId();
        for (int i = 0; i < portalsColumn.length; i++) {
            String infbarId = portalsColumn[i].substring(0, portalsColumn[i].indexOf(","));
            String columnSeq = portalsColumn[i].substring(portalsColumn[i].indexOf(",") + 1);
            List<SysUserInfBar> sysUserInfBars = sysUserInfBarSevice.findByHql("from SysUserInfBar where infBarId=? and  userThemeId =? ", infbarId , themeId);
            SysUserInfBar sysUserInfBar = new SysUserInfBar();
            sysUserInfBar.setUserThemeId(themeId);
            sysUserInfBar.setInfBarId(infbarId);
            sysUserInfBar.setSeq(Integer.parseInt(columnSeq));
            if (sysUserInfBars.size() == 0) {
                sysUserInfBarSevice.save(sysUserInfBar);
            } else {
                SysUserInfBar oldInfBar = sysUserInfBars.get(0);
                String oldId = oldInfBar.getId();
                BeanUtils.copyProperties(sysUserInfBar, oldInfBar); 
                oldInfBar.setId(oldId);
                sysUserInfBarSevice.update(oldInfBar);
            }
        }
    }

    @Override
    public void saveUserInfbar(String parameter) {
        SysUserTheme sysUserTheme = sysUserThemeSevice.getCurrentUserTheme();
        String userThemeId = sysUserTheme.getId();
        String[] infbarIds = parameter.split(",");
        for (int i = 0; i < infbarIds.length; i++) {
            String infbarId = infbarIds[i];
            SysUserInfBar sysUserInfBar = new SysUserInfBar();
            sysUserInfBar.setUserThemeId(userThemeId);
            sysUserInfBar.setInfBarId(infbarId);
            sysUserInfBar.setSeq(i);
            sysUserInfBarSevice.save(sysUserInfBar);
        }
    }

    @Override
    public void removePortal(String infoBarId) {
        SysUserTheme sysUserTheme = sysUserThemeSevice.getCurrentUserTheme();
        String userThemeId = sysUserTheme.getId();
        SysUserInfBar sysUserInfBar = sysUserInfBarSevice.findUniqueByHql(" from SysUserInfBar where userThemeId=? and infBarId=?", userThemeId, infoBarId);
        sysUserInfBarSevice.delete(sysUserInfBar);
    }

}

