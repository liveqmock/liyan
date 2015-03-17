/** Copyright ? 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.component.rbac.privilage.service.impl;

import com.innofi.framework.service.IBaseService;
import com.innofi.framework.dao.pagination.Page;
import com.innofi.framework.pojo.BasePojo;
import com.innofi.framework.service.impl.BaseServiceImpl;
import org.hibernate.criterion.*;

import java.io.Serializable;
import java.util.*;

/**
 * 功能/ 模块：权限控制模块
 *
 * @author 刘名寓 liumy2009@126.com
 * @version 2.0.0 12-11-30
 *          基于权限框架的业务服务基类实现
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
public abstract class PrivilegeBaseServiceImpl<T extends BasePojo, PK extends Serializable> extends BaseServiceImpl<T, PK> implements IBaseService<T, PK> {

/*
    */
/**
     * @see com.innofi.framework.service.IBaseService#queryBeanForList(String sql, Object... objects)
     *//*

    public List<T> dynamicQueryBeanForList(String sql, Object... objects) {
        Page page = new Page(Page.BIG_PAGE_SIZE, 1);
        dynamicQueryBeanForPage(sql, page, objects);
        return page.getEntities();
    }

     *//*

    public void dynamicQueryBeanForPage(String sql, Page<T> page, Object... objects) {
    sql = assemblySqlRestrictions(sql);//增加校验策略
    getDaoSupport().dynamicQueryBeanForPage(entityType, sql, page, objects);
    }

    */
/**
     * @see com.innofi.framework.service.IBaseService#queryBeanForPage(String, String, List, Page)
     *//*

    public void dynamicQueryBeanForPage(String moduleId, String sqlId, List<SQLParam> params, Page<T> page) {
    String sql = getDaoSupport().getJdbcDao().getFormatHql(moduleId, sqlId, params);
    sql = assemblySqlRestrictions(sql);
    getDaoSupport().dynamicQueryBeanForPage(entityType, sql, page);
    ///////////////////////////MAP/////////////////////////////////

    */
/**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.IJdbcDao#queryMapForUnique(String, String, java.util.List)
     *//*

    public Map<String, Object> dynamicQueryMapForUnique(String moduleId, String sqlId, List<SQLParam> params) {
        List<Map<String, Object>> queryResult = dynamicQueryMapForList(moduleId, sqlId, params);
        if (queryResult.size() > 1) {
            throw new FrameworkJdbcRuntimeException("查询结果不唯一，请检查SQL的条件是否正确！");
            } else if (queryResult.size() == 0) {
            return new HashMap<String, Object>();
            }
        return queryResult.get(0);
        }

    */
/**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.IJdbcDao#queryMapForList(String, String, java.util.List)
     *//*

    public List<Map<String, Object>> dynamicQueryMapForList(String moduleId, String sqlId, List<SQLParam> params) {
        Page<Map<String, Object>> page = new Page<Map<String, Object>>(Page.BIG_PAGE_SIZE, 1);
        dynamicQueryMapForList(moduleId, sqlId, params, page);
        return page.getEntities();
        }

    */
/**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.IJdbcDao#queryMapForList(String, String)
     *//*

    public List<Map<String, Object>> dynamicQueryMapForList(String moduleId, String sqlId) {
        return dynamicQueryMapForList(moduleId, sqlId, new ArrayList<SQLParam>());
        }

    */
/**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.IJdbcDao#queryMapForList(String, com.innofi.framework.dao.pagination.Page)
     *//*

    public void dynamicQueryMapForList(String sql, Page<Map<String, Object>> page) {
        protected Class<T> entityType = getEntityType();//当前服务实体对象类型
        */
/**
         * 操作类型常量
         *//*

        private final String OPER_TYPE = "'" + FrameworkConstants.DA_TYPE_BROWSER + "','" + FrameworkConstants.DA_TYPE_MODIFY + "','" + FrameworkConstants.DA_TYPE_DELETE + "'";//默认值为  1,2,3  策略操作类型 ：1浏览 <2 修改 <3 删除  实际浏览：1,2,3 修改：2,3 删除：3

        */
/**
         * 根据策略编号，策略操作类型，查询策略控制信息SQL
         *//*

        private String policyDimenSql = "SELECT DISTINCT " +
                "policy.table_auth_id as table_auth_id," +   //策略ID
                "policy.table_name as p_table_name," +       //策略对应表名
                "dimension.table_name," +                    //维度对应表名
                "field.field_name," +                        //维度字段名
                "field.dimen_field_id," +                    //维度字段ID
                "data.dimen_value," +                        //维度值
                "data.dimen_auth_type," +                    //维度权限类型：例如机构维度，U：当前机构所有上级节点 D：当前机构所有下级节点 V：当前机构垂直所有节点 H: 当前机构水平所有节点 O: 实际值，
                "data.operator," +                           //操作类型：1:浏览 2:修改 3:删除
                "dimension.dimen_type," +                    //维度类型：机构、时间、业务线、产品、岗位等
                "related.restrict_id " +                     //关联关系ID 作为关联公式的KEY
                "FROM da_table_policy policy," +             //表权限控制策略表
                "da_dimen_control control," +                //维度控制表
                "da_dimen_data data," +                      //维度数据表
                "da_dimen_data_restrict_related related," +  //维度数据约束关系关联表
                "md_dimen_field field," +                    //维度详细信息表
                "md_dimension  dimension," +                 //维度表
                "md_table mdtable" +                         //表结构表
                " WHERE policy.table_auth_id = control.table_auth_id" +
                " AND control.dimen_control_id = data.dimen_control_id " +
                " AND data.dimen_field_id = field.dimen_field_id " +
                " AND data.dimen_data_id = related.dimen_data_id " +
                " AND field.dimen_id = dimension.dimen_id " +
                " AND policy.table_auth_id in (#policyId#) " +
                " AND policy.oper_type in (#opertype#)" +
                " AND mdtable.is_da_control = '1'" +
                " ORDER by policy.table_name, field.field_name";

        */
/**
         * 根据策略编号，策略操作类型，查询策略WHERE信息
         *//*

        private String policyWhereSql = "SELECT DISTINCT policy.table_name,policy.sql_where FROM da_table_policy policy,md_table mdtable " +
                "WHERE policy.table_auth_id IN (#policyId#) " +
                "AND policy.table_name = mdtable.table_name " +
                "AND policy.oper_type in (#opertype#) " +
                "AND mdtable.is_da_control = '1'";

//////////////////////////////////////////hql start/////////////////////////////////////////////////////////

        */
/**
         * @see com.innofi.framework.service.IBaseService#findByHql(String hql, Object... values)
         *//*

        public List<T> findByHql(String hql, Object... values) {
            hql = assemblyHqlRestrictions(hql, null, values);
            return getDaoSupport().getHibernateDao().findByHql(entityType, hql, values);
        }

        */
/**
         * @see com.innofi.framework.service.IBaseService#findByHql<String, Object>) {
         *//*

        public List<T> findByNamedHql(String hql, Map<String, ?> values) {
            hql = assemblyHqlRestrictions(hql, values, null);
            return getDaoSupport().getHibernateDao().findByNamedHql(entityType, hql, values);
        }

        */
/**
         * @see com.innofi.framework.service.IBaseService#findByHql_Page(String hql, Page page, Object... values)
         *//*

    public void findByHql_Page(String hql, Page page, Object... values) {
        hql = assemblyHqlRestrictions(hql, null, values);
        getDaoSupport().getHibernateDao().findByHql_Page(entityType, hql, page, values);
    }

    */
/**
     * @see com.innofi.framework.service.IBaseService#findByHql_Page(String hql, Page page, Object... values)
     *//*

    public void findByNamedHql_Page(String hql, Page page, Map<String, ?> values) {
        hql = assemblyHqlRestrictions(hql, values, null);
        getDaoSupport().getHibernateDao().findByNamedHql_Page(entityType, hql, page, values);
    }

    */
/**
     * @see com.innofi.framework.service.IBaseService#findUniqueByHql(String hql, Object... values)
     *//*

    public <X> X findUniqueByHql(String hql, Object... values) {
        hql = assemblyHqlRestrictions(hql, null, values);
        return (X) getDaoSupport().getHibernateDao().findUnique(entityType, hql, values);
    }

    */
/**
     * @see com.innofi.framework.service.IBaseService#findUniqueByHql(String sql, Object... objects)
     *//*

    @SuppressWarnings("unchecked")
    public <X> X findUniqueByNamedHql(String hql, Map<String, ?> values) {
        hql = assemblyHqlRestrictions(hql, values, null);
        return (X) getDaoSupport().getHibernateDao().findUnique(entityType, hql, values);
    }
//////////////////////////////////////////hql end/////////////////////////////////////////////////////////
//////////////////////////////////////////sql start///////////////////////////////////////////////////////

    */
/**
     * @see com.innofi.framework.service.IBaseService#queryBeanForUnique(String sql, Object... objects)
     *//*

    public <X> X dynamicQueryBeanForUnique(String sql, Object... objects) {
        List<T> beanList = dynamicQueryBeanForList(sql, objects);
        if (beanList.size() > 1) {
            throw new RuntimeException("查询结果不唯一，请检查SQL的条件是否正确！");
        } else if (beanList.size() == 0) {
            return null;
        }
        return (X) beanList.get(0);
    }

    */
/**
     * @see com.innofi.framework.service.IBaseService#queryBeanForUnique(String, String, List)
     *//*

    public <X> X dynamicQueryBeanForUnique(String moduleId, String sqlId, List<SQLParam> params) {
        List<T> beanList = dynamicQueryBeanForList(moduleId, sqlId, params);
        if (beanList.size() > 1) {
            throw new RuntimeException("查询结果不唯一，请检查SQL的条件是否正确！");
        } else if (beanList.size() == 0) {
            return null;
        }
        return (X) beanList.get(0);
    }

    */
/**
     * @see com.innofi.framework.service.IBaseService#queryBeanForList(String, String, List)
     *//*

    public List<T> dynamicQueryBeanForList(String moduleId, String sqlId, List<SQLParam> params) {
        Page page = new Page(Page.BIG_PAGE_SIZE, 1);
        dynamicQueryBeanForPage(moduleId, sqlId, params, page);
        return page.getEntities();
    }

    */
/**
     * @see com.innofi.framework.service.IBaseService#queryBeanForPage(String, Page, Object...)
    }
    DBDialect dbDialect = getDaoSupport().getJdbcDao().getDBDialect();
        sql = assemblySqlRestrictions(sql);//增加校验策略
        String countSql = SqlUtil.getCountSql(sql);
        int totalCount = getDaoSupport().getJdbcDao().queryForInt(countSql);
        String pageSql = dbDialect.getLimitSql(sql, page);
        List<Map<String, Object>> result = getDaoSupport().getJdbcDao().query(pageSql, new ColumnMapRowMapper());
        page.setEntities(result);
        page.setEntityCount(totalCount);
    }

    */
/**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.IJdbcDao#queryMapForList(String, String, java.util.List, com.innofi.framework.dao.pagination.Page)
     *//*

    public void dynamicQueryMapForList(String moduleId, String sqlId, List<SQLParam> params, Page<Map<String, Object>> page) {
        String sql = getDaoSupport().getJdbcDao().getFormatHql(moduleId, sqlId, params);
        sql = assemblySqlRestrictions(sql);//增加校验策略
        DBDialect dbDialect = getDaoSupport().getJdbcDao().getDBDialect();
        String countSql = SqlUtil.getCountSql(sql);
        int totalCount = getDaoSupport().getJdbcDao().queryForInt(countSql);
        String pageSql = dbDialect.getLimitSql(sql, page);
        List<Map<String, Object>> result = getDaoSupport().getJdbcDao().query(pageSql, new ColumnMapRowMapper());
        page.setEntities(result);
        page.setEntityCount(totalCount);
    }

    */
