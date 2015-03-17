
package com.innofi.component.rbac.privilage.service.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.innofi.framework.pojo.metadata.MdDimension;
import com.innofi.component.metadata.dimension.service.IMdDimensionService;
import com.innofi.component.rbac.privilage.pojo.DaDimenControl;
import com.innofi.component.rbac.privilage.service.IDaDimenControlService;
import com.innofi.component.rbac.privilage.service.IDaDimenDataService;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.framework.service.impl.BaseServiceImpl;
import com.innofi.framework.spring.context.ContextHolder;


/**
 * 功能/ 模块：todo 模块中文名称
 *
 * @author  todo 开发人员姓名 邮箱
 * @version todo 当前版本 日期
 *          todo 类描述
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
@Component(value="daDimenControlService")
public class DaDimenControlServiceImpl  extends BaseServiceImpl<DaDimenControl,String> implements IDaDimenControlService {
	
	@Resource(name="mdDataTitleTableMappingDaoSupport")
	protected DaoSupport daoSupport;

	public DaoSupport getDaoSupport(){
		return daoSupport;
	}
	/**
	 * 根据表名id去对应的元数据维度信息
	 */
	public List<MdDimension> findMdDimensionForControls(String tableAuthId,String tableId,String searchFlag) {
		IMdDimensionService mdDimensionService=getSpringBean("mdDimensionService");
		List<PropertyFilter> filters=new ArrayList<PropertyFilter>();
		this.addPropertyFilter(filters, "tableId", tableId, QueryConstants.EQUAL, true);
		List<MdDimension> dimens= mdDimensionService.findByPropertyFilters(filters, null);
		if(!StringUtils.isBlank(tableAuthId)){//修改
			List<MdDimension> rsList=new ArrayList<MdDimension>();
			Map<String,DaDimenControl> controlMap=this.findDaDimenControlByTableId(tableAuthId);
			for(MdDimension dimen:dimens){
				if(controlMap.containsKey(dimen.getId())){
					dimen.setSelectFlag(true);
					dimen.setDimenControlId(controlMap.get(dimen.getId()).getId());
					rsList.add(dimen);
				}else if(!"disp".equals(searchFlag)){//查看
					rsList.add(dimen);
				}
			}
			return rsList;
		}else{//新增
			return dimens;
		}
	}
	/**
	 * 传入主表带出子表信息
	   
	 * findMdDimensionForTableAndRelate(这里用一句话描述这个方法的作用)    
	   
	 * @param   name    
	   
	 * @param  @return    设定文件    
	   
	 * @return String    DOM对象    
	   
	 * @Exception 异常对象    
	   
	 * @since  CodingExample　Ver(编码范例查看) 1.1
	 */
	public List<MdDimension> findMdDimensionForTableAndRelate(String tableAuthId,String mainTableName,String searchFlag){
		//取主表和子表的信息
	    String relateSql = " select * from ( select t3.* from MD_DIMENSION t3 where t3.status = '1' and t3.table_name = ? "+
				" union "+
				" select t1.* from MD_DIMENSION t1,MD_TABLE_RELATE t2 where t1.table_name =t2.foreign_table_name and t2.main_table_name = ? and t1.status = '1')  ORDER BY TABLE_NAME ";
    
	    List args = new ArrayList(2);
	    args.add(mainTableName);
	    args.add(mainTableName);
	    IMdDimensionService mdDimensionService=getSpringBean("mdDimensionService");
	    List<MdDimension> dimens = mdDimensionService.queryBeanForList(relateSql, args.toArray());//取主表和相关联子表的维度信息

	    if(!StringUtils.isBlank(tableAuthId)){//修改
			List<MdDimension> rsList=new ArrayList<MdDimension>();
			Map<String,DaDimenControl> controlMap=this.findDaDimenControlByTableId(tableAuthId);
			for(MdDimension dimen:dimens){
				if(controlMap.containsKey(dimen.getId())){
					dimen.setSelectFlag(true);
					dimen.setDimenControlId(controlMap.get(dimen.getId()).getId());
					rsList.add(dimen);
				}else if(!"disp".equals(searchFlag)){//查看
					rsList.add(dimen);
				}
			}
			return rsList;
		}else{//新增
			return dimens;
		}
	}

	private Map<String,DaDimenControl> findDaDimenControlByTableId(String tableAuthId){
		Map<String,DaDimenControl> controlMap=new HashMap<String,DaDimenControl>();
		List<PropertyFilter> filters=new ArrayList<PropertyFilter>();
		this.addPropertyFilter(filters, "tableAuthId", tableAuthId, QueryConstants.EQUAL, true);
		List<DaDimenControl> controls=this.findByPropertyFilters(filters, null);
		for(DaDimenControl control:controls){
			controlMap.put(control.getDimenId(),control);
		}
		return controlMap;
	}

	public void deleteControlData(String tableAuthId, String dimenId) {
		List<PropertyFilter> filters=new ArrayList<PropertyFilter>();
		this.addPropertyFilter(filters, "tableAuthId", tableAuthId, QueryConstants.EQUAL, true);
		this.addPropertyFilter(filters, "dimenId", dimenId, QueryConstants.EQUAL, true);
		List<DaDimenControl> controls=this.findByPropertyFilters(filters, null);
		IDaDimenDataService daDimenDataService=ContextHolder.getSpringBean("daDimenDataService");
		filters.clear();
		for(DaDimenControl control:controls){
			this.addPropertyFilter(filters, "dimenControlId", control.getId(), QueryConstants.EQUAL, true);
			daDimenDataService.deleteByPropertyFilters(filters);
			this.delete(control);
		}
	}
}

