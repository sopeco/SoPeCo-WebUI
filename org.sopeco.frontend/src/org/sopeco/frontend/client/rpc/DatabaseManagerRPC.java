package org.sopeco.frontend.client.rpc;

import java.util.List;

import org.sopeco.frontend.shared.definitions.DatabaseDefinition;
import org.sopeco.persistence.metadata.entities.DatabaseInstance;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("databaseManagerRPC")
public interface DatabaseManagerRPC extends RemoteService {
	
	List<DatabaseDefinition> getAllDatabases ();
	
	boolean addDatabase (DatabaseDefinition databaseDefinition);
	
	boolean removeDatabase ( DatabaseDefinition databaseDefinition );
	
	boolean updateDatabase ( DatabaseDefinition databaseDefinition );

}
