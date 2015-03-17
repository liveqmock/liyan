package com.innofi.component.rbac.privilage.pojo;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

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
@Table(name="DA_TABLE_POLICY")

public class DaTablePolicy extends BasePojo{
		/**
	* 表结构ID
	*/
	private String tableId;
		
		
		/**
	* 表名
	*/
	private String tableName;
		
		/**
	* 数据权限类型:1-浏览 2-修改 3-删除,有修改删除的权限就有浏览权限。
	*/
	private String operType;
		
		/**
	* 控制策略描述
	*/
	private String authDesc;
		
		/**
	* 创建机构
	*/
	private String crtOrgCode;
		
		/**
	* 创建日期
	*/
	private Date crtDate;
		
		/**
	* 创建人
	*/
	private String crtUserCode;
		
		/**
	* 其它条件:比如status字段='1' and crtusercode='00001'
	*/
	private String sqlWhere;
		
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
		
	private boolean selectFlag;
	
	private String tableDesc;//对应的策略表描述，这里用于模块名描述。
	
	
	@Column(name="TABLE_ID")
	public String getTableId(){
		return this.tableId;
	}
	public void setTableId(String tableId){
		this.tableId = tableId;
	} 
		
	@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid",strategy="org.hibernate.id.UUIDGenerator")
	@Column(name="TABLE_AUTH_ID")
	public String getId(){
		return super.getId();
	}
		
			@Column(name="TABLE_NAME")
	public String getTableName(){
		return this.tableName;
	}
	public void setTableName(String tableName){
		this.tableName = tableName;
	} 
		
			@Column(name="OPER_TYPE")
	public String getOperType(){
		return this.operType;
	}
	public void setOperType(String operType){
		this.operType = operType;
	} 
		
			@Column(name="AUTH_DESC")
	public String getAuthDesc(){
		return this.authDesc;
	}
	public void setAuthDesc(String authDesc){
		this.authDesc = authDesc;
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
		
			@Column(name="SQL_WHERE")
	public String getSqlWhere(){
		return this.sqlWhere;
	}
	public void setSqlWhere(String sqlWhere){
		this.sqlWhere = sqlWhere;
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
	@Transient
	public boolean getSelectFlag() {
		return selectFlag;
	}
	public void setSelectFlag(boolean selectFlag) {
		this.selectFlag = selectFlag;
	}
	@Transient
	public String getTableDesc() {
		return tableDesc;
	}
	public void setTableDesc(String tableDesc) {
		this.tableDesc = tableDesc;
	} 
	}