package com.website.jsf;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;

import com.website.entities.Author;
import com.website.entities.Event;
import com.website.enumeration.Webpagename;
import com.website.services.EventService;
import com.website.tools.EventServiceException;
import com.website.tools.HttpErrorHandler;
import com.website.tools.PathBuilder;
import com.website.tools.SessionChecker;
import com.website.tools.Uploader;

@Named
@ViewScoped
public class EventEdition implements Serializable
{
	private static final long serialVersionUID = -7682934518094297522L;
	private Long id;
	private Author author;
	private Event event;
	private String username;
	@Inject
	private EventService eventService;

	@PostConstruct
	public void init()
	{
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		try
		{
			username = SessionChecker.getUsername(ec);
			author = eventService.selectAuthorByUsername(username);
		} catch (EventServiceException eventServiceException)
		{
			HttpErrorHandler.print500(ec, eventServiceException);
			return;
		}
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Author getAuthor()
	{
		return author;
	}

	public void setAuthor(Author author)
	{
		this.author = author;
	}

	public Event getEvent()
	{
		return event;
	}

	public void setEvent(Event event)
	{
		this.event = event;
	}

	public void load() throws IOException
	{
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		try
		{
			if (id != null)
			{
				if (!eventService.isEventsAuthor(id, username, ec))
				{
					return;
				}
				event = eventService.selectEventByEventId(id);
			}
		} catch (NullPointerException e)
		{
			ec.redirect(PathBuilder.getJsfPath(Webpagename.eventsList.toString()));
			return;
		} catch (EventServiceException eventServiceException)
		{
			HttpErrorHandler.print500(ec, eventServiceException);
			return;
		}
	}

	public void upload(FileUploadEvent fue)
	{
		event.setFilename(Uploader.uploadFile(fue));
	}

	public void editEvent() throws IOException
	{
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		if (!eventService.isEventsAuthor(id, username, ec))
		{
			return;
		}
		eventService.updateEvent(id, event.getTitle(), event.getDescription(), event.getFilename());
		Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
		flash.put("flash.message", "Évènement édité avec succès");
		ec.redirect(PathBuilder.getJsfPath(Webpagename.eventManagement.toString(), "eventId", id));
	}
}
