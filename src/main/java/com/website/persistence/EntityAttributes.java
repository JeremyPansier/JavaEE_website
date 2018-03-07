package com.website.persistence;

/**
 * The persistence entity attributes.</br>
 *
 * @author Jérémy Pansier
 */
public enum EntityAttributes {

	/** The id. */
	ID("id"),

	/** The user id. */
	USERID("userId"),

	/** The status. */
	STATUS("status"),

	/** The name. */
	NAME("name"),

	/** The hash. */
	HASH("hash"),

	/** The email. */
	EMAIL("email"),

	/** The URL. */
	URL("url");

	/** The entity attribute name. */
	private String name;

	/**
	 * Instantiates a new entity attribute.</br>
	 *
	 * @param name the entity attribute name
	 */
	private EntityAttributes(final String name) {
		this.name = name;
	}

	/**
	 * Gets the entity attribute name.
	 *
	 * @return the entity attribute name
	 */
	public String getName() {
		return name;
	}
}
