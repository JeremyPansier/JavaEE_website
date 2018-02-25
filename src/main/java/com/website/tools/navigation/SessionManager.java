package com.website.tools.navigation;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.website.views.WebPages;

/**
 * The session manager.</br>
 *
 * @author Jérémy Pansier
 */
public class SessionManager {

	/** The user cookie name. */
	private static final String USER_COOKIE_NAME = "user";

	/**
	 * The private constructor to hide the implicit empty public one.
	 */
	private SessionManager() {}

	/**
	 * Checks if the session user is connected.
	 *
	 * @return the session user name
	 */
	public static String checkSessionUserName() {
		final String username = getSessionUserName();
		if (null == username) {
			Redirector.redirect(WebPages.CONNECTION.createJsfUrl(), true, "Connexion requise");
			return null;
		}
		return username;
	}

	/**
	 * Gets the session user name.
	 *
	 * @return the session user name
	 */
	public static String getSessionUserName() {
		final Cookie cookie = getUserCookie();
		String name = null;
		if (null != cookie) {
			name = cookie.getValue();
		}
		return name;
	}

	/**
	 * Tracks the specified user with a cookie.</br>
	 * The cookie will persist in the session until browser shutdown.
	 *
	 * @param username the user name
	 */
	public static void trackUser(final String username) {

		Cookie cookie = getUserCookie();

		if (cookie != null) {
			cookie.setValue(username);
		} else {
			cookie = new Cookie(USER_COOKIE_NAME, username);
			cookie.setPath(ContextManager.getRequest().getContextPath());
		}

		ContextManager.getResponse().addCookie(cookie);
	}

	/**
	 * Releases the browser current user.
	 */
	public static void releaseUser() {

		Cookie cookie = getUserCookie();

		if (cookie == null) {
			cookie = new Cookie(USER_COOKIE_NAME, null);
			cookie.setPath(ContextManager.getRequest().getContextPath());
		}

		cookie.setMaxAge(0);

		ContextManager.getResponse().addCookie(cookie);
	}

	/**
	 * Gets the user cookie.
	 *
	 * @return the user cookie
	 */
	private static Cookie getUserCookie() {

		final HttpServletRequest request = ContextManager.getRequest();

		final Cookie[] userCookies = request.getCookies();
		Cookie cookie = null;
		if (userCookies != null && userCookies.length > 0) {
			for (int i = 0; i < userCookies.length; i++) {
				if (userCookies[i].getName().equals(USER_COOKIE_NAME)) {
					cookie = userCookies[i];
				}
			}
		}
		return cookie;
	}
}
