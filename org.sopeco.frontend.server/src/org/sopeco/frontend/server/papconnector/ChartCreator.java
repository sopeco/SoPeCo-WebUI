package org.sopeco.frontend.server.papconnector;



public class ChartCreator {
	private static final String BASE_URL = "http://deqkal279.qkal.sap.corp:8080/pap/j/result/";
	private static PAPConnector papConnector = new PAPConnector();
	private static final String SQL_DUMMY = "SELECT * FROM perf_pap.pvexperiment";

	public static String getSimpleChartLink(String experimentName, String rScript){
		papConnector.addUser("testuser", "testmail@test.de");
		String uid = papConnector.verifyUser("testuser");
		String pid = papConnector.addProject(uid, experimentName);
		if (pid.equals("no")){
			//TODO delete project and recreate it
		}
		String qid = papConnector.addQuery(pid, "testquery", SQL_DUMMY, "empty");
		String procid = papConnector.addProcess(pid, "testprocess", 1, rScript);
		String oid = papConnector.addOutput(pid, "testoutput", 1, "plot(c0,c1)", "desc");
		String bid = papConnector.addResult(pid, procid, qid, "testresult", uid, oid);
		return BASE_URL + uid + "/" + pid + "/" + bid;
	}
}
