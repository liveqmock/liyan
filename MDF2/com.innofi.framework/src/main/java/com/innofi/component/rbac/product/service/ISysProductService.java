
package com.innofi.component.rbac.product.service;

import java.util.List;

import com.innofi.component.rbac.product.pojo.SysProduct;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.pagination.Page;
import com.innofi.framework.service.IBaseService;

public interface ISysProductService  extends IBaseService<SysProduct,String> {

	/**
	 * 产品下拉框
	 * @param searchType
	 * @param parentId
	 * @param filters
	 * @param innofiPage
	 */
	void findSysProductList(String searchType, String parentId,List<PropertyFilter> filters,Page innofiPage);

	/**
	 * 产品停用
	 * @param code
	 * @return
	 */
	String invalidProduct(String code);
	
    /**
     * 通过产品ID找到条线信息
     */
	public SysProduct  getSysProductByPdId(String productId);
    /**
     * 通过条线Code找到条线信息
     */
	public SysProduct  getSysProductByPdCode(String productCode);
	/**
	 * 获取所有子节点
	 */
	public List<SysProduct> getSysProductByParentPdId(String productId);

	
	/**U：所有上级节点**/ 
    public List<SysProduct> getUpLevelSysProductByPdId(String productId);
    
	/**D：所有下级节点 **/
    public List<SysProduct> getDownLevelSysProductByPdId(String productId);
    
	/**V：垂直所有节点 **/ 
    public List<SysProduct> getVerticalLevelSysProductByPdId(String productId);
	
	/**H: 水平所有节点 **/
    public List<SysProduct> getSameLevelSysProductByPdId(String productId);
	/**O: 其它实际值**/
	
	
	
	
}