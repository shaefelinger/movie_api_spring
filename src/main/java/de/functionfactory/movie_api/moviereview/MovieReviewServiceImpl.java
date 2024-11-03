package de.functionfactory.movie_api.moviereview;

import de.functionfactory.movie_api.moviereview.dto.MovieReviewView;
import de.functionfactory.movie_api.moviereview.dto.ReviewCreateRequest;
import de.functionfactory.movie_api.moviereview.entity.MovieReview;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieReviewServiceImpl implements MovieReviewService {
    private final MovieReviewDao movieReviewDao;

    public MovieReviewServiceImpl(MovieReviewDao movieReviewDao) {
        this.movieReviewDao = movieReviewDao;
    }

    public List<MovieReviewView> getMovieReviews() {
        List<MovieReviewView> movieReviews = movieReviewDao.selectAllReviews();
        return movieReviews;
    }

    public List<MovieReviewView> getReviewsByMovieId(String movieId) {
        List<MovieReviewView> movieReviews = movieReviewDao.selectReviewsByMovieId(movieId);
        return movieReviews;
    }

    public MovieReview getReviewById(String reviewId) {
      MovieReview review = movieReviewDao.selectReviewById(reviewId);
      return review;
    }

    @Override
    public MovieReview createReview(MovieReview movieReview) {
        var newReview = movieReviewDao.saveReview(movieReview);
        return newReview;
    }

    public void deleteReview(String reviewId) {
        movieReviewDao.deleteById(reviewId);
    }
}
