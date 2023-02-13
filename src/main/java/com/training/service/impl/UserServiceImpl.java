package com.training.service.impl;

import com.training.dto.user.InputUserDto;
import com.training.dto.user.OutputUserDto;
import com.training.dto.user.UpdatedUserDto;
import com.training.entity.Role;
import com.training.mapper.UserMapper;
import com.training.repository.RoleRepository;
import com.training.repository.UserPrincipalRepository;
import com.training.repository.UserRepository;
import com.training.entity.UserPrincipal;
import com.training.service.UserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private static final String ROLE_NOT_FOUND_MSG = "Role not found.";
    private static final String USER_IS_ALREADY_EXISTS_MSG = "User with email [%s] already exists.";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserPrincipalRepository userPrincipalRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public OutputUserDto getUserById(UUID id) {
        return userMapper.convertToDto(userRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MSG)));
    }

    @Override
    @Transactional(readOnly = true)
    public OutputUserDto getUserByEmail(String email) {

        return userMapper.convertToDto(userRepository
                .findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MSG)));
    }

    @Override
//    @PreAuthorize("hasPermission(#categoryDto, 'CREATE_TICKET')")
    @Transactional(readOnly = true)
    public List<OutputUserDto> getAllUsers() {
        var userList = userRepository.findAllUsers();

        return userList
                .stream()
                .map(userMapper::convertToDto)
                .toList();
    }

    @Override
    @Transactional
    public void addUser(InputUserDto inputUserDto) {
        var user = userMapper.convertToEntity(inputUserDto);

        if (userRepository.existsUserByEmail(user.getEmail())) {
            throw new EntityExistsException(USER_IS_ALREADY_EXISTS_MSG.formatted(user.getEmail()));
        }
        Role role = roleRepository
                .findByName(user.getRole().getName())
                .orElseThrow(() -> new EntityNotFoundException(ROLE_NOT_FOUND_MSG));

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(role);
        userRepository.save(user);

        var userPrincipal = UserPrincipal.builder()
                .enabled(true)
                .username(user.getEmail())
                .user(user)
                .build();
        userPrincipalRepository.save(userPrincipal);
    }

    @Override
    @Transactional
    public void updateUser(UpdatedUserDto updatedUserDto) {
        var userPrincipal = (UserPrincipal) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        var user = userRepository
                .findById(userPrincipal.getUser().getId())
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
    @PreAuthorize("denyAll()")
    @Transactional
    public void removeUser(UUID id) {
        userPrincipalRepository.deleteByUserId(id);
        userRepository.deleteUserById(id);
    }
}
