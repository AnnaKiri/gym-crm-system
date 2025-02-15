package com.annakirillova.crmsystem.repository;

import com.annakirillova.crmsystem.exception.NotFoundException;
import com.annakirillova.crmsystem.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.isActive = :isActive WHERE u.username = :username")
    int updateIsActiveByUsername(@Param("username") String username, @Param("isActive") Boolean isActive);

    @Modifying
    @Query("DELETE FROM User u WHERE u.username = :username")
    int deleteByUsername(@Param("username") String username);

    @Query("SELECT u.isActive FROM User u WHERE u.username = :username")
    Boolean findIsActiveByUsername(@Param("username") String username);

    @Query("SELECT u.username FROM User u WHERE u.firstName = :firstName AND u.lastName = :lastName ORDER BY u.username")
    List<String> findUsernamesByFirstNameAndLastName(String firstName, String lastName);

    Optional<User> findByUsername(String username);

    default User getUserIfExists(String username) {
        return findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User with username=" + username + " not found"));
    }
}
