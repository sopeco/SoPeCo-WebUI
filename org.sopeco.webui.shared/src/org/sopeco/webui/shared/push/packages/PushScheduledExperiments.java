package org.sopeco.webui.shared.push.packages;

import java.util.ArrayList;

import org.sopeco.webui.shared.entities.FrontendScheduledExperiment;
import org.sopeco.webui.shared.push.PushDomain;
import org.sopeco.webui.shared.push.PushPackage;
import org.sopeco.webui.shared.push.PushSerializable;

public class PushScheduledExperiments extends PushPackage {

	public PushScheduledExperiments () {
	}
	
	public PushScheduledExperiments(PushDomain pDomain) {
		super(pDomain);
	}

	/** */
	private static final long serialVersionUID = 1L;

	private ArrayList<FrontendScheduledExperiment> list;

	public ArrayList<FrontendScheduledExperiment> getList() {
		return list;
	}

	public void setList(ArrayList<FrontendScheduledExperiment> list) {
		this.list = list;
	}

}
