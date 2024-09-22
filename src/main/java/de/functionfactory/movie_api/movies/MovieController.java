package de.functionfactory.movie_api.movies;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/movies")
@Validated
public class MovieController {

    private final MovieService movieService;

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
        var movie = movieService.getMovieById(id.toString());
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
}
