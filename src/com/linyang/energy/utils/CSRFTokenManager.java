package com.linyang.energy.utils;

import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public final class CSRFTokenManager {

	static final String CSRF_PARAM_NAME = "CSRFToken";

	public static final String CSRF_TOKEN_FOR_SESSION_ATTR_NAME = CSRFTokenManager.class.getName() + ".tokenval";

	public static String getTokenForSession(HttpSession session) {
		String token = null;
		synchronized (session) {
			token = (String) session.getAttribute(CSRF_TOKEN_FOR_SESSION_ATTR_NAME);
			if (null == token) {
				token = UUID.randomUUID().toString();
				session.setAttribute(CSRF_TOKEN_FOR_SESSION_ATTR_NAME, token);
			}
		}

		return token;
	}

	public static String getTokenFromSession(HttpSession session) {
		Object token = session.getAttribute(CSRF_TOKEN_FOR_SESSION_ATTR_NAME);
		if (token != null)
			return token.toString();
		return null;
	}

	/**
	 * Extracts the token value from the session
	 * 
	 * @param request
	 * @return
	 */

	public static String getTokenFromRequest(HttpServletRequest request) {
		return request.getParameter(CSRF_PARAM_NAME);
	}

	private CSRFTokenManager() {

	};
}
