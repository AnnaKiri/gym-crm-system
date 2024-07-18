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
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
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

    public Training get(int trainingId) {
        Session session = sessionFactory.getCurrentSession();
        log.debug("Get training with id = " + trainingId);
        return session.get(Training.class, trainingId);
    }

    @Transactional
    public List<Training> getTraineeTrainings(String traineeUsername, Date fromDate, Date toDate, String trainingType, String trainerFirstName, String trainerLastName) {
        return getTrainings(traineeUsername, null, fromDate, toDate, trainingType, trainerFirstName, trainerLastName, null, null);
    }

    @Transactional
    public List<Training> getTrainerTrainings(String trainerUsername, Date fromDate, Date toDate, String traineeFirstName, String traineeLastName) {
        return getTrainings(null, trainerUsername, fromDate, toDate, null, null, null, traineeFirstName, traineeLastName);
    }

    private List<Training> getTrainings(String traineeUsername, String trainerUsername, Date fromDate, Date toDate,
                                        String trainingType, String trainerFirstName, String trainerLastName,
                                        String traineeFirstName, String traineeLastName) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Training> cq = cb.createQuery(Training.class);
        Root<Training> training = cq.from(Training.class);
        Join<Object, Object> traineeJoin = training.join("trainee", JoinType.LEFT);
        Join<Object, Object> trainerJoin = training.join("trainer", JoinType.LEFT);
        Join<Object, Object> traineeUserJoin = traineeJoin.join("user", JoinType.LEFT);
        Join<Object, Object> trainerUserJoin = trainerJoin.join("user", JoinType.LEFT);
        Join<Object, Object> trainingTypeJoin = training.join("type", JoinType.LEFT);

        Predicate predicate = cb.conjunction();

        if (traineeUsername != null && !traineeUsername.isEmpty()) {
            predicate = cb.and(predicate, cb.equal(traineeUserJoin.get("username"), traineeUsername));
        }

        if (trainerUsername != null && !trainerUsername.isEmpty()) {
            predicate = cb.and(predicate, cb.equal(trainerUserJoin.get("username"), trainerUsername));
        }

        if (fromDate != null) {
            predicate = cb.and(predicate, cb.greaterThanOrEqualTo(training.get("date"), fromDate));
        }

        if (toDate != null) {
            predicate = cb.and(predicate, cb.lessThanOrEqualTo(training.get("date"), toDate));
        }

        if (trainerFirstName != null && !trainerFirstName.isEmpty()) {
            predicate = cb.and(predicate, cb.equal(trainerUserJoin.get("firstName"), trainerFirstName));
        }

        if (trainerLastName != null && !trainerLastName.isEmpty()) {
            predicate = cb.and(predicate, cb.equal(trainerUserJoin.get("lastName"), trainerLastName));
        }

        if (traineeFirstName != null && !traineeFirstName.isEmpty()) {
            predicate = cb.and(predicate, cb.equal(traineeUserJoin.get("firstName"), traineeFirstName));
        }

        if (traineeLastName != null && !traineeLastName.isEmpty()) {
            predicate = cb.and(predicate, cb.equal(traineeUserJoin.get("lastName"), traineeLastName));
        }

        if (trainingType != null && !trainingType.isEmpty()) {
            predicate = cb.and(predicate, cb.equal(trainingTypeJoin.get("name"), trainingType));
        }

        cq.where(predicate);

        List<Training> trainings = session.createQuery(cq).getResultList();
        log.debug("Filtered trainings found: " + trainings.size());
        return trainings;
    }
}
