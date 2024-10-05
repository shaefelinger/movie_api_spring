package de.functionfactory.movie_api.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry
                .addMapping("/**")
                .allowedOrigins(
                        "myfrontend.com",
                        "http://127.0.0.1:63342"
                )
                .allowedMethods("GET","HEAD","PUT","PATCH","POST");
//                .allowedMethods(HttpMethod.GET.name());
    }
}
