import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.*;

public class GoRestHomeworkTest {

    private RequestSpecification reqSpec;

    private HashMap<String, String> requestBody;

    private Object userId;


    @BeforeClass
    public void setup() {

        RestAssured.baseURI = "https://gorest.co.in";

        reqSpec = given()
                .log().uri()
                .header("Authorization", "Bearer 3552b21c78056c29958ec50112cc2afacfe0d9da2826aa3865018da59883d9f6")
                .contentType(ContentType.JSON);
        requestBody = new HashMap<>();
        requestBody.put("name", "Allen Woods");
        requestBody.put("email", "allenwoods@uefa.com");
        requestBody.put("gender", "male");
        requestBody.put("status", "active");

    }

    @Test
    public void createPositiveUserTest() {

        userId = given()
                .spec(reqSpec)
                .body(requestBody)
                .when()
                .post("/public/v2/users")
                .then()
                .log().body()
                .statusCode(201)
                .extract().path("id");

    }

    @Test(dependsOnMethods = "createPositiveUserTest")
    public void createUserNegativeTest() {

        given()
                .spec(reqSpec)
                .body(requestBody)
                .when()
                .post("/public/v2/users")
                .then()
                .statusCode(422);

    }

    @Test(dependsOnMethods = "createUserNegativeTest")
    public void getUserInformationTest() {

        given()
                .spec(reqSpec)
                .when()
                .get("/public/v2/users" + userId)
                .then()
                .log().body();

    }

    @Test(dependsOnMethods = "getUserInformationTest")
    public void editUserEmail() {

        HashMap<String, String> editUserEmail = new HashMap<>();
        editUserEmail.put("email", "troublemaker@uefa.com");

        given()
                .spec(reqSpec)
                .body(editUserEmail)
                .when()
                .put("/public/v2/users/" + userId)
                .then()
                .statusCode(200);

    }

    @Test(dependsOnMethods = "editUserEmail")
    public void deleteUserPositiveTest() {
        given()
                .spec(reqSpec)
                .when()
                .delete("/public/v2/users/" + userId)
                .then()
                .statusCode(204);
    }

    @Test(dependsOnMethods = "deleteUserPositiveTest")
    public void deleteUserNegativeTest() {

        given()
                .spec(reqSpec)
                .when()
                .delete("/public/v2/users/" + userId)
                .then()
                .statusCode(404);
    }


}
