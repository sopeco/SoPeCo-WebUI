package org.sopeco.frontend.server.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.persistence.EntityFactory;
import org.sopeco.persistence.entities.definition.MeasurementEnvironmentDefinition;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterNamespace;
import org.sopeco.persistence.entities.definition.ParameterRole;

/**
 * Builder for the measurement environment.
 * 
 * @author Marius Oehler
 * 
 */
public class MeasurementEnvironmentBuilder {

	/**
	 * name of the root namespace.
	 */
	private static final String ROOTNAME = "root";
	/**
	 * Default delimiter which seperates the "paths".
	 */
	private static final String DELIMITER = "/";
	private static final Logger LOGGER = LoggerFactory.getLogger(MeasurementEnvironmentBuilder.class);
	private MeasurementEnvironmentDefinition definiton;
	private ParameterNamespace lastCreatedNamespace = null;

	public MeasurementEnvironmentBuilder() {
		LOGGER.debug("Creating a MeasurementEnvironmentBuilder");

		definiton = new MeasurementEnvironmentDefinition();

		setRootNamespace();
	}

	/**
	 * Adds a new namespace to the target namespace.
	 * 
	 * @param name
	 *            Name of the new namespace
	 * @param targetNamespace
	 *            Namespace which gets the new namespace
	 * @return The new namespace
	 */
	public ParameterNamespace addNamespace(String name, ParameterNamespace targetNamespace) {
		LOGGER.debug("adding new namespace '" + name + "' to parent '" + targetNamespace.getFullName() + "'");

		ParameterNamespace newNamepsace = EntityFactory.createNamespace(name);
		newNamepsace.setParent(targetNamespace);

		targetNamespace.getChildren().add(newNamepsace);

		lastCreatedNamespace = newNamepsace;

		return newNamepsace;
	}

	/**
	 * Adds a new namespace to the root namespace.
	 * 
	 * @param name
	 *            Name of the new namespace
	 * @return The new namespace
	 */
	public ParameterNamespace addNamespace(String name) {
		return addNamespace(name, definiton.getRoot());
	}

	/**
	 * Adds all namespaces of the given path if they aren't exit already. The
	 * path will be seperated by the delimiter '/' and MUST be relative to the
	 * root! So the root is not given in the path. E.g.: "first/second/third"
	 * will create: root / first / second / third
	 * 
	 * @param path
	 *            Nodes that will be added
	 * @return The last namespace of the path
	 */
	public ParameterNamespace addNamespaces(String path) {
		LOGGER.debug("adding new namespaces '" + path + "'");

		String[] nodes = path.split(DELIMITER);

		if (nodes.length == 0) {
			LOGGER.warn("no namespaces given");
			return null;
		}

		ParameterNamespace currentNamespace = definiton.getRoot();

		for (int i = 0; i < nodes.length - 1; i++) {
			for (ParameterNamespace ns : currentNamespace.getChildren()) {
				if (ns.getName().equals(nodes[i])) {
					currentNamespace = ns;
					break;
				}
			}

			currentNamespace = addNamespace(nodes[i], currentNamespace);
		}

		currentNamespace = addNamespace(nodes[nodes.length - 1], currentNamespace);

		return currentNamespace;
	}

	/**
	 * Removes a namespace and all children from the ME.
	 * 
	 * @param namespace
	 *            namespace which will be removed
	 * @return was the removal successful
	 */
	public boolean removeNamespace(ParameterNamespace namespace) {
		return removeNamespace(namespace, false);
	}

