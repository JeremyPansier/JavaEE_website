package com.website.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.faces.context.ExternalContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.website.beans.Email;
import com.website.entities.Author;
import com.website.entities.Event;
import com.website.entities.Subscriber;
import com.website.entities.User;
import com.website.entities.Visit;
import com.website.enumeration.Constant;
import com.website.tools.EventServiceException;
import com.website.tools.Hasher;
import com.website.tools.HttpErrorHandler;

@Stateless
public class EventService
{
	@PersistenceContext(unitName = "event")
	private EntityManager em;
	@Inject
	javax.enterprise.event.Event<Email> myEventMail;
	private static final String EVENTID = Constant.eventId.toString();
	private static final String USERID = Constant.userId.toString();
	private static final String STATUS = Constant.status.toString();
	private static final String USERNAME = Constant.username.toString();
	private static final String HASH = Constant.hash.toString();
	private static final String EMAIL = Constant.email.toString();

	public Long insertEvent(Long id, String title, String description, String filename)
	{
		Event e = new Event();
		e.setTitle(title);
		e.setDescription(description);
		if (filename != null)
		{
			e.setFilename(filename);
		}
		e.setAuthorId(id);
		em.persist(e);
		return e.getId();
	}

	public User insertUser(String email)
	{
		User u = new User();
		u.setEmail(email);
		em.persist(u);
		return u;
	}

	public void insertVisit(String url, String ip)
	{
		Visit s = new Visit();
		s.setUrl(url);
		s.setIp(ip);
		em.persist(s);
	}

	public void insertAuthor(String username, String password)
	{
		Author a = new Author();
		a.setUsername(username);
		a.setPassword(password);
		em.persist(a);
	}

	public Subscriber insertSubscriber(Long eventId, User user)
	{
		Subscriber s = new Subscriber();
		s.setEventId(eventId);
		s.setUserId(user.getId());
		s.setHash(Hasher.sha1ToHex(user.getEmail() + "#" + eventId));
		em.persist(s);
		return s;
	}

	// TODO methode a extraire du service
	public void insertSubscribers(Event event, String emailList)
	{
		for (String email : emailList.split(";"))
		{
			User user;
			if (selectUserByEmail(email) == null)
			{
				user = insertUser(email);
			} else
			{
				user = selectUserByEmail(email);
			}
			String hash = selectHashByEventIdAndUserId(event.getId(), user.getId());
			if (hash == null)
			{
				Subscriber subscriber = insertSubscriber(event.getId(), user);
				hash = subscriber.getHash();
			}
			myEventMail.fire(new Email(email, event.getTitle(), hash));
		}
	}

	public Event selectEventByEventId(Long id)
	{
		return em.find(Event.class, id);
	}

	public List<Event> selectEventsByAuthorUsername(String username)
	{
		String jpql = "SELECT e FROM Event e JOIN e.author a WHERE a.username =:username";
		TypedQuery<Event> query = em.createQuery(jpql, Event.class);
		query.setParameter(USERNAME, username);
		return query.getResultList();
	}

	public Subscriber selectSubscriberByHash(String hash)
	{
		try
		{
			String jpql = "SELECT s FROM Subscriber s WHERE s.hash = :hash";
			TypedQuery<Subscriber> query = em.createQuery(jpql, Subscriber.class);
			query.setParameter(HASH, hash);
			return query.getSingleResult();
		} catch (NoResultException e)
		{
			return null;
		}
	}

	public User selectUserById(Long id)
	{
		return em.find(User.class, id);
	}

	public List<Subscriber> selectSubscribersByEventId(Long eventId)
	{
		String jpql = "SELECT s FROM Subscriber s WHERE s.event.id = :eventId";
		TypedQuery<Subscriber> query = em.createQuery(jpql, Subscriber.class);
		query.setParameter(EVENTID, eventId);
		return query.getResultList();
	}

	public User selectUserByEmail(String email)
	{
		try
		{
			String jpql = "SELECT u FROM User u WHERE u.email = :email";
			TypedQuery<User> query = em.createQuery(jpql, User.class);
			query.setParameter(EMAIL, email);
			return query.getSingleResult();
		} catch (NoResultException e)
		{
			return null;
		}
	}

	public String selectHashByEventIdAndUserId(Long eventId, Long userId)
	{
		try
		{
			String jpql = "SELECT s.hash FROM Subscriber s WHERE s.event.id = :eventId AND s.user.id = :userId";
			TypedQuery<String> query = em.createQuery(jpql, String.class);
			query.setParameter(EVENTID, eventId);
			query.setParameter(USERID, userId);
			return query.getSingleResult();
		} catch (NoResultException e)
		{
			return null;
		}
	}

	public Long selectAuthorIdByEventId(Long eventId)
	{
		try
		{
			String jpql = "SELECT e.author.id FROM Event e WHERE e.id = :eventId";
			TypedQuery<Long> query = em.createQuery(jpql, Long.class);
			query.setParameter(EVENTID, eventId);
			return query.getSingleResult();
		} catch (NoResultException e)
		{
			return 0L;
		}
	}

	public Author selectAuthorByUsername(String username)
	{
		try
		{
			String jpql = "SELECT a FROM Author a WHERE a.username = :username";
			TypedQuery<Author> query = em.createQuery(jpql, Author.class);
			query.setParameter(USERNAME, username);
			return query.getSingleResult();
		} catch (NoResultException e)
		{
			return null;
		}
	}

	public String selectPasswordByUserName(String username)
	{
		try
		{
			String jpql = "SELECT a.password FROM Author a WHERE a.username = :username";
			TypedQuery<String> query = em.createQuery(jpql, String.class);
			query.setParameter(USERNAME, username);
			return query.getSingleResult();
		} catch (NoResultException e)
		{
			return null;
		}
	}

