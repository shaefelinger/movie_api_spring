package de.functionfactory.movie_api.movie;

import de.functionfactory.movie_api.movie.dto.MovieSearchRequest;
import de.functionfactory.movie_api.movie.dto.MovieUpdateRequestDto;
import de.functionfactory.movie_api.movie.entity.Movie;
import de.functionfactory.movie_api.tech.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {
    private final MovieDao movieDao;

    public MovieServiceImpl(MovieDao movieDao) {
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
        return movieDao.saveMovie(movie);
    }

    public void deleteMovie(Movie movie) {
         movieDao.deleteMovie(movie);
    }

    public Movie updateMovie(String id, MovieUpdateRequestDto movieUpdateRequestDto) {
        Movie existingMovie = movieDao.selectMovieById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie with id [%s] not found".formatted(id)));

        Movie updatedMovie = Movie.builder()
            .id(existingMovie.getId())
            .title(movieUpdateRequestDto.getTitle() != null ? movieUpdateRequestDto.getTitle() : existingMovie.getTitle())
            .overview(movieUpdateRequestDto.getOverview() != null ? movieUpdateRequestDto.getOverview() : existingMovie.getOverview())
            .tagline(movieUpdateRequestDto.getTagline() != null ? movieUpdateRequestDto.getTagline() : existingMovie.getTagline())
            .runtime(movieUpdateRequestDto.getRuntime() != null ? movieUpdateRequestDto.getRuntime() : existingMovie.getRuntime())
            .release_date(movieUpdateRequestDto.getRelease_date() != null ? movieUpdateRequestDto.getRelease_date() : existingMovie.getRelease_date())
            .revenue(movieUpdateRequestDto.getRevenue() != null ? movieUpdateRequestDto.getRevenue() : existingMovie.getRevenue())
            .poster_path(movieUpdateRequestDto.getPoster_path() != null ? movieUpdateRequestDto.getPoster_path() : existingMovie.getPoster_path())
            .build();

        return movieDao.saveMovie(updatedMovie);
    }
}
