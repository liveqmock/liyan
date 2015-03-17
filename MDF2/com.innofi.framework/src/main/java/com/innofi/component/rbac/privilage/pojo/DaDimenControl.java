package com.innofi.component.rbac.privilage.pojo;

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
 * @author todo 开发人员姓名 邮箱
 * @version todo 当前版本 日期
 *          todo 类描述
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
@Entity
@Table(name = "DA_DIMEN_CONTROL")

public class DaDimenControl extends BasePojo {

    /**
     * 维度ID
     */
    private String dimenId;

    /**
     * 表权限控制策略ID
     */
    private String tableAuthId;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "DIMEN_CONTROL_ID")
    public String getId() {
        return super.getId();
    }

    @Column(name = "DIMEN_ID")
    public String getDimenId() {
        return this.dimenId;
    }

    public void setDimenId(String dimenId) {
        this.dimenId = dimenId;
    }

    @Column(name = "TABLE_AUTH_ID")
    public String getTableAuthId() {
        return this.tableAuthId;
    }

    public void setTableAuthId(String tableAuthId) {
        this.tableAuthId = tableAuthId;
    }

}