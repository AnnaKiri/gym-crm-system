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
    public void delete(int userId) {
        Session session = sessionFactory.getCurrentSession();
        session.remove(session.get(User.class, userId));
        log.debug("User with id = " + userId + " deleted");
    }

    public User get(int userId) {
        Session session = sessionFactory.getCurrentSession();
        log.debug("Get user with id = " + userId);
        return session.get(User.class, userId);
    }

    public List<String> findUsernamesByFirstNameAndLastName(String firstName, String lastName) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("SELECT u.username FROM User u WHERE u.firstName = :firstName AND u.lastName = :lastName ORDER BY u.username", String.class)
                .setParameter("firstName", firstName)
                .setParameter("lastName", lastName)
                .list();
    }
}

