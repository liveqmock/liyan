
package com.innofi.component.rbac.privilage.service.impl;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.innofi.framework.utils.date.DateUtil;
import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.innofi.component.metadata.dimenfield.service.IMdDimenFieldService;
import com.innofi.component.rbac.privilage.service.IDaDimenDataService;
import com.innofi.component.codetransfer.IdfCodeTransfer;
import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.framework.pojo.metadata.MdDimenField;
import com.innofi.component.rbac.privilage.pojo.DaDimenData;
import com.innofi.component.rbac.org.pojo.SysOrgInfo;
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
@Component(value="daDimenDataService")
public class DaDimenDataServiceImpl  extends BaseServiceImpl<DaDimenData,String> implements IDaDimenDataService {
	
	@Resource(name="mdDataTitleTableMappingDaoSupport")
	protected DaoSupport daoSupport;

	public DaoSupport getDaoSupport(){
		return daoSupport;
	}

	public List<MdDimenField> findDimenFieldForDimenControl(String dimenControlId,String dimenId) {
		IMdDimenFieldService mdDimenFieldService=getSpringBean("mdDimenFieldService");
		//IMdFieldService mdFieldService = getSpringBean("mdFieldService");
		
//		List<PropertyFilter> filters=new ArrayList<PropertyFilter>();
//		this.addPropertyFilter(filters, "dimenId", dimenId, QueryConstants.EQUAL, true);
//		List<MdDimenField> fields=mdDimenFieldService.findByPropertyFilters(filters, null);
		
		String sql = "select t1.*,t2.field_cn_name from MD_DIMEN_FIELD t1,MD_FIELD t2 where t1.field_id = t2.field_id and t1.dimen_id = ?";
		List paramList = new ArrayList(1);
		paramList.add(dimenId);
		List<MdDimenField> fields = mdDimenFieldService.queryBeanForList(sql, paramList.toArray());
		
		if(!StringUtils.isBlank(dimenControlId)){
			Set<String> fieldIds=this.findDaDimenControlByTableId(dimenControlId);
			for(MdDimenField field:fields){
				
				if(fieldIds.contains(field.getId())){
					field.setDimenControlId(dimenControlId);
					field.setSelectFlag(true);
				}
			}
			
			
			
		}
		return fields;
	}

