package com.website.tools;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HttpErrorHandler
{

	private static final Logger LOGGER = LogManager.getLogger();

	private HttpErrorHandler()
	{
		super();
	}

	public static void print500(final HttpServletResponse response, final EventServiceException eventServiceException)
	{
		LOGGER.fatal("The query doesn't match with the database", eventServiceException);
		try
		{
			response.sendError(500, "Something wrong append");
		}
		catch (final Exception e)
		{
			LOGGER.fatal(e);
		}
	}

	public static void print404(final String errorMessage)
	{
		LOGGER.fatal(errorMessage);
		try
		{
			ContextManager.CONTEXT.responseSendError(404, "Page not found");
		}
		catch (final Exception e)
		{
			LOGGER.fatal(e);
		}

	}

	public static void print404(final Exception exception, final String errorMessage)
	{
		LOGGER.fatal(errorMessage, exception);
		try
		{
			ContextManager.CONTEXT.responseSendError(404, "Page not found");
		}
		catch (final Exception e)
		{
			LOGGER.fatal(e);
		}

	}

	public static void print401(final String errorMessage)
	{
		LOGGER.fatal("Authentication required", errorMessage);
		try
		{
			ContextManager.CONTEXT.responseSendError(401, "Please log in to access this page");
		}
		catch (final Exception e)
		{
			LOGGER.fatal(e);
		}
	}

	public static void print500(final EventServiceException eventServiceException)
	{
		LOGGER.fatal("The query doesn't match with the database", eventServiceException);
		try
		{
			ContextManager.CONTEXT.responseSendError(500, "Something wrong append");
		}
		catch (final Exception e)
		{
			LOGGER.fatal(e);
		}
	}

	public static void print500(final Exception exception, final String errorMessage)
	{
		LOGGER.fatal(errorMessage, exception);
		try
		{
			ContextManager.CONTEXT.responseSendError(500, "Something wrong append");
		}
		catch (final Exception e)
		{
			LOGGER.fatal(e);
		}
	}
}
