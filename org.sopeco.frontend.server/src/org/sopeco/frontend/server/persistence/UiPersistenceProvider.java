package org.sopeco.frontend.server.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

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
	public List<AccountDetails> loadAllAccountDetails() {
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
}
