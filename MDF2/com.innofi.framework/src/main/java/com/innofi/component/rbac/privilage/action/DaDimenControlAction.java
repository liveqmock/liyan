package com.innofi.component.rbac.privilage.action;
import com.innofi.framework.pojo.metadata.MdDimension;
import com.innofi.component.rbac.privilage.pojo.DaDimenControl;
import com.innofi.component.rbac.privilage.service.IDaDimenControlService;

import com.innofi.framework.action.impl.BaseActionImpl;
import com.innofi.framework.dao.hibernate.PropertyFilter;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import java.util.Map;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;


/**
 * 功能/ 模块：todo
 *
 * @author  todo 开发人员姓名 邮箱
 * @version todo 当前版本 日期
 *          todo 为dorado7界面维护操作的提供支持，实现DaDimenControl对象信息的加载与保存操作
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
@Component
public class DaDimenControlAction extends BaseActionImpl {
    @Resource
	private   IDaDimenControlService daDimenControlSevice;

    @DataProvider
    public void findDaDimenControls(Page page, Map<String, Object> parameter) throws Exception {
    	List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
        com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
        daDimenControlSevice.findByPage_Filters(propertyFilters, null, innofiPage);
    }

    @DataProvider
    public List<MdDimension> findMdDimensionForControls(Map<String, String> parameter){
    	if(parameter==null || parameter.size()==0){
    		return null;
    	}
    	//String tableId=parameter.get("tableId");
    	String tableName=parameter.get("tableName");
    	String tableAuthId=parameter.get("tableAuthId");
    	String searchFlag=parameter.get("searchFlag");
    	//是否取业务子表的标记~~~
    	String  relateTableFlag=parameter.get("relateTableFlag");
    	if(StringUtils.isNotBlank(relateTableFlag)&&"true".equals(relateTableFlag)){
    		
    		return daDimenControlSevice.findMdDimensionForTableAndRelate(tableAuthId, tableName, searchFlag);//是否取关联表的维度。。。
    	}else{
      		return daDimenControlSevice.findMdDimensionForControls(tableAuthId,tableName,searchFlag);
    	}
    }
    
    @DataResolver
    public void saveDaDimenControls(Collection<DaDimenControl> objs) {
		for (Iterator iterator = objs.iterator(); iterator.hasNext();) {
			DaDimenControl daDimenControl = (DaDimenControl) iterator.next();
			EntityState state = EntityUtils.getState(daDimenControl);
			if (EntityState.DELETED.equals(state)) {
				daDimenControlSevice.delete(daDimenControl);
			} else if (EntityState.MODIFIED.equals(state)
					|| EntityState.MOVED.equals(state)) {
				daDimenControlSevice.update(daDimenControl);
			} else if (EntityState.NEW.equals(state)) {
				daDimenControlSevice.save(daDimenControl);
			}
		}
    }
}
