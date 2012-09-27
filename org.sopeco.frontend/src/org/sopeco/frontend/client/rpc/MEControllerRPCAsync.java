package org.sopeco.frontend.client.rpc;

import java.util.List;

import org.sopeco.persistence.entities.definition.MeasurementEnvironmentDefinition;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 * @author Marius Oehler
 *
 */
public interface MEControllerRPCAsync {

	void checkControllerStatus(String url, AsyncCallback<Integer> callback);

	void getMEControllerList(AsyncCallback<List<String>> callback);

	void getValidUrlPattern(AsyncCallback<String[]> callback);

	void getMEDefinitionFromMEC(String controllerUrl, AsyncCallback<MeasurementEnvironmentDefinition> callback);

	void getBlankMEDefinition(AsyncCallback<MeasurementEnvironmentDefinition> callback);

}
