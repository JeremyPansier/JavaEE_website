package com.aiconoa.trainings.jsf;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aiconoa.trainings.entity.Author;
import com.aiconoa.trainings.entity.Event;
import com.aiconoa.trainings.entity.Subscriber;
import com.aiconoa.trainings.services.EventService;
import com.aiconoa.trainings.services.EventServiceException;
import com.aiconoa.trainings.services.HttpErrorHandler;
import com.aiconoa.trainings.services.LoginService;

@Named
@ViewScoped
public class EventLink implements Serializable{
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String USERNAME = "username";
    @Inject
    private EventService eventService;
    @Inject
    private LoginService loginService;
    private Author author;
    private String username;
    private int idEvent;
    private Event event;
    private List<Subscriber> subscribers;
    @EmailList
    private String emailList;
    
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

    public String getEmailList() {
        return emailList;
    }
    
    public void init() {
        Map<String, Object> httpSession = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            username = (String) httpSession.get(USERNAME);
            if (httpSession.get("username") == null) {
                try {
                    ec.redirect(ec.getRequestContextPath() + "/faces/login.xhtml");
                } catch (Exception e) {
                    LOGGER.error("redirection impossible", e);
                }
                return;
            }
            
            if (!eventService.checkIfEventCorrespondToAuthor(idEvent, username, ec)) {
                return;
            }
            // recherche de l'autheur
            author = loginService.selectIdAuthorByUsername(username);
            // recherche de l'evenement par l'idEvent
            event = eventService.selectEventByIdEvent(idEvent);
            // recherche des membres ajoutés et statuts pour l'IdEvent
            subscribers = eventService.listSelectSubscribersByIdEvent(idEvent);
            
        } catch (EventServiceException e) {
            HttpErrorHandler.print500(ec, e);
            return;
        } catch (IllegalStateException e) {
            HttpErrorHandler.print500(ec, e, "forward impossible");
            return;
        }
    }
    
    public void executeEventLink() {
        Map<String, Object> httpSession = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            username = (String) httpSession.get(USERNAME);
            if (httpSession.get("username") == null) {
                try {
                    ec.redirect(ec.getRequestContextPath() + "/faces/login.xhtml");
                } catch (Exception e) {
                    LOGGER.error("redirection impossible", e);
                }
                return;
            }
            LOGGER.info("ici impossible");
            if (!eventService.checkIfEventCorrespondToAuthor(idEvent, username, ec)) {
                return;
            }
            
            // recherche de l'evenement par l'idEvent
            event = eventService.selectEventByIdEvent(idEvent);
            
            if (emailList == null || emailList.trim().isEmpty()) {
                ec.redirect(ec.getRequestContextPath() + "/faces/eventLink.xhtml?id=" + idEvent);
                return;
            }
            eventService.addSubscribersToEvent(event, emailList);
            ec.redirect(ec.getRequestContextPath() + "/faces/eventLink.xhtml?id=" + idEvent);
            
        } catch (NumberFormatException e) {
            HttpErrorHandler.print404(ec, e, "Le parametre de la requete http ne peut pas etre converti en Integer dans le doPost de EventLinkServlet");
            return;
        } catch (EventServiceException e) {
            HttpErrorHandler.print500(ec, e);
            return;
        } catch (IOException | IllegalStateException e) {
            HttpErrorHandler.print500(ec, e, "redirection impossible");
            return;
        }
    }
    
    public void setEmailList(String emailList) {
        this.emailList = emailList;
    }
    
    public void removeSubscriber(Subscriber subscriber) throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            eventService.deleteSubscriber(subscriber.getId());
            ec.redirect(ec.getRequestContextPath() + "/faces/eventLink.xhtml?id=" + idEvent);
        } catch (EventServiceException eventServiceException) {
            HttpErrorHandler.print500(ec, eventServiceException);
            return;
        } catch (IOException | IllegalStateException e) {
            HttpErrorHandler.print500(ec, e, "redirection impossible");
            return;
        }
        return;
    }

}
