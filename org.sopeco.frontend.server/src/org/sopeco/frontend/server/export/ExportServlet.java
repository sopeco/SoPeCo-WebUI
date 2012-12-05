package org.sopeco.frontend.server.export;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.sopeco.engine.model.ScenarioDefinitionWriter;
import org.sopeco.frontend.server.user.UserManager;
import org.sopeco.persistence.entities.definition.ScenarioDefinition;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ExportServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String SCNEARIO_EXPORT = "scenario";

	private HttpSession session;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		session = req.getSession();

		if (req.getParameter(SCNEARIO_EXPORT) != null) {
			sendScenarioAsXML(resp, session.getId());
		} else {
			resp.sendError(204);
		}
	}

	/**
	 * 
	 * @param resp
	 * @throws IOException
	 */
	private void sendScenarioAsXML(HttpServletResponse resp, String sessionId) throws IOException {
		if (!UserManager.existSession(session.getId())) {
			resp.sendError(204);
			return;
		}

		ScenarioDefinition definition = UserManager.getUser(session.getId()).getCurrentScenarioDefinitionBuilder()
				.getBuiltScenario();

		if (definition != null) {
			ScenarioDefinitionWriter writer = new ScenarioDefinitionWriter(sessionId);
			String definitionXML = writer.convertToXMLString(definition);
			String fileName = "scenario-" + definition.getScenarioName() + ".xml";

			sendXML(resp, definitionXML, fileName);
		} else {
			resp.sendError(204);
		}
	}

	/**
	 * 
	 * @param resp
	 * @param xmlContent
	 * @throws IOException
	 */
	private void sendXML(HttpServletResponse resp, String xmlContent, String fileName) throws IOException {

		resp.setContentType("text/xml");
		resp.addHeader("Content-Disposition", "attachment; filename=" + fileName);
		resp.setContentLength((int) xmlContent.length());

		resp.getWriter().write(xmlContent);
	}
}
