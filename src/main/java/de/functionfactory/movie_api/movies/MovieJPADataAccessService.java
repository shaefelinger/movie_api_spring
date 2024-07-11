package de.functionfactory.movie_api.movies;

import org.springframework.stereotype.Repository;

import java.util.List;

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

}
