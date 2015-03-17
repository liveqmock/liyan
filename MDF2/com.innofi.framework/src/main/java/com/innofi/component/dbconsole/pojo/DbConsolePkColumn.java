package com.innofi.component.dbconsole.pojo;

import com.innofi.framework.utils.string.StringUtil;

import java.util.Comparator;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @author liumy2009@126.com
 * @version 2.0
 * @found date: 2012-5-20
 * @found time: 下午3:47:07
 * PK对应的列对象信息
 */
public class DbConsolePkColumn {
    private String column;
    private int seq;

    public DbConsolePkColumn(String col, int colSequence) {
        this.column = col;
        this.seq = colSequence;
    }

    public void setColumn(String newName) {
        this.column = StringUtil.trimWhole(newName);
    }

    public String getColumn() {
        return this.column;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof DbConsolePkColumn) {
            DbConsolePkColumn otherCol = (DbConsolePkColumn) other;
            return column.equals(otherCol.column);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + (this.column != null ? this.column.hashCode() : 0);
        return hash;
    }

    public static Comparator<DbConsolePkColumn> getSequenceSorter() {
        return new Comparator<DbConsolePkColumn>() {
            public int compare(DbConsolePkColumn o1, DbConsolePkColumn o2) {
                return o1.seq - o2.seq;
            }
        };
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int sequence) {
        this.seq = sequence;
    }

    @Override
    public String toString() {
        return column;
    }

}
