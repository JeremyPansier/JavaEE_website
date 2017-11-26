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

import com.website.services.EventService;
import com.website.tools.EventServiceException;
import com.website.tools.HttpErrorHandler;

@WebServlet("/ReadEmailTackerServlet")
public class ReadEmailTrackerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LogManager.getLogger();
    private static final int ISREAD = 1;
    @Inject
    private EventService eventService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String hash = request.getParameter("msg_id");
        try {
            eventService.updateSubscribersEmailstatusByHash(hash, ISREAD);
            request.getRequestDispatcher("/images/pixel.png")
                   .forward(request, response);
        } catch (EventServiceException e) {
            HttpErrorHandler.print500(response, e);
            return;
        } catch (Exception e) {
            LOGGER.error("forward issue", e);
            return;
        }
    }
}
