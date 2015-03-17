/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.framework.pojo.metadata;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.innofi.framework.pojo.BasePojo;

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
@Entity
@Table(name="MD_METAMODEL")
public class MdMetaModel extends BasePojo {
	
	@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid",strategy="org.hibernate.id.UUIDGenerator")
	@Column(name="MDM_ID")
	public String getId(){
		return super.getId();
	}
		@Column(name="CRT_ORG_CODE")
	public String getCrtOrgCode() {
		return super.getCrtOrgCode();
	}
	@Column(name="UPD_ORG_CODE")
	public String getUpdOrgCode() {
		return super.getUpdOrgCode();
	}
	@Column(name="CRT_USER_CODE")
	public String getCrtUserCode() {
		return super.getCrtUserCode();
	}
	@Column(name="CRT_DATE")
	public Date getCrtDate() {
		return super.getCrtDate();
	}
	@Column(name="UPD_DATE")
	public Date getUpdDate() {
		return super.getUpdDate();
	}
	@Column(name="UPD_USER_CODE")
	public String getUpdUserCode() {
		return super.getUpdUserCode();
	}
	/**
	* 父元模型ID
	*/
	private String parentId;
	/**
	* 元模型编码
	*/
	private String mdmCode;
	/**
	* 元模型名称
	*/
	private String mdmName;
	/**
	* 元模型描述
	*/
	private String mdmDesc;
	/**
	* 元模型排序
	*/
	private BigDecimal mdmSeq;
	/**
	* 显示图标
	*/
	private String nodePic;
	/**
	* 是否显示子类
	*/
	private String isDiscSonId;
	/**
	* 保存路径
	*/
	private String savePath;
	/**
	* 保存类型
	*/
	private String saveType;
	/**
	* UI界面
	*/
	private String uiName;
	/**
	* 层级
	*/
	private BigDecimal mdmLevel;
	/**
	* 有效状态
	*/
	private String status;
	/**
	* 数等级
	*/
	private String treeSeq;
	/**
	* 视图类型
	*/
	private String viewType;
	/**
	* 模型名称
	*/
	private String parentMdmName;
	
	@Column(name="PARENT_ID")
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	@Column(name="MDM_CODE")
	public String getMdmCode() {
		return mdmCode;
	}
	public void setMdmCode(String mdmCode) {
		this.mdmCode = mdmCode;
	}
	@Column(name="MDM_NAME")
	public String getMdmName() {
		return mdmName;
	}
	public void setMdmName(String mdmName) {
		this.mdmName = mdmName;
	}
	@Column(name="MDM_DESC")
	public String getMdmDesc() {
		return mdmDesc;
	}
	public void setMdmDesc(String mdmDesc) {
		this.mdmDesc = mdmDesc;
	}
	@Column(name="NODE_PIC")
	public String getNodePic() {
		return nodePic;
	}
	public void setNodePic(String nodePic) {
		this.nodePic = nodePic;
	}
	@Column(name="IS_DISC_SON_ID")
	public String getIsDiscSonId() {
		return isDiscSonId;
	}
	public void setIsDiscSonId(String isDiscSonId) {
		this.isDiscSonId = isDiscSonId;
	}
	@Column(name="SAVE_PATH")
	public String getSavePath() {
		return savePath;
	}
	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}
	@Column(name="SAVE_TYPE")
	public String getSaveType() {
		return saveType;
	}
	public void setSaveType(String saveType) {
		this.saveType = saveType;
	}
	@Column(name="UI_NAME")
	public String getUiName() {
		return uiName;
	}
	public void setUiName(String uiName) {
		this.uiName = uiName;
	}
	@Column(name="MDM_LEVEL")
	public BigDecimal getMdmLevel() {
		return mdmLevel;
	}
	public void setMdmLevel(BigDecimal mdmLevel) {
		this.mdmLevel = mdmLevel;
	}
	@Column(name="STATUS")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Column(name="TREE_SEQ")
	public String getTreeSeq() {
		return treeSeq;
	}
	public void setTreeSeq(String treeSeq) {
		this.treeSeq = treeSeq;
	}
	@Column(name="VIEW_TYPE")
	public String getViewType() {
		return viewType;
	}
	public void setViewType(String viewType) {
		this.viewType = viewType;
	}
	@Column(name="MDM_SEQ")
	public BigDecimal getMdmSeq() {
		return mdmSeq;
	}
	public void setMdmSeq(BigDecimal mdmSeq) {
		this.mdmSeq = mdmSeq;
	}
	@Transient
	public String getParentMdmName() {
		return parentMdmName;
	}
	public void setParentMdmName(String parentMdmName) {
		this.parentMdmName = parentMdmName;
	}

}
