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
package org.sopeco.webui.client.layout.center.specification;

import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.webui.client.event.EventControl;
import org.sopeco.webui.client.event.InitialAssignmentChangedEvent;
import org.sopeco.webui.client.event.InitialAssignmentChangedEvent.ChangeType;
import org.sopeco.webui.client.layout.environment.EnvTreeItem;
import org.sopeco.webui.client.layout.environment.EnvironmentTree;
import org.sopeco.webui.client.manager.ScenarioManager;
import org.sopeco.webui.client.resources.R;

import com.google.gwt.core.client.GWT;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class SpecificationEnvironmentTree extends EnvironmentTree {

	public SpecificationEnvironmentTree() {
		super(false, R.get("envParameter"));
		
		getView().setFirstInfoText(R.get("addToInitAssignments"));
	}

	@Override
	public void onClick(ECheckBox checkbox, EnvTreeItem item, boolean value) {
		ChangeType type;
		if (value) {
			type = ChangeType.Added;
		} else {
			type = ChangeType.Removed;
		}

		GWT.log("add: " + item.getPath());
		InitialAssignmentChangedEvent changeEvent = new InitialAssignmentChangedEvent(item.getPath(), type);
		EventControl.get().fireEvent(changeEvent);
	}

	@Override
	public boolean isFirstChecked(ParameterDefinition parameter) {
		return ScenarioManager.get().getBuilder().getSpecificationBuilder().containsInitialAssignment(parameter);
	}

	@Override
	public boolean isSecondChecked(ParameterDefinition parameter) {
		return false;
	}
}
