package de.functionfactory.movie_api.utils;

import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;

import de.functionfactory.movie_api.movie.MovieRepository;
import de.functionfactory.movie_api.movie.entity.Movie;
import de.functionfactory.movie_api.moviereview.MovieReviewRepository;
import de.functionfactory.movie_api.moviereview.entity.MovieReview;
import de.functionfactory.movie_api.tech.exceptions.ResourceNotFoundException;

@Component
public class TestDataGenerator {
    private final Faker faker = new Faker();
    private final MovieRepository movieRepository;
    private final MovieReviewRepository movieReviewRepository;

    public record MovieWithReview(Movie movie, MovieReview review) {}

    public TestDataGenerator(MovieRepository movieRepository, MovieReviewRepository movieReviewRepository) {
        this.movieRepository = movieRepository;
        this.movieReviewRepository = movieReviewRepository;
    }

    public Movie createFakeMovie() {
        Movie movie = Movie.builder()
                .release_date("2024-10-12")
                .title(faker.book().title())
                .overview(faker.lorem().paragraph(3))
                .tagline(faker.lordOfTheRings().character())
                .runtime("01:45:00") // fixed runtime for simplicity
                .revenue((int) Long.parseLong(faker.number().digits(6)))
                .poster_path(faker.internet().image())
                .build();

        return movieRepository.save(movie);
    }


    public MovieReview createFakeMovieReview(Movie movie) {
        MovieReview review = MovieReview.builder()
                .content(faker.lorem().paragraph(5))
                .rating(faker.number().numberBetween(1, 5))
                .authorName(faker.name().fullName())
                .movie(movie)
                .build();

        final MovieReview saved = movieReviewRepository.save(review);
        return saved;
    }

    public MovieWithReview createFakeMovieWithReviews() {
        Movie movie = createFakeMovie();
        MovieReview review = createFakeMovieReview(movie);

        Movie updatedMovie = movieRepository.findById(movie.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

        return new MovieWithReview(updatedMovie, review);
    }

}
