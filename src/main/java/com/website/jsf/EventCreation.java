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
public class EventCreation implements Serializable
{
	private static final long serialVersionUID = 6144143620477608461L;
	private Long id;
	private Author author;
	private Event event = new Event();
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

	public void upload(FileUploadEvent fue)
	{
		event.setFilename(Uploader.uploadFile(fue));
	}

	public void createEvent() throws IOException
	{
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
		String message = "avec succès";
		if (event.getFilename() == null)
		{
			event.setFilename("");
			message = "sans image";
		}
		try
		{
			id = eventService.insertEvent(author.getId(), event.getTitle(), event.getDescription(), event.getFilename());
		} catch (EventServiceException eventServiceException)
		{
			HttpErrorHandler.print500(ec, eventServiceException);
			return;
		}
		flash.put("flash.message", "Évènement créé " + message);
		ec.redirect(PathBuilder.getJsfPath(Webpagename.eventManagement.toString(), "eventId", id));
	}
}
