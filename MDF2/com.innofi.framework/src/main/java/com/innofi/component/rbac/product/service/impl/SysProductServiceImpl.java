
package com.innofi.component.rbac.product.service.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.innofi.component.rbac.product.pojo.SysProduct;
import com.innofi.component.rbac.product.service.ISysProductService;
import com.innofi.framework.FrameworkConstants;
import com.innofi.component.codetransfer.IdfCodeTransfer;
import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.PropertyOrder;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.framework.dao.pagination.Page;
import com.innofi.framework.service.impl.BaseServiceImpl;
import com.innofi.framework.spring.context.ContextHolder;



@Component(value="sysProductService")
public class SysProductServiceImpl  extends BaseServiceImpl<SysProduct,String> implements ISysProductService {
	
	@Resource(name="mdDataTitleTableMappingDaoSupport")
	protected DaoSupport daoSupport;

	public DaoSupport getDaoSupport(){
		return daoSupport;
	}

	
	
	@Override
	public List<SysProduct> findByProperty(String propertyName, Object value,
			String matchType) {
		List products= super.findByProperty(propertyName, value, matchType);
		if(products.size()>0){
			Map map=new HashMap();
			map.put("manageOrgCode", "manageOrgName");
			IdfCodeTransfer codeTransfer = ContextHolder.getSpringBean("mdf.codeTransfer");
			codeTransfer.transferResult(products, "sysOrgInfoService", map);
			map.clear();
			map.put("busilineId", "busilineName");
			codeTransfer.transferResult(products, "sysBusinessLineService", map);
		}
		return products;
	}



	@Override
	public void findByPage_Filters(List<PropertyFilter> filters,
			List<PropertyOrder> orders, Page page) {
		super.findByPage_Filters(filters, orders, page);
		List products=page.getEntities();
		if(products.size()>0){
			Map map=new HashMap();
			map.put("manageOrgCode", "manageOrgName");
			IdfCodeTransfer codeTransfer = ContextHolder.getSpringBean("mdf.codeTransfer");
			codeTransfer.transferResult(products, "sysOrgInfoService", map);
			map.clear();
			map.put("busilineId", "busilineName");
			codeTransfer.transferResult(products, "sysBusinessLineService", map);
		}
	}



	@Override
	public String getCnFieldName() {
		return "productName";
	}



	public void findSysProductList(String searchType, String parentId,
			List<PropertyFilter> filters, Page innofiPage) {
		if ("tree".equals(searchType) && !StringUtils.isBlank(parentId)) {
			String productCode="0";
			if(!"0".equals(parentId)){
				SysProduct line=this.get(parentId);
				productCode=line.getProductCode();
			}
            this.removePropertyFilter(filters, "parentId");
            this.addPropertyFilter(filters, "id", parentId, QueryConstants.NOT_EQUAL, true);
            this.addPropertyFilter(filters, "treeSeq", "." + productCode + ".", QueryConstants.LIKE, true);
            findByPage_Filters(filters, null, innofiPage);
        } else {
            findByPage_Filters(filters, null, innofiPage);
        }		
	}

	public String invalidProduct(String code) {
		List<PropertyFilter> filters=new ArrayList<PropertyFilter>();
		this.addPropertyFilter(filters, "treeSeq", "." + code + ".", QueryConstants.LIKE, true);
		List<SysProduct> products=this.findByPropertyFilters(filters, null);
		for(SysProduct product:products){
			product.setStatus(FrameworkConstants.STATUS_INVALID);
			this.update(product);
		}
		return "ok";
	}
	
	   /**
     * 通过产品ID找到条线信息
     */
	public SysProduct  getSysProductByPdId(String productId){
	 	List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
    	orders.add( new PropertyOrder("treeSeq",QueryConstants.ORDER_ASC));
		List<SysProduct> list= super.findByProperty("id", productId,  QueryConstants.EQUAL,orders);
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
    /**
     * 通过条线Code找到条线信息
     */
	public SysProduct  getSysProductByPdCode(String productCode){
	 	List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
    	orders.add(new PropertyOrder("treeSeq",QueryConstants.ORDER_ASC));
		List<SysProduct> list= super.findByProperty("productCode", productCode,  QueryConstants.EQUAL,orders);
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
	/**
	 *根据父节点信息找到所有子节点信息 
	 */
	public List<SysProduct> getSysProductByParentPdId(String productId){
	 	List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
    	orders.add( new PropertyOrder("treeSeq",QueryConstants.ORDER_ASC));
		return super.findByProperty("parentId", productId,  QueryConstants.EQUAL,orders);
	}
	/**U：所有本上级节点**/ 
	public List<SysProduct> getUpLevelSysProductByPdId(String productId){
       	List<SysProduct> list = new ArrayList();
  	//满足treeSeq .a.b.c. 的规则。
       	SysProduct sp = getSysProductByPdId(productId);
  	if(null!=sp){
  		//获取Seq
  		String sbTreeLinesSeq = sp.getTreeSeq();
  		sbTreeLinesSeq = sbTreeLinesSeq.substring(1,sbTreeLinesSeq.length()-1);//去除首尾字符
  		if(sbTreeLinesSeq.indexOf(".")>0){
  			String sbCode[] = StringUtils.split(sbTreeLinesSeq,".");
  			for(int i=0;i<sbCode.length;i++){
  			    list.add(getSysProductByPdCode(sbCode[i]));
  			}
  		}else{
  			//表示为顶点 
  			list.add(getSysProductByPdCode(sbTreeLinesSeq));
  		}
  	}
      return list;
    }
    
	/**D：所有本下级节点 **/
	public List<SysProduct> getDownLevelSysProductByPdId(String productId){
		SysProduct sp = getSysProductByPdId(productId);
    	List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
    	orders.add( new PropertyOrder("treeSeq",QueryConstants.ORDER_ASC));
    	return super.findByProperty("treeSeq", "%." + sp.getTreeSeq() + ".%",  QueryConstants.LIKE,orders);
    }
    
	/**V：垂直所有节点 **/ 
	public List<SysProduct> getVerticalLevelSysProductByPdId(String productId){
    	List<SysProduct> list = new ArrayList();
    	//这里 直接取上一级节点开始
    	list.addAll(getUpLevelSysProductByPdId(productId));//本上级所有节点
    	list.remove(getUpLevelSysProductByPdId(productId).size()-1);//移除本上级节点最后一个节点 即本节点
        list.addAll(getDownLevelSysProductByPdId(productId));//本下级所有节点
    	return list;
    }
    
	/**H: 水平所有节点 **/
	public List<SysProduct> getSameLevelSysProductByPdId(String productId){
		  SysProduct sp = getSysProductByPdId(productId);

		  return  getSysProductByParentPdId(sp.getParentId());
	}




	
}

