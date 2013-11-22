package org.sopeco.webui.server.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.sopeco.webui.server.user.UserManager;

public class Security {

	public static void requiredLoggedIn(HttpServletRequest request) {
		requiredLoggedIn(request.getSession());
	}

	public static void requiredLoggedIn(HttpSession session) {
		requiredLoggedIn(session.getId());
	}

	public static void requiredLoggedIn(String sessionId) {
		if (!UserManager.instance().existUser(sessionId)) {
			throw new IllegalAccessError("Access denied. You have to be logged in.");
		}
	}

}
