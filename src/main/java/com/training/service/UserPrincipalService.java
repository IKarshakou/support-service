package com.training.service;

import com.training.entity.UserPrincipal;

import java.util.UUID;

public interface UserPrincipalService {
    void saveRefreshToken(UserPrincipal userPrincipal);
    void saveRefreshToken(String refreshToken, UUID userPrincipalId);
    UserPrincipal findByUsername(String username);
}
