package com.training.filter;

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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

@Slf4j
public class JwtTokenGeneratorFilter extends OncePerRequestFilter {

    private static final String ISSUER = "Some Application";
    private static final String SUBJECT = "JWT Token";
    private static final String AUTHENTICATION_LOG = "User {} is successfully authenticated and has the authorities {}";
    private static final String AUTHORITIES_DELIMITER = ",";

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

            var jwtToken = Jwts.builder()
                    .setIssuer(ISSUER)
                    .setSubject(SUBJECT)
                    .claim(SecurityConstants.ID_NAME, userPrincipal.getId())
                    .claim(SecurityConstants.EMAIL_NAME, userPrincipal.getEmail())
                    .claim(SecurityConstants.AUTHORITIES_NAME, populateAuthorities(userPrincipal.getAuthorities()))
                    .setIssuedAt(nowDate)
                    .setExpiration(new Date(nowDate.getTime() + expirationTime))
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
