package stepDefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import pojoClasses.LoginResponse;
import resources.APIResources;
import resources.TestDataBuild;
import resources.Utilities;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class StepDefinition extends Utilities {
    RequestSpecification requestSpecification;
    TestDataBuild testDataBuild = new TestDataBuild();
    Response response;
    static String place_id;

    @Given("add place payload with the following:{string},{string},{string}")
    public void addPlacePayloadWithTheFollowing(String name, String address, String website) throws IOException {
      requestSpecification = given().spec(requestSpecification())
              .body(testDataBuild.addPlacePayload(name, address, website));
    }

    @When("user calls {string} with {string} http request method")
    public void userCallsWithHttpRequestMethod(String resource, String method) {
        APIResources apiResources = APIResources.valueOf(resource);

        if(method.equalsIgnoreCase("POST"))
           response = requestSpecification.when().post(apiResources.getResource());
        else if(method.equalsIgnoreCase("GET"))
           response = requestSpecification.when().get(apiResources.getResource());
        else if(method.equalsIgnoreCase("PUT"))
            response = requestSpecification.when().put(apiResources.getResource());
    }

    @Then("the API call got success with status code {int}")
    public void theAPICallGotSuccessWithStatusCode(int arg0) {
        assertEquals(response.getStatusCode(), 200);
    }

    @And("{string} in response body is {string}")
    public void inResponseBodyIs(String value, String expectedValue) {
        assertEquals(getJsonPath(response, value), expectedValue);
    }
    @Then("using {string} verify {string} with the help of place_id")
    public void usingVerifyWithTheHelpOfPlace_id(String resource, String expectedName) throws IOException {
        place_id = getJsonPath(response,"place_id");
        requestSpecification = given().spec(requestSpecification()).queryParam("place_id", place_id);
        userCallsWithHttpRequestMethod(resource,"GET");
        String actualName = getJsonPath(response,"name");
        assertEquals(actualName,expectedName);
    }

    @Then("using {string} change payload")
    public void usingChangePayload(String resource) throws IOException {
        requestSpecification = given().spec(requestSpecification())
                .body(testDataBuild.updatePlacePayload(place_id, "BestWestern", "767 Bucharest","htpps://bw.com" ));
        userCallsWithHttpRequestMethod(resource,"PUT");
    }

    @Given("DeletePlace Payload")
    public void deleteplacePayload() throws IOException {
        requestSpecification = given().spec(requestSpecification()).body(testDataBuild.deletePlacePayload(place_id));
    }

//    --------------------------------------------------------------------------------------------------------------------
//  Ecommerce stepDefinitions

    LoginResponse loginResponse = new LoginResponse();
    String token;
    String userId;
    String productId;
    Response addProductResponse;
    Response responseAddOrder;
    Response deleteProductResponse;

    @Given("user is logging on the website with email {string} and password {string}")
    public void userIsLoggingOnTheWebsiteWithEmailAndPassword(String email, String password) throws IOException {
        RequestSpecification reqLogin = given().relaxedHTTPSValidation().spec(reqSpecific()).body(testDataBuild.loginRequest(email, password));
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

        RequestSpecification createOrderReq = given().spec(reqSpecificOrder(token)).body(testDataBuild.getOrder(productId));
        responseAddOrder = createOrderReq.when().post(getGlobalValue("createOrderAPI"))
                .then().extract().response();
    }

    @And("{string} is the {string} in the body response")
    public void isTheInTheBodyResponse(String expectedValue, String value) {
        assertEquals(getJsonPath(responseAddOrder, value), expectedValue);
    }

    @Then("user delete product ordered")
    public void userDeleteProductOrdered() throws IOException {
        RequestSpecification deleteProductReq = given().log().all().spec(reqSpecificOrder(token)).pathParam("productId", productId);
        deleteProductResponse = deleteProductReq.when().delete(getGlobalValue("deleteProductOrderedAPI"))
                .then().log().all().extract().response();
    }

    @And("{string} is the {string} displayed")
    public void isTheDisplayed(String expectedValue, String value) {
        assertEquals(getJsonPath(deleteProductResponse, value), expectedValue);
    }
}

