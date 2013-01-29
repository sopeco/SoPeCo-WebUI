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
package org.sopeco.frontend.server.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.sopeco.frontend.server.persistence.entities.ScheduledExperiment;
import org.sopeco.frontend.shared.entities.AccountDetails;
import org.sopeco.frontend.shared.entities.ExecutedExperimentDetails;
import org.sopeco.frontend.shared.entities.MECLog;
import org.sopeco.frontend.shared.entities.Visualization;
import org.sopeco.persistence.exceptions.DataNotFoundException;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class UiPersistenceProvider {

	private EntityManagerFactory emf;

	/**
	 * Constructor.
	 */
	UiPersistenceProvider(EntityManagerFactory factory) {
		emf = factory;
	}

	/**
	 * Returns the AccountDetails object with the given id. The Id is composed
	 * of: dbHost + ":" + dbPort + "/" + dbName
	 * 
	 * @param accountId
	 * @return AccountDetails with the given Id
	 */
	public AccountDetails loadAccountDetails(String accountId) {
		EntityManager em = emf.createEntityManager();
		AccountDetails foundDetails = em.find(AccountDetails.class, accountId);
		em.close();
		return foundDetails;
	}

	/**
	 * Returns all AccountDetails object from the database.
	 * 
	 * @return AccountDetails with the given Id
	 */
	@SuppressWarnings("unchecked")
	public List<AccountDetails> loadAllAccountDetails() throws DataNotFoundException {
		List<AccountDetails> accountDetails = null;
		EntityManager em = emf.createEntityManager();
		try {
			Query query = em.createNamedQuery("getAllAccounts");
			accountDetails = query.getResultList();
			return accountDetails;
		} catch (Exception e) {
			return new ArrayList<AccountDetails>();
		} finally {
			em.close();
		}
	}

	/**
	 * Removes the given AccountDetails object from the database.
	 * 
	 * @param accountDetails
	 *            which will be removed
	 */
	public void removeAccountDetails(AccountDetails accountDetails) {
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			AccountDetails toBeRemoved = em.merge(accountDetails);
			em.remove(toBeRemoved);
			em.getTransaction().commit();
		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			em.close();
		}
	}

	/**
	 * Stores the given AccountDetails object in the database.
	 * 
	 * @param accountDetails
	 */
	public void storeAccountDetails(AccountDetails accountDetails) {
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			em.merge(accountDetails);
			em.getTransaction().commit();
		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			em.close();
		}
	}

	public void storeScheduledExperiment(ScheduledExperiment scheduledExperiment) {
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			em.merge(scheduledExperiment);
			em.getTransaction().commit();
		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			em.close();
		}
	}

	public List<ScheduledExperiment> loadAllScheduledExperiments() {
		List<ScheduledExperiment> accountDetails = null;
		EntityManager em = emf.createEntityManager();
		try {
			TypedQuery<ScheduledExperiment> query = em.createNamedQuery("getAllExperiments", ScheduledExperiment.class);
			accountDetails = query.getResultList();
			return accountDetails;
		} catch (Exception e) {
			return new ArrayList<ScheduledExperiment>();
		} finally {
			em.close();
		}
	}

	public List<ScheduledExperiment> loadScheduledExperimentsByAccount(String accountName) {
		List<ScheduledExperiment> accountDetails = null;
		EntityManager em = emf.createEntityManager();
		try {
			TypedQuery<ScheduledExperiment> query = em.createNamedQuery("getExperimentsByAccount",
					ScheduledExperiment.class);
			accountDetails = query.setParameter("account", accountName).getResultList();
			return accountDetails;
		} catch (Exception e) {
			return new ArrayList<ScheduledExperiment>();
		} finally {
			em.close();
		}
	}

	public List<ExecutedExperimentDetails> loadExecutedExperimentDetails(String accountId, String scenarioName) {
		List<ExecutedExperimentDetails> mecLogs = null;
		EntityManager em = emf.createEntityManager();
		try {
			TypedQuery<ExecutedExperimentDetails> query = em.createNamedQuery("getExperiments",
					ExecutedExperimentDetails.class);
			mecLogs = query.setParameter("accountId", accountId).setParameter("scenarioName", scenarioName)
					.getResultList();
			return mecLogs;
		} catch (Exception e) {
			return new ArrayList<ExecutedExperimentDetails>();
		} finally {
			em.close();
		}
	}

	public long storeExecutedExperimentDetails(ExecutedExperimentDetails experimentDetails) {
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			experimentDetails = em.merge(experimentDetails);
			em.getTransaction().commit();
		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			em.close();
		}
		return experimentDetails.getId();
	}

	public void storeMECLog(MECLog mecLog) {
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			em.merge(mecLog);
			em.getTransaction().commit();
		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			em.close();
		}
	}

	public MECLog loadMECLog(long id) {
		EntityManager em = emf.createEntityManager();
		MECLog log = em.find(MECLog.class, id);
		em.close();
		return log;
	}

	public ScheduledExperiment loadScheduledExperiment(long id) {
		EntityManager em = emf.createEntityManager();
		ScheduledExperiment experiment = em.find(ScheduledExperiment.class, id);
		em.close();
		return experiment;
	}

	public void removeScheduledExperiment(ScheduledExperiment experiment) {
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			ScheduledExperiment toBeRemoved = em.merge(experiment);
			em.remove(toBeRemoved);
			em.getTransaction().commit();
		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			em.close();
		}
	}

	public List<Visualization> loadAllVisualizations() {
		List<Visualization> visualizations = null;
		EntityManager em = emf.createEntityManager();
		try {
			TypedQuery<Visualization> query = em.createNamedQuery("getAllVisualizations", Visualization.class);
			visualizations = query.getResultList();
			return visualizations;
		} catch (Exception e) {
			return new ArrayList<Visualization>();
		} finally {
			em.close();
		}
	}

	public List<Visualization> loadVisualizationsByAccount(String accountName) {
		List<Visualization> visualizations = null;
		EntityManager em = emf.createEntityManager();
		try {
			TypedQuery<Visualization> query = em.createNamedQuery("getVisualizationsByAccount", Visualization.class);
			visualizations = query.setParameter("accountId", accountName).getResultList();
			return visualizations;
		} catch (Exception e) {
			return new ArrayList<Visualization>();
		} finally {
			em.close();
		}
	}

	public void storeVisualization(Visualization visualization) {
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			em.merge(visualization);
			em.getTransaction().commit();
		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			em.close();
		}
	}

	public void removeVisualization(Visualization visualization) {
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			Visualization toBeRemoved = em.merge(visualization);
			em.remove(toBeRemoved);
			em.getTransaction().commit();
		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			em.close();
		}
	}
}
