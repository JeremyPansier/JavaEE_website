package com.website.tools;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

public class ServletContextTracker {
    
    public static ExternalContext getServletContext() {
            return FacesContext.getCurrentInstance().getExternalContext();
    }
}