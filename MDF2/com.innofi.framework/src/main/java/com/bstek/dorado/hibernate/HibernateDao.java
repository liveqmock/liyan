package com.bstek.dorado.hibernate;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;

import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.entity.FilterType;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.util.Assert;
import com.bstek.dorado.util.proxy.ProxyBeanUtils;

/**
 * 此类的实现在相当程度上借鉴了SpringSide3.x
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-1-21
 */
public class HibernateDao<T, PK extends Serializable> {
	private static final Log logger = LogFactory.getLog(HibernateDao.class);

	@SuppressWarnings("rawtypes")
	private static final List EMPTY_UNMUTABLE_LIST = java.util.Collections.EMPTY_LIST;

	protected SessionFactory sessionFactory;
	protected Class<T> entityType = getEntityType();

	@Autowired(required=false)
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public Session getSession() {
		return getSessionFactory().getCurrentSession();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected Class<T> getEntityType() {
		Class cl = getClass();
		Class<T> resultType = null;
		Type superType = cl.getGenericSuperclass();

		if (superType instanceof ParameterizedType) {
			Type[] paramTypes = ((ParameterizedType) superType)
					.getActualTypeArguments();
			if (paramTypes.length > 0) {
				resultType = (Class<T>) paramTypes[0];
			} else {
				logger.warn("Can not determine EntityType for class ["
						+ cl.getSimpleName() + "].");
			}
		} else {
			logger.warn("[" + cl.getSimpleName()
					+ "] is not a parameterized type.");
		}
		return resultType;
	}

	protected String getIdPropertyName() {
		ClassMetadata meta = getSessionFactory().getClassMetadata(entityType);
		return meta.getIdentifierPropertyName();
	}

	public void save(T entity) {
		String entityName = this.getEntityName(entity);
		getSession().saveOrUpdate(entityName, entity);
	}

	public void delete(T entity) {
		String entityName = this.getEntityName(entity);
		getSession().delete(entityName, entity);
	}

	public void delete(PK id) {
		delete(get(id));
	}

	public EntityState persistEntity(T entity) {
		EntityState state = EntityUtils.getState(entity);
		if (EntityState.DELETED.equals(state)) {
			delete(entity);
		} else if (EntityState.MODIFIED.equals(state)
				|| EntityState.NEW.equals(state)
				|| EntityState.MOVED.equals(state)) {
			save(entity);
		}
		return state;
	}

	@SuppressWarnings("unchecked")
	public int persistEntities(Collection<T> entities) {
		int i = 0;
		for (Object entity : EntityUtils.getIterable(entities,
				FilterType.DELETED)) {
			delete((T) entity);
			i++;
		}
		for (Object entity : EntityUtils.getIterable(entities,
				FilterType.MODIFIED)) {
			save((T) entity);
			i++;
		}
		for (Object entity : EntityUtils
				.getIterable(entities, FilterType.MOVED)) {
			save((T) entity);
			i++;
		}
		for (Object entity : EntityUtils.getIterable(entities, FilterType.NEW)) {
			save((T) entity);
			i++;
		}
		return i;
	}

	@SuppressWarnings("unchecked")
	public T get(PK id) {
		return (T) getSession().load(entityType, id);
	}

	public Criteria createCriteria() {
		return getSession().createCriteria(entityType);
	}

	public Criteria createCriteria(Criterion... criterions) {
		Criteria criteria = createCriteria();
		for (Criterion c : criterions) {
			criteria.add(c);
		}
		return criteria;
	}

	public Query createQuery(String hql, Object... parameters) {
		Query q = getSession().createQuery(hql);
		if (parameters != null) {
			for (int i = 0; i < parameters.length; ++i) {
				q.setParameter(i, parameters[i]);
			}
		}
		return q;
	}

	public Query createQuery(String queryString, Map<String, ?> parameters) {
		Query query = getSession().createQuery(queryString);
		if (parameters != null) {
			query.setProperties(parameters);
		}
		return query;
	}

	@SuppressWarnings("unchecked")
	public List<T> find(Criteria criteria) {
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public Page<T> find(Page<T> page, Criteria criteria) {
		notNull(page, "page");

		long totalCount = countCriteriaResult(criteria);
		page.setEntityCount((int) totalCount);
		setPageParameterToCriteria(criteria, page);
		page.setEntities(criteria.list());
		return page;
	}

	public List<T> find(Criterion... criterions) {
		return find(createCriteria(criterions));
	}

	public List<T> find(DetachedCriteria detachedCriteria) {
		Criteria criteria = detachedCriteria
				.getExecutableCriteria(getSession());
		return find(criteria);
	}

	public Page<T> find(Page<T> page, DetachedCriteria detachedCriteria) {
		Criteria criteria = detachedCriteria
				.getExecutableCriteria(getSession());
		return find(page, criteria);
	}

	public Page<T> find(Page<T> page, Criterion... criterions) {
		notNull(page, "page");
		return find(page, createCriteria(criterions));
	}

	@SuppressWarnings("rawtypes")
	protected long countCriteriaResult(Criteria criteria) {
		CriteriaImplHelper implHelper = new CriteriaImplHelper(criteria);
		long count = 0;
		List orderEntries = null;
		
		try {
			orderEntries = implHelper.getOrderEntries();
			implHelper.setOrderEntries(EMPTY_UNMUTABLE_LIST);
			
			Projection projection = implHelper.getProjection();
			ResultTransformer transformer = implHelper.getResultTransformer();
			
			count = ((Number) criteria.setProjection(Projections.rowCount())
					.uniqueResult()).longValue();
			criteria.setProjection(projection);
			if (projection == null) {
				criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
			}
			if (transformer != null) {
				criteria.setResultTransformer(transformer);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				implHelper.setOrderEntries(orderEntries);
			} catch (Exception e) {
				logger.warn(e, e);
			}
		}
		return count;
	}

	protected long countHqlResult(String hql, Object... parameters) {
		String countHql = generateCountHql(hql);
		return ((Number) findUnique(countHql, parameters)).longValue();
	}

	protected long countHqlResult(String hql, Map<String, ?> parameters) {
		String countHql = generateCountHql(hql);
		return ((Number) findUnique(countHql, parameters)).longValue();
	}

	private String generateCountHql(String hql) {
		hql = "from " + StringUtils.substringAfter(hql, "from");
		hql = StringUtils.substringBefore(hql, "order by");
		String countHql = "select count(*) " + hql;
		return countHql;
	}

	protected Criteria setPageParameterToCriteria(Criteria c, Page<T> page) {
		c.setFirstResult(page.getFirstEntityIndex());
		c.setMaxResults(page.getPageSize());
		return c;
	}

	protected Query setPageParameterToQuery(Query q, Page<T> page) {
		q.setFirstResult(page.getFirstEntityIndex());
		q.setMaxResults(page.getPageSize());
		return q;
	}

	public List<T> get(Collection<PK> ids) {
		return find(new Criterion[] { Restrictions.in(getIdPropertyName(), ids) });
	}

	@SuppressWarnings("unchecked")
	public List<T> getAll() {
		return createCriteria().list();
	}

	public Page<T> getAll(Page<T> page) {
		return find(page);
	}

	@SuppressWarnings("unchecked")
	public <X> X findUnique(String hql, Object... parameters) {
		return (X) createQuery(hql, parameters).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public <X> X findUnique(String hql, Map<String, ?> parameters) {
		return (X) createQuery(hql, parameters).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public <X> List<X> find(String hql, Object... parameters) {
		return createQuery(hql, parameters).list();
	}

	@SuppressWarnings("unchecked")
	public <X> List<X> find(String hql, Map<String, ?> parameters) {
		return createQuery(hql, parameters).list();
	}

	@SuppressWarnings("unchecked")
	public Page<T> find(Page<T> page, String hql, Object... parameters) {
		notNull(page, "page");

		Query q = createQuery(hql, parameters);
		long totalCount = countHqlResult(hql, parameters);
		page.setEntityCount((int) totalCount);
		setPageParameterToQuery(q, page);
		page.setEntities(q.list());
		return page;
	}

	@SuppressWarnings("unchecked")
	public Page<T> find(Page<T> page, String hql, Map<String, ?> parameters) {
		notNull(page, "page");

		Query q = createQuery(hql, parameters);
		long totalCount = countHqlResult(hql, parameters);
		page.setEntityCount((int) totalCount);
		setPageParameterToQuery(q, page);
		page.setEntities(q.list());
		return page;
	}
	
	protected String getEntityName(Object object) {
		if (object != null) {
			Class<?> cl = ProxyBeanUtils.getProxyTargetType(object);
			return cl.getName();
		} else {
			return null;
		}
	}
	
	protected void notNull(Object obj, String name) {
		Assert.notNull(obj, "[" + name + "] must not be null.");
	}
}
