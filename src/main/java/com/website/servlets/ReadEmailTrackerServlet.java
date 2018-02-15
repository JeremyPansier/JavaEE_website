package com.website.servlets;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.website.persistence.GuestService;
import com.website.tools.EventServiceException;
import com.website.tools.HttpErrorHandler;

@WebServlet("/ReadEmailTackerServlet")
public class ReadEmailTrackerServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LogManager.getLogger();
	private static final int ISREAD = 1;
	/** The service managing the guest persistence. */
	@Inject
	private GuestService guestService;

	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException
	{
		final String hash = request.getParameter("msg_id");
		try
		{
			guestService.updateGuestsEmailstatusByHash(hash, ISREAD);
			request.getRequestDispatcher("/images/pixel.png")
					.forward(request, response);
		}
		catch (final EventServiceException e)
		{
			HttpErrorHandler.print500(response, e);
			return;
		}
		catch (final Exception e)
		{
			LOGGER.error("forward issue", e);
			return;
		}
	}
}
