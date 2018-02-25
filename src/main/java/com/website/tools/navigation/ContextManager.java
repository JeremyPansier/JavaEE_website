package com.website.tools.navigation;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The context manager.</br>
 *
 * @author Jérémy Pansier
 */
public class ContextManager {

	/** The flash message key. */
	private static final String MESSAGE_KEY = "flash.message";

	/**
	 * The private constructor to hide the implicit empty public one.
	 */
	private ContextManager() {}

	/**
	 * @return the website URL
	 */
	public static String getWebsiteUrl() {
		final HttpServletRequest originRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		final String originUrl = originRequest.getRequestURL().toString();
		return originUrl.substring(0, originUrl.lastIndexOf('/'));
	}

	/**
	 * Gets the message stored in the context map.
	 *
	 * @return the message stored in the context map or null if there is no message stored
	 */
	public static String getMessage() {
		final Object message = FacesContext.getCurrentInstance().getExternalContext().getFlash().get(MESSAGE_KEY);
		return null == message ? null : message.toString();
	}

	/**
	 * Puts the given message in the context map.</br>
	 * 
	 * @param value the message content
	 */
	protected static void putMessage(final String value) {
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put(MESSAGE_KEY, value);
	}

	/**
	 * @return the environment-specific object instance for the current request.
	 */
	public static HttpServletRequest getRequest() {
		return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	}

	/**
	 * @return the environment-specific object instance for the current response.
	 */
	public static HttpServletResponse getResponse() {
		return (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
	}
}
