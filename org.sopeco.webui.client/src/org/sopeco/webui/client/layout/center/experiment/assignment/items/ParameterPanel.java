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
package org.sopeco.webui.client.layout.center.experiment.assignment.items;

import java.util.Map;

import org.sopeco.persistence.entities.definition.DynamicValueAssignment;
import org.sopeco.persistence.entities.definition.ParameterValueAssignment;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
public abstract class ParameterPanel extends FlowPanel {

	private static final String PANEL_CSS_CLASS = "parameterPanel";
	private AssignmentItem assignment;

	public ParameterPanel(AssignmentItem item) {
		assignment = item;

		addStyleName(PANEL_CSS_CLASS);
	}

	public AssignmentItem getAssignmentItem() {
		return assignment;
	}

	public abstract ParameterValueAssignment getValueAssignment();

	/**
	 * Returns the configuration map of the DynamicValueAssignment which is
	 * stored in the assignment item.
	 * 
	 * @return
	 */
	protected Map<String, String> getDVAConfiguration() {
		if (!(assignment.getAssignment() instanceof DynamicValueAssignment)) {
			throw new IllegalStateException(
					"The ParameterValueAssignment is not an instance of DynamicValueAssignment.");
		}
		return ((DynamicValueAssignment) assignment.getAssignment()).getConfiguration();
	}

	/**
	 * Creates a DynamicValueAssignment with the current parameter and
	 * configuration map.
	 * 
	 * @return
	 */
	protected DynamicValueAssignment createDynamicValueAssignment() {
		if (!(assignment.getAssignment() instanceof DynamicValueAssignment)) {
			throw new IllegalStateException(
					"The ParameterValueAssignment is not an instance of DynamicValueAssignment.");
		}
		DynamicValueAssignment dva = new DynamicValueAssignment();
		dva.setName(getAssignmentItem().getCurrentVariationName());
		dva.setParameter(getAssignmentItem().getAssignment().getParameter());
		dva.getConfiguration().clear();
		dva.getConfiguration().putAll(getDVAConfiguration());
		return dva;
	}
}
