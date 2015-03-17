
package com.innofi.component.rbac.busiline.service.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.innofi.component.rbac.busiline.pojo.SysBusinessLine;
import com.innofi.component.rbac.busiline.service.ISysBusinessLineService;
import com.innofi.framework.FrameworkConstants;
import com.innofi.component.codetransfer.IdfCodeTransfer;
import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.PropertyOrder;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.framework.dao.pagination.Page;
import com.innofi.framework.service.impl.BaseServiceImpl;
import com.innofi.framework.spring.context.ContextHolder;



@Component(value="sysBusinessLineService")
public class SysBusinessLineServiceImpl  extends BaseServiceImpl<SysBusinessLine,String> implements ISysBusinessLineService {
	
	@Resource(name="mdDataTitleTableMappingDaoSupport")
	protected DaoSupport daoSupport;

	public DaoSupport getDaoSupport(){
		return daoSupport;
	}

	public String getCnFieldName() {
		return "busilineName";
	}
	
	public void findSysBusinessLineList(String searchType,
			String parentBusilineId, List<PropertyFilter> filters,
			Page innofiPage) {
        List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
        orders.add(new PropertyOrder("seq",QueryConstants.ORDER_ASC));
		if ("tree".equals(searchType) && !StringUtils.isBlank(parentBusilineId)) {
			String busiCode="0";
			if(!"0".equals(parentBusilineId)){
				SysBusinessLine line=this.get(parentBusilineId);
				busiCode=line.getBusilineCode();
			}
            this.removePropertyFilter(filters, "parentBusilineId");
            this.addPropertyFilter(filters, "id", parentBusilineId, QueryConstants.NOT_EQUAL, true);
            this.addPropertyFilter(filters, "treeSeq", "." + busiCode + ".", QueryConstants.LIKE, true);
            findByPage_Filters(filters, orders, innofiPage);
        } else {
            findByPage_Filters(filters, orders, innofiPage);
        }
	}
	
	public void findByPage_Filters(List<PropertyFilter> filters,
			List<PropertyOrder> orders, Page page) {
		super.findByPage_Filters(filters, orders, page);
		List lines=page.getEntities();
		if(lines.size()>0){
			Map map=new HashMap();
			map.put("busilineBaselId", "busilineBaselName");
			IdfCodeTransfer codeTransfer = ContextHolder.getSpringBean("mdf.codeTransfer");
			codeTransfer.transferResult(lines, "sysBaselBusinessLineService", map);
			map.clear();
			map.put("belongOrgCode", "belongOrgName");
			codeTransfer.transferResult(lines, "sysOrgInfoService", map);
		}
	}
	
	public List<SysBusinessLine> findByProperty(String propertyName,
			Object value, String matchType) {
		List lines=super.findByProperty(propertyName, value, matchType);
		if(lines.size()>0){
			Map map=new HashMap();
			map.put("busilineBaselId", "busilineBaselName");
			IdfCodeTransfer codeTransfer = ContextHolder.getSpringBean("mdf.codeTransfer");
			codeTransfer.transferResult(lines, "sysBaselBusinessLineService", map);
			map.clear();
			map.put("belongOrgCode", "belongOrgName");
			codeTransfer.transferResult(lines, "sysOrgInfoService", map);
		}
		return lines;
	}

	public String invalidBizLine(String code) {
		List<PropertyFilter> filters=new ArrayList<PropertyFilter>();
		this.addPropertyFilter(filters, "treeSeq", "." + code + ".", QueryConstants.LIKE, true);
		List<SysBusinessLine> lines=this.findByPropertyFilters(filters, null);
		for(SysBusinessLine line:lines){
			line.setStatus(FrameworkConstants.STATUS_INVALID);
			this.update(line);
		}
		return "ok";
	}
	
	
    /**
     * 通过条线ID找到条线信息
     */
	public SysBusinessLine  getBusLineByBusId(String busilineId){
	 	List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
    	orders.add( new PropertyOrder("treeSeq",QueryConstants.ORDER_ASC));
		List<SysBusinessLine> list= super.findByProperty("id", busilineId,  QueryConstants.EQUAL,orders);
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
    /**
     * 通过条线Code找到条线信息
     */
	public SysBusinessLine  getBusLineByBusCode(String busilineBaselCode){
	 	List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
    	orders.add(new PropertyOrder("treeSeq",QueryConstants.ORDER_ASC));
		List<SysBusinessLine> list= super.findByProperty("busilineBaselCode", busilineBaselCode,  QueryConstants.EQUAL,orders);
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
	/**
	 * 
	   
	 * getBusLinesByParentBusId(这里用一句话描述这个方法的作用)    
	   
	 * @param   name    
	   
	 * @param  @return    设定文件    
	   
	 * @return String    DOM对象    
	   
	 * @Exception 异常对象    
	   
	 * @since  CodingExample　Ver(编码范例查看) 1.1
	 */
	public List<SysBusinessLine> getBusLinesByParentBusId(String busilineBaselId){
	 	List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
    	orders.add(new PropertyOrder("seq",QueryConstants.ORDER_ASC));
		return super.findByProperty("parentBusilineId", busilineBaselId,  QueryConstants.EQUAL,orders);
	}
	
	/**U：所有本上级节点**/ 
    public List<SysBusinessLine> getUpLevelBusLinesByBusId(String busilineBaselId){
       	List<SysBusinessLine> list = new ArrayList();
  	//满足treeSeq .a.b.c. 的规则。
    SysBusinessLine sbline = getBusLineByBusId(busilineBaselId);
  	if(null!=sbline){
  		//获取Seq
  		String sbTreeLinesSeq = sbline.getTreeSeq();
  		sbTreeLinesSeq = sbTreeLinesSeq.substring(1,sbTreeLinesSeq.length()-1);//去除首尾字符
  		if(sbTreeLinesSeq.indexOf(".")>0){
  			String sbCode[] = StringUtils.split(sbTreeLinesSeq,".");
  			for(int i=0;i<sbCode.length;i++){
  			    list.add(getBusLineByBusCode(sbCode[i]));
  			}
  		}else{
  			//表示为顶点 
  			list.add(getBusLineByBusCode(sbTreeLinesSeq));
  		}
  	}
      return list;
    }
    
	/**D：所有本下级节点 **/
    public List<SysBusinessLine> getDownLevelBusLinesByBusId(String busilineBaselId){
    	  SysBusinessLine sbline = getBusLineByBusId(busilineBaselId);
    	List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
    	orders.add( new PropertyOrder("treeSeq",QueryConstants.ORDER_ASC));
    	return super.findByProperty("treeSeq", "%." + sbline.getTreeSeq() + ".%",  QueryConstants.LIKE,orders);
    }
    
	/**V：垂直所有节点 **/ 
    public List<SysBusinessLine> getVerticalLevelBusLinesByBusId(String busilineBaselId){
    	List<SysBusinessLine> list = new ArrayList();
    	//这里 直接取上一级节点开始
    	list.addAll(getUpLevelBusLinesByBusId(busilineBaselId));//本上级所有节点
    	list.remove(getUpLevelBusLinesByBusId(busilineBaselId).size()-1);//移除本上级节点最后一个节点 即本节点
        list.addAll(getDownLevelBusLinesByBusId(busilineBaselId));//本下级所有节点
    	return list;
    }
    
	/**H: 水平所有节点 **/
	public List<SysBusinessLine> getSameLevelBusLinesByBusId(String busilineBaselId){
		  SysBusinessLine sbline = getBusLineByBusId(busilineBaselId);
		  return  getBusLinesByParentBusId(sbline.getParentBusilineId());
	}
	

}

