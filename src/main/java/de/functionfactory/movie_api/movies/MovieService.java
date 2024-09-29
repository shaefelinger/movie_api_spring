package de.functionfactory.movie_api.movies;

import de.functionfactory.movie_api.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

    public Movie createMovie(Movie movie) {
        return movieDao.createMovie(movie);
    }

    public void deleteMovie(Movie movie) {
         movieDao.deleteMovie(movie);
    }

    public Optional<Movie> updateMovie(String id, MovieUpdateRequestDto movieUpdateRequestDto) {
        Optional<Movie> optionalMovie = movieDao.selectMovieById(id);

        if (optionalMovie.isPresent()) {
            Movie movie = optionalMovie.get();

            // Update only fields that are not null in the DTO
            if (movieUpdateRequestDto.getTitle() != null) {
                movie.setTitle(movieUpdateRequestDto.getTitle());
            }
            if (movieUpdateRequestDto.getOverview() != null) {
                movie.setOverview(movieUpdateRequestDto.getOverview());
            }
            if (movieUpdateRequestDto.getTagline() != null) {
                movie.setTagline(movieUpdateRequestDto.getTagline());
            }
            if (movieUpdateRequestDto.getRuntime() != null) {
                movie.setRuntime(movieUpdateRequestDto.getRuntime());
            }
            if (movieUpdateRequestDto.getRelease_date() != null) {
                movie.setRelease_date(movieUpdateRequestDto.getRelease_date());
            }
            if (movieUpdateRequestDto.getRevenue() != null) {
                movie.setRevenue(movieUpdateRequestDto.getRevenue());
            }
            if (movieUpdateRequestDto.getPoster_path() != null) {
                movie.setPoster_path(movieUpdateRequestDto.getPoster_path());
            }

            return Optional.of(movieDao.createMovie(movie));
        } else {
            return Optional.empty();
        }
    }

//    private boolean hasId(Movie movie, Integer id) {
//        return movie.getId().equals(id);
//    }

    private boolean hasTitle(Movie movie, String title) {
        return movie.getTitle().toLowerCase().contains(title.toLowerCase());
    }
}
