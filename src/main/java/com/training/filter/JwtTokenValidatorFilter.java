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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

@Slf4j
public class JwtTokenValidatorFilter extends OncePerRequestFilter {

    private static final String BEARER = "Bearer ";
    private static final String VALIDATION_EXCEPTION_LOG = "Error validation token: {}";
    private static final String ERROR_MESSAGE_KEY = "error_message";

    @Value("${spring.jwt.token.secret}")
    private String jwtKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        var authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        try {
            if (authorizationHeader != null && authorizationHeader.startsWith(BEARER)) {

                var accessToken = authorizationHeader.substring(BEARER.length());
                var secretKey = Keys.hmacShaKeyFor(jwtKey.getBytes(StandardCharsets.UTF_8));

                var claims = Jwts.parserBuilder()
                        .setSigningKey(secretKey)
                        .build()
                        .parseClaimsJws(accessToken)
                        .getBody();

                var id = UUID.fromString(claims.get(SecurityConstants.ID_NAME).toString());
                var email = String.valueOf(claims.get(SecurityConstants.EMAIL_NAME));
                var authorities = String.valueOf(claims.get(SecurityConstants.AUTHORITIES_NAME));

                var authoritiesSet
                        = Set.copyOf(AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
                var userPrincipal = new UserPrincipal(id, email, authoritiesSet);

                var authentication = new UsernamePasswordAuthenticationToken(
                        userPrincipal, null, authoritiesSet);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            log.error(VALIDATION_EXCEPTION_LOG, ex.getMessage());
            response.setStatus(HttpStatus.FORBIDDEN.value());
            var errorMap = new HashMap<>();
            errorMap.put(ERROR_MESSAGE_KEY, ex.getMessage());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), errorMap);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().equals(SecurityConstants.LOGIN_PATH);
    }
}