/**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.IJdbcDao#queryForList(String)
     *//*

    public List<Map<String, Object>> queryForList(String sql) {
        sql = assemblySqlRestrictions(sql);//增加校验策略
        return getDaoSupport().getJdbcDao().queryForList(sql);
    }

    */
/**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.IJdbcDao#queryForList(String, Object...)
     *//*

    public List<Map<String, Object>> queryForList(String sql, Object... args) {
        sql = assemblySqlRestrictions(sql);//增加校验策略
        return getDaoSupport().getJdbcDao().queryForList(sql, args);
    }

    */
/**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.jdbc.IJdbcDao#queryForList
     *//*

    public List<Map<String, Object>> querForList(String sql, Map<String, ?> paramMap) {
        sql = assemblySqlRestrictions(sql);//增加校验策略
        return getDaoSupport().getJdbcDao().queryForList(sql, paramMap);
    }
//////////////////////////////////////////sql end/////////////////////////////////////////////////////////
/////////////////////////////////////////Criteria start///////////////////////////////////////////////////

    */
/**
     * @see com.innofi.framework.service.IBaseService#findByProperty(String, Object, String)
     *//*

    public List<T> findByProperty(final String propertyName, final Object value, final String matchType) {
        if (Validator.isNull(matchType)) {
            return findByProperty(entityType, propertyName, value, QueryConstants.EQUAL);
        }
        return findByProperty(entityType, propertyName, value, matchType);
    }

    */
/**
     * @see com.innofi.framework.dao.hibernate.IHibernateDao#findByProperty(Class, String, Object, String) {
     *//*

    public List<T> findByProperty(Class<T> clazz, final String propertyName, final Object value, final String matchType) {
        Criterion criterion = buildPropertyFilterCriterion(propertyName, value, matchType);
        return findByCriterions(clazz, null, criterion);
    }


    */
/**
     * @see com.innofi.framework.service.IBaseService#findByProperty(String, Object, String, List)
     *//*

    public List<T> findByProperty(final String propertyName, final Object value, final String matchType, List<PropertyOrder> orders) {
        if (Validator.isNull(matchType)) {
            return findByProperty(entityType, propertyName, value, QueryConstants.EQUAL, orders);
        }
        return findByProperty(entityType, propertyName, value, matchType, orders);
    }

    */
/**
     * @see com.innofi.framework.dao.hibernate.IHibernateDao#findByProperty(Class, String, Object, String, List) {
     *//*

    public List<T> findByProperty(Class<T> clazz, String propertyName, Object value, String matchType, List<PropertyOrder> orders) {
        Criterion criterion = buildPropertyFilterCriterion(propertyName, value, matchType);
        return findByCriterions(clazz, orders, criterion);
    }


    */
/**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.hibernate.IHibernateDao#findByCriterions
     *//*

    public List<T> findByCriterions(Class<T> clazz, List<PropertyOrder> orders, Criterion... criterions) {
        Page page = new Page(Page.BIG_PAGE_SIZE, 1);
        findPage_Criterion(clazz, orders, page, null, criterions);
        return page.getEntities();
    }


    */
/**
     * @see com.innofi.framework.service.IBaseService#findUniqueByProperty(String, Object, String)
     *//*

    public T findUniqueByProperty(final String propertyName, final Object value, final String matchType) {
        List<T> beanList = findByProperty(propertyName, value, matchType);
        if (beanList.size() > 1) {
            throw new RuntimeException("查询结果不唯一，请检查条件是否正确！");
        } else if (beanList.size() == 0) {
            return null;
        }
        return beanList.get(0);
    }


    public List<T> findByIds(List<PK> ids, String operType) {
        return findByIds(entityType, ids);
    }


    */
/**
     * @see com.innofi.framework.service.IBaseService#findByIds(List<PK>)
     *//*

    public List<T> findByIds(List<PK> ids) {
        return findByIds(entityType, ids);
    }

    */
/**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.hibernate.IHibernateDao#findByIds(Class, List<PK>)
     *//*

    public List<T> findByIds(Class<T> clazz, List<PK> ids) {
        Assert.notNull(clazz, "clazz类型不能为空");
        Assert.notEmpty(ids, "id列表不能为空");
        return findByCriterions(clazz, null, Restrictions.in(getIdPropertyName(clazz), ids));
    }


    */
/**
     * @see com.innofi.framework.dao.hibernate.IHibernateDao#findPage_Criterion(Class, List, Page, Criterion...) {
     *//*

    @SuppressWarnings("unchecked")
    public void findPage_Criterion(Class<T> clazz, List<PropertyOrder> orders, final Page<T> page, List<PropertyFilter> filters, Criterion... criterions) {
        Assert.notNull(clazz, "clazz类型不能为空");
        Assert.notNull(page, "page不能为空");
        Criteria c = createCriteria(clazz, filters, null, criterions);

        if (page.isAutoCount()) {
            long totalCount = this.getDaoSupport().getHibernateDao().countCriteriaResult(c);
            page.setEntityCount((int) totalCount);
        }
        setPageParameter(c, orders, page);
        List result = c.list();
        page.setEntities(result);
    }

    */
/**
     * @see com.innofi.framework.service.IBaseService#findByPropertyFilters(List, List)
     *//*

    public List<T> findByPropertyFilters(List<PropertyFilter> filters, List<PropertyOrder> orders) {
        Page page = new Page(Page.BIG_PAGE_SIZE, 1);
        findByPage_Filters(filters, orders, page);
        return page.getEntities();
    }

    */
/**
     * @see com.innofi.framework.service.IBaseService#findByPage_Filters(List, List, Page)
     *//*

    public void findByPage_Filters(List<PropertyFilter> filters, List<PropertyOrder> orders, Page page) {
        findByPage_Filters(entityType, filters, orders, page);
    }

    */
/**
     * @see com.innofi.framework.dao.hibernate.IHibernateDao#findByPage_Filters(Class, java.util.List, java.util.List, com.innofi.framework.dao.pagination.Page)
     *//*

    public void findByPage_Filters(Class<T> clazz, final List<PropertyFilter> filters, List<PropertyOrder> orders, final Page page) {
        findPage_Criterion(clazz, orders, page, filters);
    }

    */
/**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.hibernate.IHibernateDao#createCriteria(Class, Criterion...)
     *//*

    public Criteria createCriteria(Class<T> clazz, List<PropertyFilter> filters, String operType, Criterion... criterions) {
        Criteria criteria = createCriteria(clazz, operType);

        List<Criterion> orCriterions = new ArrayList();

        if (filters != null) {
            for (PropertyFilter filter : filters) {
                Criterion criterion = buildPropertyFilterCriterion(filter.getPropertyName(), filter.getPropertyValue(), filter.getMatchType());
                if (criterion != null) {
                    if (QueryConstants.RESTRICTION_TYPE_AND.equals(filter.getRestrictionType())) {
                        if (orCriterions.size() > 0) {
                            Disjunction disjunction = Restrictions.disjunction();
                            for (Criterion orCriterion : orCriterions) {
                                disjunction.add(orCriterion);
                            }
                            criteria.add(disjunction);
                            orCriterions.clear();
                        }
                        criteria.add(criterion);
                    } else {
                        orCriterions.add(criterion);
                    }
                }
            }
            if (orCriterions.size() > 0) {
                Disjunction disjunction = Restrictions.disjunction();
                for (Criterion orCriterion : orCriterions) {
                    disjunction.add(orCriterion);
                }
                criteria.add(disjunction);
                orCriterions.clear();
            }
        } else if (criterions != null) {
            for (Criterion c : criterions) {
                if (c != null) criteria.add(c);
            }
        }
        return criteria;
    }

    */
/**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.hibernate.IHibernateDao#createCriteria(Class, Criterion...)
     *//*

    public Criteria createCriteria(Class<T> clazz, String operType, Criterion... criterions) {
        Criteria criteria = createCriteria(clazz, operType);
        for (Criterion c : criterions) {
            criteria.add(c);
        }
        return criteria;
    }

    */
/**
     * 创建实体对象Criteria查询对象
     *
     * @return criteria对象实例
     *//*

    protected Criteria createCriteria(Class<T> clazz, String operType) {
        Criteria c = getDaoSupport().getHibernateDao().getSession().createCriteria(clazz);
        List<String> projectionFields = this.getProjectionFields();
        if (projectionFields.size() > 0) {
            ProjectionList projections = Projections.projectionList();
            for (String fieldName : projectionFields) {
                projections.add(Projections.property(fieldName).as(fieldName));
            }
            c.setProjection(projections);
            c.setResultTransformer(Transformers.aliasToBean(clazz));
        }
        c.setCacheable(getCacheAble(clazz));
        if (operType == null) operType = OPER_TYPE;//如果为空，那么添加默认
        c = assemblyCriteriaRestrictions(c, clazz, operType);//增加策略方法
        return c;
    }
/////////////////////////////////////////Criteria end///////////////////////////////////////////////////
/////////////////////////////////////////assemblySqlRestrictions////////////////////////////////////////


    */
/**
     * 获取权限限制条件
     *
     * @param sql 查询SQL
     * @return AND crt_org_code IN (SELECT org_code FROM sys_org_info WHERE org_code IN ('001','002','003') OR org_seq LIKE  '200.20010%' OR org_seq LIKE  '%200.20010')
     *//*

    protected String assemblySqlRestrictions(String sql) {
        if (null == ContextHolder.getContext()) {//针对应用启动初始化时，存在无法获取到用户登录信息的情况，判断上下文是否为空，如果为空则不作权限控制
            return sql;
        }
        try {
            CCJSqlParserManager sqlParserManager = new CCJSqlParserManager();//SQL解析器
            Select select = (Select) sqlParserManager.parse(new StringReader(sql));
            String assemblySql = "";
            if (isWithStatement(select)) {
                assemblySql = assemblyWithSqlRestrictions(select);
            } else if (isUnionStatement(select)) {
                assemblySql = assemblyUnionSqlRestrictions(select);
            } else {
                PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
                assemblySql = assemblyPlainSelectRestrictions(plainSelect);

            }
            //在SQL解析分析过程中会产生"AS","[","]",将这些信息替换为""。
            assemblySql = StringUtil.replace(assemblySql, " AS ", " ");
            assemblySql = StringUtil.replace(assemblySql, " as ", " ");
            assemblySql = StringUtil.replace(assemblySql, "[", "");
            assemblySql = StringUtil.replace(assemblySql, "]", "");
            ConsoleUtil.info("--------------------最终权限SQL:" + assemblySql.toString() + "");
            return assemblySql;
        } catch (JSQLParserException e) {
            e.printStackTrace();
            throw new BusinessRuntimeException("权限管理，SQL解析异常！具体信息【" + e.getMessage() + "】");
        }
    }

    */
/**
     * 处理Union SQL
     *
     * @param select
     * @return 拼装好的SQL
     *//*

    private String assemblyUnionSqlRestrictions(Select select) throws JSQLParserException {
        StringBuffer assemblySql = new StringBuffer();
        Union union = (Union) select.getSelectBody();
        for (int i = 0; i < union.getPlainSelects().size(); i++) {
            PlainSelect plainSelect = (PlainSelect) union.getPlainSelects().get(i);
            String subSelectSql = assemblyPlainSelectRestrictions(plainSelect);
            if (i < union.getPlainSelects().size() - 1) {
                if (union.isAll()) {
                    assemblySql.append(subSelectSql + " UNION ALL");
                } else {
                    assemblySql.append(subSelectSql + " UNION ");
                }
            } else {
                assemblySql.append(subSelectSql);
            }

        }
        return assemblySql.toString();
    }

    */
