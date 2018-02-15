package com.website.views.pages;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import com.website.views.WebPages;

/**
 * The view for the home web page.</br>
 * This {@link Named} class is bound with the same named xhtml file.
 *
 * @author Jérémy Pansier
 */
@Named
@RequestScoped
public class Home
{
	/** The web page. */
	public static final WebPages WEB_PAGE = WebPages.HOME;

	/**
	 * @return the web page
	 */
	public WebPages getWebPage()
	{
		return WEB_PAGE;
	}
}
