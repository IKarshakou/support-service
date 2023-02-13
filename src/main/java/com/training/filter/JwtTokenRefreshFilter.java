package com.training.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.constants.SecurityConstants;
import com.training.entity.UserPrincipal;
import com.training.repository.UserPrincipalRepository;
import com.training.repository.UserRepository;
import com.training.service.UserPrincipalService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

@Slf4j
public class JwtTokenRefreshFilter extends OncePerRequestFilter {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserPrincipalService userPrincipalService;

//    @Autowired
//    private ObjectMapper objectMapper;

    @Autowired
    private UserPrincipalRepository userPrincipalRepository;

    private static final String SUBJECT_ACCESS_TOKEN = "Access Token";
    private static final String SUBJECT_REFRESH_TOKEN = "Refresh Token";
    private static final String REFRESH_TOKEN_PATH = "/refresh";
    private static final String USER_NOT_FOUND_MSG = "User not found.";
    private static final String USER_PRINCIPAL_NOT_FOUND_MSG = "UserPrincipal not found.";
    private static final String BEARER = "Bearer ";
    private static final String AUTHORITIES_DELIMITER = ",";
    private static final String ROLE_PREFIX = "ROLE_";
    private static final String VALIDATION_EXCEPTION_LOG = "Error validation token: {}";
    private static final String ERROR_MESSAGE_KEY = "error_message";

    @Value("${spring.jwt.token.secret}")
    private String jwtKey;
    @Value("${spring.jwt.token.expired}")
    private Long expirationTime;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            if (authentication != null) {

                var userPrincipal = (UserPrincipal) authentication.getPrincipal();
                var actualUserPrincipal = userPrincipalService.findByUsername(userPrincipal.getUsername());

                var refreshToken = actualUserPrincipal.getRefreshToken();
                var secretKey = Keys.hmacShaKeyFor(jwtKey.getBytes(StandardCharsets.UTF_8));

                var claims = Jwts.parserBuilder()
                        .setSigningKey(secretKey)
                        .build()
                        .parseClaimsJws(refreshToken)
                        .getBody();

                if (actualUserPrincipal.isEnabled()) {
                    var email = actualUserPrincipal.getUsername();
//                    var email = String.valueOf(claims.get(SecurityConstants.EMAIL_NAME));

//                    var user = userRepository
//                            .findByEmail(email)
//                            .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MSG));
                    var user = actualUserPrincipal.getUser();

                    var authorities = new HashSet<GrantedAuthority>();
                    authorities.add(new SimpleGrantedAuthority(ROLE_PREFIX + user.getRole().getName().name()));
                    user
                            .getRole()
                            .getPermissions()
                            .forEach(permission -> {
                                authorities.add(new SimpleGrantedAuthority(permission.getName().name()));
                            });

                    var nowDate = new Date();
                    var accessToken = Jwts.builder()
                            .setIssuer(request.getRequestURL().toString())
                            .setSubject(SUBJECT_ACCESS_TOKEN)
                            .claim(SecurityConstants.ID_NAME, user.getId())
                            .claim(SecurityConstants.EMAIL_NAME, user.getEmail())
                            .claim(SecurityConstants.AUTHORITIES_NAME, populateAuthorities(authorities))
                            .setIssuedAt(nowDate)
                            .setExpiration(new Date(nowDate.getTime() + expirationTime))
                            .signWith(secretKey)
                            .compact();

                    var tokensMap = new HashMap<>();
                    tokensMap.put(SUBJECT_ACCESS_TOKEN, accessToken);
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), tokensMap);
//                    objectMapper.writeValue(response.getOutputStream(), tokensMap);
                }
            }
        } catch (Exception ex) {
            log.error(VALIDATION_EXCEPTION_LOG, ex.getMessage());
            response.setStatus(HttpStatus.FORBIDDEN.value());
            var errorMap = new HashMap<>();
            errorMap.put(ERROR_MESSAGE_KEY, ex.getMessage());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), errorMap);
//            objectMapper.writeValue(response.getOutputStream(), errorMap);
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getServletPath().equals(REFRESH_TOKEN_PATH);
    }

    private String populateAuthorities(Collection<? extends GrantedAuthority> collection) {
        var authoritiesSet = new HashSet<String>();

        for (var authority : collection) {
            authoritiesSet.add(authority.getAuthority());
        }

        return String.join(AUTHORITIES_DELIMITER, authoritiesSet);
    }
}