/**
     * 处理With SQL
     *
     * @param select
     * @return 拼装好的SQL
     *//*

    private String assemblyWithSqlRestrictions(Select select) throws JSQLParserException {
        StringBuffer assemblySql = new StringBuffer();
        for (int i = 0; i < select.getWithItemsList().size(); i++) {
            String withSelectTrailing = " ) " + select.getSelectBody().toString();
            WithItem withItem = (WithItem) select.getWithItemsList().get(i);
            String withStatementLeading = " WITH " + withItem.toString().toUpperCase().substring(0, withItem.toString().indexOf("AS (") + 4);
            PlainSelect plainSelect = (PlainSelect) withItem.getSelectBody();
            String assemblyWithSubSelectSql = assemblyPlainSelectRestrictions(plainSelect);
            assemblySql.append(withStatementLeading + assemblyWithSubSelectSql + withSelectTrailing);
        }
        return assemblySql.toString();
    }


    */
/**
     * 处理简单SQL
     *
     * @param plainSelect
     * @return 拼装好的SQL
     *//*

    private String assemblyPlainSelectRestrictions(PlainSelect plainSelect) throws JSQLParserException {
        StringBuffer assemblySql = new StringBuffer();                                       //保存拼接后的权限控制SQL
        SysUser sysUser = ContextHolder.getContext().getLoginUser();                         //获取当前用户信息
        List<SysRole> roles = getUserDataRoles(sysUser);                                     //获取用户数据角色信息
        IDaTablePolicyService dataTablePolicyService = getSpringBean("daTablePolicyService");//注入策略服务
        IMdTableRelateService mdTableRelateService = getSpringBean("mdTableRelateService");  //注入元数据表表关系服务
        Map<String, List<MdTableRelate>> tableRelateMapping = new HashMap<String, List<MdTableRelate>>(); //建立主从表关联关系映射,key为主表名称，value为List,每个元素为MdTableRelate

        */
/*
         * 解析SQL找到SQL中包含的数据表名，根据表名查找用户拥有对应的数据访问策略进行权限控制，对于SQL中包含但用户没有访问策略的数据表则不进行权限控制。
         *//*

        Map<String, String> tableAliasMapping = analysisSqlFinderTableAndAlias(plainSelect); //表名及表别名映射，每组键值对包括 key 查询的表名 value 查询的表别名
        Set<String> tableNameSet = new HashSet<String>();
        for (String key : tableAliasMapping.keySet()) {
            tableNameSet.add(key.toLowerCase());
            tableNameSet.add(key.toUpperCase());
            List<MdTableRelate> relateTableList = mdTableRelateService.findMdTableRelateByMainTable(key.toUpperCase());
            if (null != relateTableList) {
                tableRelateMapping.put(key.toUpperCase(), relateTableList);
            }
        }

        List<DaTablePolicy> policies = dataTablePolicyService.findPoliciesByUserRolesOpenTypeAndTable(roles, OPER_TYPE, tableNameSet);//查询SQL中包含同时用户拥有的数据访问策略

        if (policies == null || policies.size() == 0) {//没有查找相应访问策略，则不作权限控制，将原有SQL保存至buffer中返回
            assemblySql.append(plainSelect.toString());
        } else {//找到相应策略，拼接权限控制SQL
            StringBuffer tableRestrictConditionBuffer = new StringBuffer();
            this.buildRestrictConditionBufferByPolices(tableRestrictConditionBuffer, policies); //根据数据策略构建约束条件字符串 格式:表名.字段名=约束条件ID AND 表名.字段名=约束条件ID OR 表名.字段名=约束条件ID
            String tableRestrictConditionStr = tableRestrictConditionBuffer.toString();

            if (Validator.isEmpty(tableRestrictConditionStr)) {//如果为空，说明没有进行数据策略配置，将原有SQL保存至buffer中返回
                assemblySql.append(plainSelect.toString());
            } else {
                ConsoleUtil.info("--------------------数据表访问控制约束条件串[" + tableRestrictConditionBuffer.toString());
                String policyIds = dataTablePolicyService.getPropertyValueString(policies);
                String queryDimenValueSql = policyDimenSql.replaceAll("#policyId#", policyIds).replaceAll("#opertype#", OPER_TYPE);
                */
/*
                 * 取维度控制信息
                 * table_name       策略对应的表名
                 * fieldName        维度字段名
                 * dimen_value      维度值
                 * dimen_auth_type  维度权限类型：例如机构维度，U：当前机构所有上级节点 D：当前机构所有下级节点 V：当前机构垂直所有节点 H: 当前机构水平所有节点 O: 其它实际值，
                 * dimen_type       维度类型：机构、时间、业务线、产品
                 * restrict_id      关联关系ID 作为关联公式的KEY
                 *//*

                List<Map<String, Object>> policyDimenValues = getDaoSupport().getJdbcDao().queryForList(queryDimenValueSql);

                */
/*
                * 1.根据表名获取数据表的数据全部控制维度
                * 2.获取维度值 解析维度值 维度值如果为根节点则不添加权限控制条件
                * 3.组装SQL
                *//*

                Map<String, StringBuffer> restrictFieldMap = new HashMap<String, StringBuffer>();
                Map<String, StringBuffer> restrictFieldMainDataMap = new HashMap<String, StringBuffer>();

                for (int i = 0; i < policyDimenValues.size(); i++) {
                    Map policyDimenValueMap = policyDimenValues.get(i);
                    String policyTableName = StringUtil.trimTrailing((String) policyDimenValueMap.get("P_TABLE_NAME"));
                    String dimenTableName = StringUtil.trimTrailing((String) policyDimenValueMap.get("TABLE_NAME"));
                    String fieldName = StringUtil.trimTrailing((String) policyDimenValueMap.get("FIELD_NAME"));
                    String dimenValue = StringUtil.trimTrailing((String) policyDimenValueMap.get("DIMEN_VALUE"));
                    String operator = StringUtil.trimTrailing((String) policyDimenValueMap.get("OPERATOR"));
                    String restrictId = StringUtil.trimTrailing((String) policyDimenValueMap.get("RESTRICT_ID"));
                    String dimenAuthType = StringUtil.trimTrailing((String) policyDimenValueMap.get("DIMEN_AUTH_TYPE"));
                    String dimenType = StringUtil.trimTrailing((String) policyDimenValueMap.get("DIMEN_TYPE"));


                    if (policyTableName.equals(dimenTableName)) {//策略表名与维度表名相同为主表
                        String alias = tableAliasMapping.get(dimenTableName.toUpperCase());
                        if (Validator.isNotEmpty(alias)) {
                            fieldName = alias + "." + fieldName;
                        }
                        if (!restrictFieldMap.containsKey(restrictId)) {//如果约束在Map中不存在，则添加到Map中，并创建一个保存过滤条件StringBuffer
                            restrictFieldMap.put(restrictId, new StringBuffer("1=2"));
                        }
                        StringBuffer filterBuffer = restrictFieldMap.get(restrictId);//获取过滤条件Buffer
                        assemblyFieldFilterCondition(sysUser, fieldName, dimenValue, dimenAuthType, dimenType, operator, filterBuffer, restrictId, false);//拼装字段过滤条件
                    } else {//策略表名与维度表名不相同为从表
                        List<MdTableRelate> relateList = tableRelateMapping.get(policyTableName);
                        String mainField = "";
                        String foreignField = "";
                        for (MdTableRelate mdRelate : relateList) {
                            if (mdRelate.getForeignTableName().equals(dimenTableName)) {
                                mainField = mdRelate.getMainField();
                                foreignField = mdRelate.getForeignField();
                                break;
                            }
                        }
                        String alias = tableAliasMapping.get(policyTableName.toLowerCase());
                        if (Validator.isNotEmpty(alias)) {
                            mainField = alias + "." + mainField;
                        }

                        StringBuffer mainfilterBuffer = new StringBuffer("");

                        if (dimenValue.equalsIgnoreCase("IS NULL")) {
                            mainfilterBuffer.append(mainField + " NOT IN (SELECT " + foreignField + " FROM " + dimenTableName + ")");
                        } else if (dimenValue.equalsIgnoreCase("IS NOT NULL")) {
                            mainfilterBuffer.append(mainField + " IN (SELECT " + foreignField + " FROM " + dimenTableName + ")");
                        } else {
                            mainfilterBuffer.append(mainField + " IN (SELECT " + foreignField + " FROM " + dimenTableName + " WHERE #data)");
                        }

                        if (!restrictFieldMap.containsKey(restrictId)) {//如果字段在Map中不存在，则添加到Map中，并创建一个保存过滤条件StringBuffer
                            restrictFieldMap.put(restrictId, mainfilterBuffer);
                        }

                        if (!dimenValue.equalsIgnoreCase("IS NULL") && !dimenValue.equalsIgnoreCase("IS NOT NULL")) {
                            if (!restrictFieldMainDataMap.containsKey(restrictId)) {//如果字段在Map中不存在，则添加到Map中，并创建一个保存过滤条件StringBuffer
                                restrictFieldMainDataMap.put(restrictId, new StringBuffer("1=2"));
                            }
                            StringBuffer filterBuffer = restrictFieldMainDataMap.get(restrictId);//获取过滤条件Buffer
                            assemblyFieldFilterCondition(sysUser, fieldName, dimenValue, dimenAuthType, dimenType, operator, filterBuffer, restrictId, false);//拼装字段过滤条件
                        }
                    }
                }
                StringBuffer policySqlBuffer = new StringBuffer();//连接多个字段过滤条件
                for (Map.Entry<String, StringBuffer> entry : restrictFieldMap.entrySet()) {
                    String restrictId = entry.getKey();
                    StringBuffer filterBuffer = entry.getValue();
                    tableRestrictConditionStr = tableRestrictConditionStr.replace(restrictId, filterBuffer);
                    if (restrictFieldMainDataMap.containsKey(restrictId)) {
                        tableRestrictConditionStr = tableRestrictConditionStr.replace("#data", restrictFieldMainDataMap.get(restrictId));
                    }
                }

                policySqlBuffer.append(tableRestrictConditionStr);

                restoreInputQuery(plainSelect, assemblySql, policySqlBuffer);
            }
        }
        return assemblySql.toString();
    }

/////////////////////////////////////////assemblyHqlRestrictions////////////////////////////////////////

    */
/**
     * 获取权限限制条件
     *
     * @param hql 执行查询的hql
     * @return AND objAlias.crtOrgCode IN (SELECT s.orgCode FROM sysOrgInfo s WHERE s.orgCode IN ('001','002','003') OR s.orgSeq LIKE  '200.20010%' OR s.orgSeq LIKE  '%200.20010')
     *//*

    protected String assemblyHqlRestrictions(String hql, Map<String, ?> values, Object[] objects) {
        if (null == ContextHolder.getContext()) {//针对应用启动初始化时，存在无法获取到用户登录信息的情况，判断上下文是否为空，如果为空则不作权限控制
            return hql;
        }
        StringBuffer assemblyHql = new StringBuffer();                                                    //保存拼接后的权限控制HQL
        Map<String, List<MdTableRelate>> tableRelateMapping = new HashMap<String, List<MdTableRelate>>(); //建立主从表关联关系映射,key为主表名称，value为List,每个元素为MdTableRelate
         */
