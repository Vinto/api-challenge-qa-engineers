import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class PostTest {

    private static RequestSpecification requestSpecification;
    private static ResponseSpecification responseSpecification;

    String baseUrl = ReadDataProperties.getInstance().getUrl();

    @BeforeClass
    public void createRequestSpecification() {
        requestSpecification = new RequestSpecBuilder()
                .setBaseUri(baseUrl).build();
    }

    @BeforeClass
    public void createResponseSpecification() {
        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .build();
    }

    @Test(dataProvider = "usernameDetails", dataProviderClass = DataProviderTestData.class)
    public void validateEmailAddresses(String username) {

        // Search for Delphine
        given()
                .spec(requestSpecification)
                .log().all()
                .when()
                .get("users?username=" + username)
                .then()
                .spec(responseSpecification)
                .log().body();

        // Extract id which is the userId
        int userId = given()
                .spec(requestSpecification)
                .when()
                .get("users?username=" + username)
                .then()
                .extract()
                .path("[0].id");

        // Fetch posts written by user with userId in response
        given()
                .spec(requestSpecification)
                .log().all()
                .when()
                .get("posts?userId=" + userId)
                .then()
                .spec(responseSpecification)
                .log().body();
    }

    @Test
    public void invalidUserId() {
        given()
                .spec(requestSpecification)
                .when()
                .get("users?userId=x.x.x.x.x")
                .then()
                .spec(responseSpecification)
                .log().body();
    }

    @Test
    public void invalidUsername() {
        given()
                .spec(requestSpecification)
                .when()
                .get("users?username=x.x.x.x.x")
                .then()
                .spec(responseSpecification)
                .log().body();
    }

    @Test
    public void invalidPostId() {
        given()
                .spec(requestSpecification)
                .when()
                .get("posts/x.x.x.x.x")
                .then()
                .log().body();
    }
}
