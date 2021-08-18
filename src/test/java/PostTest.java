import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
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

    @DataProvider()
    public static Object[][] usernameDetails() {
        return new Object[][]{
                {"Delphine"},
        };
    }

    @Test(dataProvider = "usernameDetails")
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
}
