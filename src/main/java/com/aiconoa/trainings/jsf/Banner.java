package com.aiconoa.trainings.jsf;

import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Named;

@Named
@RequestScoped
public class Banner {

    private String message;
    private String username;

    public String getMessage() {
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        return (String) flash.get("flash.message");
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        Map<String, Object> httpSession = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        return (String) httpSession.get("username");
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
