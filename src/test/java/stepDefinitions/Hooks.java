package stepDefinitions;

import io.cucumber.java.Before;
import stepDefinitions.PlaceStepDefinition;

import java.io.IOException;

public class Hooks {

	@Before("@CreateGetUpdateDeletePlace")
	public void beforeScenario() throws IOException {
		PlaceStepDefinition placeStepDefinition = new PlaceStepDefinition();
		if(PlaceStepDefinition.place_id == null) {
			placeStepDefinition.addPlacePayloadWithTheFollowing("Hilton","Bucharest", "https://hilton.com");
			placeStepDefinition.userCallsWithHttpRequestMethod("addPlaceAPI", "POST");
			placeStepDefinition.usingVerifyWithTheHelpOfPlace_id("getPlaceAPI", "Hilton");
		}
	}
}
