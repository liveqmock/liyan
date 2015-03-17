package com.innofi.component.rbac.parameter.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.innofi.framework.spring.context.ContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.innofi.component.rbac.parameter.pojo.SysParameter;
import com.innofi.component.rbac.parameter.service.ISysParameterService;
import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.framework.service.impl.BaseServiceImpl;


@Service(value = "sysParameterService")
public class SysParameterServiceImpl extends
        BaseServiceImpl<SysParameter, String> implements ISysParameterService {

    @Resource(name="sysParameterDaoSupport")
    protected DaoSupport daoSupport;

    public static Map<String,String> parameters = new HashMap<String, String>();

    public DaoSupport getDaoSupport(){
        return daoSupport;
    }

    public void setDaoSupport(DaoSupport daoSupport) {
		this.daoSupport = daoSupport;
	}



	public SysParameterServiceImpl(){
    	/*String parameterValue = "";
    	List<SysParameter> sysParameters = this.getAll(null);
        for(SysParameter sysParameter : sysParameters){
            parameters.put(sysParameter.getParaCode(),sysParameter.getParaValue());
        }*/
    }

    public boolean checkCode(String paraCode) {
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        this.addPropertyFilter(filters, "paraCode", paraCode,QueryConstants.EQUAL, true);
        List<SysParameter> list = this.findByPropertyFilters(filters, null);
        if (list.size() > 0) {
            return false;
        } else {
            return true;
        }
    }

    public String getParamValue(String paraCode) {
        String parameterValue = "";

        if (parameters.size()!=0) {
            parameterValue = parameters.get(paraCode);
        }else{
            List<SysParameter> sysParameters = this.getAll(null);
            for(SysParameter sysParameter : sysParameters){
                parameters.put(sysParameter.getParaCode(),sysParameter.getParaValue());
            }
            parameterValue = parameters.get(paraCode);
        }
        if (null != parameterValue && !"".equals(parameterValue)) {
            return parameterValue;
        }
        return null;
    }

    /**
     * 修改系统参数值
     *
     * @param input
     */
    public String updateParamValue(Map<String, String> input) {
        String paraValueDefault = "";
        if (null != input) {
            String paraId = input.get("paraId");
            String paraCode = input.get("paraCode");
            String paraValue = input.get("paraValue");
            paraValueDefault = ContextHolder.getSystemProperties().getDefaultString(paraCode, paraValue);
            this.getDaoSupport().getJdbcDao().update("update sys_parameter set para_value=? where para_id=?", new Object[]{paraValueDefault, paraId});
            parameters.put(paraCode,paraValueDefault);
        }
        return paraValueDefault;
    }


    public void loadNewParameter(){
        Map<String,String> properties = ContextHolder.getSystemProperties().getProperties();
        for(Map.Entry<String,String> property:properties.entrySet()){
            SysParameter sysParameter = findUniqueByProperty("paraCode",property.getKey(),QueryConstants.EQUAL);
            if(sysParameter==null){
                sysParameter = new SysParameter();
                sysParameter.setParaCode(property.getKey());
                sysParameter.setParaValue(property.getValue());
                save(sysParameter);
            }
        }
    }

}
