package com.kirillova.gymcrmsystem.repository;

import com.kirillova.gymcrmsystem.error.NotFoundException;
import com.kirillova.gymcrmsystem.models.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface TraineeRepository extends JpaRepository<Trainee, Integer>, JpaSpecificationExecutor<Trainee> {

    @Query("SELECT DISTINCT t FROM Training tr JOIN tr.trainee t JOIN FETCH t.user u JOIN tr.trainer trn WHERE trn.user.username = :username")
    List<Trainee> findTraineesByTrainerUsername(@Param("username") String username);

    @Query("SELECT t FROM Trainee t JOIN FETCH t.user u WHERE u.username = :username")
    Optional<Trainee> getWithUser(@Param("username") String username);

    @Query("SELECT t FROM Trainee t JOIN t.user u WHERE u.username = :username")
    Optional<Trainee> findByUsername(@Param("username") String username);

    default Trainee getExisted(String username) {
        return findByUsername(username).orElseThrow(() -> new NotFoundException("Trainee with username=" + username + " not found"));
    }
}
