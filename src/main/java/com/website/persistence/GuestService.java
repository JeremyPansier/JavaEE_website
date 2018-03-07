package com.website.persistence;

import static com.website.persistence.EntityAttributes.HASH;
import static com.website.persistence.EntityAttributes.ID;
import static com.website.persistence.EntityAttributes.STATUS;
import static com.website.persistence.EntityAttributes.USERID;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.website.data.GuestStatus;
import com.website.models.beans.Email;
import com.website.models.entities.Event;
import com.website.models.entities.Guest;
import com.website.models.entities.User;
import com.website.tools.security.Hasher;

/**
 * The service managing the guest persistence.</br>
 *
 * @author Jérémy Pansier
 */
@Stateless
public class GuestService {

	/** The entity manager binding the model to the persistence entity. */
	@PersistenceContext(unitName = "website")
	private EntityManager entityManager;

	/** The service managing the email. */
	@Inject
	private javax.enterprise.event.Event<Email> emailEvent;

	/** The service managing the user persistence. */
	@Inject
	private UserService userService;

	/**
	 * Counts the guests with the specified hash.
	 *
	 * @param hash the hash
	 * @return the guests count
	 */
	public Long countGuestsByHash(final String hash) {
		try {
			final String jpql = "SELECT COUNT(guest) FROM Guest guest WHERE guest.hash=:hash";
			final TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
			query.setParameter(HASH.getName(), hash);
			return query.getSingleResult();
		}
		catch (final NoResultException e) {
			return 0L;
		}
	}

	/**
	 * Counts the guests with the specified status for the specified event.
	 *
	 * @param eventId the event id
	 * @param status the guest status (invitation accepted, declined or pending)
	 * @return the guests count
	 */
	public Long countGuestsByEventAndByStatus(final Long eventId, final int status) {
		try {
			final String jpql = "SELECT COUNT(guest) FROM Guest guest WHERE guest.event.id = :id AND guest.status = :status";
			final TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
			query.setParameter(ID.getName(), eventId);
			query.setParameter(STATUS.getName(), status);
			return query.getSingleResult();
		}
		catch (final NoResultException e) {
			return 0L;
		}
	}

	/**
	 * Counts the guests with the specified status after being informed for the specified event id.
	 *
	 * @param eventId the event id
	 * @param status the guest status (invitation accepted, declined or pending)
	 * @return the guests count
	 */
	public Long countGuestsByStatusAndByEventAfterBeingInformed(final Long eventId, final int status) {
		try {
			final String jpql = "SELECT COUNT(guest) FROM Guest guest WHERE guest.event.id=:id AND guest.informed = 1 AND guest.status=:status";
			final TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
			query.setParameter(ID.getName(), eventId);
			query.setParameter(STATUS.getName(), status);
			return query.getSingleResult();
		}
		catch (final NoResultException e) {
			return 0L;
		}
	}

	/**
	 * Counts the informed guests for the specified event id.
	 *
	 * @param eventId the event id
	 * @return the guests count
	 */
	public Long countInformedGuestsByEventId(final Long eventId) {
		try {
			final String jpql = "SELECT COUNT(guest) FROM Guest guest WHERE guest.event.id = :id AND guest.informed = 1";
			final TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
			query.setParameter(ID.getName(), eventId);
			return query.getSingleResult();
		}
		catch (final NoResultException e) {
			return 0L;
		}
	}

	/**
	 * Finds the hash for the specified event and user ids.
	 *
	 * @param eventId the event id
	 * @param userId the user id
	 * @return the found hash or null if the user is not a guest
	 */
	private String findHashByEventIdAndUserId(final Long eventId, final Long userId) {
		try {
			final String jpql = "SELECT guest.hash FROM Guest guest WHERE guest.event.id = :id AND guest.user.id = :userId";
			final TypedQuery<String> query = entityManager.createQuery(jpql, String.class);
			query.setParameter(ID.getName(), eventId);
			query.setParameter(USERID.getName(), userId);
			return query.getSingleResult();
		}
		catch (final NoResultException e) {
			return null;
		}
	}

