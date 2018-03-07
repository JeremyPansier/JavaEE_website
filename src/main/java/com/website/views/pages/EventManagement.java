package com.website.views.pages;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.website.managers.email.EmailList;
import com.website.models.entities.Event;
import com.website.models.entities.Guest;
import com.website.persistence.AuthorService;
import com.website.persistence.EventService;
import com.website.persistence.GuestService;
import com.website.tools.error.HttpErrorHandler;
import com.website.tools.navigation.SessionManager;
import com.website.views.WebPages;

/**
 * The event management web page view.</br>
 * This {@link Named} class is bound with the same named xhtml file.
 * 
 * @author Jérémy Pansier
 */
@Named
@ViewScoped
public class EventManagement implements Serializable {

	/** The serial version UID. */
	private static final long serialVersionUID = 5873143732364895613L;

	/** The web page. */
	public static final WebPages WEB_PAGE = WebPages.EVENT_MANAGEMENT;

	/** The service managing the event persistence. */
	@Inject
	private EventService eventService;

	/** The service managing the author persistence. */
	@Inject
	private AuthorService authorService;

	/** The service managing the guest persistence. */
	@Inject
	private GuestService guestService;

	/** The event id HTTP request parameter. */
	private Long id;

	/** The event to manage. */
	private transient Event event;

	/** The event guests. */
	private transient List<Guest> guests;

	/** The e-mails to be sent to guests. */
	@EmailList
	private String emails;

	/**
	 * Sets fields values on the web page loading, depending on the HTTP request parameter:
	 * <ul>
	 * <li>Sets the event.</li>
	 * <li>Sets the guests.</li>
	 * </ul>
	 */
	public void load() {
		try {
			final String username = SessionManager.getSessionUserNameOrRedirect();
			if (!authorService.isEventsAuthor(id, username)) {
				return;
			}
			event = eventService.findEventByEventId(id);
			guests = guestService.findGuestsByEventId(id);
		}
		catch (final IllegalStateException illegalStateException) {
			HttpErrorHandler.print500("forward impossible", illegalStateException);
			return;
		}
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
	 * Gets the event id HTTP request parameter.
	 *
	 * @return the event id HTTP request parameter
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the event id HTTP request parameter.
	 *
	 * @param id the event id HTTP request parameter
	 */
	public void setId(final Long id) {
		this.id = id;
	}

	/**
	 * Gets the event to manage.
	 *
	 * @return the event to manage
	 */
	public Event getEvent() {
		return event;
	}

	/**
	 * Gets the event guests.
	 *
	 * @return the event guests
	 */
	public List<Guest> getGuests() {
		return guests;
	}

	/**
	 * Gets the e-mails to be sent to guests.
	 *
	 * @return the e-mails to be sent to guests
	 */
	public String getEmails() {
		return emails;
	}

	/**
	 * Sets the e-mails to be sent to guests.
	 *
	 * @param emailList the e-mails to be sent to guests
	 */
	public void setEmails(final String emailList) {
		this.emails = emailList;
	}

	/**
	 * Adds guests to the event.
	 */
	public void addGuests() {
		try {
			final String username = SessionManager.getSessionUserNameOrRedirect();
			if (!authorService.isEventsAuthor(id, username)) {
				return;
			}
			event = eventService.findEventByEventId(id);
			if (emails == null || emails.trim().isEmpty()) {
				return;
			}
			for (final String email : emails.split(";")) {
				guestService.persistGuest(event, email);
			}
			guests = guestService.findGuestsByEventId(id);
		}
		catch (final NumberFormatException numberFormatException) {
			HttpErrorHandler.print404("Le parametre de la requete http ne peut pas etre converti en Integer dans le doPost de EventLinkServlet", numberFormatException);
			return;
		}
	}

	/**
	 * Removes the given guest from the event.
	 * 
	 * @param guest the guest to remove from the event
	 */
	public void removeGuest(final Guest guest) {
		guestService.removeGuest(guest);
		guests = guestService.findGuestsByEventId(id);
	}
}
