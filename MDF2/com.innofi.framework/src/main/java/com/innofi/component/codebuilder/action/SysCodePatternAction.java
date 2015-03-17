package com.innofi.component.codebuilder.action;
import com.innofi.component.codebuilder.pojo.SysCodePattern;
import com.innofi.component.codebuilder.service.ISysCodePatternService;

import com.innofi.framework.action.impl.BaseActionImpl;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.PropertyOrder;
import com.innofi.framework.dao.hibernate.QueryConstants;



import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import java.util.Map;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;


/**
 * 功能/ 模块：todo
 *
 * @author  todo 开发人员姓名 邮箱
 * @version todo 当前版本 日期
 *          todo 为dorado7界面维护操作的提供支持，实现SysCodePattern对象信息的加载与保存操作
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
@Component
public class SysCodePatternAction extends BaseActionImpl {
    @Resource
	private   ISysCodePatternService sysCodePatternSevice;

    @DataProvider
    public List<SysCodePattern> findSysCodePatterns(Map<String, Object> parameter) throws Exception {
    	List<PropertyFilter> propertyFilters = this.buildEqPropertyFilters(parameter);
        List<PropertyOrder> orders=new ArrayList<PropertyOrder>();
        PropertyOrder order=new PropertyOrder("patternSeq",QueryConstants.ORDER_ASC);
        orders.add(order);
        return sysCodePatternSevice.findByPropertyFilters(propertyFilters, orders);
    }

    @DataResolver
    public void saveSysCodePatterns(Collection<SysCodePattern> objs) {
		for (Iterator iterator = objs.iterator(); iterator.hasNext(); ) {
			SysCodePattern sysCodePattern = (SysCodePattern) iterator.next();
            EntityState state = EntityUtils.getState(sysCodePattern);
            if (EntityState.DELETED.equals(state)) {
            	sysCodePatternSevice.delete(sysCodePattern);
            } else if (EntityState.MODIFIED.equals(state) || EntityState.MOVED.equals(state)) {
            	sysCodePatternSevice.update(sysCodePattern);
            } else if (EntityState.NEW.equals(state)) {
            	sysCodePatternSevice.save(sysCodePattern);
            }
		}
    }
}
