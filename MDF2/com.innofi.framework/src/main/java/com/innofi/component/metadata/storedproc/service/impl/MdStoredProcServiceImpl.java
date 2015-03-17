package com.innofi.component.metadata.storedproc.service.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.innofi.component.metadata.storedproc.service.IMdStoredProcService;
import com.innofi.framework.FrameworkConstants;
import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.framework.pojo.metadata.MdStoredProc;
import com.innofi.framework.service.impl.BaseServiceImpl;
import com.innofi.framework.utils.reflect.ReflectionUtil;


/**
 * 功能/ 模块：元数据模块
 *
 * @author 张磊 Alex.zhang@innofi.com.cn
 * @version 2.0.0 12-11-30
 *          测试Service接口
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
@Component(value = "mdStoredProcService")
public class MdStoredProcServiceImpl extends BaseServiceImpl<MdStoredProc, String> implements IMdStoredProcService {

    @Resource(name="mdDataTitleTableMappingDaoSupport")
    protected DaoSupport daoSupport;

    public DaoSupport getDaoSupport(){
        return daoSupport;
    }

    public List<MdStoredProc> findMdStoredProcByStatus(String status) {
        return findByProperty("status", status, QueryConstants.EQUAL);
    }
    
    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.metadata.tablespace.service.IMdStoredProcervice#refreshMetaData(Map)
     */
    public String refreshMetaData(Map<String, Object> parameter) {
        int result = 0; // 存储过程没有变化
        if (parameter != null) {
            List<String> ProcNameList = (List) parameter.get("arrProcName");
            for (String ProcName : ProcNameList) {
                if (refreshProcMetaData(ProcName) == 1) {
                    result = 1; // 存储过程有变化
                }
            }
        } else {
            result = refreshAllProcMetaData();
        }
        return String.valueOf(result);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.metadata.tablespace.service.IMdStoredProcervice#refreshProcMetaData(String)
     */
    public int refreshProcMetaData(String procName) {
        List<MdStoredProc> storedProcDBList = dynamicQueryBeanForList("metadata-dynamic-statement", "findProceduresInfo");// 数据字典中的存储过程信息
        List<MdStoredProc> storedProcList = findMdStoredProcByStatus(FrameworkConstants.STATUS_EFFECTIVE);                     //MD_STORED_PROC表中状态为1的存储过程信息

        int result = 0; // 存储过程没有变化

        MdStoredProc dbMdStoredProc = null;
        MdStoredProc preMdStoredProc = null;

        for (MdStoredProc storedProc : storedProcList) {
            String storedProcName = storedProc.getProcName();
            if (storedProcName.equals(procName)) {
                preMdStoredProc = storedProc;
                break;
            }
        }

        // 取该存储过程数据库字典中的信息
        for (MdStoredProc storedProcDB : storedProcDBList) {
            String storedProcDBName = storedProcDB.getProcName();
            if (storedProcDBName.equals(procName)) {
                dbMdStoredProc = storedProcDB;
                break;
            }
        }

        if (dbMdStoredProc == null) {
			if (preMdStoredProc != null) {
	        	// 数据库中不存在该存储过程,更新原存储过程为无效状态
	            preMdStoredProc.setStatus(FrameworkConstants.STATUS_INVALID);
	            update(preMdStoredProc);
	            result = 1;
        	}
        } else if (preMdStoredProc == null) { 
        	// 新增加的存储过程
            // 将数据库字典中的信息覆盖MD_STORED_PROC表中取该存储过程
            dbMdStoredProc.setStatus(FrameworkConstants.STATUS_EFFECTIVE);
            save(dbMdStoredProc);
            result = 1;

        } else {
            // 将数据库字典中的信息覆盖MD_STORED_PROC表中取该存储过程
            boolean tableEq = preMdStoredProc.equals(dbMdStoredProc);

            if (!tableEq) {
                ReflectionUtil.copyProperties(dbMdStoredProc,preMdStoredProc);
                // 保存最新的存储过程
            	update(preMdStoredProc);
                result = 1;
            }
        }

        return result;
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.component.metadata.tablespace.service.IMdTableSpaceService#refreshAllProcMetaData()
     */
    public int refreshAllProcMetaData() {
        int result = 0; // 存储过程信息没有变化
        List<MdStoredProc> storedProcDBList = dynamicQueryBeanForList("metadata-dynamic-statement", "findProceduresInfo");// 数据字典中的存储过程信息
        for (MdStoredProc storedProcDB : storedProcDBList) {
            String storedProcDBName = storedProcDB.getProcName();
            if (refreshProcMetaData(storedProcDBName) == 1) {
                result = 1;
            }
        }

        return result;
    }
}

