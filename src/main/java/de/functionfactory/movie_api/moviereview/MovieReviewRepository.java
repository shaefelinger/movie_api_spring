package de.functionfactory.movie_api.moviereview;

import de.functionfactory.movie_api.moviereview.dto.MovieReviewView;
import de.functionfactory.movie_api.moviereview.entity.MovieReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface MovieReviewRepository extends JpaRepository<MovieReview, String> {
    @NonNull
    @Query(value = "SELECT new de.functionfactory.movie_api.moviereview.dto.MovieReviewViewImpl(" +
           "r.id, r.authorName, r.content, r.rating, r.movie.id) FROM MovieReview r")
    List<MovieReviewView> findAllProjectedBy();

    @NonNull
    @Query(value = "SELECT new de.functionfactory.movie_api.moviereview.dto.MovieReviewViewImpl(" +
           "r.id, r.authorName, r.content, r.rating, r.movie.id) " +
           "FROM MovieReview r WHERE r.movie.id = :movieId")
    List<MovieReviewView> findMovieReviewsByMovie_Id(@NonNull @Param("movieId") String movieId);

    @NonNull
    void deleteById(@NonNull String reviewId);

    @NonNull
    Optional<MovieReview> findMovieReviewById(@NonNull String reviewId);
}
