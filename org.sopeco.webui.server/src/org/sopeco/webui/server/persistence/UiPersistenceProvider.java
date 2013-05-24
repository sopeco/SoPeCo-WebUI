/**
 * Copyright (c) 2013 SAP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the SAP nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL SAP BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.sopeco.webui.server.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.persistence.exceptions.DataNotFoundException;
import org.sopeco.webui.server.persistence.entities.ScheduledExperiment;
import org.sopeco.webui.shared.entities.ExecutedExperimentDetails;
import org.sopeco.webui.shared.entities.MECLog;
import org.sopeco.webui.shared.entities.Visualization;
import org.sopeco.webui.shared.entities.account.Account;
import org.sopeco.webui.shared.entities.account.AccountDetails;
import org.sopeco.webui.shared.entities.account.RememberMeToken;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class UiPersistenceProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(UiPersistenceProvider.class);

	private EntityManagerFactory emf;

	/**
	 * Constructor.
	 */
	UiPersistenceProvider(EntityManagerFactory factory) {
		emf = factory;
	}

	public AccountDetails loadAccountDetails(long accountId) {
		return loadSingleById(AccountDetails.class, accountId);
	}

	public List<AccountDetails> loadAllAccountDetails() throws DataNotFoundException {
		return loadByQuery(AccountDetails.class, "getAllAccountDetails");
	}

	public void removeAccountDetails(AccountDetails accountDetails) {
		remove(accountDetails);
	}

	public void storeAccountDetails(AccountDetails accountDetails) {
		store(accountDetails);
	}

	public void storeScheduledExperiment(ScheduledExperiment scheduledExperiment) {
		store(scheduledExperiment);
	}

	public List<ScheduledExperiment> loadAllScheduledExperiments() {
		return loadByQuery(ScheduledExperiment.class, "getAllExperiments");
	}

	public List<ScheduledExperiment> loadScheduledExperimentsByAccount(long accountId) {
		return loadByQuery(ScheduledExperiment.class, "getExperimentsByAccount", "account", accountId);
	}

	public List<ExecutedExperimentDetails> loadExecutedExperimentDetails(long accountId, String scenarioName) {
		return loadByQuery(ExecutedExperimentDetails.class, "getExperiments", "accountId", accountId, "scenarioName",
				scenarioName);
	}

	public long storeExecutedExperimentDetails(ExecutedExperimentDetails experimentDetails) {
		ExecutedExperimentDetails entity = store(experimentDetails);
		return entity == null ? -1 : entity.getId();
	}

	public void storeMECLog(MECLog mecLog) {
		store(mecLog);
	}

	public MECLog loadMECLog(long id) {
		return loadSingleById(MECLog.class, id);
	}

	public ScheduledExperiment loadScheduledExperiment(long id) {
		return loadSingleById(ScheduledExperiment.class, id);
	}

	public void removeScheduledExperiment(ScheduledExperiment experiment) {
		remove(experiment);
	}

	public List<Visualization> loadAllVisualizations() {
		return loadByQuery(Visualization.class, "getAllVisualizations");
	}

	public List<Visualization> loadVisualizationsByAccount(long accountId) {
		return loadByQuery(Visualization.class, "getVisualizationsByAccount", "accountId", accountId);
	}

	public void storeVisualization(Visualization visualization) {
		store(visualization);
	}

	public void removeVisualization(Visualization visualization) {
		remove(visualization);
	}

	public Account storeAccount(Account account) {
		return store(account);
	}

	public void removeAccount(Account account) {
		remove(account);
	}

	public Account loadAccount(String accountName) {
		return loadSingleByQuery(Account.class, "getAccountByName", "accountName", accountName);
	}

	public Account loadAccount(long primaryKey) {
		return loadSingleById(Account.class, primaryKey);
	}

	public void storeRememberMeToken(RememberMeToken token) {
		store(token);
	}

	public RememberMeToken loadRememberMeToken(String tokenHash) {
		return loadSingleById(RememberMeToken.class, tokenHash);
	}

	public int deleteExpiredRememberMeToken() {
		return updateQuery("deleteExipredTokens", "expireDate", System.currentTimeMillis());
	}

	public void removeRememberMeToken(RememberMeToken rememberMeToken) {
		remove(rememberMeToken);
	}

	/********************************************************************/

	private <T> T store(T object) {
		EntityManager em = emf.createEntityManager();
		T managedObject = null;
		try {
			em.getTransaction().begin();
			managedObject = em.merge(object);
			em.getTransaction().commit();
		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			em.close();
		}
		return managedObject;
	}

	private <T> void remove(T object) {
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			T removeObject = em.merge(object);
			em.remove(removeObject);
			em.getTransaction().commit();
		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			em.close();
		}
	}

	private <T> T loadSingleById(Class<T> returnClazz, Object primaryKey) {
		EntityManager em = emf.createEntityManager();
		T entity = em.find(returnClazz, primaryKey);
		em.close();
		return entity;
	}

	private <T> T loadSingleByQuery(Class<T> returnClazz, String queryName, Object... parameterList) {
		T result = null;
		EntityManager em = emf.createEntityManager();
		try {
			TypedQuery<T> query = em.createNamedQuery(queryName, returnClazz);
			for (int i = 0; i <= parameterList.length / 2; i += 2) {
				query.setParameter((String) parameterList[i], parameterList[i + 1]);
			}
			result = query.getSingleResult();
		} catch (NoResultException e) {
			String parameter = "[";
			for (int i = 0; i < parameterList.length; i++) {
				parameter += (i == 0 ? "" : ", ") + parameterList[i];
			}
			parameter += "]";
			LOGGER.debug("No result with query '" + queryName + "' with parameter " + parameter);
		} catch (NonUniqueResultException e) {
			String parameter = "[";
			for (int i = 0; i < parameterList.length; i++) {
				parameter += (i == 0 ? "" : ", ") + parameterList[i];
			}
			parameter += "]";
			LOGGER.debug("No unique result with query '" + queryName + "' with parameter " + parameter);
		} catch (IllegalStateException e) {
			LOGGER.error("Query '" + queryName + "' failed: " + e);
		} finally {
			em.close();
		}
		return result;
	}

	private <T> List<T> loadByQuery(Class<T> clazz, String queryName, Object... parameterList) {
		List<T> result = new ArrayList<T>();
		EntityManager em = emf.createEntityManager();
		try {
			TypedQuery<T> query = em.createNamedQuery(queryName, clazz);
			for (int i = 0; i + 1< parameterList.length; i += 2) {
				query.setParameter((String) parameterList[i], parameterList[i + 1]);
			}
			result = query.getResultList();
		} catch (IllegalStateException e) {
			LOGGER.error("Query '" + queryName + "' failed: " + e);
		} finally {
			em.close();
		}
		return result;
	}

	private int updateQuery(String queryName, Object... parameterList) {
		EntityManager em = emf.createEntityManager();
		Query query = em.createNamedQuery(queryName);
		for (int i = 0; i <= parameterList.length / 2; i += 2) {
			query.setParameter((String) parameterList[i], parameterList[i + 1]);
		}
		int count = 0;
		try {
			em.getTransaction().begin();
			count = query.executeUpdate();
			em.getTransaction().commit();
		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			em.close();
		}
		return count;
	}
}
