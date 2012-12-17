package org.sopeco.frontend.client.rpc;

import java.util.List;

import org.sopeco.frontend.shared.entities.FrontendScheduledExperiment;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * 
 * @author Marius Oehler
 * 
 */
@RemoteServiceRelativePath("executeRPC")
public interface ExecuteRPC extends RemoteService {
	
	void scheduleExperiment(FrontendScheduledExperiment rawScheduledExperiment);
	
	List<FrontendScheduledExperiment> getScheduledExperiments();
	
	boolean removeScheduledExperiment(long id);
	
	boolean setScheduledExperimentEnabled(long id, boolean enabled);
}
