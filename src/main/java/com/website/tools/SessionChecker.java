package com.website.tools;

import java.io.IOException;

import javax.faces.context.ExternalContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.website.enumeration.Webpagename;

public class SessionChecker {
    private static final Logger LOGGER = LogManager.getLogger();

    public static String getUsername(ExternalContext externalContext) {
        try {
            if (externalContext.getSessionMap().get("username") == null) {
                externalContext.redirect(PathBuilder.getJsfPath(Webpagename.login.toString()));
                return null;
            }
        } catch (IOException e) {
            LOGGER.error("Redirection issue", e);
        }
        return externalContext.getSessionMap().get("username").toString();
    }

}
