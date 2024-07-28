package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.error.NotFoundException;
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

    private static final String UPDATE_USER_PASSWORD_QUERY = "UPDATE User u SET u.password = :password WHERE u.username = :username";
    private static final String GET_USER_BY_USERNAME_QUERY = "FROM User u WHERE u.username = :username";
    private static final String DELETE_USER_BY_USERNAME_QUERY = "DELETE FROM User u WHERE u.username = :username";
    private static final String UPDATE_USER_ACTIVE_STATUS_QUERY = "UPDATE User u SET u.isActive = :isActive WHERE u.username = :username";
    private static final String FIND_USERNAMES_BY_FIRST_NAME_AND_LAST_NAME_QUERY = "SELECT u.username FROM User u WHERE u.firstName = :firstName AND u.lastName = :lastName ORDER BY u.username";
    private static final String GET_USER_BY_USERNAME_AND_PASSWORD_QUERY = "FROM User u WHERE u.username = :username AND u.password = :password";
    private static final String USERNAME_PARAM = "username";
    private static final String PASSWORD_PARAM = "password";
    private static final String IS_ACTIVE_PARAM = "isActive";
    private static final String FIRST_NAME_PARAM = "firstName";
    private static final String LAST_NAME_PARAM = "lastName";

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
        int updatedEntities = session.createQuery(UPDATE_USER_PASSWORD_QUERY)
                .setParameter(USERNAME_PARAM, username)
                .setParameter(PASSWORD_PARAM, newPassword)
                .executeUpdate();

        if (updatedEntities > 0) {
            log.debug("Changed password for user with username = {}", username);
            return true;
        } else {
            throw new NotFoundException("Not found entity with " + username);
        }
    }

    public User get(String username) {
        Session session = sessionFactory.getCurrentSession();
        log.debug("Get user with username = {}", username);
        return session.createQuery(GET_USER_BY_USERNAME_QUERY, User.class)
                .setParameter(USERNAME_PARAM, username)
                .uniqueResult();
    }

    public void update(User updatedUser) {
        Session session = sessionFactory.getCurrentSession();
        session.update(updatedUser);
        log.debug("User with id = {} updated", updatedUser.getId());
    }

    public boolean delete(String username) {
        Session session = sessionFactory.getCurrentSession();
        int deletedEntities = session.createQuery(DELETE_USER_BY_USERNAME_QUERY)
                .setParameter(USERNAME_PARAM, username)
                .executeUpdate();
        if (deletedEntities > 0) {
            log.debug("User and related entities with username = {} deleted", username);
            return true;
        } else {
            throw new NotFoundException("Not found entity with " + username);
        }
    }

    public boolean setActive(String username, boolean isActive) {
        Session session = sessionFactory.getCurrentSession();
        int updatedEntities = session.createQuery(UPDATE_USER_ACTIVE_STATUS_QUERY)
                .setParameter(USERNAME_PARAM, username)
                .setParameter(IS_ACTIVE_PARAM, isActive)
                .executeUpdate();

        if (updatedEntities > 0) {
            log.debug("Changed active status for user with username = {}", username);
            return true;
        } else {
            throw new NotFoundException("Not found entity with " + username);
        }
    }

    public List<String> findUsernamesByFirstNameAndLastName(String firstName, String lastName) {
        Session session = sessionFactory.getCurrentSession();
        log.debug("Get all usernames by firstname = {} and lastname = {}", firstName, lastName);
        return session.createQuery(FIND_USERNAMES_BY_FIRST_NAME_AND_LAST_NAME_QUERY, String.class)
                .setParameter(FIRST_NAME_PARAM, firstName)
                .setParameter(LAST_NAME_PARAM, lastName)
                .list();
    }

    public User getByUsernameAndPassword(String username, String password) {
        Session session = sessionFactory.getCurrentSession();
        log.debug("Get user with username = {} for authentication", username);
        return session.createQuery(GET_USER_BY_USERNAME_AND_PASSWORD_QUERY, User.class)
                .setParameter(USERNAME_PARAM, username)
                .setParameter(PASSWORD_PARAM, password)
                .uniqueResult();
    }
}
