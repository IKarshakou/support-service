package com.training.security;

import com.training.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserPasswordAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String BAD_CREDENTIALS_MSG = "Invalid password.";
    private static final String FIELDS_ARE_NOT_FILLED_MSG = "Please, fill out the required field.";
    private static final String USER_NOT_FOUND_MSG = "User %s not found.";
    private static final String ROLE_PREFIX = "ROLE_";
    private static final String LOGIN_TO_SYSTEM_LOG = "User [{}] attempts to login to system.";
    private static final String INCORRECT_PASSWORD_LOG = "User [{}] entered incorrect password.";
    private static final String USER_FOUND = "User [{}] found.";

    @Override
    @Transactional(readOnly = true)
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var email = authentication.getName();
        var password = authentication.getCredentials().toString();

        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            throw new BadCredentialsException(FIELDS_ARE_NOT_FILLED_MSG);
        }

        log.info(LOGIN_TO_SYSTEM_LOG, email);
        var user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND_MSG.formatted(email)));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            log.info(INCORRECT_PASSWORD_LOG, email);
            throw new BadCredentialsException(BAD_CREDENTIALS_MSG);
        }

        log.debug(USER_FOUND, email);
        var authorities = new HashSet<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(ROLE_PREFIX + user.getRole().name()));

        var userPrincipal = new UserPrincipal(user.getId(), user.getEmail(), authorities);

        return new UsernamePasswordAuthenticationToken(userPrincipal, null, authorities);
    }

    @Override
    public boolean supports(Class<?> authenticationType) {
        return authenticationType.equals(UsernamePasswordAuthenticationToken.class);
    }
}