	public List<Visit> selectVisits()
	{
		String jpql = "SELECT v FROM Visit v";
		TypedQuery<Visit> query = em.createQuery(jpql, Visit.class);
		return query.getResultList();
	}

	public Long countVisitsByUrlGroupByUrl(String url)
	{
		try
		{
			String jpql = "SELECT COUNT(v) FROM Visit v WHERE v.url = :url GROUP BY v.url";
			TypedQuery<Long> query = em.createQuery(jpql, Long.class);
			query.setParameter("url", url);
			return query.getSingleResult();
		} catch (NoResultException e)
		{
			return 0L;
		}
	}

	public boolean countEventsById(Long eventId)
	{
		try
		{
			String jpql = "SELECT COUNT(e) FROM Event e WHERE e.id = :eventId";
			TypedQuery<Long> query = em.createQuery(jpql, Long.class);
			query.setParameter(EVENTID, eventId);
			return query.getSingleResult() == 1L;
		} catch (NoResultException e)
		{
			return false;
		}
	}

	public Long countSubscribersByEventAndByStatus(Long eventId, int accept)
	{
		try
		{
			String jpql = "SELECT COUNT(s) FROM Subscriber s WHERE s.event.id = :eventId AND s.status = :status";
			TypedQuery<Long> query = em.createQuery(jpql, Long.class);
			query.setParameter(EVENTID, eventId);
			query.setParameter(STATUS, accept);
			return query.getSingleResult();
		} catch (NoResultException e)
		{
			return 0L;
		}
	}

	public Long countReadEmailsByEvent(Long eventId)
	{
		try
		{
			String jpql = "SELECT COUNT(s) FROM Subscriber s WHERE s.event.id = :eventId AND s.emailstatus = 1";
			TypedQuery<Long> query = em.createQuery(jpql, Long.class);
			query.setParameter(EVENTID, eventId);
			return query.getSingleResult();
		} catch (NoResultException e)
		{
			return 0L;
		}
	}

	public Long countSubscribersBySatusAndByEventAfterEmailReading(Long eventId, int accept)
	{
		try
		{
			String jpql = "SELECT COUNT(s) FROM Subscriber s WHERE s.event.id=:eventId AND s.emailstatus = 1 AND s.status=:status";
			TypedQuery<Long> query = em.createQuery(jpql, Long.class);
			query.setParameter(EVENTID, eventId);
			query.setParameter(STATUS, accept);
			return query.getSingleResult();
		} catch (NoResultException e)
		{
			return 0L;
		}
	}

	public boolean countSubscribersByHash(String hash)
	{
		try
		{
			String jpql = "SELECT COUNT(s) FROM Subscriber s WHERE s.hash=:hash";
			TypedQuery<Long> query = em.createQuery(jpql, Long.class);
			query.setParameter(HASH, hash);
			return query.getSingleResult() == 1L;
		} catch (NoResultException e)
		{
			return false;
		}
	}

	public void updateEvent(Long id, String title, String description, String filename)
	{
		Event e = em.find(Event.class, id);
		if (e != null)
		{
			e.setTitle(title);
			e.setDescription(description);
			if (filename != null)
				e.setFilename(filename);
		}
	}

	public void updateSubscriberStatusByHash(String hash, int status)
	{
		try
		{
			Subscriber s = selectSubscriberByHash(hash);
			if (s != null)
			{
				s.setStatus(status);
			}
		} catch (Exception e)
		{
			throw new EventServiceException("Error with update subscriber status for hash = " + hash, e);
		}
	}

	public void updateSubscribersEmailstatusByHash(String hash, int emailstatus)
	{
		try
		{
			Subscriber s = selectSubscriberByHash(hash);
			if (s != null)
			{
				s.setEmailstatus(emailstatus);
			}
		} catch (Exception e)
		{
			throw new EventServiceException("Error with update subscriber's emailstatus for hash" + hash, e);
		}
	}

	public boolean isAuthor(String username)
	{
		try
		{
			String jpql = "SELECT COUNT(a.username) FROM Author a WHERE a.username=:username";
			TypedQuery<Long> query = em.createQuery(jpql, Long.class);
			query.setParameter(USERNAME, username);
			return query.getSingleResult() == 1L;
		} catch (NoResultException e)
		{
			return false;
		}
	}

	public boolean isEventsAuthor(Long eventId, String username, ExternalContext ec)
	{
		if (!countEventsById(eventId))
		{
			HttpErrorHandler.print404(ec, "There is no event for this id in the database");
			return false;
		}
		Author author = selectAuthorByUsername(username);
		if (author.getId() != selectAuthorIdByEventId(eventId))
		{
			HttpErrorHandler.print401(ec, "The author cannot access to this event");
			return false;
		}
		return true;
	}

	public void deleteSubscriber(Long id)
	{
		Subscriber subscriber = em.find(Subscriber.class, id);
		em.remove(subscriber);
	}

	public void deleteSubscribersByEventId(Long idEvent)
	{
		List<Subscriber> subscribers = selectSubscribersByEventId(idEvent);
		for (Subscriber subscriber : subscribers)
		{
			em.remove(subscriber);
		}
	}

	public void deleteEvent(Long id)
	{
		deleteSubscribersByEventId(id);
		Event event = em.find(Event.class, id);
		em.remove(event);
	}

	public void deleteVisits()
	{
		List<Visit> visits = selectVisits();
		for (Visit visit : visits)
		{
			em.remove(visit);
		}
	}

}
