package com.training.repository;

import com.training.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    boolean existsUserByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.role = 'MANAGER'")
    List<User> findAllManagers();

    @Query("SELECT u FROM User u WHERE u.role = 'ENGINEER'")
    List<User> findAllEngineers();

    @Modifying
    @Query("DELETE FROM User u WHERE u.id = :id")
    void deleteUserById(UUID id);
}
