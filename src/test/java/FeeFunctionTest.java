import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class FeeFunctionTest {

    private RequestSpecification reqSpec;
    private Cookies cookies;
    private String fee_id;

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
    public void createFeeTest() {
        HashMap<String, String> reqBody = new HashMap<>();
        reqBody.put("name", "Hamburger");
        reqBody.put("code", "139");
        reqBody.put("active", "true");
        reqBody.put("priority", "553344");

        fee_id =  given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(reqBody)
                .when()
                .post("/school-service/api/fee-types")
                .then()
                .log().body()
                .statusCode(201)
                .body("description", equalTo(reqBody.get("description")))
                .body("code", equalTo(reqBody.get("code")))
                .extract().jsonPath().getString("id");

    }
    @Test(priority = 4)
    public void getFeeTest() {
        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .get("/school-service/api/fee-types/" + fee_id)
                .then()
                .statusCode(200);
    }
    @Test(priority = 5)
    public void createFeeNegativeTest() {
        HashMap<String, String> reqBody = new HashMap<>();
        reqBody.put("name", "Hamburger");
        reqBody.put("code", "139");
        reqBody.put("active", "true");
        reqBody.put("priority", "553344");

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
    public void editFeeTest() {
        HashMap<String, String> updateReqBody = new HashMap<>();
        updateReqBody.put("id", fee_id);
        updateReqBody.put("name", "Wendy");
        updateReqBody.put("code", "1067");
        updateReqBody.put("active", "true");
        updateReqBody.put("priority", "108");

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(updateReqBody)
                .when()
                .put("/school-service/api/fee-types")
                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(updateReqBody.get("name")))
                .body("code", equalTo(updateReqBody.get("code")));

    }
    @Test(priority = 7)
    public void deleteFeeTest() {
        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .delete("/school-service/api/fee-types/" + fee_id)
                .then()
                .log().body()
                .statusCode(200);
    }
    @Test(priority = 8)
    public void getFeeNegativeTest() {
        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .get("/school-service/api/fee-types/" + fee_id)
                .then()
                .statusCode(400);
    }
    @Test(priority = 9)
    public void deleteFeeNegativeTest() {
        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .delete("/school-service/api/fee-types/" + fee_id)
                .then()
                .log().body()
                .statusCode(400);
    }

}
