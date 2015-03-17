/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.framework.pojo.metadata;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

import com.innofi.framework.pojo.BasePojo;

/**
 * 功能/ 模块：元数据模块
 *
 * @author 张磊 alex.zhang@innofi.com.cn
 * @version 2.0.0 12-11-30
 *          测试实体对象
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
@Entity
@Table(name="MD_DATATITLE_TABLE_MAPPING")
public class MdDataTitleTableMapping extends BasePojo {
	
	@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid",strategy="org.hibernate.id.UUIDGenerator")
	@Column(name="DATATITLE_TABLE_ID")
	public String getId(){
		return super.getId();
	}
	
	/**
	* 数据主题ID
	*/
	private String dataTitleId;
	/**
	* 表结构ID
	*/
	private String tableId;
	/**
	* 表结构名称
	*/
	private String tableName;
	
	@Column(name="DATA_TITLE_ID")
	public String getDataTitleId() {
		return dataTitleId;
	}
	public void setDataTitleId(String dataTitleId) {
		this.dataTitleId = dataTitleId;
	}
	@Column(name="TABLE_ID")
	public String getTableId() {
		return tableId;
	}
	public void setTableId(String tableId) {
		this.tableId = tableId;
	}
	@Column(name="TABLE_NAME")
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
}
