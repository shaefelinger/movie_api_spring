package de.functionfactory.movie_api.moviereview;

import de.functionfactory.movie_api.moviereview.dto.MovieReviewView;
import de.functionfactory.movie_api.moviereview.entity.MovieReview;

import java.util.List;

public interface MovieReviewDao {
    List<MovieReviewView> selectAllReviews();
    List<MovieReviewView> selectReviewsByMovieId(String movieId);
    MovieReview saveReview(MovieReview movieReview);
    MovieReview selectReviewById(String reviewId);
    void deleteById(String reviewId);
}
