package de.functionfactory.movie_api.movie;

import de.functionfactory.movie_api.movie.entity.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieDao {
    List<Movie> selectAllMovies();
    Optional<Movie> selectMovieById(String id);
    List<Movie> selectMovieByTitle(String title);

    Movie saveMovie(Movie movie);

    void deleteMovie(Movie movie);
}
