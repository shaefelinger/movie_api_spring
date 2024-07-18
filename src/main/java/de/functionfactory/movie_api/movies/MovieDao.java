package de.functionfactory.movie_api.movies;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MovieDao {
    List<Movie> selectAllMovies();
    Optional<Movie> selectMovieById(String id);
    List<Movie> selectMovieByTitle(String title);
}
