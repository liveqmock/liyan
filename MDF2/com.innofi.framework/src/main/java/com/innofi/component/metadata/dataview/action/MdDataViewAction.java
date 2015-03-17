package com.innofi.component.metadata.dataview.action;
import com.innofi.component.metadata.commons.MetadataConstants;
import com.innofi.framework.pojo.metadata.MdDataView;
import com.innofi.component.metadata.dataview.service.IMdDataViewService;

import com.innofi.framework.action.impl.BaseActionImpl;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.QueryConstants;



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
 *          todo 为dorado7界面维护操作的提供支持，实现MdDataView对象信息的加载与保存操作
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
@Component
public class MdDataViewAction extends BaseActionImpl {
    @Resource
	private   IMdDataViewService mdDataViewSevice;
    private String parentIdString = "";

    @DataProvider
    public void findMdDataViews(Page page, Map<String, Object> parameter) throws Exception {
    List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
    	if(parameter != null){
    		if (parameter.get("parentId") != null){
    			addPropertyFilter(propertyFilters, "parentId", parameter.get("parentId"), QueryConstants.EQUAL, true);
    		}
		 addPropertyFilter(propertyFilters, "id", parameter.get("id"), QueryConstants.EQUAL, true);
         addPropertyFilter(propertyFilters, "mdmLevel", parameter.get("mdmLevel"), QueryConstants.EQUAL, true);
         addPropertyFilter(propertyFilters, "mdmName", parameter.get("mdmName"), QueryConstants.LIKE, true);
         addPropertyFilter(propertyFilters, "status", parameter.get("status"), QueryConstants.EQUAL, true);    		
		}
    	
        com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
        mdDataViewSevice.findByPage_Filters(propertyFilters, null, innofiPage);
    }
    /**
     * 通过父ID查询数据信息
     * @param parentId
     * @return
     * @throws Exception
     */
	@DataProvider
    public List<MdDataView> findDataViewsByParentId(String parentId) throws Exception {
    	if(StringUtils.isBlank(parentId)){
    		parentId = MetadataConstants.TREE_ROOT;
		}
    	List<MdDataView> mdDataView=mdDataViewSevice.findDataViewsByParentId(parentId);
        return mdDataView;
    }
	
    @DataResolver
    public void saveMdDataViews(Collection<MdDataView> objs) {
    	for (Iterator iterator = objs.iterator(); iterator.hasNext();) {
    		MdDataView mdDataView = (MdDataView) iterator.next();
			EntityState state = EntityUtils.getState(mdDataView);
			Collection<MdDataView> children = EntityUtils.getValue(mdDataView, "children");

			if(children != null && children.size() > 0){
				parentIdString = mdDataView.getId();
				if(EntityState.NEW.equals(state)){
					mdDataViewSevice.save(mdDataView);
					parentIdString = mdDataView.getId();
				}
				
				saveMdDataViews(children);
			}else{
				if(EntityState.NEW.equals(state)){
					if(mdDataView.getParentId()==null||"".equals(mdDataView.getParentId())){
						mdDataView.setParentId(parentIdString);
					}
					mdDataViewSevice.save(mdDataView);
				}else if(EntityState.MODIFIED.equals(state)){
					mdDataViewSevice.update(mdDataView);
				}else if(EntityState.DELETED.equals(state)){
					mdDataViewSevice.delete(mdDataView);
				}
			}
		}
	} 
}
