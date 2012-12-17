package org.sopeco.frontend.client.rpc;

import java.util.List;

import org.sopeco.frontend.shared.entities.FrontendScheduledExperiment;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 * @author Marius Oehler
 * 
 */
public interface ExecuteRPCAsync {
	void scheduleExperiment(FrontendScheduledExperiment rawScheduledExperiment, AsyncCallback<Void> callback);

	void getScheduledExperiments(AsyncCallback<List<FrontendScheduledExperiment>> callback);

	void removeScheduledExperiment(long id, AsyncCallback<Boolean> callback);
	
	void setScheduledExperimentEnabled(long id, boolean enabled, AsyncCallback<Boolean> callback);
}
