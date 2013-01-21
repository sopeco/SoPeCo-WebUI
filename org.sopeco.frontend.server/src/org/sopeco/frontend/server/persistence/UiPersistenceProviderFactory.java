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

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.sopeco.config.Configuration;
import org.sopeco.config.IConfiguration;
import org.sopeco.frontend.server.UiConfiguration;
import org.sopeco.persistence.config.PersistenceConfiguration;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class UiPersistenceProviderFactory {

	private static final Logger LOGGER = Logger.getLogger(UiPersistenceProviderFactory.class.getName());
	
	private static final String DB_URL = "javax.persistence.jdbc.url";
	private static final String SERVER_URL_PREFIX = "jdbc:derby://";
	private static final String SERVER_URL_SUFFIX = ";create=true";

	/**
	 * Hidden constructor.
	 */
	private UiPersistenceProviderFactory() {

	}

	/**
	 * Creates a new UiPersistenceProvider.
	 * 
	 * @return UiPersistenceProvider
	 */
	public static UiPersistenceProvider createUiPersistenceProvider() {
		try {
			EntityManagerFactory factory = Persistence.createEntityManagerFactory("sopeco-frontend",
					getConfigOverrides());
			return new UiPersistenceProvider(factory);
		} catch (Exception e) {
			LOGGER.severe(e.getLocalizedMessage());
			throw new IllegalArgumentException("Could not create peristence provider!", e);
		}
	}

	/**
	 * Creates a configuration map, which contains the connection url to the
	 * database.
	 * 
	 * @return
	 */
	private static Map<String, Object> getConfigOverrides() {
		Map<String, Object> configOverrides = new HashMap<String, Object>();
		configOverrides.put(DB_URL, getServerUrl());
		return configOverrides;
	}

	/**
	 * Builds the connection-url of the ui-database.
	 * 
	 * @return connection-url
	 */
	private static String getServerUrl() {
		PersistenceConfiguration.getSessionSingleton(Configuration.getGlobalSessionId());
		IConfiguration config = Configuration.getSessionSingleton(Configuration.getGlobalSessionId());
		if (config.getPropertyAsStr(UiConfiguration.META_DATA_HOST) == null) {
			throw new NullPointerException("No MetaDataHost defined.");
		}
		String host = config.getPropertyAsStr(UiConfiguration.META_DATA_HOST);
		String port = config.getPropertyAsStr(UiConfiguration.META_DATA_PORT);
		String name = config.getPropertyAsStr(UiConfiguration.SOPECO_UI_DATABASE_NAME);
		String user = config.getPropertyAsStr(UiConfiguration.SOPECO_UI_DATABASE_USER);
		String password = config.getPropertyAsStr(UiConfiguration.SOPECO_UI_DATABASE_PASSWORD);
		return SERVER_URL_PREFIX + host + ":" + port + "/" + name + ";user=" + user + ";password=" + password
				+ SERVER_URL_SUFFIX;
	}
}
