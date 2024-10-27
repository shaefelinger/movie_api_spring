package de.functionfactory.movie_api.moviereview;

import de.functionfactory.movie_api.moviereview.dto.MovieReviewView;
import de.functionfactory.movie_api.moviereview.dto.ReviewCreateRequest;
import de.functionfactory.movie_api.moviereview.entity.MovieReview;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MovieReviewJPADataAccessService implements MovieReviewDao{
    private final MovieReviewRepository movieReviewRepository;

    public MovieReviewJPADataAccessService(MovieReviewRepository movieReviewRepository) {
        this.movieReviewRepository = movieReviewRepository;
    }

    @Override
    public List<MovieReviewView> selectAllReviews() {
        return  movieReviewRepository.findAllProjectedBy();
    }

    @Override
    public List<MovieReviewView> selectReviewsByMovieId(String movieId) {
        return movieReviewRepository.findMovieReviewsByMovie_Id(movieId);
    }

    @Override
    public MovieReview saveReview(MovieReview movieReview) {
        return movieReviewRepository.save(movieReview);
    }
}
