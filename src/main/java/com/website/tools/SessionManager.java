package com.website.tools;

import java.io.Serializable;

import javax.faces.view.ViewScoped;

import com.website.views.WebPages;

/**
 * The session manager.</br>
 *
 * @author Jérémy Pansier
 */
@ViewScoped
public class SessionManager implements Serializable
{
	/** The serial version UID. */
	private static final long serialVersionUID = 8742974460508983480L;

	/** The user name key. */
	private static final String USERNAME_KEY = "username";

	/**
	 * Checks if the session user is connected.
	 *
	 * @return the session user name
	 */
	public static String checkSessionUserName()
	{
		final String username = getSessionUserName();
		if (null == username)
		{
			MessageManager.putMessage("Connexion requise");
			Redirector.redirect(WebPages.CONNECTION.createJsfUrl());
			return null;
		}
		return username;
	}

	/**
	 * Gets the session user name.
	 *
	 * @return the session user name
	 */
	public static String getSessionUserName()
	{
		final Object username = ContextManager.getSessionMap().get(USERNAME_KEY);
		if (null == username)
		{
			return null;
		}
		return username.toString();
	}

	/**
	 * Stores the session user name.
	 *
	 * @param username the session user name to store
	 */
	public static void putSessionUserName(final String username)
	{
		ContextManager.getSessionMap().put(USERNAME_KEY, username);
	}

	/**
	 * Invalidates the session.
	 */
	public static void invalidateSession()
	{
		ContextManager.getSession().invalidate();
	}

	/**
	 * Clears the session.
	 */
	public static void clearSession()
	{
		ContextManager.getSessionMap().clear();
	}
}
