
package com.innofi.component.rbac.privilage.service.impl;


import com.innofi.component.rbac.privilage.pojo.SysObjBusiness;
import com.innofi.framework.service.impl.BaseServiceImpl;
import com.innofi.component.rbac.privilage.service.ISysObjBusinessService;

import javax.annotation.Resource;

import com.innofi.component.rbac.post.pojo.SysPost;
import com.innofi.component.rbac.user.pojo.SysUser;
import com.innofi.framework.spring.context.ContextHolder;
import com.innofi.framework.utils.validate.Validator;
import org.springframework.stereotype.Component;
import com.innofi.framework.dao.DaoSupport;

import java.util.List;


/**
 * 功能/ 模块：权限模块
 *
 * @author liumy liumy2009@126.com
 * @version 1.0.6 2013-08-19
 *          机构、岗位、用户与业务条线关系实现类
 *          修订历史：日期  作者  参考  描述
 *          2013-08-19 liumy 无 创建
 *          北京名晟信息技术有限公司版权所有.
 */
@Component(value = "sysObjBusinessService")
public class SysObjBusinessServiceImpl extends BaseServiceImpl<SysObjBusiness, String> implements ISysObjBusinessService {

    @Resource(name="mdDataTitleTableMappingDaoSupport")
    protected DaoSupport daoSupport;

    public DaoSupport getDaoSupport(){
        return daoSupport;
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.rbac.privilage.service.ISysObjBusinessService#getSysUserCurrentBusinessLine(boolean)
     */
    public String getSysUserCurrentBusinessLine(boolean isHql) {
        String queryBusinessSql = "'noexsit'";
        SysUser sysUser = ContextHolder.getContext().getLoginUser();

        if ("1".equals(ContextHolder.getSystemProperties().getString("mdf.sysuser.bizline.pattern"))) {//机构模式
            String orgId = sysUser.getSysOrgInfo().getId();//当前用户所属部门ID
            if (Validator.isNotEmpty(orgId)) {
                if (isHql) {
                    queryBusinessSql = "select t.busilineId from SysObjBusiness t where t.objId='" + orgId + "' and t.objBusilineType='1'";
                } else {
                    queryBusinessSql = "select t.busiline_id from sys_obj_business t where t.obj_id='" + orgId + "' and t.obj_busiline_type='1'";
                }
            }
        } else if ("2".equals(ContextHolder.getSystemProperties().getString("mdf.sysuser.bizline.pattern"))) {//岗位模式
            StringBuffer postIds = new StringBuffer();//用户岗位IDS
            List<SysPost> postList = sysUser.getPosts();
            int index = 0;
            for (SysPost sysPost : postList) {
                if (index == postList.size() - 1) {
                    postIds.append("'" + sysPost.getPostName() + "'");
                } else {
                    postIds.append("'" + sysPost.getPostName() + "',");
                }
                index++;
            }
            if (Validator.isNotEmpty(postIds.toString())) {
                if (isHql) {
                    queryBusinessSql = "select t.busilineId from SysObjBusiness t where t.objId in (" + postIds.toString() + ") and t.objBusilineType='2'";
                } else {
                    queryBusinessSql = "select t.busiline_id from sys_obj_business t where t.obj_id in (" + postIds.toString() + ") and t.obj_busiline_type='2'";
                }

            }
        } else if ("3".equals(ContextHolder.getSystemProperties().getString("mdf.sysuser.bizline.pattern"))) {//用户模式
            String userId = sysUser.getId();
            if (Validator.isNotEmpty(userId)) {
                if (isHql) {
                    queryBusinessSql = "select t.busilineId from SysObjBusiness t where t.objId ='" + userId + "' and t.objBusilineType='3'";
                } else {
                    queryBusinessSql = "select t.busiline_id from sys_obj_business t where t.obj_id = '" + userId + "' and t.obj_busiline_type='3'";
                }

            }
        }
        return queryBusinessSql;
    }

}

