
package com.innofi.component.rbac.workspace.infobar.service;

import java.util.Collection;
import java.util.Map;

import com.innofi.component.rbac.workspace.infobar.pojo.SysInfBar;
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
public interface ISysInfBarService  extends IBaseService<SysInfBar,String> {
	/**
	 * 
	* @Title: saveSysInfBars 
	* @Description: 添加公用信息栏
	* @return void    返回类型 
	* @throws
	 */
	public void saveSysInfBars(Collection<SysInfBar> objs);

	/**
	 * 
	* @Title: saveTheme 
	* @Description: 保存版式 
	* @param     parameter
	* @return void    返回类型 
	* @throws
	 */
	public void saveTheme(Map parameter);

	/**
	 * 
	* @Title: saveUserInfbar 
	* @Description: 添加用户信息栏 
	* @param @param parameter    设定文件 
	* @return void    返回类型 
	* @throws
	 */

	public void saveUserInfbar(String parameter) ;
	/**
	 * 
	* @Title: removePortal 
	* @Description: 移除信息栏 
	* @param @param parameter    信息栏ID
	* @return void    返回类型 
	* @throws
	 */
	public void removePortal(String infoBarId);

}