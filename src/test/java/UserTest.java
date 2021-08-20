import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;

public class UserTest {

    private static RequestSpecification requestSpecification;

    String baseUrl = ReadDataProperties.getInstance().getUrl();

    @BeforeClass
    public void createRequestSpecification() {
        requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(baseUrl).build();
    }

    /**
     * Fetching a user with an invalid userId
     */
    @Test
    public void invalidUserId() {
        Response response = given()
                .spec(requestSpecification)
                .when()
                .get("users?id=x.x.x.x.x");

        Assert.assertEquals(200, response.statusCode());

        response.then().body("", hasSize(0));
    }

    /**
     * Fetching a user with an invalid username
     */
    @Test
    public void invalidUsername() {
        Response response = given()
                .spec(requestSpecification)
                .when()
                .get("users?username=x.x.x.x.x");

        Assert.assertEquals(200, response.statusCode());

        response.then().body("", hasSize(0));
    }

    /**
     * Fetch all todos for user given userId 1
     */
    @Test
    public void getTodosForUser() {
        Response response = given()
                .spec(requestSpecification)
                .when()
                .get("users/1/todos")
                .then().extract().response();

        Assert.assertEquals(200, response.statusCode());

        Object object = com.jayway.jsonpath.JsonPath.read("{\n" +
                "        \"userId\": 1,\n" +
                "        \"id\": 1,\n" +
                "        \"title\": \"delectus aut autem\",\n" +
                "        \"completed\": false\n" +
                "    }", "$");

        response.then().body("$", hasItem(object));
    }

    /**
     * Fetch all albums for user with userId 1
     */
    @Test
    public void getUserAlbums() {
        Response response = given()
                .spec(requestSpecification)
                .when()
                .get("users/1/albums")
                .then().extract().response();

        Assert.assertEquals(200, response.statusCode());

        Object object = com.jayway.jsonpath.JsonPath.read("{\n" +
                "        \"userId\": 1,\n" +
                "        \"id\": 1,\n" +
                "        \"title\": \"quidem molestiae enim\"\n" +
                "    }", "$");

        response.then().body("$", hasItem(object));
    }
}
