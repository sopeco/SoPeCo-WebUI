package org.sopeco.frontend.shared.push;

import java.util.ArrayList;

import org.sopeco.frontend.client.rpc.PushRPC.Type;
import org.sopeco.frontend.shared.entities.FrontendScheduledExperiment;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ScheduledExperimentsPackage extends PushPackage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ArrayList<FrontendScheduledExperiment> attachement;

	public ScheduledExperimentsPackage() {
	}

	public ScheduledExperimentsPackage(Type type) {
		super(type);
	}

	public ScheduledExperimentsPackage(Type type, ArrayList<FrontendScheduledExperiment> pAttachement) {
		super(type);
		attachement = pAttachement;
	}

	public ArrayList<FrontendScheduledExperiment> getAttachement() {
		return attachement;
	}

	public void setAttachement(ArrayList<FrontendScheduledExperiment> pAttachement) {
		this.attachement = pAttachement;
	}

}
