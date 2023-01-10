package com.training.security;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public class UserPrincipal implements Serializable {

    @Serial
    private static final long serialVersionUID = -6690946457172875352L;

    private final UUID id;
    private final String email;
    private final Set<GrantedAuthority> authorities;

    @Override
    public String toString() {
        return email;
    }
}
