import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SetsTests {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://api.magicthegathering.io/v1";
    }

    @Test
    public void getAllSets() {
        given()
                .when()
                .get("/sets")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("sets", not(empty()));
    }

    @Test
    public void getSetByCode() {
        String setCode = "2X2";

        given()
                .pathParam("code", setCode)
                .when()
                .get("/sets/{code}")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("set.code", equalTo(setCode));
    }

    @Test
    public void getSetByInvalidCode() {
        given()
                .pathParam("code", "invalid-code")
                .when()
                .get("/sets/{code}")
                .then()
                .statusCode(404);
    }

    @Test
    public void getBoostersBySetId() {
        given()
                .pathParam("id", "2ED")
                .when()
                .get("/sets/{id}/booster")
                .then()
                .statusCode(200);
    }

    @Test
     public void checkIfSetHasBoosters() {
        List<String> cardNames = given()
                .pathParam("id", "2ED")
                .when()
                .get("/sets/{id}/booster")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().response()
                .jsonPath()
                .getList("cards.name");

        assertFalse(cardNames.isEmpty());

        given()
                .pathParam("id", "10E")
                .when()
                .get("/sets/{id}/booster")
                .then()
                .statusCode(400);
    }
}