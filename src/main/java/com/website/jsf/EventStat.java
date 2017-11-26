package com.website.jsf;

import java.io.Serializable;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.website.beans.EventStatData;
import com.website.entities.Author;
import com.website.entities.Event;
import com.website.entities.Subscriber;
import com.website.services.EventService;
import com.website.tools.EventServiceException;
import com.website.tools.HttpErrorHandler;
import com.website.tools.SessionChecker;

@Named
@ViewScoped
public class EventStat implements Serializable
{
	private static final long serialVersionUID = 4353732048329697944L;
	private static final int ACCEPT = 1;
	private static final int DECLINE = 2;
	@Inject
	private EventService eventService;

	private Author author;
	private String username;
	private Long id;
	private EventStatData eventStatData = new EventStatData();
	private Event event;
	private List<Subscriber> subscribers;

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

	public EventStatData getEventStatData()
	{
		return eventStatData;
	}

	public void setEventStatData(EventStatData eventStatData)
	{
		this.eventStatData = eventStatData;
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

			eventStatData.setIdEvent(id);

			// recherche des membres ajoutés et statuts
			eventStatData.setNbTotalInvitation(subscribers.size());

			// recherche du nombre de participants ACCEPT
			eventStatData.setNbIsPresent(eventService.countSubscribersByEventAndByStatus(id, ACCEPT));

			// recherche du nombre de participants DECLINE
			eventStatData.setNbIsNotPresent(eventService.countSubscribersByEventAndByStatus(id, DECLINE));

			// recherche du nombre de mails lus
			eventStatData.setNbIsRead(eventService.countReadEmailsByEvent(id));

			// recherche du nombre de presents apres ouverture du mail
			eventStatData.setNbIsPresentAfterReading(
					eventService.countSubscribersBySatusAndByEventAfterEmailReading(id, ACCEPT));

			// recherche du nombre d'absents apres ouverture du mail
			eventStatData.setNbIsAbsentAfterReading(
					eventService.countSubscribersBySatusAndByEventAfterEmailReading(id, DECLINE));

		} catch (EventServiceException e)
		{
			HttpErrorHandler.print500(ec, e);
			return;
		} catch (NumberFormatException e)
		{
			HttpErrorHandler.print404(ec, e, "Cannot convert to Integer. Method: " + Thread.currentThread().getStackTrace()[1].getMethodName() + " Class: " + this.getClass().getName());
			return;
		} catch (IllegalStateException exception)
		{
			HttpErrorHandler.print500(ec, exception, "forward impossible");
			return;
		}
	}
}
