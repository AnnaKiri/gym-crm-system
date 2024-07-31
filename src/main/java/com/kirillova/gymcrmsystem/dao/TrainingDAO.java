package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.models.Training;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Fetch;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TrainingDAO {

    private final SessionFactory sessionFactory;

    private static final String GET_TRAINING_WITH_DETAILS_QUERY = """
            SELECT tr FROM Training tr 
            JOIN FETCH tr.trainee t 
            JOIN FETCH t.user tu 
            JOIN FETCH tr.trainer r 
            JOIN FETCH r.user ru 
            JOIN FETCH tr.type tp 
            WHERE tr.id = :id
            """;
    private static final String ID_PARAM = "id";
    private static final String TRAINEE_PARAM = "trainee";
    private static final String TRAINER_PARAM = "trainer";
    private static final String DATE_PARAM = "date";
    private static final String NAME_PARAM = "name";
    private static final String FIRST_NAME_PARAM = "firstName";
    private static final String LAST_NAME_PARAM = "lastName";
    private static final String USERNAME_PARAM = "username";

    public Training save(Training training) {
        Session session = sessionFactory.getCurrentSession();
        session.save(training);
        session.flush();
        session.refresh(training);
        log.debug("New training with id = {} saved", training.getId());
        return training;
    }

    public Training get(int id) {
        Session session = sessionFactory.getCurrentSession();
        log.debug("Get training with id = {}", id);
        return session.get(Training.class, id);
    }

    public Training getFull(int id) {
        Session session = sessionFactory.getCurrentSession();
        log.debug("Get training with id = {}", id);
        return session.createQuery(GET_TRAINING_WITH_DETAILS_QUERY, Training.class)
                .setParameter(ID_PARAM, id)
                .uniqueResult();
    }

    public List<Training> getTraineeTrainings(String traineeUsername, LocalDate fromDate,
                                              LocalDate toDate, String trainingType,
                                              String trainerFirstName, String trainerLastName) {
        return getTrainings(traineeUsername, null,
                fromDate, toDate, trainingType, trainerFirstName,
                trainerLastName, null, null);
    }

    public List<Training> getTrainerTrainings(String trainerUsername,
                                              LocalDate fromDate, LocalDate toDate,
                                              String traineeFirstName, String traineeLastName) {
        return getTrainings(null, trainerUsername,
                fromDate, toDate, null,
                null, null,
                traineeFirstName, traineeLastName);
    }

    private List<Training> getTrainings(String traineeUsername, String trainerUsername, LocalDate fromDate, LocalDate toDate,
                                        String trainingType, String trainerFirstName, String trainerLastName,
                                        String traineeFirstName, String traineeLastName) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Training> cq = cb.createQuery(Training.class);
        Root<Training> training = cq.from(Training.class);

        Fetch<Object, Object> traineeFetch = training.fetch(TRAINEE_PARAM, JoinType.LEFT);
        Fetch<Object, Object> trainerFetch = training.fetch(TRAINER_PARAM, JoinType.LEFT);
        Fetch<Object, Object> traineeUserFetch = ((Join<?, ?>) traineeFetch).fetch("user", JoinType.LEFT);
        Fetch<Object, Object> trainerUserFetch = ((Join<?, ?>) trainerFetch).fetch("user", JoinType.LEFT);
        Fetch<Object, Object> trainingTypeFetch = training.fetch("type", JoinType.LEFT);

        Predicate predicate = cb.conjunction();

        if (StringUtils.isNotBlank(traineeUsername)) {
            predicate = cb.and(predicate, cb.equal(((Join<?, ?>) traineeUserFetch).get(USERNAME_PARAM), traineeUsername));
        }

        if (StringUtils.isNotBlank(trainerUsername)) {
            predicate = cb.and(predicate, cb.equal(((Join<?, ?>) trainerUserFetch).get(USERNAME_PARAM), trainerUsername));
        }

        if (fromDate != null) {
            predicate = cb.and(predicate, cb.greaterThanOrEqualTo(training.get(DATE_PARAM), fromDate));
        }

        if (toDate != null) {
            predicate = cb.and(predicate, cb.lessThanOrEqualTo(training.get(DATE_PARAM), toDate));
        }

        if (StringUtils.isNotBlank(trainerFirstName)) {
            predicate = cb.and(predicate, cb.equal(((Join<?, ?>) trainerUserFetch).get(FIRST_NAME_PARAM), trainerFirstName));
        }

        if (StringUtils.isNotBlank(trainerLastName)) {
            predicate = cb.and(predicate, cb.equal(((Join<?, ?>) trainerUserFetch).get(LAST_NAME_PARAM), trainerLastName));
        }

        if (StringUtils.isNotBlank(traineeFirstName)) {
            predicate = cb.and(predicate, cb.equal(((Join<?, ?>) traineeUserFetch).get(FIRST_NAME_PARAM), traineeFirstName));
        }

        if (StringUtils.isNotBlank(traineeLastName)) {
            predicate = cb.and(predicate, cb.equal(((Join<?, ?>) traineeUserFetch).get(LAST_NAME_PARAM), traineeLastName));
        }

        if (StringUtils.isNotBlank(trainingType)) {
            predicate = cb.and(predicate, cb.equal(((Join<?, ?>) trainingTypeFetch).get(NAME_PARAM), trainingType));
        }

        cq.where(predicate);

        List<Training> trainings = session.createQuery(cq).getResultList();
        log.debug("Filtered trainings found: {}", trainings.size());
        return trainings;
    }
}
