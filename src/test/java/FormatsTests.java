import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class FormatsTests {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://api.magicthegathering.io/v1";
    }

    @Test
    public void getAllFormats() {
        given()
                .when()
                .get("/formats")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("formats", not(empty()));
    }

    @Test
    public void checkIfLengthOfFormatsIsValid() {
        List<String> formats =given()
                .when()
                .get("/formats")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().response()
                .jsonPath()
                .getList("formats");

        assertEquals(23, formats.size());
    }
}
