package com.training.filter;

import com.training.constants.SecurityConstants;
import com.training.security.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;

public class JwtTokenValidatorFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenValidatorFilter.class);

    private static final String INVALID_TOKEN_MSG = "Invalid Token received.";
    private static final String LOGIN_PATH = "/users/login";

    @Value("${spring.jwt.token.secret}")
    private String jwtKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            String jwtToken = request.getHeader(SecurityConstants.JWT_HEADER);

            if (jwtToken != null) {
                SecretKey secretKey = Keys.hmacShaKeyFor(jwtKey.getBytes(StandardCharsets.UTF_8));

                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(secretKey)
                        .build()
                        .parseClaimsJws(jwtToken)
                        .getBody();

                Long id = Long.valueOf(claims.get(SecurityConstants.ID_NAME).toString());
                String email = String.valueOf(claims.get(SecurityConstants.EMAIL_NAME));
                String authorities = String.valueOf(claims.get(SecurityConstants.AUTHORITIES_NAME));

                Set<GrantedAuthority> authoritiesSet
                        = Set.copyOf(AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
                UserPrincipal userPrincipal = new UserPrincipal(id, email, authoritiesSet);

                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        userPrincipal, null, authoritiesSet);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            throw new BadCredentialsException(INVALID_TOKEN_MSG);
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().equals(LOGIN_PATH);
    }
}
