package com.training.service.impl;

import com.training.dto.user.InputUserDto;
import com.training.dto.user.OutputUserDto;
import com.training.dto.user.UpdatedUserDto;
import com.training.entity.User;
import com.training.mapper.UserMapper;
import com.training.repository.UserRepository;
import com.training.service.UserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private static final String USER_NOT_FOUND_MSG = "User not found.";
    private static final String USER_IS_ALREADY_EXISTS_MSG = "User with email [%s] already exists.";

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public OutputUserDto getUserById(Long id) {
        log.info("Getting User by ID = [{}]", id);
        return userMapper.convertToDto(userRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MSG)));
    }

    @Override
    @Transactional(readOnly = true)
    public OutputUserDto getUserByEmail(String email) {
        log.info("Getting User by email = [{}]", email);

        return userMapper.convertToDto(userRepository
                .findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MSG)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OutputUserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        log.info("Getting all Users. Result is empty = [{}]", users.isEmpty());

        return users
                .stream()
                .map(userMapper::convertToDto)
                .toList();
    }

    @Override
    @Transactional
    public OutputUserDto addUser(InputUserDto inputUserDto) {
        User user = userMapper.convertToEntity(inputUserDto);
        log.info("Adding User to database: [{}].", user);

        if (userRepository.isUserExistsByEmail(user.getEmail())) {
            throw new EntityExistsException(USER_IS_ALREADY_EXISTS_MSG.formatted(user.getEmail()));
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userMapper.convertToDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public void updateUser(UpdatedUserDto updatedUserDto) {
        User user = userMapper.convertUpdatedToEntity(updatedUserDto);
        log.info("Updating User with ID = [{}].", user.getId());

        if (userRepository.existsById(user.getId())) {
            userRepository.save(user);
        } else {
            log.info("User with ID = [{}] not found.", user.getId());
            throw new EntityNotFoundException(USER_NOT_FOUND_MSG);
        }
    }

    @Override
    @Transactional
    public void removeUser(Long id) {
        log.info("Removing User with ID = [{}]", id);

        userRepository.delete(userRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MSG)));

        log.info("User successfully removed.");
    }
}
