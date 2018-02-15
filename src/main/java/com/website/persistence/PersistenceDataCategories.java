package com.website.persistence;

/**
 * The persistence data categories.</br>
 *
 * @author Jérémy Pansier
 */
public enum PersistenceDataCategories
{
	EVENTID("eventId"),
	USERID("userId"),
	STATUS("status"),
	NAME("name"),
	HASH("hash"),
	EMAIL("email"),
	PICTUREID("pictureId"),
	ID("id");

	/** The persistence data category name. */
	private String name;

	/**
	 * Instantiates a new persistence data category.</br>
	 *
	 * @param name the name
	 */
	private PersistenceDataCategories(final String name)
	{
		this.name = name;
	}

	/**
	 * Gets the persistence data category name.
	 *
	 * @return the persistence data category name
	 */
	public String getName()
	{
		return name;
	}
}
