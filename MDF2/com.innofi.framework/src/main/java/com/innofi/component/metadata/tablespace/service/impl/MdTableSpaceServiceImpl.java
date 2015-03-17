/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.component.metadata.tablespace.service.impl;

import com.innofi.component.metadata.tablespace.service.IMdTableSpaceService;
import com.innofi.framework.FrameworkConstants;
import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.framework.pojo.metadata.MdTableSpace;
import com.innofi.framework.service.impl.BaseServiceImpl;
import com.innofi.framework.utils.reflect.ReflectionUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 功能/ 模块：测试模块
 *
 * @author 张磊 Alex.zhang@innofi.com.cn
 * @version 2.0.0 12-11-30
 *          类描述
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
@Component(value = "mdTableSpaceService")
public class MdTableSpaceServiceImpl extends BaseServiceImpl<MdTableSpace, String> implements IMdTableSpaceService {


    @Resource(name="mdDataTitleTableMappingDaoSupport")
    protected DaoSupport daoSupport;

    public DaoSupport getDaoSupport(){
        return daoSupport;
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.metadata.tablespace.service.IMdTableSpaceService#findMdTableSpacesByStatus(String)
     */
    public List<MdTableSpace> findMdTableSpacesByStatus(String status) {
        return findByProperty("status", status, QueryConstants.EQUAL);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.metadata.tablespace.service.IMdTableSpaceService#refreshMetaData(Map)
     */
    public String refreshMetaData(Map<String, Object> parameter) {
        int result = 0; // 表空间没有变化
        if (parameter != null) {
            List<String> tbsNameList = (List) parameter.get("arrTbsName");
            for (String tbsName : tbsNameList) {
                if (refreshTbsMetaData(tbsName) == 1) {
                    result = 1; // 表空间有变化
                }
            }
        } else {
            result = refreshAllTbsMetaData();
        }
        return String.valueOf(result);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.metadata.tablespace.service.IMdTableSpaceService#refreshTbsMetaData(String)
     */
    public int refreshTbsMetaData(String tbsName) {
        List<MdTableSpace> tbsDBList = dynamicQueryBeanForList("metadata-dynamic-statement", "findTableSpaceInfo");// 数据字典中的表空间信息
        List<MdTableSpace> tbsList = findMdTableSpacesByStatus(FrameworkConstants.STATUS_EFFECTIVE);                     //MD_TABESPACE表中状态为1的表空间信息

        int result = 0; // 表空间没有变化

        MdTableSpace dbMdTableSpace = null;
        MdTableSpace preMdTableSpace = null;

        for (MdTableSpace tbsPojo : tbsList) {
            String tbsPojoName = tbsPojo.getTbsName();
            if (tbsPojoName.equals(tbsName)) {
                preMdTableSpace = tbsPojo;
                break;
            }
        }

        // 取该表空间数据库字典中的信息
        for (MdTableSpace tbsPojo : tbsDBList) {
            String tbsPojoName = tbsPojo.getTbsName();
            if (tbsPojoName.equals(tbsName)) {
                dbMdTableSpace = tbsPojo;
                break;
            }
        }

        if (dbMdTableSpace == null) {
			if (preMdTableSpace != null) {
	        	// 数据库中不存在该表空间,更新原表空间为无效状态
	            preMdTableSpace.setStatus(FrameworkConstants.STATUS_INVALID);
	            update(preMdTableSpace);
	            result = 1;
        	}
        } else if (preMdTableSpace == null) { 
        	// 新增加的表空间
            // 将数据库字典中的信息覆盖MD_TABESPACE表中取该表空间
            dbMdTableSpace.setStatus(FrameworkConstants.STATUS_EFFECTIVE);
            save(dbMdTableSpace);
            result = 1;

        } else {
            // 将数据库字典中的信息覆盖MD_TABESPACE表中取该表空间
            boolean tableEq = preMdTableSpace.equals(dbMdTableSpace);

            if (!tableEq) {
                ReflectionUtil.copyProperties(dbMdTableSpace,preMdTableSpace);
                // 保存最新的表空间
            	update(preMdTableSpace);
                result = 1;
            }
        }

        return result;
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.metadata.tablespace.service.IMdTableSpaceService#refreshAllTbsMetaData()
     */
    public int refreshAllTbsMetaData() {
        int result = 0; // 表空间信息没有变化
        List<MdTableSpace> tbsDBList = dynamicQueryBeanForList("metadata-dynamic-statement", "findTableSpaceInfo");// 数据字典中的表空间信息
        for (MdTableSpace tbsPojo : tbsDBList) {
            String tbsName = tbsPojo.getTbsName();
            if (refreshTbsMetaData(tbsName) == 1) {
                result = 1;
            }
        }

        return result;
    }

}
