package de.functionfactory.sendev_springboot.movies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("jpa")
public class MovieJPADataAccessService implements MovieDoa {
    private final MovieRepository movieRepository;

    @Autowired
    public MovieJPADataAccessService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public List<Movie> selectAllMovies() {
        return movieRepository.findAll();
    }
}
