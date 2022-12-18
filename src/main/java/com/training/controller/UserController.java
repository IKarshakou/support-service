package com.training.controller;

import com.training.dto.user.InputUserDto;
import com.training.dto.user.OutputUserDto;
import com.training.dto.user.UpdatedUserDto;
import com.training.service.ErrorsHandlerService;
import com.training.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private static final String REDIRECT_TO_LOGIN_PAGE = "/login";

    private final UserService userService;
    private final ErrorsHandlerService errorsHandlerService;

    @PostMapping("/login")
    public ResponseEntity<OutputUserDto> login(Principal principal) {
        return ResponseEntity.ok(userService.getUserByEmail(principal.getName()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity
                .status(HttpStatus.SEE_OTHER)
                .location(URI.create(REDIRECT_TO_LOGIN_PAGE))
                .build();
    }

    @GetMapping
    public ResponseEntity<List<OutputUserDto>> getUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OutputUserDto> getUserById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<String> createUser(@Validated @RequestBody InputUserDto inputUserDto, Errors errors) {
        errorsHandlerService.checkErrors(errors);

        userService.addUser(inputUserDto);
        URI location = URI.create(REDIRECT_TO_LOGIN_PAGE);
        return ResponseEntity.created(location).build();
    }

    @PatchMapping
    public ResponseEntity<Void> updateUser(@Validated @RequestBody UpdatedUserDto updatedUserDto, Errors errors) {
        errorsHandlerService.checkErrors(errors);

        userService.updateUser(updatedUserDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        userService.removeUser(id);
        return ResponseEntity.noContent().build();
    }
}
