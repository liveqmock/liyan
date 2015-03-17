package com.innofi.framework.dao.hibernate.impl;

import com.innofi.framework.dao.dynamicstatement.DynamicStatementContext;
import com.innofi.framework.dao.hibernate.*;
import com.innofi.framework.dao.jdbc.Database;
import com.innofi.framework.dao.pagination.Page;
import com.innofi.framework.exception.DAOException;
import com.innofi.framework.pojo.BasePojo;
import com.innofi.framework.utils.reflect.ReflectionUtil;
import com.innofi.framework.utils.sql.SqlUtil;
import com.innofi.framework.utils.string.StringUtil;
import com.innofi.framework.utils.validate.Validator;

import org.apache.commons.lang.StringUtils;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.*;
import org.hibernate.engine.jdbc.LobCreator;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.Component;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.transform.ResultTransformer;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.util.Assert;

import java.io.*;
import java.lang.InstantiationException;
import java.sql.Blob;
import java.sql.Clob;
import java.util.*;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司 all rights reserved.
 *
 * @param <T>  DAO操作的对象类型
 * @param <ID> 主键类型
 *             基于hibernate4 扩展Dorado中HibernateDo功能，Hibernate DAO泛型基类.
 *             <p/>
 *             扩展功能包括分页查询,按属性过滤条件列表查询. 可在Service层直接使用,也可以扩展泛型DAO子类使用,见两个构造函数的注释.
 * @author liumy2009@126.com
 * @version 2.0
 * @found date: 2010-11-23
 * @found time: 20:40:56
 */
public class HibernateDaoImpl<T, ID extends Serializable> extends HibernateDaoSupport implements IHibernateDao<T, ID> {

