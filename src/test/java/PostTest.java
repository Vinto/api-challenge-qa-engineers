import io.restassured.http.ContentType;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.testng.annotations.Test;

public class PostTest {

    @Test
    public void getPost() {
        given()
                .log().all()
                .when()
                .get("https://jsonplaceholder.typicode.com/posts/1")
                .then()
                .log().body()
                .assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(1));
    }
}
