package com.website.views.includes;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import com.website.tools.MessageManager;
import com.website.tools.SessionManager;

/**
 * A view for the banner showed on most of this project web pages.</br>
 * This {@link Named} class is bound with the same named xhtml file.
 *
 * @author Jérémy Pansier
 */
@Named
@RequestScoped
public class Banner
{
	/**
	 * Gets the flash message to be displayed in the banner of the web page.
	 *
	 * @return the flash message to be displayed in the banner of the web page
	 */
	public String getMessage()
	{
		return MessageManager.getMessage();
	}

	/**
	 * Gets the connected user name to be displayed in the banner of the web page.
	 *
	 * @return the user name to be displayed in the banner of the web page
	 */
	public String getUsername()
	{
		return SessionManager.getSessionUserName();
	}
}
