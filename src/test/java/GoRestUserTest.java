import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class GoRestUserTest {

    private RequestSpecification reqSpec;
    private HashMap<String, String> requestBody;
    private Object userId;

    @BeforeClass
    public void setup() {

        RestAssured.baseURI = "https://gorest.co.in"; // baseUri

        reqSpec = given()  // request specification
                .log().uri()
                .header("Authorization", "Bearer 3552b21c78056c29958ec50112cc2afacfe0d9da2826aa3865018da59883d9f6")
                .contentType(ContentType.JSON);

        requestBody = new HashMap<>();
        requestBody.put("name", "Javier Santos");
        requestBody.put("email", "Jard2d@gmail.com");
        requestBody.put("gender", "male");
        requestBody.put("status", "active");

    }

    @Test(priority = 1)
    public void createUserTest() {

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

    @Test(priority = 2)
    public void editUserTest() {

        HashMap<String, String> editUser = new HashMap<>();
        editUser.put("name", "Mario Tony");

        given()
                .spec(reqSpec)
                .body(editUser)
                .when()
                .put("/public/v2/users/" + userId)
                .then()
                .log().body()
                .statusCode(200);

    }

    @Test(priority = 3)
    public void deleteUserTest() {

        given()
                .spec(reqSpec)
                .when()
                .delete("/public/v2/users/" + userId)
                .then()
                .log().body()
                .statusCode(204);
    }



}
