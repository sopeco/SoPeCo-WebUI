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
package org.sopeco.webui.shared.builder;

import java.io.Serializable;
import java.util.logging.Logger;

import org.sopeco.persistence.entities.definition.MeasurementEnvironmentDefinition;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterNamespace;
import org.sopeco.persistence.entities.definition.ParameterRole;
import org.sopeco.service.configuration.ServiceConfiguration;

/**
 * Builder for the {@code MeasurementEnvironmentDefinition}. <br />
 * For a better reading, MeasurementEnvironmentDefinition is shorten MED.
 * 
 * @author Marius Oehler
 * @author Peter Merkert
 */
public class MeasurementEnvironmentDefinitionBuilder implements Serializable {

	private static final Logger LOGGER = Logger.getLogger(MeasurementEnvironmentDefinitionBuilder.class.getName());
	
	private static final long serialVersionUID = 1L;
	/**
	 * The {@code ScenarioDefinitionBuilder} this builder is connected to.
	 */
	private ScenarioDefinitionBuilder scenarioDefinitionBuilder;
	
	/**
	 * Creates a new {@code MeasurementEnvironmentDefinitionBuilder} with the given {@code ScenarioDefinitionBuilder}. <br />
	 * A new {@code MeasurementEnvironmentDefinition} is created and added to the
	 * given {@code ScenarioDefinitionBuilder}. As well as a new {@code ParameterNamespace} is created and
	 * appended to the {@code ScenarioDefinitionBuilder}.
	 * 
	 * @param scenarioDefinitionBuilder the {@code ScenarioDefinitionBuilder} for this builder
	 */
	public MeasurementEnvironmentDefinitionBuilder(ScenarioDefinitionBuilder scenarioDefinitionBuilder) {
		LOGGER.info("Creating a new MeasurementEnvironmentDefinitionBuilder");

		this.scenarioDefinitionBuilder = scenarioDefinitionBuilder;
		MeasurementEnvironmentDefinition med = new MeasurementEnvironmentDefinition();
		this.scenarioDefinitionBuilder.setMeasurementEnvironmentDefinition(med);
		
		// set the default namespace
		ParameterNamespace ns = SimpleEntityFactory.createNamespace(ServiceConfiguration.MEASUREMENTENVIRONMENT_ROOTNAME);
		this.scenarioDefinitionBuilder.getMeasurementEnvironmentDefinition().setRoot(ns);
	}
	
	/**
	 * Creates an empty MED.
	 * 
	 * @return an empty MED
	 */
	public static MeasurementEnvironmentDefinition createBlankEnvironmentDefinition() {
		ScenarioDefinitionBuilder builder = new ScenarioDefinitionBuilder();
		return builder.getMeasurementEnvironmentDefinition();
	}

	/**
	 * Adds a new namespace to the root namespace.
	 * 
	 * @param namespaceName name of the new namespace
	 * @return 				the new namespace
	 */
	public ParameterNamespace addNamespace(String namespaceName) {
		return addNamespace(namespaceName, scenarioDefinitionBuilder.getMeasurementEnvironmentDefinition().getRoot());
	}

	/**
	 * Adds a new namespace to the target namespace.
	 * 
	 * @param namespaceName 	name of the new namespace
	 * @param targetNamespace 	namespace which gets the new namespace
	 * @return 					the new namespace
	 */
	public ParameterNamespace addNamespace(String namespaceName, ParameterNamespace targetNamespace) {
		LOGGER.info("Adding new namespace '" + namespaceName + "' to parent '" + targetNamespace.getFullName() + "'.");

		// the namespaces are stored in a tree 
		ParameterNamespace newNamespace = SimpleEntityFactory.createNamespace(namespaceName);
		newNamespace.setParent(targetNamespace);
		targetNamespace.getChildren().add(newNamespace);

		return newNamespace;
	}

