package de.functionfactory.movie_api.movie;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class MovieControllerTest {

    @Autowired
    private MovieController movieController;

    @Test
    void getAllBooks() {
        var result = movieController.getMovies();
        assertNotNull(result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(5, result.getBody().size());
    }
}
