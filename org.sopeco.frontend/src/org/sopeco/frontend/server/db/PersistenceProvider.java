package org.sopeco.frontend.server.db;

import javax.servlet.http.HttpSession;

import org.sopeco.persistence.IMetaDataPersistenceProvider;
import org.sopeco.persistence.IPersistenceProvider;
import org.sopeco.persistence.PersistenceProviderFactory;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class PersistenceProvider {

	private static final String META_PROVIDER = "metaPersistenceProvider";
	private static final String PERSISTENCE_PROVIDER = "persistenceProvider";
	private static final String UI_PROVIDER = "uiProvider";

	private PersistenceProvider() {
	}

	/**
	 * Stores the persistence provider in a session attribute.
	 * 
	 * @param session
	 *            current session
	 * @return IMetaDataPersistenceProvider
	 */
	public static IMetaDataPersistenceProvider getMetaProvider(HttpSession session) {
		if (session.getAttribute(META_PROVIDER) == null) {
			session.setAttribute(META_PROVIDER, PersistenceProviderFactory.getInstance()
					.getMetaDataPersistenceProvider(session.getId()));
		}

		return (IMetaDataPersistenceProvider) session.getAttribute(META_PROVIDER);
	}

	/**
	 * Stores the PersistenceProvider in the given Session.
	 * 
	 * @param provider
	 *            provider to store
	 * @param session
	 *            session to store
	 */
	public static void setPersistenceProvider(IPersistenceProvider provider, HttpSession session) {
		session.setAttribute(PERSISTENCE_PROVIDER, provider);
	}

	/**
	 * Returns the PersistenceProvider, which is stored in the given session.
	 * 
	 * @param session
	 * @return IPersistenceProvider
	 */
	public static IPersistenceProvider getPersistenceProvider(HttpSession session) {
		return (IPersistenceProvider) session.getAttribute(PERSISTENCE_PROVIDER);
	}
	
	/**
	 * Stores the UIPersistenceProvider in the given Session.
	 * 
	 * @param provider
	 *            provider to store
	 * @param session
	 *            session to store
	 */
	static void setUIPersistenceProvider(UIPersistenceProvider provider, HttpSession session) {
		session.setAttribute(UI_PROVIDER, provider);
	}

	/**
	 * Returns the UIPersistenceProvider, which is stored in the given session.
	 * 
	 * @param session
	 * @return IPersistenceProvider
	 */
	public static UIPersistenceProvider getUIPersistenceProvider(HttpSession session) {
		return (UIPersistenceProvider) session.getAttribute(UI_PROVIDER);
	}
	
}
