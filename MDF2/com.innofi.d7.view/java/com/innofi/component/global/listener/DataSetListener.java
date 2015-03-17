package com.innofi.component.global.listener;

import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.bstek.dorado.data.listener.GenericObjectListener;
import com.bstek.dorado.view.widget.data.DataSet;
import com.innofi.component.metadata.field.service.IMdFieldService;
import com.innofi.component.metadata.metamodel.service.IMdMetaModelService;
import com.innofi.component.metadata.table.service.IMdTableService;
import com.innofi.framework.FrameworkConstants;
import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.spring.context.ContextHolder;
import com.innofi.framework.utils.validate.Validator;

public class DataSetListener extends GenericObjectListener<DataSet>{
	
    @Resource
    private IMdMetaModelService mdMetaModelServcie;
    @Resource
    private IMdFieldService mdFieldService;
    @Resource
    private IMdTableService mdTableService;
	@Resource(name="mdDataTitleTableMappingDaoSupport")
    protected DaoSupport daoSupport;
    @Override
    public boolean beforeInit(DataSet dataSet) throws Exception {
        return true;
    }


    @Override
    public void onInit(DataSet dataSet) throws Exception {
        long startTime = new Date().getTime();
    	HttpServletRequest request = ContextHolder.getContext().getDoradoContext().getRequest();
    	 String mdmId = request.getParameter("mdmId");
    	 String mdId = request.getParameter("mdId");
    	 String lookStyle = request.getParameter("lookStyle");
    	 String mainFieldValue = request.getParameter("mainFieldValue");
    	 String foreignField = request.getParameter("foreignField");
         String uri = request.getRequestURI();
         if (Validator.isNotNull(lookStyle)) {
    	     Map<String, Object> parameter = new HashMap<String, Object>();
    	     parameter.put("status", FrameworkConstants.STATUS_EFFECTIVE);
    	     parameter.put(tnToEn(foreignField).get(0), mainFieldValue);
    	     dataSet.setParameter(parameter);
         }
        long endTime = new Date().getTime();

        //ConsoleUtil.test("DataSetListener execute time [" + ((endTime-startTime)/1000) + "]");
    }
    
    /**
     * 把表名变成类名和对象名
     *
     * @param tableName
     * @return
     */
    public List<String> tnToEn(String tableName) {
        //定义实体对象名和类名
        StringBuffer entityName = new StringBuffer();
        StringBuffer className = new StringBuffer();
        tableName = tableName.toLowerCase();
        String[] stringArray = tableName.split("_");
        for (int i = 0; i < stringArray.length; i++) {
            entityName.append(stringArray[i].substring(0, 1).toUpperCase() + stringArray[i].substring(1, stringArray[i].length()));
        }
        for (int i = 0; i < stringArray.length; i++) {
            if (i == 0) {
                className.append(stringArray[i].toString());
            } else {
                className.append(stringArray[i].substring(0, 1).toUpperCase() + stringArray[i].substring(1, stringArray[i].length()));
            }
        }
        List<String> list = new ArrayList();
        list.add(className.toString());
        list.add(entityName.toString());
        return list;
    }
}
