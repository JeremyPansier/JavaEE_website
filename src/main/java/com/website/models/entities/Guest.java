package com.website.models.entities;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.website.data.GuestStatus;
import com.website.persistence.GuestService;

/**
 * The guest model.</br>
 * This class is an entity managed by JPA (Java Persistence API).</br>
 *
 * @author Jérémy Pansier
 */
@Entity
public class Guest {

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** The user corresponding to this guest. */
	@ManyToOne
	@JoinColumn(name = "userId")
	private User user;

	/** The event to which this guest may subscribe. */
	@ManyToOne
	@JoinColumn(name = "eventId")
	private Event event;

	/** The hash used to access the dedicated guest's pages securely without logging in. */
	private String hash;

	/** The status code (pending = 0, accept = 1, decline = 2). */
	private int status;

	/**
	 * If the guest is aware of the invitation to the event, i.e. if he has opened the invitation email (not informed = 0, informed = 1).</br>
	 * This field is used by the guest service directly from the persistence unit. This class only lets to set it.
	 * 
	 * @see GuestService#countGuestsByStatusAndByEventAfterBeingInformed
	 * @see GuestService#countInformedGuestsByEventId
	 */
	@SuppressWarnings("unused")
	private int informed;

	/** The email date. */
	@Column(insertable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date emaildate;

	/**
	 * Default constructor needed by the Java Persistence API.
	 */
	@SuppressWarnings("unused")
	private Guest() {}

	/**
	 * Instantiates a new guest.
	 *
	 * @param user the user corresponding to this guest
	 * @param event the event to which this guest may subscribe
	 */
	public Guest(final User user, final Event event) {
		this.user = user;
		this.event = event;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Long getId() {
		return id;
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
	 * Gets the event.
	 *
	 * @return the event
	 */
	public Event getEvent() {
		return event;
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

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public GuestStatus getStatus() {
		return GuestStatus.getStatus(status);
	}

	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public void setStatus(final GuestStatus status) {
		this.status = status.getCode();
	}

	/**
	 * Sets whether the guest is informed of the invitation to the event.
	 *
	 * @param informed whether the guest is informed of the invitation to the event
	 */
	public void setInformed(final int informed) {
		this.informed = informed;
	}

	/**
	 * Gets the email date formatted into a string.
	 *
	 * @return the formatted email date
	 */
	public String getFormattedEmailDate() {
		final SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
		return formater.format(emaildate);
	}
}
