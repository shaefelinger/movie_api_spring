package de.functionfactory.sendev_springboot.movies;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {
    private final MovieDoa movieDoa;

    public MovieService(MovieDoa movieDoa) {
        this.movieDoa = movieDoa;
    }

    public List<Movie> getMovies() {
        return movieDoa.selectAllMovies();
    }
}
