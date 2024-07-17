package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.models.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserDAO {

    private final SessionFactory sessionFactory;

    @Transactional
    public User save(User user) {
        Session session = sessionFactory.getCurrentSession();
        session.save(user);
        session.flush();
        session.refresh(user);
        log.debug("New user with id = " + user.getId() + " saved");
        return user;
    }

    @Transactional
    public void update(User updatedUser) {
        Session session = sessionFactory.getCurrentSession();
        session.update(updatedUser);
        log.debug("User with id = " + updatedUser.getId() + " updated");
    }

    @Transactional
    public void delete(int id) {
        Session session = sessionFactory.getCurrentSession();
        session.remove(session.get(User.class, id));
        log.debug("User with id = " + id + " deleted");
    }

    public User get(int id) {
        Session session = sessionFactory.getCurrentSession();
        log.debug("Get user with id = " + id);
        return session.get(User.class, id);
    }

    public List<String> findUsernamesByFirstNameAndLastName(String firstName, String lastName) {
        Session session = sessionFactory.getCurrentSession();
        log.debug("Get all usernames by firstname = " + firstName + " and lastname = " + lastName);
        return session.createQuery("SELECT u.username FROM User u WHERE u.firstName = :firstName AND u.lastName = :lastName ORDER BY u.username", String.class)
                .setParameter("firstName", firstName)
                .setParameter("lastName", lastName)
                .list();
    }

    public User getByUsername(String username) {
        Session session = sessionFactory.getCurrentSession();
        log.debug("Get user with username = " + username);
        return session.createQuery("FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .uniqueResult();
    }

    @Transactional
    public boolean changePassword(int id, String newPassword) {
        Session session = sessionFactory.getCurrentSession();
        log.debug("Change password for user with id = " + id);
        int updatedEntities = session.createQuery("UPDATE User u SET u.password = :password WHERE u.id = :id")
                .setParameter("id", id)
                .setParameter("password", newPassword)
                .executeUpdate();

        return updatedEntities > 0;
    }

    @Transactional
    public boolean active(int id, boolean isActive) {
        Session session = sessionFactory.getCurrentSession();
        log.debug("Change active status for user with id = " + id);
        int updatedEntities = session.createQuery("UPDATE User u SET u.isActive = :isActive WHERE u.id = :id")
                .setParameter("id", id)
                .setParameter("isActive", isActive)
                .executeUpdate();

        return updatedEntities > 0;
    }

    @Transactional
    public void deleteByUsername(String username) {
        Session session = sessionFactory.getCurrentSession();
        int deletedEntities = session.createQuery("DELETE FROM User u WHERE u.username = :username")
                .setParameter("username", username)
                .executeUpdate();
        if (deletedEntities > 0) {
            log.debug("User and related entities with username = " + username + " deleted");
        } else {
            log.debug("No user found with username = " + username);
        }
    }
}

