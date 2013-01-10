package org.sopeco.frontend;

import org.junit.Test;
import org.sopeco.frontend.server.papconnector.PAPConnector;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;

import static junit.framework.Assert.*;

public class TestPapConnection {
	
	private final String username = "testuser";
	private final String testmail = "testmail@test.de";
	private final String testproject = "testproject";
	private final String queryname = "testquery";
	private final String sqlDummy = "SELECT * FROM perf_pap.pvexperiment";
	private final String dataEndPoint = "n";
	private final String processName = "testprocess";
	private final int processType = 1;
	private final String processScript = "a <- c(1,2,3,4,5)" + System.getProperty("line.separator") + "b <- c(1,2,3,4,5)";
	private final String outputName = "testoutput";
	private final int outputType = 1;
	private final String outputScript = "plot(a,b)";
	private final String outputDescription = "Test-Description";
	private PAPConnector papConnector = new PAPConnector();
	
	@Test
	public void testConnection(){
		papConnector.addUser(username, testmail);
		String uid = papConnector.verifyUser(username);
		System.out.println(uid);
		assertFalse("Verification failed",uid.equals("no"));
		String pid = papConnector.addProject(uid, testproject);
		System.out.println(pid);
		assertFalse("Adding project failed",pid.equals("no"));
		String qid = papConnector.addQuery(pid,queryname,sqlDummy,dataEndPoint);
		System.out.println(qid);
		assertFalse("Adding query failed",qid.equals("no"));
		String procID = papConnector.addProcess(pid, processName, processType, processScript);
		System.out.println(procID);
		assertFalse("Adding Procees failed",procID.equals("no"));
		String oid = papConnector.addOutput(pid, outputName, outputType, outputScript, outputDescription);
		System.out.println(oid);
		assertFalse("Adding Process failed",oid.equals("no"));
		String bid = papConnector.addResult(pid, procID, qid, "testresult", uid, oid);
		System.out.println(bid);
		assertFalse("Adding Result failed",bid.equals("no"));
		String resultList = papConnector.getResult(pid);
		System.out.println(resultList);
		assertFalse("Adding Result failed",resultList.equals("no"));
		String del = papConnector.deleteProject(pid);
		assertFalse("Deleting project failed",del.equals("no"));
	}

}
