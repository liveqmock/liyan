/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.framework.pojo.metadata;

/**
 * 功能/ 模块：测试模块
 *
 * @author 张磊 alex.zhang@innofi.com.cn
 * @version 2.0.0 12-11-30
 *          测试实体对象
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
public class MdMetaModelAndDataViewTree {
	
	/**
	* 元模型Id
	*/
	private String mdmId;
	/**
	* 元模型名称
	*/
	private String mdmName;
	/**
	* 保存路径
	*/
	private String savePath;

	/**
	* UI界面
	*/
	private String uiName;
	/**
	* viewType
	*/
	private String viewType;
	/**
	* 图标
	*/
	private String nodePic;

	public String getMdmId() {
		return mdmId;
	}
	public void setMdmId(String mdmId) {
		this.mdmId = mdmId;
	}
	public String getMdmName() {
		return mdmName;
	}
	public void setMdmName(String mdmName) {
		this.mdmName = mdmName;
	}
	public String getSavePath() {
		return savePath;
	}
	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}
	public String getUiName() {
		return uiName;
	}
	public void setUiName(String uiName) {
		this.uiName = uiName;
	}
	public String getViewType() {
		return viewType;
	}
	public void setViewType(String viewType) {
		this.viewType = viewType;
	}
	public String getNodePic() {
		return nodePic;
	}
	public void setNodePic(String nodePic) {
		this.nodePic = nodePic;
	}
}
