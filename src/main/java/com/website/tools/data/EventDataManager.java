package com.website.tools.data;

public class EventDataManager
{
	/** The event id. */
	private Long eventId;

	/** The invitations count. */
	private int invitationsCount;

	/** The accepted invitations count. */
	private Long acceptedInvitationsCount;

	/** The declined invitations count. */
	private Long declinedInvitationsCount;

	/** The count of the invitations pending an answer. */
	private Long pendingAnswerInvitationsCount;

	/** The count of invitations which are known by their receivers (i.e. the invitation e-mail has been opened). */
	private Long knownInvitationsCount;

	/** The count of invitations which are not known by their receivers (i.e. the invitation e-mail has been opened). */
	private Long notKnownInvitationsCount;

	/** The count of invitations which are known and accepted by their receivers (i.e. the invitation has been accepted after opening the invitation e-mail). */
	private Long acceptedKnownInvitationsCount;

	/** The count of invitations which are known and declined by their receivers (i.e. the invitation has been declined after opening the invitation e-mail). */
	private Long declinedKnownInvitationsCount;

	/** The count of invitations which are known and has not been replied by their receivers (i.e. the invitation has nor been accepted neither declined after opening the invitation e-mail). */
	private Long pendingAnswerKnownInvitationsCount;

	/**
	 * Gets the event id.
	 *
	 * @return the event id
	 */
	public Long getEventId()
	{
		return eventId;
	}

	/**
	 * Sets the event id.
	 *
	 * @param eventId the new event id
	 */
	public void setEventId(final Long eventId)
	{
		this.eventId = eventId;
	}

	/**
	 * Gets the invitations count.
	 *
	 * @return the invitations count
	 */
	public int getInvitationsCount()
	{
		return invitationsCount;
	}

	/**
	 * Sets the invitations count.
	 *
	 * @param invitationsCount the new invitations count
	 */
	public void setInvitationsCount(final int invitationsCount)
	{
		this.invitationsCount = invitationsCount;
	}

	/**
	 * Gets the accepted invitations count.
	 *
	 * @return the accepted invitations count
	 */
	public Long getAcceptedInvitationsCount()
	{
		return acceptedInvitationsCount;
	}

	/**
	 * Sets the accepted invitations count.
	 *
	 * @param acceptedInvitationsCount the new accepted invitations count
	 */
	public void setAcceptedInvitationsCount(final Long acceptedInvitationsCount)
	{
		this.acceptedInvitationsCount = acceptedInvitationsCount;
	}

	/**
	 * Gets the declined invitations count.
	 *
	 * @return the declined invitations count
	 */
	public Long getDeclinedInvitationsCount()
	{
		return declinedInvitationsCount;
	}

	/**
	 * Sets the declined invitations count.</br>
	 * Sets the pending answer invitations count.
	 *
	 * @param declinedInvitationsCount the new declined invitations count
	 */
	public void setDeclinedInvitationsCount(final Long declinedInvitationsCount)
	{
		this.declinedInvitationsCount = declinedInvitationsCount;
		pendingAnswerInvitationsCount = invitationsCount - declinedInvitationsCount - acceptedInvitationsCount;
	}

	/**
	 * Gets the count of the invitations pending an answer.
	 *
	 * @return the count of the invitations pending an answer
	 */
	public Long getPendingAnswerInvitationsCount()
	{
		return pendingAnswerInvitationsCount;
	}

	/**
	 * Sets the count of the invitations pending an answer.
	 *
	 * @param pendingAnswerInvitationsCount the new count of the invitations pending an answer
	 */
	public void setPendingAnswerInvitationsCount(final Long pendingAnswerInvitationsCount)
	{
		this.pendingAnswerInvitationsCount = pendingAnswerInvitationsCount;
	}

	/**
	 * Gets the count of invitations which are known by their receivers (i.e. the invitation e-mail has been opened).
	 *
	 * @return the count of invitations which are known by their receivers (i
	 */
	public Long getKnownInvitationsCount()
	{
		return knownInvitationsCount;
	}

