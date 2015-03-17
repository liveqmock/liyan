
package com.innofi.component.rbac.busiline.service;

import java.util.List;

import com.innofi.component.rbac.busiline.pojo.SysBusinessLine;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.pagination.Page;
import com.innofi.framework.service.IBaseService;

public interface ISysBusinessLineService  extends IBaseService<SysBusinessLine,String> {

	/**
	 * 业务线下拉框
	 * @param searchType
	 * @param parentBusilineId
	 * @param propertyFilters
	 * @param innofiPage
	 */
	void findSysBusinessLineList(String searchType, String parentBusilineId,List<PropertyFilter> propertyFilters,Page innofiPage);

	/**
	 * 停用业务线
	 * @param code
	 * @return
	 */
	String invalidBizLine(String code);
	
    /**
     * 通过条线ID找到条线信息
     */
	public SysBusinessLine  getBusLineByBusId(String busilineId);
    /**busilineId
     * 通过条线Code找到条线信息
     */
	public SysBusinessLine  getBusLineByBusCode(String productCode);
	/**
	 * 
	   
	 * getBusLinesByParentBusId(这里用一句话描述这个方法的作用)    
	   
	 * @param   name    
	   
	 * @param  @return    设定文件    
	   
	 * @return String    DOM对象    
	   
	 * @Exception 异常对象    
	   
	 * @since  CodingExample　Ver(编码范例查看) 1.1
	 */
	public List<SysBusinessLine> getBusLinesByParentBusId(String busilineId);

	/**U：所有上级节点**/ 
    public List<SysBusinessLine> getUpLevelBusLinesByBusId(String busilineId);
    
	/**D：所有下级节点 **/
    public List<SysBusinessLine> getDownLevelBusLinesByBusId(String busilineId);
    
	/**V：垂直所有节点 **/ 
    public List<SysBusinessLine> getVerticalLevelBusLinesByBusId(String busilineId);
	
	/**H: 水平所有节点 **/
    public List<SysBusinessLine> getSameLevelBusLinesByBusId(String busilineId);
	/**O: 其它实际值**/
	
}