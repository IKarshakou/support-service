package com.training.config;

import com.training.constants.SecurityConstants;
import com.training.filter.JwtTokenGeneratorFilter;
import com.training.filter.JwtTokenRefreshFilter;
import com.training.filter.JwtTokenValidatorFilter;
import com.training.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity//(debug = true)
@EnableMethodSecurity//(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    private static final String LOGOUT_PATH = "/logout";
    private static final String REFRESH_TOKEN_PATH = "/refresh";
    private static final String NEW_USER_PATH = "/users";
    private static final String ALLOW_CROSS_ORIGIN_FOR = "http://localhost:[*]";
    private static final String ALL = "*";
    private static final Long HOUR_AGE = 3600L;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .cors().configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOriginPatterns(Collections.singletonList(ALLOW_CROSS_ORIGIN_FOR));
                    config.setAllowedMethods(Collections.singletonList(ALL));
                    config.setAllowCredentials(true);
                    config.setAllowedHeaders(Collections.singletonList(ALL));
                    config.setExposedHeaders(List.of(HttpHeaders.AUTHORIZATION));
                    config.setMaxAge(HOUR_AGE);
                    return config;
                })

                .and()
                .csrf().disable()

                .addFilterBefore(jwtTokenValidatorFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(jwtTokenRefreshFilter(), JwtTokenValidatorFilter.class)
                .addFilterAfter(jwtTokenGeneratorFilter(), BasicAuthenticationFilter.class)

                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST,
                        SecurityConstants.LOGIN_PATH,
                        LOGOUT_PATH,
                        REFRESH_TOKEN_PATH,
                        NEW_USER_PATH).permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic();
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4);
    }

    @Bean
    public JwtTokenValidatorFilter jwtTokenValidatorFilter() {
        return new JwtTokenValidatorFilter();
    }

    @Bean
    public JwtTokenGeneratorFilter jwtTokenGeneratorFilter() {
        return new JwtTokenGeneratorFilter();
    }

    @Bean
    public JwtTokenRefreshFilter jwtTokenRefreshFilter() {
        return new JwtTokenRefreshFilter();
    }
}
