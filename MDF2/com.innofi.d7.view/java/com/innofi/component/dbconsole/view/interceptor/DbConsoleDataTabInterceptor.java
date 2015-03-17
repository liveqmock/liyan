package com.innofi.component.dbconsole.view.interceptor;

import com.bstek.dorado.data.type.DefaultEntityDataType;
import com.bstek.dorado.data.type.manager.DataTypeManager;
import com.bstek.dorado.data.type.property.BasePropertyDef;
import com.bstek.dorado.view.View;
import com.bstek.dorado.view.widget.base.toolbar.Button;
import com.bstek.dorado.view.widget.data.DataSet;
import com.bstek.dorado.view.widget.datacontrol.DataPilot;
import com.bstek.dorado.view.widget.grid.DataColumn;
import com.bstek.dorado.view.widget.grid.DataGrid;
import com.bstek.dorado.view.widget.grid.RowSelectorColumn;
import com.bstek.dorado.web.DoradoContext;
import com.innofi.component.dbconsole.service.IDbConsoleService;
import com.innofi.component.dbconsole.pojo.DbConsoleColumn;
import com.innofi.component.dbconsole.pojo.DbConsoleTable;
import com.innofi.component.dbconsole.pojo.ProcessResult;
import com.innofi.framework.spring.context.ContextHolder;
import com.innofi.framework.utils.sql.SqlUtil;
import com.innofi.framework.utils.string.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @version 2.0
 * @author liumy2009@126.com
 * @found date: 12-7-12
 * @found time: 上午11:23
 * <p/>
 * 数据展现视图拦截器
 */
@Component(value="dbConsoleDataTabInterceptor")
public class DbConsoleDataTabInterceptor {

    private Logger _log = LoggerFactory.getLogger(DbConsoleDataTabInterceptor.class);

    private static final String COLUMN_WIDTH = "150";

    @Resource(name="dbConsoleService",type=IDbConsoleService.class)
    private IDbConsoleService dbConsoleService;

    public void onInit(DefaultEntityDataType dataTypeData) throws Exception {
        DoradoContext dc = DoradoContext.getCurrent();
        String groupId = (String) dc.getAttribute(DoradoContext.VIEW, "groupId");
        String connName = (String) dc.getAttribute(DoradoContext.VIEW, "connName");
        String schemaName = (String)dc.getAttribute(DoradoContext.VIEW,"schemaName");
        String tableName = (String) dc.getAttribute(DoradoContext.VIEW, "tableName");
        String token = (String) dc.getAttribute(DoradoContext.VIEW, "token");
        List<DbConsoleColumn> columns = null;
        if (StringUtil.hasText(token)) {
            ProcessResult<DbConsoleTable> processResult = dbConsoleService.getTaskResultMetaInfo(token);
            columns = processResult.getData().getColumns();
            int startCount = 0 ;
            int pageCount = 0 ;
            if(processResult.getData().getData()!=null&&processResult.getData().getData().size()>0){
                startCount = ((processResult.getPageNo()-1)*processResult.getLimitSize()+1);
            }
            int pageNo = processResult.getPageNo();
            int limitSize = processResult.getLimitSize();

            if(startCount>0){
                pageCount = processResult.getPageCount();
            }
            int dataTotalCount = processResult.getTotalCount();
            double consumeTime = processResult.getConsumeTime();
            String querySql = processResult.getQuerySql();

            dc.setAttribute(DoradoContext.VIEW, "columns", columns);
            dc.setAttribute(DoradoContext.VIEW,"dataTotalCount",dataTotalCount);
            dc.setAttribute(DoradoContext.VIEW,"consumeTime",consumeTime);
            dc.setAttribute(DoradoContext.VIEW,"pageCount",pageCount);
            dc.setAttribute(DoradoContext.VIEW,"limitSize",limitSize+"");
            dc.setAttribute(DoradoContext.VIEW,"pageNo",pageNo+"");
            dc.setAttribute(DoradoContext.VIEW,"querySql",querySql);
            dc.setAttribute(DoradoContext.VIEW,"startCount",startCount);
        }else{
            columns = dbConsoleService.loadColumns(groupId,connName,schemaName,tableName);
            dc.setAttribute(DoradoContext.VIEW, "columns", columns);
        }


        DataTypeManager dataTypeManager = ContextHolder.getSpringBean("dorado.dataTypeManager");
        BasePropertyDef pd;
        if (columns != null) {
            for (DbConsoleColumn info : columns) {
                pd = new BasePropertyDef();
                pd.setName(info.getColumnName());
                String columnType = info.getColumnType();
                _log.debug(String.format("datagrid[columnName=%s,columnType=%s]", info.getColumnName(), info.getColumnType()));
                String doradoType = SqlUtil.getDroadoType(columnType);
                if (StringUtils.hasText(doradoType)) {
                    pd.setDataType(dataTypeManager.getDataType(doradoType));
                }

                if (StringUtil.hasText(token)) {
                    if (info.isIsnullAble()) {
                        pd.setRequired(false);
                    } else {
                        pd.setRequired(true);
                    }
                }

                dataTypeData.addPropertyDef(pd);
            }
        }
    }

    public void onInit(View view) throws Exception {
        DataPilot dataPilotData = (DataPilot) view.getComponent("dataPilotData");
        Button buttonSave = (Button) view.getComponent("toolBarButtonSave");
        DataGrid dataGridData = (DataGrid) view.getComponent("dataGridData");
    }

    public void onReady(DataSet dataSetData) throws Exception {
        DoradoContext dc = DoradoContext.getCurrent();
        String groupId = (String) dc.getAttribute(DoradoContext.VIEW, "groupId");
        String connName = (String) dc.getAttribute(DoradoContext.VIEW, "connName");
        String schemaName = (String)dc.getAttribute(DoradoContext.VIEW,"schemaName");
        String tableName = (String) dc.getAttribute(DoradoContext.VIEW, "tableName");
        String token = (String) dc.getAttribute(DoradoContext.VIEW, "token");
        Map<String, Object> parameter = new HashMap<String, Object>();
        parameter.put("groupId", groupId);
        parameter.put("connName", connName);
        parameter.put("schemaName",schemaName);
        parameter.put("tableName", tableName);
        parameter.put("token", token);
        dataSetData.setParameter(parameter);
    }

    public void onReady(DataGrid dataGridData) throws Exception {
        IDbConsoleService dbConsoleService = ContextHolder.getSpringBeanFactory().getBean(IDbConsoleService.class);
        DoradoContext dc = DoradoContext.getCurrent();
        String groupId = (String) dc.getAttribute(DoradoContext.VIEW, "groupId");
        String connName = (String) dc.getAttribute(DoradoContext.VIEW, "connName");
        String schemaName = (String) dc.getAttribute(DoradoContext.VIEW, "schemaName");
        String tableName = (String) dc.getAttribute(DoradoContext.VIEW, "tableName");
        List<DbConsoleColumn> columns = null;

        columns = (List<DbConsoleColumn>) dc.getAttribute(DoradoContext.VIEW, "columns");
        if (columns == null) {
            columns = dbConsoleService.loadColumns(groupId, connName, schemaName, tableName);
        }

        RowSelectorColumn selector = new RowSelectorColumn();
        selector.setVisible(true);
        selector.setIgnored(false);
        selector.setSupportsOptionMenu(true);
        dataGridData.addColumn(selector);

        DataColumn column;
        if (columns != null) {
            for (DbConsoleColumn info : columns) {
                column = new DataColumn();
                column.setName(info.getColumnName());
                column.setWidth(COLUMN_WIDTH);
                dataGridData.addColumn(column);
            }
        }
    }

}
