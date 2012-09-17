package org.sopeco.frontend;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.sopeco.frontend.server.rpc.DatabaseManagerRPCImpl;
import org.sopeco.persistence.metadata.entities.DatabaseInstance;

/**
 * Unittests for the DatabaseRPCalls.
 * 
 * @author Marius Oehler
 * 
 */
public class DatabaseManagerRPCTest {

	@Test
	public void testInstanceEqual() {
		DatabaseManagerRPCImpl db = new DatabaseManagerRPCImpl();

		DatabaseInstance i1 = new DatabaseInstance();
		DatabaseInstance i2 = new DatabaseInstance();

		i1.setDbName("dbName");
		i1.setHost("dbHost");
		i1.setPort("1234");
		i1.setProtectedByPassword(true);

		i2.setDbName("dbName");
		i2.setHost("dbHost");
		i2.setPort("1234");
		i2.setProtectedByPassword(true);

		assertTrue(db.instanceEqual(i1, i2));
	}
}
