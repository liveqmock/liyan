package com.innofi.component.message.dao.impl;

import java.util.List;
import java.util.Map;

import com.innofi.component.message.dao.IGetSysParameterDao;
import com.innofi.framework.dao.DaoSupport;

public class GetSysParameterDaoImpl extends DaoSupport implements
		IGetSysParameterDao {

	public static final String sql = "SELECT para_value from sys_parameter where para_code=?";

	@Override
	public String getSysParameter(String paraCode) {
		List<Map<String, Object>> list = this.getJdbcDao().queryForList(sql, new Object[] { paraCode });
		String retStr = "";
		if (list != null && list.size() > 0) {
			retStr = (String) list.get(0).get("PARA_VALUE");
		}
		return retStr;
	}
}
