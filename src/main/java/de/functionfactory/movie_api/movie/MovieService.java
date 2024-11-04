package de.functionfactory.movie_api.movie;

import de.functionfactory.movie_api.movie.dto.MovieSearchRequest;
import de.functionfactory.movie_api.movie.dto.MovieUpdateRequestDto;
import de.functionfactory.movie_api.movie.entity.Movie;

import java.util.List;

public interface MovieService {
    List<Movie> getMovies();
    Movie getMovieById(String id);
    MoviePageResponse getMoviesByTitle(String title, int page, int limit);
    List<Movie> searchMovies(MovieSearchRequest searchRequest);
    Movie createMovie(Movie movie);
    void deleteMovie(Movie movie);
    Movie updateMovie(String id, MovieUpdateRequestDto movieUpdateRequestDto);
}
