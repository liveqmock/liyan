
package com.innofi.component.rbac.privilage.service;

import com.innofi.component.rbac.privilage.pojo.SysObjBusiness;
import com.innofi.framework.service.IBaseService;

/**
 * 功能/ 模块：权限模块
 *
 * @author  liumy liumy2009@126.com
 * @version 1.0.6 2013-08-19
 *          机构、岗位、用户与业务条线关系
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
public interface ISysObjBusinessService  extends IBaseService<SysObjBusiness,String> {


    /**
     * 根据配置的管理策略返回用户当前业务条线字符串<br/>
     * 返回对应的查询SQL select中值包含busiline_id
     * @param isHQL 是否为hql查询,hql查询时应返回hql语句
     * @return 1、值形式返回:，多个用','分割, 格式:"'业务条线1编码','业务条线2编码'..." 2、查询语句形式返回 SQL:select busiline_id from ..... HQL：select t.busilineId from ....
     */
    public String getSysUserCurrentBusinessLine(boolean isHQL);

}