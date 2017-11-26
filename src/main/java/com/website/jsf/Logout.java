package com.website.jsf;

import java.io.IOException;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import com.website.enumeration.Webpagename;
import com.website.tools.PathBuilder;

@Named
@RequestScoped
public class Logout {

    public void logout() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        session.invalidate();
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        flash.put("flash.message", "Déconnection réussie");
        ec.redirect(PathBuilder.getJsfPath(Webpagename.home.toString()));
    }
}
