package com.innofi.framework.utils.collection;

import org.apache.commons.collections.map.LinkedMap;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 对象列表的实现。此类相当于org.apache.commons.collections.map.LinkedMap 的Adapter
 * Copyright (c) 2010 北京名晟信息技术有限公司
 * all rights reserved.
 * author: liumy
 * found date: 2010-11-23
 * found time: 20:40:56
 */
public class ObjectList implements ObjectCollection, Serializable {
    private LinkedMap objects;

    public ObjectList() {
        this.objects = new LinkedMap();
    }

    /**
     * 添加对象方法，出现同一个key时抛出DuplicateKeyException
     *
     * @param key    对象key
     * @param object 对象
     */
    public void add(Object key, Object object) {
        if (this.objects.containsKey(key)) {
            throw new DuplicateKeyException(key);
        }
        this.objects.put(key, object);
    }

    /**
     * 添加对象方法,同样的key如果存在则覆盖
     *
     * @param key    对象key
     * @param object 对象
     */
    public void forceAdd(Object key, Object object) {
        this.objects.put(key, object);
    }

    /**
     * 添加对象方法，指定对象存放位置
     *
     * @param index  索引
     * @param key    对象key
     * @param object 对象
     */
    public void add(int index, Object key, Object object) {
        Object k;
        add(key, object);

        LinkedMap tempMap = new LinkedMap();
        int size = this.objects.size();
        for (int i = size - 1; i >= index; --i) {
            k = this.objects.get(i);
            tempMap.put(k, this.objects.remove(i));
        }

        add(key, object);
        size = tempMap.size();
        for (int i = size - 1; i >= 0; --i) {
            k = tempMap.get(i);
            this.objects.put(k, tempMap.get(k));
        }
    }

    /**
     * 根据指定key获得对应对象
     *
     * @param key 参数key
     * @return 对应对象
     */
    public Object get(Object key) {
        return this.objects.get(key);
    }

    /**
     * 根据索引获得对应对象
     *
     * @param index 索引
     * @return 对应对象
     */
    public Object get(int index) {
        return this.objects.getValue(index);
    }

    /**
     * 设置索引所指向的对象
     *
     * @param index  索引
     * @param object 对象
     */
    public void set(int index, Object object) {
        Object key = this.objects.get(index);
        if (key != null) {
            this.objects.put(key, object);
        } else
            throw new ArrayIndexOutOfBoundsException(index);
    }

    /**
     * 返回指定对象在集合中的位置
     *
     * @param key 对象
     * @return 返回对象所在位置
     */
    public int indexOf(Object key) {
        return this.objects.indexOf(key);
    }

    /**
     * 返回集合索引位置的key
     *
     * @param index 索引的位置
     * @return 索引对应的key
     */
    public Object getKey(int index) {
        return this.objects.get(index);
    }

    /**
     * 删除指定对象
     *
     * @param key 对象key
     * @return 返回key对应的对象
     */
    public Object remove(Object key) {
        return this.objects.remove(key);
    }

    /**
     * 删除索引位置的对象
     *
     * @param index 索引位置
     * @return 索引对应的对象
     */
    public Object remove(int index) {
        return this.objects.remove(index);
    }

    /**
     * 清空对象集合
     */
    public void removeAll() {
        this.objects.clear();
    }

    /**
     * 对象集合大小
     *
     * @return
     */
    public int size() {
        return this.objects.size();
    }

    /**
     * 获得对象集合key列表
     *
     * @return 对象集合key的列表
     */
    public List keyList() {
        return this.objects.asList();
    }

    /**
     * 获得对象集合
     *
     * @return 对象的集合
     */
    public Map map() {
        return this.objects;
    }

    /**
     * 获得对象集合的迭代器
     *
     * @return 对象迭代器
     */
    public Iterator iterator() {
        return this.objects.asList().iterator();
    }

    /**
     * 复制对象集合
     *
     * @return 对象的集合
     * @throws CloneNotSupportedException
     */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public boolean equals(Object obj) {
        if (obj instanceof ObjectList) {
            return this.objects.equals(((ObjectList) obj).objects);
        }

        return false;
    }

    public int hashCode() {
        if (this.objects.isEmpty()) {
            return 0;
        }

        return this.objects.hashCode();
    }

    public String toString() {
        return this.objects.toString();
    }
}