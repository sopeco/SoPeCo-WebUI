package org.sopeco.frontend.shared.builder;

import java.util.logging.Logger;

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
	private static final Logger LOGGER = Logger.getLogger(MeasurementEnvironmentBuilder.class.getName());

	/**
	 * Creates an empty MEnvironmentDefinition.
	 * 
	 * @return
	 */
	public static MeasurementEnvironmentDefinition createBlankEnvironmentDefinition() {
		ScenarioDefinitionBuilder builder = new ScenarioDefinitionBuilder();
		return builder.getMEDefinition();
	}

	private ScenarioDefinitionBuilder scenarioBuilder;

	private ParameterNamespace lastCreatedNamespace = null;

	public MeasurementEnvironmentBuilder(ScenarioDefinitionBuilder sBuilder) {
		LOGGER.info("Creating a MeasurementEnvironmentBuilder");

		scenarioBuilder = sBuilder;
		scenarioBuilder.setMEDefinition(new MeasurementEnvironmentDefinition());

		setRootNamespace();
	}

	/**
	 * Adds a new namespace to the root namespace.
	 * 
	 * @param name
	 *            Name of the new namespace
	 * @return The new namespace
	 */
	public ParameterNamespace addNamespace(String name) {
		return addNamespace(name, scenarioBuilder.getMEDefinition().getRoot());
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
		LOGGER.info("adding new namespace '" + name + "' to parent '" + targetNamespace.getFullName() + "'");

		ParameterNamespace newNamepsace = SimpleEntityFactory.createNamespace(name);
		newNamepsace.setParent(targetNamespace);

		targetNamespace.getChildren().add(newNamepsace);

		lastCreatedNamespace = newNamepsace;

		return newNamepsace;
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
		LOGGER.info("adding new namespaces '" + path + "'");

		String[] nodes = path.split(DELIMITER);

		if (nodes.length == 0) {
			LOGGER.warning("no namespaces given");
			return null;
		}

		if (nodes.length == 1 || !nodes[0].equals(scenarioBuilder.getMEDefinition().getRoot().getName())) {
			LOGGER.warning("cant add an other root");

			return scenarioBuilder.getMEDefinition().getRoot();
		}

		ParameterNamespace currentNamespace = scenarioBuilder.getMEDefinition().getRoot();

		for (int i = 1; i < nodes.length - 1; i++) {
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
		LOGGER.info("adding new parameter '" + name + "' to namespace '" + namespace.getFullName() + "'");

		ParameterDefinition newParameter = SimpleEntityFactory.createParameterDefinition(name, type, role);

		newParameter.setNamespace(namespace);
		namespace.getParameters().add(newParameter);

		return newParameter;
	}

	/**
	 * Returns the build definition.
	 * 
	 * @return The build measurement environment definition
	 */
	public MeasurementEnvironmentDefinition getMEDefinition() {
		return scenarioBuilder.getMEDefinition();
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
		return getNamespace(path, DELIMITER);
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
	 */
	public ParameterNamespace getNamespace(String path, String delimiter) {
		LOGGER.info("Getting namespace by path '" + path + "'");

		if (path.length() > 1 && path.substring(0, 1).equals(delimiter)) {
			path = path.substring(1);
		}

		String[] nodes = path.split(delimiter);

		if (nodes.length <= 0) {
			LOGGER.warning("no nodes in array");
			return null;
		}

		int startIndex;
		
		if ( nodes.length == 1 && nodes[0].equals(getRootNamespace().getName()) ) {
			LOGGER.info("namespace is root!");
			return scenarioBuilder.getMEDefinition().getRoot();
		} else if ( nodes[0].equals(getRootNamespace().getName()) ) {
			startIndex = 1;
		} else {
			startIndex = 0;
		}
		
		ParameterNamespace currentNamespace = scenarioBuilder.getMEDefinition().getRoot();
		
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
		
//		if (!nodes[0].equals(getRootNamespace().getName())) {
//			LOGGER.warning("first namespace must be the root namespace");
//			return null;
//		} else if (nodes.length == 1) {
//			return scenarioBuilder.getMEDefinition().getRoot();
//		}
//
//		ParameterNamespace currentNamespace = scenarioBuilder.getMEDefinition().getRoot();
//
//		for (int i = 1; i < nodes.length; i++) {
//			if (currentNamespace.getChildren().size() <= 0) {
//				return null;
//			}
//
//			boolean found = false;
//			for (ParameterNamespace ns : currentNamespace.getChildren()) {
//				if (ns.getName().equals(nodes[i])) {
//					currentNamespace = ns;
//					found = true;
//					break;
//				}
//
//			}
//
//			if (found) {
//				continue;
//			}
//
//			return null;
//		}

		LOGGER.info("found namespace '" + currentNamespace.getFullName() + "'");

		return currentNamespace;
	}

	/**
	 * Search and return the parameter with the given name.
	 * 
	 * @param name
	 * @param namespace
	 * @return
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
	 * Returns the root namespace of the measurement environment.
	 * 
	 * @return root namespace.
	 */
	public ParameterNamespace getRootNamespace() {
		return scenarioBuilder.getMEDefinition().getRoot();
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
		LOGGER.info("removing namespace '" + namespace.getFullName() + "' // appendChildrenToParent: "
				+ appendChildrenToParent);

		if (namespace.getName().equals(ROOTNAME) || namespace.getParent() == null) {
			LOGGER.warning("root namespace can not be removed.");
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
	 * Removes the given parameter.
	 * 
	 * @param parameter
	 *            parameter you want to remove
	 * @return was the parameter successful removed
	 */
	public boolean removeParameter(ParameterDefinition parameter) {
		LOGGER.info("removing parameter '" + parameter.getName() + "' from namespace '"
				+ parameter.getNamespace().getFullName() + "'");

		ParameterNamespace parent = parameter.getNamespace();

		return parent.getParameters().remove(parameter);
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
		LOGGER.info("removing parameter '" + name + "' from namespace '" + namespace.getFullName() + "'");

		ParameterDefinition parameter = namespace.getParameter(name);

		return namespace.getParameters().remove(parameter);
	}

	/**
	 * Renames the given namespace to the given name.
	 * 
	 * @param namespace
	 * @param newName
	 */
	public void renameNamespace(ParameterNamespace namespace, String newName) {
		namespace.setName(newName);
	}

	/**
	 * Setting the root namespace to defaut.
	 */
	private void setRootNamespace() {
		LOGGER.info("Setting the default root namespace.");

		ParameterNamespace rootNamespace = SimpleEntityFactory.createNamespace(ROOTNAME);

		scenarioBuilder.getMEDefinition().setRoot(rootNamespace);
	}
}
