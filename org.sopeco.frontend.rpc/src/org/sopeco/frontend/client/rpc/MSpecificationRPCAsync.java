package org.sopeco.frontend.client.rpc;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 * @author Marius Oehler
 * 
 */
public interface MSpecificationRPCAsync {

	void getAllSpecificationNames(AsyncCallback<List<String>> callback);

	void setWorkingSpecification(String specificationName, AsyncCallback<Boolean> callback);

	void createSpecification(String name, AsyncCallback<Boolean> callback);

	void renameWorkingSpecification(String newName, AsyncCallback<Boolean> callback);
}
