package com.example.microblog;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(HttpMethod.GET, "/posts/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/posts").hasAnyRole("USER", "MODERATOR")
                        .requestMatchers(HttpMethod.DELETE, "/posts/**").hasRole("MODERATOR")
                        .requestMatchers("/url").authenticated()
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user1 = User.withDefaultPasswordEncoder()
                .username("user1")
                .password("password")
                .roles("USER")
                .build();
        UserDetails user2 = User.withDefaultPasswordEncoder()
                .username("user2")
                .password("password")
                .roles("USER")
                .build();
        UserDetails user3 = User.withDefaultPasswordEncoder()
                .username("user3")
                .password("password")
                .roles("USER")
                .build();
        UserDetails moderator1 = User.withDefaultPasswordEncoder()
                .username("moderator1")
                .password("password")
                .roles("MODERATOR")
                .build();
        UserDetails moderator2 = User.withDefaultPasswordEncoder()
                .username("moderator2")
                .password("password")
                .roles("MODERATOR", "USER")
                .build();
        return new InMemoryUserDetailsManager(user1, user2, user3, moderator1, moderator2);
    }

}
