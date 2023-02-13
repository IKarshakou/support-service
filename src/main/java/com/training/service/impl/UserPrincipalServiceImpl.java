package com.training.service.impl;

import com.training.entity.UserPrincipal;
import com.training.repository.UserPrincipalRepository;
import com.training.service.UserPrincipalService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserPrincipalServiceImpl implements UserPrincipalService {

    private static final String USER_PRINCIPAL_NOT_FOUND_MSG = "UserPrincipal is not found.";

    private final UserPrincipalRepository userPrincipalRepository;

    @Override
    @Transactional
    public void saveRefreshToken(UserPrincipal userPrincipal) {
        userPrincipalRepository.save(userPrincipal);
    }

    @Override
    @Transactional
    public void saveRefreshToken(String refreshToken, UUID userPrincipalId) {
        userPrincipalRepository.saveRefreshToken(refreshToken, userPrincipalId);
    }

    @Override
    @Transactional(readOnly = true)
    public UserPrincipal findByUsername(String username) {
        var userPrincipal = userPrincipalRepository
                .findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(USER_PRINCIPAL_NOT_FOUND_MSG));
        var role = userPrincipal.getUser().getRole();
        return userPrincipal;
    }
}
