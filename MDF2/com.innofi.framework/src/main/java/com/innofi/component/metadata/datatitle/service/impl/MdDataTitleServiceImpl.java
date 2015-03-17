/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.component.metadata.datatitle.service.impl;

import com.innofi.component.metadata.datatitletablemapping.service.IMdDataTitleTableMappingService;
import com.innofi.framework.FrameworkConstants;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.PropertyOrder;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.framework.pojo.metadata.MdDataTitle;
import com.innofi.component.metadata.datatitle.service.IMdDataTitleService;
import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.pojo.metadata.MdDataTitleTableMapping;
import com.innofi.framework.service.impl.BaseServiceImpl;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能/ 模块：数据主题模块
 *
 * @author 张磊 Alex.zhang@innofi.com.cn
 * @version 2.0.0 12-11-30 数据主题Service实现类 修订历史： 日期 作者 参考 描述 北京名晟信息技术有限公司版权所有.
 */
@Component(value = "mdDataTitleService")
public class MdDataTitleServiceImpl extends
        BaseServiceImpl<MdDataTitle, String> implements IMdDataTitleService {

    @Resource(name="mdDataTitleTableMappingDaoSupport")
    protected DaoSupport<MdDataTitle, String> daoSupport;

    public DaoSupport<MdDataTitle, String> getDaoSupport() {
        return daoSupport;
    }

    /**
     * @see com.innofi.component.metadata.datatitle.service.IMdDataTitleService#findMdDataTitlesByParentId(String)
     */
    public List<MdDataTitle> findMdDataTitlesByParentId(String parentId) {
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        List<PropertyOrder> orders = new ArrayList<PropertyOrder>();
        addPropertyFilter(filters, "parentId", parentId, QueryConstants.EQUAL,
                true);
        addPropertyFilter(filters, "status",
                FrameworkConstants.STATUS_EFFECTIVE, QueryConstants.EQUAL, true);
        return findByPropertyFilters(filters, orders);
    }

    /**
     * @see com.innofi.component.metadata.datatitle.service.IMdDataTitleService#deleteDataTitle(MdDataTitle)
     */
    public void deleteDataTitle(MdDataTitle mdDataTitle) {
        String dataTitleId = mdDataTitle.getId();
        IMdDataTitleTableMappingService mdDataTitleTableMappingService = getSpringBean("mdDataTitleTableMappingService");
        List<MdDataTitleTableMapping> dataTitleTableMappingList = mdDataTitleTableMappingService
                .findByProperty("dataTitleId", dataTitleId,
                        QueryConstants.EQUAL);
        for (MdDataTitleTableMapping dataTitleTableMapping : dataTitleTableMappingList) {
            mdDataTitleTableMappingService.delete(dataTitleTableMapping);
        }
        delete(mdDataTitle);
    }
}
