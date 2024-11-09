package de.functionfactory.movie_api.moviereview;

import de.functionfactory.movie_api.moviereview.dto.MovieReviewView;
import de.functionfactory.movie_api.moviereview.dto.ReviewUpdateRequestDto;
import de.functionfactory.movie_api.moviereview.entity.MovieReview;
import de.functionfactory.movie_api.tech.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieReviewServiceImpl implements MovieReviewService {
    private final MovieReviewDao movieReviewDao;

    public MovieReviewServiceImpl(MovieReviewDao movieReviewDao) {
        this.movieReviewDao = movieReviewDao;
    }

    public List<MovieReviewView> getMovieReviews() {
        return movieReviewDao.selectAllReviews();
    }

    public List<MovieReviewView> getReviewsByMovieId(String movieId) {
        return movieReviewDao.selectReviewsByMovieId(movieId);
    }

    public MovieReview getReviewById(String reviewId) {
        return movieReviewDao.selectReviewById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review with id [%s] not found".formatted(reviewId)));
    }

    @Override
    public MovieReview createReview(MovieReview movieReview) {
        return movieReviewDao.saveReview(movieReview);
    }

    public void deleteReview(String reviewId) {
        movieReviewDao.deleteById(reviewId);
    }

    @Override
    public MovieReview updateReview(String reviewId, ReviewUpdateRequestDto newReview) {

        MovieReview existingReview = movieReviewDao.selectReviewById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review with id [%s] not found".formatted(reviewId)));

           MovieReview updatedReview = MovieReview.builder()
           .id(existingReview.getId())
           .movie(existingReview.getMovie())
           .content(newReview.getContent() != null ? newReview.getContent() : existingReview.getContent())
           .rating(newReview.getRating() != null ? newReview.getRating() : existingReview.getRating())
           .authorName(newReview.getAuthorName() != null ? newReview.getAuthorName() : existingReview.getAuthorName())
           .build();

        return movieReviewDao.saveReview(updatedReview);
    }
}