	/**
	 * Finds the guest for the specified hash.
	 *
	 * @param hash the hash
	 * @return the found guest or null if there is no guest for the specified hash
	 */
	public Guest findGuestByHash(final String hash) {
		try {
			final String jpql = "SELECT guest FROM Guest guest WHERE guest.hash = :hash";
			final TypedQuery<Guest> query = entityManager.createQuery(jpql, Guest.class);
			query.setParameter(HASH.getName(), hash);
			return query.getSingleResult();
		}
		catch (final NoResultException e) {
			return null;
		}
	}

	/**
	 * Finds the guests for the specified event id.
	 *
	 * @param eventId the event id
	 * @return the found guests or an empty list if there is no guest for the specified event id
	 */
	public List<Guest> findGuestsByEventId(final Long eventId) {
		final String jpql = "SELECT guest FROM Guest guest WHERE guest.event.id = :id";
		final TypedQuery<Guest> query = entityManager.createQuery(jpql, Guest.class);
		query.setParameter(ID.getName(), eventId);
		return query.getResultList();
	}

	/**
	 * Persists the specified guest.
	 *
	 * @param guest the guest to persist
	 */
	public void persistGuest(final Guest guest) {
		entityManager.persist(guest);
	}

	/**
	 * Persists a new user with the specified email and a new guest for the specified event.
	 *
	 * @param event the event
	 * @param email the guest email
	 */
	public void persistGuest(final Event event, final String email) {
		/* Create and persist a new user if doesn't exist. */
		User user;
		if (userService.findUserByEmail(email) == null) {
			user = new User();
			user.setEmail(email);
			userService.persistUser(user);
		} else {
			user = userService.findUserByEmail(email);
		}
		/* Create and persist a new guest if doesn't exist. */
		String hash = findHashByEventIdAndUserId(event.getId(), user.getId());
		if (hash == null) {
			final Guest guest = new Guest(user, event);
			guest.setHash(Hasher.sha1ToHex(email + "#" + event.getId()));
			persistGuest(guest);
			hash = guest.getHash();
		}
		emailEvent.fire(new Email(email, event.getTitle(), hash));
	}

	/**
	 * Removes the specified guest.
	 * </p>
	 * <b>Note:</b> </br>
	 * This method uses the {@link EntityManager}.</br>
	 * But here's the thing: {@link EntityManager#remove} works only on entities which are managed in the current transaction/context. To remove entities which are not managed, the only way is to make
	 * it managed by merging the specified entity with the managed entity that have the same id.</br>
	 * For this purpose, this method first checks if the entity is managed (with {@link EntityManager#contains}) and if not, then make it managed (with {@link EntityManager#merge}).</br>
	 * Only then will the entity be removed (with {@link EntityManager#remove}).
	 *
	 * @param guest the guest to remove
	 */
	public void removeGuest(final Guest guest) {
		Guest managedGuest = guest;
		/* Check if the guest is managed */
		if (!entityManager.contains(guest)) {
			/* If not, then make it managed */
			managedGuest = entityManager.merge(guest);
		}
		/* Remove the guest */
		entityManager.remove(managedGuest);
	}

	/**
	 * Updates the status of the guest with the specified hash.
	 *
	 * @param hash the hash
	 * @param status the new status
	 */
	public void updateGuestStatusByHash(final String hash, final GuestStatus status) {
		final Guest guest = findGuestByHash(hash);
		if (guest != null) {
			guest.setStatus(status);
		}
	}

	/**
	 * Updates the state informed of the guest with the specified hash.
	 *
	 * @param hash the hash
	 * @param informed whether the guest is informed
	 */
	public void updateGuestInformedByHash(final String hash, final int informed) {
		final Guest guest = findGuestByHash(hash);
		if (guest != null) {
			guest.setInformed(informed);
		}
	}
}
