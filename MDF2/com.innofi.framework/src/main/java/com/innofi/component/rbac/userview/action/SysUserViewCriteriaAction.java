package com.innofi.component.rbac.userview.action;
import com.innofi.framework.service.IBaseService;
import com.innofi.component.rbac.userview.pojo.SysUserViewCriteria;
import com.innofi.component.rbac.userview.service.ISysUserViewCriteriaService;
import com.innofi.framework.action.impl.BaseActionImpl;
import java.util.Collection;
import java.util.Iterator;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;
import com.innofi.framework.utils.validate.Validator;
import org.hibernate.criterion.DetachedCriteria;
import com.bstek.dorado.hibernate.HibernateUtils;
import com.bstek.dorado.data.provider.Criteria;


/**
* 功能/ 模块：SysUserViewCriteria模块
*
* @author  liumy2009@126.com
* @version V1.0.0
*          为dorado7界面维护操作的提供支持，实现SysUserViewCriteria对象信息的加载与保存操作
*          修订历史：
*          日期  作者  参考  描述
*          北京名晟信息技术有限公司版权所有.
*/
@Component("sysUserViewCriteriaAction")
public class SysUserViewCriteriaAction extends BaseActionImpl {
    @Resource
    private ISysUserViewCriteriaService sysUserViewCriteriaService;

    public IBaseService getBusinessService(){
        return sysUserViewCriteriaService;
    }

    @DataProvider
    public void findSysUserViewCriterias(Page page, Criteria criteria) throws Exception {
        com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
        if(Validator.isNotNull(criteria)){
            DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getBusinessService().getEntityType());
            HibernateUtils.createFilter(detachedCriteria, criteria);
            getBusinessService().findByPageAndCriteria(innofiPage, detachedCriteria);
        }else{
            getBusinessService().findByPage_Filters(null, null, innofiPage);
        }
    }

    @DataResolver
    public void saveSysUserViewCriterias(Collection<SysUserViewCriteria> objs) {
        for (Iterator iterator = objs.iterator(); iterator.hasNext(); ) {
            SysUserViewCriteria sysUserViewCriteria = (SysUserViewCriteria) iterator.next();
            EntityState state = EntityUtils.getState(sysUserViewCriteria);
            if (EntityState.NEW.equals(state)) {
                sysUserViewCriteriaService.save(sysUserViewCriteria);
            } else if (EntityState.MODIFIED.equals(state)) {
                sysUserViewCriteriaService.update(sysUserViewCriteria);
            } else if (EntityState.DELETED.equals(state)) {
                sysUserViewCriteriaService.delete(sysUserViewCriteria);
            }
        }
    }
}