	/**
	 * Removes a namespace from the ME.
	 * 
	 * @param namespace
	 *            namespace which will be removed
	 * @param appendChildrenToParent
	 *            should the child namespaces are removed or attached to the
	 *            parent
	 * @return was the removal successful
	 */
	public boolean removeNamespace(ParameterNamespace namespace, boolean appendChildrenToParent) {
		LOGGER.debug("removing namespace '" + namespace.getFullName() + "' // appendChildrenToParent: "
				+ appendChildrenToParent);

		if (namespace.getName().equals(ROOTNAME) || namespace.getParent() == null) {
			LOGGER.warn("root namespace can not be removed.");
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

		if (namespace == lastCreatedNamespace) {
			lastCreatedNamespace = null;
		}

		return true;
	}

	/**
	 * Setting the root namespace to defaut.
	 */
	private void setRootNamespace() {
		LOGGER.debug("Setting the default root namespace.");

		ParameterNamespace rootNamespace = EntityFactory.createNamespace(ROOTNAME);

		definiton.setRoot(rootNamespace);
	}

	/**
	 * Returns the root namespace of the measurement environment.
	 * 
	 * @return root namespace.
	 */
	public ParameterNamespace getRootNamespace() {
		return definiton.getRoot();
	}

	/**
	 * Returns the namespace specified by the path. The path will be seperated
	 * by the delimiter '/' and every node representing a namespace.
	 * 
	 * @param path
	 *            path to namespace
	 * @return searched namespace
	 */
	public ParameterNamespace getNamespace(String path) {
		LOGGER.debug("Getting namespace by path '" + path + "'");

		if (path.length() > 1 && path.substring(0, 1).equals(DELIMITER)) {
			path = path.substring(1);
		}

		String[] nodes = path.split(DELIMITER);

		if (nodes.length <= 0) {
			return null;
		}

		if (!nodes[0].equals(ROOTNAME)) {
			LOGGER.warn("first namespace must be the root namespace");
			return null;
		} else if (nodes.length == 1) {
			return definiton.getRoot();
		}

		ParameterNamespace currentNamespace = definiton.getRoot();

		for (int i = 1; i < nodes.length; i++) {
			for (ParameterNamespace ns : currentNamespace.getChildren()) {
				if (ns.getName().equals(nodes[i])) {
					currentNamespace = ns;
					break;
				}
				return null;
			}
		}

		LOGGER.debug("found namespace '" + currentNamespace.getFullName() + "'");

		return currentNamespace;
	}

	/**
	 * Adding a new parameter to the last created namespace.
	 * 
	 * @param name
	 *            name of the new parameter
	 * @param type
	 *            type of the new parameter
	 * @param role
	 *            role of the new parameter
	 */
	public void addParameter(String name, String type, ParameterRole role) {
		addParameter(name, type, role, lastCreatedNamespace);
	}

	/**
	 * Adding a new parameter to the given namespace.
	 * 
	 * @param name
	 *            name of the new parameter
	 * @param type
	 *            type of the new parameter
	 * @param role
	 *            role of the new parameter
	 * @param namespace
	 *            namespace where the parameter will be added
	 */
	public ParameterDefinition addParameter(String name, String type, ParameterRole role, ParameterNamespace namespace) {
		LOGGER.debug("adding new parameter '" + name + "' to namespace '" + namespace.getFullName() + "'");

		ParameterDefinition newParameter = EntityFactory.createParameterDefinition(name, type, role);

		newParameter.setNamespace(namespace);
		namespace.getParameters().add(newParameter);

		return newParameter;
	}

	/**
	 * Removes a parameter from the namespace.
	 * 
	 * @param name
	 *            parameter you want to remove
	 * @param namespace
	 *            namespace where's the parameter located
	 * @return was the parameter successful removed
	 */
	public boolean removeParameter(String name, ParameterNamespace namespace) {
		LOGGER.debug("removing parameter '" + name + "' from namespace '" + namespace.getFullName() + "'");

		ParameterDefinition parameter = namespace.getParameter(name);

		return namespace.getParameters().remove(parameter);
	}

	/**
	 * Removes the given parameter.
	 * 
	 * @param parameter
	 *            parameter you want to remove
	 * @return was the parameter successful removed
	 */
	public boolean removeParameter(ParameterDefinition parameter) {
		LOGGER.debug("removing parameter '" + parameter.getName() + "' from namespace '"
				+ parameter.getNamespace().getFullName() + "'");

		ParameterNamespace parent = parameter.getNamespace();

		return parent.getParameters().remove(parameter);
	}

	/**
	 * Returns the build definition.
	 * 
	 * @return The build measurement environment definition
	 */
	public MeasurementEnvironmentDefinition getMEDefinition() {
		return definiton;
	}
}
