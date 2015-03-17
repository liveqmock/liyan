package com.innofi.framework.pojo;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Java实体对象的基类，实现Serializable序列化接口 <p/>
 *
 * 提取公共属性包括：主键:id，继承后续按照如下格式声明主键字段<br/>
 * @Id<br/>
 * @GeneratedValue(generator="uuid")<br/>
 * @GenericGenerator(name="uuid",strategy="org.hibernate.id.UUIDGenerator")<br/>
 * @Column(name="TABLE_ID")<br/>
 * @Override<br/>
 * public String getId(){<br/>
 *  return super.getId();<br/>
 * }<br/>
 *               name:对象中文名称
 *               version：用于hibernate乐观锁子类使用时，需要在表中定义VERSION字段，数据类型为：数值,并声明格式如下<br/>
 *               @Version<br/>
 *               public int getVersion(){<br/>
 *                  return super.getVersion();<br/>
 *               }<br/>
 *               crtDate     对象创建时间
 *               updDate     最后更新时间
 *               crtOrgCode  创建机构代码
 *               updOrgCode  最后更新机构代码
 *               crtUserCode 创建用户员工编号
 *               updUserCode 最后更新用户员工编号
 *               objStatus   对象状态
 *               delFlag     0未删除 , 1已删除
 *               cacheAble   定义当前对象在Hibernate查询时是否使用缓存 true缓存、false不缓存 ,默认false
 *               预留扩展字段1至5
 *
 *               protected String ext1;
 *
 *               protected String ext2;
 *
 *               protected String ext3;
 *
 *               protected String ext4;
 *
 *               protected String ext5;
 *
 * @author 刘名寓 liumy2009@126.com
 * @version 2.0 2012-5-20  上午11:37:06
 *
 * 覆盖重写 toString、equals、hashCode方法<p/>
 * <p/>
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司 <p/>
 * all rights reserved.
 * @since JDK1.6
 */

/**
 * CacheConcurrencyStrategy.NONE
 * <p/>
 * 　　 CacheConcurrencyStrategy.READ_ONLY ，只读模式，在此模式下，如果对数据进行更新操作，会有异常；
 * <p/>
 * 　　 CacheConcurrencyStrategy.READ_WRITE ，读写模式在更新缓存的时候会把缓存里面的数据换成一个锁，其它事务如果去取相应的缓存数据，发现被锁了，直接就去数据库查询；
 * <p/>
 * 　　 CacheConcurrencyStrategy.NONSTRICT_READ_WRITE ，不严格的读写模式则不会的缓存数据加锁；
 * <p/>
 * 　　 CacheConcurrencyStrategy.TRANSACTIONAL ，事务模式指缓存支持事务，当事务回滚时，缓存也能回滚，只支持 JTA 环境。
 * <p/>
 * <p/>
 * 缓存的注释写法如下，加在 Entity 的 java 类上：
 */
