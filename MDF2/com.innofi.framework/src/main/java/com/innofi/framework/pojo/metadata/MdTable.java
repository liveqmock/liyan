package com.innofi.framework.pojo.metadata;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

import com.innofi.framework.FrameworkConstants;
import com.innofi.framework.spring.context.ContextHolder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.innofi.framework.pojo.BasePojo;

/**
 * 功能/ 模块：元数据模块
 *
 * @author 张磊 alex.zhang@innofi.com.cn
 * @version 2.0.0 12-11-30
 *          元数据表体对象
 *          修订历史：2013年1月20日
 *          日期  作者  参考  描述
 *          2013年1月20日  刘名寓 修订 equals方法 构造方法 添加版本递增及初始化状态逻辑
 *          北京名晟信息技术有限公司版权所有.
 */
@Entity
@Table(name = "MD_TABLE")

public class MdTable extends BasePojo {

    public MdTable() {
        verNo = new BigDecimal(1);
        status = FrameworkConstants.STATUS_EFFECTIVE;
    	String adminString = ContextHolder.getSystemProperties().getString("mtp.system.admin.account","admin");
    	if(adminString.indexOf(",")!=-1){
    		crtUserCode = adminString.split(",")[0];
    	}else{
            crtUserCode = adminString;
    	}
    }

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "TABLE_ID")
    @Override
    public String getId() {
        return super.getId();
    }

    @Column(name = "CRT_ORG_CODE")
    public String getCrtOrgCode() {
        return super.getCrtOrgCode();
    }

    @Column(name = "UPD_ORG_CODE")
    public String getUpdOrgCode() {
        return super.getUpdOrgCode();
    }

    @Column(name = "CRT_USER_CODE")
    public String getCrtUserCode() {
        return super.getCrtUserCode();
    }

    @Column(name = "CRT_DATE")
    public Date getCrtDate() {
        return super.getCrtDate();
    }

    @Column(name = "UPD_DATE")
    public Date getUpdDate() {
        return super.getUpdDate();
    }

    @Column(name = "UPD_USER_CODE")
    public String getUpdUserCode() {
        return super.getUpdUserCode();
    }

    /**
     * 系统ID
     */
    private String systemCode;
    /**
     * 表名
     */
    private String tableName;
    /**
     * 表中文名称
     */
    private String tableCnName;
    /**
     * 表描述
     */
    private String tableDesc;
    /**
     * 表类型:1-系统表2-业务（应用）表3-平台表4-源数据表
     */
    private String tableType;
    /**
     * 实体对象名
     */
    private String entityName;
    /**
     * DAO类名：包含类包路径
     */
    private String daoName;
    /**
     * SERVICE类名：包含类包路径
     */
    private String serviceName;
    /**
     * ACTION类名：包含类包路径
     */
    private String actionName;
    /**
     * UI界面：包含类包路径
     */
    private String uiName;
    /**
     * 加载方式
     */
    private String loadMethod;
    /**
     * 存储表空间
     */
    private String tbsName;
    /**
     * 数据存储状态:1-只保存最新数据2-保存历史数据3-历史数据保存历史表
     */
    private String dataStatus;
    /**
     * 历史数据保存表名
     */
    private String hisTabCode;
    /**
     * 是否树型结构
     */
    private String isTreeStruct;
    /**
     * 是否记操作日志
     */
    private String isLog;
    /**
     * 有效状态:1-有效2-无效
     */
    private String status;
    /**
     * 版本号
     */
    private BigDecimal verNo;
    /**
     * 是否保存锁定
     */
    private String isCloseSaved;
    /**
     * 是否保存锁定
     */
    private String dataSetName;
    /**
     * 是否保存锁定
     */
    private String queryMethod;
    /**
     * 是否进行权限控制
     */
    private String isDaControl;

    /**
     * 是否动态维护表
     */
    private String isMaintain;

    @Column(name = "SYSTEM_CODE")
    public String getSystemCode() {
        return this.systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    @Column(name = "TABLE_NAME")
    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Column(name = "TABLE_CN_NAME")
    public String getTableCnName() {
        return this.tableCnName;
    }

    public void setTableCnName(String tableCnName) {
        this.tableCnName = tableCnName;
    }

    @Column(name = "TABLE_DESC")
    public String getTableDesc() {
        return this.tableDesc;
    }

    public void setTableDesc(String tableDesc) {
        this.tableDesc = tableDesc;
    }

    @Column(name = "TABLE_TYPE")
    public String getTableType() {
        return this.tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    @Column(name = "ENTITY_NAME")
    public String getEntityName() {
        return this.entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    @Column(name = "DAO_NAME")
    public String getDaoName() {
        return this.daoName;
    }

    public void setDaoName(String daoName) {
        this.daoName = daoName;
    }

    @Column(name = "SERVICE_NAME")
    public String getServiceName() {
        return this.serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Column(name = "ACTION_NAME")
    public String getActionName() {
        return this.actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    @Column(name = "UI_NAME")
    public String getUiName() {
        return this.uiName;
    }

    public void setUiName(String uiName) {
        this.uiName = uiName;
    }

    @Column(name = "LOAD_METHOD")
    public String getLoadMethod() {
        return this.loadMethod;
    }

    public void setLoadMethod(String loadMethod) {
        this.loadMethod = loadMethod;
    }

    @Column(name = "TBS_NAME")
    public String getTbsName() {
        return this.tbsName;
    }

    public void setTbsName(String tbsName) {
        this.tbsName = tbsName;
    }

    @Column(name = "DATA_STATUS")
    public String getDataStatus() {
        return this.dataStatus;
    }

    public void setDataStatus(String dataStatus) {
        this.dataStatus = dataStatus;
    }

    @Column(name = "HIS_TAB_CODE")
    public String getHisTabCode() {
        return this.hisTabCode;
    }

    public void setHisTabCode(String hisTabCode) {
        this.hisTabCode = hisTabCode;
    }

    @Column(name = "IS_TREE_STRUCT")
    public String getIsTreeStruct() {
        return this.isTreeStruct;
    }

    public void setIsTreeStruct(String isTreeStruct) {
        this.isTreeStruct = isTreeStruct;
    }

    @Column(name = "IS_LOG")
    public String getIsLog() {
        return this.isLog;
    }

    public void setIsLog(String isLog) {
        this.isLog = isLog;
    }

    @Column(name = "STATUS")
    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "VER_NO", nullable = false, columnDefinition = "number default 1")
    public BigDecimal getVerNo() {
        return this.verNo;
    }

    public void setVerNo(BigDecimal verNo) {
        this.verNo = verNo;
    }

    @Column(name = "IS_CLOSE_SAVED")
    public String getIsCloseSaved() {
        return isCloseSaved;
    }

    public void setIsCloseSaved(String isCloseSaved) {
        this.isCloseSaved = isCloseSaved;
    }

    @Column(name = "DATASET_NAME")
    public String getDataSetName() {
        return dataSetName;
    }

    public void setDataSetName(String dataSetName) {
        this.dataSetName = dataSetName;
    }

    @Column(name = "QUERY_METHOD")
    public String getQueryMethod() {
        return queryMethod;
    }

    public void setQueryMethod(String queryMethod) {
        this.queryMethod = queryMethod;
    }

    @Column(name = "IS_DA_CONTROL")
    public String getIsDaControl() {
        return isDaControl;
    }

    public void setIsDaControl(String isDaControl) {
        this.isDaControl = isDaControl;
    }

    @Column(name = "IS_MAINTAIN")
    public String getIsMaintain() {
        return isMaintain;
    }

    public void setIsMaintain(String maintain) {
        isMaintain = maintain;
    }
    
    

    /**
     * 使用Hibernate缓存
     *
     * @return true
     */
    @Transient
    public boolean getCacheAble() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        MdTable tablePojo = (MdTable) o;

        if (actionName != null ? !actionName.equals(tablePojo.actionName) : tablePojo.actionName != null) return false;
        if (daoName != null ? !daoName.equals(tablePojo.daoName) : tablePojo.daoName != null) return false;
        if (dataStatus != null ? !dataStatus.equals(tablePojo.dataStatus) : tablePojo.dataStatus != null) return false;
        if (entityName != null ? !entityName.equals(tablePojo.entityName) : tablePojo.entityName != null) return false;
        if (hisTabCode != null ? !hisTabCode.equals(tablePojo.hisTabCode) : tablePojo.hisTabCode != null) return false;
        if (isCloseSaved != null ? !isCloseSaved.equals(tablePojo.isCloseSaved) : tablePojo.isCloseSaved != null)
            return false;
        if (isLog != null ? !isLog.equals(tablePojo.isLog) : tablePojo.isLog != null) return false;
        if (isTreeStruct != null ? !isTreeStruct.equals(tablePojo.isTreeStruct) : tablePojo.isTreeStruct != null)
            return false;
        if (loadMethod != null ? !loadMethod.equals(tablePojo.loadMethod) : tablePojo.loadMethod != null) return false;
        if (serviceName != null ? !serviceName.equals(tablePojo.serviceName) : tablePojo.serviceName != null)
            return false;
        if (systemCode != null ? !systemCode.equals(tablePojo.systemCode) : tablePojo.systemCode != null) return false;
        if (tableCnName != null ? !tableCnName.equals(tablePojo.tableCnName) : tablePojo.tableCnName != null)
            return false;
        if (tableDesc != null ? !tableDesc.equals(tablePojo.tableDesc) : tablePojo.tableDesc != null) return false;
        if (tableName != null ? !tableName.equals(tablePojo.tableName) : tablePojo.tableName != null) return false;
        if (tbsName != null ? !tbsName.equals(tablePojo.tbsName) : tablePojo.tbsName != null) return false;
        if (uiName != null ? !uiName.equals(tablePojo.uiName) : tablePojo.uiName != null) return false;

        return true;
    }

}