package org.sopeco.frontend.shared.push;

import org.sopeco.frontend.shared.entities.CurrentControllerExperiment;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class CurrentControllerExperimentPackage extends PushPackage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private CurrentControllerExperiment currentControllerExperiment;

	/**
	 * @return the currentControllerExperiment
	 */
	public CurrentControllerExperiment getCurrentControllerExperiment() {
		return currentControllerExperiment;
	}

	/**
	 * @param pCurrentControllerExperiment
	 *            the currentControllerExperiment to set
	 */
	public void setCurrentControllerExperiment(CurrentControllerExperiment pCurrentControllerExperiment) {
		this.currentControllerExperiment = pCurrentControllerExperiment;
	}

}
