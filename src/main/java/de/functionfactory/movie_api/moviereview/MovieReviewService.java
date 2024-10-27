package de.functionfactory.movie_api.moviereview;

import de.functionfactory.movie_api.moviereview.dto.ReviewCreateRequest;
import de.functionfactory.movie_api.moviereview.dto.MovieReviewView;
import de.functionfactory.movie_api.moviereview.entity.MovieReview;

import java.util.List;

public interface MovieReviewService {
    List<MovieReviewView> getMovieReviews();
    List<MovieReviewView> getReviewsByMovieId(String movieId);
    MovieReview createReview(MovieReview movieReview);
}
