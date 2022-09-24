import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class DiscountFunctionTest {

    private RequestSpecification reqSpec;
    private Cookies cookies;
    private String discount_id;


    @BeforeClass
    public void setup() {

        RestAssured.baseURI = "https://demo.mersys.io";

        reqSpec = given()
                .log().body()
                .contentType(ContentType.JSON);
    }
    @Test(priority = 1)
    public void loginNegativeTest() {

        HashMap<String, String> credentials = new HashMap<>();
        credentials.put("username", "richfield.org");
        credentials.put("password", "Richfield2021");
        credentials.put("rememberMe", "true");

        given()
                .spec(reqSpec)
                .body(credentials)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(401);
    }

    @Test(priority = 2)
    public void loginTest() {

        HashMap<String, String> credentials = new HashMap<>();
        credentials.put("username", "richfield.edu");
        credentials.put("password", "Richfield2020!");
        credentials.put("rememberMe", "true");

        cookies = given()
                .spec(reqSpec)
                .body(credentials)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract().detailedCookies();

    }
    @Test(priority = 3)
    public void createDiscountTest() {
        HashMap<String, String> reqBody = new HashMap<>();
        reqBody.put("description", "Hamburger");
        reqBody.put("code", "139");
        reqBody.put("active", "true");

        discount_id =  given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(reqBody)
                .when()
                .post("/school-service/api/discounts")
                .then()
                .log().body()
                .statusCode(201)
                .body("description", equalTo(reqBody.get("description")))
                .body("code", equalTo(reqBody.get("code")))
                .extract().jsonPath().getString("id");

    }
    @Test(priority = 4)
    public void getDiscountTest() {
        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .get("/school-service/api/discounts/" + discount_id)
                .then()
                .statusCode(200);
    }
    @Test(priority = 5)
    public void createDiscountNegativeTest() {
        HashMap<String, String> reqBody = new HashMap<>();
        reqBody.put("description", "Hamburger");
        reqBody.put("code", "139");
        reqBody.put("active", "true");

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(reqBody)
                .when()
                .post("/school-service/api/discounts")
                .then()
                .log().body()
                .statusCode(400);

    }
    @Test(priority = 6)
    public void editDiscountTest() {
        HashMap<String, String> updateReqBody = new HashMap<>();
        updateReqBody.put("id", discount_id);
        updateReqBody.put("description", "Pizza");
        updateReqBody.put("code", "454");
        updateReqBody.put("active", "true");

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(updateReqBody)
                .when()
                .put("/school-service/api/discounts")
                .then()
                .log().body()
                .statusCode(200)
                .body("description", equalTo(updateReqBody.get("description")));
    }
    @Test(priority = 7)
    public void deleteDiscountTest() {
        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .delete("/school-service/api/discounts/" + discount_id)
                .then()
                .log().body()
                .statusCode(200);
    }
    @Test(priority = 8)
    public void getDiscountNegativeTest() {
        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .get("/school-service/api/discounts/" + discount_id)
                .then()
                .statusCode(400);
    }
    @Test(priority = 9)
    public void deleteDiscountNegativeTest() {
        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .delete("/school-service/api/discounts/" + discount_id)
                .then()
                .log().body()
                .statusCode(400);
    }


}
