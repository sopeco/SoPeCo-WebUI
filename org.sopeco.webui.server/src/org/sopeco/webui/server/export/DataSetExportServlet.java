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
package org.sopeco.webui.server.export;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.entities.ExperimentSeries;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.ScenarioInstance;
import org.sopeco.persistence.exceptions.DataNotFoundException;
import org.sopeco.persistence.util.DataSetCsvHandler;
import org.sopeco.webui.server.security.Security;
import org.sopeco.webui.server.user.User;
import org.sopeco.webui.server.user.UserManager;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class DataSetExportServlet extends HttpServlet {

	/**	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Security.requiredLoggedIn(req);

		String seperator = req.getParameter("seperator");
		String pTimestamp = req.getParameter("timestamp");
		String experimentName = req.getParameter("experimentName");
		String controllerURL = req.getParameter("controllerURL");
		String scenarioName = req.getParameter("scenarioName");

		long timestamp = Long.parseLong(pTimestamp);
		String seriesName = experimentName;
		String url = controllerURL;
		String scenario = scenarioName;
		char[] separator = new String(seperator).toCharArray();

		try {
			ScenarioInstance instance = getScenarioInstance(req.getSession().getId(), scenario, url);
			ExperimentSeries series = getSeries(instance, seriesName);
			ExperimentSeriesRun run = getRun(series, timestamp);

			DataSetAggregated dataset = run.getSuccessfulResultDataSet();
			// SimpleDataSet simpleDataset = dataset.convertToSimpleDataSet();

			DataSetCsvHandler handler = new DataSetCsvHandler(separator[0], separator[1], separator[2], true);
			String csvData = handler.convertToCSVString(dataset);

			sendData(resp, csvData, run.getLabel().replaceAll(" ", "_") + ".csv");

		} catch (DataNotFoundException e) {
			resp.sendError(204);
			return;
		} catch (IOException e) {
			resp.sendError(400);
			return;
		}
	}

	private void sendData(HttpServletResponse resp, String data, String name) throws IOException {
		resp.setContentType("text/xml");
		resp.addHeader("Content-Disposition", "attachment; filename=" + name);
		resp.setContentLength((int) data.length());

		resp.getWriter().write(data);
	}

	/**
	 *
	 */
	private ExperimentSeriesRun getRun(ExperimentSeries series, Long timestamp) throws DataNotFoundException {
		for (ExperimentSeriesRun run : series.getExperimentSeriesRuns()) {
			System.out.println(run.getTimestamp() + " " + timestamp);
			if (timestamp.equals(run.getTimestamp())) {
				return run;
			}
		}

		throw new DataNotFoundException("No ExperimentSeriesRun with timestamp '" + timestamp + "' found..");
	}

	/**
	 * 
	 */
	private ExperimentSeries getSeries(ScenarioInstance instance, String name) throws DataNotFoundException {
		for (ExperimentSeries series : instance.getExperimentSeriesList()) {
			if (series.getName().equals(name)) {
				return series;
			}
		}

		throw new DataNotFoundException("No ExperimentSeries '" + name + "' found..");
	}

	/**
	 * 
	 */
	private ScenarioInstance getScenarioInstance(String sId, String scenarioName, String url)
			throws DataNotFoundException {
		User user = UserManager.instance().getUser(sId);
		if (user == null) {
			throw new DataNotFoundException("No user at session '" + sId + "' found..");
		}
		ScenarioInstance instance = user.getCurrentPersistenceProvider().loadScenarioInstance(scenarioName, url);
		return instance;
	}
}
