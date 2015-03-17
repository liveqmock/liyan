/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 * 
 * Copyright (c) 2002-2012 BSTEK Corp. All rights reserved.
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial (http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */
package com.bstek.dorado.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public enum SessionStrategy {
	DEFAULT {

		@Override
		public <T> T doWork(SessionFactory sessionFactory,
				SessionWorker<T> worker) throws Exception {
			Session session = null;
			boolean autoClose = false;
			try {
				session = sessionFactory.getCurrentSession();
			} catch (Exception e) {
				session = sessionFactory.openSession();
				autoClose = true;
			}
			
			try {
				T result = worker.doSession(session);
				return result;
			} finally {
				if (autoClose) {
					session.close();
				}
			}
		}
	},
	CURRENT{

		@Override
		public <T> T doWork(SessionFactory sessionFactory, SessionWorker<T> worker) throws Exception {
			Session session = sessionFactory.getCurrentSession();
			return worker.doSession(session);
		}
		
	},
	OPEN{

		@Override
		public <T> T doWork(SessionFactory sessionFactory,
				SessionWorker<T> worker) throws Exception {
			Session session = sessionFactory.openSession();
			try {
				T result = worker.doSession(session);
				return result;
			} finally {
				session.close();
			}
		}
	};
	
	public abstract <T> T doWork(SessionFactory sessionFactory, SessionWorker<T> worker) throws Exception;
	
	public static abstract class SessionWorker<T> {
		public abstract T doSession(Session session) throws Exception;
	}
}
