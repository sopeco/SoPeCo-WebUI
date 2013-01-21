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
package org.sopeco.frontend.client.helper;

import org.sopeco.frontend.shared.helper.Metering;
import org.sopeco.persistence.entities.definition.MeasurementEnvironmentDefinition;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterNamespace;
import org.sopeco.persistence.entities.definition.ParameterRole;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class EnvironmentFilter {

	private boolean filterObservation;
	private boolean filterEmptyNamespaces;

	public EnvironmentFilter() {
		filterObservation = false;
		filterEmptyNamespaces = false;
	}

	/**
	 * @return the filterObservation
	 */
	public boolean isFilterObservation() {
		return filterObservation;
	}

	/**
	 * @param observation
	 *            the filterObservation to set
	 */
	public void setFilterObservation(boolean observation) {
		this.filterObservation = observation;
	}

	/**
	 * @return the filterEmptyNamespaces
	 */
	public boolean isFilterEmptyNamespaces() {
		return filterEmptyNamespaces;
	}

	/**
	 * @param emptyNamespaces
	 *            the filterEmptyNamespaces to set
	 */
	public void setFilterEmptyNamespaces(boolean emptyNamespaces) {
		this.filterEmptyNamespaces = emptyNamespaces;
	}

	/**
	 * Filters the given definition with the set properties and returns the
	 * filtered definition.
	 * 
	 * @param source
	 * @return
	 */
	public MeasurementEnvironmentDefinition filter(MeasurementEnvironmentDefinition source) {
		double metering = Metering.start();

		ParameterNamespace root = recursive(source.getRoot());

		MeasurementEnvironmentDefinition filteredDefinition = new MeasurementEnvironmentDefinition();
		filteredDefinition.setRoot(root);

		Metering.stop(metering);
		return filteredDefinition;
	}

	/**
	 * Duplicates the MEDefinition and filters out the desired elements.
	 * 
	 * @param sourceNs
	 * @return
	 */
	private ParameterNamespace recursive(ParameterNamespace sourceNs) {
		ParameterNamespace targetNs = new ParameterNamespace();
		targetNs.setName(sourceNs.getName());

		for (ParameterDefinition definition : sourceNs.getParameters()) {
			// FILTER: filterObservation
			if (!filterObservation || definition.getRole() != ParameterRole.OBSERVATION) {
				targetNs.getParameters().add(definition);
			}
		}

		for (ParameterNamespace namespace : sourceNs.getChildren()) {
			ParameterNamespace child = recursive(namespace);
			// FILTER: filterEmptyNamespaces
			if (!filterEmptyNamespaces || !child.getAllParameters().isEmpty()) {
				targetNs.getChildren().add(child);
			}
		}

		return targetNs;
	}
}
