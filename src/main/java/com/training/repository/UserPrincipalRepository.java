package com.training.repository;

import com.training.entity.UserPrincipal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserPrincipalRepository extends JpaRepository<UserPrincipal, UUID> {

    Optional<UserPrincipal> findByUsername(String username);

    @Modifying
    @Query(value = "UPDATE users_principal SET refresh_token = ?1 WHERE id = ?2", nativeQuery = true)
    void saveRefreshToken(String refreshToken, UUID userPrincipalId);

    @Modifying
    @Query(value = "DELETE FROM users_principal WHERE user_id = ?1", nativeQuery = true)
    void deleteByUserId(UUID userId);
}
