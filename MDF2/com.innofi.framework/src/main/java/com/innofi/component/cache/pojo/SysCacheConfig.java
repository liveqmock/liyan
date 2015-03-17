package com.innofi.component.cache.pojo;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.innofi.framework.pojo.BasePojo;


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
@Table(name="SYS_CACHE_CONFIG")

public class SysCacheConfig extends BasePojo{
		/**
	* 缓存描述
	*/
	private String cacheDesc;
		
		
		/**
	* 缓存名称
	*/
	private String cacheName;
		
		/**
	* 刷新策略：1-定时刷新2-实时刷新
	*/
	private String cachePolicy;
		
		/**
	* 缓存类型：1-临时缓存2-永久缓存
	*/
	private String cacheType;
		
		/**
	* 创建时间
	*/
	private Date crtDate;
		
		/**
	* 创建机构
	*/
	private String crtOrgCode;
		
		/**
	* 创建人
	*/
	private String crtUserCode;
		
		/**
	* 服务对象ID
	*/
	private String objId;
		
		/**
	* 服务对象名：service类名
	*/
	private String objName;
		
		/**
	* 刷新频率：单位为S，只定时缓存有刷新频率
	*/
	private BigDecimal refreshFreq;
		
		/**
	* 状态：1-启用 0-停用
	*/
	private String status;
		
		/**
	* 表结构ID
	*/
	private String tableId;
		
		/**
	* 表名
	*/
	private String tableName;
		
		/**
	* 修改日期
	*/
	private Date updDate;
		
		/**
	* 修改机构
	*/
	private String updOrgCode;
		
		/**
	* 修改人
	*/
	private String updUserCode;
		
		@Column(name="CACHE_DESC")
	public String getCacheDesc(){
		return this.cacheDesc;
	}
	public void setCacheDesc(String cacheDesc){
		this.cacheDesc = cacheDesc;
	} 
		
			@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid",strategy="org.hibernate.id.UUIDGenerator")
	@Column(name="CACHE_ID")
	public String getId(){
		return super.getId();
	}
		
			@Column(name="CACHE_NAME")
	public String getCacheName(){
		return this.cacheName;
	}
	public void setCacheName(String cacheName){
		this.cacheName = cacheName;
	} 
		
			@Column(name="CACHE_POLICY")
	public String getCachePolicy(){
		return this.cachePolicy;
	}
	public void setCachePolicy(String cachePolicy){
		this.cachePolicy = cachePolicy;
	} 
		
			@Column(name="CACHE_TYPE")
	public String getCacheType(){
		return this.cacheType;
	}
	public void setCacheType(String cacheType){
		this.cacheType = cacheType;
	} 
		
			@Column(name="CRT_DATE")
	public Date getCrtDate(){
		return this.crtDate;
	}
	public void setCrtDate(Date crtDate){
		this.crtDate = crtDate;
	} 
		
			@Column(name="CRT_ORG_CODE")
	public String getCrtOrgCode(){
		return this.crtOrgCode;
	}
	public void setCrtOrgCode(String crtOrgCode){
		this.crtOrgCode = crtOrgCode;
	} 
		
			@Column(name="CRT_USER_CODE")
	public String getCrtUserCode(){
		return this.crtUserCode;
	}
	public void setCrtUserCode(String crtUserCode){
		this.crtUserCode = crtUserCode;
	} 
		
			@Column(name="OBJ_ID")
	public String getObjId(){
		return this.objId;
	}
	public void setObjId(String objId){
		this.objId = objId;
	} 
		
			@Column(name="OBJ_NAME")
	public String getObjName(){
		return this.objName;
	}
	public void setObjName(String objName){
		this.objName = objName;
	} 
		
			@Column(name="REFRESH_FREQ")
	public BigDecimal getRefreshFreq(){
		return this.refreshFreq;
	}
	public void setRefreshFreq(BigDecimal refreshFreq){
		this.refreshFreq = refreshFreq;
	} 
		
			@Column(name="STATUS")
	public String getStatus(){
		return this.status;
	}
	public void setStatus(String status){
		this.status = status;
	} 
		
			@Column(name="TABLE_ID")
	public String getTableId(){
		return this.tableId;
	}
	public void setTableId(String tableId){
		this.tableId = tableId;
	} 
		
			@Column(name="TABLE_NAME")
	public String getTableName(){
		return this.tableName;
	}
	public void setTableName(String tableName){
		this.tableName = tableName;
	} 
		
			@Column(name="UPD_DATE")
	public Date getUpdDate(){
		return this.updDate;
	}
	public void setUpdDate(Date updDate){
		this.updDate = updDate;
	} 
		
			@Column(name="UPD_ORG_CODE")
	public String getUpdOrgCode(){
		return this.updOrgCode;
	}
	public void setUpdOrgCode(String updOrgCode){
		this.updOrgCode = updOrgCode;
	} 
		
			@Column(name="UPD_USER_CODE")
	public String getUpdUserCode(){
		return this.updUserCode;
	}
	public void setUpdUserCode(String updUserCode){
		this.updUserCode = updUserCode;
	} 
		
	}