package com.website.views.pages;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;

import com.website.models.entities.Author;
import com.website.models.entities.Event;
import com.website.persistence.AuthorService;
import com.website.persistence.EventService;
import com.website.tools.EventServiceException;
import com.website.tools.HttpErrorHandler;
import com.website.tools.MessageManager;
import com.website.tools.Redirector;
import com.website.tools.SessionManager;
import com.website.tools.Uploader;
import com.website.views.WebPages;

/**
 * The event creation web page view.</br>
 * This {@link Named} class is bound with the same named xhtml file.
 * 
 * @author Jérémy Pansier
 */
@Named
@ViewScoped
public class EventCreation implements Serializable
{

	/** The serial version UID. */
	private static final long serialVersionUID = 6144143620477608461L;

	/** The web page. */
	public static final WebPages WEB_PAGE = WebPages.EVENT_CREATION;

	/** The service managing the event persistence. */
	@Inject
	private EventService eventService;

	/** The service managing the author persistence. */
	@Inject
	private AuthorService authorService;

	/** The event. */
	private transient Event event;

	/** The author. */
	private transient Author author;

	/**
	 * Initializes the event and the author just after the construction.
	 */
	@PostConstruct
	public void init()
	{
		try
		{
			event = new Event();
			final String sessionUserName = SessionManager.checkSessionUserName();
			author = authorService.selectAuthorByAuthorName(sessionUserName);
		}
		catch (final EventServiceException eventServiceException)
		{
			HttpErrorHandler.print500(eventServiceException);
			return;
		}
	}

	/**
	 * @return the web page
	 */
	public WebPages getWebPage()
	{
		return WEB_PAGE;
	}

	/**
	 * Gets the event.
	 *
	 * @return the event
	 */
	public Event getEvent()
	{
		return event;
	}

	/**
	 * Gets the author.
	 *
	 * @return the author
	 */
	public Author getAuthor()
	{
		return author;
	}

	/**
	 * Uploads the file corresponding to the specified file upload event.
	 *
	 * @param fileUploadEvent the file upload event
	 */
	public void upload(final FileUploadEvent fileUploadEvent)
	{
		final String filename = Uploader.uploadFile(fileUploadEvent);
		event.setFilename(filename);
	}

	/**
	 * Creates the event.
	 */
	public void createEvent()
	{
		String message = "avec succès";
		if (null == event.getFilename())
		{
			event.setFilename("");
			message = "sans image";
		}
		Long id = null;
		try
		{
			id = eventService.insertEvent(author.getId(), event.getTitle(), event.getDescription(), event.getFilename());
		}
		catch (final EventServiceException eventServiceException)
		{
			HttpErrorHandler.print500(eventServiceException);
			return;
		}
		MessageManager.putMessage("Évènement créé " + message);
		Redirector.redirect(WebPages.EVENT_MANAGEMENT.createJsfUrl("eventId", id));
	}
}
