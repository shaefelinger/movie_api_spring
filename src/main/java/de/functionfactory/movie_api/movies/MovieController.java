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

//    @PatchMapping("/{id}")
//    ResponseEntity<Movie> patchMovie(
//            @PathVariable @Valid UUID id,
//            @RequestBody @Valid Movie editedMovie
//    ) {
//        Movie foundMovie = movieService.getMovieById(id.toString());
//        Movie combinedMovie = new Movie();
//
////        final HashMap<Object, Object> combinedMovie = new HashMap<>() {{
////            putAll(foundMovie);
////            putAll(editedMovie);
////        }};
//
//
//        return ResponseEntity.ok().body(foundMovie);
//    }



//    @PatchMapping("/{id}")
//    public ResponseEntity<Movie> updateMovie(@PathVariable String id, @RequestBody Map<String, Object> updates) {
//        Optional<Movie> optionalMovie = movieRepository.findById(id);
//
//        if (optionalMovie.isPresent()) {
//            Movie movie = optionalMovie.get();
//
//            updates.forEach((key, value) -> {
//                Field field = ReflectionUtils.findField(Movie.class, key);
//                if (field != null) {
//                    field.setAccessible(true);
//                    ReflectionUtils.setField(field, movie, value);
//                }
//            });
//
//            Movie updatedMovie = movieRepository.save(movie);
//            return ResponseEntity.ok(updatedMovie);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }

    @PatchMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(
            @PathVariable String id,
            @RequestBody MovieUpdateRequestDto movieUpdateRequestDto) {

        Optional<Movie> optionalMovie = movieRepository.findById(id);

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

            Movie updatedMovie = movieRepository.save(movie);
            return ResponseEntity.ok(updatedMovie);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
