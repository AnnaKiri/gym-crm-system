package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.models.TrainingType;
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
public class TrainingTypeDAO {

    private final SessionFactory sessionFactory;

    public List<TrainingType> getAll() {
        Session session = sessionFactory.getCurrentSession();
        log.debug("Get training types");
        return session.createQuery("FROM TrainingType", TrainingType.class).getResultList();
    }

    public TrainingType get(int id) {
        Session session = sessionFactory.getCurrentSession();
        log.debug("Get training type with id {}", id);
        return session.get(TrainingType.class, id);
    }
}

