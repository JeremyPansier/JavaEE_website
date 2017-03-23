package com.aiconoa.trainings.jsf;

import java.io.IOException;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

@Named
@RequestScoped
public class Logout {

    public void logout() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        session.invalidate();
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        flash.put("flash.message", "Vous vous êtes déconnectés avec succès");
        ec.redirect("login.xhtml");
    }
}
