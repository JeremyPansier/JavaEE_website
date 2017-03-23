package com.aiconoa.trainings.services;

import java.sql.SQLException;

import javax.faces.context.ExternalContext;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HttpErrorHandler {

    private static final Logger LOGGER = LogManager.getLogger();

    private HttpErrorHandler() {
        super();
    }

    public static void print404(HttpServletResponse response, Exception e, String errorMessage) {
        LOGGER.fatal(errorMessage, e);
        try {
            response.sendError(404, "page introuvable");
        } catch (Exception e1) {
            LOGGER.fatal(e1);
        }
    }

    public static void print404(HttpServletResponse response, String errorMessage) {
        LOGGER.fatal(errorMessage);
        try {
            response.sendError(404, "page introuvable");
        } catch (Exception e1) {
            LOGGER.fatal(e1);
        }
    }

    public static void print401(HttpServletResponse response, String errorMessage) {
        LOGGER.fatal("probleme d'authentification", errorMessage);
        try {
            response.sendError(401, "Please log in to access this page");
        } catch (Exception e1) {
            LOGGER.fatal(e1);
        }
    }

    public static void print500(HttpServletResponse response, SQLException e) {
        LOGGER.fatal("probleme avec la requete sql", e);
        try {
            response.sendError(500, "Something wrong append");
        } catch (Exception e1) {
            LOGGER.fatal(e1);
        }
    }

    public static void print500(HttpServletResponse response, EventServiceException e) {
        LOGGER.fatal("parametre incohérent avec la bdd", e);
        try {
            response.sendError(500, "Something wrong append");
        } catch (Exception e1) {
            LOGGER.fatal(e1);
        }
    }

    public static void print500(HttpServletResponse response, Exception e, String errorMessage) {
        LOGGER.fatal(errorMessage, e);
        try {
            response.sendError(500, "Something wrong append");
        } catch (Exception e1) {
            LOGGER.fatal(e1);
        }
    }

    public static void print404(ExternalContext ec, String errorMessage) {
        LOGGER.fatal(errorMessage);
        try {
            ec.responseSendError(404, "page introuvable");
        } catch (Exception e) {
            LOGGER.fatal(e);
        }

    }
    
    public static void print404(ExternalContext ec, Exception e,String errorMessage) {
        LOGGER.fatal(errorMessage,e);
        try {
            ec.responseSendError(404, "page introuvable");
            
        } catch (Exception e1) {
            LOGGER.fatal(e1);
        }

    }

    public static void print401(ExternalContext ec, String errorMessage) {
        LOGGER.fatal("probleme d'authentification", errorMessage);
        try {
            ec.responseSendError(401, "Please log in to access this page");
        } catch (Exception e1) {
            LOGGER.fatal(e1);
        }
    }
    public static void print500(ExternalContext ec, EventServiceException e) {
        LOGGER.fatal("parametre incohérent avec la bdd", e);
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
