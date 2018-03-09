package com.website.persistence;

import static com.website.persistence.EntityAttributes.ID;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.website.models.entities.Author;
import com.website.models.entities.Event;
import com.website.models.entities.Guest;
import com.website.tools.error.HttpErrorHandler;

/**
 * The service managing the event persistence.</br>
 *
 * @author Jérémy Pansier
 */
@Stateless
public class EventService {

	/** The entity manager binding the model to the persistence entity. */
	@PersistenceContext(unitName = "website")
	private EntityManager entityManager;

	/** The service managing the author persistence. */
	@Inject
	private AuthorService authorService;

	/** The service managing the guest persistence. */
	@Inject
	private GuestService guestService;

	/**
	 * Counts the events with the specified id.
	 *
	 * @param eventId the event id
	 * @return the events count
	 */
	public Long countEventsById(final Long eventId) {
		try {
			final String jpql = "SELECT COUNT(e) FROM Event e WHERE e.id = :id";
			final TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
			query.setParameter(ID.getName(), eventId);
			return query.getSingleResult();
		}
		catch (final NoResultException noSuchResultException) {
			return 0L;
		}
	}

	/**
	 * Finds the event with the specified event id.
	 *
	 * @param id the id
	 * @return the found event
	 */
	public Event findEventByEventId(final Long id) {
		return entityManager.find(Event.class, id);
	}

	/**
	 * Finds the events with the specified author id.
	 *
	 * @param authorId the author id
	 * @return the found events or an empty list if there is no event corresponding to specified id
	 */
	public List<Event> findEventsByAuthorId(final Long authorId) {
		final String jpql = "SELECT event FROM Event event JOIN event.author author WHERE author.id =:id";
		final TypedQuery<Event> query = entityManager.createQuery(jpql, Event.class);
		query.setParameter(ID.getName(), authorId);
		return query.getResultList();
	}

	/**
	 * Checks if the specified name is the name of the specified id event author.
	 *
	 * @param eventId the event id
	 * @param authorName the author name
	 * @return true, if the specified name is the name of the specified id event author
	 */
	public boolean isEventsAuthor(final Long eventId, final String authorName) {
		if (0 == countEventsById(eventId)) {
			HttpErrorHandler.print404("There is no event for this id in the database");
			return false;
		}
		final Author author = authorService.findAuthorByAuthorName(authorName);
		if (null != author.getId() && author.getId() != authorService.findAuthorIdByEventId(eventId)) {
			HttpErrorHandler.print401("The author cannot access to this event");
			return false;
		}
		return true;
	}

	/**
	 * Persists the specified event.
	 * 
	 * @param event the event to persist
	 */
	public void persistEvent(final Event event) {
		entityManager.persist(event);
	}

	/**
	 * Removes the specified event and all its guests.
	 * </p>
	 * <b>Note:</b> </br>
	 * This method uses the {@link EntityManager}.</br>
	 * But here's the thing: {@link EntityManager#remove} works only on entities which are managed in the current transaction/context. To remove entities which are not managed, the only way is to make
	 * it managed by merging the specified entity with the managed entity that have the same id.</br>
	 * For this purpose, this method first checks if the entity is managed (with {@link EntityManager#contains}) and if not, then make it managed (with {@link EntityManager#merge}).</br>
	 * Only then will the entity be removed (with {@link EntityManager#remove}).
	 * 
	 * @param event the event to remove
	 */
	public void removeEvent(final Event event) {
		/* Remove all the event guests */
		final List<Guest> guests = guestService.findGuestsByEventId(event.getId());
		for (final Guest guest : guests) {
			guestService.removeGuest(guest);
		}

		Event managedEvent = event;
		/* Check if the event is managed */
		if (!entityManager.contains(event)) {
			/* If not, then make it managed */
			managedEvent = entityManager.merge(event);
		}
		/* Remove the event */
		entityManager.remove(managedEvent);
	}

	/**
	 * Updates the specified event.
	 * 
	 * @param event the event to update
	 */
	public void updateEvent(final Event event) {
		entityManager.merge(event);
	}

}
