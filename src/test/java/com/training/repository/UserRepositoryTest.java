package com.training.repository;

import com.training.entity.User;
import com.training.entity.enums.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private static final String EMAIL = "Maria_Some_Test@gmail.com";
    private static final String PASSWORD = "1Oe)weE";

    private final User user = User.builder().email(EMAIL).role(Role.EMPLOYEE).password(PASSWORD).build();

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("should return user by his email from database.")
    void testFindByEmailUserExists() {
        // given
        userRepository.save(user);

        // when
        Optional<User> result = userRepository.findByEmail(EMAIL);

        // then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    @DisplayName("should return empty optional by user email from database.")
    void testFindByEmailUserDoesNotExists() {
        // when
        Optional<User> result = userRepository.findByEmail(EMAIL);

        // then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("should return true if user with the same email already exists.")
    void testIsUserExistsByEmailIsTrue() {
        // given
        userRepository.save(user);

        // when
        boolean result = userRepository.isUserExistsByEmail(EMAIL);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("should return false if user with the same email does not exists.")
    void testIsUserExistsByEmailIsFalse() {
        // when
        boolean result = userRepository.isUserExistsByEmail(EMAIL);

        // then
        assertThat(result).isFalse();
    }
}
