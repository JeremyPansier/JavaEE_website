package com.website.views.pages;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import com.website.tools.navigation.Redirector;
import com.website.views.WebPages;

/**
 * The disconnection web page view.</br>
 * This {@link Named} class is bound with the same named xhtml file.
 * 
 * @author Jérémy Pansier
 */
@Named
@RequestScoped
public class Disconnection {

	/**
	 * Logs the website user out.
	 */
	public void logout() {
		Redirector.redirect(WebPages.HOME.createJsfUrl(), true, "Déconnection réussie");
	}
}
