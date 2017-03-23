package com.aiconoa.trainings.jsf;

import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aiconoa.trainings.entity.Author;
import com.aiconoa.trainings.entity.Event;
import com.aiconoa.trainings.entity.EventStatData;
import com.aiconoa.trainings.entity.Subscriber;
import com.aiconoa.trainings.services.EventService;
import com.aiconoa.trainings.services.EventServiceException;
import com.aiconoa.trainings.services.HttpErrorHandler;
import com.aiconoa.trainings.services.LoginService;

@Named
@RequestScoped
public class EventStat {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final int ACCEPT = 1;
    private static final int DECLINE = 2;
    private static final int PRESENT = 1;
    private static final int NOTPRESENT = 2;
    @Inject
    private EventService eventService;
    @Inject
    private LoginService loginService;

    private Author author;
    private String username;
    private int idEvent;
    private EventStatData eventStatData= new EventStatData();
    private Event event;
    private List<Subscriber> subscribers;

    
    public void init() {
        Map<String, Object> httpSession = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        username=(String) httpSession.get("username");
        if (httpSession.get("username") == null) {
            try {
                ec.redirect(ec.getRequestContextPath() + "/faces/login.xhtml");
            } catch (Exception e) {
                LOGGER.error("redirection impossible", e);
            }
            return;
        }

        try {

            if (!eventService.checkIfEventCorrespondToAuthor(idEvent, username, ec)) {
                return;
            }

            eventStatData.setIdEvent(idEvent);

            // recherche de l'evenement
            event = eventService.selectEventByIdEvent(idEvent);

            // recherche de l'auteur
            setAuthor(loginService.selectIdAuthorByUsername(username));

            // recherche des membres ajoutés et statuts
            subscribers = eventService.listSelectSubscribersByIdEvent(idEvent);
            eventStatData.setNbTotalInvitation(subscribers.size());

            // recherche du nombre de participants ACCEPT
            eventStatData.setNbIsPresent(eventService.countNumberPresentOrNot(idEvent, ACCEPT));

            // recherche du nombre de participants DECLINE
            eventStatData.setNbIsNotPresent(eventService.countNumberPresentOrNot(idEvent, DECLINE));

            // recherche du nombre de mails lus
            eventStatData.setNbIsRead(eventService.countNumberMailRead(idEvent));

            // recherche du nombre de presents apres ouverture du mail
            eventStatData.setNbIsPresentAfterReading(
                    eventService.countNbIsPresentOrNotAfterReading(idEvent, PRESENT));

            // recherche du nombre d'absents apres ouverture du mail
            eventStatData.setNbIsAbsentAfterReading(
                    eventService.countNbIsPresentOrNotAfterReading(idEvent, NOTPRESENT));

        } catch (EventServiceException e) {
            HttpErrorHandler.print500(ec, e);
            return;
        } catch (NumberFormatException e) {
            HttpErrorHandler.print404(ec, e, "Le parametre de la requete http ne peut pas etre converti en Integer dans le doGet de EventStatServlet");
            return;
        } catch (IllegalStateException exception) {
            HttpErrorHandler.print500(ec, exception, "forward impossible");
            return;
        }

    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(int idEvent) {
        this.idEvent = idEvent;
    }

    public EventStatData getEventStatData() {
        return eventStatData;
    }

    public void setEventStatData(EventStatData eventStatData) {
        this.eventStatData = eventStatData;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public List<Subscriber> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(List<Subscriber> subscribers) {
        this.subscribers = subscribers;
    }

}
