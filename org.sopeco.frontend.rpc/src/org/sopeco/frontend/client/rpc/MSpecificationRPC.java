package org.sopeco.frontend.client.rpc;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * 
 * @author Marius Oehler
 * 
 */
@RemoteServiceRelativePath("mSpecificationRPC")
public interface MSpecificationRPC extends RemoteService {

	/**
	 * Return a list with all specification names, of the selected scenario.
	 * 
	 * @return list with names
	 */
	List<String> getAllSpecificationNames();

}
