package com.innofi.component.codebuilder.service.impl;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.innofi.component.codebuilder.pojo.SysCodeBuilder;
import com.innofi.component.codebuilder.pojo.SysCodePattern;
import com.innofi.component.codebuilder.service.ISysCodeBuilderService;
import com.innofi.component.codebuilder.service.ISysCodePatternService;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.PropertyOrder;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.component.rbac.user.pojo.SysUser;
import com.innofi.framework.service.impl.BaseServiceImpl;
import com.innofi.framework.spring.context.ContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司
 * all rights reserved.
 *
 * @author liumy2009@126.com
 * @version 2.0
 *          业务编码生成服务类
 * @found date: 2010-11-23
 * @found time: 20:40:56
 */
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
@Service(value = "sysCodeBuilderService")
public class SysCodeBuilderServiceImpl extends BaseServiceImpl<SysCodeBuilder, String> implements ISysCodeBuilderService {

    @Resource(name="mdDataTitleTableMappingDaoSupport")
    protected DaoSupport daoSupport;

    public DaoSupport getDaoSupport(){
        return daoSupport;
    }



    public synchronized String saveCodeByBuilderType(String builderType) {
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        this.addPropertyFilter(filters, "builderType", builderType, QueryConstants.EQUAL, true);
        List<SysCodeBuilder> codeBuilders = this.findByPropertyFilters(filters, null);
        if (codeBuilders.size() > 0) {
            SysCodeBuilder builder = codeBuilders.get(0);
            ISysCodePatternService sysCodePatternSevice = getSpringBean("sysCodePatternService");
            //更新
            filters.clear();
            this.addPropertyFilter(filters, "builderId", builder.getId(), QueryConstants.EQUAL, true);
            this.addPropertyFilter(filters, "patternType", "4", QueryConstants.EQUAL, true);//类型为序号
            List<SysCodePattern> seqPatterns = sysCodePatternSevice.findByPropertyFilters(filters, null);
            for (SysCodePattern pattern : seqPatterns) {
                pattern.setCurrentSeq(pattern.getCurrentSeq().add(pattern.getValueStep()));
                sysCodePatternSevice.update(pattern);
            }

            //查询
            List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
            StringBuilder buff = new StringBuilder();
            PropertyOrder order = new PropertyOrder("patternSeq", QueryConstants.ORDER_ASC);
            orders.add(order);
            this.removePropertyFilter(filters, "patternType");
            List<SysCodePattern> patterns = sysCodePatternSevice.findByPropertyFilters(filters, orders);
            //
            String delimiter = StringUtils.isBlank(builder.getDelimiter()) ? "" : builder.getDelimiter();
            for (int i = 0; i < patterns.size(); i++) {
                SysCodePattern pattern = patterns.get(i);
                if (i != 0) {
                    buff.append(delimiter);
                }
                buff.append(this.parseCodePattern(pattern));
            }
            return buff.toString();
        } else {
            return "请设置'" + builderType + "'编码规则";
        }

    }


    private String parseCodePattern(SysCodePattern pattern) {
        StringBuffer rsCode = new StringBuffer();
        String type = pattern.getPatternType();
        if ("1".equals(type)) {//固定值
            return pattern.getValueFmt();
        } else if ("2".equals(type)) {//日期
            String format = "yyyyMMdd";
            if (!StringUtils.isBlank(pattern.getValueFmt())) {
                format = pattern.getValueFmt();
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            return dateFormat.format(new Date());
        } else if ("3".equals(type)) {//机构
            SysUser user = (SysUser) ContextHolder.getContext().getLoginUser();
            return user.getOrgCode();
        } else if ("4".equals(type)) {//序号
            int len = pattern.getValueLen().intValue();
            String currSeq = pattern.getCurrentSeq().toString();
            for (int i = 0; i < len - currSeq.length(); i++) {
                rsCode.append("0");
            }
            rsCode.append(currSeq);
        }
        return rsCode.toString();
    }
}

