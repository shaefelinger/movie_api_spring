package de.functionfactory.movie_api.moviereview;

import de.functionfactory.movie_api.movie.MovieService;
import de.functionfactory.movie_api.movie.entity.Movie;
import de.functionfactory.movie_api.moviereview.dto.MovieReviewView;
import de.functionfactory.movie_api.moviereview.dto.ReviewUpdateRequestDto;
import de.functionfactory.movie_api.moviereview.entity.MovieReview;
import de.functionfactory.movie_api.tech.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/movies")
@Validated
public class MovieReviewController {
    private final MovieReviewService movieReviewService;
    private final MovieService movieService;

    public MovieReviewController(MovieReviewService movieReviewService, MovieService movieService) {
        this.movieReviewService = movieReviewService;
        this.movieService = movieService;
    }

    @GetMapping("/reviews")
    ResponseEntity<List<MovieReviewView>> getMovieReviews() {
        var reviews = movieReviewService.getMovieReviews();
        return ResponseEntity.status(HttpStatus.OK).body(reviews);
    }

    @GetMapping("/{movieId}/reviews")
    ResponseEntity<List<MovieReviewView>> getMovieReviewsByMovieId(@PathVariable @Valid UUID movieId) {
        List<MovieReviewView> reviews = movieReviewService.getReviewsByMovieId(movieId.toString());
        return ResponseEntity.status(HttpStatus.OK).body(reviews);
    }

    @GetMapping("/{movieId}/reviews/{reviewId}")
    ResponseEntity<MovieReview> getReviewById(@PathVariable @Valid UUID movieId,
                                                        @PathVariable @Valid UUID reviewId) {
        MovieReview review = movieReviewService.getReviewById(reviewId.toString());

        if(review == null) {
            throw new ResourceNotFoundException("Review not found");
        }

        return ResponseEntity.status(HttpStatus.OK).body(review);
    }

    @PostMapping("/{movieId}/reviews")
    ResponseEntity<MovieReview> postReview(@PathVariable @Valid UUID movieId,
                                           @RequestBody @Valid MovieReview movieReview) {

        // Fetch the Movie entity by movieId
        Movie movie = movieService.getMovieById(movieId.toString());

        // Set the movie in the review
        movieReview.setMovie(movie);

        var newReview = movieReviewService.createReview(movieReview);
        return ResponseEntity.status(HttpStatus.OK).body(newReview);
    }

    @DeleteMapping("/{movieId}/reviews/{reviewId}")
    ResponseEntity<Void> deleteReview(@PathVariable @Valid UUID movieId, @PathVariable @Valid UUID reviewId) {
        movieService.getMovieById(movieId.toString());
        movieReviewService.getReviewById(reviewId.toString());

        movieReviewService.deleteReview(reviewId.toString());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{movieId}/reviews/{reviewId}")
    ResponseEntity<MovieReview> updateReview(@PathVariable @Valid UUID movieId,
                                             @PathVariable @Valid UUID reviewId,
                                             @RequestBody @Valid ReviewUpdateRequestDto newReview) {

        movieService.getMovieById(movieId.toString());
        movieReviewService.getReviewById(reviewId.toString());

        var updatedReview = movieReviewService.updateReview(reviewId.toString(), newReview);
        return ResponseEntity.status(HttpStatus.OK).body(updatedReview);
    }

}
