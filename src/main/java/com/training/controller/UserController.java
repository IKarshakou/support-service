package com.training.controller;

import com.training.constants.SecurityConstants;
import com.training.dto.user.InputUserDto;
import com.training.dto.user.OutputUserDto;
import com.training.dto.user.UpdatedUserDto;
import com.training.validator.ErrorsChecker;
import com.training.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ErrorsChecker errorsChecker;

    @GetMapping
    public ResponseEntity<List<OutputUserDto>> getUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OutputUserDto> getUserById(@PathVariable("id") UUID userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PostMapping
    public ResponseEntity<String> createUser(@Validated @RequestBody InputUserDto inputUserDto, Errors errors) {
        errorsChecker.checkErrors(errors);

        userService.addUser(inputUserDto);
        var location = URI.create(SecurityConstants.LOGIN_PATH);
        return ResponseEntity.created(location).build();
    }

    @PatchMapping
    public ResponseEntity<Void> updateUser(@Validated @RequestBody UpdatedUserDto updatedUserDto, Errors errors) {
        errorsChecker.checkErrors(errors);

        userService.updateUser(updatedUserDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") UUID userId) {
        userService.removeUser(userId);
        return ResponseEntity.noContent().build();
    }
}
