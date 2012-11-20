package org.sopeco.frontend.server.rpc;

import org.sopeco.frontend.server.StartUp;
import org.sopeco.frontend.server.user.User;
import org.sopeco.frontend.server.user.UserManager;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class SuperRemoteServlet extends RemoteServiceServlet {

	/**
	 * Returns the current session id.
	 */
	private static final long serialVersionUID = 1L;

	protected String getSessionId() {
		return getThreadLocalRequest().getSession().getId();
	}

	protected User getUser() {
		return UserManager.getUser(getSessionId());
	}

	@Override
	protected void onAfterResponseSerialized(String serializedResponse) {
		StartUp.start(getSessionId());

		getUser().setLastRequestTime(System.currentTimeMillis());
	}
}
