package com.aiconoa.trainings.jsf;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.aiconoa.trainings.entity.Event;
import com.aiconoa.trainings.entity.StatusAcceptDecline;
import com.aiconoa.trainings.entity.Subscriber;
import com.aiconoa.trainings.entity.User;
import com.aiconoa.trainings.services.EventService;
import com.aiconoa.trainings.services.EventServiceException;
import com.aiconoa.trainings.services.HttpErrorHandler;

@Named
@RequestScoped
public class EventConfirm {
    @Inject
    private EventService eventService;
    private ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
    private Subscriber subscriber;
    private Event event;
    private User user;
    private String hash;

    public void init() {
        try {
            subscriber = eventService.selectSubscriberByHashcode(hash);
            event = eventService.selectEventByIdEvent(subscriber.getIdEvent());
            user = eventService.selectUserByIdUser(subscriber.getIdUser());
        } catch (EventServiceException eventServiceException) {
            HttpErrorHandler.print500(ec, eventServiceException);
            return;
        } catch (NullPointerException nullPointerException) {
            HttpErrorHandler.print404(ec, nullPointerException, "le hashcode n'existe pas dans la bdd");
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

    public String choose(String choice) {
        try {
            int responseUser = StatusAcceptDecline.valueOf(choice).getStatus();
            eventService.updateSubscribersIsPresentWhereHashCode(hash, responseUser);
        } catch (EventServiceException e) {
            HttpErrorHandler.print500(ec, e);
            return null;
        }
        return "eventConfirmValidation?token=" + hash + "faces-redirect=true";
    }
}
