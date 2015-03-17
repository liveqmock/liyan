package com.bstek.dorado.hibernate.provider;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.transform.ResultTransformer;

import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.core.Context;
import com.bstek.dorado.data.provider.AbstractDataProvider;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.hibernate.HibernateUtils;
import com.bstek.dorado.hibernate.SessionStrategy;
import com.bstek.dorado.hibernate.criteria.HibernateCriteriaTransformer;
import com.bstek.dorado.hibernate.criteria.TopCriteria;
import com.bstek.dorado.util.Assert;

/**
 * 利用Hibernate条件查询(Criteria Queries)功能实现的DataProvider
 * 
 * @author mark
 * 
 */
@XmlNode(fixedProperties = "type=hibernateCriteria")
public class CriteriaDataProvider extends AbstractDataProvider {
	private String sessionFactory;
	private boolean unique = false;
	private boolean autoFilter = false;
	private ResultTransformer resultTransformer;
	
	private SessionStrategy sessionStrategy = SessionStrategy.DEFAULT;
	
	public String getSessionFactory() {
		return sessionFactory;
	}
	public void setSessionFactory(String sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public void setSessionStrategy(SessionStrategy sessionStrategy) {
		this.sessionStrategy = sessionStrategy;
	}
	@ClientProperty(escapeValue="DEFAULT")
	public SessionStrategy getSessionStrategy() {
		Assert.notNull(sessionStrategy, "[Assertion failed] - this argument 'sessionStrategy' is required; it must not be null");
		return sessionStrategy;
	}
	
	@ClientProperty(escapeValue="false")
	public boolean isUnique() {
		return unique;
	}
	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	public void setAutoFilter(boolean autoFilter) {
		this.autoFilter = autoFilter;
	}
	
	@ClientProperty(escapeValue="false")
	public boolean isAutoFilter() {
		return this.autoFilter;
	}
	

	@XmlProperty(parser="spring:dorado.hibernate.resultTransformerParser")
	@IdeProperty(enumValues="ALIAS_TO_ENTITY_MAP,ROOT_ENTITY,DISTINCT_ROOT_ENTITY,PROJECTION")
	public ResultTransformer getResultTransformer() {
		return resultTransformer;
	}

	public void setResultTransformer(ResultTransformer resultTransformer) {
		this.resultTransformer = resultTransformer;
	}
	
	protected SessionFactory getSessionFactoryOject() throws Exception {
		SessionFactoryManager sessionManager = (SessionFactoryManager) Context
				.getCurrent().getServiceBean("hibernateSessionFactoryManager");
		SessionFactory sessionFactoryBean = sessionManager
				.getSessionFactory(sessionFactory);
		Assert.notNull(sessionFactoryBean, "SessionFactory named [" + sessionFactory + "] cound not be found.");
		return sessionFactoryBean;
	}
	
	private TopCriteria criterita;

	@XmlSubNode(fixed = true)
	public TopCriteria getCriteria() {
		Assert.notNull(criterita, "Criteria must not be null.");
		return criterita;
	}

	public void setCriteria(TopCriteria criterita) {
		this.criterita = criterita;
	}

	protected HibernateCriteriaTransformer getCriteriaTransformer()
			throws Exception {
		return (HibernateCriteriaTransformer) Context.getCurrent()
				.getServiceBean("criteriaTransformer");
	}

	/**
	 * 创建DetachedCriteria
	 * 
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	protected DetachedCriteria createDetachedCriteria(Object parameter, SessionFactory f) throws Exception {
		HibernateCriteriaTransformer transformer = this.getCriteriaTransformer();
		Object realParameter = HibernateUtils.getRealParameter(parameter);
		TopCriteria topCriteria = this.getCriteria();
		
		DetachedCriteria detachedCriteria = transformer.toHibernate(topCriteria, realParameter, f);
		if (this.isAutoFilter()) {
			com.bstek.dorado.data.provider.Criteria filterCriteria = HibernateUtils.getFilterCriteria(parameter);
			if (filterCriteria != null) {
				detachedCriteria = HibernateUtils.createFilter(detachedCriteria, filterCriteria);
			}
		}
		
		return detachedCriteria;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Object internalGetResult(Object parameter, DataType resultDataType)
			throws Exception {
		SessionFactory f = this.getSessionFactoryOject();
		final DetachedCriteria detachedCriteria = this.createDetachedCriteria(parameter, f);
		
		SessionStrategy ss = this.getSessionStrategy();
		Object result = ss.doWork(f, new SessionStrategy.SessionWorker<Object>() {

			@Override
			public Object doSession(Session session) throws Exception {
				Criteria criteria = detachedCriteria.getExecutableCriteria(session);
				ResultTransformer rtf = getResultTransformer();
				if (rtf != null) {
					criteria.setResultTransformer(rtf);
				}
				
				if (!isUnique()) {
					List list = criteria.list();
					return list;
				} else {
					return criteria.uniqueResult();
				}
			}
			
		});
		
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void internalGetPagingResult(Object parameter, final Page<?> page,
			DataType resultDataType) throws Exception {
		SessionFactory f = this.getSessionFactoryOject();
		final DetachedCriteria detachedCriteria = this.createDetachedCriteria(parameter, f);
		
		SessionStrategy ss = this.getSessionStrategy();
		ss.doWork(f, new SessionStrategy.SessionWorker<Object>() {

			@Override
			public Object doSession(Session session) throws Exception {
				Criteria criteria = detachedCriteria.getExecutableCriteria(session);
				ResultTransformer rtf = getResultTransformer();
				if (rtf != null) {
					criteria.setResultTransformer(rtf);
				}
				
				if (!isUnique()) {
					// 查询结果记录
					criteria.setFirstResult(page.getFirstEntityIndex());
					criteria.setMaxResults(page.getPageSize());
					List list = criteria.list();
					page.setEntities(list);

					// 查询记录条数
					HibernateUtils.makeCount(criteria);
					Number c = (Number) criteria.uniqueResult();
					page.setEntityCount(c.intValue());
				} else {
					Object object = criteria.uniqueResult();
					if (object != null) {
						List entities = new ArrayList(1);
						entities.add(object);
						page.setEntities(entities);
						page.setEntityCount(entities.size());
					}
				}
				return null;
			}
		});
	}

}
