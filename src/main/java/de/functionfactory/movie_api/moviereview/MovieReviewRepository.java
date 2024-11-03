package de.functionfactory.movie_api.moviereview;

import de.functionfactory.movie_api.moviereview.dto.MovieReviewView;
import de.functionfactory.movie_api.moviereview.entity.MovieReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MovieReviewRepository extends JpaRepository<MovieReview, String> {
    List<MovieReviewView> findAllProjectedBy();
    List<MovieReviewView> findMovieReviewsByMovie_Id(String movieId);
    // MovieReview save(MovieReview movieReview);
    void deleteById(String reviewId);
    Optional<MovieReview> findMovieReviewById(String reviewId);
}