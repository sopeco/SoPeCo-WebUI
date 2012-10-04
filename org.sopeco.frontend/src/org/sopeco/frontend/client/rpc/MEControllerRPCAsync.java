package org.sopeco.frontend.client.rpc;

import java.util.List;

import org.sopeco.persistence.entities.definition.MeasurementEnvironmentDefinition;
import org.sopeco.persistence.entities.definition.ParameterRole;

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

	void removeNamespace(String path, AsyncCallback<Boolean> callback);

	void addNamespace(String path, AsyncCallback<Boolean> callback);

	void getCurrentMEDefinition(AsyncCallback<MeasurementEnvironmentDefinition> callback);

	void renameNamespace(String namespacePath, String newName, AsyncCallback<Boolean> callback);

	void addParameter(String path, String name, String type, ParameterRole role, AsyncCallback<Boolean> callback);

	void removeParameter(String path, String name, AsyncCallback<Boolean> callback);

	void updateParameter(String path, String oldName, String newName, String type, ParameterRole role,
			AsyncCallback<Boolean> callback);

}
