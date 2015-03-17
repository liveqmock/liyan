package com.innofi.framework.utils.collection;

import com.innofi.framework.utils.comparator.CaseInsensitiveComparator;
import com.innofi.framework.utils.object.ObjectUtil;

import java.util.*;

/**
 * Copyright (c) 2010 北京名晟信息技术有限公司
 * all rights reserved.
 * author: liumy
 * found date: 2010-1-8
 * found time: 21:25:38
 * 集合工具类
 */
public abstract class CollectionUtil {

    /**
     * 如果提供的Collection为null <codebuilder>null</codebuilder> 或者为 empty 返回<codebuilder>true</codebuilder>否则返回 <codebuilder>false</codebuilder>.
     *
     * @param collection 校验给定 Collection
     * @return 返回是否为空
     */
    public static boolean isEmpty(Collection collection) {
        return (collection == null || collection.isEmpty());
    }

    /**
     * 如果提供的Map为null <codebuilder>null</codebuilder> 或者为 empty 返回<codebuilder>true</codebuilder>否则返回 <codebuilder>false</codebuilder>.
     *
     * @param map 校验给定 Map
     * @return 返回是否为空
     */
    public static boolean isEmpty(Map map) {
        return (map == null || map.isEmpty());
    }

    /**
     * 如果提供的Collection为null <codebuilder>null</codebuilder> 或者为 empty 返回<codebuilder>false</codebuilder>否则返回 <codebuilder>true</codebuilder>.
     *
     * @param c 校验给定 Collection
     * @return 返回是否非空
     */
    public static boolean isNotEmpty(Collection c) {
        return (c != null && c.size() > 0);
    }

    /**
     * 将给定的数据组对象以ArrayList方式返回
     *
     * @param a   指定的数据
     * @param <E> 存储的元素
     * @return 转换后的ArrayList对象
     */
    public static <E> List<E> arrayList(E... a) {
        ArrayList<E> result = new ArrayList<E>(a.length);
        result.addAll(Arrays.asList(a));
        return result;
    }

    /**
     * 将source复制一份返回
     *
     * @param source 期望被复制的ArrayList
     * @param <E>    节点类型
     * @return
     */
    public static <E> List<E> arrayList(List<E> source) {
        return new ArrayList<E>(source);
    }

    /**
     * 将数据转换为只读的ArrayList
     *
     * @param a 源数组
     * @return 只读的ArrayList
     */
    public static <E> List<E> readOnlyList(E... a) {
        return Collections.unmodifiableList(arrayList(a));
    }

    /**
     * 合并给定的数据和集合当中的元素
     *
     * @param array      将要被加入到集合当中的数组 (可以为 <codebuilder>null</codebuilder>)
     * @param collection 合并后的集合对象
     */
    @SuppressWarnings("unchecked")
    public static void mergeArrayIntoCollection(Object array, Collection collection) {
        if (collection == null) {
            throw new IllegalArgumentException("Collection must not be null");
        }
        Object[] arr = ObjectUtil.toObjectArray(array);
        for (Object elem : arr) {
            collection.add(elem);
        }
    }

    /**
     * 将属性集合并到给定的Map中
     *
     * @param props 将要被加入到集合当中的属性集 (可以为 <codebuilder>null</codebuilder>)
     * @param map   合并后的Map对象
     */
    @SuppressWarnings("unchecked")
    public static void mergePropertiesIntoMap(Properties props, Map map) {
        if (map == null) {
            throw new IllegalArgumentException("Map must not be null");
        }
        if (props != null) {
            for (Enumeration en = props.propertyNames(); en.hasMoreElements(); ) {
                String key = (String) en.nextElement();
                Object value = props.getProperty(key);
                if (value == null) {
                    value = props.get(key);
                }
                map.put(key, value);
            }
        }
    }


