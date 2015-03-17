package com.innofi.framework.utils.collection;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 对象列表接口,定义对象列表的基本方法。
 * Copyright (c) 2010 北京名晟信息技术有限公司
 * all rights reserved.
 * author: liumy
 * found date: 2010-11-23
 * found time: 20:40:56
 */
public abstract interface ObjectCollection extends Cloneable, Serializable {
    /**
     * 添加对象方法，出现同一个key时抛出DuplicateKeyException
     *
     * @param key    对象key
     * @param object 对象
     */
    public abstract void add(Object key, Object object);

    /**
     * 添加对象方法,同样的key如果存在则覆盖
     *
     * @param key    对象key
     * @param object 对象
     */
    public abstract void forceAdd(Object key, Object object);

    /**
     * 添加对象方法，指定对象存放位置
     *
     * @param index  索引
     * @param key    对象key
     * @param object 对象
     */
    public abstract void add(int index, Object key, Object object);

    /**
     * 根据指定key获得对应对象
     *
     * @param key 参数key
     * @return 对应对象
     */
    public abstract Object get(Object key);

    /**
     * 根据索引获得对应对象
     *
     * @param index 索引
     * @return 对应对象
     */
    public abstract Object get(int index);

    /**
     * 设置索引所指向的对象
     *
     * @param index  索引
     * @param object 对象
     */
    public abstract void set(int index, Object object);

    /**
     * 返回指定对象在集合中的位置
     *
     * @param object 对象
     * @return 返回对象所在位置
     */
    public abstract int indexOf(Object object);

    /**
     * 返回集合索引位置的key
     *
     * @param index 索引的位置
     * @return 索引对应的key
     */
    public abstract Object getKey(int index);

    /**
     * 删除指定对象
     *
     * @param key 对象key
     * @return 返回key对应的对象
     */
    public abstract Object remove(Object key);

    /**
     * 删除索引位置的对象
     *
     * @param index 索引位置
     * @return 索引对应的对象
     */
    public abstract Object remove(int index);

    /**
     * 清空对象集合
     */
    public abstract void removeAll();

    /**
     * 对象集合大小
     *
     * @return
     */
    public abstract int size();

    /**
     * 获得对象集合key列表
     *
     * @return 对象集合key的列表
     */
    public abstract List keyList();

    /**
     * 获得对象集合
     *
     * @return 对象的集合
     */
    public abstract Map map();

    /**
     * 获得对象集合的迭代器
     *
     * @return 对象迭代器
     */
    public abstract Iterator iterator();

    /**
     * 复制对象集合
     *
     * @return 对象的集合
     * @throws CloneNotSupportedException
     */
    public abstract Object clone() throws CloneNotSupportedException;

}