package com.website.persistence;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.website.models.entities.Visit;

/**
 * The service managing the visit persistence.</br>
 *
 * @author Jérémy Pansier
 */
@Stateless
public class VisitService {

	/** The entity manager binding the model to the persistence entity. */
	@PersistenceContext(unitName = "website")
	private EntityManager entityManager;

	public void insertVisit(final String url, final String ip) {
		final Visit s = new Visit();
		s.setUrl(url);
		s.setIp(ip);
		entityManager.persist(s);
	}

	public List<Visit> selectVisits() {
		final String jpql = "SELECT v FROM Visit v";
		final TypedQuery<Visit> query = entityManager.createQuery(jpql, Visit.class);
		return query.getResultList();
	}

	public Long countVisitsByUrlGroupByUrl(final String url) {
		try {
			final String jpql = "SELECT COUNT(v) FROM Visit v WHERE v.url = :url GROUP BY v.url";
			final TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
			query.setParameter("url", url);
			return query.getSingleResult();
		}
		catch (final NoResultException e) {
			return 0L;
		}
	}

	public void deleteVisits() {
		final List<Visit> visits = selectVisits();
		for (final Visit visit : visits) {
			entityManager.remove(visit);
		}
	}
}
