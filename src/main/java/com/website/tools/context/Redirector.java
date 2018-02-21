package com.website.tools.context;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.jboss.logging.Logger;

@Named
@ViewScoped
public class Redirector implements Serializable
{

	/** The serial version UID. */
	private static final long serialVersionUID = 1148601551383990989L;

	/** The logger. */
	private static final Logger LOGGER = Logger.getLogger(SessionManager.class);

	/**
	 * Redirects to the given URL.
	 *
	 * @param url the URL
	 */
	public static void redirect(final String url)
	{
		try
		{
			ContextManager.CONTEXT.redirect(url);
		}
		catch (final IllegalStateException e)
		{
			LOGGER.error("Session not found", e);
			HttpErrorHandler.print500(e, "No session found");
			return;
		}
		catch (final IOException e)
		{
			HttpErrorHandler.print500(e, "Redirection issue");
			return;
		}
	}
}
