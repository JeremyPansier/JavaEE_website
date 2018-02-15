package com.website.persistence;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.website.models.beans.Email;
import com.website.models.entities.Event;
import com.website.models.entities.Guest;
import com.website.models.entities.User;
import com.website.tools.EventServiceException;
import com.website.tools.GuestStatus;
import com.website.tools.Hasher;

/**
 * The service managing the guest persistence.</br>
 *
 * @author Jérémy Pansier
 */
@Stateless
public class GuestService
{
	/** The entity manager binding the model to the persistence entity. */
	@PersistenceContext(unitName = "website")
	private EntityManager entityManager;

	/** The service managing the email. */
	@Inject
	javax.enterprise.event.Event<Email> myEventMail;

	/** The service managing the event persistence. */
	@Inject
	EventService eventService;

	/** The service managing the user persistence. */
	@Inject
	UserService userService;

	/** The event id persistence data category name. */
	private static final String EVENTID = PersistenceDataCategories.EVENTID.getName();

	/** The user id persistence data category name. */
	private static final String USERID = PersistenceDataCategories.USERID.getName();

	/** The status persistence data category name. */
	private static final String STATUS = PersistenceDataCategories.STATUS.getName();

	/** The hash persistence data category name. */
	private static final String HASH = PersistenceDataCategories.HASH.getName();

	public void insertGuests(final Event event, final String emailList)
	{
		for (final String email : emailList.split(";"))
		{
			User user;
			if (userService.selectUserByEmail(email) == null)
			{
				user = userService.insertUser(email);
			}
			else
			{
				user = userService.selectUserByEmail(email);
			}
			String hash = selectHashByEventIdAndUserId(event.getId(), user.getId());
			if (hash == null)
			{
				final Guest guest = insertGuest(event, user);
				hash = guest.getHash();
			}
			myEventMail.fire(new Email(email, event.getTitle(), hash));
		}
	}

	public Guest insertGuest(final Event event, final User user)
	{
		final Guest guest = new Guest(user, event);
		guest.setHash(Hasher.sha1ToHex(user.getEmail() + "#" + event.getId()));
		entityManager.persist(guest);
		return guest;
	}

	public String selectHashByEventIdAndUserId(final Long eventId, final Long userId)
	{
		try
		{
			final String jpql = "SELECT guest.hash FROM Guest guest WHERE guest.event.id = :eventId AND guest.user.id = :userId";
			final TypedQuery<String> query = entityManager.createQuery(jpql, String.class);
			query.setParameter(EVENTID, eventId);
			query.setParameter(USERID, userId);
			return query.getSingleResult();
		}
		catch (final NoResultException e)
		{
			return null;
		}
	}

	public Guest selectGuestByHash(final String hash)
	{
		try
		{
			final String jpql = "SELECT guest FROM Guest guest WHERE guest.hash = :hash";
			final TypedQuery<Guest> query = entityManager.createQuery(jpql, Guest.class);
			query.setParameter(HASH, hash);
			return query.getSingleResult();
		}
		catch (final NoResultException e)
		{
			return null;
		}
	}

	public List<Guest> selectGuestsByEventId(final Long eventId)
	{
		final String jpql = "SELECT guest FROM Guest guest WHERE guest.event.id = :eventId";
		final TypedQuery<Guest> query = entityManager.createQuery(jpql, Guest.class);
		query.setParameter(EVENTID, eventId);
		return query.getResultList();
	}

	public Long countGuestsByEventAndByStatus(final Long eventId, final int accept)
	{
		try
		{
			final String jpql = "SELECT COUNT(guest) FROM Guest guest WHERE guest.event.id = :eventId AND guest.status = :status";
			final TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
			query.setParameter(EVENTID, eventId);
			query.setParameter(STATUS, accept);
			return query.getSingleResult();
		}
		catch (final NoResultException e)
		{
			return 0L;
		}
	}

	public Long countGuestsBySatusAndByEventAfterEmailReading(final Long eventId, final int accept)
	{
		try
		{
			final String jpql = "SELECT COUNT(guest) FROM Guest guest WHERE guest.event.id=:eventId AND guest.emailstatus = 1 AND guest.status=:status";
			final TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
			query.setParameter(EVENTID, eventId);
			query.setParameter(STATUS, accept);
			return query.getSingleResult();
		}
		catch (final NoResultException e)
		{
			return 0L;
		}
	}

	public boolean countGuestsByHash(final String hash)
	{
		try
		{
			final String jpql = "SELECT COUNT(guest) FROM Guest guest WHERE guest.hash=:hash";
			final TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
			query.setParameter(HASH, hash);
			return query.getSingleResult() == 1L;
		}
		catch (final NoResultException e)
		{
			return false;
		}
	}

	public Long countReadEmailsByEvent(final Long eventId)
	{
		try
		{
			final String jpql = "SELECT COUNT(guest) FROM Guest guest WHERE guest.event.id = :eventId AND guest.emailstatus = 1";
			final TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
			query.setParameter(EVENTID, eventId);
			return query.getSingleResult();
		}
		catch (final NoResultException e)
		{
			return 0L;
		}
	}

	public void updateGuestStatusByHash(final String hash, final GuestStatus status)
	{
		try
		{
			final Guest guest = selectGuestByHash(hash);
			if (guest != null)
			{
				guest.setStatus(status);
			}
		}
		catch (final Exception e)
		{
			throw new EventServiceException("Error with update guest status for hash = " + hash, e);
		}
	}

	public void updateGuestsEmailstatusByHash(final String hash, final int emailstatus)
	{
		try
		{
			final Guest guest = selectGuestByHash(hash);
			if (guest != null)
			{
				guest.setEmailstatus(emailstatus);
			}
		}
		catch (final Exception e)
		{
			throw new EventServiceException("Error with update guest's emailstatus for hash" + hash, e);
		}
	}

	public void deleteGuest(final Long id)
	{
		final Guest guest = entityManager.find(Guest.class, id);
		entityManager.remove(guest);
	}

	public void deleteGuestsByEventId(final Long idEvent)
	{
		final List<Guest> guests = selectGuestsByEventId(idEvent);
		for (final Guest guest : guests)
		{
			entityManager.remove(guest);
		}
	}

}
