package de.functionfactory.movie_api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MovieApiApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(MovieApiApplication.class);

    public static void main(String[] args) {
        LOGGER.info("😎Starting Movie API");
        SpringApplication.run(MovieApiApplication.class, args);
    }

}
