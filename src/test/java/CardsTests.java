import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CardsTests {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://api.magicthegathering.io/v1";
    }

    @Test
    public void getAllCardsWithHeaderCheck() {
        given()
                .when()
                .get("/cards")
                .then()
                .statusCode(200)
                .header("Content-Type", "application/json; charset=utf-8");
    }

    @Test
    public void getAllCards() {
        given()
                .when()
                .get("/cards")
                .then()
                .statusCode(200);
    }

    @Test
    public void getCardById() {
        String cardId = "654742";
        given()
                .pathParam("id", cardId)
                .when()
                .get("/cards/{id}")
                .then()
                .statusCode(200);
    }

    @Test
    public void getCardByInvalidId() {
        given()
                .pathParam("id", "invalid-id")
                .when()
                .get("/cards/{id}")
                .then()
                .statusCode(404);
    }

    @ParameterizedTest
    @CsvSource({
            // ID, Name, Types
            "654742, Windbrisk Heights, Land",
            "84672, Flame Wave, Sorcery",
            "777, Wall of Ice, Creature",
    })
    public void cardTestWithParams(String id, String name, String type) {
        given().pathParam("cardID", id)
                .when().get("/cards/{cardID}")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("card.name", is(name));

        given().pathParam("cardID", id)
                .when().get("/cards/{cardID}")
                .then()
                .statusCode(200)
                .body("card.types", hasItem(type));
    }

    @ParameterizedTest
    @CsvSource({
            // ID, Name, Types
            "Windbrisk Heights",
            "Flame Wave",
            "Wall of Ice",
    })
    public void getCardByName(String name) {
        given()
                .queryParam("name", name)
                .when().get("/cards")
                .then()
                .statusCode(200);
    }

    @ParameterizedTest
    @CsvSource({
            // Name, Foreign name, Language
            "Windbrisk Heights, 風立ての高地, Japanese",
            "Flame Wave, Ola de llamas, Spanish",
    })
    public void getAllCardsByForeignName(String originalName, String foreignName, String language) {
        given()
                .queryParam("name", foreignName)
                .queryParam("language", language)
                .when().get("/cards")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().response()
                .then().body("cards.name", hasItem(originalName));
    }

    @ParameterizedTest
    @CsvSource({
            //Foreign name, Language
            "Null, Null",
    })
    public void getCardsByInvalidForeignName(String foreignName, String language) {
        given()
                .queryParam("name", foreignName)
                .queryParam("language", language)
                .when().get("/cards")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().response()
                .then().body("cards", hasSize(0));
    }

    @Test
    public void testPagination() {
        int pageSize = 100;
        List<Object> cards  = given()
                .queryParam("page", 3)
                .queryParam("pageSize", pageSize)
                .when().get("/cards")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().response()
                .jsonPath()
                .getList("cards");

        assertEquals(cards.size(), pageSize);
    }

    @Test
    public void testSorting() {
        List<String> cardNames  = given()
                .queryParam("orderBy", "name")
                .when().get("/cards")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().response()
                .jsonPath()
                .getList("cards.name");
    }
}
