package org.sopeco.webui.server.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.sopeco.webui.server.user.TokenManager;

public class Security {

	/**
	 * Checks if the current {@link HttpServletRequest} is logged in.
	 * 
	 * @param request				the current {@link HttpServletRequest}
	 * @throws IllegalAccessError 	if the request is not logged in
	 */
	public static void requiredLoggedIn(HttpServletRequest request) {
		requiredLoggedIn(request.getSession());
	}

	/**
	 * Checks if the current {@link HttpSession} is logged in.
	 * 
	 * @param session				the {@link HttpSession}
	 * @throws IllegalAccessError 	if the session is not logged in
	 */
	public static void requiredLoggedIn(HttpSession session) {
		requiredLoggedIn(session.getId());
	}

	/**
	 * Checks if the current session is logged in.
	 * 
	 * @param sessionId				the session ID
	 * @throws IllegalAccessError 	if the session id is not logged in
	 */
	public static void requiredLoggedIn(String sessionId) {
		
		if (!TokenManager.instance().hasValidToken(sessionId)) {

			throw new IllegalAccessError("Access denied. You have to be logged in.");
			
		}
	}

}
