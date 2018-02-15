package com.website.views.pages;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.website.models.entities.Event;
import com.website.models.entities.Guest;
import com.website.persistence.EventService;
import com.website.persistence.GuestService;
import com.website.tools.EventServiceException;
import com.website.tools.GuestStatus;
import com.website.tools.HttpErrorHandler;
import com.website.tools.Redirector;
import com.website.views.WebPages;

/**
 * The event subscription web page view.</br>
 * This {@link Named} class is bound with the same named xhtml file.
 * 
 * @author Jérémy Pansier
 */
@Named
@RequestScoped
public class EventSubscription
{
	/** The service managing the event persistence. */
	@Inject
	private EventService eventService;

	/** The service managing the guest persistence. */
	@Inject
	private GuestService guestService;

	/** The web page. */
	public static final WebPages WEB_PAGE = WebPages.EVENT_SUBSCRIPTION;

	/** The event. */
	private Event event;

	/** The hash. */
	private String hash;

	/**
	 * Sets the event on the web page loading, depending on the HTTP request parameter:
	 */
	public void load()
	{
		try
		{
			final Guest guest = guestService.selectGuestByHash(hash);
			event = eventService.selectEventByEventId(guest.getEvent().getId());
		}
		catch (final EventServiceException eventServiceException)
		{
			HttpErrorHandler.print500(eventServiceException);
			return;
		}
		catch (final NullPointerException nullPointerException)
		{
			HttpErrorHandler.print404(nullPointerException, "The hash of the current http request doesn't exist in the database");
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
	 * Sets the hash.
	 *
	 * @return the hash
	 */
	public String getHash()
	{
		return hash;
	}

	/**
	 * Sets the hash.
	 *
	 * @param hash the new hash
	 */
	public void setHash(final String hash)
	{
		this.hash = hash;
	}

	/**
	 * Updates the guest status on invitation acceptance.
	 */
	public void accept()
	{
		try
		{
			guestService.updateGuestStatusByHash(hash, GuestStatus.ACCEPT);
		}
		catch (final EventServiceException e)
		{
			HttpErrorHandler.print500(e);
			return;
		}
		Redirector.redirect(WebPages.EVENT_SUBSCRIPTION_CONFIRMATION.createJsfUrl("token", hash));
	}

	/**
	 * Updates the guest status on invitation decline.
	 */
	public void decline()
	{
		try
		{
			guestService.updateGuestStatusByHash(hash, GuestStatus.DECLINE);
		}
		catch (final EventServiceException e)
		{
			HttpErrorHandler.print500(e);
			return;
		}
		Redirector.redirect(WebPages.EVENT_SUBSCRIPTION_CONFIRMATION.createJsfUrl("token", hash));
	}
}
