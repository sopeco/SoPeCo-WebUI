package org.sopeco.webui.shared.push.packages;

import org.sopeco.webui.shared.entities.RunningControllerStatus;
import org.sopeco.webui.shared.push.PushDomain;
import org.sopeco.webui.shared.push.PushPackage;

public class PushControllerStatus extends PushPackage {

	public PushControllerStatus() {
	}

	public PushControllerStatus(PushDomain pDomain) {
		super(pDomain);
	}

	/** */
	private static final long serialVersionUID = 1L;

	private RunningControllerStatus ccExperiment;

	public RunningControllerStatus getCcExperiment() {
		return ccExperiment;
	}

	public void setCcExperiment(RunningControllerStatus ccExperiment) {
		this.ccExperiment = ccExperiment;
	}

}
