package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.models.Training;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class TrainingDAO {

    private final SessionFactory sessionFactory;

    public Training save(Training training) {
        Session session = sessionFactory.getCurrentSession();
        session.save(training);
        session.flush();
        session.refresh(training);
        log.debug("New training with id = " + training.getId() + " saved");
        return training;
    }

    public Training getTraining(long trainingId) {
        Session session = sessionFactory.getCurrentSession();
        log.debug("Get training with id = " + trainingId);
        return session.get(Training.class, trainingId);
    }
}
