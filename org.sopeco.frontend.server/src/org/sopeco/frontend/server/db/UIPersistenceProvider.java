package org.sopeco.frontend.server.db;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.frontend.server.db.entities.MEControllerUrl;
import org.sopeco.frontend.shared.entities.AccountDetails;
import org.sopeco.persistence.exceptions.DataNotFoundException;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class UIPersistenceProvider {

	private EntityManagerFactory emf;
	private static final Logger LOGGER = LoggerFactory.getLogger(UIPersistenceProvider.class);

	public UIPersistenceProvider(EntityManagerFactory factory) {

		emf = factory;

	}

	/**
	 * Returns the AccountDetails object with the given key.
	 * 
	 * @param scenarioName
	 * @return
	 */
	public AccountDetails getAccountDetails(String accountId) {
		EntityManager em = emf.createEntityManager();

		AccountDetails foundDetails = em.find(AccountDetails.class, accountId);
		em.close();
		
		if (foundDetails == null) {
			LOGGER.warn("No AccountDetails with key '" + accountId + "' found.");
		}
		
		return foundDetails;
	}

	/**
	 * Stores the given AccountDetails object in the database.
	 * 
	 * @param details
	 */
	public void storeAccountDetails(AccountDetails details) {
		EntityManager em = emf.createEntityManager();

		try {
			em.getTransaction().begin();
			em.merge(details);
			em.getTransaction().commit();
		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			em.close();
		}
	}

	@SuppressWarnings("unchecked")
	public List<MEControllerUrl> loadAllMEControllerUrls() throws DataNotFoundException {
		List<MEControllerUrl> coltrollerUrls;
		String errorMsg = "Could not find a mecUrl in the database.";

		EntityManager em = emf.createEntityManager();

		try {

			Query query = em.createNamedQuery("findAllColtrollerUrls");
			coltrollerUrls = query.getResultList();

		} catch (Exception e) {

			LOGGER.error(errorMsg);
			throw new DataNotFoundException(errorMsg, e);
		} finally {
			em.close();
		}

		// check if query was successful
		if (coltrollerUrls != null) {
			return coltrollerUrls;
		} else {
			LOGGER.debug(errorMsg);
			throw new DataNotFoundException(errorMsg);
		}
	}

	/**
	 * Checks if the given controller url are already in the database.
	 * 
	 * @param url
	 * @return true if the url was found
	 */
	public boolean checkMEControllerUrlExists(String url) {
		MEControllerUrl meController = null;

		EntityManager em = emf.createEntityManager();

		try {
			meController = em.find(MEControllerUrl.class, url);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			em.close();
		}

		// check if query was successful
		if (meController != null) {
			return true;
		}

		return false;
	}

	/**
	 * Stores the given mec url in the database.
	 * 
	 * @param url
	 */
	public void storeMEControllerUrl(String url) {
		EntityManager em = emf.createEntityManager();

		MEControllerUrl t = new MEControllerUrl();
		t.setUrl(url);
		t.setAdded(System.currentTimeMillis());

		try {
			em.getTransaction().begin();
			em.merge(t);
			em.getTransaction().commit();
		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			em.close();
		}
	}
}
