package com.website.views.pages;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.website.models.entities.Author;
import com.website.models.entities.Event;
import com.website.models.entities.Guest;
import com.website.persistence.AuthorService;
import com.website.persistence.EventService;
import com.website.persistence.GuestService;
import com.website.tools.EventDataManager;
import com.website.tools.EventServiceException;
import com.website.tools.GuestStatus;
import com.website.tools.HttpErrorHandler;
import com.website.tools.SessionManager;
import com.website.views.WebPages;

/**
 * The event report web page view.</br>
 * This {@link Named} class is bound with the same named xhtml file.
 * 
 * @author Jérémy Pansier
 */
@Named
@ViewScoped
public class EventReport implements Serializable
{
	/** The serial version UID. */
	private static final long serialVersionUID = 4353732048329697944L;

	/** The web page. */
	public static final WebPages WEB_PAGE = WebPages.EVENT_REPORT;

	/** The service managing the event persistence. */
	@Inject
	private EventService eventService;

	/** The service managing the event persistence. */
	@Inject
	private AuthorService authorService;

	/** The service managing the guest persistence. */
	@Inject
	private GuestService guestService;

	/** The event id HTTP request parameter. */
	private Long id;

	/** The author. */
	private transient Author author;

	/** The event. */
	private transient Event event;

	/** The guests. */
	private transient List<Guest> guests;

	/** The event data manager. */
	private transient EventDataManager eventDataManager = new EventDataManager();

	/**
	 * @return the web page
	 */
	public WebPages getWebPage()
	{
		return WEB_PAGE;
	}

	/**
	 * Gets the event id HTTP request parameter.
	 *
	 * @return the event id HTTP request parameter
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * Sets the event id HTTP request parameter.
	 *
	 * @param id the new event id HTTP request parameter
	 */
	public void setId(final Long id)
	{
		this.id = id;
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
	 * Gets the guests.
	 *
	 * @return the guests
	 */
	public List<Guest> getGuests()
	{
		return guests;
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
	 * Gets the event data manager.
	 *
	 * @return the event data manager
	 */
	public EventDataManager getEventDataManager()
	{
		return eventDataManager;
	}

	/**
	 * Sets fields values on the web page loading, depending on the HTTP request parameter:
	 * <ul>
	 * <li>Sets the event.</li>
	 * <li>Sets the guests.</li>
	 * </ul>
	 */
	public void load()
	{
		try
		{
			final String sessionUserName = SessionManager.checkSessionUserName();
			if (!authorService.isEventsAuthor(id, sessionUserName))
			{
				return;
			}
			author = authorService.selectAuthorByAuthorName(sessionUserName);
			event = eventService.selectEventByEventId(id);
			guests = guestService.selectGuestsByEventId(id);

			eventDataManager.setEventId(id);

			// count event guests
			eventDataManager.setInvitationsCount(guests.size());

			// count event guests who accepted the invitation
			eventDataManager.setAcceptedInvitationsCount(guestService.countGuestsByEventAndByStatus(id, GuestStatus.ACCEPT.getCode()));

			// count event guests who declined the invitation
			eventDataManager.setDeclinedInvitationsCount(guestService.countGuestsByEventAndByStatus(id, GuestStatus.DECLINE.getCode()));

			// count event guests who read the invitation email
			eventDataManager.setKnownInvitationsCount(guestService.countReadEmailsByEvent(id));

			// count event guests who accepted the invitation after reading the invitation email
			eventDataManager.setAcceptedKnownInvitationsCount(
					guestService.countGuestsBySatusAndByEventAfterEmailReading(id, GuestStatus.ACCEPT.getCode()));

			// count event guests who declined the invitation after reading the invitation email
			eventDataManager.setDeclinedKnownInvitationsCount(
					guestService.countGuestsBySatusAndByEventAfterEmailReading(id, GuestStatus.DECLINE.getCode()));

		}
		catch (final EventServiceException e)
		{
			HttpErrorHandler.print500(e);
			return;
		}
		catch (final NumberFormatException e)
		{
			HttpErrorHandler.print404(e, "Cannot convert to Integer. Method: " + Thread.currentThread().getStackTrace()[1].getMethodName() + " Class: " + this.getClass().getName());
			return;
		}
		catch (final IllegalStateException e)
		{
			HttpErrorHandler.print500(e, "forward impossible");
			return;
		}
	}
}
