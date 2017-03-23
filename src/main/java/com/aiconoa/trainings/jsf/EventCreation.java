package com.aiconoa.trainings.jsf;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;

import com.aiconoa.trainings.entity.Author;
import com.aiconoa.trainings.entity.Event;
import com.aiconoa.trainings.services.EventService;
import com.aiconoa.trainings.services.EventServiceException;
import com.aiconoa.trainings.services.HttpErrorHandler;
import com.aiconoa.trainings.services.LoginService;
import com.aiconoa.trainings.services.Tools;

@Named
@ViewScoped
public class EventCreation implements Serializable {
    private static final long serialVersionUID = 1L;
    private ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
    private Integer id;
    private Author author;
    private Event event = new Event();
    private String sessionUsername;
    @Inject
    private EventService eventService;
    @Inject
    private LoginService loginService;

    @PostConstruct
    public void init() throws IOException {
        try {
            Map<String, Object> httpSession = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
            if (httpSession.get("username") == null) {
                ec.redirect("login.xhtml");
                return;
            }
            sessionUsername = httpSession.get("username").toString();
            author = loginService.selectIdAuthorByUsername(sessionUsername);
        } catch (EventServiceException eventServiceException) {
            HttpErrorHandler.print500(ec, eventServiceException);
            return;
        }
    }
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Author getAuthor() {
        return author;
    }
    public void setAuthor(Author author) {
        this.author = author;
    }
    public Event getEvent() {
        return event;
    }
    public void setEvent(Event event) {
        this.event = event;
    }
        
    public void upload(FileUploadEvent fue) {
        event.setFileName(Tools.uploadMyFile(fue));
    }
    
    public String createEvent() {
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        if (event.getFileName() == null) {
            flash.put("flash.message", "Vous devez ajouter une image !");
            return "eventCreation";
        }
        try {
            id = eventService.insertEvent(event.getTitle(), event.getDescription(), event.getFileName(), author.getIdAuthor());
        } catch (EventServiceException eventServiceException) {
            HttpErrorHandler.print500(ec, eventServiceException);
            return null;
        }
        flash.put("flash.message", "Vous avez créé un évènement avec succès");

        return "eventLink?id=" + id + "faces-redirect=true";
    }
}

