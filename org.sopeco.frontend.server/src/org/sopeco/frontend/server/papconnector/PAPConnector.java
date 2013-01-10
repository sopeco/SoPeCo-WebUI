package org.sopeco.frontend.server.papconnector;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.sopeco.frontend.server.papconnector.HTTPConnector;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestBuilder.Method;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;


public class PAPConnector {

	public static final String UID = "Ben";
	public static final String PAP_URL = "http://deqkal279.qkal.sap.corp:8080/pap/j/";

	public static final String VERIFY_USER_WITHOUT_PASSWORD = "verifyuserwithoutpassword";

	public static final String ADD_USER = "addUser"; // GET
	public static final String ADD_PROJECT = "addproject"; // GET
	public static final String ADD_QUERY = "addQuery"; // POST
	public static final String ADD_PROCESS = "addProcess"; // POST
	public static final String ADD_OUTPUT = "addOutput"; // POST
	public static final String ADD_RESULT = "addResult"; // GET

	public static final String DEL_PROJECT = "delproject"; // GET

	public static final String GET_LIST = "getlist"; // GET
	public static final String GET_RESULT = "getResult"; // GET

	private HTTPConnector httpConnector;

	public PAPConnector() {
		httpConnector = new HTTPConnector();
	}

	public String addUser(String username, String email) {
		String[] s = httpConnector.executeGet(PAP_URL + ADD_USER, "u=" + username + "&e="
				+ email);
		return s[0];
	}

	public String verifyUser(String username) {
		String[] s = httpConnector.executeGet(PAP_URL
				+ VERIFY_USER_WITHOUT_PASSWORD, "u=" + username);
		return s[0];
	}

	public String addProject(String userid, String projectname) {
		String name = "NoName";
		try {
			name = URLEncoder.encode(projectname, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] s = httpConnector.executeGet(PAP_URL + ADD_PROJECT, "uid="
				+ userid + "&projname=" + name);
		return s[0];
	}

	public String deleteProject(String projectID) {
		String[] s = httpConnector.executeGet(PAP_URL + DEL_PROJECT, "pid="
				+ projectID);
		return s[0];
	}

	public String getProjectList(String userid) {
		String[] s = httpConnector.executeGet(PAP_URL + GET_LIST, "uid="
				+ userid);
		return s[0];
	}

	public String addQuery(String projectID, String queryname,
			String sqlStatement, String dataEndPoint) {
		String[] s = httpConnector.executePost(PAP_URL + ADD_QUERY, "pid="
				+ projectID + "&qname=" + queryname + "&sqlstmt="
				+ sqlStatement + "&dtep=" + dataEndPoint);
		return s[0];
	}

	public String addProcess(String processID, String processName,
			int processType, String processScript) {
		String[] s = httpConnector.executePost(PAP_URL + ADD_PROCESS, "pid="
				+ processID + "&procname=" + processName + "&proctype="
				+ processType + "&procscript=" + processScript);
		return s[0];
	}

	public String getResult(String pid) {
		String[] s = httpConnector.executeGet(PAP_URL + GET_RESULT, "pid="
				+ pid);
		return s[0];
	}

	public String addOutput(String projectID, String outputName,
			int outputType, String outputScript, String description) {
		String[] s = httpConnector.executePost(PAP_URL + ADD_OUTPUT, "pid="
				+ projectID + "&outputname=" + outputName + "&outputtype="
				+ outputType + "&outputscript=" + outputScript
				+ "&description=" + description);
		return s[0];
	}

	public String addResult(String projectID, String processID, String queryID,
			String resultName, String userID, String outputID) {
		String[] s = httpConnector.executeGet(PAP_URL + ADD_RESULT, "pid="
				+ projectID + "&procid=" + processID + "&qid=" + queryID
				+ "&bname=" + resultName + "&uid=" + userID + "&oid="
				+ outputID);
		return s[0];
	}
}
