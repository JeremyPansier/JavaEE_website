package com.aiconoa.trainings.jsf;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.aiconoa.trainings.entity.Author;
import com.aiconoa.trainings.entity.Event;
import com.aiconoa.trainings.services.EventService;
import com.aiconoa.trainings.services.EventServiceException;
import com.aiconoa.trainings.services.HttpErrorHandler;
import com.aiconoa.trainings.services.LoginService;

@Named
@RequestScoped
public class EventsList {
    @Inject
    private EventService eventService;
    @Inject
    private LoginService loginService;

    private String username;
    private List<Event> events;
    private Author author;
    private Event event;

    @PostConstruct
    public void init() throws IOException {
        Map<String, Object> httpSession = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        if (httpSession.get("username") == null) {
            ec.redirect(ec.getRequestContextPath() + "/faces/login.xhtml");
            return;
        }
        
        String sessionUsername = httpSession.get("username").toString();
        
        try {
            events = eventService.listSelectEventColumns(sessionUsername);
            author = loginService.selectIdAuthorByUsername(sessionUsername);
        } catch (EventServiceException e) {
            HttpErrorHandler.print500(ec, e);
            return;
        }
        
        return;
    }
    
    public String getUsername() {
        return username;
    }

    public List<Event> getEvents() {
        return events;
    }

    public Author getAuthor() {
        return author;
    }

    public Event getEvent() {
        return event;
    }

    public String createNewEvent() throws IOException {
        return "eventCreation?faces-redirect=true";
    }

    public String removeEvent(Event event) throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            eventService.deleteEvent(event.getIdEvent());
        } catch (EventServiceException eventServiceException) {
            HttpErrorHandler.print500(ec, eventServiceException);
            return null;
        }
        return "eventsList?faces-redirect=true";
    }
}
