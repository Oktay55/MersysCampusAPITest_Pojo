import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class CitizenshipFunctionTest {

    private RequestSpecification reqSpec;
    private Cookies cookies;
    private String citizen_id;

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
    public void createCitizenShipTest() {
        HashMap<String, String> reqBody = new HashMap<>();
        reqBody.put("name", "ToffeePeanut");
        reqBody.put("shortName", "TP");


        citizen_id =  given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(reqBody)
                .when()
                .post("/school-service/api/citizenships")
                .then()
                .log().body()
                .statusCode(201)
                .body("name", equalTo(reqBody.get("name")))
                .body("shortName", equalTo(reqBody.get("shortName")))
                .extract().jsonPath().getString("id");

    }
    @Test(priority = 4)
    public void getCitizenShipTest() {
        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .get("/school-service/api/citizenships/" + citizen_id)
                .then()
                .statusCode(200);
    }
    @Test(priority = 5)
    public void createCitizenShipNegativeTest() {
        HashMap<String, String> reqBody = new HashMap<>();
        reqBody.put("name", "ToffeePeanut");
        reqBody.put("shortName", "TP");

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(reqBody)
                .when()
                .post("/school-service/api/citizenships")
                .then()
                .log().body()
                .statusCode(400);

    }
    @Test(priority = 6)
    public void editCitizenShipTest() {
        HashMap<String, String> updateReqBody = new HashMap<>();
        updateReqBody.put("id", citizen_id);
        updateReqBody.put("name", "ToffeePeanut");
        updateReqBody.put("shortName", "TP");

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(updateReqBody)
                .when()
                .put("/school-service/api/citizenships")
                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(updateReqBody.get("name")))
                .body("shortName", equalTo(updateReqBody.get("shortName")));

    }
    @Test(priority = 7)
    public void deleteCitizenShipTest() {
        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .delete("/school-service/api/citizenships/" + citizen_id)
                .then()
                .log().body()
                .statusCode(200);
    }
    @Test(priority = 8)
    public void getCitizenShipNegativeTest() {
        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .get("/school-service/api/citizenships/" + citizen_id)
                .then()
                .statusCode(400);
    }
    @Test(priority = 9)
    public void deleteCitizenShipNegativeTest() {
        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .delete("/school-service/api/citizenships/" + citizen_id)
                .then()
                .log().body()
                .statusCode(400);
    }

}

