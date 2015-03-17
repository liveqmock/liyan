package com.innofi.component.codebuilder.action;

import com.innofi.component.codebuilder.pojo.SysCodeBuilder;
import com.innofi.component.codebuilder.pojo.SysCodePattern;
import com.innofi.component.codebuilder.service.ISysCodeBuilderService;
import com.innofi.component.codebuilder.service.ISysCodePatternService;
import com.innofi.framework.action.impl.BaseActionImpl;
import com.innofi.framework.dao.hibernate.PropertyFilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import java.util.Map;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;


/**
 * 功能/ 模块：todo
 *
 * @author todo 开发人员姓名 邮箱
 * @version todo 当前版本 日期
 *          todo 为dorado7界面维护操作的提供支持，实现SysCodeBuilder对象信息的加载与保存操作
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
@Component
public class SysCodeBuilderAction extends BaseActionImpl {
    @Resource
    private ISysCodeBuilderService sysCodeBuilderService;
    @Resource
    private ISysCodePatternService sysCodePatternService;

    @DataProvider
    public void findSysCodeBuilders(Page page, Map<String, Object> parameter) throws Exception {
        List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
        if (parameter != null) {
            //addPropertyFilter(propertyFilters,"pattern",parameter.get("pattern"),parameter.get("qMpattern").toString(),true);
            addPropertyFilter(propertyFilters, "builderType", parameter.get("builderType"), parameter.get("qMbuilderType").toString(), true);
            //addPropertyFilter(propertyFilters,"currentSeq",parameter.get("currentSeq"),parameter.get("qMcurrentSeq").toString(),true);
            addPropertyFilter(propertyFilters, "remark", parameter.get("remark"), parameter.get("qMremark").toString(), true);
            //addPropertyFilter(propertyFilters,"prefix",parameter.get("prefix"),parameter.get("qMprefix").toString(),true);
        }
        com.innofi.framework.dao.pagination.Page innofiPage = super.translateDoradoPageToInnofiPage(page);
        sysCodeBuilderService.findByPage_Filters(propertyFilters, null, innofiPage);
    }

    @Expose
    public String findCodeByBuilderType(String builderType) {
        return sysCodeBuilderService.saveCodeByBuilderType(builderType);
    }

    @DataResolver
    public void saveSysCodeBuilders(Collection<SysCodeBuilder> objs) {
        for (Iterator<SysCodeBuilder> iterator = objs.iterator(); iterator.hasNext(); ) {
            SysCodeBuilder codeBuilder = iterator.next();
            Collection<SysCodePattern> patterns = EntityUtils.getValue(codeBuilder, "patterns");

            if (EntityState.NEW.equals(EntityUtils.getState(codeBuilder))) {
                persistCodeBuilder(codeBuilder);
                for (SysCodePattern pattern : patterns) {
                    pattern.setBuilderId(codeBuilder.getId());
                }
                if (patterns != null && patterns.size() > 0) {
                    persistPatterns(codeBuilder, patterns);
                }
            } else if (EntityState.DELETED.equals(EntityUtils.getState(codeBuilder))) {
                if (patterns != null && patterns.size() > 0) {
                    persistPatterns(codeBuilder, patterns);
                }
                persistCodeBuilder(codeBuilder);
            } else if (EntityState.MODIFIED.equals(EntityUtils.getState(codeBuilder))) {
                persistCodeBuilder(codeBuilder);
                if (patterns != null && patterns.size() > 0) {
                    persistPatterns(codeBuilder, patterns);
                }
            }
        }
    }

    private void persistCodeBuilder(SysCodeBuilder codeBuilder) {
        EntityState state = EntityUtils.getState(codeBuilder);
        if (EntityState.NEW.equals(state)) {
            sysCodeBuilderService.save(codeBuilder);
        } else if (EntityState.MODIFIED.equals(state)) {
            sysCodeBuilderService.update(codeBuilder);
        } else if (EntityState.DELETED.equals(state)) {
            sysCodeBuilderService.delete(codeBuilder);
        }
    }

    private void persistPatterns(SysCodeBuilder codeBuilder, Collection<SysCodePattern> patterns) {
        EntityState state;
        for (SysCodePattern pattern : patterns) {
            state = EntityUtils.getState(codeBuilder);
            if (EntityState.NEW.equals(state)) {
                sysCodePatternService.save(pattern);
            } else if (EntityState.MODIFIED.equals(state)) {
                sysCodePatternService.update(pattern);
            } else if (EntityState.DELETED.equals(state)) {
                sysCodePatternService.delete(pattern);
            }
        }
    }
}
