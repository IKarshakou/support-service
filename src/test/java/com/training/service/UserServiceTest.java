package com.training.service;

import com.training.dto.user.InputUserDto;
import com.training.entity.User;
import com.training.entity.enums.Role;
import com.training.mapper.UserMapper;
import com.training.repository.UserRepository;
import com.training.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private UserService userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;

    private static final String EMAIL = "Maria_Some_Test@gmail.com";
    private static final String PASSWORD = "1Oe)weE";

    private final InputUserDto inputUserDto = InputUserDto.builder().email(EMAIL).password(PASSWORD).build();
    private final User user = User.builder().email(EMAIL).role(Role.EMPLOYEE).password(PASSWORD).build();

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, userMapper, passwordEncoder);
    }

    @Test
    void getUserById() {
    }

    @Test
    void getUserByEmail() {
    }

    @Test
    void testGetAllUsers() {
        // when
        userService.getAllUsers();

        // then
        verify(userRepository).findAll();
    }

    @Test
    void testAddUser() {
        // when
        when(userMapper.convertToEntity(inputUserDto)).thenReturn(user);
        when(userRepository.isUserExistsByEmail(EMAIL)).thenReturn(false);

        userService.addUser(inputUserDto);

        // then
        verify(passwordEncoder).encode(PASSWORD);
        verify(userRepository).save(user);
    }

    @Test
    void updateUser() {
    }

    @Test
    void removeUser() {
    }
}
