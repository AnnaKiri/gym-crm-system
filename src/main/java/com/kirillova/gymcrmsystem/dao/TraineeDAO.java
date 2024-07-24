package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.models.Trainee;
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
public class TraineeDAO {

    private final SessionFactory sessionFactory;

    @Transactional
    public Trainee save(Trainee trainee) {
        Session session = sessionFactory.getCurrentSession();
        session.save(trainee);
        session.flush();
        session.refresh(trainee);
        log.debug("New trainee with id = " + trainee.getId() + " saved");
        return trainee;
    }

    @Transactional
    public void update(Trainee updatedTrainee) {
        Session session = sessionFactory.getCurrentSession();
        session.update(updatedTrainee);
        log.debug("Trainee with id = " + updatedTrainee.getId() + " updated");
    }

    @Transactional
    public void delete(int id) {
        Session session = sessionFactory.getCurrentSession();
        session.remove(session.get(Trainee.class, id));
        log.debug("Trainee with id = " + id + " deleted");
    }

    public Trainee get(int id) {
        Session session = sessionFactory.getCurrentSession();
        log.debug("Get trainee with id = " + id);
        return session.get(Trainee.class, id);
    }

    public Trainee getByUserId(int userId) {
        Session session = sessionFactory.getCurrentSession();
        log.debug("Get trainee with userid = " + userId);
        return session.createQuery("FROM Trainee t WHERE t.user.id = :userId", Trainee.class)
                .setParameter("userId", userId)
                .uniqueResult();
    }

    public List<Trainee> getTraineesForTrainer(int id) {
        Session session = sessionFactory.getCurrentSession();
        log.debug("Get trainees list for trainer with id = " + id);
        return session.createQuery("SELECT DISTINCT t " +
                        "FROM Trainee t " +
                        "JOIN Training tr ON tr.trainee.id = t.id " +
                        "WHERE tr.trainer.id = :trainerId", Trainee.class)
                .setParameter("trainerId", id)
                .list();
    }


}

