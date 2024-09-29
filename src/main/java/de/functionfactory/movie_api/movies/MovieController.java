package de.functionfactory.movie_api.movies;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/movies")
@Validated
public class MovieController {

    private final MovieService movieService;


    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    ResponseEntity<List<Movie>> getMovies() {
        var books = movieService.getMovies();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    ResponseEntity<Movie> getMovieById(@PathVariable @Valid UUID id) {
        Movie movie = movieService.getMovieById(id.toString());
        return ResponseEntity.status(HttpStatus.OK).body(movie);
    }

    @GetMapping(params = "title")
    ResponseEntity<List<Movie>> getMovieByTitle(
            @RequestParam
            @NotBlank
            @Size(min=3, message="Title must be at least 3 characters long")
            String title) {
        var movie = movieService.getMoviesByTitle(title);
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

        Optional<Movie> updatedMovie = movieService.updateMovie(id.toString(), movieUpdateRequestDto);

        if (updatedMovie.isPresent()) {
            return ResponseEntity.ok(updatedMovie.get());
        } else {
            return ResponseEntity.notFound().build();
        }

    }
}
