package com.training.repository;

import com.training.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(String email);

    @Query("SELECT CASE WHEN COUNT(u) > 0 "
            + "THEN TRUE "
            + "ELSE FALSE END "
            + "FROM User u WHERE u.email = :email")
    boolean isUserExistsByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.role = 'MANAGER'")
    List<User> findAllManagers();

    @Query("SELECT u FROM User u WHERE u.role = 'ENGINEER'")
    List<User> findAllEngineers();
}
