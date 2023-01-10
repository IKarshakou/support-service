package com.training.service;

import com.training.dto.user.InputUserDto;
import com.training.dto.user.OutputUserDto;
import com.training.dto.user.UpdatedUserDto;

import java.util.List;
import java.util.UUID;

public interface UserService {
    OutputUserDto getUserById(UUID id);

    OutputUserDto getUserByEmail(String email);

    List<OutputUserDto> getAllUsers();

    void addUser(InputUserDto inputUserDto);

    void updateUser(UpdatedUserDto updatedUserDto);

    void removeUser(UUID id);
}
