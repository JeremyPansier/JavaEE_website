package com.website.jsf;

import java.io.IOException;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.website.enumeration.Webpagename;
import com.website.entities.Event;
import com.website.entities.Subscriber;
import com.website.entities.User;
import com.website.enumeration.StatusAcceptDecline;
import com.website.services.EventService;
import com.website.tools.EventServiceException;
import com.website.tools.HttpErrorHandler;
import com.website.tools.PathBuilder;

@Named
@RequestScoped
public class EventSubscription {
    @Inject
    private EventService eventService;
    private Subscriber subscriber;
    private Event event;
    private User user;
    private String hash;

    public void init() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            subscriber = eventService.selectSubscriberByHash(hash);
            event = eventService.selectEventByEventId(subscriber.getIdEvent());
            user = eventService.selectUserById(subscriber.getIdUser());
        } catch (EventServiceException eventServiceException) {
            HttpErrorHandler.print500(ec, eventServiceException);
            return;
        } catch (NullPointerException nullPointerException) {
            HttpErrorHandler.print404(ec, nullPointerException, "The hash of the current http request doesn't exist in the database");
            return;
        }
    }

    public Subscriber getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void choose(String choice) throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            int responseUser = StatusAcceptDecline.valueOf(choice).getIntStatus();
            eventService.updateSubscriberStatusByHash(hash, responseUser);
        } catch (EventServiceException e) {
            HttpErrorHandler.print500(ec, e);
            return;
        }
        ec.redirect(PathBuilder.getJsfPath(Webpagename.eventSubscriptionConfirmation.toString(),"token",hash));
    }
}
