package com.aiconoa.trainings.servlet;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aiconoa.trainings.services.EventService;
import com.aiconoa.trainings.services.EventServiceException;
import com.aiconoa.trainings.services.HttpErrorHandler;

@WebServlet("/MailReadServlet")
public class MailReadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LogManager.getLogger();
    private static final int MAILREAD = 1;
    @Inject
    private EventService eventService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String hashcodeString = request.getParameter("msg_id");
        try {
            eventService.updateSubscribersMailStatusWhereHashcode(MAILREAD, hashcodeString);
            request.getRequestDispatcher("/images/pixel.png")
                   .forward(request, response);
        } catch (EventServiceException e) {
            HttpErrorHandler.print500(response, e);
            return;
        } catch (Exception e) {
            LOGGER.error("probleme avec le forward du dispatcher", e);
            return;
        }
    }
}
