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

    private static final String GET_ALL_TRAINING_TYPES_QUERY = "FROM TrainingType";
    private static final String GET_TRAINING_TYPE_BY_ID_QUERY = "FROM TrainingType WHERE id = :id";
    private static final String ID_PARAM = "id";

    public List<TrainingType> getAll() {
        Session session = sessionFactory.getCurrentSession();
        log.debug("Get training types");
        return session.createQuery(GET_ALL_TRAINING_TYPES_QUERY, TrainingType.class).getResultList();
    }

    public TrainingType get(int id) {
        Session session = sessionFactory.getCurrentSession();
        log.debug("Get training type with id {}", id);
        return session.createQuery(GET_TRAINING_TYPE_BY_ID_QUERY, TrainingType.class)
                .setParameter(ID_PARAM, id)
                .uniqueResult();
    }
}
