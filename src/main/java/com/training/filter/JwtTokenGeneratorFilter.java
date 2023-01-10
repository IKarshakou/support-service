package com.training.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.constants.SecurityConstants;
import com.training.security.UserPrincipal;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

@Slf4j
public class JwtTokenGeneratorFilter extends OncePerRequestFilter {

    private static final String SUBJECT_ACCESS_TOKEN = "Access Token";
    private static final String SUBJECT_REFRESH_TOKEN = "Refresh Token";
    private static final String AUTHENTICATION_LOG = "User {} is successfully authenticated and has the authorities {}";
    private static final String AUTHORITIES_DELIMITER = ",";

    private static final Long REFRESH_TOKEN_EXP_TIME = 1000L * 60 * 60 * 24 * 30;

    @Value("${spring.jwt.token.secret}")
    private String jwtKey;
    @Value("${spring.jwt.token.expired}")
    private Long expirationTime;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            var userPrincipal = (UserPrincipal) authentication.getPrincipal();
            var secretKey = Keys.hmacShaKeyFor(jwtKey.getBytes(StandardCharsets.UTF_8));
            var nowDate = new Date();

            var accessToken = Jwts.builder()
                    .setIssuer(request.getRequestURL().toString())
                    .setSubject(SUBJECT_ACCESS_TOKEN)
                    .claim(SecurityConstants.ID_NAME, userPrincipal.getId())
                    .claim(SecurityConstants.EMAIL_NAME, userPrincipal.getEmail())
                    .claim(SecurityConstants.AUTHORITIES_NAME, populateAuthorities(userPrincipal.getAuthorities()))
                    .setIssuedAt(nowDate)
                    .setExpiration(new Date(nowDate.getTime() + expirationTime))
                    .signWith(secretKey)
                    .compact();
            var refreshToken = Jwts.builder()
                    .setIssuer(request.getRequestURL().toString())
                    .setSubject(SUBJECT_REFRESH_TOKEN)
                    .claim(SecurityConstants.EMAIL_NAME, userPrincipal.getEmail())
                    .setIssuedAt(nowDate)
                    .setExpiration(new Date(nowDate.getTime() + REFRESH_TOKEN_EXP_TIME))
                    .signWith(secretKey)
                    .compact();

            log.info(AUTHENTICATION_LOG,
                    authentication.getPrincipal().toString(),
                    authentication.getAuthorities().toString());

            var tokensMap = new HashMap<>();
            tokensMap.put(SUBJECT_ACCESS_TOKEN, accessToken);
            tokensMap.put(SUBJECT_REFRESH_TOKEN, refreshToken);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), tokensMap);
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getServletPath().equals(SecurityConstants.LOGIN_PATH);
    }

    private String populateAuthorities(Collection<? extends GrantedAuthority> collection) {
        var authoritiesSet = new HashSet<String>();

        for (var authority : collection) {
            authoritiesSet.add(authority.getAuthority());
        }

        return String.join(AUTHORITIES_DELIMITER, authoritiesSet);
    }
}
