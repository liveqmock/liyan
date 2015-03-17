
package com.innofi.framework.dao.pagination.impl;

import com.innofi.framework.dao.jdbc.IJdbcDao;
import com.innofi.framework.dao.jdbc.sqldialect.DBDialect;
import com.innofi.framework.dao.jdbc.sqldialect.DBDialectUtil;
import com.innofi.framework.dao.pagination.PaginationQuery;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author jack.liu@innofi.com
 * @version 2.0
 */
public class PaginationQueryImpl<T> implements PaginationQuery<T> {

	public PaginationQueryImpl(IJdbcDao jdbcDao) {
		this.jdbcDao=jdbcDao;
	}

	/* (non-Javadoc)
	 * @see com.innofi.framework.dao.pagination.PaginationQuery#pageNo(int)
	 */
	public PaginationQuery<T> pageNo(int pageNo) {
		Assert.state(pageNo > 0, "页号必须大于0");
		this.pageNo = pageNo;
		return this;
	}

	/* (non-Javadoc)
	 * @see com.innofi.framework.dao.pagination.PaginationQuery#pageSize(int)
	 */
	public PaginationQuery<T> pageSize(int pageSize) {
		Assert.state(pageSize > 0, "页大小必须大于0");
		this.pageSize = pageSize;
		return this;
	}


	/* (non-Javadoc)
	 * @see com.innofi.framework.dao.pagination.PaginationQuery#sql(java.lang.String)
	 */
	public PaginationQuery<T> sql(String sql) {
		this.sql = sql;
		return this;
	}

	/* (non-Javadoc)
	 * @see com.innofi.framework.dao.pagination.PaginationQuery#countSql(java.lang.String)
	 */
	public PaginationQuery<T> countSql(String countSql) {
		this.countSql = countSql;
		return this;
	}

	/* (non-Javadoc)
	 * @see com.innofi.framework.dao.pagination.PaginationQuery#parameters(java.lang.Object[])
	 */
	public PaginationQuery<T> parameters(Object[] parameters) {
		this.parameters = parameters;
		return this;
	}

	/* (non-Javadoc)
	 * @see com.innofi.framework.dao.pagination.PaginationQuery#rowMapper(org.springframework.jdbc.core.RowMapper)
	 */
	public PaginationQuery<T> rowMapper(RowMapper<T> rowMapper) {
		if (null != rowMapper) {
			this.rowMapper = rowMapper;
		}
		return this;
	}

	/* (non-Javadoc)
	 * @see com.innofi.framework.dao.pagination.PaginationQuery#count()
	 */
	public int count() {
		if (!StringUtils.hasText(this.countSql)) {
			if (!StringUtils.hasText(this.sql)) {
				throw new IllegalArgumentException("必须至少指定countSql或sql中的一个");
			}
			String tmp = this.sql.toLowerCase();
			int firstIndexOfFrom = tmp.indexOf(" from ");
			Assert.state(-1 < firstIndexOfFrom, "您的sql里没有from关键字,请确认其是一条查询sql");
			this.countSql = "SELECT COUNT(*) "
					+ this.sql.substring(firstIndexOfFrom + 1);
		}
		int count = this.jdbcDao.queryForInt(this.countSql,this.parameters);
		return count;
	}

	/* (non-Javadoc)
	 * @see com.innofi.framework.dao.pagination.PaginationQuery#list()
	 */
	@SuppressWarnings("unchecked")
	public List<T> list() {
		DBDialect dbDialect = this.jdbcDao.execute(
				new ConnectionCallback<DBDialect>() {

					public DBDialect doInConnection(Connection connection)
							throws SQLException, DataAccessException {
						return DBDialectUtil.getDBDialect(connection);
					}

				});
		if(null == this.rowMapper){
			this.rowMapper = (RowMapper<T>)COLUMN_MAP_ROW_MAPPER;
		}
		List<T> list = dbDialect.query(this.jdbcDao,this.sql,this.parameters,this.pageNo, this.pageSize,this.rowMapper);

        return list;
	}


	private int pageNo = 1;
	private int pageSize = DEFAULT_PAGE_SIZE;
	private IJdbcDao jdbcDao;
	private String sql = null;
	private String countSql = null;
	private Object[] parameters = null;
	private RowMapper<T> rowMapper = null;

	private static final ColumnMapRowMapper COLUMN_MAP_ROW_MAPPER = new ColumnMapRowMapper();

}
