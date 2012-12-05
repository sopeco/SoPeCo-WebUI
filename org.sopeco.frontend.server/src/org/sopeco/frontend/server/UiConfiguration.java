package org.sopeco.frontend.server;

/**
 * 
 * @author Marius Oehler
 * 
 */
public abstract class UiConfiguration {
	public static final String USER_TIMEOUT = "sopeco.ui.userTimeout";
	public static final String TIMEOUT_CHECK_INTERVAL = "sopeco.ui.timeoutCheckInterval";

	public static final String META_DATA_HOST = "sopeco.config.persistence.metaServer.host";
	public static final String META_DATA_PORT = "sopeco.config.persistence.metaServer.port";

	public static final String SOPECO_UI_DATABASE_NAME = "sopeco.ui.persistence.name";
	public static final String SOPECO_UI_DATABASE_USER = "sopeco.ui.persistence.user";
	public static final String SOPECO_UI_DATABASE_PASSWORD = "sopeco.ui.persistence.password";

	public static final String SOPECO_UI_USERTIMEOUT = "sopeco.ui.userTimeout";
}
