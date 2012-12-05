package org.sopeco.frontend.server.db;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.sopeco.frontend.shared.entities.AccountDetails;

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
	public AccountDetails getAccountDetails(String accountId) {
		EntityManager em = emf.createEntityManager();
		AccountDetails foundDetails = em.find(AccountDetails.class, accountId);
		em.close();
		return foundDetails;
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
}
