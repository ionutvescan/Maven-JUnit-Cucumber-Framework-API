package stepDefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import pojoClasses.LoginResponse;
import resources.TestDataBuild;
import resources.Utilities;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class EcommerceStepDefinition extends Utilities {

    TestDataBuild testDataBuild = new TestDataBuild();
    LoginResponse loginResponse = new LoginResponse();
    String token;
    String userId;
    String productId;
    Response addProductResponse;
    Response responseAddOrder;
    Response deleteProductResponse;

    @Given("user is logging on the website with email {string} and password {string}")
    public void userIsLoggingOnTheWebsiteWithEmailAndPassword(String email, String password) throws IOException {
        RequestSpecification reqLogin = given().relaxedHTTPSValidation().spec(reqSpecific())
                .body(testDataBuild.loginRequest(email, password));
        loginResponse = reqLogin.when().post(getGlobalValue("loginAPI"))
                .then().extract().response().as(LoginResponse.class);
        token = loginResponse.getToken();
        userId = loginResponse.getUserId();
    }

    @Then("product is added by the user")
    public void productIsAddedByTheUser() throws IOException {
        RequestSpecification reqAddProduct = given().spec(reqSpecificProd(token, userId));
        addProductResponse = reqAddProduct.when().post(getGlobalValue("addProductAPI"))
                .then().extract().response();
        productId = getJsonPath(addProductResponse, "productId");
    }

    @And("{string} in body is {string}")
    public void inBodyIs(String value, String expectedValue) {
        assertEquals(getJsonPath(addProductResponse, value), expectedValue);
    }

    @Then("user create order")
    public void userCreateOrder() throws IOException {

        RequestSpecification createOrderReq = given().spec(reqSpecificOrder(token))
                .body(testDataBuild.getOrder(productId));
        responseAddOrder = createOrderReq.when().post(getGlobalValue("createOrderAPI"))
                .then().extract().response();
    }

    @And("{string} is the {string} in the body response")
    public void isTheInTheBodyResponse(String expectedValue, String value) {
        assertEquals(getJsonPath(responseAddOrder, value), expectedValue);
    }

    @Then("user delete product ordered")
    public void userDeleteProductOrdered() throws IOException {
        RequestSpecification deleteProductReq = given().log().all().spec(reqSpecificOrder(token))
                .pathParam("productId", productId);
        deleteProductResponse = deleteProductReq.when().delete(getGlobalValue("deleteProductOrderedAPI"))
                .then().log().all().extract().response();
    }

    @And("{string} is the {string} displayed")
    public void isTheDisplayed(String expectedValue, String value) {
        assertEquals(getJsonPath(deleteProductResponse, value), expectedValue);
    }
}
