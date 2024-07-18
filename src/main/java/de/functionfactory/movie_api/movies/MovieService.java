package de.functionfactory.movie_api.movies;

import de.functionfactory.movie_api.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MovieService {
    private final MovieDao movieDao;

    public MovieService(@Qualifier("jpa") MovieDao movieDao) {
        this.movieDao = movieDao;
    }

    public List<Movie> getMovies() {
        return movieDao.selectAllMovies();
    }

    public Movie getMovieById(String id) {
        return movieDao.selectMovieById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie with id [%s] not found".formatted(id)));
    }
    public List<Movie> getMoviesByTitle(String title) {
        return movieDao.selectMovieByTitle(title);
//                .orElseThrow(() -> new ResourceNotFoundException("Movie with title [%s] not found".formatted(title)));

    }

    public List<Movie> searchMovies(MovieSearchRequest searchRequest) {
        return movieDao.selectAllMovies().stream()
                .filter(m -> m.getTitle().toLowerCase().contains(searchRequest.getTitle().toLowerCase())
                        || m.getOverview().equalsIgnoreCase(searchRequest.getOverview()))
                .toList();
    }

//    private boolean hasId(Movie movie, Integer id) {
//        return movie.getId().equals(id);
//    }

    private boolean hasTitle(Movie movie, String title) {
        return movie.getTitle().toLowerCase().contains(title.toLowerCase());
    }

}
