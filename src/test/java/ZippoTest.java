import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ZippoTest {

    @BeforeClass
    public void setup() {

        RestAssured.baseURI = "https://api.zippopotam.us";
    }

    @Test
    public void test() {

        given()
                .when()
                .then();

    }

    @Test
    public void checkingStatusCode() {

        given()
                .when()
                .get("/us/08075")
                .then()
                .statusCode(200);


    }

    @Test
    public void loggingRequestDetails() {

        given()
                .log().all()
                .when()
                .get("/us/08075")
                .then()
                .statusCode(200);

    }

    @Test
    public void loggingResponseDetails() {

        given()
                .when()
                .get("/us/08075")
                .then()
                .log().all()
                .statusCode(200);


    }

    @Test
    public void checkContentType() {

        given()
                .when()
                .get("/us/08075")
                .then()
                .contentType(ContentType.JSON)
                .statusCode(200);

    }

    @Test
    public void validateCountryTest() {

        given()
                .when()
                .get("/us/08075")
                .then()
                .body("country", equalTo("United States"))
                .statusCode(200);

    }

    @Test
    public void validateCountryAbv() {

        given()
                .when()
                .get("/us/08075")
                .then()
                .body("'country abbreviation'", equalTo("US"))
                .statusCode(200);

    }

    @Test
    public void validateStateInfo() {

        given()
                .when()
                .get("/us/08075")
                .then()
                .body("places[0].state", equalTo("New Jersey"))
                .statusCode(200);

    }

    @Test
    public void pathParametersTest() {

        String country = "us";
        String zipcode = "08075";

        given()
                .pathParam("country", country)
                .pathParam("zipcode", zipcode)
                .when()
                .get("/{country}/{zipcode}")
                .then()
                .statusCode(200);
    }

    @Test
    public void queryParameters() {

        String gender = "female";
        String status = "active";

        given()
                .param("gender", gender)
                .param("status", status)
                .when()
                .get("https://gorest.co.in/public/v2/users")
                .then()
                .statusCode(200)
                .log().body();


    }

    @Test
    public void extractValueTest() {

        Object countryInfo = given()
                .when()
                .get("/us/08075")
                .then()
                .extract().path("country");

        System.out.println(countryInfo);


    }
}
