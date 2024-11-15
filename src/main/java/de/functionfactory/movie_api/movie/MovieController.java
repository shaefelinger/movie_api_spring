package de.functionfactory.movie_api.movie;

import de.functionfactory.movie_api.movie.dto.MovieSearchRequest;
import de.functionfactory.movie_api.movie.dto.MovieUpdateRequestDto;
import de.functionfactory.movie_api.movie.entity.Movie;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/movies")
@Validated

public class MovieController {

    private final MovieService movieService;

    @Autowired
    public MovieController(MovieServiceImpl movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/{id}")
    ResponseEntity<Movie> getMovieById(@PathVariable @Valid UUID id) {
        Movie movie = movieService.getMovieById(id.toString());
        return ResponseEntity.status(HttpStatus.OK).body(movie);
    }

    @GetMapping()
    ResponseEntity<MoviePageResponse> getMovieByTitle(
            @RequestParam(defaultValue = "")
            String title,
            @RequestParam(defaultValue = "0")
            int page,
            @RequestParam(defaultValue = "10")
            int limit) {
        var movie = movieService.getMoviesByTitle(title, page, limit);
        return ResponseEntity.status(HttpStatus.OK).body(movie);
    }

    @PostMapping("/search")
    ResponseEntity<List<Movie>> searchMovies(@RequestBody MovieSearchRequest searchRequest) {
        var searchResult = movieService.searchMovies(searchRequest);
        return ResponseEntity.status(HttpStatus.OK).body(searchResult);
    }

    @PostMapping
    ResponseEntity<Movie> createMovie(@RequestBody @Valid Movie movie) {
        Movie createdMovie = movieService.createMovie(movie);
        return ResponseEntity.status(HttpStatus.OK).body(createdMovie);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteMovie(@PathVariable @Valid UUID id) {
        Movie movie = movieService.getMovieById(id.toString());

        movieService.deleteMovie(movie);
        return ResponseEntity.ok("OK");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(
            @PathVariable @Valid UUID id,
            @RequestBody @Valid MovieUpdateRequestDto movieUpdateRequestDto) {

        Movie updatedMovie = movieService.updateMovie(id.toString(), movieUpdateRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(updatedMovie);
    }
}
