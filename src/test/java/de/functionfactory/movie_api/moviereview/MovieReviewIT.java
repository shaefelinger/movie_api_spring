package de.functionfactory.movie_api.moviereview;

import de.functionfactory.movie_api.movie.MovieController;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Testcontainers

@SpringBootTest
@AutoConfigureMockMvc
public class MovieReviewIT {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>("postgres:16-alpine")
                    .withDatabaseName("movies-test-db");

    @Autowired
    private MovieController movieController;

    @Autowired
    private MovieReviewController movieReviewController;

    @Test
    void canStartPostgresDB() throws Exception {
        assertThat(postgresContainer.isCreated()).isTrue();
        assertThat(postgresContainer.isRunning()).isTrue();
    }

    @Nested
    @DisplayName("GET request to /api/movies/reviews")
    class whenGetReviews {

        @Test
        @DisplayName("list of all reviews is returned")
        public void whenGetReviews_thenListOfReviewsIsReturned() {
            RestAssuredMockMvc.standaloneSetup(movieReviewController);
            RestAssuredMockMvc.given()
                    .log().all()
                    .when().get("/api/movies/reviews")
                    .then()
                    .log().all()
                    .status(HttpStatus.OK)
//                    .body("size()", equalTo(2))
                    .body("authorName[0]", equalTo("Ze Blah"));
        }
    }

    @Nested
    @DisplayName("GET request to /api/movies/{id}/reviews")
    class whenGetReviewByMovieId {

        @Test
        @DisplayName("list of reviews for the specific movie is returned")
        public void whenGetReviewsByMovieId_thenListOfReviewsIsReturned() {
            RestAssuredMockMvc.standaloneSetup(movieReviewController);
            RestAssuredMockMvc.given()
                    .log().all()
                    .when().get("/api/movies/f74cf1ca-8c7b-435b-96c6-e4448a653596/reviews")
                    .then()
                    .log().all()
                    .status(HttpStatus.OK)
//                    .body("size()", equalTo(1))
                    .body("authorName[0]", equalTo("Ze Blah"));
        }
    }

    @Nested
    @DisplayName("POST request to /api/movies/{id}/reviews")
    class whenPostReview {

        @Test
        public void whenMovieExistsAndDataIsValid_thenMovieReviewIsCreated() {
            // Assuming createFakeMovieWithReviews() creates a movie and returns it
//        Movie movie = createFakeMovieWithReviews();

            // Prepare the review data
            String reviewData = "{ \"content\": \"I really enjoyed the movie, particularly the ending. This is by far one of the best movies out there.\", " +
                    "\"authorName\": \"Foo Bar\", \"rating\": 8 }";

            String MOVIE_ID = "f74cf1ca-8c7b-435b-96c6-e4448a653596";
            // Make the POST request to create a review

            RestAssuredMockMvc.standaloneSetup(movieReviewController);
            RestAssuredMockMvc.given()
//                .auth()
//                .basic("admin", "supersecret") // Replace with actual username and password
                    .log().all()
                    .contentType("application/json")
                    .body(reviewData)
                    .when()
                    .post("/api/movies/" + MOVIE_ID + "/reviews")
                    .then()
                    .statusCode(200)
                    .body("content", equalTo("I really enjoyed the movie, particularly the ending. This is by far one of the best movies out there."))
                    .body("authorName", equalTo("Foo Bar"))
                    .body("rating", equalTo(8));
        }
    }

    // Implement this method based on your test setup
//    private Movie createFakeMovieWithReviews() {
//        // Logic to create a movie with reviews
//    }
//}
}
