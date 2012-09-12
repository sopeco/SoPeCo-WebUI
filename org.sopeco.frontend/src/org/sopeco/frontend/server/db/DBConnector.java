package org.sopeco.frontend.server.db;

public class DBConnector {

	private final static String SERVER_HOST = "deqkal276.qkal.sap.corp";
	private final static int SERVER_PORT = 1527;
	private final static String DATABASE_NAME = "secop-frontend";
	private final static String SERVER_URL_PREFIX = "jdbc:derby://";
	private final static String SERVER_URL_SUFFIX = ";create=true";
	
	private final static String SERVER_DB_DRIVER_CLASS_VALUE = "org.apache.derby.jdbc.ClientDriver";
}
