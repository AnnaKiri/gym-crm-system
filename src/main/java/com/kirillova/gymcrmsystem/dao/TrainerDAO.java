package com.kirillova.gymcrmsystem.dao;

import com.kirillova.gymcrmsystem.models.Trainer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
}
