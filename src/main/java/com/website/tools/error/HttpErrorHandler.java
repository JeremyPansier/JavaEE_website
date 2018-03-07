package com.website.tools.error;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.website.tools.navigation.ContextManager;

/**
 * The handler for HTTP errors.</br>
 * Lets to log an exception and send an error response to the client using the wanted status.
 *
 * @author Jérémy Pansier
 */
public class HttpErrorHandler {

	/** The logger. */
	private static final Logger LOGGER = LogManager.getLogger();

	/**
	 * Private constructor to hide the implicit empty public one.
	 */
	private HttpErrorHandler() {}

	/**
	 * Logs the error message and sends an error response to the client using the error 404 status code.
	 *
	 * @param errorMessage the error message
	 */
	public static void print404(final String errorMessage) {
		LOGGER.fatal(errorMessage);
		ContextManager.sendError(404, "Page not found");
	}

	/**
	 * Logs the error message and the stack trace and sends an error response to the client using the error 404 status code.
	 * 
	 * @param errorMessage the error message
	 * @param exception the exception to print
	 */
	public static void print404(final String errorMessage, final Exception exception) {
		LOGGER.fatal(errorMessage, exception);
		ContextManager.sendError(404, "Page not found");
	}

	/**
	 * Logs the error message and sends an error response to the client using the error 401 status code.
	 *
	 * @param errorMessage the error message
	 */
	public static void print401(final String errorMessage) {
		LOGGER.fatal("Authentication required", errorMessage);
		ContextManager.sendError(401, "Please log in to access this page");
	}

	/**
	 * Logs the error message and the stack trace and sends an error response to the client using the error 500 status code.
	 * 
	 * @param errorMessage the error message
	 * @param exception the exception to print
	 */
	public static void print500(final String errorMessage, final Exception exception) {
		LOGGER.fatal(errorMessage, exception);
		ContextManager.sendError(500, "Something wrong append");
	}
}
