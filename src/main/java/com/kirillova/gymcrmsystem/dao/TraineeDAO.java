package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.models.Trainee;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TraineeDAO {

    private final SessionFactory sessionFactory;

    @Transactional
    public Trainee save(Trainee trainee) {
        Session session = sessionFactory.getCurrentSession();
        session.save(trainee);
        session.flush();
        session.refresh(trainee);
        log.debug("New trainee with id = " + trainee.getId() + " saved");
        return trainee;
    }

    @Transactional
    public void update(Trainee updatedTrainee) {
        Session session = sessionFactory.getCurrentSession();
        session.update(updatedTrainee);
        log.debug("Trainee with id = " + updatedTrainee.getId() + " updated");
    }

    @Transactional
    public void delete(int traineeId) {
        Session session = sessionFactory.getCurrentSession();
        session.remove(session.get(Trainee.class, traineeId));
        log.debug("Trainee with id = " + traineeId + " deleted");
    }

    public Trainee get(int traineeId) {
        Session session = sessionFactory.getCurrentSession();
        log.debug("Get trainee with id = " + traineeId);
        return session.get(Trainee.class, traineeId);
    }
}

