package de.functionfactory.movie_api.movie;

import de.functionfactory.movie_api.movie.entity.Movie;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jpa")
public class MovieJPADataAccessService implements MovieDao {
    private final MovieRepository movieRepository;

    public MovieJPADataAccessService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public List<Movie> selectAllMovies() {
        return movieRepository.findAll();
    }

    @Override
    public Optional<Movie> selectMovieById(String id) {
        return movieRepository.findById(id);
    }

    @Override
    public List<Movie> selectMovieByTitle(String title) {
        return movieRepository.findByTitleContainingIgnoreCase(title);
    }

    @Override
    public Movie saveMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    @Override
    public void deleteMovie(Movie movie) {
        movieRepository.delete(movie);
    }

}
