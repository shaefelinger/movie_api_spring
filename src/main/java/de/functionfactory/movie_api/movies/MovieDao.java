package de.functionfactory.movie_api.movies;

import java.util.List;

public interface MovieDao {
    List<Movie> selectAllMovies();
}
