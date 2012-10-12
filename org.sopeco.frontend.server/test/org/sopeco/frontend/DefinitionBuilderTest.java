package org.sopeco.frontend;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.sopeco.frontend.server.model.MeasurementEnvironmentBuilder;
import org.sopeco.frontend.server.model.MeasurementSpecificationBuilder;
import org.sopeco.frontend.server.model.ScenarioDefinitionBuilder;
import org.sopeco.persistence.EntityFactory;
import org.sopeco.persistence.entities.definition.ConstantValueAssignment;
import org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition;
import org.sopeco.persistence.entities.definition.ExperimentTerminationCondition;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterNamespace;
import org.sopeco.persistence.entities.definition.ParameterRole;

/**
 * Unit tests of the scenarion, me and ms builder.
 * 
 * @author Marius Oehler
 * 
 */
public class DefinitionBuilderTest {

	/**
	 * Test-String.
	 */
	public static final String TESTNAME = "test123";

	@Test
	public void buildScenarionDefinition() {
		ScenarioDefinitionBuilder builder = new ScenarioDefinitionBuilder();

		String name = ScenarioDefinitionBuilder.buildEmptyScenario(TESTNAME).getScenarioName();

		assertEquals(name, TESTNAME);

		builder.setScenarioName(TESTNAME);

		assertEquals(builder.getBuiltScenario().getScenarioName(), TESTNAME);
	}

	@Test
	public void buildMEDefinition() {
		ScenarioDefinitionBuilder builder = new ScenarioDefinitionBuilder();
		assertEquals(builder.getMEDefinition().getRoot().getName(), "root");
	}

	@Test
	public void addParameterNamespaceToME() {
		MeasurementEnvironmentBuilder builder = new ScenarioDefinitionBuilder().getEnvironmentBuilder();

		ParameterNamespace newNamespace = builder.addNamespace(TESTNAME);

		boolean result = false;
		for (ParameterNamespace ns : builder.getRootNamespace().getChildren()) {
			if (ns.getName().equals(TESTNAME)) {
				result = true;
				break;
			}
		}

		assertTrue(result);

		builder.addNamespace(TESTNAME + "2", newNamespace);

		result = false;
		for (ParameterNamespace ns : newNamespace.getChildren()) {
			if (ns.getName().equals(TESTNAME + "2")) {
				result = true;
				break;
			}
		}

		assertTrue(result);

		builder.addNamespaces("first/second/third");
	}

	@Test
	public void removeParameterNamespaceOfME() {
		MeasurementEnvironmentBuilder builder = new ScenarioDefinitionBuilder().getEnvironmentBuilder();
		ParameterNamespace newNamespace = builder.addNamespace(TESTNAME);
		ParameterNamespace newSecond = builder.addNamespace(TESTNAME + "2", newNamespace);

		assertFalse(builder.removeNamespace(builder.getRootNamespace()));
		assertFalse(builder.removeNamespace(builder.getRootNamespace(), true));
		assertFalse(builder.removeNamespace(builder.getRootNamespace(), false));

		boolean result = builder.removeNamespace(newNamespace, true);
		assertTrue(result);

		result = false;
		for (ParameterNamespace ns : builder.getRootNamespace().getChildren()) {
			if (ns.getName().equals(TESTNAME + "2")) {
				result = true;
				break;
			}
		}
		assertTrue(result);

		builder.addNamespace(TESTNAME, newSecond);
		builder.removeNamespace(newSecond);

		assertTrue(builder.getRootNamespace().getChildren().isEmpty());
	}

	@Test
	public void testAddingAndRemovingByPath() {
		MeasurementEnvironmentBuilder builder = new ScenarioDefinitionBuilder().getEnvironmentBuilder();

		builder.addNamespaces("first/second/third");
		assertNull(builder.getNamespace("root/first/second/third"));
		
		builder.addNamespaces("root/first/second/third");

		assertNull(builder.getNamespace("root/first/second/thi"));
		assertNull(builder.getNamespace("rt/first/second/third"));

		assertEquals(builder.getRootNamespace(), builder.getNamespace("/root/"));

		String path = builder.getNamespace("root/first/second/third").getFullName();

		assertEquals(path, "root.first.second.third");
	}

	@Test
	public void addParameterDefinition() {
		MeasurementEnvironmentBuilder builder = new ScenarioDefinitionBuilder().getEnvironmentBuilder();

		ParameterNamespace newNamespace = builder.addNamespace(TESTNAME);

		builder.addParameter("First", "FirstType", ParameterRole.INPUT);
		builder.addParameter("Second", "SecondType", ParameterRole.OBSERVATION, newNamespace);

		assertEquals(newNamespace.getAllParameters().size(), 2);

		assertEquals(newNamespace.getAllParameters().get(0).getName(), "First");
		assertEquals(newNamespace.getAllParameters().get(0).getType(), "FirstType");
		assertEquals(newNamespace.getAllParameters().get(0).getRole(), ParameterRole.INPUT);

		assertEquals(newNamespace.getAllParameters().get(1).getName(), "Second");
		assertEquals(newNamespace.getAllParameters().get(1).getType(), "SecondType");
		assertEquals(newNamespace.getAllParameters().get(1).getRole(), ParameterRole.OBSERVATION);
	}

