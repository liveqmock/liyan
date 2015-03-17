package com.innofi.framework.pojo.metadata;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import com.innofi.framework.pojo.BasePojo;
import org.hibernate.annotations.GenericGenerator;


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
@Entity
@Table(name="MD_DATA_VIEW")

public class MdDataView extends BasePojo{
		/**
	* 修改机构
	*/
	private String updOrgCode;
		
		/**
	* 修改日期
	*/
	private Date updDate;
		
		/**
	* 元模型序列序列:用元模型编号表示元模型路径，如：.121.146.
	*/
	private String treeSeq;
		
		/**
	* 有效状态:1-有效2-无效
	*/
	private String status;
		
		/**
	* 元模型父类ID：如果没有父元模型，则为空。
	*/
	private String parentId;
		
		/**
	* 层级:0层作为业务视图的元模型。
	*/
	private BigDecimal mdmLevel;
		
		/**
	* 元模型ID
	*/
	private String mdmId;
	/**
	* 元模型ID
	*/
	private String mdmName;	
	/**
	* 排序字段
	*/
	private BigDecimal mdmSeq;	
		
		/**
	* 创建人
	*/
	private String crtUserCode;
		
		/**
	* 创建机构
	*/
	private String crtOrgCode;
		
		/**
	* 创建日期
	*/
	private Date crtDate;
		
		/**
	* 修改人
	*/
	private String updUserCode;
		
		@Column(name="UPD_ORG_CODE")
	public String getUpdOrgCode(){
		return this.updOrgCode;
	}
	public void setUpdOrgCode(String updOrgCode){
		this.updOrgCode = updOrgCode;
	} 
		
			@Column(name="UPD_DATE")
	public Date getUpdDate(){
		return this.updDate;
	}
	public void setUpdDate(Date updDate){
		this.updDate = updDate;
	} 
		
			@Column(name="TREE_SEQ")
	public String getTreeSeq(){
		return this.treeSeq;
	}
	public void setTreeSeq(String treeSeq){
		this.treeSeq = treeSeq;
	} 
		
			@Column(name="STATUS")
	public String getStatus(){
		return this.status;
	}
	public void setStatus(String status){
		this.status = status;
	} 
		
			@Column(name="PARENT_ID")
	public String getParentId(){
		return this.parentId;
	}
	public void setParentId(String parentId){
		this.parentId = parentId;
	} 
		
			@Column(name="MDM_LEVEL")
	public BigDecimal getMdmLevel(){
		return this.mdmLevel;
	}
	public void setMdmLevel(BigDecimal mdmLevel){
		this.mdmLevel = mdmLevel;
	} 
		
			@Column(name="MDM_ID")
	public String getMdmId(){
		return this.mdmId;
	}
	public void setMdmId(String mdmId){
		this.mdmId = mdmId;
	} 
		
			@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid",strategy="org.hibernate.id.UUIDGenerator")
	@Column(name="DATA_VIEW_ID")
	public String getId(){
		return super.getId();
	}
		
			@Column(name="CRT_USER_CODE")
	public String getCrtUserCode(){
		return this.crtUserCode;
	}
	public void setCrtUserCode(String crtUserCode){
		this.crtUserCode = crtUserCode;
	} 
		
			@Column(name="CRT_ORG_CODE")
	public String getCrtOrgCode(){
		return this.crtOrgCode;
	}
	public void setCrtOrgCode(String crtOrgCode){
		this.crtOrgCode = crtOrgCode;
	} 
		
			@Column(name="CRT_DATE")
	public Date getCrtDate(){
		return this.crtDate;
	}
	public void setCrtDate(Date crtDate){
		this.crtDate = crtDate;
	} 
		
			@Column(name="UPD_USER_CODE")
	public String getUpdUserCode(){
		return this.updUserCode;
	}
	public void setUpdUserCode(String updUserCode){
		this.updUserCode = updUserCode;
	}
	@Column(name="MDM_NAME")
	public String getMdmName() {
		return mdmName;
	}
	public void setMdmName(String mdmName) {
		this.mdmName = mdmName;
	}
	@Column(name="MDM_SEQ")
	public BigDecimal getMdmSeq() {
		return mdmSeq;
	}
	public void setMdmSeq(BigDecimal mdmSeq) {
		this.mdmSeq = mdmSeq;
	} 
		
	}