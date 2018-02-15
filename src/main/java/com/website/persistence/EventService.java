package com.website.persistence;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.website.models.entities.Event;
import com.website.models.entities.Visit;

/**
 * The service managing the event persistence.</br>
 *
 * @author Jérémy Pansier
 */
@Stateless
public class EventService
{
	/** The entity manager binding the model to the persistence entity. */
	@PersistenceContext(unitName = "website")
	private EntityManager entityManager;

	/** The service managing the guest persistence. */
	@Inject
	GuestService guestService;

	/** The event id persistence data category name. */
	private static final String EVENTID = PersistenceDataCategories.EVENTID.getName();

	/** The user name persistence data category name. */
	private static final String NAME = PersistenceDataCategories.NAME.getName();

	public Long insertEvent(final Long id, final String title, final String description, final String filename)
	{
		final Event e = new Event();
		e.setTitle(title);
		e.setDescription(description);
		if (filename != null)
		{
			e.setFilename(filename);
		}
		e.getAuthor().setId(id);
		entityManager.persist(e);
		return e.getId();
	}

	public void insertVisit(final String url, final String ip)
	{
		final Visit s = new Visit();
		s.setUrl(url);
		s.setIp(ip);
		entityManager.persist(s);
	}

	public Event selectEventByEventId(final Long id)
	{
		return entityManager.find(Event.class, id);
	}

	public List<Event> selectEventsByAuthorName(final String authorName)
	{
		final String jpql = "SELECT e FROM Event e JOIN e.author a WHERE a.name =:name";
		final TypedQuery<Event> query = entityManager.createQuery(jpql, Event.class);
		query.setParameter(NAME, authorName);
		return query.getResultList();
	}

	public List<Visit> selectVisits()
	{
		final String jpql = "SELECT v FROM Visit v";
		final TypedQuery<Visit> query = entityManager.createQuery(jpql, Visit.class);
		return query.getResultList();
	}

	public Long countVisitsByUrlGroupByUrl(final String url)
	{
		try
		{
			final String jpql = "SELECT COUNT(v) FROM Visit v WHERE v.url = :url GROUP BY v.url";
			final TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
			query.setParameter("url", url);
			return query.getSingleResult();
		}
		catch (final NoResultException e)
		{
			return 0L;
		}
	}

	public boolean countEventsById(final Long eventId)
	{
		try
		{
			final String jpql = "SELECT COUNT(e) FROM Event e WHERE e.id = :eventId";
			final TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
			query.setParameter(EVENTID, eventId);
			return query.getSingleResult() == 1L;
		}
		catch (final NoResultException e)
		{
			return false;
		}
	}

	public void updateEvent(final Long id, final String title, final String description, final String filename)
	{
		final Event e = entityManager.find(Event.class, id);
		if (e != null)
		{
			e.setTitle(title);
			e.setDescription(description);
			if (filename != null)
				e.setFilename(filename);
		}
	}

	public void deleteEvent(final Long id)
	{
		guestService.deleteGuestsByEventId(id);
		final Event event = entityManager.find(Event.class, id);
		entityManager.remove(event);
	}

	public void deleteVisits()
	{
		final List<Visit> visits = selectVisits();
		for (final Visit visit : visits)
		{
			entityManager.remove(visit);
		}
	}

}
