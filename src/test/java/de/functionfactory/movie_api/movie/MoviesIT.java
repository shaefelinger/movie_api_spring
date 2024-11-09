package de.functionfactory.movie_api.movie;

import de.functionfactory.movie_api.movie.entity.Movie;
import de.functionfactory.movie_api.utils.TestDataGenerator;
import io.restassured.common.mapper.TypeRef;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
public class MoviesIT {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>("postgres:16-alpine")
                    .withDatabaseName("movies-test-db");

    @BeforeAll
    static void setUpDB() {
        postgresContainer.start();
    }

    @AfterAll
    static void tearDown() {
        if (postgresContainer != null && postgresContainer.isRunning()) {
            postgresContainer.stop();
        }
    }

    @Autowired
    private MovieController movieController;

    @Autowired
    private TestDataGenerator testDataGenerator;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        // Clear all data from the movies table
        jdbcTemplate.execute("TRUNCATE TABLE movie CASCADE");
    }

    @Test
    void canStartPostgresDB() throws Exception {
        assertThat(postgresContainer.isCreated()).isTrue();
        assertThat(postgresContainer.isRunning()).isTrue();
    }

    @Nested
    @DisplayName("GET request to /api/movies")
    class whenGetMovies {
        @Test
        @DisplayName("list of movies is returned with correct content")
        public void whenGetMovies_thenListOfMoviesIsReturned() {
            // Step 1: Create test movies
            Movie movieOne = testDataGenerator.createFakeMovie();
            Movie movieTwo = testDataGenerator.createFakeMovie();

            RestAssuredMockMvc.standaloneSetup(movieController);

            // Step 2: Get all movies and verify
            Map<String, Object> response = RestAssuredMockMvc
                    .given()
                    .when().get("/api/movies")
                    .then()
                    .status(HttpStatus.OK)
                    .extract().as(new TypeRef<>() {
                    });

            List<Map<String, Object>> movies = (List<Map<String, Object>>) response.get("data");

            // Step 3: Verify both movies are in the response
            assertThat(movies.stream()
                    .anyMatch(movie -> movie.get("id").toString().equals(movieOne.getId())))
                    .isTrue();
            assertThat(movies.stream()
                    .anyMatch(movie -> movie.get("id").toString().equals(movieTwo.getId())))
                    .isTrue();
        }

        @Test
        @DisplayName("pagination returns correct results")
        public void whenGetMoviesWithPagination_thenCorrectPagesAreReturned() {
            // Step 1: Create 20 test movies
            List<Movie> createdMovies = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                Movie movie = testDataGenerator.createFakeMovie();
                createdMovies.add(movie);
            }

            RestAssuredMockMvc.standaloneSetup(movieController);

            // Step 2: Get first page (0-9)
            Map<String, Object> firstPageResponse = RestAssuredMockMvc
                    .given()
                    .log().all()
                    .queryParam("page", 0)
                    .queryParam("limit", 10)
                    .when().get("/api/movies")
                    .then()
                    .log().all()
                    .status(HttpStatus.OK)
                    .extract().as(new TypeRef<>() {
                    });

            List<Map<String, Object>> firstPageContent = (List<Map<String, Object>>) firstPageResponse.get("data");

            // Verify first page
            for (int i = 0; i < 10; i++) {
                final int index = i;
                assertThat(firstPageContent.stream()
                        .anyMatch(movie -> movie.get("id").equals(createdMovies.get(index).getId())))
                        .isTrue();
            }

            // Step 3: Get second page (10-19)
            Map<String, Object> secondPageResponse = RestAssuredMockMvc
                    .given()
                    .queryParam("page", 1)
                    .queryParam("limit", 10)
                    .when().get("/api/movies")
                    .then()
                    .status(HttpStatus.OK)
                    .extract().as(new TypeRef<Map<String, Object>>() {
                    });

            List<Map<String, Object>> secondPageContent = (List<Map<String, Object>>) secondPageResponse.get("data");

            // Verify second page
            for (int i = 10; i < 20; i++) {
                final int index = i;
                assertThat(secondPageContent.stream()
                        .anyMatch(movie -> movie.get("id").equals(createdMovies.get(index).getId())))
                        .isTrue();
            }
        }
    }

    @Nested
    @DisplayName("When we make a GET request to api/movies/:id for a single movie")
    class WhenGetMovieById {

        @Nested
        @DisplayName("With a valid uuid")
        class WithValidUuid {
            @Test
            @DisplayName("Then the correct movie is returned")
            void thenCorrectMovieIsReturned() {
                // Create test movie
                Movie movieOne = testDataGenerator.createFakeMovie();

                RestAssuredMockMvc.standaloneSetup(movieController);
                Map<String, Object> response = RestAssuredMockMvc
                        .given()
                        .when().get("/api/movies/{id}", movieOne.getId())
                        .then()
                        .status(HttpStatus.OK)
                        .extract().as(new TypeRef<>() {
                        });

                // Verify response matches created movie
                assertThat(response.get("id")).isEqualTo(movieOne.getId());
                assertThat(response.get("title")).isEqualTo(movieOne.getTitle());
                // Add other relevant assertions
            }
        }

        @Nested
        @DisplayName("With an invalid uuid")
        class WithInvalidUuid {
            @Test
            @DisplayName("Then a 400 status code is returned")
            void thenBadRequestIsReturned() {
                String movieId = "random-id";

                RestAssuredMockMvc.standaloneSetup(movieController);
                RestAssuredMockMvc
                        .given()
                        .when().get("/api/movies/{id}", movieId)
                        .then()
                        .status(HttpStatus.BAD_REQUEST);
            }
        }

        @Nested
        @DisplayName("With a uuid that does not exist in the database")
        class WithNonExistentUuid {
            @Test
            @DisplayName("Then a 404 Not Found status code is returned")
            void thenNotFoundIsReturned() {
                String movieId = "05acb69a-ad9b-43fa-bea8-95e4c95e30e0";

                RestAssuredMockMvc.standaloneSetup(movieController);
                RestAssuredMockMvc
                        .given()
                        .when().get("/api/movies/{id}", movieId)
                        .then()
                        .status(HttpStatus.NOT_FOUND);
            }
        }
    }

    // POST
    @Nested
    @DisplayName("POST request to api/movies/:id ")
    class whenPostMovie {
        @Test
        @DisplayName("Too long title -> BAD_REQUEST returned")
        public void whenPostMovieWithTooLongTitle_thenBadRequestStatusIsReturned() {
            String longTitle = """
                    Lorem Ipsum is simply dummy text of the printing and typesetting industry. 
                    Lorem Ipsum has been the industry standard dummy text ever since the 1500s, 
                    when an unknown printer took a galley of type and scrambled it to make a type specimen book. 
                    It has survived not only five centuries, but also the leap into electronic typesetting, 
                    remaining essentially unchanged. It was popularised in the 1960s with the release of 
                    Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing 
                    software like Aldus PageMaker including versions of Lorem Ipsum.
                    """;

            Map<String, Object> mockMovie = new HashMap<>();
            mockMovie.put("title", longTitle);
            mockMovie.put("overview", "");
            mockMovie.put("tagline", "");
            mockMovie.put("runtime", "02:00:00");
            mockMovie.put("release_date", "1994-04-04");
            mockMovie.put("revenue", 1000000);
            mockMovie.put("poster_path", "");

            RestAssuredMockMvc.standaloneSetup(movieController);
            RestAssuredMockMvc
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(mockMovie)
                    .when().post("/api/movies/")
                    .then()
                    .log().all()
                    .status(HttpStatus.BAD_REQUEST);

            // Optionally, add checks for error message:
            // .body("message", containsString("title too long"));
        }

        @Test
        @DisplayName("Wrong runtime format -> BAD_REQUEST returned")
        public void whenPostMovieWithWrongRuntimeFormat_thenBadRequestStatusIsReturned() {
            String incorrectRuntime = "02:00"; // Incorrect format (expected format might be "HH:mm:ss")

            String longTitle = """
                    Lorem Ipsum is simply dummy text of the printing and typesetting industry.
                    """;

            Map<String, Object> mockMovie = new HashMap<>();
            mockMovie.put("title", longTitle);
            mockMovie.put("overview", "");
            mockMovie.put("tagline", "");
            mockMovie.put("runtime", incorrectRuntime);
            mockMovie.put("release_date", "1994-04-04");
            mockMovie.put("revenue", 1000000);
            mockMovie.put("poster_path", "");

            RestAssuredMockMvc.standaloneSetup(movieController);
            RestAssuredMockMvc
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(mockMovie)
                    .when().post("/api/movies/")
                    .then()
                    .log().all()
                    .status(HttpStatus.BAD_REQUEST);

            // Optionally, you can check for a specific error message:
            // .body("message", containsString("Invalid runtime format"));
        }

        @Test
        @DisplayName("valid input -> Movie is created")
        public void whenPostMovieWithValidInput_thenMovieIsCreated() {
            // Step 1: Define the mock movie
            Map<String, Object> mockMovie = new HashMap<>();
            mockMovie.put("title", "Porco Rosso");
            mockMovie.put("overview", """
                        In Italy in the 1930s, sky pirates in biplanes terrorize wealthy cruise ships as they sail the Adriatic Sea. 
                        The only pilot brave enough to stop the scourge is the mysterious Porco Rosso, a former World War I flying ace who was somehow turned into a pig during the war. 
                        As he prepares to battle the pirate crew's American ace, Porco Rosso enlists the help of spunky girl mechanic Fio Piccolo and his longtime friend Madame Gina.
                    """);
            mockMovie.put("tagline", "A Pig Got to Fly.");
            mockMovie.put("runtime", "01:34:00");
            mockMovie.put("release_date", "1992-07-18");
            mockMovie.put("revenue", 44600000);
            mockMovie.put("poster_path", "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/byKAndF6KQSDpGxp1mTr23jPbYp.jpg");

            // Step 2: Send POST request to create the movie
            RestAssuredMockMvc.standaloneSetup(movieController);
            Map response = RestAssuredMockMvc
                    .given()
//                .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(mockMovie)
                    .when().post("/api/movies/")
                    .then()
                    .log().all()
                    .status(HttpStatus.OK)
                    .extract().as(Map.class);

            // Step 3: Validate the response

            assertThat(response.get("title")).isEqualTo("Porco Rosso");
            assertThat(response.get("tagline")).isEqualTo("A Pig Got to Fly.");
            assertThat(response.get("runtime")).isEqualTo("01:34:00");
            assertThat(response.get("release_date")).isEqualTo("1992-07-18");
            assertThat(response.get("revenue")).isEqualTo(44600000);
            assertThat(response.get("poster_path")).isEqualTo("https://www.themoviedb.org/t/p/w600_and_h900_bestv2/byKAndF6KQSDpGxp1mTr23jPbYp.jpg");

        }
    }

    // PATCH
    @Nested
    @DisplayName("PATCH request to api/movies/:id ")
    class whenPatchMovie {
        @Test
        @DisplayName("invalid uuid -> BAD_REQUEST returned")
        public void whenPatchMovieWithInvalidUuid_thenBadRequestStatusIsReturned() {
            String invalidId = "invalid-uuid"; // Invalid UUID format

            RestAssuredMockMvc.standaloneSetup(movieController);
            RestAssuredMockMvc
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().patch("/api/movies/{id}", invalidId)
                    .then()
                    .log().all()
                    .status(HttpStatus.BAD_REQUEST);

            // Optionally, you can check for specific error messages:
            // .body("message", containsString("Invalid UUID"));
        }

        @Test
        @DisplayName("Non-existent uuid -> NOT_FOUND returned")
        public void whenPatchMovieWithNonExistentId_thenNotFoundStatusIsReturned() {
            // Step 1: Arrange
            String nonExistentId = UUID.randomUUID().toString(); // Generate a random UUID
            // Define the mock movie for creation
            Map<String, Object> mockCreateMovie = new HashMap<>();
            mockCreateMovie.put("title", "Grave of the Fireflies");
            mockCreateMovie.put("overview", """
                        In the final months of World War II, 14-year-old Seita and his sister Setsuko are orphaned when their mother is killed during an air raid in Kobe, Japan. 
                        After a falling out with their aunt, they move into an abandoned bomb shelter. With no surviving relatives and their emergency rations depleted, 
                        Seita and Setsuko struggle to survive.
                    """);
            mockCreateMovie.put("tagline", "");
            mockCreateMovie.put("runtime", "01:29:00");
            mockCreateMovie.put("release_date", "1988-04-16");
            mockCreateMovie.put("revenue", 516962);
            mockCreateMovie.put("poster_path", "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/qG3RYlIVpTYclR9TYIsy8p7m7AT.jpg");

            RestAssuredMockMvc.standaloneSetup(movieController);
            RestAssuredMockMvc
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(mockCreateMovie)
                    .when().patch("/api/movies/{id}", nonExistentId)
                    .then()
                    .log().all()
                    .status(HttpStatus.NOT_FOUND);

            // Optionally, you can add more specific checks:
            // .body("message", containsString("Movie not found"));
        }

        @Test
        @DisplayName("valid input -> updated Movie is returned")
        public void whenPatchMovieWithValidInput_thenUpdatedMovieIsReturned() {
            // Step 1: Define the mock movie for creation
            Map<String, Object> mockCreateMovie = new HashMap<>();
            mockCreateMovie.put("title", "Grave of the Fireflies");
            mockCreateMovie.put("overview", """
                        In the final months of World War II, 14-year-old Seita and his sister Setsuko are orphaned when their mother is killed during an air raid in Kobe, Japan. 
                        After a falling out with their aunt, they move into an abandoned bomb shelter. With no surviving relatives and their emergency rations depleted, 
                        Seita and Setsuko struggle to survive.
                    """);
            mockCreateMovie.put("tagline", "");
            mockCreateMovie.put("runtime", "01:29:00");
            mockCreateMovie.put("release_date", "1988-04-16");
            mockCreateMovie.put("revenue", 516962);
            mockCreateMovie.put("poster_path", "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/qG3RYlIVpTYclR9TYIsy8p7m7AT.jpg");

            // Step 2: Create the movie via POST request
            RestAssuredMockMvc.standaloneSetup(movieController);
            Map createdMovieResponse = RestAssuredMockMvc
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(mockCreateMovie)
                    .when().post("/api/movies/")
                    .then()
                    .log().all()
                    .status(HttpStatus.OK)
                    .extract().as(Map.class);

            // Step 3: Define the movie update
            Map<String, Object> movieUpdate = new HashMap<>();
            movieUpdate.put("revenue", 519000);

            // Step 4: Send PATCH request to update the movie
            Map updatedMovieResponse = RestAssuredMockMvc
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(movieUpdate)
                    .when().patch("/api/movies/" + createdMovieResponse.get("id"))
                    .then()
                    .log().all()
                    .status(HttpStatus.OK)
                    .extract().as(Map.class);

            // Step 5: Validate the updated response
            assertThat(updatedMovieResponse)
                    .usingRecursiveComparison()
                    .ignoringFields("id") // Ignore the generated 'id'
                    .isEqualTo(new HashMap<>() {{
                        putAll(createdMovieResponse);
                        putAll(movieUpdate);
                    }});
        }
    }

    // DELETE
    @Nested
    @DisplayName("DELETE request to api/movies/:id ")
    class whenDeleteMovie {
        @Test
        @DisplayName("invalid uuid -> BAD_REQUEST returned")
        public void whenDeleteMovieWithInvalidId_thenBadRequestStatusIsReturned() {
            String invalidId = "invalid-uuid"; // Invalid UUID format

            RestAssuredMockMvc.standaloneSetup(movieController);
            RestAssuredMockMvc
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().delete("/api/movies/{id}", invalidId)
                    .then()
                    .log().all()
                    .status(HttpStatus.BAD_REQUEST);

            // Optionally, check for a specific error message:
            // .body("message", containsString("Invalid UUID"));
        }

        @Test
        @DisplayName("Non-existent uuid -> NOT_FOUND returned")
        public void whenDeleteMovieWithNonExistentId_thenNotFoundStatusIsReturned() {
            String nonExistentId = UUID.randomUUID().toString(); // Generate a random UUID

            RestAssuredMockMvc.standaloneSetup(movieController);
            RestAssuredMockMvc
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().delete("/api/movies/{id}", nonExistentId)
                    .then()
                    .log().all()
                    .status(HttpStatus.NOT_FOUND);

            // Optionally, check for a specific error message:
            // .body("message", containsString("Movie not found"));
        }

        @Test
        @DisplayName("valid uuid -> Movie is deleted and OK returned")

        public void whenDeleteMovieWithValidId_thenMovieIsDeletedSuccessfully() {
            // Step 1: Create movie
            Map<String, Object> mockCreateMovie = new HashMap<>();
            mockCreateMovie.put("title", "Kiki's Delivery Service");
            mockCreateMovie.put("overview", """
                        A young witch, on her mandatory year of independent life, finds fitting into a new community difficult while 
                        she supports herself by running an air courier service.
                    """);
            mockCreateMovie.put("tagline", "I was feeling blue, but I'm better now.");
            mockCreateMovie.put("runtime", "01:43:00");
            mockCreateMovie.put("release_date", "1989-07-29");
            mockCreateMovie.put("revenue", 449017);
            mockCreateMovie.put("poster_path", "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/pN4iKcvpyxkhtyOKUKe2tTqEDYm.jpg");

            // Step 2: Create the movie
            RestAssuredMockMvc.standaloneSetup(movieController);
            String createdMovieId = RestAssuredMockMvc
                    .given()
//                .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(mockCreateMovie)
                    .when().post("/api/movies/")
                    .then()
                    .log().all()
                    .status(HttpStatus.OK)
                    .extract().path("id");

            // Step 3: Delete the movie
            RestAssuredMockMvc
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().delete("/api/movies/{id}", createdMovieId)
                    .then()
                    .log().all()
                    .status(HttpStatus.OK); // 204 No Content

            // Step 4: Check that the movie was deleted
            RestAssuredMockMvc
                    .given()
                    .log().all()
                    .when().get("/api/movies/{id}", createdMovieId)
                    .then()
                    .log().all()
                    .status(HttpStatus.NOT_FOUND); // 404 Not Found
        }
    }
}