/*
          * 获取当前用户及用户拥有的数据角色
          *//*

        SysUser sysUser = ContextHolder.getContext().getLoginUser();
        List<SysRole> roles = getUserDataRoles(sysUser);


        try {
            hql = StringUtil.trimTrailing(StringUtil.trimLeading(hql));
            if (StringUtil.trimTrailing(hql.toLowerCase()).indexOf("select") != 0) {//如果select不为起始位置，则加入select * 以便sql分析器正常解析。
                hql = "SELECT * " + hql;
            }

            //将hql中: ? 参数替换，否则无法进行HQL解析
            Map<String, String> colonParamMapping = new HashMap<String, String>();
            int colonParamIndex = 1;

            if (values != null) {
                for (Map.Entry<String, ?> entry : values.entrySet()) {
                    colonParamMapping.put(":" + entry.getKey(), "'$PARAM" + colonParamIndex + "'");
                    hql = hql.replace(":" + entry.getKey(), "'$PARAM" + (colonParamIndex++) + "'");
                }
            }

            Map<String, String> askParamMapping = new HashMap<String, String>();
            int askParamIndex = colonParamIndex;

            if (objects != null) {
                while (hql.indexOf("?") > -1) {
                    askParamMapping.put("?", "'$PARAM" + askParamIndex + "'");
                    hql = hql.replace("?", "'$PARAM" + (askParamIndex++) + "'");
                }
            }


            Map<String, String> chinessParamMapping = new HashMap<String, String>();

            String reg = "[^/u4e00-u9fa5'\"\\s,\\.\\w\\+\\-\\*\\/()'\\$!~@#%^&]+";

            Pattern p = Pattern.compile(reg); // 正则表达式
            Matcher m = p.matcher(hql); // 操作的字符串
            while (m.find()) {
                String uuid = java.util.UUID.randomUUID().toString();
                System.out.println("group=" + m.group());
                hql = hql.replace(m.group(), uuid);
                chinessParamMapping.put(m.group(), uuid);
            }

            CCJSqlParserManager sqlParserManager = new CCJSqlParserManager();//SQL解析器
            Select select = (Select) sqlParserManager.parse(new StringReader(hql));
            PlainSelect plainSelect = (PlainSelect) select.getSelectBody();


            */
/*
            * 解析SQL找到此次访问的表并获取当前用户的数据访问策略
            *//*

            Map<String, String> entityNameAliasMapping = analysisSqlFinderTableAndAlias(plainSelect);//实体对象名及表别名映射，每组键值对包括 key 查询的表名 value 查询的表别名
            IDaTablePolicyService dataTablePolicyService = getSpringBean("daTablePolicyService");   //注入策略服务

            IMdTableRelateService mdTableRelateService = getSpringBean("mdTableRelateService");//注入关联关系服务

            Map<String, String> tableAliasMapping = new HashMap();
            Map<String, HashMap<String, PropertyMapping>> tablePropertyMapping = new HashMap();

            for (Map.Entry<String, String> entry : entityNameAliasMapping.entrySet()) {
                String entityName = entry.getKey();
                String aliasName = entry.getValue();
                PersistentClass persistentClass = entitySimpleNamePersistenceClassMapping.get(entityName);
                String tableName = persistentClass.getTable().getName().toUpperCase();
                HashMap<String, PropertyMapping> propertyMappings = getDaoSupport().getEntityHbmMapping(persistentClass);//取得表对应的属性名
                tableAliasMapping.put(tableName, aliasName);
                tablePropertyMapping.put(tableName, propertyMappings);
            }

            Set<String> tableNameSet = new HashSet<String>();
            for (String key : tableAliasMapping.keySet()) {
                tableNameSet.add(key.toLowerCase());
                tableNameSet.add(key.toUpperCase());
                List<MdTableRelate> relateTableList = mdTableRelateService.findMdTableRelateByMainTable(key.toUpperCase());
                if (null != relateTableList) {
                    tableRelateMapping.put(key.toUpperCase(), relateTableList);//建立主从表关联关系
                }
                //对从表作属性列处理。。
                for (MdTableRelate mdTable : relateTableList) {
                    String tableName = mdTable.getForeignTableName();
                    String entityName = tableNameEntityMapping.get(tableName);
                    PersistentClass persistentClass = entitySimpleNamePersistenceClassMapping.get(entityName);
                    HashMap<String, PropertyMapping> propertyMappings = getDaoSupport().getEntityHbmMapping(persistentClass);//取得表对应的属性名
                    tablePropertyMapping.put(tableName, propertyMappings);
                }
            }

            List<DaTablePolicy> policies = dataTablePolicyService.findPoliciesByUserRolesOpenTypeAndTable(roles, OPER_TYPE, tableNameSet);
            if (policies == null || policies.size() == 0) {//没有配置访问策略则不作权限控制，返回原有HQL
                //将HQL: ? 参数还原
                hql = restoreHqlFromSql(values, objects, colonParamMapping, askParamMapping, chinessParamMapping, hql);

                //还原HQL
                hql = StringUtils.replace(hql, "SELECT *", "");
                assemblyHql.append(hql);
            } else {
                StringBuffer tableRestrictConditionBuffer = new StringBuffer();
                this.buildRestrictConditionBufferByPolices(tableRestrictConditionBuffer, policies); //根据数据策略构建约束条件字符串 格式:表名.字段名=约束条件ID AND 表名.字段名=约束条件ID OR 表名.字段名=约束条件ID
                String tableRestrictConditionStr = tableRestrictConditionBuffer.toString();

                if (Validator.isEmpty(tableRestrictConditionStr)) {//如果为空，说明没有进行数据策略配置，将原有SQL保存至buffer中返回
                    //将HQL: ? 参数还原
                    hql = restoreHqlFromSql(values, objects, colonParamMapping, askParamMapping, chinessParamMapping, hql);

                    //还原HQL
                    hql = StringUtils.replace(hql, "SELECT *", "");
                    assemblyHql.append(hql);
                } else {
                    ConsoleUtil.info("--------------------数据表访问控制约束条件串[" + tableRestrictConditionBuffer.toString());
                    String policyIds = dataTablePolicyService.getPropertyValueString(policies);
                    String queryDimenValueSql = policyDimenSql.replaceAll("#policyId#", policyIds).replaceAll("#opertype#", OPER_TYPE);
                    */
/*
                    * 取维度控制信息
                    * table_name 控制表名
                    * fieldName 维度字段名
                    * dimen_value 维度值
                    * dimen_auth_type 维度权限类型：例如机构维度，U：当前机构所有上级节点 D：当前机构所有下级节点 V：当前机构垂直所有节点 H: 当前机构水平所有节点 O: 其它实际值，
                    * dimen_type 维度类型：机构、时间、业务线、产品
                    *//*

                    List<Map<String, Object>> policyDimenValues = getDaoSupport().getJdbcDao().queryForList(queryDimenValueSql);

                    */
/*
                    * 1.根据表名获取数据表的数据全部控制维度
                    * 2.获取维度值 解析维度值 维度值如果为根节点则不添加权限控制条件
                    * 3.组装HQL
                    *//*

                    Map<String, StringBuffer> restrictFieldMap = new HashMap<String, StringBuffer>();
                    Map<String, StringBuffer> restrictFieldMainDataMap = new HashMap<String, StringBuffer>();

                    for (int i = 0; i < policyDimenValues.size(); i++) {
                        Map policyDimenValueMap = policyDimenValues.get(i);
                        String policyTable = StringUtil.trimTrailing((String) policyDimenValueMap.get("P_TABLE_NAME"));
                        String dimenTable = StringUtil.trimTrailing((String) policyDimenValueMap.get("TABLE_NAME"));
                        String fieldName = StringUtil.trimTrailing((String) policyDimenValueMap.get("FIELD_NAME"));
                        String dimenValue = StringUtil.trimTrailing((String) policyDimenValueMap.get("DIMEN_VALUE"));
                        String operator = StringUtil.trimTrailing((String) policyDimenValueMap.get("OPERATOR"));
                        String restrictId = StringUtil.trimTrailing((String) policyDimenValueMap.get("RESTRICT_ID"));
                        String dimenAuthType = StringUtil.trimTrailing((String) policyDimenValueMap.get("DIMEN_AUTH_TYPE"));
                        String dimenType = StringUtil.trimTrailing((String) policyDimenValueMap.get("DIMEN_TYPE"));

                        if (policyTable.equals(dimenTable)) {//主表策略
                            HashMap<String, PropertyMapping> propertyMappings = tablePropertyMapping.get(dimenTable.toUpperCase()); //根据表名取出属性列表
                            String property = propertyMappings.get(fieldName.toUpperCase()).getPropertyName();

                            String alias = tableAliasMapping.get(dimenTable.toUpperCase()); //转换字段变成属性
                            if (Validator.isNotEmpty(alias)) {
                                property = alias + "." + property;
                            }

                            if (!restrictFieldMap.containsKey(restrictId)) {//如果字段在Map中不存在，则添加到Map中，并创建一个保存过滤条件StringBuffer
                                restrictFieldMap.put(restrictId, new StringBuffer("1=2"));
                            }
                            StringBuffer filterBuffer = restrictFieldMap.get(restrictId);//获取过滤条件Buffer

                            assemblyFieldFilterCondition(sysUser, property, dimenValue, dimenAuthType, dimenType, operator, filterBuffer, restrictId, true);//拼装字段过滤条件
                        } else {
                            HashMap<String, PropertyMapping> mainPropertyMappings = tablePropertyMapping.get(policyTable.toUpperCase()); //根据表名取出属性列表
                            HashMap<String, PropertyMapping> foreignPropertyMappings = tablePropertyMapping.get(dimenTable.toUpperCase()); //根据表名取出从表属性列表
                            String property = foreignPropertyMappings.get(fieldName.toUpperCase()).getPropertyName();
                            List<MdTableRelate> listRelate = tableRelateMapping.get(policyTable);
                            String mainField = "";
                            String foreignField = "";
                            String mainFieldProperty = "";
                            String foreignFieldProperty = "";
                            String dimenEntiy = tableNameEntityMapping.get(dimenTable);
                            for (MdTableRelate mdRelate : listRelate) {
                                if (mdRelate.getForeignTableName().equals(dimenTable)) {
                                    mainField = mdRelate.getMainField();
                                    foreignField = mdRelate.getForeignField();
                                    mainFieldProperty = mainPropertyMappings.get(mainField.toUpperCase()).getPropertyName();
                                    foreignFieldProperty = foreignPropertyMappings.get(foreignField.toUpperCase()).getPropertyName();
                                    break;
                                }
                            }
                            String alias = tableAliasMapping.get(policyTable.toUpperCase());
                            if (Validator.isNotEmpty(alias)) {
                                mainFieldProperty = alias + "." + mainFieldProperty;
                            }

                            StringBuffer mainfilterBuffer = new StringBuffer("");

                            if (dimenValue.equalsIgnoreCase("IS NULL")) {
                                mainfilterBuffer.append(mainFieldProperty + " NOT IN (SELECT " + foreignFieldProperty + " FROM " + dimenEntiy + ")");
                            } else if (dimenValue.equalsIgnoreCase("IS NOT NULL")) {
                                mainfilterBuffer.append(mainFieldProperty + " IN (SELECT " + foreignFieldProperty + " FROM " + dimenEntiy + ")");
                            } else {
                                mainfilterBuffer.append(mainFieldProperty + " IN (SELECT " + foreignFieldProperty + " FROM " + dimenEntiy + " WHERE #data)");
                            }


                            if (!restrictFieldMap.containsKey(restrictId)) {//如果字段在Map中不存在，则添加到Map中，并创建一个保存过滤条件StringBuffer
                                restrictFieldMap.put(restrictId, mainfilterBuffer);
                            }

                            if (!dimenValue.equalsIgnoreCase("IS NULL") && !dimenValue.equalsIgnoreCase("IS NOT NULL")) {
                                if (!restrictFieldMainDataMap.containsKey(restrictId)) {//如果字段在Map中不存在，则添加到Map中，并创建一个保存过滤条件StringBuffer
                                    restrictFieldMainDataMap.put(restrictId, new StringBuffer("1=2"));
                                }
                                StringBuffer filterBuffer = restrictFieldMainDataMap.get(restrictId);//获取过滤条件Buffer

                                assemblyFieldFilterCondition(sysUser, property, dimenValue, dimenAuthType, dimenType, operator, filterBuffer, restrictId, true);//拼装字段过滤条件
                            }
                        }
                    }

                    for (Map.Entry<String, StringBuffer> entry : restrictFieldMap.entrySet()) {
                        String restrictId = entry.getKey();
                        tableRestrictConditionStr = tableRestrictConditionStr.replace(restrictId, entry.getValue());
                        if (restrictFieldMainDataMap.containsKey(restrictId)) {
                            tableRestrictConditionStr = tableRestrictConditionStr.replace("#data", restrictFieldMainDataMap.get(restrictId));
                        }
                    }

                    StringBuffer policyHqlBuffer = new StringBuffer();//拼接条件的HQL BUFFER
                    policyHqlBuffer.append(tableRestrictConditionStr);
                    restoreInputQuery(plainSelect, assemblyHql, policyHqlBuffer);
                    //去掉所有的AS[ ],有可能是分析器里带出来的
                    String assemblyHqlStr = assemblyHql.toString();
                    assemblyHqlStr = StringUtil.replace(assemblyHqlStr, "AS", "");
                    assemblyHqlStr = StringUtil.replace(assemblyHqlStr, "as", "");
                    assemblyHqlStr = StringUtil.replace(assemblyHqlStr, "[", "");
                    assemblyHqlStr = StringUtil.replace(assemblyHqlStr, "]", "");
                    assemblyHqlStr = restoreHqlFromSql(values, objects, colonParamMapping, askParamMapping, chinessParamMapping, assemblyHqlStr);
                    assemblyHqlStr = StringUtils.replace(assemblyHqlStr, "SELECT *", "");
                    assemblyHql.replace(0, assemblyHql.length(), assemblyHqlStr);
                }
            }
        } catch (JSQLParserException e) {
            e.printStackTrace();
            throw new BusinessRuntimeException("权限管理，HQL解析异常！具体信息【" + e.getMessage() + "】");
        }

        ConsoleUtil.info("--------------------最终权限HQL:" + assemblyHql.toString() + "");
        return assemblyHql.toString();
    }

/////////////////////////////////////assemblyCriteriaRestrictions////////////////////////////////////////

    */
