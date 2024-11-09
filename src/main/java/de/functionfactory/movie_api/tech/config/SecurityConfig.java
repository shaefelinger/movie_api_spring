package de.functionfactory.movie_api.tech.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
//                .requiresChannel(channel ->
//                        channel.anyRequest().requiresSecure())
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers
                    .httpStrictTransportSecurity(hsts -> hsts
                        .includeSubDomains(true)
                        .maxAgeInSeconds(31536000)))
                .authorizeHttpRequests(authorize ->
                        authorize.anyRequest().permitAll())
                .requestCache(cache -> cache.disable())
                .securityContext(context -> context.requireExplicitSave(false))
                .build();
    }
}
