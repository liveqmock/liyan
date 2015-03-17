
package com.innofi.component.codebuilder.service;

import com.innofi.component.codebuilder.pojo.SysCodeBuilder;
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
public interface ISysCodeBuilderService  extends IBaseService<SysCodeBuilder,String> {

	/**
	 * 根据编码类型取到当前编码
	 * @param builderType
	 * @return
	 */
	String saveCodeByBuilderType(String builderType);
	
}