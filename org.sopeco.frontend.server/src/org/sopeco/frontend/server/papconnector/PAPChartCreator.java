package org.sopeco.frontend.server.papconnector;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.sopeco.frontend.shared.entities.Visualization;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public class PAPChartCreator implements ChartCreator {
	private static final String BASE_URL = "http://deqkal279.qkal.sap.corp:8080/pap/j/result/";
	private static PAPConnector papConnector = new PAPConnector();
	private static final String SQL_DUMMY = "SELECT * FROM perf_pap.pvexperiment";

	private static Set<Visualization> savedVisualizations = new HashSet<Visualization>(); // link, projectname
	private static HashMap<String, String> savedProjects = new HashMap<String, String>();// projectname, pid
	private static String uid;
	private static Runnable run = new Runnable() {
		
		@Override
		public void run() {
			papConnector.addUser("testuser", "testmail@test.de");
			uid = papConnector.verifyUser("testuser");
			String pList = papConnector.getProjectList(uid);
			//TODO check pList for null
			JsonElement elem = new JsonParser().parse(pList).getAsJsonObject()
					.get("adProject");
			JsonArray jArray;
			if (elem instanceof JsonArray){
				jArray = elem.getAsJsonArray();
			}
			else {
				jArray = new JsonArray();
				jArray.add(elem);
			}
			
			for (int i = 0; i < jArray.size(); i++) {
				String pid = jArray.get(i).getAsJsonObject().get("PID")
						.getAsString();
				String pname = jArray.get(i).getAsJsonObject().get("projectName")
						.getAsString();
				savedProjects.put(pname, pid);
				String bList = papConnector.getResult(pid);
				
				if (bList != null && !bList.equals("null")) {
					JsonElement element = new JsonParser().parse(bList).getAsJsonObject().get("adBinding");
					if (element instanceof JsonArray) {
						JsonArray array = element.getAsJsonArray();
						for (int j = 0; j < array.size(); j++) {
							JsonObject lobject = array.get(j).getAsJsonObject();
							String bid = lobject.get("BID")
									.getAsString();
							
							String link = BASE_URL + uid + "/" + pid + "/" + bid;
							Visualization visualization = createVisualization(link, pname);
							savedVisualizations.add(visualization);
						}
					} else if (element instanceof JsonObject) {
						JsonObject object = element.getAsJsonObject();
						String bid = object.get("BID")
								.getAsString();
						
						String link = BASE_URL + uid + "/" + pid + "/" + bid;
						Visualization visualization = createVisualization(link, pname);
						savedVisualizations.add(visualization);
					}
				}

			}
		}
	};
	
	static {
		Thread thread = new Thread(run);
		thread.start();
	}

	public PAPChartCreator() {
		
	}

	/**
	 * Generates a new project in PAP and returns the link to the chart
	 * 
	 * @param experimentName
	 * @param processScript
	 * @param outputScript
	 * @return A link pointing to the new PAP-Chart
	 */
	public Visualization getChartHTML(String experimentName, Double[][] data, String[] columnNames, ChartTypes type) {
		String processScript = createProcessScript(data, columnNames);
		String outputScript = creatOutputScript(type, columnNames);
		String pid = getOrCreateProject(experimentName);
		String qid = papConnector.addQuery(pid, "testquery" + generateID(),
				SQL_DUMMY, "empty");
		String procid = papConnector.addProcess(pid, "testprocess"
				+ generateID(), 1, processScript);
		String oid = papConnector.addOutput(pid, "testoutput" + generateID(),
				1, outputScript, "desc");
		String bid = papConnector.addResult(pid, procid, qid, "testresult"
				+ generateID(), uid, oid);
		String link = BASE_URL + uid + "/" + pid + "/" + bid;
		Visualization visualization = createVisualization(link, experimentName);
		savedVisualizations.add(visualization);
		return visualization;
	}
	
	private static Visualization createVisualization(String link, String name){
		Visualization visualization = new Visualization();
		SafeHtmlBuilder builder = new SafeHtmlBuilder();
		builder.appendHtmlConstant("<iframe class='gwt-Frame chartView' src='");
		builder.appendEscaped(link);
		builder.appendHtmlConstant("'></iframe>");
		visualization.setChart(builder.toSafeHtml());
		visualization.setLink(link);
		visualization.setName(name);
		return visualization;
	}

	private String createProcessScript(Double[][] data, String[] columnNames){
		StringBuffer rValue = new StringBuffer();

		for (int i = 0; i < data.length; i++) {
			rValue.append(columnNames[i]);
			rValue.append(i);
			rValue.append(" <- c(");
			for (int j = 0; j < data[i].length; j++) {
				rValue.append(data[i][j]);
				if (j < data[i].length - 1) {
					rValue.append(", ");
				}
			}
			rValue.append(")\n");
		}
		return rValue.toString();
	}
	
	private String creatOutputScript(ChartTypes type, String[] columnNames){
		StringBuffer outputScript = new StringBuffer();
		System.out.println("type3: " + type);
		switch (type){
		case BAR_CHART:
			outputScript.append("data <- do.call(rbind, list(");
			for (int i = 1; i < columnNames.length; i++){
				outputScript.append(columnNames[i]+i);
				if (i < columnNames.length-1){
					outputScript.append(",");
				}
			}
			outputScript.append("))\n");
			outputScript.append("matr <- as.matrix(data,nrow=" + (columnNames.length-1) +")\n");
			outputScript.append("barplot(matr,");
			
			outputScript.append("names.arg="+columnNames[0]+0);
			outputScript.append(",beside=TRUE,col=rainbow("+(columnNames.length-1)+"))");
			break;
		case PIE_CHART:
			outputScript.append("pie3D(");
			outputScript.append(columnNames[1]+1+","+columnNames[0]+0);
			outputScript.append(",explode=0.1,col=rainbow(length("+columnNames[0]+0+")))");
			break;
		default:
			outputScript.append("plot(");
			outputScript.append(columnNames[0]+0);
			outputScript.append(","+columnNames[1]+1);
			outputScript.append(",type=\"l\",col=rainbow("+(columnNames.length-1)+")[1])");
			for (int i = 2; i < columnNames.length; i++){
				outputScript.append("\nlines(");
				outputScript.append(columnNames[i]+i);
				outputScript.append(",type=\"l\",col=rainbow("+(columnNames.length-1)+")["+i+"])");
			}
		}
		outputScript.append("\nlegend(\"topleft\", c(");
		for (int i = 1; i < columnNames.length; i++){
			outputScript.append("\""+columnNames[i]+i+ "\"");
			if (i < columnNames.length -1){
				outputScript.append(",");
			}
		}
		outputScript.append("), col=rainbow("+(columnNames.length-1)+"),lwd=2,bty=\"n\")");
//		legend("topleft", c("outNs.out1","inputNs.input2"), col=rainbow(2), 
//				   lwd=2, bty="n");
		return outputScript.toString();
	}

	public Visualization[] getAllVisualizations() {
		return savedVisualizations.toArray(new Visualization[savedVisualizations.size()]);
	}

	private String getOrCreateProject(String projectname) {
		String pid = savedProjects.get(projectname);
		if (pid == null) {
			pid = papConnector.addProject(uid, projectname);
		}
		return pid;
	}

	private static String generateID() {
		String id;
		try {
			id = URLEncoder.encode(Calendar.getInstance().getTime().toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			id = "badid";
		}
		return id;
	}
}
