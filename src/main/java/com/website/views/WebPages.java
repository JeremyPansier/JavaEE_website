package com.website.views;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * An enumeration for the titles of the web pages.</br>
 * It can provide localized titles from WebPages.properties files thanks to the method {@link WebPages#getTitle()}
 *
 * @author Jérémy Pansier
 */
public enum WebPages {
	/** The page "index". */
	INDEX,

	/** The page "home". */
	HOME,

	/** The page "connection". */
	CONNECTION,

	/** The page "registration". */
	REGISTRATION,

	/** The page "disconnection". */
	DISCONNECTION,

	/** The page "events list". */
	EVENTS_LIST,

	/** The page "event creation". */
	EVENT_CREATION,

	/** The page "event edition". */
	EVENT_EDITION,

	/** The page "event management". */
	EVENT_MANAGEMENT,

	/** The page "event report". */
	EVENT_REPORT,

	/** The page "event subscription". */
	EVENT_SUBSCRIPTION,

	/** The page "event subscription confirmation". */
	EVENT_SUBSCRIPTION_CONFIRMATION,

	/** The page "pictures gallery". */
	PICTURES_GALLERY,

	/** The page "picture publication". */
	PICTURE_PUBLICATION,

	/** The page "picture edition". */
	PICTURE_EDITION,

	/** The page "picture report". */
	PICTURE_REPORT,

	/** The page "profile edition". */
	PROFILE_EDITION,

	/** The page "visits recording". */
	VISITS_RECORDING;

	/** The properties file base name. */
	private static final String RESOURCE = WebPages.class.getSimpleName();

	/** The position of the title in the properties file. */
	private static final int TITLE_POSITION = 0;

	/** The position of the filename in the properties file. */
	private static final int NAME_POSITION = 1;

	/** The position of the filename in the properties file. */
	private static final int FILENAME_POSITION = 2;

	/** The strings separator in the properties file. */
	private static final String SEPARATOR = ",";

	/** The path of the URL for the faces context. */
	private static final String FACES = "/faces/";

	/** The file extension of the URL. */
	private static final String EXTENSION = ".xhtml";

	/** The question mark indicating the parameter of the URL. */
	private static final String PARAMETER_MARK = "?";

	/** The equal sign indicating the parameter value of the URL. */
	private static final String EQUAL_SIGN = "=";

	/**
	 * Creates the absolute URL for this web page in a JSF context.
	 * 
	 * @return the absolute URL for this web page in a JSF context
	 */
	public String createJsfUrl() {
		return FACES + getFilename() + EXTENSION;
	}

	/**
	 * Creates the absolute URL with parameter for this web page in a JSF context.</br>
	 * The given {@link Long} will be converted to {@link String}.
	 * 
	 * @param parameterName the name of the URL parameter
	 * @param parameterValue the value of the parameter
	 * @return the absolute URL with parameter for this web page in a JSF context
	 * @see Long#toString()
	 */
	public String createJsfUrl(final String parameterName, final Long parameterValue) {
		return createJsfUrl(parameterName, parameterValue.toString());
	}

	/**
	 * Creates the absolute URL with parameter for this web page in a JSF context.
	 * 
	 * @param parameterName the name of the URL parameter
	 * @param parameterValue the value of the parameter
	 * @return the absolute URL with parameter for this web page in a JSF context
	 */
	public String createJsfUrl(final String parameterName, final String parameterValue) {
		return FACES + getFilename() + EXTENSION + PARAMETER_MARK + parameterName + EQUAL_SIGN + parameterValue;
	}

	/**
	 * Creates the absolute URL for this web page in a JSF context.
	 * 
	 * @return the absolute URL for this web page in a JSF context
	 */
	public String createUrl() {
		return getFilename() + EXTENSION;
	}

	/**
	 * Creates the absolute URL with parameter for this web page in a JSF context.</br>
	 * The given {@link Long} will be converted to {@link String}.
	 * 
	 * @param parameterName the name of the URL parameter
	 * @param parameterValue the value of the parameter
	 * @return the absolute URL with parameter for this web page in a JSF context
	 * @see Long#toString()
	 */
	public String createUrl(final String parameterName, final Long parameterValue) {
		return createUrl(parameterName, parameterValue.toString());
	}

	/**
	 * Creates the absolute URL with parameter for this web page in a JSF context.
	 * 
	 * @param parameterName the name of the URL parameter
	 * @param parameterValue the value of the parameter
	 * @return the absolute URL with parameter for this web page in a JSF context
	 */
	public String createUrl(final String parameterName, final String parameterValue) {
		return getFilename() + EXTENSION + PARAMETER_MARK + parameterName + EQUAL_SIGN + parameterValue;
	}

	/**
	 * Finds the file name corresponding to this web page.</br>
	 * 
	 * @return the file name of this web page
	 */
	public String getFilename() {
		final ResourceBundle bundle = ResourceBundle.getBundle(RESOURCE, Locale.ROOT);
		return bundle.getString(name()).split(SEPARATOR)[FILENAME_POSITION];
	}

	/**
	 * Finds the localized title of this web page.</br>
	 * The localized title depends on the default {@link Locale}.
	 * 
	 * @return the localized title of this web page
	 * @see Locale#getDefault()
	 */
	public String getTitle() {
		final ResourceBundle bundle = ResourceBundle.getBundle(RESOURCE);
		return bundle.getString(name()).split(SEPARATOR)[TITLE_POSITION];
	}

	/**
	 * Finds the localized name of this web page.</br>
	 * The localized title depends on the default {@link Locale}.
	 * 
	 * @return the localized title of this web page
	 * @see Locale#getDefault()
	 */
	public String getName() {
		final ResourceBundle bundle = ResourceBundle.getBundle(RESOURCE);
		return bundle.getString(name()).split(SEPARATOR)[NAME_POSITION];
	}
}
