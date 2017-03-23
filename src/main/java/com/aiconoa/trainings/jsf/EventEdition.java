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
public class EventEdition implements Serializable {
    private static final long serialVersionUID = 1L;
    private ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
    private Integer id;
    private Author author;
    private Event event;
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
    
    public void load() throws IOException {
        try {
            if (id != null) { // Mode edition
                if (!eventService.checkIfEventCorrespondToAuthor(id, sessionUsername, ec)) {
                    return;
                }
                event = eventService.selectEventByIdEvent(id);
            }
        } catch (NullPointerException e) {
            ec.redirect(ec.getRequestContextPath() + "/faces/eventCreation.xhtml");
            return;
        } catch (EventServiceException eventServiceException) {
            HttpErrorHandler.print500(ec, eventServiceException);
            return;
        }
    }
    
    public void upload(FileUploadEvent fue) {
        event.setFileName(Tools.uploadMyFile(fue));
    }
    
    public String editEvent() {
        if (!eventService.checkIfEventCorrespondToAuthor(id, sessionUsername, ec)) {
            return null;
        }
        eventService.updateEvent(id, event.getTitle(), event.getDescription(), event.getFileName());
//        request.setAttribute("flash.message", "Vous avez édité un évènement avec succès");
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        flash.put("flash.message", "Vous avez édité l'évènement avec succès");
        return "eventLink?id=" + id + "faces-redirect=true";
    }
}
