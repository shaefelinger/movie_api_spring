package de.functionfactory.movie_api.movies;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class MovieControllerTest {

    @Autowired
    private MovieController movieController;

    @Test
    void getAllBooks() {
        assertNotNull(movieController.getMovies().getBody());
        assertEquals(2, movieController.getMovies().getBody().size());
    }
}
