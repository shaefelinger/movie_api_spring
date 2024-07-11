package de.functionfactory.movie_api.movies;

import de.functionfactory.movie_api.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {
    private final MovieDao movieDao;

    public MovieService(@Qualifier("jpa") MovieDao movieDao) {
        this.movieDao = movieDao;
    }

    public List<Movie> getMovies() {
        return movieDao.selectAllMovies();
    }

    public Movie getMovieById(Integer id) {
        var movie = movieDao.selectAllMovies().stream()
                .filter(m -> hasId(m, id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Movie with Id " + id + " not found"));
        return movie;
    }
    public Movie getMovieByTitle(String title) {
        var movie = movieDao.selectAllMovies().stream()
                .filter(m -> hasTitle(m, title))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Movie with Title " + title + " not found"));
        return movie;
    }

    public List<Movie> searchMovies(MovieSearchRequest searchRequest) {
        return movieDao.selectAllMovies().stream()
                .filter(m -> m.getTitle().toLowerCase().contains(searchRequest.getTitle().toLowerCase())
                        || m.getOverview().equalsIgnoreCase(searchRequest.getOverview()))
                .toList();
    }

    private boolean hasId(Movie movie, Integer id) {
        return movie.getId().equals(id);
    }

    private boolean hasTitle(Movie movie, String title) {
        return movie.getTitle().toLowerCase().contains(title.toLowerCase());
    }

}
