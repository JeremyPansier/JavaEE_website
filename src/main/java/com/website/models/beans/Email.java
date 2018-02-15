package com.website.models.beans;

/**
 * A model for an email.</br>
 *
 * @author Jérémy Pansier
 */
public class Email
{
	/** The e-mail address. */
	private final String address;

	/** The title of the e-mail message. */
	private final String title;

	/** The hash code identifying the event guest. */
	private final String hash;

	/**
	 * Constructs a new Email.</br>
	 *
	 * @param address the e-mail address
	 * @param title the title of the e-mail message
	 * @param hash the hash code identifying the event guest
	 */
	public Email(final String address, final String title, final String hash)
	{
		this.address = address;
		this.title = title;
		this.hash = hash;
	}

	/**
	 * Gets the e-mail address.
	 *
	 * @return the e-mail address
	 */
	public String getAddress()
	{
		return address;
	}

	/**
	 * Gets the title of the e-mail message.
	 *
	 * @return the title of the e-mail message
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * Gets the hash code identifying the event guest.
	 *
	 * @return the hash code identifying the event guest
	 */
	public String getHash()
	{
		return hash;
	}
}