/**
     * 获取权限限制条件
     *
     * @param ca    Hibernate Criteria接口
     * @param clazz 类对象
     * @return 添加限定条件后的 Criteria接口
     *//*


    protected Criteria assemblyCriteriaRestrictions(Criteria ca, Class<T> clazz, String operType) {
        if (null == ContextHolder.getContext()) {//针对应用启动初始化时，存在无法获取到用户登录信息的情况，判断上下文是否为空，如果为空则不作权限控制
            return ca;
        }
        Map<String, List<MdTableRelate>> tableRelateMapping = new HashMap<String, List<MdTableRelate>>(); //建立主从表关联关系映射,key为主表名称，value为List,每个元素为MdTableRelate
        */
/*
         * 获取当前用户及用户拥有的数据角色
         *//*

        SysUser sysUser = ContextHolder.getContext().getLoginUser();
        List<SysRole> roles = getUserDataRoles(sysUser);

        */
/**
         * 2.解析 Criteria
         *//*

        String table = getDaoSupport().getPersistenceClass(clazz).getTable().getName();
        Set<String> tables = new HashSet<String>(1);
        tables.add(table.toUpperCase());
        tables.add(table.toLowerCase());
        IDaTablePolicyService dataTablePolicyService = getSpringBean("daTablePolicyService");//注入策略服务
        IMdTableRelateService mdTableRelateService = getSpringBean("mdTableRelateService");//注入关联关系服务
        List<MdTableRelate> relateTableList = mdTableRelateService.findMdTableRelateByMainTable(table.toUpperCase());
        if (null != relateTableList) {
            tableRelateMapping.put(table.toUpperCase(), relateTableList);//建立主从表关联关系
        }

        List<DaTablePolicy> policies = dataTablePolicyService.findPoliciesByUserRolesOpenTypeAndTable(roles, operType, tables);
        if (policies == null || policies.size() == 0) {//没有配置访问策略则不作权限控制，返回原有SQL
            ConsoleUtil.info("--------------------未找到数据表访问控制策略--------------------");
        } else {
            StringBuffer tableRestrictConditionBuffer = new StringBuffer();
            this.buildRestrictConditionBufferByPolices(tableRestrictConditionBuffer, policies); //根据数据策略构建约束条件字符串 格式:表名.字段名=约束条件ID AND 表名.字段名=约束条件ID OR 表名.字段名=约束条件ID
            String tableRestrictConditionStr = tableRestrictConditionBuffer.toString();
            if (Validator.isEmpty(tableRestrictConditionStr)) {//如果为空，说明没有进行数据策略配置，将原有SQL保存至buffer中返回
                ConsoleUtil.info("--------------------数据表访问控制约束条件串为空--------------------");
            } else {
                ConsoleUtil.info("--------------------数据表访问控制约束条件串[" + tableRestrictConditionBuffer.toString());
                String policyIds = dataTablePolicyService.getPropertyValueString(policies);
                String queryDimenValueSql = policyDimenSql.replaceAll("#policyId#", policyIds).replaceAll("#opertype#", OPER_TYPE);
                */
/*
                 * 取维度控制信息
                 * table_name 控制表名
                 * fieldName 维度字段名
                 * dimen_value 维度值
                 * dimen_auth_type 维度权限类型：例如机构维度，U：当前机构所有上级节点 D：当前机构所有下级节点 V：当前机构垂直所有节点 H: 当前机构水平所有节点 O: 其它实际值，
                 * dimen_type 维度类型：机构、时间、业务线、产品
                 *//*

                List<Map<String, Object>> policyDimenValues = getDaoSupport().getJdbcDao().queryForList(queryDimenValueSql);
                */
/*
                 * 1.根据表名获取数据表的数据全部控制维度
                 * 2.获取维度值 解析维度值 维度值如果为根节点则不添加权限控制条件
                 * 3.组装SQL
                 *//*

                Map<String, StringBuffer> restrictFieldMap = new HashMap<String, StringBuffer>();
                Map<String, StringBuffer> restrictFieldMainDataMap = new HashMap<String, StringBuffer>();

                for (int i = 0; i < policyDimenValues.size(); i++) {
                    Map policyDimenValueMap = policyDimenValues.get(i);
                    String policyTableName = StringUtil.trimTrailing((String) policyDimenValueMap.get("P_TABLE_NAME"));
                    String dimenTableName = StringUtil.trimTrailing((String) policyDimenValueMap.get("TABLE_NAME"));
                    String fieldName = StringUtil.trimTrailing((String) policyDimenValueMap.get("FIELD_NAME"));
                    String dimenValue = StringUtil.trimTrailing((String) policyDimenValueMap.get("DIMEN_VALUE"));
                    String restrictId = StringUtil.trimTrailing((String) policyDimenValueMap.get("RESTRICT_ID"));
                    String operator = StringUtil.trimTrailing((String) policyDimenValueMap.get("OPERATOR"));
                    String dimenAuthType = StringUtil.trimTrailing((String) policyDimenValueMap.get("DIMEN_AUTH_TYPE"));
                    String dimenType = StringUtil.trimTrailing((String) policyDimenValueMap.get("DIMEN_TYPE"));

                    if (policyTableName.equals(dimenTableName)) {//主表策略
                        if (!restrictFieldMap.containsKey(restrictId)) {//如果字段在Map中不存在，则添加到Map中，并创建一个保存过滤条件StringBuffer
                            restrictFieldMap.put(restrictId, new StringBuffer("1=2"));
                        }
                        StringBuffer filterBuffer = restrictFieldMap.get(restrictId);//获取过滤条件Buffer
                        assemblyFieldFilterCondition(sysUser, fieldName, dimenValue, dimenAuthType, dimenType, operator, filterBuffer, restrictId, false);//拼装字段过滤条件
                    } else {//此时dimenTable并不存在SQL当中，而是作为主表的子表策略 policyTable为主表  dimenTableName 为子表
                        List<MdTableRelate> relateList = tableRelateMapping.get(policyTableName);
                        String mainField = "";
                        String foreignField = "";
                        for (MdTableRelate mdRelate : relateList) {
                            if (mdRelate.getForeignTableName().equals(dimenTableName)) {
                                mainField = mdRelate.getMainField();
                                foreignField = mdRelate.getForeignField();
                                break;
                            }
                        }

                        StringBuffer mainfilterBuffer = new StringBuffer("");

                        if (dimenValue.equalsIgnoreCase("IS NULL")) {
                            mainfilterBuffer.append(mainField + " NOT IN (select " + foreignField + " FROM " + dimenTableName + ")");
                        } else if (dimenValue.equalsIgnoreCase("IS NOT NULL")) {
                            mainfilterBuffer.append(mainField + " IN (SELECT " + foreignField + " FROM " + dimenTableName + ")");
                        } else {
                            mainfilterBuffer.append(mainField + " IN (SELECT " + foreignField + " FROM " + dimenTableName + " WHERE #data)");
                        }


                        if (!restrictFieldMap.containsKey(restrictId)) {//如果字段在Map中不存在，则添加到Map中，并创建一个保存过滤条件StringBuffer
                            restrictFieldMap.put(restrictId, mainfilterBuffer);
                        }


                        if (!dimenValue.equalsIgnoreCase("IS NULL") && !dimenValue.equalsIgnoreCase("IS NOT NULL")) {
                            if (!restrictFieldMainDataMap.containsKey(restrictId)) {//如果字段在Map中不存在，则添加到Map中，并创建一个保存过滤条件StringBuffer
                                restrictFieldMainDataMap.put(restrictId, new StringBuffer("1=2"));
                            }
                            StringBuffer filterBuffer = restrictFieldMainDataMap.get(restrictId);//获取过滤条件Buffer
                            assemblyFieldFilterCondition(sysUser, fieldName, dimenValue, dimenAuthType, dimenType, operator, filterBuffer, restrictId, false);//拼装字段过滤条件
                        }
                    }
                }
                //这里是拼接含有的主表的信息.
                StringBuffer policySqlBuffer = new StringBuffer("");//连接多个字段过滤条件
                for (Map.Entry<String, StringBuffer> entry : restrictFieldMap.entrySet()) {
                    String restrictId = entry.getKey();
                    StringBuffer filterBuffer = entry.getValue();

                    tableRestrictConditionStr = tableRestrictConditionStr.replace(restrictId, filterBuffer);
                    if (restrictFieldMainDataMap.containsKey(restrictId)) {
                        tableRestrictConditionStr = tableRestrictConditionStr.replace("#data", restrictFieldMainDataMap.get(restrictId));
                    }
                }

                policySqlBuffer.append(tableRestrictConditionStr);

                ConsoleUtil.info("--------------------拼装最终条件:" + policySqlBuffer.toString() + "");
                ca.add(Restrictions.sqlRestriction(policySqlBuffer.toString()));
            }
        }
        return ca;
    }
////////////////////////////////////////////////////////以下是三者通用方法//////////////////////////////////////////////////////////////////

    */
