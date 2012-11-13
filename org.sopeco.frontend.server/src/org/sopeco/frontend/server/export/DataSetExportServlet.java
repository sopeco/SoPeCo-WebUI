package org.sopeco.frontend.server.export;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sopeco.frontend.server.user.User;
import org.sopeco.frontend.server.user.UserManager;
import org.sopeco.frontend.shared.helper.Base64;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.SimpleDataSet;
import org.sopeco.persistence.entities.ExperimentSeries;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.ScenarioInstance;
import org.sopeco.persistence.exceptions.DataNotFoundException;
import org.sopeco.persistence.util.DataSetCsvHandler;

/**
 * 
 * @author Marius Oehler
 *
 */
public class DataSetExportServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// long run = Long.parseLong(req.getParameter("run"));
		// String series = Base64.decodeString(req.getParameter("series"));
		// String url = Base64.decodeString(req.getParameter("url"));
		// String scenario = Base64.decodeString(req.getParameter("scenario"));
		//
		// resp.getWriter().write(" run: " + run);
		// resp.getWriter().write(" series: " + series);
		// resp.getWriter().write(" url: " + url);
		// resp.getWriter().write(" scenario: " + scenario);

		String param = Base64.decodeString(req.getParameter("param"));
		String[] splittedParameter = param.split("\\|");

		if (splittedParameter.length != 5) {
			resp.sendError(400);
			return;
		}

		long timestamp = Long.parseLong(splittedParameter[0]);
		String seriesName = splittedParameter[1];
		String url = splittedParameter[2];
		String scenario = splittedParameter[3];
		char[] separator = Base64.decodeString(splittedParameter[4]).toCharArray();

		try {
			ScenarioInstance instance = getScenarioInstance(req.getSession().getId(), scenario, url);
			ExperimentSeries series = getSeries(instance, seriesName);
			ExperimentSeriesRun run = getRun(series, timestamp);

			DataSetAggregated dataset = run.getSuccessfulResultDataSet();
			SimpleDataSet simpleDataset = dataset.convertToSimpleDataSet();

			DataSetCsvHandler handler = new DataSetCsvHandler(separator[0], separator[1], separator[2], true);
			String csvData = handler.convertToCSVString(simpleDataset);

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
		User user = UserManager.getUser(sId);
		if (user == null) {
			throw new DataNotFoundException("No user at session '" + sId + "' found..");
		}
		ScenarioInstance instance = user.getCurrentPersistenceProvider().loadScenarioInstance(scenarioName, url);
		return instance;
	}
}
