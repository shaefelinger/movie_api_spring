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
               .portMapper(portMapper -> portMapper
                       .http(8080).mapsTo(8443))
               .requiresChannel(channel ->
                       channel.anyRequest().requiresSecure())
               .csrf(csrf -> csrf.disable())
               .headers(headers -> headers
                   .httpStrictTransportSecurity(hsts -> hsts
                       .includeSubDomains(true)
                       .maxAgeInSeconds(31536000)))
               .authorizeHttpRequests(authorize ->
                       authorize.anyRequest().permitAll())
               .build();
   }

}
