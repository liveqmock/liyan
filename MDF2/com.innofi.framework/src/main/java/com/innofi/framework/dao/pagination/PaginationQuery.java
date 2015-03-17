/**
 * PaginationQuery.java
 */
package com.innofi.framework.dao.pagination;

import org.springframework.jdbc.core.RowMapper;

import java.util.List;

/**
 * 分页查询器接口.
 * <p>
 * 定义了如何进行分页查询的接口.<br/>
 * </p>
 * @author jack.liu@innofi.com
 * @version 2.0
 */
public interface PaginationQuery<T> {
	public static final int DEFAULT_PAGE_SIZE = 10;

	/**
	 * 页号.
	 * 
	 * @param pageNo
	 *            页号
	 * @return 自身
	 */
	public PaginationQuery<T> pageNo(int pageNo);

	/**
	 * 页大小.
	 * 
	 * @param pageSize
	 *            页大小
	 * @return 自身
	 */
	public PaginationQuery<T> pageSize(int pageSize);

	/**
	 * 不带分页的查询sql.
	 * 
	 * @param sql
	 *            查询sql
	 * @return 自身
	 */
	public PaginationQuery<T> sql(String sql);

	/**
	 * 不带分页的查询数据量的sql.
	 * 
	 * @param countSql
	 *            查询数据量的sql
	 * @return 自身
	 */
	public PaginationQuery<T> countSql(String countSql);

	/**
	 * 指定参数数组.
	 * 
	 * @param parameters
	 *            参数数组
	 * @return 自身
	 */
	public PaginationQuery<T> parameters(Object[] parameters);

	/**
	 * 行映射器.
	 * 
	 * @param rowMapper
	 *            行映射器.
	 * @return 自身
	 */
	public PaginationQuery<T> rowMapper(RowMapper<T> rowMapper);

	/**
	 * 获取全部记录数.
	 * 
	 * @return 全部记录数
	 */
	public int count();

	/**
	 * 取得分页结果.
	 * 
	 * @return 结果
	 */
	public List<T> list();
}
