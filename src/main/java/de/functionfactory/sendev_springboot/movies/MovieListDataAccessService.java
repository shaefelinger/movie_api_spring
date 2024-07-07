package de.functionfactory.sendev_springboot.movies;

import java.util.ArrayList;
import java.util.List;

public class MovieListDataAccessService {
    private final MovieRepository movieRepository;

    public MovieListDataAccessService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    private static final List movies;

    static {
        movies = new ArrayList();
        Movie movie1 = new Movie();
        movies.add(movie1);
    }


}
