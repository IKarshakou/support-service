package com.training.filter;

import com.training.constants.SecurityConstants;
import com.training.security.UserPrincipal;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class JwtTokenGeneratorFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenGeneratorFilter.class);

    private static final String ISSUER = "Some Application";
    private static final String SUBJECT = "JWT Token";
    private static final String AUTHENTICATION_LOG = "User {} is successfully authenticated and has the authorities {}";
    private static final String LOGIN_PATH = "/users/login";
    private static final String AUTHORITIES_DELIMITER = ",";

    @Value("${spring.jwt.token.secret}")
    private String jwtKey;
    @Value("${spring.jwt.token.expired}")
    private Long expirationTime;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            SecretKey secretKey = Keys.hmacShaKeyFor(jwtKey.getBytes(StandardCharsets.UTF_8));
            Date now = new Date();

            String jwtToken = Jwts.builder()
                    .setIssuer(ISSUER)
                    .setSubject(SUBJECT)
                    .claim(SecurityConstants.ID_NAME, userPrincipal.getId())
                    .claim(SecurityConstants.EMAIL_NAME, userPrincipal.getEmail())
                    .claim(SecurityConstants.AUTHORITIES_NAME, populateAuthorities(userPrincipal.getAuthorities()))
                    .setIssuedAt(now)
                    .setExpiration(new Date(now.getTime() + expirationTime))
                    .signWith(secretKey)
                    .compact();

            log.info(AUTHENTICATION_LOG,
                    authentication.getPrincipal().toString(),
                    authentication.getAuthorities().toString());
            response.setHeader(SecurityConstants.JWT_HEADER, jwtToken);
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getServletPath().equals(LOGIN_PATH);
    }

    private String populateAuthorities(Collection<? extends GrantedAuthority> collection) {
        Set<String> authoritiesSet = new HashSet<>();

        for (GrantedAuthority authority : collection) {
            authoritiesSet.add(authority.getAuthority());
        }

        return String.join(AUTHORITIES_DELIMITER, authoritiesSet);
    }
}
