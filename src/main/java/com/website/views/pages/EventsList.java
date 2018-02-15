package com.website.views.pages;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.website.models.entities.Author;
import com.website.models.entities.Event;
import com.website.persistence.AuthorService;
import com.website.persistence.EventService;
import com.website.tools.EventServiceException;
import com.website.tools.HttpErrorHandler;
import com.website.tools.Redirector;
import com.website.tools.SessionManager;
import com.website.views.WebPages;

/**
 * The events list web page view.</br>
 * This {@link Named} class is bound with the same named xhtml file.
 * 
 * @author Jérémy Pansier
 */
@Named
@RequestScoped
public class EventsList
{
	/** The service managing the event persistence. */
	@Inject
	private EventService eventService;

	/** The service managing the author persistence. */
	@Inject
	private AuthorService authorService;

	/** The web page. */
	public static final WebPages WEB_PAGE = WebPages.EVENTS_LIST;

	/** The events. */
	private List<Event> events;

	/** The author. */
	private Author author;

	/**
	 * Initializes the event and the author just after the construction.
	 */
	@PostConstruct
	public void init()
	{
		final String username = SessionManager.checkSessionUserName();

		try
		{
			events = eventService.selectEventsByAuthorName(username);
			author = authorService.selectAuthorByAuthorName(username);
		}
		catch (final EventServiceException e)
		{
			HttpErrorHandler.print500(e);
			return;
		}
	}

	/**
	 * Gets the web page.
	 *
	 * @return the web page
	 */
	public WebPages getWebPage()
	{
		return WEB_PAGE;
	}

	/**
	 * Gets the events.
	 *
	 * @return the events
	 */
	public List<Event> getEvents()
	{
		return events;
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
	 * Gets the size.
	 *
	 * @return the size
	 */
	public int getSize()
	{
		return events.size();
	}

	/**
	 * Redirects to the event creation web page.
	 */
	public void createNewEvent()
	{
		Redirector.redirect(WebPages.EVENT_CREATION.createJsfUrl());
	}

	/**
	 * Removes the specified event.
	 *
	 * @param event the event to remove
	 */
	public void removeEvent(final Event event)
	{
		try
		{
			eventService.deleteEvent(event.getId());
		}
		catch (final EventServiceException eventServiceException)
		{
			HttpErrorHandler.print500(eventServiceException);
			return;
		}
		Redirector.redirect(WebPages.EVENTS_LIST.createJsfUrl());
	}
}
