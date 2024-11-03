package de.functionfactory.movie_api.moviereview;

import de.functionfactory.movie_api.moviereview.dto.MovieReviewView;
import de.functionfactory.movie_api.moviereview.entity.MovieReview;

import java.util.List;
import java.util.Optional;

public interface MovieReviewDao {
    List<MovieReviewView> selectAllReviews();
    List<MovieReviewView> selectReviewsByMovieId(String movieId);
    MovieReview saveReview(MovieReview movieReview);
    Optional<MovieReview> selectReviewById(String reviewId);
    void deleteById(String reviewId);
}
