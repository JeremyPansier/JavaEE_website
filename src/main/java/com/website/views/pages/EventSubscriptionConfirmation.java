package com.website.views.pages;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.website.models.entities.Event;
import com.website.models.entities.Guest;
import com.website.models.entities.User;
import com.website.persistence.EventService;
import com.website.persistence.GuestService;
import com.website.persistence.UserService;
import com.website.tools.error.HttpErrorHandler;
import com.website.views.WebPages;

/**
 * The event subscription confirmation web page view.</br>
 * This {@link Named} class is bound with the same named xhtml file.
 * 
 * @author Jérémy Pansier
 */
@Named
@RequestScoped
public class EventSubscriptionConfirmation {

	/** The service managing the event persistence. */
	@Inject
	private EventService eventService;

	/** The service managing the user persistence. */
	@Inject
	private UserService userService;

	/** The service managing the guest persistence. */
	@Inject
	private GuestService guestService;

	/** The web page. */
	public static final WebPages WEB_PAGE = WebPages.EVENT_SUBSCRIPTION_CONFIRMATION;

	/** The guest. */
	private Guest guest;

	/** The event. */
	private Event event;

	/** The user. */
	private User user;

	/** The hash. */
	private String hash;

	/**
	 * Sets fields values on the web page loading, depending on the HTTP request parameter:
	 * <ul>
	 * <li>Sets the guest.</li>
	 * <li>Sets the event.</li>
	 * <li>Sets the user.</li>
	 * </ul>
	 */
	public void load() {
		if (0 == guestService.countGuestsByHash(hash)) {
			HttpErrorHandler.print404("The following hash doesn't exist in the database : " + hash);
			return;
		}
		guest = guestService.findGuestByHash(hash);
		event = eventService.findEventByEventId(guest.getEvent().getId());
		user = userService.findUserById(guest.getUser().getId());
	}

	/**
	 * Gets the web page.
	 *
	 * @return the web page
	 */
	public WebPages getWebPage() {
		return WEB_PAGE;
	}

	/**
	 * Gets the guest.
	 *
	 * @return the guest
	 */
	public Guest getGuest() {
		return guest;
	}

	/**
	 * Sets the guest.
	 *
	 * @param guest the new guest
	 */
	public void setGuest(final Guest guest) {
		this.guest = guest;
	}

	/**
	 * Gets the event.
	 *
	 * @return the event
	 */
	public Event getEvent() {
		return event;
	}

	/**
	 * Sets the event.
	 *
	 * @param event the new event
	 */
	public void setEvent(final Event event) {
		this.event = event;
	}

	/**
	 * Gets the user.
	 *
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Sets the user.
	 *
	 * @param user the new user
	 */
	public void setUser(final User user) {
		this.user = user;
	}

	/**
	 * Gets the hash.
	 *
	 * @return the hash
	 */
	public String getHash() {
		return hash;
	}

	/**
	 * Sets the hash.
	 *
	 * @param hash the new hash
	 */
	public void setHash(final String hash) {
		this.hash = hash;
	}
}
