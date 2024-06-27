import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class TypesTests {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://api.magicthegathering.io/v1";
    }

    @Test
    public void getAllTypes() {
        given()
                .when()
                .get("/types")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("types", not(empty()))
                .body("types", hasItem("Artifact"));
    }

    @Test
    public void getAllSubtypes() {
        given()
                .when()
                .get("/subtypes")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("subtypes", not(empty()))
                .body("subtypes", hasItem("Angel"));
    }

    @Test
    public void getAllSupertypes() {
        given()
                .when()
                .get("/supertypes")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("supertypes", not(empty()))
                .body("supertypes", hasItem("Legendary"))
                .body("supertypes", hasItem("Ongoing"));
    }
}