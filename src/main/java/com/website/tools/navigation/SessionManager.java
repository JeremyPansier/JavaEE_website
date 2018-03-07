package com.website.tools.navigation;

import com.website.views.WebPages;

/**
 * The session manager.</br>
 *
 * @author Jérémy Pansier
 */
public class SessionManager {

	/** The user cookie name. */
	private static final String USER_KEY = "user";

	/**
	 * The private constructor to hide the implicit empty public one.
	 */
	private SessionManager() {}

	/**
	 * Gets the session user name stored in the JSESSIONID cookie.</br>
	 * Redirects to the connection web page if its null.
	 *
	 * @return the session user name or null if there is no session user name stored in the JSESSIONID cookie
	 */
	public static String getSessionUserNameOrRedirect() {
		final String username = getSessionUserName();
		if (null == username) {
			Redirector.redirect(WebPages.CONNECTION.createJsfUrl(), true, "Connexion requise");
			return null;
		}
		return username;
	}

	/**
	 * Gets the session user name stored in the JSESSIONID cookie.</br>
	 *
	 * @return the session user name or null if there is no session user name stored in the JSESSIONID cookie
	 */
	public static String getSessionUserName() {
		final Object attribute = ContextManager.getRequest().getSession().getAttribute(USER_KEY);
		return attribute == null ? null : attribute.toString();
	}

	/**
	 * Tracks the specified user with a parameter stored in the JSESSIONID cookie.</br>
	 * The cookie will persist in the session until browser shutdown.
	 *
	 * @param username the user name to store
	 */
	public static void trackUser(final String username) {
		ContextManager.getRequest().getSession().setAttribute(USER_KEY, username);
	}

	/**
	 * Releases the browser current user.</br>
	 * Invalidates the session and by the way destroy the JSESSIONID cookie.
	 */
	public static void releaseUser() {
		ContextManager.getRequest().getSession().invalidate();
	}
}
