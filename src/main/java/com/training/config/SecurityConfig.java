package com.training.config;

import com.training.constants.SecurityConstants;
import com.training.filter.JwtTokenGeneratorFilter;
import com.training.filter.JwtTokenValidatorFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    private static final String LOGIN = "/users/login";
    private static final String LOGOUT = "/users/logout";
    private static final String CREATE_USER = "/users";
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
                    config.setExposedHeaders(List.of(SecurityConstants.JWT_HEADER));
                    config.setMaxAge(HOUR_AGE);
                    return config;
                })

                .and()
                .csrf().disable()

                .addFilterBefore(jwtTokenValidatorFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(jwtTokenGeneratorFilter(), BasicAuthenticationFilter.class)

                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST, LOGIN, LOGOUT, CREATE_USER).permitAll()
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
}
