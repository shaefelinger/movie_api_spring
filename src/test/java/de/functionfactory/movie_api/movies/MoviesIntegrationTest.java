package de.functionfactory.movie_api.movies;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
@AutoConfigureMockMvc
public class MoviesIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MovieController movieController;

    @Nested
    @DisplayName("GET request to /api/movies")
    class whenGetMovies {

        @Test
        @DisplayName("list of all movies is returned")
        public void whenGetMovies_thenListOfMoviesIsReturned() {
            RestAssuredMockMvc.standaloneSetup(movieController);
            RestAssuredMockMvc.
                    given()
                    .log().all()
                    .when().get("/api/movies")
                    .then()
                    .log().all()
                    .status(HttpStatus.OK)
//                .body("size()", equalTo(5))
                    .body("title[0]", equalTo("Spirited Away"));

//        MockMvcResponse mockMvcResponse = RestAssuredMockMvc.
//                given()
//                .log().all()
//                .when().get("/api/movies")
//                .andReturn();
//        mockMvcResponse.
//                then()
//                .log().all()
//                .statusCode(200)
//                .body("title[0]", equalTo("Spirited Away"))
//                .body("size()", equalTo(5));
        }
    }

    @Nested
    @DisplayName("GET request to api/movies/:id for a single movie")
    class whenGetMovieById {
        @Test
        @DisplayName("valid uuid -> correct movie is returned")
        public void whenGetMovieWithValidUuid_thenCorrectMovieIsReturned() {
            String movieId = "f74cf1ca-8c7b-435b-96c6-e4448a653596";

            RestAssuredMockMvc.standaloneSetup(movieController);
            RestAssuredMockMvc
                    .given()
                    .log().all()
                    .when().get("/api/movies/{id}", movieId)
                    .then()
                    .log().all()
                    .status(HttpStatus.OK)
                    .body("title", equalTo("Spirited Away"))
                    .body("revenue", equalTo(274925095));
        }

        @Test
        @DisplayName("Invalid uuid -> BAD_REQUEST returned")
        public void whenGetMovieWithInvalidUuid_thenBadRequestStatusIsReturned() {
            String movieId = "random-id";

            RestAssuredMockMvc.standaloneSetup(movieController);
            RestAssuredMockMvc
                    .given()
                    .log().all()
                    .when().get("/api/movies/{id}", movieId)
                    .then()
                    .log().all()
                    .status(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("Non-existent uuid -> NOT_FOUND returned")
        public void whenGetMovieWithNonExistentUuid_thenNotFoundStatusIsReturned() {
            String movieId = "05acb69a-ad9b-43fa-bea8-95e4c95e30e0";

            RestAssuredMockMvc.standaloneSetup(movieController);
            RestAssuredMockMvc
                    .given()
                    .log().all()
                    .when().get("/api/movies/{id}", movieId)
                    .then()
                    .log().all()
                    .status(HttpStatus.NOT_FOUND);
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

            System.out.println("🙈" + updatedMovieResponse);

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

//    @Test
//    @DisplayName("jo das ist mit der test util")
//    void testWithTestUtil() {
//        var result = getRawBodyFromGetReq("/api/movies", HttpStatus.OK);
//
//        System.out.println("😂" + result);
//
//        final DocumentContext parsed = JsonPath.parse(result);
//
//        final Object read = parsed.read("$..title");
//        System.out.println(read);
//    }
//
//    @Test
//    public void whenGetMovies_thenListOfMoviesIsReturned() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/movies")
//                        .contentType("application/json"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(5));
//    }
}