public abstract class BasePojo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 对象主键 , 默认主键生成策略为Hibernate uuid ， 子类可覆盖重新指定生成策略
     */
    protected String id;

    /**
     * 创建时间
     */
    protected Date crtDate;

    /**
     * 最后更新时间
     */
    protected Date updDate;

    /**
     * 创建机构
     */
    protected String crtOrgCode;

    /**
     * 创建机构名称
     */
    protected String crtOrgName;

    /**
     * 更新机构
     */
    protected String updOrgCode;

    /**
     * 更新机构名称
     */
    protected String updOrgName;

    /**
     * 创建用户
     */
    protected String crtUserCode;

    /**
     * 创建用户名称
     */
    protected String crtUserName;

    /**
     * 最后更新用户
     */
    protected String updUserCode;

    /**
     * 更新用户名称
     */
    protected String updUserName;

    /**
     * 删除标志 0未删除 , 1已删除
     */
    protected String delFlag;

    /**
     * 是否缓存
     */
    private boolean cacheAble = false;

    /**
     * 是否树形结构
     */
    private boolean treeStruct = false;

    /**
     * 树ID字段
     */
    private String treeIdFiledName;

    /**
     * 树父ID字段名称
     */
    private String treeParentIdFieldName;

    /**
     * 树父略经序列字段名称
     */
    private String treeSeqFieldName;

    /**
     * 预留扩展字段1至5
     */
    protected String ext1;

    protected String ext2;

    protected String ext3;

    protected String ext4;

    protected String ext5;


    protected BasePojo() {

    }

    protected BasePojo(String id) {
        this.id = id;
    }

    /**
     * 获得对象主键值
     *
     * @return the id
     */
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    public String getId() {
        return id;
    }

    /**
     * 设置对象主键值
     *
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获得对象建立时间
     */
    public Date getCrtDate() {
        return crtDate;
    }

    /**
     * 设置对象建立时间
     *
     * @param crtDate 建立时间
     */
    public void setCrtDate(Date crtDate) {
        this.crtDate = crtDate;
    }

    /**
     * 获得对象更新时间
     */
    public Date getUpdDate() {
        return updDate;
    }

    /**
     * 设置对象建立时间
     *
     * @param updDate 更新时间
     */
    public void setUpdDate(Date updDate) {
        this.updDate = updDate;
    }

    /**
     * 获得对象建立用户
     */
    public String getCrtUserCode() {
        return crtUserCode;
    }

    /**
     * 设置对象建立时间
     *
     * @param crtUserCode 建立用户
     */
    public void setCrtUserCode(String crtUserCode) {
        this.crtUserCode = crtUserCode;
    }

    /**
     * 获得对象更新用户
     */
    public String getUpdUserCode() {
        return updUserCode;
    }

    /**
     * 设置对象建立时间
     *
     * @param updUserCode 更新用户
     */
    public void setUpdUserCode(String updUserCode) {
        this.updUserCode = updUserCode;
    }

    /**
     * 获取创建机构编号
     *
     * @return
     */
    public String getCrtOrgCode() {
        return crtOrgCode;
    }

    /**
     * 设置创建机构编号
     *
     * @param crtOrgCode 机构编号
     */
    public void setCrtOrgCode(String crtOrgCode) {
        this.crtOrgCode = crtOrgCode;
    }

    /**
     * 获取最后更新机构编号
     *
     * @return
     */
    public String getUpdOrgCode() {
        return updOrgCode;
    }

    /**
     * 获取创建机构名称
     * @return 机构名称
     */
    public String getCrtOrgName() {
        return crtOrgName;
    }

    /**
     * 设置创建机构名称
     * @return 机构名称
     */
    public void setCrtOrgName(String crtOrgName) {
        this.crtOrgName = crtOrgName;
    }

    /**
     * 获取更新机构名称
     * @return 机构名称
     */
    public String getUpdOrgName() {
        return updOrgName;
    }

    /**
     * 设置更新机构名称
     * @return 机构名称
     */
    public void setUpdOrgName(String updOrgName) {
        this.updOrgName = updOrgName;
    }

    /**
     * 获取创建用户名称
     * @return 用户名称
     */
    public String getCrtUserName() {
        return crtUserName;
    }

    /**
     * 设置创建用户名称
     * @return 用户名称
     */
    public void setCrtUserName(String crtUserName) {
        this.crtUserName = crtUserName;
    }

    /**
     * 获取最后更新用户名称
     * @return 用户名称
     */
    public String getUpdUserName() {
        return updUserName;
    }

    /**
     * 设置最后更新用户名称
     * @return 用户名称
     */
    public void setUpdUserName(String updUserName) {
        this.updUserName = updUserName;
    }

    /**
     * 获取当前对象是否缓存
     *
     * @return
     */
    @Transient
    public boolean getCacheAble() {
        return cacheAble;
    }

    /**
     * 设置当前对象是否缓存
     *
     * @param cacheAble
     */
    public void setCacheAble(boolean cacheAble) {
        this.cacheAble = cacheAble;
    }

    /**
     * 设置最后更新机构编号
     *
     * @param updOrgCode
     */
    public void setUpdOrgCode(String updOrgCode) {
        this.updOrgCode = updOrgCode;
    }

    /**
     * 重写toString方法,对象在打印时的格式如下:<br/>
     * 例:com.innofi.component.metadata.table.pojo.MdTablePojo@19134f4[systemId=<null>,tableName=<null>,tableCnName=<null>,tableDesc=<null>,tableType=<null>,entityName=<null>,daoName=<null>,serviceName=<null>,actionName=<null>,uiName=<null>,loadMethod=<null>,tbsName=<null>,dataStatus=<null>,hisTabCode=<null>,isTreeStruct=<null>,isLog=<null>,status=<null>,verNo=<null>,id=<null>,version=0,crtDate=<null>,updDate=<null>,crtOrgCode=<null>,updOrgCode=<null>,crtUserCode=<null>,updUserCode=<null>,objStatus=<null>]
     *
     * @return 实体对象属性
     */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    /**
     * 获取当前对象hibernate的投影字段
     *
     * @return 投影字段名称列表
     */
    public List<String> getProjectionFields(){
    	return new ArrayList();
    }

    /**
     * 重写equals方法，
     * 例:com.innofi.component.metadata.table.pojo.MdTablePojo@19134f4[systemId=<null>,tableName=<null>,tableCnName=<null>,tableDesc=<null>,tableType=<null>,entityName=<null>,daoName=<null>,serviceName=<null>,actionName=<null>,uiName=<null>,loadMethod=<null>,tbsName=<null>,dataStatus=<null>,hisTabCode=<null>,isTreeStruct=<null>,isLog=<null>,status=<null>,verNo=<null>,id=<null>,version=0,crtDate=<null>,updDate=<null>,crtOrgCode=<null>,updOrgCode=<null>,crtUserCode=<null>,updUserCode=<null>,objStatus=<null>]
     *
     * @return 实体对象属性
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BasePojo basePojo = (BasePojo) o;
        if (id != null ? !id.equals(basePojo.id) : basePojo.id != null) return false;

        return true;
    }

    /**
     * 重写对象hashCode算法
     *
     * @return
     */
    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        return result;
    }

    @Transient
    public boolean getTreeStruct() {
        return treeStruct;
    }

    public void setTreeStruct(boolean treeStruct) {
        this.treeStruct = treeStruct;
    }

    @Transient
    public String getTreeParentIdFieldName() {
        return treeParentIdFieldName;
    }

    public void setTreeParentIdFieldName(String treeParentIdFieldName) {
        this.treeParentIdFieldName = treeParentIdFieldName;
    }

    @Transient
    public String getTreeSeqFieldName() {
        return treeSeqFieldName;
    }

    public void setTreeSeqFieldName(String treeSeqFieldName) {
        this.treeSeqFieldName = treeSeqFieldName;
    }

    @Transient
    public String getTreeIdFiledName() {
        return treeIdFiledName;
    }

    public void setTreeIdFiledName(String treeIdFiledName) {
        this.treeIdFiledName = treeIdFiledName;
    }
}
