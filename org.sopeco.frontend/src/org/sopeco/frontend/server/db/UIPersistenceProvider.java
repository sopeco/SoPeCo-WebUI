package org.sopeco.frontend.server.db;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.frontend.server.db.entities.Test;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class UIPersistenceProvider {

	private EntityManagerFactory emf;
	private static Logger logger = LoggerFactory.getLogger(UIPersistenceProvider.class);

	public UIPersistenceProvider(EntityManagerFactory factory) {

		emf = factory;

	}

	public void store() {
		EntityManager em = emf.createEntityManager();

		Test t = new Test("marius");

		try {

			em.getTransaction().begin();
			em.merge(t);
			em.getTransaction().commit();

			logger.debug("test 1");
		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			em.close();
		}
	}
	
	public void load() {
		Test result = null;
		
		EntityManager em = emf.createEntityManager();

		try {
			result = em.find(Test.class, "marius");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			em.close();
		}

		// check if query was successful
		if (result != null) {
			logger.error("ok");
		} else {
			logger.error("error");
		}
	}

}
