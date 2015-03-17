package com.innofi.component.codebuilder.pojo;
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
@Table(name="SYS_CODE_PATTERN")

public class SysCodePattern extends BasePojo{
		/**
	* 长度
	*/
	private BigDecimal valueLen;
		
		
		/**
	* 生成规则ID
	*/
	private String builderId;
		
		/**
	* 状态:1-启用 2-停用
	*/
	private String status;
		
		/**
	* 步长
	*/
	private BigDecimal valueStep;
		
		/**
	* 显示格式
	*/
	private String valueFmt;
		
		/**
	* 排列顺序:属性对应的值在生成的编码中的顺序
	*/
	private BigDecimal patternSeq;
		
		/**
	* 当前流水号
	*/
	private BigDecimal currentSeq;
		
		/**
	* 属性类别：1-固定值（前缀） 2-系统日期 3-系统机构 4-流水号 5-对象属性（字段名）
	*/
	private String patternType;
		
		/**
	* 取值属性
	*/
	private String valueField;
		
		/**
	* 初始值
	*/
	private BigDecimal valueDef;
		
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
		
		@Column(name="VALUE_LEN")
	public BigDecimal getValueLen(){
		return this.valueLen;
	}
	public void setValueLen(BigDecimal valueLen){
		this.valueLen = valueLen;
	} 
		
			@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid",strategy="org.hibernate.id.UUIDGenerator")
	@Column(name="CODE_PATTERN_ID")
	public String getId(){
		return super.getId();
	}
		
			@Column(name="BUILDER_ID")
	public String getBuilderId(){
		return this.builderId;
	}
	public void setBuilderId(String builderId){
		this.builderId = builderId;
	} 
		
			@Column(name="STATUS")
	public String getStatus(){
		return this.status;
	}
	public void setStatus(String status){
		this.status = status;
	} 
		
			@Column(name="VALUE_STEP")
	public BigDecimal getValueStep(){
		return this.valueStep;
	}
	public void setValueStep(BigDecimal valueStep){
		this.valueStep = valueStep;
	} 
		
			@Column(name="VALUE_FMT")
	public String getValueFmt(){
		return this.valueFmt;
	}
	public void setValueFmt(String valueFmt){
		this.valueFmt = valueFmt;
	} 
		
			@Column(name="PATTERN_SEQ")
	public BigDecimal getPatternSeq(){
		return this.patternSeq;
	}
	public void setPatternSeq(BigDecimal patternSeq){
		this.patternSeq = patternSeq;
	} 
		
			@Column(name="CURRENT_SEQ")
	public BigDecimal getCurrentSeq(){
		return this.currentSeq;
	}
	public void setCurrentSeq(BigDecimal currentSeq){
		this.currentSeq = currentSeq;
	} 
		
			@Column(name="PATTERN_TYPE")
	public String getPatternType(){
		return this.patternType;
	}
	public void setPatternType(String patternType){
		this.patternType = patternType;
	} 
		
			@Column(name="VALUE_FIELD")
	public String getValueField(){
		return this.valueField;
	}
	public void setValueField(String valueField){
		this.valueField = valueField;
	} 
		
			@Column(name="VALUE_DEF")
	public BigDecimal getValueDef(){
		return this.valueDef;
	}
	public void setValueDef(BigDecimal valueDef){
		this.valueDef = valueDef;
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
		
	}