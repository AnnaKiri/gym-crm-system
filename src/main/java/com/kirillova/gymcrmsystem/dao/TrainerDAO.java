package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.models.Trainer;
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

    public Trainer get(int trainerId) {
        Session session = sessionFactory.getCurrentSession();
        log.debug("Get trainer with id = " + trainerId);
        return session.get(Trainer.class, trainerId);
    }

    public Trainer getByUserId(int userId) {
        Session session = sessionFactory.getCurrentSession();
        log.debug("Get trainer with userid = " + userId);
        return session.createQuery("FROM Trainer t WHERE t.user.id = :userId", Trainer.class)
                .setParameter("userId", userId)
                .uniqueResult();
    }

    public List<Trainer> getFreeTrainersForTrainee(String traineeUsername) {
        Session session = sessionFactory.getCurrentSession();
        log.debug("Get trainers list that not assigned on trainee by trainee's username = " + traineeUsername);
        return session.createQuery("SELECT DISTINCT Trainer " +
                        "FROM Trainer " +
                        "JOIN Trainer.user userTrainer " +
                        "JOIN Training ON Training.trainer = Trainer " +
                        "JOIN Training.trainee " +
                        "JOIN Trainee.user " +
                        "WHERE Trainer NOT IN (" +
                        "    SELECT Trainer " +
                        "    FROM Trainer " +
                        "    JOIN Trainer.user userTrainer " +
                        "    JOIN Training ON Training.trainer = Trainer " +
                        "    JOIN Training.trainee " +
                        "    JOIN Trainee.user " +
                        "    WHERE User.username = :username" +
                        ")", Trainer.class)
                .setParameter("username", traineeUsername)
                .list();

//
//        CriteriaBuilder builder = session.getCriteriaBuilder();
//        CriteriaQuery<Trainer> query = builder.createQuery(Trainer.class);
//        Root<Trainer> trainerRoot = query.from(Trainer.class);
//
//        // Subquery to find trainers already assigned to the trainee
//        CriteriaQuery<Trainer> subquery = builder.createQuery(Trainer.class);
//        Root<Trainer> subTrainerRoot = subquery.from(Trainer.class);
//        Join<Trainer, Training> subTrainingJoin = subTrainerRoot.join("trainings");
//        Join<Training, Trainee> subTraineeJoin = subTrainingJoin.join("trainee");
//        Join<Trainee, User> subUserJoin = subTraineeJoin.join("user");
//
//        subquery.select(subTrainerRoot)
//                .where(builder.equal(subUserJoin.get("username"), traineeUsername));
//
//        // Main query to find trainers not assigned to the trainee
//        query.select(trainerRoot).distinct(true)
//                .where(builder.not(trainerRoot.in(subquery)));
//
//        return session.createQuery(query).getResultList();
    }

    public List<Trainer> getTrainersForTrainee(int traineeId) {
        Session session = sessionFactory.getCurrentSession();
        log.debug("Get trainers list for trainee with id = " + traineeId);
        return session.createQuery("SELECT DISTINCT t " +
                        "FROM Trainer t " +
                        "JOIN Training tr ON tr.trainer.id = t.id " +
                        "WHERE tr.trainee.id = :traineeId", Trainer.class)
                .setParameter("traineeId", traineeId)
                .list();
    }
}
