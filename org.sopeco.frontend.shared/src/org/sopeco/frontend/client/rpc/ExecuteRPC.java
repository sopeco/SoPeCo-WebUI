package org.sopeco.frontend.client.rpc;

import org.sopeco.frontend.shared.entities.RawScheduledExperiment;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * 
 * @author Marius Oehler
 * 
 */
@RemoteServiceRelativePath("executeRPC")
public interface ExecuteRPC extends RemoteService {

	/**
	 * Execute the current Scenariodefinition on the controller with the given
	 * url.
	 * 
	 * @param url
	 */
	void execute(String url);

	/**
	 * Chekcs whether the controller on hte given url is running. If it's
	 * running, the return value is the time when it was started. If the
	 * controller is not running, it return -1.
	 * 
	 * @param url
	 * @return
	 */
	long isRunning(String url);

	/**
	 * No functionality at the moment.
	 * 
	 * @param url
	 */
	void stop(String url);

	void scheduleExperiment(RawScheduledExperiment rawScheduledExperiment);
}
