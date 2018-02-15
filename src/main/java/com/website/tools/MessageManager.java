package com.website.tools;

/**
 * The message manager.</br>
 *
 * @author Jérémy Pansier
 */
public class MessageManager
{
	/**
	 * Private constructor to hide the implicit empty public one.
	 */
	private MessageManager()
	{
	}

	/**
	 * Puts the given message in the context map.</br>
	 * 
	 * @param value the message content
	 */
	public static void putMessage(final String message)
	{
		ContextManager.putMessage(message);
	}

	/**
	 * Gets the message stored in the context map.
	 *
	 * @return the message stored in the context map
	 */
	public static String getMessage()
	{
		return ContextManager.getMessage();
	}
}
