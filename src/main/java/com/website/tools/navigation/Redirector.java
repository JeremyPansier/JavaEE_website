package com.website.tools.navigation;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.jboss.logging.Logger;

/**
 * The URL redirector.</br>
 * Lets to redirect to a specified URL.
 *
 * @author Jérémy Pansier
 */
@Named
@ViewScoped
public class Redirector implements Serializable {

	/** The serial version UID. */
	private static final long serialVersionUID = 1148601551383990989L;

	/** The logger. */
	private static final Logger LOGGER = Logger.getLogger(SessionManager.class);

	/**
	 * Redirects to the specified URL.</br>
	 *
	 * @param url the URL to point to
	 */
	public static void redirect(final String url) {
		redirect(url, false, null);
	}

	/**
	 * Redirects to the specified URL.</br>
	 * Invalidates the session if needed and store a message to display on the next page.
	 * 
	 * Note: The message will not be stored if it's null.
	 *
	 * @param url the URL to point to
	 * @param isInvalid whether the session have to be invalidated
	 * @param message the message to display on the next page.
	 */
	public static void redirect(final String url, final boolean isInvalid, final String message) {
		try {
			if (isInvalid) SessionManager.releaseUser();
			if (null != message) ContextManager.putMessage(message);
			FacesContext.getCurrentInstance().getExternalContext().redirect(url);
		}
		catch (final IllegalStateException e) {
			LOGGER.error("Session not found", e);
			HttpErrorHandler.print500(e, "No session found");
			return;
		}
		catch (final IOException e) {
			HttpErrorHandler.print500(e, "Redirection issue");
			return;
		}
	}
}