    /**
     * 校验给定的迭代器当中是否包含特定对象
     *
     * @param iterator 迭代器
     * @param element  检查的节电
     * @return 存在<codebuilder>true</codebuilder>否则<codebuilder>false</codebuilder>
     */
    public static boolean contains(Iterator iterator, Object element) {
        if (iterator != null) {
            while (iterator.hasNext()) {
                Object candidate = iterator.next();
                if (ObjectUtil.nullSafeEquals(candidate, element)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 校验给定的枚举集合当中是否包含特定对象
     *
     * @param enumeration 枚举集合
     * @param element     指定对象
     * @return 存在<codebuilder>true</codebuilder>否则<codebuilder>false</codebuilder>
     */
    public static boolean contains(Enumeration enumeration, Object element) {
        if (enumeration != null) {
            while (enumeration.hasMoreElements()) {
                Object candidate = enumeration.nextElement();
                if (ObjectUtil.nullSafeEquals(candidate, element)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 校验给定的集合当中是否包含特定对象，比较方式为内存地址
     *
     * @param collection 指定集合
     * @param element    指定对象
     * @return 存在<codebuilder>true</codebuilder>否则<codebuilder>false</codebuilder>
     */
    public static boolean containsInstance(Collection collection, Object element) {
        if (collection != null) {
            for (Object candidate : collection) {
                if (candidate == element) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 校验给定的集合当中是否包含子集合
     *
     * @param source     指定集合
     * @param candidates 子集合
     * @return 存在<codebuilder>true</codebuilder>否则<codebuilder>false</codebuilder>
     */
    public static boolean containsAny(Collection source, Collection candidates) {
        if (isEmpty(source) || isEmpty(candidates)) {
            return false;
        }
        for (Object candidate : candidates) {
            if (source.contains(candidate)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 从指定集合中查找子集合，返回第一个匹配结果
     *
     * @param source     指定集合
     * @param candidates 子集合
     * @return 存在<codebuilder>true</codebuilder>否则<codebuilder>false</codebuilder>
     */
    public static Object findFirstMatch(Collection source, Collection candidates) {
        if (isEmpty(source) || isEmpty(candidates)) {
            return null;
        }
        for (Object candidate : candidates) {
            if (source.contains(candidate)) {
                return candidate;
            }
        }
        return null;
    }

    /**
     * 创建一个大小写不敏感的有序的Set
     *
     * @return 大小写不敏感的有序的Set
     */
    public static Set<String> caseInsensitiveSet() {
        return new TreeSet<String>(CaseInsensitiveComparator.INSTANCE);
    }

    /**
     * 创建一个大小写不敏感的有序的Set，并将数组当中的元素加到Set中
     *
     * @param a 指定数组
     * @return 已添加数组中数据的Set
     */
    public static Set<String> caseInsensitiveSet(String... a) {
        Set<String> result = caseInsensitiveSet();
        result.addAll(Arrays.asList(a));
        return result;
    }

    /**
     * 创建一个大小写不敏感的有序的Set，并将Set中以及数组当中的元素加到Set中
     *
     * @param base 指定Set
     * @param a    指定数组
     * @return 已添加上述二者数据的Set
     */
    public static Set<String> caseInsensitiveSet(Set<String> base, String... a) {
        Set<String> result = caseInsensitiveSet();
        result.addAll(base);
        result.addAll(Arrays.asList(a));
        return result;
    }

    /**
     * Find a single value of the given type in the given Collection.
     *
     * @param collection the Collection to search
     * @param type       the type to look for
     * @return a value of the given type found if there is a clear match,
     *         or <codebuilder>null</codebuilder> if none or more than one such value found
     */
    @SuppressWarnings("unchecked")
    public static <T> T findValueOfType(Collection<?> collection, Class<T> type) {
        if (isEmpty(collection)) {
            return null;
        }
        T value = null;
        for (Object element : collection) {
            if (type == null || type.isInstance(element)) {
                if (value != null) {
                    // More than one value found... no clear single value.
                    return null;
                }
                value = (T) element;
            }
        }
        return value;
    }

    /**
     * Find a single value of one of the given types in the given Collection:
     * searching the Collection for a value of the first type, then
     * searching for a value of the second type, etc.
     *
     * @param collection the collection to search
     * @param types      the types to look for, in prioritized order
     * @return a value of one of the given types found if there is a clear match,
     *         or <codebuilder>null</codebuilder> if none or more than one such value found
     */
    public static Object findValueOfType(Collection<?> collection, Class<?>[] types) {
        if (isEmpty(collection) || ObjectUtil.isEmpty(types)) {
            return null;
        }
        for (Class<?> type : types) {
            Object value = findValueOfType(collection, type);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    /**
     * Determine whether the given Collection only contains a single unique object.
     *
     * @param collection the Collection to check
     * @return <codebuilder>true</codebuilder> if the collection contains a single reference or
     *         multiple references to the same instance, <codebuilder>false</codebuilder> else
     */
    public static boolean hasUniqueObject(Collection collection) {
        if (isEmpty(collection)) {
            return false;
        }
        boolean hasCandidate = false;
        Object candidate = null;
        for (Object elem : collection) {
            if (!hasCandidate) {
                hasCandidate = true;
                candidate = elem;
            } else if (candidate != elem) {
                return false;
            }
        }
        return true;
    }

    /**
     * Find the common element type of the given Collection, if any.
     *
     * @param collection the Collection to check
     * @return the common element type, or <codebuilder>null</codebuilder> if no clear
     *         common type has been found (or the collection was empty)
     */
    public static Class<?> findCommonElementType(Collection collection) {
        if (isEmpty(collection)) {
            return null;
        }
        Class<?> candidate = null;
        for (Object val : collection) {
            if (val != null) {
                if (candidate == null) {
                    candidate = val.getClass();
                } else if (candidate != val.getClass()) {
                    return null;
                }
            }
        }
        return candidate;
    }

    /**
     * Marshal the elements from the given enumeration into an array of the given type.
     * Enumeration elements must be assignable to the type of the given array. The array
     * returned will be a different instance than the array given.
     */
    public static <A, E extends A> A[] toArray(Enumeration<E> enumeration, A[] array) {
        ArrayList<A> elements = new ArrayList<A>();
        while (enumeration.hasMoreElements()) {
            elements.add(enumeration.nextElement());
        }
        return elements.toArray(array);
    }

    /**
     * Adapt an enumeration to an iterator.
     *
     * @param enumeration the enumeration
     * @return the iterator
     */
    public static <E> Iterator<E> toIterator(Enumeration<E> enumeration) {
        return new EnumerationIterator<E>(enumeration);
    }

    /**
     * Iterator wrapping an Enumeration.
     */
    private static class EnumerationIterator<E> implements Iterator<E> {

        private Enumeration<E> enumeration;

        public EnumerationIterator(Enumeration<E> enumeration) {
            this.enumeration = enumeration;
        }

        public boolean hasNext() {
            return this.enumeration.hasMoreElements();
        }

        public E next() {
            return this.enumeration.nextElement();
        }

        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Not supported");
        }
    }

}
