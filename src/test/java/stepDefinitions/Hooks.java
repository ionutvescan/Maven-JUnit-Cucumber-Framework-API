package stepDefinitions;

import io.cucumber.java.Before;

import java.io.IOException;

public class Hooks {

	@Before("@DeletePlace")
	public void beforeScenario() throws IOException {
		StepDefinition stepDefinition = new StepDefinition();
		if(StepDefinition.place_id == null) {
			stepDefinition.addPlacePayloadWithTheFollowing("Hilton","Bucharest", "https://hilton.com");
			stepDefinition.userCallsWithHttpRequestMethod("addPlaceAPI", "POST");
			stepDefinition.usingVerifyWithTheHelpOfPlace_id("getPlaceAPI", "Hilton");
		}
	}
}
