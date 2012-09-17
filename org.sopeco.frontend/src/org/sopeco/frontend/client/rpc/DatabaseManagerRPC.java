package org.sopeco.frontend.client.rpc;

import java.util.List;

import org.sopeco.frontend.shared.definitions.DatabaseDefinition;
import org.sopeco.persistence.metadata.entities.DatabaseInstance;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("databaseManagerRPC")
public interface DatabaseManagerRPC extends RemoteService {
	
	/**
	 * Get a list of all available databases
	 * @return List of all databases
	 */
	List<DatabaseInstance> getAllDatabases ();
	
	/**
	 * Add a new database in the metadatabase
	 * @param databaseInstance of the new database
	 * @return true if succeeded
	 */
	boolean addDatabase (DatabaseInstance databaseInstance, String passwd);
	
	/**
	 * Removes the committed database
	 * @param database which will be removed
	 * @return true if succeeded
	 */
	boolean removeDatabase ( DatabaseInstance databaseInstance );
	
	/**
	 * Select a database
	 * @param database which will be selected
	 * @return true if succeeded
	 */
	boolean selectDatabase (DatabaseInstance databaseInstance, String passwd );
}
