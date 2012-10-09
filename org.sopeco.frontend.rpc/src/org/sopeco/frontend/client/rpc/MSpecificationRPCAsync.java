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

}
