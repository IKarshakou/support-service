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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;

@Slf4j
public class JwtTokenValidatorFilter extends OncePerRequestFilter {

    private static final String INVALID_TOKEN_MSG = "Invalid Token received.";

    @Value("${spring.jwt.token.secret}")
    private String jwtKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            var jwtToken = request.getHeader(SecurityConstants.JWT_HEADER);

            if (jwtToken != null) {
                var secretKey = Keys.hmacShaKeyFor(jwtKey.getBytes(StandardCharsets.UTF_8));

                var claims = Jwts.parserBuilder()
                        .setSigningKey(secretKey)
                        .build()
                        .parseClaimsJws(jwtToken)
                        .getBody();

                var id = Long.valueOf(claims.get(SecurityConstants.ID_NAME).toString());
                var email = String.valueOf(claims.get(SecurityConstants.EMAIL_NAME));
                var authorities = String.valueOf(claims.get(SecurityConstants.AUTHORITIES_NAME));

                var authoritiesSet
                        = Set.copyOf(AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
                var userPrincipal = new UserPrincipal(id, email, authoritiesSet);

                var authentication = new UsernamePasswordAuthenticationToken(
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
        return request.getServletPath().equals(SecurityConstants.LOGIN_PATH);
    }
}
