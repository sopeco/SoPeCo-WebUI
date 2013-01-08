package org.sopeco.frontend.shared.push;

import java.util.List;

import org.sopeco.frontend.shared.entities.FrontendScheduledExperiment;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class CurrentControllerQueuePackage extends PushPackage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<FrontendScheduledExperiment> experiments;

	/**
	 * @return the experiments
	 */
	public List<FrontendScheduledExperiment> getExperiments() {
		return experiments;
	}

	/**
	 * @param pExperiments
	 *            the experiments to set
	 */
	public void setExperiments(List<FrontendScheduledExperiment> pExperiments) {
		this.experiments = pExperiments;
	}

}
