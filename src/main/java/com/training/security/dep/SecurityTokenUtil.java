package com.training.security.dep;

import com.training.constants.SecurityConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

@Component
public class SecurityTokenUtil {

    private static final String SUBJECT_ACCESS_TOKEN = "Access Token";
    private static final String SUBJECT_REFRESH_TOKEN = "Refresh Token";
    private static final String REFRESH_TOKEN_PATH = "/refresh";
    private static final String USER_NOT_FOUND_MSG = "User not found.";
    private static final String BEARER = "Bearer ";
    private static final String AUTHORITIES_DELIMITER = ",";
    private static final String ROLE_PREFIX = "ROLE_";
    private static final String VALIDATION_EXCEPTION_LOG = "Error validation token: {}";
    private static final String AUTHENTICATION_LOG = "User {} is successfully authenticated and has the authorities {}";
    private static final String ERROR_MESSAGE_KEY = "error_message";

    private static final Long REFRESH_TOKEN_EXP_TIME = 1000L * 60 * 60 * 24 * 30;

//    public String generateAccessToken(HttpServletRequest request,
//                                      HttpServletResponse response) {
//        var authentication = SecurityContextHolder.getContext().getAuthentication();
//        var userPrincipal = (UserPrincipal) authentication.getPrincipal();
//        var secretKey = Keys.hmacShaKeyFor(jwtKey.getBytes(StandardCharsets.UTF_8));
//        var nowDate = new Date();
//
//        var accessToken = Jwts.builder()
//                .setIssuer(request.getRequestURL().toString())
//                .setSubject(SUBJECT_ACCESS_TOKEN)
//                .claim(SecurityConstants.ID_NAME, userPrincipal.getId())
//                .claim(SecurityConstants.EMAIL_NAME, userPrincipal.getEmail())
//                .claim(SecurityConstants.AUTHORITIES_NAME, populateAuthorities(userPrincipal.getAuthorities()))
//                .setIssuedAt(nowDate)
//                .setExpiration(new Date(nowDate.getTime() + expirationTime))
//                .signWith(secretKey)
//                .compact();
//        return accessToken;
//    }

//    public String generateRefreshToken(HttpServletRequest request, HttpServletResponse response) {
//        var authentication = SecurityContextHolder.getContext().getAuthentication();
//        var userPrincipal = (UserPrincipal) authentication.getPrincipal();
//        var secretKey = Keys.hmacShaKeyFor(jwtKey.getBytes(StandardCharsets.UTF_8));
//        var nowDate = new Date();
//
//        var refreshToken = Jwts.builder()
//                .setIssuer(request.getRequestURL().toString())
//                .setSubject(SUBJECT_REFRESH_TOKEN)
//                .claim(SecurityConstants.EMAIL_NAME, userPrincipal.getEmail())
//                .setIssuedAt(nowDate)
//                .setExpiration(new Date(nowDate.getTime() + REFRESH_TOKEN_EXP_TIME))
//                .signWith(secretKey)
//                .compact();
//        return refreshToken;
//    }

//    public Authentication getUsernamePasswordAuthenticationToken(HttpServletRequest request, HttpServletResponse response) {
//
//        return new UsernamePasswordAuthenticationToken(userPrincipal, null, authoritiesSet);
//    }

    private String populateAuthorities(Collection<? extends GrantedAuthority> collection) {
        var authoritiesSet = new HashSet<String>();

        for (var authority : collection) {
            authoritiesSet.add(authority.getAuthority());
        }

        return String.join(AUTHORITIES_DELIMITER, authoritiesSet);
    }
}
