package org.sopeco.frontend.server.persistence;

import org.sopeco.persistence.IMetaDataPersistenceProvider;
import org.sopeco.persistence.PersistenceProviderFactory;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class UiPersistence {

	/**
	 * Hidden Constructor
	 */
	private UiPersistence() {
	}

	private static IMetaDataPersistenceProvider metaPersistenceProvider;
	private static UiPersistenceProvider uiPersistenceProvider;

	/**
	 * Returns the PersistenceProvider of the MetaDatabase. The MetaDatabase
	 * contains the information about the existing databases/accounts (host,
	 * name..).
	 * 
	 * @return IMetaDataPersistenceProvider
	 */
	public static IMetaDataPersistenceProvider getMetaProvider() {
		if (metaPersistenceProvider == null) {
			metaPersistenceProvider = PersistenceProviderFactory.getInstance().getMetaDataPersistenceProvider();
		}
		return metaPersistenceProvider;
	}

	/**
	 * Returns the provider of the database, in which all settings, like
	 * AccountDetails, ..., are stored.
	 * 
	 * @return UiPersistenceProvider
	 */
	public static UiPersistenceProvider getUiProvider() {
		if (uiPersistenceProvider == null) {
			uiPersistenceProvider = UiPersistenceProviderFactory.createUiPersistenceProvider();
		}
		return uiPersistenceProvider;
	}
}
