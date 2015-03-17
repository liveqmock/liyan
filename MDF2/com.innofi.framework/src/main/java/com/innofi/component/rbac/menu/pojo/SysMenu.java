package com.innofi.component.rbac.menu.pojo;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import com.innofi.framework.pojo.BasePojo;
import org.hibernate.annotations.GenericGenerator;

/**
 * ${model.modelCnName}
 */

@Entity
@Table(name = "SYS_MENU")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="sysMenuType")
@DiscriminatorValue("sysMenu")

public class SysMenu extends BasePojo {
    /**
     * 资源图片
     */
    private String menuPict;

    /**
     * 菜单路径URL
     */
    private String menuUrl;

    /**
     * 菜单编码
     */
    private String menuCode;

    /**
     * 菜单显示
     */
    private String menuLabel;

    /**
     * 菜单层次
     */
    private BigDecimal menuLevel;

    /**
     * 菜单名称
     */
    private String menuName;


    /**
     * 窗口打开方式
     */
    private String openMode;

    /**
     * 是否相对路径
     */
    private String isRelativePath;

    /**
     * 是否启用
     */
    private String isStart;

    /**
     * 提示信息
     */
    private String menuTip;

    /**
     * 所属父菜单
     */
    private String parentMenuId;

    /**
     * 同级菜单顺序号
     */
    private BigDecimal menuSeq;

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

    private String treeSeq;

    private boolean selectFlag = false;

    @Column(name = "MENU_PICT")
    public String getMenuPict() {
        return this.menuPict;
    }

    public void setMenuPict(String menuPict) {
        this.menuPict = menuPict;
    }

    @Column(name = "MENU_URL")
    public String getMenuUrl() {
        return this.menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    @Column(name = "MENU_CODE")
    public String getMenuCode() {
        return this.menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    @Column(name = "MENU_LABEL")
    public String getMenuLabel() {
        return this.menuLabel;
    }

    public void setMenuLabel(String menuLabel) {
        this.menuLabel = menuLabel;
    }

    @Column(name = "MENU_LEVEL")
    public BigDecimal getMenuLevel() {
        return this.menuLevel;
    }

    public void setMenuLevel(BigDecimal menuLevel) {
        this.menuLevel = menuLevel;
    }

    @Column(name = "MENU_NAME")
    public String getMenuName() {
        return this.menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "MENU_ID")
    public String getId() {
        return super.getId();
    }

    @Column(name = "OPEN_MODE")
    public String getOpenMode() {
        return this.openMode;
    }

    public void setOpenMode(String openMode) {
        this.openMode = openMode;
    }

    @Column(name = "IS_RELATIVE_PATH")
    public String getIsRelativePath() {
        return this.isRelativePath;
    }

    public void setIsRelativePath(String isRelativePath) {
        this.isRelativePath = isRelativePath;
    }

    @Column(name = "IS_START")
    public String getIsStart() {
        return this.isStart;
    }

    public void setIsStart(String isStart) {
        this.isStart = isStart;
    }

    @Column(name = "MENU_TIP")
    public String getMenuTip() {
        return this.menuTip;
    }

    public void setMenuTip(String menuTip) {
        this.menuTip = menuTip;
    }

    @Column(name = "PARENT_MENU_ID")
    public String getParentMenuId() {
        return this.parentMenuId;
    }

    public void setParentMenuId(String parentMenuId) {
        this.parentMenuId = parentMenuId;
    }

    @Column(name = "MENU_SEQ")
    public BigDecimal getMenuSeq() {
        return this.menuSeq;
    }

    public void setMenuSeq(BigDecimal menuSeq) {
        this.menuSeq = menuSeq;
    }

    @Column(name = "CRT_ORG_CODE")
    public String getCrtOrgCode() {
        return this.crtOrgCode;
    }

    public void setCrtOrgCode(String crtOrgCode) {
        this.crtOrgCode = crtOrgCode;
    }

    @Column(name = "CRT_DATE")
    public Date getCrtDate() {
        return this.crtDate;
    }

    public void setCrtDate(Date crtDate) {
        this.crtDate = crtDate;
    }

    @Column(name = "CRT_USER_CODE")
    public String getCrtUserCode() {
        return this.crtUserCode;
    }

    public void setCrtUserCode(String crtUserCode) {
        this.crtUserCode = crtUserCode;
    }

    @Column(name = "UPD_ORG_CODE")
    public String getUpdOrgCode() {
        return this.updOrgCode;
    }

    public void setUpdOrgCode(String updOrgCode) {
        this.updOrgCode = updOrgCode;
    }

    @Column(name = "UPD_DATE")
    public Date getUpdDate() {
        return this.updDate;
    }

    public void setUpdDate(Date updDate) {
        this.updDate = updDate;
    }

    @Column(name = "UPD_USER_CODE")
    public String getUpdUserCode() {
        return this.updUserCode;
    }

    public void setUpdUserCode(String updUserCode) {
        this.updUserCode = updUserCode;
    }

    @Column(name = "TREE_SEQ")
    public String getTreeSeq() {
        return treeSeq;
    }

    public void setTreeSeq(String treeSeq) {
        this.treeSeq = treeSeq;
    }

    @Transient
    public boolean getSelectFlag() {
        return selectFlag;
    }

    public void setSelectFlag(boolean selectFlag) {
        this.selectFlag = selectFlag;
    }
}