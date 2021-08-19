import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import org.apache.commons.validator.routines.EmailValidator;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

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

    /**
     * Return a userId given a valid username
     * @param username
     * @return uId
     */
    private int getUserId(String username) {
        Response response = given()
                .spec(requestSpecification)
                .when()
                .get("users?username=" + username);

        Assert.assertEquals(200, response.statusCode());

        int uId = response.then().extract().path("[0].id");

        return uId;
    }

    /**
     * Fetch posts of a user, then validate email addresses in the
     * comments of the posts
     * @param username
     */
    @Test(dataProvider = "usernameDetails", dataProviderClass = DataProviderTestData.class)
    public void validateEmailAddresses(String username) {
        int userId = getUserId(username);

        /* Fetch posts written by user with userId in response */
        Response response = given()
                .spec(requestSpecification)
                .when()
                .get("posts?userId=" + userId);

        /* Get a list of ids of the posts */
        List postIds = response.jsonPath().getList("id");

        SoftAssert softAssert = new SoftAssert();

        for(Object id: postIds) {
            response = given()
                    .spec(requestSpecification)
                    .when()
                    .get("posts/" + id + "/comments");

            /* Get a list of emails in the comments of the posts */
            List emails = response.jsonPath().getList("email");

            System.out.println(emails);

            for(Object email: emails) {
                /* Validate email addresses */
                boolean valid = EmailValidator.getInstance().isValid(email.toString());
                System.out.println(valid);

                /* Continue to the next step even after encountering a failure */
                softAssert.assertTrue(valid, email + " from post " + id + " is invalid");
            }
        }
        softAssert.assertAll();
    }

    /**
     * Fetching a user with an invalid userId
     */
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

    /**
     * Fetching a user with an invalid username
     */
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

    /**
     * Fetching a user with an invalid postId
     */
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
