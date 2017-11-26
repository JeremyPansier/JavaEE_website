package com.website.jsf;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.website.entities.Author;
import com.website.entities.Event;
import com.website.enumeration.Webpagename;
import com.website.services.EventService;
import com.website.tools.EventServiceException;
import com.website.tools.HttpErrorHandler;
import com.website.tools.PathBuilder;
import com.website.tools.SessionChecker;

@Named
@RequestScoped
public class EventsList {
    @Inject
    private EventService eventService;

    private String username;
    private List<Event> events;
    private Author author;

    @PostConstruct
    public void init() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        username = SessionChecker.getUsername(ec);
        
        try {
            events = eventService.selectEventsByAuthorUsername(username);
            author = eventService.selectAuthorByUsername(username);
        } catch (EventServiceException e) {
            HttpErrorHandler.print500(ec, e);
            return;
        }
    }
    
    public List<Event> getEvents() {
        return events;
    }

    public Author getAuthor() {
        return author;
    }

    public int getSize() {
        return events.size();
    }
    
    public void createNewEvent() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(PathBuilder.getJsfPath(Webpagename.eventCreation.toString()));
    }

    public void removeEvent(Event event) throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            eventService.deleteEvent(event.getId());
        } catch (EventServiceException eventServiceException) {
            HttpErrorHandler.print500(ec, eventServiceException);
            return;
        }
        ec.redirect(PathBuilder.getJsfPath(Webpagename.eventsList.toString()));
    }
}
