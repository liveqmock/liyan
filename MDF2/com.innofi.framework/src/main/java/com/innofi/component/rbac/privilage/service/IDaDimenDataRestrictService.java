
package com.innofi.component.rbac.privilage.service;

import com.innofi.component.rbac.privilage.pojo.DaDimenDataRestrict;
import com.innofi.component.rbac.privilage.pojo.DaTablePolicy;
import com.innofi.framework.service.IBaseService;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 * 功能/ 模块：权限管理模块，数据表约束信息服务
 * @author liumy2009@126.com
 * @version 2.0
 * @found date: 2010-11-23
 * @found time: 20:40:56
 * 修订历史：日期  作者  参考  描述
 */
public interface IDaDimenDataRestrictService extends IBaseService<DaDimenDataRestrict, String> {

    /**
     * 保存数据策略，策略约束数据，维度数据
     * @param policy     策略对象
     * @param objs       约束对象
     * @param parameter
     */
    public void saveDaDimenDataRestricts(DaTablePolicy policy, Collection<DaDimenDataRestrict> objs, Map<String, String> parameter);

    /**
     * 根据表策略ID，删除不存在关联的垃圾约束数据及维度数据
     * @param tableAuthId 表策略编号
     */
    public void removeNotJoinData(String tableAuthId);

    /**
     * 根据策略编号，上级约束编号获取约束对象
     * @param policyId 策略ID
     * @param parentId 上级约束ID
     * @return 符合条件的约束对象集合
     */
    public List<DaDimenDataRestrict> findDaDimenRestrictByPolicyAndParentId(String policyId,String parentId);

}