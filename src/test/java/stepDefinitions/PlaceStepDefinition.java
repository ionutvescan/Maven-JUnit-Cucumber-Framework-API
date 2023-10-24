package stepDefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import resources.APIResources;
import resources.TestDataBuild;
import resources.Utilities;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class PlaceStepDefinition extends Utilities {
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
           response = requestSpecification.when().post(apiResources.getResource())
                   .then().spec(responseSpecification()).extract().response();
        else if(method.equalsIgnoreCase("GET"))
           response = requestSpecification.when().get(apiResources.getResource())
                   .then().spec(responseSpecification()).extract().response();
        else if(method.equalsIgnoreCase("PUT"))
            response = requestSpecification.when().put(apiResources.getResource())
                    .then().spec(responseSpecification()).extract().response();
    }

    @Then("the API call got success with status code {int}")
    public void theAPICallGotSuccessWithStatusCode(int code) {
        assertEquals(response.getStatusCode(), code);
    }

    @And("{string} in response body is {string}")
    public void inResponseBodyIs(String key, String expectedValue) {
        assertEquals(getJsonPath(response, key), expectedValue);
    }

    @Then("using {string} verify {string} with the help of place_id")
    public void usingVerifyWithTheHelpOfPlace_id(String resource, String expectedName) throws IOException {
        place_id = getJsonPath(response,"place_id");
        requestSpecification = given().spec(requestSpecification()).queryParam("place_id", place_id);
        userCallsWithHttpRequestMethod(resource,"GET");
        String actualName = getJsonPath(response,"name");
        assertEquals(actualName,expectedName);
    }

    @And("using {string} change payload")
    public void usingChangePayload(String resource) throws IOException {
        requestSpecification = given().spec(requestSpecification())
                .body(testDataBuild.updatePlacePayload(place_id,
                        "BestWestern", "767 Bucharest","htpps://bw.com" ));
        userCallsWithHttpRequestMethod(resource,"PUT");
    }

    @Then("using {string} delete place")
    public void usingDeletePlace(String resource) throws IOException {
        requestSpecification = given().spec(requestSpecification()).body(testDataBuild.deletePlacePayload(place_id));
        userCallsWithHttpRequestMethod(resource, "POST");
        theAPICallGotSuccessWithStatusCode(200);
        inResponseBodyIs("status","OK");
    }
}

