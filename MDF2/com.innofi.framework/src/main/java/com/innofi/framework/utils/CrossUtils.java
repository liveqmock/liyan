package com.innofi.framework.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 笛卡尔积工具类
 * Copyright (c) 2010 北京名晟信息技术有限公司
 * all rights reserved.
 * author: liumy
 * found date: 2010-1-8
 * found time: 21:25:38
 */
public class CrossUtils {

    /**
     * 产生笛卡尔积组合.
     * @param crossArgs 信息组合。
     * <pre>
     * 格式：{
     *      { 1, 2, 3 },
     *      { a, b, c, d },
     *      { A, B, C },}
     * </pre>   *   * @return 笛卡尔积组合结果
     */
    public static List<List<Map<String, Object>>> cross(List<List<Map<String, Object>>> crossArgs) {
        // 计算出笛卡尔积行数
        int rows = crossArgs.size() > 0 ? 1 : 0;
        for (List<Map<String, Object>> data : crossArgs) {
            rows *= data.size();
        }
        // 笛卡尔积索引记录
        int[] record = new int[crossArgs.size()];
        List<List<Map<String, Object>>> results = new ArrayList<List<Map<String, Object>>>();
        // 产生笛卡尔积
        for (int i = 0; i < rows; i++) {
            List<Map<String, Object>> row = new ArrayList<Map<String, Object>>();
            // 生成笛卡尔积的每组数据
            for (int index = 0; index < record.length; index++) {
                row.add(crossArgs.get(index).get(record[index]));
            }
            results.add(row);
            crossRecord(crossArgs, record, crossArgs.size() - 1);
        }
        return results;
    }

    /**
     * 产生笛卡尔积当前行索引记录.
     *
     * @param sourceArgs 要产生笛卡尔积的源数据
     * @param record     每行笛卡尔积的索引组合
     * @param level      索引组合的当前计算层级
     */
    private static void crossRecord(List<List<Map<String, Object>>> sourceArgs, int[] record, int level) {
        record[level] = record[level] + 1;
        if (record[level] >= sourceArgs.get(level).size() && level > 0) {
            record[level] = 0;
            crossRecord(sourceArgs, record, level - 1);
        }
    }
}