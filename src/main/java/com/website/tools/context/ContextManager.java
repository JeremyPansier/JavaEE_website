package com.website.tools.context;

import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * The context manager.</br>
 *
 * @author Jérémy Pansier
 */
public class ContextManager {

	/** The faces external context of the current instance. */
	protected static final ExternalContext CONTEXT = FacesContext.getCurrentInstance().getExternalContext();

	/** The temporary objects stored. */
	private static final Flash FLASH = CONTEXT.getFlash();

	/** The flash message key. */
	private static final String MESSAGE_KEY = "flash.message";

	/**
	 * This private constructor to hide the default public one.</br>
	 */
	private ContextManager() {}

	/**
	 * Gets the message stored in the context map.
	 *
	 * @return the message stored in the context map or null if there is no message stored
	 */
	protected static String getMessage() {
		final Object message = FLASH.get(MESSAGE_KEY);
		return null == message ? null : message.toString();
	}

	/**
	 * Puts the given message in the context map.</br>
	 * 
	 * @param value the message content
	 */
	protected static void putMessage(final String value) {
		FLASH.put(MESSAGE_KEY, value);
	}

	/**
	 * @return the session map of the {@link #CONTEXT}.
	 */
	protected static Map<String, Object> getSessionMap() {
		return CONTEXT.getSessionMap();
	}

	/**
	 * @return any existing session instance associated with the current request, or null if there is no such session.
	 */
	protected static HttpSession getSession() {
		return (HttpSession) CONTEXT.getSession(false);
	}

	/**
	 * @return the website URL
	 */
	public static String getWebsiteUrl() {
		final HttpServletRequest originRequest = (HttpServletRequest) CONTEXT.getRequest();
		final String originUrl = originRequest.getRequestURL().toString();
		return originUrl.substring(0, originUrl.lastIndexOf('/'));
	}

	/**
	 * @return the environment-specific object instance for the current request.
	 */
	public static HttpServletRequest getRequest() {
		return (HttpServletRequest) CONTEXT.getRequest();
	}

	/**
	 * @return the application environment object instance for the current application.
	 */
	public static ServletContext getContext() {
		return (ServletContext) CONTEXT.getContext();
	}
}
