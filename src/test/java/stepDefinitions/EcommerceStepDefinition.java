package stepDefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import pojoClasses.LoginResponse;
import resources.TestData;
import resources.Utilities;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class EcommerceStepDefinition extends Utilities {

    TestData testData = new TestData();
    String token;
    String userId;
    String productId;
    Response addProductResponse;
    Response responseAddOrder;
    Response deleteProductResponse;

    @Given("user is logging on the website with email {string} and password {string}")
    public void userIsLoggingOnTheWebsiteWithEmailAndPassword(String email, String password) throws IOException {

        RequestSpecification reqLogin = given().relaxedHTTPSValidation().spec(loginRequestSpecification())
                .body(testData.loginRequest(email, password));

        LoginResponse loginResponse = reqLogin.when().post(getGlobalValue("loginAPI"))
                .then().extract().response().as(LoginResponse.class);

        token = loginResponse.getToken();
        userId = loginResponse.getUserId();
    }

    @Then("product is added by the user")
    public void productIsAddedByTheUser() throws IOException {

        RequestSpecification reqAddProduct = given().spec(addProductRequestSpecification(token, userId));

        addProductResponse = reqAddProduct.when().post(getGlobalValue("addProductAPI"))
                .then().extract().response();

        productId = getJsonPath(addProductResponse, "productId");
    }

    @And("Message {string} in body is {string}")
    public void messageInBodyIs(String value, String expectedValue) {

        assertEquals(getJsonPath(addProductResponse, value), expectedValue);
    }

    @Then("user create order")
    public void userCreateOrder() throws IOException {

        RequestSpecification createOrderReq = given().spec(createOrderRequestSpecification(token))
                .body(testData.getOrder(productId));

        responseAddOrder = createOrderReq.when().post(getGlobalValue("createOrderAPI"))
                .then().extract().response();
    }

    @And("{string} is the message {string} in the body response")
    public void isTheMessageInTheBodyResponse(String expectedValue, String value) {

        assertEquals(getJsonPath(responseAddOrder, value), expectedValue);
    }

    @Then("user delete product ordered")
    public void userDeleteProductOrdered() throws IOException {

        RequestSpecification deleteProductReq = given().spec(createOrderRequestSpecification(token))
                .pathParam("productId", productId);

        deleteProductResponse = deleteProductReq.when().delete(getGlobalValue("deleteProductOrderedAPI"))
                .then().extract().response();
    }

    @And("{string} is the message {string} displayed")
    public void isTheMessageDisplayed(String expectedValue, String value) {

        assertEquals(getJsonPath(deleteProductResponse, value), expectedValue);
    }
}
