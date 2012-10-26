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