	/**
	 * Sets the count of invitations which are known by their receivers (i.e. the invitation e-mail has been opened).</br>
	 * Sets the not known invitation count.
	 *
	 * @param knownInvitationsCount the new count of invitations which are known by their receivers (i
	 */
	public void setKnownInvitationsCount(final Long knownInvitationsCount)
	{
		this.knownInvitationsCount = knownInvitationsCount;
		notKnownInvitationsCount = invitationsCount - knownInvitationsCount;
	}

	/**
	 * Gets the count of invitations which are not known by their receivers (i.e. the invitation e-mail has been opened).
	 *
	 * @return the count of invitations which are not known by their receivers (i
	 */
	public Long getNotKnownInvitationsCount()
	{
		return notKnownInvitationsCount;
	}

	/**
	 * Sets the count of invitations which are not known by their receivers (i.e. the invitation e-mail has been opened).
	 *
	 * @param notKnownInvitationsCount the new count of invitations which are not known by their receivers (i
	 */
	public void setNotKnownInvitationsCount(final Long notKnownInvitationsCount)
	{
		this.notKnownInvitationsCount = notKnownInvitationsCount;
	}

	/**
	 * Gets the count of invitations which are known and accepted by their receivers (i.e. the invitation has been accepted after opening the invitation e-mail).
	 *
	 * @return the count of invitations which are known and accepted by their receivers (i
	 */
	public Long getAcceptedKnownInvitationsCount()
	{
		return acceptedKnownInvitationsCount;
	}

	/**
	 * Sets the count of invitations which are known and accepted by their receivers (i.e. the invitation has been accepted after opening the invitation e-mail).
	 *
	 * @param acceptedKnownInvitationsCount the new count of invitations which are known and accepted by their receivers (i
	 */
	public void setAcceptedKnownInvitationsCount(final Long acceptedKnownInvitationsCount)
	{
		this.acceptedKnownInvitationsCount = acceptedKnownInvitationsCount;
	}

	/**
	 * Gets the count of invitations which are known and declined by their receivers (i.e. the invitation has been declined after opening the invitation e-mail).
	 *
	 * @return the count of invitations which are known and declined by their receivers (i
	 */
	public Long getDeclinedKnownInvitationsCount()
	{
		return declinedKnownInvitationsCount;
	}

	/**
	 * Sets the count of invitations which are known and declined by their receivers (i.e. the invitation has been declined after opening the invitation e-mail).
	 *
	 * @param declinedKnownInvitationsCount the new count of invitations which are known and declined by their receivers (i
	 */
	public void setDeclinedKnownInvitationsCount(final Long declinedKnownInvitationsCount)
	{
		this.declinedKnownInvitationsCount = declinedKnownInvitationsCount;
		pendingAnswerKnownInvitationsCount = knownInvitationsCount - acceptedKnownInvitationsCount - declinedKnownInvitationsCount;
	}

	/**
	 * Gets the count of invitations which are known and has not been replied by their receivers (i.e. the invitation has nor been accepted neither declined after opening the invitation e-mail).
	 *
	 * @return the count of invitations which are known and has not been replied by their receivers (i
	 */
	public Long getPendingAnswerKnownInvitationsCount()
	{
		return pendingAnswerKnownInvitationsCount;
	}

	/**
	 * Sets the count of invitations which are known and has not been replied by their receivers (i.e. the invitation has nor been accepted neither declined after opening the invitation e-mail).
	 *
	 * @param pendingAnswerKnownInvitationsCount the new count of invitations which are known and has not been replied by their receivers (i
	 */
	public void setPendingAnswerKnownInvitationsCount(final Long pendingAnswerKnownInvitationsCount)
	{
		this.pendingAnswerKnownInvitationsCount = pendingAnswerKnownInvitationsCount;
	}
}
