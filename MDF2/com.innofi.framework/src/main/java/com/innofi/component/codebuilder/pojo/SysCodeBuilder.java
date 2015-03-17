package com.innofi.component.codebuilder.pojo;
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
@Table(name="SYS_CODE_BUILDER")

public class SysCodeBuilder extends BasePojo{
		
		/**
	* 模式:指定编码生成规则，eg:｛当前机构编码}+{sysdate,8位}+{流水号,8位}
	*/
	private String pattern;
		
		/**
	* 生成规则类型:通过生成规则类型获取生成规则，约定为表对象名称
	*/
	private String builderType;
		
		
		/**
	* 备注
	*/
	private String remark;
		
		/**
	* 前缀
	*/
	private String prefix;
		
		/**
	* 创建机构
	*/
	private String crtOrgCode;
		
		/**
	* 创建时间
	*/
	private Date crtDate;
		
		/**
	* 创建人
	*/
	private String crtUserCode;
		
		/**
	* 修改机构
	*/
	private String updOrgCode;
		
		/**
	* 修改日期
	*/
	private Date updDate;
		
		/**
	* 修改人
	*/
	private String updUserCode;
	
	/**
	* 段间分隔符
	*/
	private String delimiter;
	
	/**
	* 是否允许断号
	*/
	private String isBreakCode;
	
	/**
	* 是否新增显示
	*/
	private String isAddView;
	
		@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid",strategy="org.hibernate.id.UUIDGenerator")
	@Column(name="BUILDER_ID")
	public String getId(){
		return super.getId();
	}
		
			@Column(name="PATTERN")
	public String getPattern(){
		return this.pattern;
	}
	public void setPattern(String pattern){
		this.pattern = pattern;
	} 
		
			@Column(name="BUILDER_TYPE")
	public String getBuilderType(){
		return this.builderType;
	}
	public void setBuilderType(String builderType){
		this.builderType = builderType;
	} 
			@Column(name="REMARK")
	public String getRemark(){
		return this.remark;
	}
	public void setRemark(String remark){
		this.remark = remark;
	} 
		
			@Column(name="PREFIX")
	public String getPrefix(){
		return this.prefix;
	}
	public void setPrefix(String prefix){
		this.prefix = prefix;
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
		
			@Column(name="CRT_USER_CODE")
	public String getCrtUserCode(){
		return this.crtUserCode;
	}
	public void setCrtUserCode(String crtUserCode){
		this.crtUserCode = crtUserCode;
	} 
		
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
		
			@Column(name="UPD_USER_CODE")
	public String getUpdUserCode(){
		return this.updUserCode;
	}
	public void setUpdUserCode(String updUserCode){
		this.updUserCode = updUserCode;
	}

	@Column(name="DELIMITER")
	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	@Column(name="IS_BREAK_CODE")
	public String getIsBreakCode() {
		return isBreakCode;
	}

	public void setIsBreakCode(String isBreakCode) {
		this.isBreakCode = isBreakCode;
	}
	@Column(name="IS_ADD_VIEW")
	public String getIsAddView() {
		return isAddView;
	}

	public void setIsAddView(String isAddView) {
		this.isAddView = isAddView;
	}
	}