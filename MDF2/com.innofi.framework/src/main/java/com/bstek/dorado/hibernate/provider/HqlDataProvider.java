package com.bstek.dorado.hibernate.provider;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.ResultTransformer;

import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.core.Context;
import com.bstek.dorado.data.provider.AbstractDataProvider;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.hibernate.HibernateUtils;
import com.bstek.dorado.hibernate.SessionStrategy;
import com.bstek.dorado.hibernate.hql.Hql;
import com.bstek.dorado.hibernate.hql.HqlQuerier;
import com.bstek.dorado.hibernate.hql.HqlUtil;
import com.bstek.dorado.util.Assert;

/**
 * 利用Hibernate HQL功能实现的DataProvider
 * 
 * @author mark
 */
@XmlNode(fixedProperties = "type=hibernateHql")
public class HqlDataProvider extends AbstractDataProvider {
	private String sessionFactory;
	private boolean unique = false;
	private ResultTransformer resultTransformer;
	
	private SessionStrategy sessionStrategy = SessionStrategy.DEFAULT;
	
	public String getSessionFactory() {
		return sessionFactory;
	}
	public void setSessionFactory(String sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@ClientProperty(escapeValue="false")
	public boolean isUnique() {
		return unique;
	}
	public void setUnique(boolean unique) {
		this.unique = unique;
	}
	
	public void setSessionStrategy(SessionStrategy sessionStrategy) {
		this.sessionStrategy = sessionStrategy;
	}
	@ClientProperty(escapeValue="DEFAULT")
	public SessionStrategy getSessionStrategy() {
		Assert.notNull(sessionStrategy, "[Assertion failed] - this argument 'sessionStrategy' is required; it must not be null");
		return sessionStrategy;
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
	
	private String hql;

	@IdeProperty(editor = "multiLines")
	public String getHql() {
		return hql;
	}

	public void setHql(String hql) {
		this.hql = hql;
	}

	protected Object internalGetResult(final Object parameter, final DataType resultDataType)
			throws Exception {
		Assert.notEmpty(this.hql, "Hql must not be empty.");

		SessionFactory f = this.getSessionFactoryOject();
		SessionStrategy ss = this.getSessionStrategy();
		
		Object result = ss.doWork(f, new SessionStrategy.SessionWorker<Object>() {

			@Override
			public Object doSession(Session session) throws Exception {
				Object realParameter = HibernateUtils.getRealParameter(parameter);
				Hql hql = createHql(HqlDataProvider.this.hql, realParameter, resultDataType);
				HqlQuerier querier = createHqlQuerier();
				
				Object result = querier.query(session, parameter, hql, HqlDataProvider.this);
				return result;
			}
			
		});
		
		return result;
	}

	@Override
	protected void internalGetPagingResult(final Object parameter, final Page<?> page,
			final DataType resultDataType) throws Exception {
		Assert.notEmpty(this.hql, "Hql must not be empty.");

		SessionFactory f = this.getSessionFactoryOject();
		SessionStrategy ss = this.getSessionStrategy();
		
		ss.doWork(f, new SessionStrategy.SessionWorker<Object>() {

			@Override
			public Object doSession(Session session) throws Exception {
				Object realParameter = HibernateUtils.getRealParameter(parameter);
				Hql hql = createHql(HqlDataProvider.this.hql, realParameter, resultDataType);
				HqlQuerier querier = createHqlQuerier();
				
				querier.query(session, parameter, hql, page, HqlDataProvider.this);
				return null;
			}
			
		});
	}

	protected HqlQuerier createHqlQuerier() throws Exception {
		HqlQuerier querier = (HqlQuerier) Context.getCurrent().getServiceBean(
				"hqlQuerier");
		return querier;
	}

	protected Hql createHql(String hqlClause, Object realParameter,
			DataType resultDataType) throws Exception {
		Hql hql = HqlUtil.build(hqlClause, realParameter);
		return hql;
	}

}
