package com.website.jsf;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.website.cdi.EmailList;
import com.website.entities.Author;
import com.website.entities.Event;
import com.website.entities.Subscriber;
import com.website.services.EventService;
import com.website.tools.EventServiceException;
import com.website.tools.HttpErrorHandler;
import com.website.tools.SessionChecker;

@Named
@ViewScoped
public class EventManagement implements Serializable
{
	private static final long serialVersionUID = 5873143732364895613L;
	@Inject
	private EventService eventService;
	private Author author;
	private String username;
	private Long id;
	private Event event;
	private List<Subscriber> subscribers;
	@EmailList
	private String emailList;

	public Author getAuthor()
	{
		return author;
	}

	public void setAuthor(Author author)
	{
		this.author = author;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Event getEvent()
	{
		return event;
	}

	public void setEvent(Event event)
	{
		this.event = event;
	}

	public List<Subscriber> getSubscribers()
	{
		return subscribers;
	}

	public void setSubscribers(List<Subscriber> subscribers)
	{
		this.subscribers = subscribers;
	}

	public String getEmailList()
	{
		return emailList;
	}

	public void setEmailList(String emailList)
	{
		this.emailList = emailList;
	}

	public int getSize()
	{
		return subscribers.size();
	}

	public void init()
	{
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		try
		{
			username = SessionChecker.getUsername(ec);
			if (!eventService.isEventsAuthor(id, username, ec))
			{
				return;
			}
			author = eventService.selectAuthorByUsername(username);
			event = eventService.selectEventByEventId(id);
			subscribers = eventService.selectSubscribersByEventId(id);
		} catch (EventServiceException e)
		{
			HttpErrorHandler.print500(ec, e);
			return;
		} catch (IllegalStateException e)
		{
			HttpErrorHandler.print500(ec, e, "forward impossible");
			return;
		}
	}

	public void addSubscribers(ActionEvent actionEvent)
	{
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		try
		{
			username = SessionChecker.getUsername(ec);
			if (!eventService.isEventsAuthor(id, username, ec))
			{
				return;
			}
			event = eventService.selectEventByEventId(id);
			if (emailList == null || emailList.trim().isEmpty())
			{
				return;
			}
			eventService.insertSubscribers(event, emailList);
			subscribers = eventService.selectSubscribersByEventId(id);
		} catch (NumberFormatException e)
		{
			HttpErrorHandler.print404(ec, e, "Le parametre de la requete http ne peut pas etre converti en Integer dans le doPost de EventLinkServlet");
			return;
		} catch (EventServiceException e)
		{
			HttpErrorHandler.print500(ec, e);
			return;
		}
	}

	public void removeSubscriber(Subscriber subscriber) throws IOException
	{
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		try
		{
			eventService.deleteSubscriber(subscriber.getId());
			subscribers = eventService.selectSubscribersByEventId(id);
		} catch (EventServiceException eventServiceException)
		{
			HttpErrorHandler.print500(ec, eventServiceException);
			return;
		}
	}
}
