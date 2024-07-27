package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.models.Trainer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TrainerDAO {

    private final SessionFactory sessionFactory;

    private static final String GET_TRAINERS_FOR_TRAINEE_QUERY = """
            SELECT DISTINCT trn 
            FROM Training tr 
            JOIN tr.trainer trn 
            JOIN FETCH trn.user 
            JOIN FETCH trn.specialization 
            JOIN tr.trainee t 
            JOIN t.user u 
            WHERE u.username = :username
            """;

    private static final String GET_TRAINER_BY_USERNAME_QUERY = """
            SELECT t 
            FROM Trainer t 
            JOIN User u ON t.user.id = u.id 
            WHERE u.username = :username
            """;

    private static final String GET_TRAINER_WITH_USER_AND_SPECIALIZATION_QUERY = """
            SELECT t 
            FROM Trainer t 
            JOIN FETCH t.user u 
            LEFT JOIN FETCH t.specialization s 
            WHERE u.username = :username
            """;

    private static final String GET_FREE_TRAINERS_FOR_TRAINEE_QUERY = """
            SELECT DISTINCT trainer 
            FROM Trainer trainer 
            JOIN FETCH trainer.user 
            JOIN FETCH trainer.specialization 
            LEFT JOIN Training training ON training.trainer.id = trainer.id 
            LEFT JOIN training.trainee trainee 
            LEFT JOIN trainee.user user 
            WHERE trainer.id NOT IN (
                SELECT trainer.id 
                FROM Trainer trainer 
                JOIN Training training ON training.trainer.id = trainer.id 
                JOIN training.trainee trainee 
                JOIN trainee.user user 
                WHERE user.username = :username AND user.isActive = true
            )
            """;
    private static final String USERNAME_PARAM = "username";

    @Transactional
    public Trainer save(Trainer trainer) {
        Session session = sessionFactory.getCurrentSession();
        session.save(trainer);
        session.flush();
        session.refresh(trainer);
        log.debug("New trainer with id = {} saved", trainer.getId());
        return trainer;
    }

    public List<Trainer> getTrainersForTrainee(String username) {
        Session session = sessionFactory.getCurrentSession();
        log.debug("Get trainers list for trainee with username = {}", username);
        return session.createQuery(GET_TRAINERS_FOR_TRAINEE_QUERY, Trainer.class)
                .setParameter(USERNAME_PARAM, username)
                .list();
    }

    @Transactional
    public void update(Trainer updatedTrainer) {
        Session session = sessionFactory.getCurrentSession();
        session.update(updatedTrainer);
        log.debug("Trainer with id = {} updated", updatedTrainer.getId());
    }

    public Trainer get(String username) {
        Session session = sessionFactory.getCurrentSession();
        log.debug("Get trainer with username = {}", username);
        return session.createQuery(GET_TRAINER_BY_USERNAME_QUERY, Trainer.class)
                .setParameter(USERNAME_PARAM, username)
                .uniqueResult();
    }

    public Trainer getWithUserAndSpecialization(String username) {
        Session session = sessionFactory.getCurrentSession();
        log.debug("Get trainer with username = {} with user and specialization entities", username);

        Trainer trainer = session.createQuery(GET_TRAINER_WITH_USER_AND_SPECIALIZATION_QUERY, Trainer.class)
                .setParameter(USERNAME_PARAM, username)
                .uniqueResult();
        if (trainer != null) {
            Hibernate.initialize(trainer.getSpecialization());
        }
        return trainer;
    }

    public List<Trainer> getFreeTrainersForTrainee(String username) {
        Session session = sessionFactory.getCurrentSession();
        log.debug("Get trainers list that not assigned to trainee with username = {}", username);

        List<Trainer> freeTrainers = session.createQuery(GET_FREE_TRAINERS_FOR_TRAINEE_QUERY, Trainer.class)
                .setParameter(USERNAME_PARAM, username)
                .list();

        freeTrainers.sort(Comparator.comparingLong(Trainer::getId));
        return freeTrainers;
    }
}
