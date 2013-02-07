/**
 * Copyright (c) 2013 SAP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the SAP nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL SAP BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.sopeco.webui.client.layout.center.experiment;

import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.webui.client.layout.environment.EnvTreeItem;
import org.sopeco.webui.client.layout.environment.EnvironmentTree;
import org.sopeco.webui.client.manager.ScenarioManager;
import org.sopeco.webui.client.resources.R;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ExperimentEnvironmentTree extends EnvironmentTree {

	public ExperimentEnvironmentTree() {
		super(true);

		getView().setFirstInfoText(R.get("addToExpAssignments"));
		getView().setSecondInfoText(R.get("addToPrepAssignments"));

	}

	@Override
	public boolean isFirstChecked(ParameterDefinition parameter) {
		return ScenarioManager.get().experiment().isExperimentAssignment(parameter);
	}

	@Override
	public boolean isSecondChecked(ParameterDefinition parameter) {
		return ScenarioManager.get().experiment().isPreperationAssignment(parameter);
	}

	@Override
	public void onClick(ECheckBox checkbox, EnvTreeItem item, boolean value) {
		if (checkbox == ECheckBox.FIRST) {
			if (value) {
				ScenarioManager.get().experiment().addExperimentAssignment(item.getParameter());
			} else {
				ScenarioManager.get().experiment().removeExperimentAssignment(item.getParameter());
			}
		} else {
			if (value) {
				ScenarioManager.get().experiment().addPreperationAssignment(item.getParameter());
			} else {
				ScenarioManager.get().experiment().removePreperationAssignment(item.getParameter());
			}
		}
	}

}
