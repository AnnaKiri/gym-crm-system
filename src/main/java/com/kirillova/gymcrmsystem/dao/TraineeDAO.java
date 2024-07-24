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

    public Trainee save(Trainee trainee) {
        Session session = sessionFactory.getCurrentSession();
        session.save(trainee);
        session.flush();
        session.refresh(trainee);
        log.debug("New trainee with id = {} saved", trainee.getId());
        return trainee;
    }

    public void update(Trainee updatedTrainee) {
        Session session = sessionFactory.getCurrentSession();
        session.update(updatedTrainee);
        log.debug("Trainee with id = {} updated", updatedTrainee.getId());
    }

    public Trainee get(String username) {
        Session session = sessionFactory.getCurrentSession();
        log.debug("Get trainee with username = {}", username);
        return session.createQuery("SELECT t " +
                        "FROM Trainee t " +
                        "JOIN User u ON t.user.id = u.id " +
                        "WHERE u.username = :username", Trainee.class)
                .setParameter("username", username)
                .uniqueResult();
    }

    public List<Trainee> getTraineesForTrainer(String username) {
        Session session = sessionFactory.getCurrentSession();
        log.debug("Get trainees list for trainer with username = {}", username);
        return session.createQuery("SELECT DISTINCT t " +
                        "FROM Trainee t " +
                        "JOIN User u ON t.user.id = t.id " +
                        "JOIN Training tr ON tr.trainee.id = t.id " +
                        "WHERE u.username = :username", Trainee.class)
                .setParameter("username", username)
                .list();
    }
}

