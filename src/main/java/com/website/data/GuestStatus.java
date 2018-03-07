package com.website.data;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * The guest status.</br>
 * Provides the guest status description, the guest status code and the guest status icon path.
 *
 * @author Jérémy Pansier
 */
public enum GuestStatus
{
	/** The default guest status, while he doesn't reply to the invitation. */
	DEFAULT,

	/** The guest status when he accepted the invitation. */
	ACCEPT,

	/** The guest status when he declined the invitation. */
	DECLINE;

	/** The properties file base name. */
	private static final String RESOURCE = GuestStatus.class.getSimpleName();

	/** The position of the status description in the properties file. */
	private static final int STATUS_DESCRIPTION_POSITION = 0;

	/** The position of the status in the properties file. */
	private static final int STATUS_CODE_POSITION = 1;

	/** The position of the status icon path in the properties file. */
	private static final int STATUS_ICON_PATH_POSITION = 2;

	/** The strings separator in the properties file. */
	private static final String SEPARATOR = ",";

	/**
	 * Finds the status localized description.</br>
	 * The localized description depends on the default {@link Locale}.
	 * 
	 * @return the status description
	 * @see Locale#getDefault()
	 */
	public String getDescription()
	{
		final ResourceBundle bundle = ResourceBundle.getBundle(RESOURCE);
		return bundle.getString(name()).split(SEPARATOR)[STATUS_DESCRIPTION_POSITION];
	}

	/**
	 * Finds the status code.</br>
	 * 
	 * @return the status code
	 */
	public int getCode()
	{
		final ResourceBundle bundle = ResourceBundle.getBundle(RESOURCE, Locale.ROOT);
		return Integer.parseInt(bundle.getString(name()).split(SEPARATOR)[STATUS_CODE_POSITION]);
	}

	/**
	 * Finds the status icon path.</br>
	 * 
	 * @return the status icon path
	 */
	public String getIconPath()
	{
		final ResourceBundle bundle = ResourceBundle.getBundle(RESOURCE, Locale.ROOT);
		return bundle.getString(name()).split(SEPARATOR)[STATUS_ICON_PATH_POSITION];
	}

	/**
	 * Gets the status depending on the given code (pending = 0, accept = 1, decline = 2).
	 *
	 * @param code the code corresponding to the wanted status
	 * @return the status corresponding to the given code, or null if the code is not 0, 1 or 2.
	 */
	public static GuestStatus getStatus(final int code)
	{
		for (final GuestStatus status : GuestStatus.values())
		{
			if (status.getCode() == code)
			{
				return status;
			}
		}
		return null;
	}
}
