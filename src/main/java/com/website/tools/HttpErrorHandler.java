package com.website.tools;

import javax.faces.context.ExternalContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HttpErrorHandler {

    private static final Logger LOGGER = LogManager.getLogger();

    private HttpErrorHandler() {
        super();
    }

    public static void print500(HttpServletResponse response, EventServiceException e) {
        LOGGER.fatal("The query doesn't match with the database", e);
        try {
            response.sendError(500, "Something wrong append");
        } catch (Exception e1) {
            LOGGER.fatal(e1);
        }
    }

    public static void print404(ExternalContext ec, String errorMessage) {
        LOGGER.fatal(errorMessage);
        try {
            ec.responseSendError(404, "Page not found");
        } catch (Exception e) {
            LOGGER.fatal(e);
        }

    }

    public static void print404(ExternalContext ec, Exception e, String errorMessage) {
        LOGGER.fatal(errorMessage, e);
        try {
            ec.responseSendError(404, "Page not found");

        } catch (Exception e1) {
            LOGGER.fatal(e1);
        }

    }

    public static void print401(ExternalContext ec, String errorMessage) {
        LOGGER.fatal("Authentication required", errorMessage);
        try {
            ec.responseSendError(401, "Please log in to access this page");
        } catch (Exception e1) {
            LOGGER.fatal(e1);
        }
    }

    public static void print500(ExternalContext ec, EventServiceException e) {
        LOGGER.fatal("The query doesn't match with the database", e);
        try {
            ec.responseSendError(500, "Something wrong append");
        } catch (Exception e1) {
            LOGGER.fatal(e1);
        }
    }

    public static void print500(ExternalContext ec, Exception e, String errorMessage) {
        LOGGER.fatal(errorMessage, e);
        try {
            ec.responseSendError(500, "Something wrong append");
        } catch (Exception e1) {
            LOGGER.fatal(e1);
        }
    }
}
