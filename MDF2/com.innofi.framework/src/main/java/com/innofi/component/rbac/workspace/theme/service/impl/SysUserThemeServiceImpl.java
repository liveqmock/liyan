
package com.innofi.component.rbac.workspace.theme.service.impl;


import com.innofi.component.rbac.workspace.infobar.pojo.SysUserInfBar;
import com.innofi.component.rbac.workspace.infobar.service.ISysUserInfBarService;
import com.innofi.framework.FrameworkConstants;
import com.innofi.framework.exception.FrameworkRuntimeException;
import com.innofi.component.rbac.user.pojo.SysUser;
import com.innofi.component.rbac.workspace.theme.pojo.SysUserTheme;
import com.innofi.component.rbac.workspace.theme.service.ISysUserThemeService;
import javax.annotation.Resource;

import com.innofi.framework.spring.context.ContextHolder;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.service.impl.BaseServiceImpl;

import java.util.List;


/**
 * 功能/ 模块：todo 模块中文名称
 *
 * @author  todo 开发人员姓名 邮箱
 * @version todo 当前版本 日期
 *          todo 类描述
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
@Service(value="sysUserThemeService")
public class SysUserThemeServiceImpl  extends BaseServiceImpl<SysUserTheme,String> implements ISysUserThemeService {

    @Resource(name="mdDataTitleTableMappingDaoSupport")
    protected DaoSupport daoSupport;

    public DaoSupport getDaoSupport(){
        return daoSupport;
    }


    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.rbac.workspace.theme.service.ISysUserThemeService#getSystemDefaultUserTheme()
     */
    public SysUserTheme getSystemDefaultUserTheme() {
        SysUserTheme defaultUserTheme = findUniqueByHql(" from SysUserTheme where id = ? ", "sysdefault");
        if(defaultUserTheme==null)throw new FrameworkRuntimeException("用户主题表缺少初始化数据,表名[SYS_USER_THEME]!");
        return defaultUserTheme;
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.rbac.workspace.theme.service.ISysUserThemeService#getUserTheme(String)
     */
    public SysUserTheme getUserTheme(String userCode) {
        List<SysUserTheme> sysUserThemes = findByHql("from SysUserTheme where userCode = ? ", userCode);//查询用户所有主题
        if(sysUserThemes.size()==0){
            SysUserTheme defaultUserTheme = getSystemDefaultUserTheme();
            SysUserTheme newUserTheme = new SysUserTheme();
            BeanUtils.copyProperties(defaultUserTheme,newUserTheme);
            newUserTheme.setId(null);
            newUserTheme.setUserCode(userCode);
            newUserTheme.setIsSysDefault(FrameworkConstants.COMM_Y);
            newUserTheme.setStatus(FrameworkConstants.COMM_Y);
            newUserTheme.setUserThemeName("经典主界面版式");
            newUserTheme.setInfBarFrame("0");
            newUserTheme.setIsFullScreen(FrameworkConstants.COMM_N);
            newUserTheme.setMainFrame(ContextHolder.getSystemProperties().getString("mdf.default.main.frame","tradURL"));
            addDefaultUserTheme(newUserTheme);
        }
        return findUniqueByHql("from SysUserTheme where userCode = ? and isSysDefault = ? ", userCode, FrameworkConstants.COMM_Y);//查询用户默认主题
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.rbac.workspace.theme.service.ISysUserThemeService#addDefaultUserTheme(SysUserTheme)
     */
    public void addDefaultUserTheme(SysUserTheme sysUserTheme) {
        ISysUserInfBarService sysUserInfBarService = ContextHolder.getSpringBean("sysUserInfBarService");
        save(sysUserTheme);
        List<SysUserInfBar> defaultUserInfBars = sysUserInfBarService.findByHql("from SysUserInfBar where userThemeId =? ", sysUserTheme.getId());
        for(SysUserInfBar defaultUserInfBar : defaultUserInfBars){
            SysUserInfBar currentUserInfBar = new SysUserInfBar();
            BeanUtils.copyProperties(defaultUserInfBar,currentUserInfBar);
            currentUserInfBar.setUserThemeId(sysUserTheme.getId());
            sysUserInfBarService.save(currentUserInfBar);
        }
    }


    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.rbac.workspace.theme.service.ISysUserThemeService#saveMainTheme(String,String)
     */
    public void saveMainTheme(String mainTHeme , String isFullScreen){
        SysUser user = ContextHolder.getContext().getLoginUser();
        SysUserTheme sysUserTheme = user.getSysUserTheme();
        sysUserTheme.setMainFrame(mainTHeme);
        sysUserTheme.setIsFullScreen(isFullScreen);
        sysUserTheme.setIsSysDefault(FrameworkConstants.COMM_Y);
        update(sysUserTheme);
    }

    public SysUserTheme getCurrentUserTheme() {
        SysUser user = ContextHolder.getContext().getLoginUser();
        String userCode = user.getUserCode();
        SysUserTheme sysUserTheme = null;
        if (ContextHolder.getSystemProperties().getBoolean("mdf.workspace.inofibar.use.default", false)) {
            sysUserTheme = getSystemDefaultUserTheme();
        } else {
            sysUserTheme = getUserTheme(userCode);
        }
        return sysUserTheme;
    }

}

