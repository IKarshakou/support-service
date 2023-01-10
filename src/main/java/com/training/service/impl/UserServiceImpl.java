package com.training.service.impl;

import com.training.dto.user.InputUserDto;
import com.training.dto.user.OutputUserDto;
import com.training.dto.user.UpdatedUserDto;
import com.training.mapper.UserMapper;
import com.training.repository.UserRepository;
import com.training.security.UserPrincipal;
import com.training.service.UserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String USER_NOT_FOUND_MSG = "User not found.";
    private static final String USER_IS_ALREADY_EXISTS_MSG = "User with email [%s] already exists.";

    private static final String GET_USER_BY_ID_LOG = "Getting User by ID = [{}]";
    private static final String GET_USER_BY_EMAIL_LOG = "Getting User by email = [{}]";
    private static final String GET_ALL_USERS_LOG = "Getting all Users. Result is empty = [{}]";
    private static final String ADD_USER_LOG = "Adding User to database: [{}].";
    private static final String UPDATE_USER_LOG = "Updating User with ID = [{}].";
    private static final String DELETE_USER_LOG = "Removing User with ID = [{}]";
    private static final String DELETE_USER_SUCCESS_LOG = "User successfully removed.";

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public OutputUserDto getUserById(UUID id) {
//        log.info(GET_USER_BY_ID_LOG, id);

        return userMapper.convertToDto(userRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MSG)));
    }

    @Override
    @Transactional(readOnly = true)
    public OutputUserDto getUserByEmail(String email) {
//        log.info(GET_USER_BY_EMAIL_LOG, email);

        return userMapper.convertToDto(userRepository
                .findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MSG)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OutputUserDto> getAllUsers() {
        var userList = userRepository.findAll();
//        log.info(GET_ALL_USERS_LOG, userList.isEmpty());

        return userList
                .stream()
                .map(userMapper::convertToDto)
                .toList();
    }

    @Override
    @Transactional
    public void addUser(InputUserDto inputUserDto) {
        var user = userMapper.convertToEntity(inputUserDto);
//        log.info(ADD_USER_LOG, user);

        if (userRepository.existsUserByEmail(user.getEmail())) {
            throw new EntityExistsException(USER_IS_ALREADY_EXISTS_MSG.formatted(user.getEmail()));
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateUser(UpdatedUserDto updatedUserDto) {
        var userPrincipal = (UserPrincipal) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
//        log.info(UPDATE_USER_LOG, userPrincipal.getId());

        var user = userRepository
                .findById(userPrincipal.getId())
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MSG));
        if (updatedUserDto.getFirstName() != null) {
            user.setFirstName(updatedUserDto.getFirstName());
        }
        if (updatedUserDto.getLastName() != null) {
            user.setLastName(updatedUserDto.getLastName());
        }
        if (updatedUserDto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(updatedUserDto.getPassword()));
        }
    }

    @Override
    @Transactional
    public void removeUser(UUID id) {
//        log.info(DELETE_USER_LOG, id);

        userRepository.deleteUserById(id);

//        log.info(DELETE_USER_SUCCESS_LOG);
    }
}
