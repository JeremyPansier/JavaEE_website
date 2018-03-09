package com.website.managers.email;

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

/**
 * The email tracker.</br>
 * Tracks if the email has been opened.
 *
 * @author Jérémy Pansier
 */
@WebServlet("/OpenedEmailTackerServlet")
public class OpenedEmailTrackerServlet extends HttpServlet {

	/** The serialVersionUID. */
	private static final long serialVersionUID = 689631047245938893L;

	/** The Logger. */
	private static final Logger LOGGER = LogManager.getLogger();

	/** The code indicating that the guest is aware of the invitation to the event, i.e. he has opened the invitation email. */
	private static final int INFORMED = 1;

	/** The service managing the guest persistence. */
	@Inject
	private GuestService guestService;

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse) */
	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		final String hash = request.getParameter("msg_id");
		try {
			guestService.updateGuestInformedByHash(hash, INFORMED);

			request.getRequestDispatcher("/images/pixel.png")
					.forward(request, response);
		}
		catch (final Exception e) {
			LOGGER.error("forward issue", e);
			return;
		}
	}
}