    private org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());

    protected Configuration configuration;
    protected String dbType;

    public HibernateDaoImpl() {

    }

    public HibernateDaoImpl(final SessionFactory sessionFactory, Configuration configuration) {
        super.setSessionFactory(sessionFactory);
        this.configuration = configuration;
        String dialect = (String) configuration.getProperties().get("hibernate.dialect");
        Assert.notNull(dialect,"hibernate.dialect不能为空!");
        if (dialect.toLowerCase().indexOf("db2") > -1) {
            dbType = Database.DB2.toString().toLowerCase();
        } else if (dialect.toLowerCase().indexOf("oracle") > -1) {
            dbType = Database.ORACLE.toString().toLowerCase();
        } else if (dialect.toLowerCase().indexOf("derby") > -1) {
            dbType = Database.DERBY.toString().toLowerCase();
        } else if (dialect.toLowerCase().indexOf("microsoft") > -1) {
            dbType = Database.SQL_SERVER.toString().toLowerCase();
        } else if (dialect.toLowerCase().indexOf("mysql") > -1) {
            dbType = Database.MYSQL.toString().toLowerCase();
        } else {
            dbType = Database.DEFAULT.toString().toLowerCase();
        }
    }


    @Override
    public Session getSession() {
        return getHibernateTemplate().getSessionFactory().getCurrentSession();
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.hibernate.IHibernateDao#getConfiguration()
     */
    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.hibernate.IHibernateDao#getAll(Class)
     */
    public List<T> getAll(Class<T> clazz) {
        Assert.notNull(clazz, "clazz类型不能为空");
        return findByCriterions(clazz, null);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.hibernate.IHibernateDao#findByIds(Class, List< ID >)
     */
    public List<T> findByIds(Class<T> clazz, List<ID> ids) {
        Assert.notNull(clazz, "clazz类型不能为空");
        Assert.notEmpty(ids, "id列表不能为空");
        return findByCriterions(clazz, null, Restrictions.in(getIdPropertyName(clazz), ids));
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.hibernate.IHibernateDao#find(Class, String, Object...)
     */
    public List<T> find(Class<T> clazz, String hql, Object... values) {
        return createQuery(clazz, hql, values).list();
    }


    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.hibernate.IHibernateDao#find(Class, String, Map<String, ?>)
     */
    public List<T> find(Class<T> clazz, final String hql, final Map<String, ?> values) {
        return createQuery(clazz, hql, values).list();
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.hibernate.IHibernateDao#findUnique(Class, String, Object...)
     */
    public <X> X findUnique(Class<T> clazz, final String hql, final Object... values) {
        return (X) createQuery(clazz, hql, values).uniqueResult();
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.hibernate.IHibernateDao#findUnique(Class, String, Map<String, ?>)
     */
    public <X> X findUnique(Class<T> clazz, final String hql, final Map<String, ?> values) {
        return (X) createQuery(clazz, hql, values).uniqueResult();
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.hibernate.IHibernateDao#batchExecute(String, Object...)
     */
    public int batchExecute(final String hql, final Object... values) {
        return createQuery(null, hql, values).executeUpdate();
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.hibernate.IHibernateDao#batchExecute(String, Map<String,?>)
     */
    public int batchExecute(final String hql, final Map<String, ?> values) {
        return createQuery(null, hql, values).executeUpdate();
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.hibernate.IHibernateDao#createQuery(Class, String, Object... values)
     */
    public Query createQuery(Class<T> clazz, final String queryString, final Object... values) {
        Assert.hasText(queryString, "queryString不能为空");
        Query query = createQuery(clazz, queryString);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter(i, values[i]);
            }
        }
        return query;
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.hibernate.IHibernateDao#createQuery(Class, String, Map<String, Object> )
     */
    public Query createQuery(Class<T> clazz, final String queryString, final Map<String, ?> values) {
        Assert.hasText(queryString, "queryString不能为空");
        Query query = createQuery(clazz, queryString);
        if (values != null) {
            for (Map.Entry<String, ?> parameter : values.entrySet()) {
                if (parameter.getValue() instanceof Collection) {
                    query.setParameterList(parameter.getKey(), (Collection) parameter.getValue());
                } else if (parameter.getValue() instanceof Object[]) {
                    query.setParameterList(parameter.getKey(), (Object[]) parameter.getValue());
                } else {
                    query.setParameter(parameter.getKey(), parameter.getValue());
                }
            }
        }
        return query;
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.hibernate.IHibernateDao#findByCriterions
     */
    public List<T> findByCriterions(Class<T> clazz, List<PropertyOrder> orders, Criterion... criterions) {
        Page page = new Page(Page.BIG_PAGE_SIZE, 1);
        findPage_Criterion(clazz, orders, page, criterions);
        return page.getEntities();
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.hibernate.IHibernateDao#findUnique(Class, Criterion...)
     */
    public T findUnique(Class<T> clazz, final Criterion... criterions) {
        return (T) createCriteria(clazz, criterions).uniqueResult();
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.hibernate.IHibernateDao#createCriteria(Class, Criterion...)
     */
    public Criteria createCriteria(Class<T> clazz, Criterion... criterions) {
        Criteria criteria = createCriteria(clazz);
        for (Criterion c : criterions) {
            criteria.add(c);
        }

        return criteria;
    }


    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.hibernate.IHibernateDao#getIdPropertyName(Class<T>)
     */
    public String getIdPropertyName(Class<T> clazz) {
        ClassMetadata meta = getSessionFactory().getClassMetadata(clazz);
        return meta.getIdentifierPropertyName();
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.hibernate.IHibernateDao#findByHql(Class, String hql, Object... values)
     */
    public List<T> findByHql(Class<T> clazz, final String hql, final Object... values) {
        Assert.hasText(hql);
        Query q = createQuery(clazz, hql, values);
        return q.list();
    }

    /**
     * (non-Javadoc)
     *
     * @see com.innofi.framework.dao.hibernate.IHibernateDao#findByNamedHql(Class, String hql, Map<String, Object>)
     */
    public List<T> findByNamedHql(Class<T> clazz, String hql, Map<String, ?> values) {
        Query q = createQuery(clazz, hql, values);
        return q.list();
    }


    /**
     * @see com.innofi.framework.dao.hibernate.IHibernateDao#findByHql_Page(Class, String, Page, Object...) {
     */
    @SuppressWarnings("unchecked")
    public void findByHql_Page(Class<T> clazz, final String hql, final Page page, final Object... values) {
        Assert.hasText(hql);
        Assert.notNull(page, "page不能为空");

        Query q = createQuery(clazz, hql, values);

        if (page.isAutoCount()) {
            long totalCount = countHqlResult(clazz, hql, values);
            page.setEntityCount((int) totalCount);
        }

        setPageParameter(q, page);
        List result = q.list();
        page.setEntities(result);
    }

    /**
     * @see com.innofi.framework.dao.hibernate.IHibernateDao#findByNamedHql_Page(Class, String, Page, Map<String,?>) {
     */
    @SuppressWarnings("unchecked")
    public void findByNamedHql_Page(Class<T> clazz, final String hql, final Page page, final Map<String, ?> values) {
        Assert.hasText(hql);
        Assert.notNull(page, "page不能为空");

        Query q = createQuery(clazz, hql, values);

        if (page.isAutoCount()) {
            long totalCount = countHqlResult(clazz, hql, values);
            page.setEntityCount((int) totalCount);
        }

        setPageParameter(q, page);

        List result = q.list();
        page.setEntities(result);
    }

    /**
     * @see com.innofi.framework.dao.hibernate.IHibernateDao#findPage_Criterion(Class, List, Page, Criterion...) {
     */
    @SuppressWarnings("unchecked")
    public void findPage_Criterion(Class<T> clazz, List<PropertyOrder> orders, final Page<T> page, final Criterion... criterions) {
        Assert.notNull(clazz, "clazz类型不能为空");
        Assert.notNull(page, "page不能为空");
        Criteria c = createCriteria(clazz, criterions);

        if (page.isAutoCount()) {
            long totalCount = countCriteriaResult(c);
            page.setEntityCount((int) totalCount);
        }

        setPageParameter(c, orders, page);
        List result = c.list();
        page.setEntities(result);
    }

    /**
     * @see com.innofi.framework.dao.hibernate.IHibernateDao#createBlobByFilePath(String)
     */
    @SuppressWarnings("unchecked")
    public Blob createBlobByFilePath(String filePath) {
        Blob file = null;
        try {
            FileInputStream stream = new FileInputStream(filePath);
            file = createBlobByInputStream(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * @see com.innofi.framework.dao.hibernate.IHibernateDao#createBlobByFilePath(String)
     */
    @SuppressWarnings("unchecked")
    public Blob createBlobByInputStream(InputStream inputStream) {
        Blob blob = null;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = inputStream.read(b)) != -1)
                out.write(b, 0, n);
            inputStream.close();
            out.close();
            LobCreator lobCreator = org.hibernate.Hibernate.getLobCreator(getSession());
            blob = lobCreator.createBlob(out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return blob;
    }

    /**
     * @see com.innofi.framework.dao.hibernate.IHibernateDao#createClobByFilePath(String)
     */
    @SuppressWarnings("unchecked")
    public Clob createClobByFilePath(String filePath) {
        Clob file = null;
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader(filePath));
            String lineContent = null;
            StringBuffer fileContent = new StringBuffer();
            while ((lineContent = fileReader.readLine()) != null) {
                fileContent.append(lineContent);
            }
            fileReader.close();
            LobCreator lobCreator = org.hibernate.Hibernate.getLobCreator(getSession());
            file = lobCreator.createClob(fileContent.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


    /**
     * @see com.innofi.framework.dao.hibernate.IHibernateDao#findByProperty(Class, String, Object, String) {
     */
    public List<T> findByProperty(Class<T> clazz, final String propertyName, final Object value, final String matchType) {
        Criterion criterion = buildPropertyFilterCriterion(propertyName, value, matchType);
        return findByCriterions(clazz, null, criterion);
    }

    /**
     * @see com.innofi.framework.dao.hibernate.IHibernateDao#findByProperty(Class, String, Object, String, List) {
     */
    public List<T> findByProperty(Class<T> clazz, String propertyName, Object value, String matchType, List<PropertyOrder> orders) {
        Criterion criterion = buildPropertyFilterCriterion(propertyName, value, matchType);
        return findByCriterions(clazz, orders, criterion);
    }


    /**
     * @see com.innofi.framework.dao.hibernate.IHibernateDao#findByFilters {
     */
    public List<T> findByFilters(Class<T> clazz, List<PropertyFilter> filters, List<PropertyOrder> orders) {
        Criterion[] criterions = buildPropertyFilterCriterions(filters);
        return findByCriterions(clazz, orders, criterions);
    }

    /**
     * @see com.innofi.framework.dao.hibernate.IHibernateDao#findByPage_Filters(Class, java.util.List, java.util.List, com.innofi.framework.dao.pagination.Page)
     */
    public void findByPage_Filters(Class<T> clazz, final List<PropertyFilter> filters, List<PropertyOrder> orders, final Page page) {
        Criterion[] criterions = buildPropertyFilterCriterions(filters);
        findPage_Criterion(clazz, orders, page, criterions);
    }

    /**
     * @see com.innofi.framework.dao.hibernate.IHibernateDao#countByPropertyFilters(Class, List)
     */
    public long countByPropertyFilters(Class<T> clazz, List<PropertyFilter> filters) {
        Assert.notNull(clazz, "clazz类型不能为空");
        Criterion[] criterions = buildPropertyFilterCriterions(filters);
        Criteria c = createCriteria(clazz, criterions);
        return countCriteriaResult(c);
    }

    /**
     * 获取根据实体对象Class获取hibernate持久化对象
     *
     * @param clazz 实体对象class
     * @return hibernate持久化对象
     */
    public PersistentClass getPersistenceClass(Class clazz) {
        return getConfiguration().getClassMapping(clazz.getName());
    }

    /**
     * 获取实体的hibernate映射信息
     *
     * @param clazz 实体对象class
     * @return map中key为数据库字段名称 value为PropertyMapping对象
     */
    public HashMap<String, PropertyMapping> getEntityHbmMapping(Class clazz) {
        PersistentClass persistentClass = getPersistenceClass(clazz);
        if (persistentClass == null) throw new DAOException("class[" + clazz.getName() + "]不存在hibernate映射，请检查!");
        return getEntityHbmMapping(persistentClass);
    }

    /**
     * 获取实体的hibernate属性映射信息
     *
     * @param persistentClass hibernate持久化对象
     * @return map中key为数据库字段名称 value为PropertyMapping对象
     */
    public HashMap<String, PropertyMapping> getEntityHbmMapping(PersistentClass persistentClass) {
        HashMap<String, PropertyMapping> propertyMapping = new HashMap<String, PropertyMapping>();
        org.hibernate.mapping.Property id = persistentClass.getIdentifierProperty();//主键
        if(Validator.isNotNull(id)){
	        if (id.isComposite()) {//复合主键
	            Component component = (Component) id.getValue();
	            Iterator<org.hibernate.mapping.Property> it = component.getPropertyIterator();
	            while (it.hasNext()) {
	                org.hibernate.mapping.Property property = it.next();
	                String propertyName = property.getName();
	                PropertyMapping pm = new PropertyMapping();
	                Iterator<Column> iterator = property.getColumnIterator();
	                while (iterator.hasNext()) {
	                    Column column = iterator.next();
	                    String columnName = column.getName();
	                    pm.setPropertyName(propertyName);
	                    pm.setColumnName(columnName);
	                    pm.setIdentifierProperty(true);
	                    propertyMapping.put(columnName.toUpperCase(), pm);
	                }
	            }
	        } else {
	            Iterator<Column> iterator = id.getColumnIterator();//单一主键
	            PropertyMapping pm = new PropertyMapping();
	            String propertyName = id.getName();
	            while (iterator.hasNext()) {//属性
	                Column column = iterator.next();
	                String columnName = column.getName();
	                pm.setPropertyName(propertyName);
	                pm.setColumnName(columnName);
	                pm.setIdentifierProperty(true);
	                propertyMapping.put(columnName.toUpperCase(), pm);
	            }
	        }
        }
        Iterator<org.hibernate.mapping.Property> it = persistentClass.getPropertyIterator();
        loadPropertyMapping(propertyMapping, it);
        return propertyMapping;
    }

    /**
     * 替换参数后的sql语句
     *
     * @param moduleId 模块唯一标识
     * @param hqlName  hql名称
     * @param params   hql语句中的实参
     * @return
     * @throws Exception
     */
    public String getFormatHql(String moduleId, String hqlName, Map<String, String> params) {
        return DynamicStatementContext.getHql(dbType, moduleId, hqlName, params);
    }


    /**
     * 按属性条件列表创建Criterion数组,辅助函数.
     */
    protected Criterion[] buildPropertyFilterCriterions(final List<PropertyFilter> filters) {
        List<Criterion> criterionList = new ArrayList<Criterion>();
        if (filters != null) {
            for (PropertyFilter filter : filters) {
                Criterion criterion = buildPropertyFilterCriterion(filter.getPropertyName(), filter.getPropertyValue(), filter.getMatchType());
                if (criterion != null) {
                    criterionList.add(criterion);
                }
            }
        }
        return criterionList.toArray(new Criterion[criterionList.size()]);
    }

    /**
     * 按属性条件参数创建Criterion,辅助函数. 比较符号说明 Restrictions.eq SQL 里的 = 操作
     * Restrictions.ne SQL 里的 ！= 操作 Restrictions.allEq 全等于操作 Restrictions.gt SQL
     * 里的 > 操作 Restrictions.ge SQL 里的 >= 操作 Restrictions.lt SQL里的< 操作
     * Restrictions.le SQL里的<= 操作 Restrictions.between SQL里的 BETWEEN 操作
     * Restrictions.like SQL里的 LIKE 操作 Restrictions.in SQL里的 IN 操作
     * Restrictions.isNull SQL里的 ISNull 操作 Restrictions.isNotNull SQL里的
     * ISNotNull 操作 Restrictions.isEmpty SQL里的 ISEmpty 操作
     * Restrictions.isNotEmpty SQL里的 ISNotEmpty 操作
     *
     * @param propertyName  属性名称
     * @param propertyValue String类型对象字段常量
     * @param matchType     value String类型比较方式
     * @return 添加匹配条件后的 Criterion 对象
     */
    protected Criterion buildPropertyFilterCriterion(final String propertyName, final Object propertyValue, final String matchType) {
        Criterion criterion = null;
        try {
            // 根据MatchType构造criterion

            if (QueryConstants.EQUAL.equals(matchType) || matchType == null) {
                criterion = Restrictions.eq(propertyName, propertyValue);
            } else if (QueryConstants.LIKE.equals(matchType)) {
                criterion = Restrictions.like(propertyName, (String) propertyValue, MatchMode.ANYWHERE);
            } else if (QueryConstants.LESS_OR_EQUAL.equals(matchType)) {
                criterion = Restrictions.le(propertyName, propertyValue);
            } else if (QueryConstants.LESS_THAN.equals(matchType)) {
                criterion = Restrictions.lt(propertyName, propertyValue);
            } else if (QueryConstants.GREATER_OR_EQUAL.equals(matchType)) {
                criterion = Restrictions.ge(propertyName, propertyValue);
            } else if (QueryConstants.GREATER_THAN.equals(matchType)) {
                criterion = Restrictions.gt(propertyName, propertyValue);
            } else if (QueryConstants.NOT_EQUAL.equals(matchType)) {
                criterion = Restrictions.ne(propertyName, propertyValue);
            } else if (QueryConstants.IS_NULL.equals(matchType)) {
                criterion = Restrictions.isNull(propertyName);
            } else if (QueryConstants.IS_NOT_NULL.equals(matchType)) {
                criterion = Restrictions.isNotNull(propertyName);
            } else if (QueryConstants.IS_EMPTY.equals(matchType)) {
                criterion = Restrictions.isEmpty(propertyName);
            } else if (QueryConstants.IS_NOT_EMPTY.equals(matchType)) {
                criterion = Restrictions.isNotEmpty(propertyName);
            } else if (QueryConstants.IN.equals(matchType)) {
                if (propertyValue instanceof Object[] && ((Object[]) propertyValue).length > 0) {
                    criterion = Restrictions.in(propertyName, (Object[]) propertyValue);
                } else if (propertyValue instanceof Collection
                        && ((Collection) propertyValue).size() > 0) {
                    criterion = Restrictions.in(propertyName, (Collection) propertyValue);
                }
            } else if (QueryConstants.NOT_IN.equals(matchType)) {
                if (propertyValue instanceof Object[] && ((Object[]) propertyValue).length > 0) {
                    criterion = Restrictions.not(Restrictions.in(propertyName, (Object[]) propertyValue));
                } else if (propertyValue instanceof Collection
                        && ((Collection) propertyValue).size() > 0) {
                    criterion = Restrictions.not(Restrictions.in(propertyName, (Collection) propertyValue));
                }
            } else if (QueryConstants.BETWEEN.equals(matchType)) {
                Object object1 = null;
                Object object2 = null;
                if (propertyValue instanceof Object[]) {
                    object1 = ((Object[]) propertyValue)[0];
                    object2 = ((Object[]) propertyValue)[1];
                } else if (propertyValue instanceof Collection) {
                    Collection c = (Collection) propertyValue;
                    int index = 0;
                    for (Iterator it = c.iterator(); it.hasNext() && index <= 1; ) {
                        if (index == 0) {
                            object1 = it.next();
                            index++;
                        } else {
                            object2 = it.next();
                        }
                    }
                }
                if (object1 != null && object2 != null) {
                    criterion = Restrictions.between(propertyName, object1,
                            object2);
                } else if (object1 != null) {
                    criterion = Restrictions.ge(propertyName, object1);
                } else if (object2 != null) {
                    criterion = Restrictions.le(propertyName, object1);
                }
            } else if (QueryConstants.ALL_EQUAL.equals(matchType)
                    && propertyValue instanceof Map) {
                criterion = Restrictions.allEq((Map) propertyValue);
            }
        } catch (Exception e) {
            throw ReflectionUtil.convertReflectionExceptionToUnchecked(e);
        }
        return criterion;
    }

    /**
     * 设置分页参数到Query对象,辅助函数.
     */
    protected Query setPageParameter(final Query q, final Page<T> page) {
        // hibernate的firstResult的序号从0开始
        q.setFirstResult((page.getPageNo() - 1) * page.getPageSize());
        q.setMaxResults(page.getPageSize());
        return q;
    }

    /**
     * 设置分页参数到Criteria对象,辅助函数.
     */
    protected void setPageParameter(final Criteria c, final List<PropertyOrder> orders, final Page<T> page) {
        // hibernate的firstResult的序号从0开始
        c.setFirstResult((page.getPageNo() - 1) * page.getPageSize());
        c.setMaxResults(page.getPageSize());

        if (orders != null && orders.size() > 0 && !page.isOrderBySetted()) {
            addOrders(c, orders);
        } else if (page.isOrderBySetted()) {
            addOrders(c, page);
        }

    }

    protected void addOrders(Criteria c, Page<T> page) {
        String[] orderByArray = StringUtils.split(page.getOrderBy(), ',');
        String[] orderArray = StringUtils.split(page.getOrder(), ',');

        Assert.isTrue(orderByArray.length == orderArray.length, "分页多重排序参数中,排序字段与排序方向的个数不相等");

        for (int i = 0; i < orderByArray.length; i++) {
            if (QueryConstants.ORDER_ASC.equals(orderArray[i])) {
                c.addOrder(Order.asc(orderByArray[i]));
            } else {
                c.addOrder(Order.desc(orderByArray[i]));
            }
        }
    }

    /**
     * 执行count查询获得本次Criteria查询所能获得的对象总数.
     */
    @SuppressWarnings("unchecked")
    protected long countCriteriaResult(final Criteria c) {
        CriteriaImpl impl = (CriteriaImpl) c;

        // 先把Projection、ResultTransformer、OrderBy取出来,清空三者后再执行Count操作
        Projection projection = impl.getProjection();
        ResultTransformer transformer = impl.getResultTransformer();

        List<CriteriaImpl.OrderEntry> orderEntries = null;
        try {
            orderEntries = (List) ReflectionUtil.getFieldValue(impl, "orderEntries");
            ReflectionUtil.setFieldValue(impl, "orderEntries", new ArrayList());
        } catch (Exception e) {
            logger.error("不可能抛出的异常:{}", e.getMessage());
        }

        // 执行Count查询
        long totalCount = (Long) c.setProjection(Projections.rowCount()).uniqueResult();

        // 将之前的Projection,ResultTransformer和OrderBy条件重新设回去
        c.setProjection(projection);

        if (projection == null) {
            c.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
        }
        if (transformer != null) {
            c.setResultTransformer(transformer);
        }
        try {
            ReflectionUtil.setFieldValue(impl, "orderEntries", orderEntries);
        } catch (Exception e) {
            logger.error("不可能抛出的异常:{}", e.getMessage());
        }

        return totalCount;
    }


    /**
     * 执行count查询获得本次Hql查询所能获得的对象总数.
     * <p/>
     * 本函数只能自动处理简单的hql语句,复杂的hql查询请另行编写count语句查询.
     */
    protected long countHqlResult(Class<T> clazz, final String hql, final Object... values) {

        String countHql = SqlUtil.getCountHql(hql);

        try {
            Long count = findUnique(clazz, countHql, values);
            return count;
        } catch (Exception e) {
            throw new RuntimeException("hql can't be auto count, hql is:"
                    + countHql, e);
        }
    }

    /**
     * 执行count查询获得本次Hql查询所能获得的对象总数.
     * <p/>
     * 本函数只能自动处理简单的hql语句,复杂的hql查询请另行编写count语句查询.
     */
    protected long countHqlResult(Class<T> clazz, final String hql, final Map<String, ?> values) {

        String countHql = SqlUtil.getCountHql(hql);

        try {
            Long count = (Long) findUnique(clazz, countHql, values);
            return count;
        } catch (Exception e) {
            throw new RuntimeException("hql can't be auto count, hql is:" + countHql, e);
        }
    }

    /**
     * 添加排序方法
     *
     * @param orders PropertyOrder 对象
     * @param ca     Hibernate Criteria对象
     */
    protected void addOrders(Criteria ca, List<PropertyOrder> orders) {
        for (PropertyOrder order : orders) {
            String propertyName = order.getPropertyName();
            String orderRule = order.getOrderRule();
            if (orderRule.equals(QueryConstants.ORDER_ASC)) {
                ca.addOrder(Order.asc(propertyName));
            }
            if (orderRule.equals(QueryConstants.ORDER_DESC)) {
                ca.addOrder(Order.desc(propertyName));
            }
        }
    }


    /**
     * 获取当前对象是否缓存
     *
     * @param calzz 当前对象
     * @return 返回是否缓存
     */
    protected boolean getCacheAble(Class<T> calzz) {
        try {
            BasePojo pojo = (BasePojo) calzz.newInstance();
            return pojo.getCacheAble();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 创建Query查询接口
     *
     * @param queryString
     * @return
     */
    protected Query createQuery(Class<T> clazz, String queryString) {
        Query q = getSession().createQuery(queryString);
        q.setCacheable(getCacheAble(clazz));
        return q;
    }

    /**
     * 创建实体对象Criteria查询对象
     *
     * @return criteria对象实例
     */
    protected Criteria createCriteria(Class<T> clazz) {
        Criteria c = getSession().createCriteria(clazz);
        c.setCacheable(getCacheAble(clazz));
        return c;
    }

    /*
     *将属性与字段映射信息加载到Map中
     */
    protected void loadPropertyMapping(HashMap<String, PropertyMapping> propertyMapping, Iterator<org.hibernate.mapping.Property> it) {
        while (it.hasNext()) {
            org.hibernate.mapping.Property property = it.next();
            String propertyName = property.getName();
            PropertyMapping pm = new PropertyMapping();
            Iterator<Column> iterator = property.getColumnIterator();
            while (iterator.hasNext()) {
                Column column = iterator.next();
                String columnName = column.getName();
                pm.setPropertyName(propertyName);
                pm.setColumnName(columnName);
                propertyMapping.put(columnName.toUpperCase(), pm);
            }
        }
    }


    public List<T> mapToBean(List<Map<String, Object>> records, Class<T> clazz) {
        HashMap<String, PropertyMapping> propertyMappings = getEntityHbmMapping(clazz);
        List<T> beanList = new ArrayList<T>();
        for (Map<String, ?> record : records) {
            try {
                T bean = clazz.newInstance();
                for (Map.Entry<String, ?> entry : record.entrySet()) {
                    String colName = entry.getKey();
                    Object value = entry.getValue();
                    if (propertyMappings.containsKey(colName)) {
                        PropertyMapping pm = propertyMappings.get(colName);
                        String propertyName = pm.getPropertyName();
                        ReflectionUtil.setFieldValue(bean, propertyName, value);
                    } else {
                		try{
                			ReflectionUtil.setFieldValue(bean, StringUtil.convertPropertyName(colName), value);
                		}catch (Exception e) {
						}
                    }
                }
                beanList.add(bean);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return beanList;
    }

    @Override
    public long countCriteriaResult(DetachedCriteria detachedCriteria) {
        Criteria c = detachedCriteria.getExecutableCriteria(getSession());
        // 先把Projection、ResultTransformer、OrderBy取出来,清空三者后再执行Count操作
        CriteriaImpl impl = (CriteriaImpl) c;

        Projection projection = impl.getProjection();
        ResultTransformer transformer = impl.getResultTransformer();

        List<CriteriaImpl.OrderEntry> orderEntries = null;
        try {
            orderEntries = (List) ReflectionUtil.getFieldValue(impl, "orderEntries");
            ReflectionUtil.setFieldValue(impl, "orderEntries", new ArrayList());
        } catch (Exception e) {
            logger.error("不可能抛出的异常:{}", e.getMessage());
        }

        // 执行Count查询
        long totalCount = (Long) c.setProjection(Projections.rowCount()).uniqueResult();

        // 将之前的Projection,ResultTransformer和OrderBy条件重新设回去
        c.setProjection(projection);

        if (projection == null) {
            c.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
        }
        if (transformer != null) {
            c.setResultTransformer(transformer);
        }
        try {
            ReflectionUtil.setFieldValue(impl, "orderEntries", orderEntries);
        } catch (Exception e) {
            logger.error("不可能抛出的异常:{}", e.getMessage());
        }

        return totalCount;

    }

    @Override
    public <T> T execute(HibernateCallback<T> action) throws DataAccessException {
        return getHibernateTemplate().execute(action);
    }

    @Override
    public <T> T get(Class<T> entityClass, Serializable id) throws DataAccessException {
        return getHibernateTemplate().get(entityClass, id);
    }

    @Override
    public <T> T get(Class<T> entityClass, Serializable id, LockMode lockMode) throws DataAccessException {
        return getHibernateTemplate().get(entityClass, id, lockMode);
    }

    @Override
    public Object get(String entityName, Serializable id) throws DataAccessException {
        return getHibernateTemplate().get(entityName, id);
    }

    @Override
    public Object get(String entityName, Serializable id, LockMode lockMode) throws DataAccessException {
        return getHibernateTemplate().get(entityName, id, lockMode);
    }

    @Override
    public <T> T load(Class<T> entityClass, Serializable id) throws DataAccessException {
        return getHibernateTemplate().load(entityClass, id);
    }

    @Override
    public <T> T load(Class<T> entityClass, Serializable id, LockMode lockMode) throws DataAccessException {
        return getHibernateTemplate().load(entityClass, id, lockMode);
    }

    @Override
    public Object load(String entityName, Serializable id) throws DataAccessException {
        return getHibernateTemplate().load(entityName, id);
    }

    @Override
    public Object load(String entityName, Serializable id, LockMode lockMode) throws DataAccessException {
        return getHibernateTemplate().load(entityName, id, lockMode);
    }

    @Override
    public <T> List<T> loadAll(Class<T> entityClass) throws DataAccessException {
        return getHibernateTemplate().loadAll(entityClass);
    }

    @Override
    public void load(Object entity, Serializable id) throws DataAccessException {
        getHibernateTemplate().load(entity, id);
    }

    @Override
    public void refresh(Object entity) throws DataAccessException {
        getHibernateTemplate().refresh(entity);
    }

    @Override
    public void refresh(Object entity, LockMode lockMode) throws DataAccessException {
        getHibernateTemplate().refresh(entity, lockMode);
    }

    @Override
    public boolean contains(Object entity) throws DataAccessException {
        return getHibernateTemplate().contains(entity);
    }

    @Override
    public void evict(Object entity) throws DataAccessException {
        getHibernateTemplate().evict(entity);
    }

    @Override
    public void initialize(Object proxy) throws DataAccessException {
        getHibernateTemplate().initialize(proxy);
    }

    @Override
    public Filter enableFilter(String filterName) throws IllegalStateException {
        return getHibernateTemplate().enableFilter(filterName);
    }

    @Override
    public void lock(Object entity, LockMode lockMode) throws DataAccessException {
        getHibernateTemplate().lock(entity, lockMode);
    }

    @Override
    public void lock(String entityName, Object entity, LockMode lockMode) throws DataAccessException {
        getHibernateTemplate().lock(entityName, entity, lockMode);
    }

    @Override
    public Serializable save(Object entity) throws DataAccessException {
        return getHibernateTemplate().save(entity);
    }

    @Override
    public Serializable save(String entityName, Object entity) throws DataAccessException {
        return getHibernateTemplate().save(entityName, entity);
    }

    @Override
    public void update(Object entity) throws DataAccessException {
        getHibernateTemplate().save(entity);
    }

    @Override
    public void update(Object entity, LockMode lockMode) throws DataAccessException {
        getHibernateTemplate().update(entity, lockMode);
    }

    @Override
    public void update(String entityName, Object entity) throws DataAccessException {
        getHibernateTemplate().update(entityName, entity);
    }

    @Override
    public void update(String entityName, Object entity, LockMode lockMode) throws DataAccessException {
        getHibernateTemplate().update(entityName, entity, lockMode);
    }

    @Override
    public void saveOrUpdate(Object entity) throws DataAccessException {
        getHibernateTemplate().saveOrUpdate(entity);
    }

    @Override
    public void saveOrUpdate(String entityName, Object entity) throws DataAccessException {
        getHibernateTemplate().saveOrUpdate(entityName, entity);
    }

    @Override
    public void replicate(Object entity, ReplicationMode replicationMode) throws DataAccessException {
        getHibernateTemplate().replicate(entity, replicationMode);
    }

    @Override
    public void replicate(String entityName, Object entity, ReplicationMode replicationMode) throws DataAccessException {
        getHibernateTemplate().replicate(entityName, entity, replicationMode);
    }

    @Override
    public void persist(Object entity) throws DataAccessException {
        getHibernateTemplate().persist(entity);
    }

    @Override
    public void persist(String entityName, Object entity) throws DataAccessException {
        getHibernateTemplate().persist(entityName, entity);
    }

    @Override
    public <T> T merge(T entity) throws DataAccessException {
        return getHibernateTemplate().merge(entity);
    }

    @Override
    public <T> T merge(String entityName, T entity) throws DataAccessException {
        return getHibernateTemplate().merge(entityName, entity);
    }

    @Override
    public void delete(Object entity) throws DataAccessException {
        getHibernateTemplate().delete(entity);
    }

    @Override
    public void delete(Object entity, LockMode lockMode) throws DataAccessException {
        getHibernateTemplate().delete(entity, lockMode);
    }

    @Override
    public void delete(String entityName, Object entity) throws DataAccessException {
        getHibernateTemplate().delete(entityName, entity);
    }

    @Override
    public void delete(String entityName, Object entity, LockMode lockMode) throws DataAccessException {
        getHibernateTemplate().delete(entityName, entity, lockMode);
    }

    @Override
    public void deleteAll(Collection<?> entities) throws DataAccessException {
        getHibernateTemplate().deleteAll(entities);
    }

    @Override
    public void flush() throws DataAccessException {
        getHibernateTemplate().flush();
    }

    @Override
    public void clear() throws DataAccessException {
        getHibernateTemplate().clear();
    }

    @Override
    public List<?> find(String queryString, Object... values) throws DataAccessException {
        return getHibernateTemplate().find(queryString, values);
    }

    @Override
    public List<?> findByNamedParam(String queryString, String paramName, Object value) throws DataAccessException {
        return getHibernateTemplate().findByNamedParam(queryString, paramName, value);
    }

    @Override
    public List<?> findByNamedParam(String queryString, String[] paramNames, Object[] values) throws DataAccessException {
        return getHibernateTemplate().findByNamedParam(queryString, paramNames, values);
    }

    @Override
    public List<?> findByValueBean(String queryString, Object valueBean) throws DataAccessException {
        return getHibernateTemplate().findByValueBean(queryString, valueBean);
    }

    @Override
    public List<?> findByNamedQuery(String queryName, Object... values) throws DataAccessException {
        return getHibernateTemplate().findByNamedQuery(queryName, values);
    }

    @Override
    public List<?> findByNamedQueryAndNamedParam(String queryName, String paramName, Object value) throws DataAccessException {
        return getHibernateTemplate().findByNamedQueryAndNamedParam(queryName, paramName, value);
    }

    @Override
    public List<?> findByNamedQueryAndNamedParam(String queryName, String[] paramNames, Object[] values) throws DataAccessException {
        return getHibernateTemplate().findByNamedQueryAndNamedParam(queryName, paramNames, values);
    }

    @Override
    public List<?> findByNamedQueryAndValueBean(String queryName, Object valueBean) throws DataAccessException {
        return getHibernateTemplate().findByNamedQueryAndValueBean(queryName, valueBean);
    }

    @Override
    public List<?> findByCriteria(DetachedCriteria criteria) throws DataAccessException {
        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public List<?> findByCriteria(DetachedCriteria criteria, int firstResult, int maxResults) throws DataAccessException {
        return getHibernateTemplate().findByCriteria(criteria, firstResult, maxResults);
    }

    @Override
    public <T> List<T> findByExample(T exampleEntity) throws DataAccessException {
        return getHibernateTemplate().findByExample(exampleEntity);
    }

    @Override
    public <T> List<T> findByExample(String entityName, T exampleEntity) throws DataAccessException {
        return getHibernateTemplate().findByExample(entityName, exampleEntity);
    }

    @Override
    public <T> List<T> findByExample(T exampleEntity, int firstResult, int maxResults) throws DataAccessException {
        return getHibernateTemplate().findByExample(exampleEntity, firstResult, maxResults);
    }

    @Override
    public <T> List<T> findByExample(String entityName, T exampleEntity, int firstResult, int maxResults) throws DataAccessException {
        return getHibernateTemplate().findByExample(entityName, exampleEntity, firstResult, maxResults);
    }

    @Override
    public Iterator<?> iterate(String queryString, Object... values) throws DataAccessException {
        return getHibernateTemplate().iterate(queryString, values);
    }

    @Override
    public void closeIterator(Iterator<?> it) throws DataAccessException {
        getHibernateTemplate().closeIterator(it);
    }

    @Override
    public int bulkUpdate(String queryString, Object... values) throws DataAccessException {
        return getHibernateTemplate().bulkUpdate(queryString, values);
    }
}
