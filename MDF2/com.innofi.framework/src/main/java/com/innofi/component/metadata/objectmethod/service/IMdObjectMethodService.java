/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.component.metadata.objectmethod.service;

import com.innofi.framework.pojo.metadata.MdObjectMethod;
import com.innofi.framework.service.IBaseService;

/**
 * 功能/ 模块：测试模块
 *
 * @author 张磊 Alex.zhang@innofi.com.cn
 * @version 2.0.0 12-11-30
 *          测试Service接口
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
public interface IMdObjectMethodService extends IBaseService<MdObjectMethod,String>{

	/**
	 * 通过对象反射出对象的方法。
	 * @param pojo
	 * @throws ClassNotFoundException 
	 */
	public void refreshObjecMethod(String objectId,String pojo) throws ClassNotFoundException;
	
	/**
	 * 通过反射刷新所有对象的方法.
	 */
	public void refreshAllObjecMethod() throws ClassNotFoundException;
}