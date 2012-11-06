package org.sopeco.frontend.shared.definitions.result;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class SharedExperimentSeries implements Serializable {
	/** */
	private static final long serialVersionUID = 1L;

	private SharedScenarioInstance parentInstance;

	private String experimentName;

	private long version;

	private List<SharedExperimentRuns> experimentRuns;
	public SharedExperimentSeries() {
		experimentRuns = new ArrayList<SharedExperimentRuns>();
	}

	public void addExperimentRun(SharedExperimentRuns run) {
		experimentRuns.add(run);
		run.setParentSeries(this);
	}

	/**
	 * @return the experimentName
	 */
	public String getExperimentName() {
		return experimentName;
	}

	/**
	 * @return the experimentRuns
	 */
	public List<SharedExperimentRuns> getExperimentRuns() {
		return experimentRuns;
	}

	/**
	 * @return the parentInstance
	 */
	public SharedScenarioInstance getParentInstance() {
		return parentInstance;
	}

	/**
	 * @return the version
	 */
	public long getVersion() {
		return version;
	}

	/**
	 * @param experimentName
	 *            the experimentName to set
	 */
	public void setExperimentName(String experimentName) {
		this.experimentName = experimentName;
	}

	/**
	 * @param parentInstance
	 *            the parentInstance to set
	 */
	public void setParentInstance(SharedScenarioInstance parentInstance) {
		this.parentInstance = parentInstance;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(long version) {
		this.version = version;
	}
}
