package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.models.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserDAO {

    private final SessionFactory sessionFactory;

    public User save(User user) {
        Session session = sessionFactory.getCurrentSession();
        session.save(user);
        session.flush();
        session.refresh(user);
        log.debug("New user with id = " + user.getId() + " saved");
        return user;
    }

    public void update(User updatedUser) {
        Session session = sessionFactory.getCurrentSession();
        session.update(updatedUser);
        log.debug("User with id = " + updatedUser.getId() + " updated");
    }

    public void delete(int userId) {
        Session session = sessionFactory.getCurrentSession();
        session.remove(session.get(User.class, userId));
        log.debug("User with id = " + userId + " deleted");
    }

    public User getUser(int userId) {
        Session session = sessionFactory.getCurrentSession();
        log.debug("Get user with id = " + userId);
        return session.get(User.class, userId);
    }
}

