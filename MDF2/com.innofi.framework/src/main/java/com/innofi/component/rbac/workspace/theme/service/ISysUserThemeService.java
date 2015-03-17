
package com.innofi.component.rbac.workspace.theme.service;

import com.innofi.component.rbac.workspace.theme.pojo.SysUserTheme;
import com.innofi.framework.service.IBaseService;

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
public interface ISysUserThemeService  extends IBaseService<SysUserTheme,String> {

    /**
     * 获取系统默认主题
     * @return SysUserTheme 用户主题
     */
    public SysUserTheme getSystemDefaultUserTheme();

    /**
     * 获取当前用户主题
     * @return
     */
    public SysUserTheme getCurrentUserTheme();

    /**
     * 根据用户帐号获取用户当前主题
     * @param userCode 用户帐号
     * @return SysUserTheme 用户主题
     */
    public SysUserTheme getUserTheme(String userCode);

    /**
     * 根据用户帐号添加用户主题
     * @param sysUserTheme 用户主题
     */
    public void addDefaultUserTheme(SysUserTheme sysUserTheme);

    /**
     * 保存用户登录版式、是否全屏设置
     * @param  mainTHeme 版式
     * @param isFullScreen 是否全屏
     */
    public void saveMainTheme(String mainTHeme , String isFullScreen);

	
}