	@Test
	public void removingParameter() {
		MeasurementEnvironmentBuilder builder = new ScenarioDefinitionBuilder().getEnvironmentBuilder();

		ParameterNamespace newNamespace = builder.addNamespace(TESTNAME);

		builder.addParameter("First", "FirstType", ParameterRole.INPUT);
		builder.addParameter("Second", "SecondType", ParameterRole.OBSERVATION, newNamespace);

		assertEquals(newNamespace.getAllParameters().size(), 2);

		assertFalse(builder.removeParameter("Third", newNamespace));
		assertTrue(builder.removeParameter("Second", newNamespace));

		assertEquals(newNamespace.getAllParameters().size(), 1);
	}

	@Test
	public void buildSpecification() {
		ScenarioDefinitionBuilder sdb = new ScenarioDefinitionBuilder();
		MeasurementSpecificationBuilder builder = new MeasurementSpecificationBuilder(sdb, TESTNAME);
		MeasurementSpecificationBuilder builder2 = new MeasurementSpecificationBuilder(sdb);
		builder2.setName(TESTNAME);

		assertEquals(builder.getBuiltSpecification().getName(), TESTNAME);
		assertEquals(builder2.getBuiltSpecification().getName(), TESTNAME);
	}

	@Test
	public void addInitAssignments() {
		ScenarioDefinitionBuilder sdb = new ScenarioDefinitionBuilder();
		
		MeasurementSpecificationBuilder builder = new MeasurementSpecificationBuilder(sdb, TESTNAME);

		ParameterDefinition para = EntityFactory.createParameterDefinition("First", "FirstType", ParameterRole.INPUT);

		assertEquals(builder.getBuiltSpecification().getInitializationAssignemts().size(), 0);

		assertTrue(builder.addInitAssignment(para, "FirstVal"));

		assertEquals(builder.getBuiltSpecification().getInitializationAssignemts().size(), 1);
		assertEquals(builder.getBuiltSpecification().getInitializationAssignemts().get(0).getValue(), "FirstVal");
		assertEquals(builder.getBuiltSpecification().getInitializationAssignemts().get(0).getParameter(), para);

		assertTrue(builder.removeInitialAssignment(para));

		assertEquals(builder.getBuiltSpecification().getInitializationAssignemts().size(), 0);

		ConstantValueAssignment cva = EntityFactory.createConstantValueAssignment(para, "vvvv");

		assertTrue(builder.addInitAssignment(cva));
		assertEquals(builder.getBuiltSpecification().getInitializationAssignemts().size(), 1);

		// Parameter(fullname) must be unique in assignment list
		assertFalse(builder.addInitAssignment(cva));
		assertEquals(builder.getBuiltSpecification().getInitializationAssignemts().size(), 1);

		assertEquals(builder.getBuiltSpecification().getInitializationAssignemts().get(0).getValue(), "vvvv");
		assertEquals(builder.getBuiltSpecification().getInitializationAssignemts().get(0).getParameter(), para);

		assertTrue(builder.removeInitialAssignment(cva));

		assertEquals(builder.getBuiltSpecification().getInitializationAssignemts().size(), 0);
	}

	@Test
	public void addExperimentSeriesDefinition() {
		ScenarioDefinitionBuilder sdb = new ScenarioDefinitionBuilder();
		
		MeasurementSpecificationBuilder builder = new MeasurementSpecificationBuilder(sdb, TESTNAME);

//		ExperimentTerminationCondition terminition = EntityFactory.createTimeOutTerminationCondition(10);
//
//		assertEquals(builder.getBuiltSpecification().getExperimentSeriesDefinitions().size(), 0);
//
//		ExperimentSeriesDefinition def = builder.addExperimentSeries(TESTNAME, terminition);
//
//		assertEquals(builder.getBuiltSpecification().getExperimentSeriesDefinitions().size(), 1);
//		assertEquals(def, builder.getBuiltSpecification().getExperimentSeriesDefinitions().get(0));
//
//		assertFalse(builder.addExperimentSeries(def));
//		assertNull(builder.addExperimentSeries(TESTNAME, terminition));
//
//		assertEquals(builder.getBuiltSpecification().getExperimentSeriesDefinitions().size(), 1);
//
//		assertFalse(builder.removeExperimentSeries("no exp"));
//
//		assertNull(builder.getExperimentSeries("abcd"));
//		ExperimentSeriesDefinition defGet = builder.getExperimentSeries(TESTNAME);
//		assertTrue(builder.removeExperimentSeries(defGet));
//
//		assertEquals(builder.getBuiltSpecification().getExperimentSeriesDefinitions().size(), 0);
	}
}