/**
     * 将加工过的SQL还原为HQL
     *
     * @param values
     * @param objects
     * @param colonParamMapping
     * @param askParamMapping
     * @param assemblyHqlStr
     * @return
     *//*

    private String restoreHqlFromSql(Map<String, ?> values, Object[] objects, Map<String, String> colonParamMapping, Map<String, String> askParamMapping, Map<String, String> chinessParamMapping, String assemblyHqlStr) {
        //将HQL: ? 参数还原
        if (values != null) {
            for (Map.Entry<String, String> entry : colonParamMapping.entrySet()) {
                assemblyHqlStr = assemblyHqlStr.replace(entry.getValue(), entry.getKey());
            }
        }

        if (objects != null) {
            for (Map.Entry<String, String> entry : askParamMapping.entrySet()) {
                assemblyHqlStr = assemblyHqlStr.replace(entry.getValue(), entry.getKey());
            }
        }

        for (Map.Entry<String, String> entry : chinessParamMapping.entrySet()) {
            assemblyHqlStr = assemblyHqlStr.replace(entry.getValue(), entry.getKey());
        }


        return assemblyHqlStr;
    }

    */
/**
     * 将输入的查询还原
     *
     * @param plainSelect
     * @param assemblySql
     * @param policySqlBuffer
     *//*

    private void restoreInputQuery(PlainSelect plainSelect, StringBuffer assemblySql, StringBuffer policySqlBuffer) {
        assemblySql.append("SELECT " + plainSelect.getSelectItems().toString() + " FROM " + plainSelect.getFromItem().toString());

        if (null != plainSelect.getJoins()) {
            assemblySql.append("," + plainSelect.getJoins().toString() + " ");
        }

        if (null != plainSelect.getWhere()) {
            assemblySql.append(" WHERE " + plainSelect.getWhere());
            assemblySql.append(" AND " + policySqlBuffer);
        } else {
            assemblySql.append(" WHERE ");
            assemblySql.append(policySqlBuffer);
        }

        if (null != plainSelect.getGroupByColumnReferences()) {
            assemblySql.append(" GROUP BY " + plainSelect.getGroupByColumnReferences().toString());
        }

        if (null != plainSelect.getHaving()) {
            assemblySql.append(" HAVING " + plainSelect.getHaving().toString());
        }

        if (null != plainSelect.getOrderByElements()) {
            assemblySql.append(" ORDER BY " + plainSelect.getOrderByElements().toString());
        }
    }

    */
/**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.service.IBaseService#checkUpdDelPermission(String, List, String)
     *//*

    public List<String> checkUpdDelPermission(String entityClass,
                                              List idValueList, String operationType) {
        List returnList = new ArrayList();
        try {
            Class clazz = Class.forName(entityClass);
            */
/**
             * 1.获取当前角色所有策略。
             *//*

            */
/**
             * 获取当前用户的数据角色
             *//*

            SysUser sysUser = ContextHolder.getContext().getLoginUser();
            List<SysRole> roles = getUserDataRoles(sysUser);

            */
/**
             * 2.解析 Criteria
             *//*

            IDaTablePolicyService dataTablePolicyService = getSpringBean("daTablePolicyService");// 注入策略服务
            String table = getDaoSupport().getHibernateDao().getPersistenceClass(clazz).getTable().getName();
            Set<String> tables = new HashSet<String>(1);
            tables.add(table.toLowerCase());
            tables.add(table.toUpperCase());
            List<DaTablePolicy> policys = dataTablePolicyService.findPoliciesByUserRolesAndTable(roles, tables);

            if (policys == null || policys.size() == 0) {
                return returnList;
            }

            String operType = "";
            */
/**
             * 3.从策略中匹配对应的TABLES
             *//*

            returnList.addAll(idValueList);//默认传递进来需要验证的IDVALUE都是不通过的
            Set rightListids = new HashSet();//获取具备权限的值
            for (int j = 0; j < policys.size(); j++) {
                DaTablePolicy policy = policys.get(j);
                if (FrameworkConstants.DA_TYPE_MODIFY.equals(operationType)) {
                    operType = "'" + FrameworkConstants.DA_TYPE_MODIFY
                            + "','" + FrameworkConstants.DA_TYPE_DELETE
                            + "'";
                } else if (FrameworkConstants.DA_TYPE_DELETE
                        .equals(operationType)) {
                    operType = "'" + FrameworkConstants.DA_TYPE_DELETE
                            + "'";
                } else {
                    operType = "'error'";// 没有建立此类型的策略
                }
                if (operType.indexOf(policy.getOperType()) != -1) {//存在策略处理
                    Criteria c = createCriteria(entityType, null, operType, Restrictions.in(getIdPropertyName(), idValueList));
                    List list = c.list();
                    for (int i = 0; i < list.size(); i++) {
                        BasePojo basePojo = (BasePojo) list.get(i);
                        rightListids.add(basePojo.getId());
                    }
                } else {

                }
            }
            returnList.removeAll(rightListids);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return returnList;
    }

    */
/*
     * 获取当前用户所有数据角色
     *//*

    private List<SysRole> getUserDataRoles(SysUser sysUser) {
        List<SysRole> dataRoles = new ArrayList<SysRole>();
        List<SysRole> roles = sysUser.getDataRoles();
        for (SysRole sysRole : roles) {
            if (RBACConstants.ROLE_TYPE_DATA.equals(sysRole.getRoleType())) {
                dataRoles.add(sysRole);
            }
        }
        return dataRoles;
    }

    */
/*
     * 拼接简单SQL数据权限过滤条件,时间类型、树形类型维度
     *
     * @param sysUser       当前登录用户
     * @param fieldName     字段名称
     * @param dimenValue    维度值
     * @param dimenAuthType 维度权限控制类型
     * @param dimenType     维度类型
     * @param filterBuffer  保存字段过滤值StringBuffer
     * @param isHql         是否HQL
     *//*

    private void assemblyFieldFilterCondition(SysUser sysUser, String fieldName, String dimenValue, String dimenAuthType, String dimenType, String operator, StringBuffer filterBuffer, String restrictId, boolean isHql) {
        filterBuffer.append(" OR ");
        if (Validator.isEmpty(operator)) {
            Map<String, String> dimenTypeMap = new HashMap<String, String>(); //保存维度值查询参数  key table 维度表名,id ID字段 ,parentId 父节点ID字段
            if (dimenValue.indexOf("NULL") > -1) {
                filterBuffer.append(fieldName + " " + dimenValue);
            } else if ("1".equals(dimenType)) { //机构维度
                dimenTypeMap.put("table", "SYS_ORG_INFO");//这里使用视图，全扫描所有的机构以及部门节点。
                dimenTypeMap.put("id", "ORG_CODE");
                dimenTypeMap.put("parentId", "PARENT_ORG_CODE");
                if ("${userOrgCode}".equals(dimenValue)) {
                    dimenValue = sysUser.getOwerOrgCode();//当前用户所属机构
                } else if ("${userDeptCode}".equals(dimenValue)) {
                    dimenValue = sysUser.getSysOrgInfo().getOrgCode();//当前用户所属部门
                }
                if (dimenValue.equals(ContextHolder.getSystemProperties().getString("mdf.cbRoot")) && dimenAuthType.equals(RBACConstants.DOWN)) { //如果为总行并且权限类型为本下级，添加1=1条件即满足条件
                    filterBuffer.append(" (1=1) ");
                } else {
                    if (isHql) {
                        StringBuffer fieldSqlBuffer = new StringBuffer("SELECT DISTINCT ORG_CODE FROM SYS_ORG_INFO WHERE ");
                        assemblyFieldFilterValue(dimenTypeMap.get("id"), dimenAuthType, dimenValue, dimenTypeMap, fieldSqlBuffer);
                        StringBuffer fieldArgs = new StringBuffer("");
                        this.sqlToArgsBuffer(fieldName, filterBuffer, fieldArgs, fieldSqlBuffer);
                    } else {
                        assemblyFieldFilterValue(fieldName, dimenAuthType, dimenValue, dimenTypeMap, filterBuffer);
                    }
                }
            } else if ("2".equals(dimenType)) {//时间维度
                String[] date = StringUtils.split(dimenValue, "|");
                String dateMin = date[0];
                String dateMax = date[1] + " 23:59:59";
                filterBuffer.append(fieldName + " BETWEEN TO_DATE('" + dateMin + "', 'yyyy-MM-dd HH24:mi:ss') AND TO_DATE('" + dateMax + "', 'yyyy-MM-dd HH24:mi:ss')");
            } else if ("3".equals(dimenType)) {//业务条线
                if ("${userBusinessLineCode}".equals(dimenValue)) {
                    ISysObjBusinessService sysObjBusinessService = this.getSpringBean(ContextHolder.getSystemProperties().getString("mdf.bizline.service.bean.name"));
                    if (isHql) {//区分 HQL的拼接
                        filterBuffer.append(fieldName + " IN (SELECT id FROM SysBusinessLine WHERE id IN (" + sysObjBusinessService.getSysUserCurrentBusinessLine(isHql) + "))");
                    } else {
                        filterBuffer.append(fieldName + " IN (SELECT BUSILINE_ID FROM SYS_BUSINESS_LINE WHERE BUSILINE_ID IN (" + sysObjBusinessService.getSysUserCurrentBusinessLine(isHql) + "))");
                    }
                } else {
                    dimenTypeMap.put("table", "SYS_BUSINESS_LINE");
                    dimenTypeMap.put("id", "BUSILINE_ID");
                    dimenTypeMap.put("parentId", "PARENT_BUSILINE_ID");
                    if (isHql) {
                        StringBuffer fieldSqlBuffer = new StringBuffer("SELECT DISTINCT BUSILINE_ID FROM SYS_BUSINESS_LINE WHERE "); //取各维度的结果。
                        assemblyFieldFilterValue(dimenTypeMap.get("id"), dimenAuthType, dimenValue, dimenTypeMap, fieldSqlBuffer);
                        StringBuffer fieldArgs = new StringBuffer("");
                        this.sqlToArgsBuffer(fieldName, filterBuffer, fieldArgs, fieldSqlBuffer);
                    } else {
                        assemblyFieldFilterValue(fieldName, dimenAuthType, dimenValue, dimenTypeMap, filterBuffer);
                    }
                }
            } else if ("4".equals(dimenType)) {//产品线
                dimenTypeMap.put("table", "SYS_PRODUCT");
                dimenTypeMap.put("id", "PRODUCT_ID");
                dimenTypeMap.put("parentId", "PARENT_ID");
                if ("${userProductCode}".equals(dimenValue)) {
                    List<SysPost> listPost = sysUser.getPosts();
                    ISysOrgInfoService sysOrgInfoService = this.getSpringBean("sysOrgInfoService");

                    String updepts = "";
                    for (SysPost post : listPost) {
                        SysOrgInfo updept = sysOrgInfoService.findUpDeptInfo(post.getOrgCode());
                        if (Validator.isEmpty(updepts)) {
                            updepts = "'" + updept.getOrgCode() + "'";
                        } else {
                            updepts += ",'" + updept.getOrgCode() + "'";
                        }
                    }
                    if (Validator.isEmpty(updepts)) {
                        updepts = "'noexsit'";
                    }

                    if (isHql) {//区分 HQL的拼接
                        filterBuffer.append(fieldName + " IN (SELECT id FROM SysProduct WHERE busilineId IN (SELECT id FROM SysBusinessLine WHERE belongOrgCode IN (" + updepts + ")))");
                    } else {
                        filterBuffer.append(fieldName + " IN (SELECT PRODUCT_ID FROM SYS_PRODUCT WHERE BUSILINE_ID IN (SELECT BUSILINE_ID FROM SYS_BUSINESS_LINE WHERE BELONG_ORG_CODE IN (" + updepts + ")))");
                    }

                } else {
                    if (isHql) {
                        StringBuffer fieldSqlBuffer = new StringBuffer("SELECT DISTINCT PRODUCT_ID FROM SYS_PRODUCT WHERE "); //取各维度的结果。
                        assemblyFieldFilterValue(dimenTypeMap.get("id"), dimenAuthType, dimenValue, dimenTypeMap, fieldSqlBuffer);
                        StringBuffer fieldArgs = new StringBuffer("");
                        this.sqlToArgsBuffer(fieldName, filterBuffer, fieldArgs, fieldSqlBuffer);
                    } else {
                        assemblyFieldFilterValue(fieldName, dimenAuthType, dimenValue, dimenTypeMap, filterBuffer);
                    }
                }
            } else if ("8".equals(dimenType)) {//岗位维度固定
                if ("${userPostCode}".equals(dimenValue)) {
                    List<SysPost> listPost = sysUser.getPosts();
                    String curPosts = "";
                    for (SysPost post : listPost) {
                        if (Validator.isEmpty(curPosts)) {
                            curPosts = "'" + post.getId() + "'";
                        } else {
                            curPosts += ",'" + post.getId() + "'";
                        }
                    }
                    dimenValue = curPosts;
                }
                if (Validator.isEmpty(dimenValue)) {
                    dimenValue = "'noexsit'";
                }
                filterBuffer.append(fieldName + " IN (" + dimenValue + ")");  //TODO 实际传的维度值需要确认
            }
            String filterStr = filterBuffer.toString();
            filterStr = StringUtil.replace(filterStr, "1=2 OR", "");
            filterBuffer.replace(0, filterBuffer.length(), filterStr);
        } else {
            //TODO 根据权限。。。对于有操作符的 做另外的操作
        }
    }

    */