	/**
	 * Adds all namespaces of the given path if they aren't exist already. The
	 * path will be seperated by the delimiter '/' and ARE be relative to the
	 * root! So the root must be given in the path. E.g.: "/first/second/third"
	 * will be afterwards "root/first/second/third".
	 * 
	 * @param path nodes that will be added
	 * @return The last namespace created with the path
	 */
	public ParameterNamespace addNamespaces(String path) {
		LOGGER.info("adding new namespaces '" + path + "'");

		String[] nodes = path.split(ServiceConfiguration.MEASUREMENTENVIRONMENT_DELIMITER);

		if (nodes.length == 0) {
			LOGGER.warning("no namespaces given");
			return null;
		}

		ParameterNamespace currentNamespace = scenarioDefinitionBuilder.getMeasurementEnvironmentDefinition().getRoot();

		for (int i = 1; i < nodes.length - 1; i++) {
			
			// check if a namespace in the current level with the same name already exists
			boolean found = false;
			
			for (ParameterNamespace ns : currentNamespace.getChildren()) {
				if (ns.getName().equals(nodes[i])) {
					currentNamespace = ns;
					found = true;
					break;
				}
			}

			if (found) {
				continue;
			}

			currentNamespace = addNamespace(nodes[i], currentNamespace);
		}

		currentNamespace = addNamespace(nodes[nodes.length - 1], currentNamespace);
		
		return currentNamespace;
	}

	/**
	 * Adding a new parameter to the given namespace.
	 * 
	 * @param name 		name of the new parameter
	 * @param type 		type of the new parameter
	 * @param role 		role of the new parameter
	 * @param namespace namespace where the parameter will be added
	 * @return 			the ParameterDefinition created with the given parameter
	 */
	public ParameterDefinition addParameter(String name, String type, ParameterRole role, ParameterNamespace namespace) {
		if (namespace == null) {
			LOGGER.warning("The given namespace is NULL.");
			return null;
		}
		
		LOGGER.info("Adding new parameter '" + name + "' to namespace '" + namespace.getFullName() + "'.");

		ParameterDefinition newParameter = SimpleEntityFactory.createParameterDefinition(name, type, role);

		newParameter.setNamespace(namespace);
		namespace.getParameters().add(newParameter);

		return newParameter;
	}

	/**
	 * @return a MeasurementEnvironmentDefinition
	 * 
	 * @Deprecated Use getMeasurementEnvironmentDefinition().
	 */
	@Deprecated
	public MeasurementEnvironmentDefinition getMEDefinition() {
		return getMeasurementEnvironmentDefinition();
	}
	
	/**
	 * Returns the current {@code MeasurementEnvironmentDefinition} of
	 * the {@code SscenarioDefinitionBuilder}.
	 * 
	 * @return current {@code MeasurementEnvironmentDefinition}
	 */
	public MeasurementEnvironmentDefinition getMeasurementEnvironmentDefinition() {
		return scenarioDefinitionBuilder.getMeasurementEnvironmentDefinition();
	}

	/**
	 * Returns the namespace specified by the path. The path will be seperated
	 * by the global delimiter (normally '/') and every node representing a namespace.
	 * <br />
	 * <br />
	 * The given path can start with name of the root namespace (default "root",
	 * but don't need to.
	 * 
	 * @param path path to the namespace
	 * @return searched namespace, null if namespace not found
	 */
	public ParameterNamespace getNamespace(String path) {
		String delimiter = ServiceConfiguration.MEASUREMENTENVIRONMENT_DELIMITER;
		
		LOGGER.info("Getting namespace by path '" + path + "'");

		// shorten a beginning "/"
		if (path.length() > 1 && path.substring(0, 1).equals(delimiter)) {
			path = path.substring(1);
		}

		String[] nodes = path.split(delimiter);

		if (nodes.length <= 0) {
			LOGGER.warning("No nodes in path array.");
			return null;
		}

		int startIndex;

		if (nodes.length == 1 && nodes[0].equals(getRootNamespace().getName())) {
			LOGGER.finer("Namespace is the root namespace!");
			return scenarioDefinitionBuilder.getMeasurementEnvironmentDefinition().getRoot();
		} else if (nodes[0].equals(getRootNamespace().getName())) {
			startIndex = 1;
		} else {
			startIndex = 0;
		}

		ParameterNamespace currentNamespace = scenarioDefinitionBuilder.getMeasurementEnvironmentDefinition().getRoot();

		// depth search for the given path in the namespace tree
		for (int i = startIndex; i < nodes.length; i++) {
			
			if (currentNamespace.getChildren().size() <= 0) {
				return null;
			}

			boolean found = false;
			for (ParameterNamespace ns : currentNamespace.getChildren()) {
				if (ns.getName().equals(nodes[i])) {
					currentNamespace = ns;
					found = true;
					break;
				}

			}

			if (found) {
				continue;
			}

			return null;
		}

		LOGGER.info("Found namespace '" + currentNamespace.getFullName() + "'.");

		return currentNamespace;
	}

