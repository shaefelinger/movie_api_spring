package de.functionfactory.movie_api.moviereview;

import de.functionfactory.movie_api.movie.MovieController;
import de.functionfactory.movie_api.movie.entity.Movie;
import de.functionfactory.movie_api.moviereview.entity.MovieReview;
import de.functionfactory.movie_api.utils.TestDataGenerator;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
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

    @Autowired
    private TestDataGenerator testDataGenerator;

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
    @DisplayName("GET request to /api/movies/{id}/reviews/{reviewId}")
    class whenGetReviewByReviewId {
        @Test
        @DisplayName("returns 404 when review does not exist")
        public void whenReviewDoesNotExist_thenReturn404() {
            String movieId = "f74cf1ca-8c7b-435b-96c6-e4448a653596"; // existing movie ID
            String nonExistentReviewId = UUID.randomUUID().toString();

            RestAssuredMockMvc.standaloneSetup(movieReviewController);
            RestAssuredMockMvc.given()
                    .log().all()
                    .when()
                    .get("/api/movies/" + movieId + "/reviews/" + nonExistentReviewId)
                    .then()
                    .log().all()
                    .status(HttpStatus.NOT_FOUND);
        }


        @Test
        @DisplayName("review for the specific movie is returned")
        public void whenGetReviewByReviewId_thenReviewIsReturned() {
            MovieReview fakeReview = testDataGenerator.createFakeMovieWithReviews();

            String movieId = fakeReview.getMovie().getId();
            String reviewId = fakeReview.getId();

            RestAssuredMockMvc.standaloneSetup(movieReviewController);
            RestAssuredMockMvc.given()
                    .log().all()
                    .when()
                    .get("/api/movies/" + movieId + "/reviews/" + reviewId)
                    .then()
                    .log().all()
                    .status(HttpStatus.OK)
//                    .body("size()", equalTo(1))
                    .body("authorName", equalTo(fakeReview.getAuthorName()));
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

    @Nested
    @DisplayName("DELETE request to /api/movies/{movieId}/reviews/{reviewId}")
    class whenDeleteReview {

        @Test
        @DisplayName("returns 404 when movie does not exist")
        public void whenMovieDoesNotExist_thenReturn404() {
            String nonExistentMovieId = UUID.randomUUID().toString();
            String reviewId = UUID.randomUUID().toString();

            RestAssuredMockMvc.standaloneSetup(movieReviewController);
            RestAssuredMockMvc.given()
                    .log().all()
                    .when()
                    .delete("/api/movies/" + nonExistentMovieId + "/reviews/" + reviewId)
                    .then()
                    .log().all()
                    .status(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("returns 404 when review does not exist")
        public void whenReviewDoesNotExist_thenReturn404() {
            String movieId = "f74cf1ca-8c7b-435b-96c6-e4448a653596"; // existing movie ID
            String nonExistentReviewId = UUID.randomUUID().toString();

            RestAssuredMockMvc.standaloneSetup(movieReviewController);
            RestAssuredMockMvc.given()
                    .log().all()
                    .when()
                    .delete("/api/movies/" + movieId + "/reviews/" + nonExistentReviewId)
                    .then()
                    .log().all()
                    .status(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("returns 400 when review ID is invalid")
        public void whenReviewIdIsInvalid_thenReturn400() {
            String movieId = "f74cf1ca-8c7b-435b-96c6-e4448a653596"; // existing movie ID
            String invalidReviewId = "invalid-uuid";

            RestAssuredMockMvc.standaloneSetup(movieReviewController);
            RestAssuredMockMvc.given()
                    .log().all()
                    .when()
                    .delete("/api/movies/" + movieId + "/reviews/" + invalidReviewId)
                    .then()
                    .log().all()
                    .status(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("successfully deletes review when IDs are valid")
        public void whenIdsAreValid_thenDeleteReviewAndReturn204() {
            MovieReview fakeReview = testDataGenerator.createFakeMovieWithReviews();

            String movieId = fakeReview.getMovie().getId();
            String reviewId = fakeReview.getId();

            // Delete the review
            RestAssuredMockMvc.standaloneSetup(movieReviewController);
            RestAssuredMockMvc.given()
                    .log().all()
                    .when()
                    .delete("/api/movies/" + movieId + "/reviews/" + reviewId)
                    .then()
                    .log().all()
                    .status(HttpStatus.NO_CONTENT);

            // Verify the review no longer exists
            RestAssuredMockMvc.given()
                    .log().all()
                    .when()
                    .get("/api/movies/" + movieId + "/reviews/" + reviewId)
                    .then()
                    .log().all()
                    .status(HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("PATCH request to /api/movies/{movieId}/reviews/{reviewId}")
    class whenPatchReview {

        @Test
        @DisplayName("successfully updates review when data is valid")
        public void whenUpdateDataIsValid_thenReviewIsUpdated() {
            MovieReview fakeReview = testDataGenerator.createFakeMovieWithReviews();
            String movieId = fakeReview.getMovie().getId();
            String reviewId = fakeReview.getId();
            
            String updateData = "{ \"rating\": " + (fakeReview.getRating() + 1) + " }";

            RestAssuredMockMvc.standaloneSetup(movieReviewController);
            RestAssuredMockMvc.given()
                    .log().all()
                    .contentType("application/json")
                    .body(updateData)
                    .when()
                    .patch("/api/movies/" + movieId + "/reviews/" + reviewId)
                    .then()
                    .log().all()
                    .status(HttpStatus.OK)
                    .body("rating", equalTo(fakeReview.getRating() + 1))
                    .body("authorName", equalTo(fakeReview.getAuthorName()))
                    .body("content", equalTo(fakeReview.getContent()));
        }

        @Test
        @DisplayName("returns 404 when movie does not exist")
        public void whenMovieDoesNotExist_thenReturn404() {
            String nonExistentMovieId = UUID.randomUUID().toString();
            String reviewId = UUID.randomUUID().toString();
            String updateData = "{ \"rating\": 8 }";

            RestAssuredMockMvc.standaloneSetup(movieReviewController);
            RestAssuredMockMvc.given()
                    .log().all()
                    .contentType("application/json")
                    .body(updateData)
                    .when()
                    .patch("/api/movies/" + nonExistentMovieId + "/reviews/" + reviewId)
                    .then()
                    .log().all()
                    .status(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("returns 404 when review does not exist")
        public void whenReviewDoesNotExist_thenReturn404() {
            String movieId = "f74cf1ca-8c7b-435b-96c6-e4448a653596"; // existing movie ID
            String nonExistentReviewId = UUID.randomUUID().toString();
            String updateData = "{ \"rating\": 8 }";

            RestAssuredMockMvc.standaloneSetup(movieReviewController);
            RestAssuredMockMvc.given()
                    .log().all()
                    .contentType("application/json")
                    .body(updateData)
                    .when()
                    .patch("/api/movies/" + movieId + "/reviews/" + nonExistentReviewId)
                    .then()
                    .log().all()
                    .status(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("returns 400 when review ID is invalid")
        public void whenReviewIdIsInvalid_thenReturn400() {
            String movieId = "f74cf1ca-8c7b-435b-96c6-e4448a653596"; // existing movie ID
            String invalidReviewId = "invalid-uuid";
            String updateData = "{ \"rating\": 8 }";

            RestAssuredMockMvc.standaloneSetup(movieReviewController);
            RestAssuredMockMvc.given()
                    .log().all()
                    .contentType("application/json")
                    .body(updateData)
                    .when()
                    .patch("/api/movies/" + movieId + "/reviews/" + invalidReviewId)
                    .then()
                    .log().all()
                    .status(HttpStatus.BAD_REQUEST);
        }
    }

    // Implement this method based on your test setup
//    private Movie createFakeMovieWithReviews() {
//        // Logic to create a movie with reviews
//    }
//}
}
