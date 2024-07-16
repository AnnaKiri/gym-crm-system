package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.models.TrainingType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class TrainingTypeDAO {

    private final SessionFactory sessionFactory;

    public TrainingType getTrainingType(long trainingTypeId) {
        Session session = sessionFactory.getCurrentSession();
        log.debug("Get training with id = " + trainingTypeId);
        return session.get(TrainingType.class, trainingTypeId);
    }
}

