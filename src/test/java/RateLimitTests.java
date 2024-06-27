import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class RateLimitTests {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://api.magicthegathering.io/v1";
    }

    @Test
    public void testRateLimitExceeded() {
        boolean rateLimitExceeded = false;

        Response response = RestAssured.get("/cards");
        int statusCode = response.getStatusCode();

        if (statusCode == 403) {
            rateLimitExceeded = true;
        }

        assertFalse(rateLimitExceeded);
    }
}
