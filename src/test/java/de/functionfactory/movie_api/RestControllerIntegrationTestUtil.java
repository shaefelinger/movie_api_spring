package de.functionfactory.movie_api;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import io.restassured.RestAssured;
import org.springframework.http.HttpStatus;

public class RestControllerIntegrationTestUtil {

    private RestControllerIntegrationTestUtil() {
    }

    // GET

    public static DocumentContext getBodyFromGetReq(final String url) {
        return JsonPath.parse(getRawBodyFromGetReq(url, HttpStatus.OK));
    }


    public static String getRawBodyFromGetReq(final String url,
//                                               final String login,
                                               final HttpStatus expectedStatus) {
        return RestAssured.given()
//                .auth().basic(login, BASIC_AUTH_PW)
                .when()
                .get(url)
                .then()
                .assertThat()
                .statusCode(expectedStatus.value())
                .extract().response().asString();
//                .asString();
    }
}