/*
     * 根据维度权限类型拼接维度字段过滤条件
     * @param fieldName      字段名称
     * @param dimenAuthType  维度权限控制类型
     * @param dimenValue     维度值
     * @param dimenTypeMap   维度值查询参数
     * @param filterBuffer   字段过滤条件存储Buffer
     *//*

    private void assemblyFieldFilterValue(String fieldName, String dimenAuthType, String dimenValue, Map dimenTypeMap, StringBuffer filterBuffer) {
        String dimenTable = (String) dimenTypeMap.get("table");
        String dimenId = (String) dimenTypeMap.get("id");
        String dimenParentId = (String) dimenTypeMap.get("parentId");

        IdfCodeTransfer idfcodeTransfer = this.getIdfCodeTransfer();

        SysOrgInfo sysOrgInfo = (SysOrgInfo) idfcodeTransfer.getCacheObject("sysOrgInfoService", dimenValue);
        ;

        if ("SYS_ORG_INFO".equals(dimenTable) && sysOrgInfo != null && sysOrgInfo.getCategory().equals(RBACConstants.ORG_CATEGORY_ORG)) { //机构的作特殊处理

            if (RBACConstants.UP.equals(dimenAuthType)) {
                filterBuffer.append(" " + fieldName + " IN ");
                filterBuffer.append(" (SELECT sysorg1.ORG_CODE FROM SYS_ORG_INFO sysorg1 START WITH sysorg1.ORG_CODE IN ( ");
                filterBuffer.append(" SELECT sysorg2.ORG_CODE FROM SYS_ORG_INFO sysorg2 WHERE sysorg2.CATEGORY = '" + RBACConstants.ORG_CATEGORY_DEPT + "' AND sysorg2.PARENT_ORG_CODE IN ( ");
                filterBuffer.append(" SELECT sysorg3.ORG_CODE FROM SYS_ORG_INFO sysorg3 START WITH sysorg3.ORG_CODE = '" + dimenValue + "' CONNECT BY PRIOR sysorg3.PARENT_ORG_CODE = sysorg3.ORG_CODE ))");
                filterBuffer.append(" CONNECT BY PRIOR sysorg1.ORG_CODE = sysorg1.PARENT_ORG_CODE ");
                filterBuffer.append(" UNION ");
                filterBuffer.append(" SELECT sysorg4.ORG_CODE FROM SYS_ORG_INFO sysorg4 WHERE sysorg4.CATEGORY = '" + RBACConstants.ORG_CATEGORY_ORG + "' START WITH sysorg4.ORG_CODE = '" + dimenValue + "' CONNECT BY PRIOR sysorg4.PARENT_ORG_CODE = sysorg4.ORG_CODE )");
            } else if (RBACConstants.DOWN.equals(dimenAuthType)) {
                filterBuffer.append(" " + fieldName + " IN ");
                filterBuffer.append(" (SELECT ORG_CODE ");
                filterBuffer.append(" FROM SYS_ORG_INFO ");
                filterBuffer.append(" START WITH ORG_CODE = '" + dimenValue + "'");
                filterBuffer.append(" CONNECT BY PRIOR ORG_CODE = PARENT_ORG_CODE )");
            } else if (RBACConstants.VERTICAL.equals(dimenAuthType)) {
                filterBuffer.append(" " + fieldName + " IN ");
                filterBuffer.append(" (SELECT sysorg1.ORG_CODE FROM SYS_ORG_INFO sysorg1 START WITH sysorg1.ORG_CODE IN ( ");
                filterBuffer.append(" SELECT sysorg2.ORG_CODE FROM SYS_ORG_INFO sysorg2 WHERE sysorg2.CATEGORY = '" + RBACConstants.ORG_CATEGORY_DEPT + "' AND sysorg2.PARENT_ORG_CODE IN ( ");
                filterBuffer.append(" SELECT sysorg3.ORG_CODE FROM SYS_ORG_INFO sysorg3 START WITH sysorg3.ORG_CODE = '" + dimenValue + "' CONNECT BY PRIOR sysorg3.PARENT_ORG_CODE = sysorg3.ORG_CODE ");
                filterBuffer.append(" UNION SELECT sysorg4.ORG_CODE FROM SYS_ORG_INFO sysorg4 WHERE sysorg4.CATEGORY = '" + RBACConstants.ORG_CATEGORY_ORG + "' START WITH sysorg4.ORG_CODE = '" + dimenValue + "' CONNECT BY PRIOR sysorg4.ORG_CODE = sysorg4.PARENT_ORG_CODE ))");
                filterBuffer.append(" CONNECT BY PRIOR sysorg1.ORG_CODE = sysorg1.PARENT_ORG_CODE");
                filterBuffer.append(" UNION ");
                filterBuffer.append(" SELECT sysorg3.ORG_CODE FROM SYS_ORG_INFO sysorg3 START WITH sysorg3.ORG_CODE = '" + dimenValue + "' CONNECT BY PRIOR sysorg3.PARENT_ORG_CODE = sysorg3.ORG_CODE ");
                filterBuffer.append(" UNION SELECT sysorg4.ORG_CODE FROM SYS_ORG_INFO sysorg4 WHERE sysorg4.CATEGORY = '" + RBACConstants.ORG_CATEGORY_ORG + "' START WITH sysorg4.ORG_CODE = '" + dimenValue + "' CONNECT BY PRIOR sysorg4.ORG_CODE = sysorg4.PARENT_ORG_CODE )");
            } else if (RBACConstants.HORIZONTAL.equals(dimenAuthType)) {
                filterBuffer.append(" " + fieldName + " IN ");
                filterBuffer.append(" (SELECT sysorg1.ORG_CODE FROM SYS_ORG_INFO sysorg1 START WITH sysorg1.ORG_CODE IN ( ");
                filterBuffer.append(" SELECT sysorg2.ORG_CODE FROM SYS_ORG_INFO sysorg2 WHERE sysorg2.CATEGORY = '" + RBACConstants.ORG_CATEGORY_DEPT + "' AND sysorg2.PARENT_ORG_CODE IN ( ");
                filterBuffer.append(" SELECT sysorg3.ORG_CODE FROM SYS_ORG_INFO sysorg3 WHERE sysorg3.ORG_LEVEL = (SELECT sysorg4.ORG_LEVEL FROM SYS_ORG_INFO sysorg4 WHERE sysorg4.ORG_CODE = '" + dimenValue + "') START WITH sysorg3.ORG_CODE ='" + dimenValue + "' CONNECT BY PRIOR sysorg3.PARENT_ORG_CODE = sysorg3.ORG_CODE))");
                filterBuffer.append(" CONNECT BY PRIOR sysorg1.ORG_CODE = sysorg1.PARENT_ORG_CODE ");
                filterBuffer.append(" UNION ");
                filterBuffer.append(" SELECT sysorg3.ORG_CODE FROM SYS_ORG_INFO sysorg3 WHERE sysorg3.ORG_LEVEL =( ");
                filterBuffer.append(" SELECT sysorg4.ORG_LEVEL FROM SYS_ORG_INFO sysorg4 WHERE sysorg4.ORG_CODE = '" + dimenValue + "' AND sysorg4.CATEGORY ='" + RBACConstants.ORG_CATEGORY_ORG + "')");
                filterBuffer.append(" AND sysorg3.CATEGORY = '" + RBACConstants.ORG_CATEGORY_ORG + "' ");
                filterBuffer.append(" UNION ");
                filterBuffer.append(" SELECT sysorg1.ORG_CODE FROM SYS_ORG_INFO sysorg1 START WITH sysorg1.ORG_CODE IN ( ");
                filterBuffer.append(" SELECT sysorg2.ORG_CODE FROM SYS_ORG_INFO sysorg2 WHERE sysorg2.CATEGORY = '" + RBACConstants.ORG_CATEGORY_DEPT + "' AND sysorg2.PARENT_ORG_CODE IN ( ");
                filterBuffer.append(" SELECT sysorg3.ORG_CODE FROM SYS_ORG_INFO sysorg3 WHERE sysorg3.ORG_LEVEL =( ");
                filterBuffer.append(" SELECT sysorg4.ORG_LEVEL FROM SYS_ORG_INFO sysorg4 WHERE sysorg4.ORG_CODE = '" + dimenValue + "' AND sysorg4.category ='" + RBACConstants.ORG_CATEGORY_ORG + "')");
                filterBuffer.append(" AND sysorg3.CATEGORY = '" + RBACConstants.ORG_CATEGORY_ORG + "'))");
                filterBuffer.append(" CONNECT BY PRIOR sysorg1.ORG_CODE = sysorg1.PARENT_ORG_CODE)");
            } else if (RBACConstants.ACTURE.equals(dimenAuthType)) {
                filterBuffer.append(" " + fieldName + " = ");
                filterBuffer.append("'" + dimenValue + "'");
            }
        } else {
            if (RBACConstants.UP.equals(dimenAuthType)) {
                filterBuffer.append(" " + fieldName + " IN ");
                filterBuffer.append(" (SELECT " + dimenId + " ");
                filterBuffer.append(" FROM " + dimenTable + " ");
                filterBuffer.append(" START WITH " + dimenId + " = '" + dimenValue + "'");
                filterBuffer.append(" CONNECT BY " + dimenId + " = PRIOR " + dimenParentId + ")");
            } else if (RBACConstants.DOWN.equals(dimenAuthType)) {
                filterBuffer.append(" " + fieldName + " IN ");
                filterBuffer.append(" (SELECT " + dimenId + " ");
                filterBuffer.append(" FROM " + dimenTable + " ");
                filterBuffer.append(" START WITH " + dimenId + " = '" + dimenValue + "'");
                filterBuffer.append(" CONNECT BY PRIOR " + dimenId + " = " + dimenParentId + ")");
            } else if (RBACConstants.VERTICAL.equals(dimenAuthType)) {
                filterBuffer.append(" " + fieldName + " IN ");
                filterBuffer.append(" (SELECT " + dimenId + " ");
                filterBuffer.append(" FROM " + dimenTable + " ");
                filterBuffer.append(" START WITH " + dimenId + " = '" + dimenValue + "'");
                filterBuffer.append(" CONNECT BY " + dimenId + " = PRIOR " + dimenParentId + ")");
                filterBuffer.append(" or ");
                filterBuffer.append(" " + fieldName + " IN ");
                filterBuffer.append(" (SELECT " + dimenId + " ");
                filterBuffer.append(" FROM " + dimenTable + " ");
                filterBuffer.append(" START WITH " + dimenId + " = '" + dimenValue + "'");
                filterBuffer.append(" CONNECT BY PRIOR " + dimenId + " = " + dimenParentId + ")");
            } else if (RBACConstants.HORIZONTAL.equals(dimenAuthType)) {
                filterBuffer.append(" " + fieldName + " IN ");
                filterBuffer.append(" (SELECT " + dimenId + " ");
                filterBuffer.append(" FROM " + dimenTable + " ");
                filterBuffer.append(" WHERE " + dimenParentId + " = ");
                filterBuffer.append(" (SELECT " + dimenParentId + " ");
                filterBuffer.append(" FROM " + dimenTable + " ");
                filterBuffer.append(" WHERE " + dimenId + " ='" + dimenValue + "' ))");
            } else if (RBACConstants.ACTURE.equals(dimenAuthType)) {
                filterBuffer.append(" " + fieldName + " = ");
                filterBuffer.append("'" + dimenValue + "'");
            }
        }
    }

    */