	/**
	 * Returns the namespace specified by the path. The path will be seperated
	 * by the given delimiter and every node representing a namespace.
	 * 
	 * @param path
	 *            path to namespace
	 * @param delimiter
	 *            the delimiter of the nodes
	 * @return searched namespace
	 * @Deprecated Only use the globally definied delimiter.
	 */
	@Deprecated
	public ParameterNamespace getNamespace(String path, String delimiter) {
		return getNamespace(path);
	}

	/**
	 * Search and return the parameter with the given name. The return type is
	 * the {@code ParameterDefinition}.
	 * 
	 * @param name the parameter name
	 * @param namespace the namespace in which the parameter is searched
	 * @return the {@code ParameterDefinition}, null if search failed
	 */
	public ParameterDefinition getParameter(String name, ParameterNamespace namespace) {
		for (ParameterDefinition parameter : namespace.getParameters()) {
			if (parameter.getName().equals(name)) {
				return parameter;
			}
		}
		return null;
	}

	/**
	 * Returns the root namespace of the {@code MeasurementEnvironmentDefinition}.
	 * 
	 * @return root namespace reference
	 */
	public ParameterNamespace getRootNamespace() {
		return scenarioDefinitionBuilder.getMeasurementEnvironmentDefinition().getRoot();
	}

	/**
	 * Removes a namespace and all children from the {@code MeasurementEnvironmentDefinition}.
	 * 
	 * @param namespace namespace which will be removed
	 * @return true, if the removal was succesful
	 */
	public boolean removeNamespace(ParameterNamespace namespace) {
		return removeNamespace(namespace, false);
	}

	/**
	 * Removes a namespace from the {@code MeasurementEnvironmentDefinition}.
	 * The appended children will be sticked to the parent {@code ParameterNamespace} for the given
	 * namespace.
	 * 
	 * @param namespace namespace which will be removed
	 * @param appendChildrenToParent true, if children namespaces are attached to the parent
	 * @return true, if the removal was succesful
	 */
	public boolean removeNamespace(ParameterNamespace namespace, boolean appendChildrenToParent) {
		LOGGER.finer("removing namespace '" + namespace.getFullName() + "'"
				     + "// appendChildrenToParent: " + appendChildrenToParent);

		if (namespace.getName().equals(ServiceConfiguration.MEASUREMENTENVIRONMENT_ROOTNAME) || namespace.getParent() == null) {
			LOGGER.warning("Operation forbidden: Root namespace can not be removed.");
			return false;
		}

		if (appendChildrenToParent) {
			for (ParameterNamespace child : namespace.getChildren()) {
				child.setParent(namespace.getParent());
				namespace.getParent().getChildren().add(child);
			}
			namespace.getChildren().clear();
		}

		namespace.getParent().getChildren().remove(namespace);

		return true;
	}

	/**
	 * Removes the given parameter.
	 * 
	 * @param parameter parameter to remove
	 * @return true, if the parameter deletion was successful
	 */
	public boolean removeParameter(ParameterDefinition parameter) {
		LOGGER.info("Removing parameter '" + parameter.getName() + "' from namespace '"
				    + parameter.getNamespace().getFullName() + "'.");

		ParameterNamespace parent = parameter.getNamespace();

		return parent.getParameters().remove(parameter);
	}

	/**
	 * Removes a parameter from the given namespace.
	 * 
	 * @param name parameter you want to remove
	 * @param namespace namespace where the parameter is located
	 * @return true, if the parameter removal was succesful
	 */
	public boolean removeParameter(String name, ParameterNamespace namespace) {
		LOGGER.info("Removing parameter '" + name + "' from namespace '" + namespace.getFullName() + "'.");

		ParameterDefinition parameter = namespace.getParameter(name);

		return namespace.getParameters().remove(parameter);
	}

	/**
	 * Renames the given namespace to the new passed name.
	 * 
	 * @param namespace the namespace to rename
	 * @param newName the new name for the namespace
	 */
	public void renameNamespace(ParameterNamespace namespace, String newName) {
		namespace.setName(newName);
	}

}
