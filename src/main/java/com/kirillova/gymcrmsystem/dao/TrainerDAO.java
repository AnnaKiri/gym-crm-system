package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.models.Trainer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Transactional
    public Trainer save(Trainer trainer) {
        Session session = sessionFactory.getCurrentSession();
        session.save(trainer);
        session.flush();
        session.refresh(trainer);
        log.debug("New trainer with id = " + trainer.getId() + " saved");
        return trainer;
    }

    @Transactional
    public void update(Trainer updatedTrainer) {
        Session session = sessionFactory.getCurrentSession();
        session.update(updatedTrainer);
        log.debug("Trainer with id = " + updatedTrainer.getId() + " updated");
    }

    public Trainer get(int id) {
        Session session = sessionFactory.getCurrentSession();
        log.debug("Get trainer with id = " + id);
        return session.get(Trainer.class, id);
    }

    public Trainer getByUserId(int userId) {
        Session session = sessionFactory.getCurrentSession();
        log.debug("Get trainer with userid = " + userId);
        return session.createQuery("FROM Trainer t WHERE t.user.id = :userId", Trainer.class)
                .setParameter("userId", userId)
                .uniqueResult();
    }

    public List<Trainer> getFreeTrainersForUsername(String username) {
        Session session = sessionFactory.getCurrentSession();
        log.debug("Get trainers list that not assigned to trainee with username = " + username);

        String hql = "SELECT DISTINCT trainer " +
                "FROM Trainer trainer " +
                "JOIN Training training ON training.trainer.id = trainer.id " +
                "JOIN training.trainee trainee " +
                "JOIN trainee.user user " +
                "WHERE trainer.id NOT IN (" +
                "    SELECT trainer.id " +
                "    FROM Trainer trainer " +
                "    JOIN Training training ON training.trainer.id = trainer.id " +
                "    JOIN training.trainee trainee " +
                "    JOIN trainee.user user " +
                "    WHERE user.username = :username" +
                ")";

        List<Trainer> freeTrainers = session.createQuery(hql, Trainer.class)
                .setParameter("username", username)
                .list();

        freeTrainers.sort(Comparator.comparingLong(Trainer::getId));
        return freeTrainers;
    }

    public List<Trainer> getTrainersForTrainee(int id) {
        Session session = sessionFactory.getCurrentSession();
        log.debug("Get trainers list for trainee with id = " + id);
        return session.createQuery("SELECT DISTINCT t " +
                        "FROM Trainer t " +
                        "JOIN Training tr ON tr.trainer.id = t.id " +
                        "WHERE tr.trainee.id = :traineeId", Trainer.class)
                .setParameter("traineeId", id)
                .list();
    }
}
