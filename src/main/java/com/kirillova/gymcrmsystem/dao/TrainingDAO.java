package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.models.Training;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TrainingDAO {

    private final SessionFactory sessionFactory;

    @Transactional
    public Training save(Training training) {
        Session session = sessionFactory.getCurrentSession();
        session.save(training);
        session.flush();
        session.refresh(training);
        log.debug("New training with id = " + training.getId() + " saved");
        return training;
    }

    public Training get(int id) {
        Session session = sessionFactory.getCurrentSession();
        log.debug("Get training with id = " + id);
        return session.get(Training.class, id);
    }

    public Training getFull(int id) {
        Session session = sessionFactory.getCurrentSession();
        log.debug("Get training with id = " + id);
        return session.createQuery("SELECT tr FROM Training tr " +
                        "JOIN FETCH tr.trainee t " +
                        "JOIN FETCH t.user tu " +
                        "JOIN FETCH tr.trainer r " +
                        "JOIN FETCH r.user ru " +
                        "JOIN FETCH tr.type tp " +
                        "WHERE tr.id = :id", Training.class)
                .setParameter("id", id)
                .uniqueResult();
    }

    @Transactional
    public List<Training> getTraineeTrainings(String traineeUsername, LocalDate fromDate, LocalDate toDate, String trainingType, String trainerFirstName, String trainerLastName) {
        return getTrainings(traineeUsername, null, fromDate, toDate, trainingType, trainerFirstName, trainerLastName, null, null);
    }

    @Transactional
    public List<Training> getTrainerTrainings(String trainerUsername, LocalDate fromDate, LocalDate toDate, String traineeFirstName, String traineeLastName) {
        return getTrainings(null, trainerUsername, fromDate, toDate, null, null, null, traineeFirstName, traineeLastName);
    }

    private List<Training> getTrainings(String traineeUsername, String trainerUsername, LocalDate fromDate, LocalDate toDate,
                                        String trainingType, String trainerFirstName, String trainerLastName,
                                        String traineeFirstName, String traineeLastName) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Training> cq = cb.createQuery(Training.class);
        Root<Training> training = cq.from(Training.class);

        // Use fetch instead of join
        Fetch<Object, Object> traineeFetch = training.fetch("trainee", JoinType.LEFT);
        Fetch<Object, Object> trainerFetch = training.fetch("trainer", JoinType.LEFT);
        Fetch<Object, Object> traineeUserFetch = ((Join<?, ?>) traineeFetch).fetch("user", JoinType.LEFT);
        Fetch<Object, Object> trainerUserFetch = ((Join<?, ?>) trainerFetch).fetch("user", JoinType.LEFT);
        Fetch<Object, Object> trainingTypeFetch = training.fetch("type", JoinType.LEFT);

        Predicate predicate = cb.conjunction();

        if (traineeUsername != null && !traineeUsername.isEmpty()) {
            predicate = cb.and(predicate, cb.equal(((Join<?, ?>) traineeUserFetch).get("username"), traineeUsername));
        }

        if (trainerUsername != null && !trainerUsername.isEmpty()) {
            predicate = cb.and(predicate, cb.equal(((Join<?, ?>) trainerUserFetch).get("username"), trainerUsername));
        }

        if (fromDate != null) {
            predicate = cb.and(predicate, cb.greaterThanOrEqualTo(training.get("date"), fromDate));
        }

        if (toDate != null) {
            predicate = cb.and(predicate, cb.lessThanOrEqualTo(training.get("date"), toDate));
        }

        if (trainerFirstName != null && !trainerFirstName.isEmpty()) {
            predicate = cb.and(predicate, cb.equal(((Join<?, ?>) trainerUserFetch).get("firstName"), trainerFirstName));
        }

        if (trainerLastName != null && !trainerLastName.isEmpty()) {
            predicate = cb.and(predicate, cb.equal(((Join<?, ?>) trainerUserFetch).get("lastName"), trainerLastName));
        }

        if (traineeFirstName != null && !traineeFirstName.isEmpty()) {
            predicate = cb.and(predicate, cb.equal(((Join<?, ?>) traineeUserFetch).get("firstName"), traineeFirstName));
        }

        if (traineeLastName != null && !traineeLastName.isEmpty()) {
            predicate = cb.and(predicate, cb.equal(((Join<?, ?>) traineeUserFetch).get("lastName"), traineeLastName));
        }

        if (trainingType != null && !trainingType.isEmpty()) {
            predicate = cb.and(predicate, cb.equal(((Join<?, ?>) trainingTypeFetch).get("name"), trainingType));
        }

        cq.where(predicate);

        List<Training> trainings = session.createQuery(cq).getResultList();
        log.debug("Filtered trainings found: " + trainings.size());
        return trainings;
    }

}
