package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.models.Trainee;
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
public class TraineeDAO {

    private final SessionFactory sessionFactory;

    private static final String GET_TRAINEE_BY_USERNAME_QUERY = "SELECT t FROM Trainee t JOIN User u ON t.user.id = u.id WHERE u.username = :username";
    private static final String GET_TRAINEE_WITH_USER_BY_USERNAME_QUERY = "SELECT t FROM Trainee t JOIN FETCH t.user u WHERE u.username = :username";
    private static final String GET_TRAINEES_FOR_TRAINER_QUERY = "SELECT DISTINCT t FROM Training tr JOIN tr.trainee t JOIN FETCH t.user u JOIN tr.trainer trn WHERE trn.user.username = :username";
    private static final String USERNAME_PARAM = "username";

    @Transactional
    public Trainee save(Trainee trainee) {
        Session session = sessionFactory.getCurrentSession();
        session.save(trainee);
        session.flush();
        session.refresh(trainee);
        log.debug("New trainee with id = {} saved", trainee.getId());
        return trainee;
    }

    @Transactional
    public void update(Trainee updatedTrainee) {
        Session session = sessionFactory.getCurrentSession();
        session.update(updatedTrainee);
        session.flush();
        log.debug("Trainee with id = {} updated", updatedTrainee.getId());
    }

    public Trainee get(String username) {
        Session session = sessionFactory.getCurrentSession();
        log.debug("Get trainee with username = {}", username);
        return session.createQuery(GET_TRAINEE_BY_USERNAME_QUERY, Trainee.class)
                .setParameter(USERNAME_PARAM, username)
                .uniqueResult();
    }

    public Trainee getWithUser(String username) {
        Session session = sessionFactory.getCurrentSession();
        log.debug("Get trainee with username = {} with user entity", username);
        return session.createQuery(GET_TRAINEE_WITH_USER_BY_USERNAME_QUERY, Trainee.class)
                .setParameter(USERNAME_PARAM, username)
                .uniqueResult();
    }

    public List<Trainee> getTraineesForTrainer(String username) {
        Session session = sessionFactory.getCurrentSession();
        log.debug("Get trainees list for trainer with username = {}", username);
        return session.createQuery(GET_TRAINEES_FOR_TRAINER_QUERY, Trainee.class)
                .setParameter(USERNAME_PARAM, username)
                .list();
    }
}
