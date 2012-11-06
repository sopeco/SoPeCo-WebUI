package org.sopeco.frontend.server.export;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sopeco.frontend.shared.helper.Base64;

public class DataSetExportServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

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

		if (splittedParameter.length != 4) {
			resp.sendError(400);
			return;
		}

		long run = Long.parseLong(splittedParameter[0]);
		String series = splittedParameter[1];
		String url = splittedParameter[2];
		String scenario = splittedParameter[3];

	}
}
