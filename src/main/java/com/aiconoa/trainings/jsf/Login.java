package com.aiconoa.trainings.jsf;

import java.io.IOException;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.inject.Named;

import org.hibernate.validator.constraints.NotBlank;
import org.mindrot.jbcrypt.BCrypt;

import com.aiconoa.trainings.services.LoginService;

@Named
@RequestScoped
public class Login {
    
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String password2;
    @Inject
    private LoginService loginService;
    private Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
    
    
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword2() {
        return password2;
    }
    public void setPassword2(String password2) {
        this.password2 = password2;
    }
    
    public String login() throws IOException{
        Map<String, Object> httpSession = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        
        if (httpSession.get("username") != username) {
            httpSession.put("username", username);
        }
        
        if (!loginService.checkIfAuthorExist(username)) {
            flash.put("flash.message", "Utilisateur inconnu");
            return null;
        }

        String passwordDB = loginService.getPasswordByUserName(username);

        if (!BCrypt.checkpw(password, passwordDB)) {
            flash.put("flash.message", "Mauvais password");
            return null;
        }
        
        flash.put("flash.message", "Vous vous êtes connectés avec succès");
        return "eventsList?faces-redirect=true";
    }

    public String register() throws IOException{
        Map<String, Object> httpSession = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        
        if (httpSession.get("username") != username) {
            httpSession.put("username", username);
        }

        if (loginService.checkIfAuthorExist(username)) {
            flash.put("flash.message", "le pseudonyme \"" + username + "\" est déjà pris");
            return "register?faces-redirect=true";
        }

        if (password.compareTo(password2) != 0) {
            flash.put("flash.message", "Les mots de passes ne sont pas identiques");
            return "register?faces-redirect=true";
        }
        
        String passwordDB = BCrypt.hashpw(password, BCrypt.gensalt(12));
        loginService.insertAuthor(username, passwordDB);
        flash.put("flash.message", "Vous vous êtes enregistré avec succès");
        return "eventCreation?faces-redirect=true";
    }

}