/*
     * 提取Select语句中的表名及表别名，支持SQL中包含各种JOIN
     *
     * @param plainSelect net.sf.jsqlparser.statement.select.PlainSelect 对象
     * @return HashMap key 表名 value 表别名
     *//*

    private HashMap<String, String> analysisSqlFinderTableAndAlias(PlainSelect plainSelect) throws JSQLParserException {
        HashMap<String, String> tableAliasMapping = new HashMap<String, String>();
        Table table = (Table) plainSelect.getFromItem();
        tableAliasMapping.put(table.getWholeTableName(), table.getAlias());//添加主表
        if (plainSelect.getJoins() != null) {
            for (int i = 0; i < plainSelect.getJoins().size(); i++) {//添加关联表
                Join join = (Join) plainSelect.getJoins().get(i);
                table = (Table) join.getRightItem();
                tableAliasMapping.put(table.getWholeTableName(), table.getAlias());//表名称,极其别名
            }
        }
        return tableAliasMapping;
    }

    */
/*
     * 是否为With语句
     *
     * @param select Select 对象
     * @return true 是 ， false 否
     *//*

    private boolean isWithStatement(Select select) throws JSQLParserException {
        return select.getWithItemsList() != null;
    }

    */
/*
     * 是否为Union语句
     *
     * @param select Select 对象
     * @return true 是 ， false 否
     *//*

    private boolean isUnionStatement(Select select) throws JSQLParserException {
        try {
            Union union = (Union) select.getSelectBody();
            return true;
        } catch (ClassCastException e) {
            return false;
        }
    }


    */
/**
     * 根据数据策略构建约束条件字符串 格式:表名.字段名=约束条件ID AND 表名.字段名=约束条件ID OR 表名.字段名=约束条件ID
     *
     * @param tableRestrictConditionBuffer 保存约束字符串的Buffer
     * @param policies                     数据策略集合
     *//*

    private void buildRestrictConditionBufferByPolices(StringBuffer tableRestrictConditionBuffer, List<DaTablePolicy> policies) {
        //key 数据表名称 value 约束条件的StringBuffer
        Map<String, StringBuffer> restrictMapping = new HashMap();
        for (DaTablePolicy policy : policies) {
            if (!restrictMapping.containsKey(policy.getTableName())) {
                restrictMapping.put(policy.getTableName(), new StringBuffer());
            }

            StringBuffer restrictTreeBufferTemp = restrictMapping.get(policy.getTableName());
            if (Validator.isNotEmpty(restrictTreeBufferTemp.toString())) {//同表之间用OR链接
                restrictTreeBufferTemp.append(" OR ");
            }

            */
/*
             * 根据策略中的约束构建约束buffer
             *//*


            IDaDimenDataRestrictService daDimenDataRestrictService = getSpringBean("daDimenDataRestrictService");
            String findRootRestrictHql = " from DaDimenDataRestrict where tableAuthId = ? AND parentRestrictId = ? order by restrictType";
            DaDimenDataRestrict rootRestrict = daDimenDataRestrictService.findUniqueByHql(findRootRestrictHql, policy.getId(), "0");

            */
/*
             * 取SQL WHERE条件控制
             *//*

            String queryPolicyWhereSql = policyWhereSql.replaceAll("#policyId#", "'" + policy.getId() + "'").replaceAll("#opertype#", OPER_TYPE);
            Map<String, Object> policyWhereValues = getDaoSupport().getJdbcDao().queryForMap(queryPolicyWhereSql);
            String dimenSqlWhere = (String) policyWhereValues.get("SQL_WHERE");

            String policyRestrict = "";
            */
/*
             *拼接约束条件和SQL WHERE
             *//*

            if (rootRestrict != null && Validator.isNotEmpty(dimenSqlWhere)) {//找到策略中的约束,拼接约束条件
                policyRestrict = recursionBuildRestrictConditionByRestrict(daDimenDataRestrictService, rootRestrict);
                restrictTreeBufferTemp.append("(");
                restrictTreeBufferTemp.append("(" + policyRestrict + ")");
                restrictTreeBufferTemp.append(" " + dimenSqlWhere + " ");
                restrictTreeBufferTemp.append(")");
            } else if (rootRestrict == null && Validator.isNotEmpty(dimenSqlWhere)) {
                restrictTreeBufferTemp.append("(");
                restrictTreeBufferTemp.append(dimenSqlWhere);
                restrictTreeBufferTemp.append(")");
            } else if (Validator.isEmpty(dimenSqlWhere) && rootRestrict != null) {
                policyRestrict = recursionBuildRestrictConditionByRestrict(daDimenDataRestrictService, rootRestrict);
                restrictTreeBufferTemp.append("(");
                restrictTreeBufferTemp.append(policyRestrict);
                restrictTreeBufferTemp.append(")");
            }
        }

        if (restrictMapping.size() > 1) {
            for (Map.Entry<String, StringBuffer> entry : restrictMapping.entrySet()) { //不同表之间的策略用AND链接
                if (Validator.isNotEmpty(entry.getValue().toString().trim())) {
                    if (Validator.isNotEmpty(tableRestrictConditionBuffer.toString())) {
                        tableRestrictConditionBuffer.append(" AND ");
                    }
                    tableRestrictConditionBuffer.append(entry.getValue());
                }
            }
        } else {
            tableRestrictConditionBuffer.append(restrictMapping.values().iterator().next());
        }
        if (tableRestrictConditionBuffer.length() > 0) {
            tableRestrictConditionBuffer.insert(0, "(");
            tableRestrictConditionBuffer.append(")");
        }

    }

    */
/**
     * 递归根据策略中的约束条件拼写SQL符号运算规则
     *//*

    private String recursionBuildRestrictConditionByRestrict(IDaDimenDataRestrictService daDimenDataRestrictService, DaDimenDataRestrict parentRestrict) {

        List<DaDimenDataRestrict> children = daDimenDataRestrictService.findDaDimenRestrictByPolicyAndParentId(parentRestrict.getTableAuthId(), parentRestrict.getId());
        if (children != null && children.size() > 0) {
            String restrictRelateStr = "";
            if ("1".equals(parentRestrict.getRestrictType())) {
                restrictRelateStr = " AND";
            } else if ("2".equals(parentRestrict.getRestrictType())) {
                restrictRelateStr = " OR";
            } else if ("3".equals(parentRestrict.getRestrictType())) {
                restrictRelateStr = "";
            }
            StringBuffer templateBuffer = new StringBuffer();

            List<DaDimenDataRestrict> joinNodes = new ArrayList<DaDimenDataRestrict>();
            List<DaDimenDataRestrict> conditionNodes = new ArrayList<DaDimenDataRestrict>();
            for (DaDimenDataRestrict child : children) {
                if ("1".equals(child.getRestrictType())) {
                    joinNodes.add(child);
                } else if ("2".equals(child.getRestrictType())) {
                    joinNodes.add(child);
                } else if ("3".equals(child.getRestrictType())) {
                    conditionNodes.add(child);
                }
            }

            if (conditionNodes.size() == 0) {
                if ("1".equals(parentRestrict.getRestrictType())) {
                    templateBuffer.append("(1=1");
                } else if ("2".equals(parentRestrict.getRestrictType())) {
                    templateBuffer.append("(1=2");
                }
            } else {
                templateBuffer.append("(");
                for (int i = 0; i < conditionNodes.size(); i++) {
                    DaDimenDataRestrict conditionNode = conditionNodes.get(i);
                    if (i == 0) {
                        templateBuffer.append(conditionNode.getId());
                    } else {
                        templateBuffer.append(restrictRelateStr + " " + conditionNode.getId() + " ");
                    }
                }
            }

            for (int i = 0; i < joinNodes.size(); i++) {
                if ("1".equals(parentRestrict.getRestrictType())) {
                    templateBuffer.append(" AND " + recursionBuildRestrictConditionByRestrict(daDimenDataRestrictService, joinNodes.get(i)));
                } else if ("2".equals(parentRestrict.getRestrictType())) {
                    templateBuffer.append(" OR " + recursionBuildRestrictConditionByRestrict(daDimenDataRestrictService, joinNodes.get(i)));
                }
            }
            templateBuffer.append(")");

            return templateBuffer.toString();
        }
        return parentRestrict.getId();
    }

    */
/**
     * 用于HQL不支持的递归SQL 来直接算出结果 IN
     *//*

    private void sqlToArgsBuffer(String fieldName, StringBuffer filterBuffer, StringBuffer fieldArgs, StringBuffer fieldSqlBuffer) {

        List<Map<String, Object>> fieldvalueList = getDaoSupport().getJdbcDao().queryForList(fieldSqlBuffer.toString());
        int index = 1;

        filterBuffer.append("(");
        for (Map fieldValue : fieldvalueList) {

            if (Validator.isEmpty(fieldArgs.toString())) {
                fieldArgs.append("'" + fieldValue.get(fieldValue.keySet().iterator().next()) + "'");
            } else {
                fieldArgs.append(",'" + fieldValue.get(fieldValue.keySet().iterator().next()) + "'");
            }

            if (index % 500 == 0) {
                if (index == 500) {
                    filterBuffer.append(" " + fieldName + " IN (" + fieldArgs + ")");
                } else {
                    filterBuffer.append(" OR " + fieldName + " IN (" + fieldArgs + ")");
                }
                fieldArgs.delete(0, fieldArgs.length());
            }

            index++;
        }

        if (fieldvalueList.size() < 500) {
            filterBuffer.append(" " + fieldName + " IN (" + fieldArgs + ")");
        }

        filterBuffer.append(")");

    }
*/

}
