package com.aiconoa.trainings.services;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.context.ExternalContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletResponse;

import com.aiconoa.trainings.cdi.MyEventMail;
import com.aiconoa.trainings.entity.Author;
import com.aiconoa.trainings.entity.Event;
import com.aiconoa.trainings.entity.Stats;
import com.aiconoa.trainings.entity.Subscriber;
import com.aiconoa.trainings.entity.User;

@Stateless
public class EventService {
    @PersistenceContext(unitName = "event")
    private EntityManager em;
    @Inject
    private LoginService loginService;
    private static final String IDEVENT = "idEvent";
    @Inject
    javax.enterprise.event.Event<MyEventMail> myEventMail;

    public Event selectEventByIdEvent(Integer id) {
        return em.find(Event.class, id);
    }

    public List<Event> listSelectEventColumns(String username) {
        String jpql = "SELECT e FROM Event e JOIN e.author a WHERE a.username =:username";
        TypedQuery<Event> query = em.createQuery(jpql, Event.class);
        query.setParameter("username", username);
        return query.getResultList();
    }

    public Subscriber selectSubscriberByHashcode(String hashcodeString) {
        try {
            String jpql = "SELECT s FROM Subscriber s WHERE s.hashCode = :hashcode";
            TypedQuery<Subscriber> query = em.createQuery(jpql, Subscriber.class);
            query.setParameter("hashcode", hashcodeString);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public User selectUserByIdUser(int idUser) {
        return em.find(User.class, idUser);
    }

    public List<Subscriber> listSelectSubscribersByIdEvent(int idEvent) {
        String jpql = "SELECT s FROM Subscriber s JOIN s.user u WHERE s.event.id =:idEvent";
        TypedQuery<Subscriber> query = em.createQuery(jpql, Subscriber.class);
        query.setParameter(IDEVENT, idEvent);
        return query.getResultList();
    }

    public User findUserByMail(String email) {
        try {
            String jpql = "SELECT u FROM User u WHERE u.email = :email";
            TypedQuery<User> query = em.createQuery(jpql, User.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public boolean checkIfEventIsUniqueForOneId(int id) {
        String jpql = "SELECT COUNT(e) FROM Event e WHERE e.id=:id";
        TypedQuery<Long> query = em.createQuery(jpql, Long.class);
        query.setParameter("id", id);
        return query.getSingleResult() == 1L;
    }

    public int countNumberPresentOrNot(int idEvent, int status) {
        String jpql = "SELECT COUNT(s) FROM Subscriber s WHERE s.event.id=:idEvent AND s.isPresent = :status";
        TypedQuery<Long> query = em.createQuery(jpql, Long.class);
        query.setParameter(IDEVENT, idEvent);
        query.setParameter("status", status);
        return (int) (long) query.getSingleResult();
    }

    public int countNumberMailRead(int idEvent) {
        String jpql = "SELECT COUNT(s) FROM Subscriber s WHERE s.event.id=:idEvent AND s.mailStatus = 1";
        TypedQuery<Long> query = em.createQuery(jpql, Long.class);
        query.setParameter(IDEVENT, idEvent);
        return (int) (long) query.getSingleResult();
    }

    public int countNbIsPresentOrNotAfterReading(int idEvent, int isPresent) {
        String jpql = "SELECT COUNT(s) FROM Subscriber s WHERE s.event.id=:idEvent AND s.mailStatus = 1 AND s.isPresent=:isPresent";

        TypedQuery<Long> query = em.createQuery(jpql, Long.class);
        query.setParameter(IDEVENT, idEvent);
        query.setParameter("isPresent", isPresent);
        return (int) (long) query.getSingleResult();
    }

    public boolean isSubscribersDuoIdEventIdUserIsUnique(int idEvent, int idUser) {
        String jpql = "SELECT COUNT(s) FROM Subscriber s WHERE s.event.id=:idEvent AND s.user.id=:idUser";
        TypedQuery<Long> query = em.createQuery(jpql, Long.class);
        query.setParameter(IDEVENT, idEvent);
        query.setParameter("idUser", idUser);
        return query.getSingleResult() == 1L;
    }

    public String findHashcodeByIdEventIdUser(Event event, User user) {
        try {
            String jpql = "SELECT s.hashCode FROM Subscriber s WHERE s.event.id = :idEvent AND s.user.id = :idUser";
            TypedQuery<String> query = em.createQuery(jpql, String.class);
            query.setParameter(IDEVENT, event.getIdEvent());
            query.setParameter("idUser", user.getIdUser());
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void updateEvent(int idEvent, String title, String description, String fileName) {
        Event e = em.find(Event.class, idEvent);
        if (e != null) {
            e.setTitle(title);
            e.setDescription(description);
            if (fileName != null)
                e.setFileName(fileName);
        }
    }

    public int insertEvent(String title, String description, String fileName, int idAuthor) {
        Event e = new Event();
        e.setTitle(title);
        e.setDescription(description);
        if (fileName != null) {
            e.setFileName(fileName);
        }
        e.setIdAuthor(idAuthor);
        em.persist(e);
        return e.getIdEvent();
    }

    public void updateSubscribersIsPresentWhereHashCode(String hashcodeString, int responseUser) {

        try {
            Subscriber s = selectSubscriberByHashcode(hashcodeString);
            if (s != null) {
                s.setIsPresent(responseUser);
            }
        } catch (Exception e) {
            throw new EventServiceException("Error with Update subscribers presence for hashCode" + hashcodeString, e);
        }
    }

    public User insertUser(String email) {
        User u = new User();
        u.setEmail(email);
        em.persist(u);
        return u;
    }

    public void updateSubscribersMailStatusWhereHashcode(int mailread, String hashcodeString) {

        try {
            Subscriber s = selectSubscriberByHashcode(hashcodeString);
            if (s != null) {
                s.setMailStatus(mailread);
            }
        } catch (Exception e) {
            throw new EventServiceException("Error with Update subscribers presence for hashCode" + hashcodeString, e);
        }
    }

    // verif

    public Subscriber insertSubscriber(Event event, User user) {
        Subscriber s = new Subscriber();
        s.setIdEvent(event.getIdEvent());
        s.setIdUser(user.getIdUser());
        s.setHashCode(Hasher.sha1ToHex(user.getEmail() + "#" + event.getIdEvent()));
        em.persist(s);
        return s;
    }

    public void insertVisit(String url, String ipAddress) {
        Stats s = new Stats();
        s.setUrl(url);
        s.setIpAddress(ipAddress);
        em.persist(s);
    }

    public List<Stats> nbrVisitByURL() {
        String jpql = "SELECT s, COUNT(s) AS nb FROM Stats s GROUP BY s.url ORDER BY COUNT(s) DESC";
        Query query = em.createQuery(jpql);
        List<Object[]> results = query.getResultList();
        List<Stats> statsList = new ArrayList<>();
        for (Object[] result : results) {
            Stats s = (Stats) result[0];
            int count = ((Number) result[1]).intValue();
            s.setCount(count);
            statsList.add(s);
        }

        return statsList;
    }

    public int selectIdAuthorByIdEvent(int idEvent) {
        String jpql = "SELECT e.author.id FROM Event e WHERE e.id = :id";
        TypedQuery<Integer> query = em.createQuery(jpql, Integer.class);
        query.setParameter("id", idEvent);
        return query.getSingleResult();
    }

    public boolean checkIfEventCorrespondToAuthor(int idEvent, String sessionUsername, HttpServletResponse response) {
        if (!checkIfEventIsUniqueForOneId(idEvent)) {
            HttpErrorHandler.print404(response, "Le paramètre idEvent entré n'existe pas dans la bdd");
            return false;
        }
        Author author = loginService.selectIdAuthorByUsername(sessionUsername);
        if (author.getIdAuthor() != selectIdAuthorByIdEvent(idEvent)) {
            HttpErrorHandler.print401(response, "L'autheur n'a pas accès a cet evenement");
            return false;
        }
        return true;
    }

    public void addSubscribersToEvent(Event event, String emailList) {
        for (String email : emailList.split(";")) {
            User user;
            if (findUserByMail(email) == null) {
                user = insertUser(email);
            } else {
                user = findUserByMail(email);
            }
            String hash = findHashcodeByIdEventIdUser(event, user);
            if (hash == null) {
                Subscriber subscriber = insertSubscriber(event, user);
                hash = subscriber.getHashCode();
            }
            myEventMail.fire(new MyEventMail(email, event.getTitle(), hash));
        }
    }

    public boolean checkIfEventCorrespondToAuthor(int idEvent, String username, ExternalContext ec) {
        if (!checkIfEventIsUniqueForOneId(idEvent)) {
            HttpErrorHandler.print404(ec, "Le paramètre idEvent entré n'existe pas dans la bdd");
            return false;
        }
        Author author = loginService.selectIdAuthorByUsername(username);
        if (author.getIdAuthor() != selectIdAuthorByIdEvent(idEvent)) {
            HttpErrorHandler.print401(ec, "L'autheur n'a pas accès a cet evenement");
            return false;
        }
        return true;
    }

    public void deleteSubscriber(int id) {
        Subscriber subscriber = em.find(Subscriber.class, id);
        em.remove(subscriber);
    }
    
    public List<Subscriber> findAllSubscribersByIdEvent(int idEvent) {
        String jpql = "SELECT s FROM Subscriber s WHERE s.event.id = :idEvent";
        TypedQuery<Subscriber> query = em.createQuery(jpql, Subscriber.class);
        query.setParameter("idEvent", idEvent);
        return query.getResultList();
    }
    
    public void deleteAllSubscribersByIdEvent(int idEvent) {
        List<Subscriber> subscribers = findAllSubscribersByIdEvent(idEvent);
        for (Subscriber subscriber : subscribers) {
            em.remove(subscriber);
        }
    }
    
    public void deleteEvent(int idEvent) {
        deleteAllSubscribersByIdEvent(idEvent);
        Event event = em.find(Event.class, idEvent);
        em.remove(event);
    }
    
    public List<Stats> findAllVisits() {
        String jpql = "SELECT s FROM Stats s";
        TypedQuery<Stats> query = em.createQuery(jpql, Stats.class);
        return query.getResultList();
    }
    
    public void deleteAllVisits() {
        List<Stats> statsList = findAllVisits();
        for (Stats stats : statsList) {
            em.remove(stats);
        }
    }
    
}