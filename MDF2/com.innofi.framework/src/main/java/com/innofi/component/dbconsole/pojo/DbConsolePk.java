package com.innofi.component.dbconsole.pojo;

import com.innofi.framework.utils.string.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @version 2.0
 * @author liumy2009@126.com
 * @found date: 12-7-1
 * @found time: 下午8:45
 * <p/>
 * 数据表PK定义对象
 */
public class DbConsolePk {

    private List<DbConsolePkColumn> columnDbConsoles = new ArrayList<DbConsolePkColumn>();
    private String pkName;

    public DbConsolePk(String name, List<DbConsolePkColumn> columnDbConsoles) {
        this.pkName = StringUtil.trimWhole(name);
        if (columnDbConsoles != null) {
            this.columnDbConsoles = new ArrayList<DbConsolePkColumn>(columnDbConsoles);
        }
    }

    public String getPkName() {
        return pkName;
    }

    public List<DbConsolePkColumn> getColumnDbConsoles() {
        return columnDbConsoles;
    }

    public List<String> getColumns() {

        List<String> result = new ArrayList<String>(columnDbConsoles.size());
        for (DbConsolePkColumn col : columnDbConsoles) {
            result.add(col.getColumn());
        }
        return result;
    }

    public void addColumn(DbConsolePkColumn col) {
        this.columnDbConsoles.add(col);
    }

    public DbConsolePk createCopy() {
        DbConsolePk copy = new DbConsolePk(this.pkName, this.columnDbConsoles);
        return copy;
    }

    
    public String toString() {
        return pkName + " " + columnDbConsoles;
    }

}