    public List<DaDimenData> findDimenDatasForDimenControl(String dimenControlId, String dimenFieldId) {
		List<DaDimenData> datas=new ArrayList<DaDimenData>();
		List<PropertyFilter> filters=new ArrayList<PropertyFilter>();
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd");
		String hql="select p,s.dimenType from DaDimenData p,DaDimenControl c,MdDimension s where p.dimenControlId=c.id and c.dimenId=s.id and p.dimenControlId=? and p.dimenFieldId=? ";
		List list= this.findByHql(hql, new String[]{dimenControlId,dimenFieldId});
		IdfCodeTransfer codeTransfer = ContextHolder.getSpringBean("mdf.codeTransfer");
		Map<String,String> map=new HashMap<String,String>(); 
		map.put("dimenValue", "dispName");
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Object[] o = (Object[]) iter.next();
			DaDimenData data = (DaDimenData) o[0];
		    String dimenType = (String) o[1];
		    data.setDimenType(dimenType);
		    if("1".equals(data.getDimenType())){//机构
		    	ListOrderedMap cacheObjects =codeTransfer.getCacheObjects("sysOrgInfoService");
		    	String orgCode=data.getDimenValue();
		    	if(orgCode.equals("${userDeptCode}")){
		    		data.setDispName("当前用户所属部门");
		    		data.setCategory("0");
		    	}else if(orgCode.equals("${userOrgCode}")){
		    		data.setDispName("当前用户所属机构");
		    		data.setCategory("1");
		    	}else{
		    		SysOrgInfo orgInfo = (SysOrgInfo) cacheObjects.get(orgCode);
		            if(orgInfo!=null){
		                data.setDispName(orgInfo.getOrgName());
		                data.setCategory(orgInfo.getCategory());
		            }
		    	}
			}else if("2".equals(data.getDimenType())){//时间
				String dateStr=data.getDimenValue();
				if(!StringUtils.isBlank(dateStr) && dateStr.indexOf("|")>0){
					String[] dates=dateStr.split("\\|");
					try {
						data.setStartDate(sdf.parse(dates[0]));
						data.setEndDate(sdf.parse(dates[1]));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			}else if("3".equals(data.getDimenType())){//业务线
				codeTransfer.transferSingleObject(data, "sysBusinessLineService", map);
				String businessValue = data.getDimenValue();
				if("${userBusinessLineCode}".equals(businessValue)) {
					data.setDispName("当前用户所属业务条线");
				}
			}else if("4".equals(data.getDimenType())){//产品
				codeTransfer.transferSingleObject(data, "sysProductService", map);
				String productNameString = data.getDimenValue();
				if("${userProductCode}".equals(productNameString)) {
					data.setDispName("当前用户所属产品");
				}
			} else if("8".equals(data.getDimenType())) {//岗位
				codeTransfer.transferSingleObject(data, "sysPostService", map);
				String postName = data.getDimenValue();
				if("${userPostCode}".equals(postName)){
					data.setDispName("当前用户所属岗位");
				}
			}
		    datas.add(data);
		}
		return datas;
	}
    
    
    public List<DaDimenData> findDimenDatasForRestrict(String restrictId) {
		List<DaDimenData> datas=new ArrayList<DaDimenData>();
		List<PropertyFilter> filters=new ArrayList<PropertyFilter>();
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd");
		
		List list= queryBeanForList("select t1.*, t5.DIMEN_TYPE from DA_DIMEN_DATA t1 , DA_DIMEN_DATA_RESTRICT_RELATED t2 , DA_DIMEN_DATA_RESTRICT t3 , DA_DIMEN_CONTROL t4 , MD_DIMENSION t5  where t1.dimen_data_id = t2.dimen_data_id\n" +
                " and t3.restrict_id = t2.restrict_id and t1.DIMEN_CONTROL_ID = t4.DIMEN_CONTROL_ID and t5.DIMEN_ID = t4.DIMEN_ID and t3.restrict_id = ?" , restrictId);
		IdfCodeTransfer codeTransfer = ContextHolder.getSpringBean("mdf.codeTransfer");
		Map<String,String> map=new HashMap<String,String>(); 
		map.put("dimenValue", "dispName");
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			DaDimenData data = (DaDimenData) iter.next();
			if(data.getDimenValue().equals("IS NULL")){
				data.setDispName("为空值");
			}else if(data.getDimenValue().equals("IS NOT NULL")){
				data.setDispName("不为空值");
			}else{
			    if("1".equals(data.getDimenType())){//机构
			    	ListOrderedMap cacheObjects =codeTransfer.getCacheObjects("sysOrgInfoService");
			    	String orgCode=data.getDimenValue();
			    	if(orgCode.equals("${userDeptCode}")){
			    		data.setDispName("当前用户所属部门");
			    		data.setCategory("0");
			    	}else if(orgCode.equals("${userOrgCode}")){
			    		data.setDispName("当前用户所属机构");
			    		data.setCategory("1");
			    	}else{
			    		SysOrgInfo orgInfo = (SysOrgInfo) cacheObjects.get(orgCode);
			            if(orgInfo!=null){
			                data.setDispName(orgInfo.getOrgName());
			                data.setCategory(orgInfo.getCategory());
			            }
			    	}
				}else if("2".equals(data.getDimenType())){//时间
					String dateStr=data.getDimenValue();
					if(!StringUtils.isBlank(dateStr) &&dateStr.indexOf("|")>0){
						String[] dates=dateStr.split("\\|");
						try {
							data.setStartDate(sdf.parse(dates[0]));
							data.setEndDate(sdf.parse(dates[1]));
                            data.setDispName(DateUtil.formatDate("yyyy年MM月dd日",data.getStartDate())+"|"+DateUtil.formatDate("yyyy年MM月dd日",data.getEndDate()));
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}else if(!StringUtils.isBlank(dateStr)&&dateStr.equals("IS NULL")){
                        data.setDispName("为空值");
                    }else if(!StringUtils.isBlank(dateStr)&&dateStr.equals("IS NOT NULL")){
                        data.setDispName("不为空值");
                    }
				}else if("3".equals(data.getDimenType())){//业务线
					codeTransfer.transferSingleObject(data, "sysBusinessLineService", map);
					String businessValue = data.getDimenValue();
					if("${userBusinessLineCode}".equals(businessValue)) {
						data.setDispName("当前用户所属业务条线");
					}
				}else if("4".equals(data.getDimenType())){//产品
					codeTransfer.transferSingleObject(data, "sysProductService", map);
					String productNameString = data.getDimenValue();
					if("${userProductCode}".equals(productNameString)) {
						data.setDispName("当前用户所属产品");
					}
				} else if("8".equals(data.getDimenType())) {//岗位
					codeTransfer.transferSingleObject(data, "sysPostService", map);
					String postName = data.getDimenValue();
					if("${userPostCode}".equals(postName)){
						data.setDispName("当前用户所属岗位");
					}
				}
			}
		    datas.add(data);
		}
		return datas;
	}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
	
	private Set<String> findDaDimenControlByTableId(String dimenControlId){
		Set<String> fieldIds=new HashSet<String>();
		List<PropertyFilter> filters=new ArrayList<PropertyFilter>();
		this.addPropertyFilter(filters, "dimenControlId", dimenControlId, QueryConstants.EQUAL, true);
		List<DaDimenData> datas=this.findByPropertyFilters(filters, null);
		for(DaDimenData data:datas){
			fieldIds.add(data.getDimenFieldId());
		}
		return fieldIds;
	}

	public void deleteDimenData(String dimenControlId, String dimenFieldId) {
		List<PropertyFilter> filters=new ArrayList<PropertyFilter>();
		this.addPropertyFilter(filters, "dimenControlId", dimenControlId, QueryConstants.EQUAL, true);
		this.addPropertyFilter(filters, "dimenFieldId", dimenFieldId, QueryConstants.EQUAL, true);
		this.deleteByPropertyFilters(filters);
	}

	public void saveDaDimenData(DaDimenData dimenData) {
		if("2".equals(dimenData.getDimenType())){//时间
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
			dimenData.setDimenValue(df.format(dimenData.getStartDate())+"|"+df.format(dimenData.getEndDate()));
		}
        EntityState state = EntityUtils.getState(dimenData);
        if (EntityState.DELETED.equals(state)) {
            delete(dimenData);
        } else if (EntityState.MODIFIED.equals(state) || EntityState.MOVED.equals(state)) {
            update(dimenData);
        } else if (EntityState.NEW.equals(state)) {
            save(dimenData);
        }
	}
	
	/**
	 * 根据维度获取维度数据
	   
	 * findMdDimensionByDimenControlId(这里用一句话描述这个方法的作用)    
	   
	 */
	public List<DaDimenData> findMdDimensionByDimenControlId(String dimensionControlId){
		String sql = "select t3.* from DA_DIMEN_CONTROL t2,DA_DIMEN_DATA t3 where t2.dimen_control_id = t3.dimen_control_id and t2.dimen_control_id = ?";
		List<DaDimenData>  list = getDaoSupport().queryBeanForList(DaDimenData.class, sql, dimensionControlId);
		return list;
	   
	}
	
	/**
	 * 根据策略ID取维度数据
	 * findMdDimensionByPolicyIds(这里用一句话描述这个方法的作用)    
	 * @param   policyIds 传入策略ID    
	 * @param   FieldName 需要过滤的维度字段名称，不过滤可以全取。
	 * @param true 该维度字段为 树形维度。。。
	 * @Exception 异常对象    
	 * @since  CodingExample　Ver(编码范例查看) 1.1
	 */
	public List<DaDimenData> findMdDimensionByPolicyIds(List<String> policyIds,String FieldName,boolean treeFlag){
		String sql = "select datas.*"+
						"from da_table_policy  policy,"+
						"da_dimen_control control,"+
						"da_dimen_data    datas,"+
						"md_dimen_field field,"+
						"md_table mdtable"+
						"where policy.table_auth_id = control.table_auth_id"+
						"and control.dimen_control_id = datas.dimen_control_id"+
						"and field.dimen_field_id = datas.dimen_field_id"+
						"and policy.table_name = mdtable.table_name"+
						"and mdtable.is_da_control = '1'"+
						"and policy.table_auth_id in (?)"; 
		String ids = "";
		for(String id:policyIds){
			if(StringUtils.isBlank(ids)){
				ids = id;
			}else{
				ids +=id;
			}
		}	
		
		List argsList = new ArrayList();
			argsList.add(ids);
		if(StringUtils.isNotBlank(FieldName)){
			sql+=" field.field_name =?";
			argsList.add(FieldName);
		}
		if(true==treeFlag){
			sql+="  and datas.dimen_auth_type IS NOT NULL ";
		}
		
		List<DaDimenData>  list  = getDaoSupport().queryBeanForList(DaDimenData.class, sql,argsList);
		return list;
	}

	@Override
	public List<DaDimenData> findDimenDatasByDimenFieldId(String dimenFieldId) {
		return findByProperty("dimenFieldId", dimenFieldId, QueryConstants.EQUAL);
	}
}

