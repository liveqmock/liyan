package com.innofi.framework.dao.pagination;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.innofi.framework.dao.hibernate.QueryConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @version 2.0
 * @author liumy2009@126.com
 * @found date: 2010-11-23
 * @found time: 20:40:56
 * 分页数据的包装类。
 */
public class Page<T> extends com.bstek.dorado.data.provider.Page {

    private static final long serialVersionUID = -6374785688279713579L;

    /**
     * 保存一份DoradoPage的副本
     */
    private com.bstek.dorado.data.provider.Page doradoPage;

    /*
     * 默认最大每页显示记录数
     */
    public static final int BIG_PAGE_SIZE = Integer.MAX_VALUE;

    /*
     * 默认每页显示记录数
     */
    public static final int DEFAULT_PAGE_SIZE = 30;

    /*
     * 显示记录数
     */
    private int pageSize = DEFAULT_PAGE_SIZE;

    /*
     * 当前页数
     */
    private int pageNo = 1;

    /*
     * 当前页数
     */
    private int firstEntityIndex;

    /*
     * 最后一页索引
     */
    private int lastEntityIndex;

    /*
     * 总记录数
     */
    private int entityCount;

    /*
     * 排序属性多个属性以','分割
     */
    protected String orderBy = null;

    /*
     * 排序规则,多个属性以','分割
     */
    protected String order = null;

    /*
     * 是否自动查询记录条数
     */
    protected boolean autoCount = true;

    /*
     * 记录集合
     */
    private List<T> entities;

    /*
     * 记录总页数
     */
    private int pageCount;


    public Page(com.bstek.dorado.data.provider.Page doradoPage){
        super(doradoPage.getPageSize(),doradoPage.getPageNo());
        this.doradoPage = doradoPage;
        this.pageSize = doradoPage.getPageSize();
        this.pageNo = doradoPage.getPageNo();
    }

    /**
     * @param pageSize 每页记录数
     * @param pageNo   页号
     */
    public Page(int pageSize, int pageNo) {
        super(pageSize,pageNo);
        this.pageSize = pageSize;
        this.pageNo = pageNo;
    }

    /**
     * 返回每一页的大小，即每页的记录数。
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 返回要提取的页的序号，该序号是从1开始计算的。
     */
    public int getPageNo() {
        return pageNo;
    }

    /**
     * 返回当前页中第一条记录对应的序号，该序号是从0开始计算的。<br>
     * 注意，此处在计算firstEntityIndex是不考虑实际提取过程中当前页是否存在的。
     */
    public int getFirstEntityIndex() {
        return firstEntityIndex;
    }

    /**
     * 返回当前页中最后一条记录对应的序号，该序号是从0开始计算的。<br>
     * 注意，此处在计算lastEntityIndex是不考虑实际提取过程中当前页是否存在或者记录数是否可达到pageSize的。
     */
    public int getLastEntityIndex() {
        return lastEntityIndex;
    }

    /**
     * 设置当页数据。
     */
    public void setEntities(List<T> entities) {
        //设置Dorado Page对象
        if(doradoPage!=null){
            doradoPage.setEntities(entities);
        }
        this.entities = entities;
    }

    /**
     * 返回当页数据。
     */
    @SuppressWarnings("unchecked")
    public List<T> getEntities() {
        return (entities != null) ? entities : Collections.EMPTY_LIST;
    }

    /**
     * 设置总记录数。
     * <p>
     * 此处的总记录数并不是指当页数据的总数，而是指整个结果的总数。 即每一页数据累计的总数。
     * </p>
     */
    public int getEntityCount() {
        return entityCount;
    }

    /**
     * 返回总记录数。
     * <p>
     * 此处的总记录数并不是指当页数据的总数，而是指整个结果的总数。即每一页数据累计的总数。
     * </p>
     */
    public void setEntityCount(int entityCount) {
        if (entityCount < 0) {
            throw new IllegalArgumentException(
                    "Illegal entityCount arguments. [entityCount="
                            + entityCount + "]");
        }

        //设置Dorado Page对象
        if(doradoPage!=null){
            doradoPage.setEntityCount(entityCount);
        }

        this.entityCount = entityCount;
        pageCount = ((entityCount - 1) / pageSize) + 1;
    }

    /**
     * 返回总的记录页数。
     */
    public int getPageCount() {
        return pageCount;
    }

    /**
     * 返回当页数据的迭代器。
     */
    public Iterator<T> iterator() {
        if (entities != null) {
            return entities.iterator();
        } else {
            return null;
        }
    }

    /**
     * 获得排序字段,无默认值.多个排序字段时用','分隔.
     */
    public String getOrderBy() {
        return orderBy;
    }

    /**
     * 设置排序字段,多个排序字段时用','分隔.
     */
    public void setOrderBy(final String orderBy) {
        this.orderBy = orderBy;
    }

    public Page<T> orderBy(final String theOrderBy) {
        setOrderBy(theOrderBy);
        return this;
    }

    /**
     * 获得排序方向.
     */
    public String getOrder() {
        return order;
    }

    /**
     * 设置排序方式向.
     *
     * @param order 可选值为desc或asc,多个排序字段时用','分隔.
     */
    public void setOrder(final String order) {
        //检查order字符串的合法值
        String[] orders = StringUtils.split(StringUtils.lowerCase(order), ',');
        for (String orderStr : orders) {
            if (!StringUtils.equals(QueryConstants.ORDER_DESC, orderStr) && !StringUtils.equals(QueryConstants.ORDER_ASC, orderStr)) {
                throw new IllegalArgumentException("排序方向" + orderStr + "不是合法值");
            }
        }

        this.order = StringUtils.lowerCase(order);
    }

    public Page<T> order(final String theOrder) {
        setOrder(theOrder);
        return this;
    }

    /**
     * 是否已设置排序字段,无默认值.
     */
    public boolean isOrderBySetted() {
        return (StringUtils.isNotBlank(orderBy) && StringUtils.isNotBlank(order));
    }

    /**
     * 查询对象时是否自动另外执行count查询获取总记录数, 默认为false.
     */
    public boolean isAutoCount() {
        return autoCount;
    }

    /**
     * 查询对象时是否自动另外执行count查询获取总记录数.
     */
    public void setAutoCount(final boolean autoCount) {
        this.autoCount = autoCount;
    }

    public Page<T> autoCount(final boolean theAutoCount) {
        setAutoCount(theAutoCount);
        return this;
    }

    /**
     * 是否还有下一页.
     */
    public boolean isHasNext() {
        return (pageNo + 1 <= getPageCount());
    }

    /**
     * 取得下页的页号, 序号从1开始.
     * 当前页为尾页时仍返回尾页序号.
     */
    public int getNextPage() {
        if (isHasNext()) {
            return pageNo + 1;
        } else {
            return pageNo;
        }
    }

    /**
     * 是否还有上一页.
     */
    public boolean isHasPre() {
        return (pageNo - 1 >= 1);
    }

    /**
     * 取得上页的页号, 序号从1开始.
     * 当前页为首页时返回首页序号.
     */
    public int getPrePage() {
        if (isHasPre()) {
            return pageNo - 1;
        } else {
            return pageNo;
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}