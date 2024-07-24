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

    public User save(User user) {
        Session session = sessionFactory.getCurrentSession();
        session.save(user);
        session.flush();
        session.refresh(user);
        log.debug("New user with id = {} saved", user.getId());
        return user;
    }

    public boolean changePassword(String username, String newPassword) {
        Session session = sessionFactory.getCurrentSession();
        log.debug("Change password for user with username = {}", username);
        int updatedEntities = session.createQuery("UPDATE User u SET u.password = :password WHERE u.username = :username")
                .setParameter("username", username)
                .setParameter("password", newPassword)
                .executeUpdate();

        return updatedEntities > 0;
    }

    public User get(String username) {
        Session session = sessionFactory.getCurrentSession();
        log.debug("Get user with username = {}", username);
        return session.createQuery("FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .uniqueResult();
    }

    public void update(User updatedUser) {
        Session session = sessionFactory.getCurrentSession();
        session.update(updatedUser);
        log.debug("User with id = {} updated", updatedUser.getId());
    }

    public void delete(String username) {
        Session session = sessionFactory.getCurrentSession();
        int deletedEntities = session.createQuery("DELETE FROM User u WHERE u.username = :username")
                .setParameter("username", username)
                .executeUpdate();
        if (deletedEntities > 0) {
            log.debug("User and related entities with username = {} deleted", username);
        } else {
            log.debug("No user found with username = {}", username);
        }
    }

    public boolean setActive(String username, boolean isActive) {
        Session session = sessionFactory.getCurrentSession();
        log.debug("Change active status for user with username = " + username);
        int updatedEntities = session.createQuery("UPDATE User u SET u.isActive = :isActive WHERE u.username = :username")
                .setParameter("username", username)
                .setParameter("isActive", isActive)
                .executeUpdate();

        return updatedEntities > 0;
    }

    public List<String> findUsernamesByFirstNameAndLastName(String firstName, String lastName) {
        Session session = sessionFactory.getCurrentSession();
        log.debug("Get all usernames by firstname = " + firstName + " and lastname = " + lastName);
        return session.createQuery("SELECT u.username FROM User u WHERE u.firstName = :firstName AND u.lastName = :lastName ORDER BY u.username", String.class)
                .setParameter("firstName", firstName)
                .setParameter("lastName", lastName)
                .list();
    }

    public User getByUsernameAndPassword(String username, String password) {
        Session session = sessionFactory.getCurrentSession();
        log.debug("Get user with username = " + username + " for authentication");
        return session.createQuery("FROM User u WHERE u.username = :username AND u.password = :password", User.class)
                .setParameter("username", username)
                .setParameter("password", password)
                .uniqueResult();
    }
}

