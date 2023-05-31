Feature: Validating Place API's
  @AddGetUpdatePlace
  Scenario Outline: Verify if place is being successfully added using addPlaceAPI
    Given add place payload with the following:"<name>","<address>","<website>"
    When user calls "addPlaceAPI" with "POST" http request method
    Then the API call got success with status code 200
    And "status" in response body is "OK"
    And "scope" in response body is "APP"
    Then using "getPlaceAPI" verify "<name>" with the help of place_id
    Then using "updatePlaceAPI" change payload

  Examples:
    |name        |address        |website                 |
    |Marriott    |777 New Work   |https://nycmarriott.com |
    |Ramada      |1678 Chicago   |https://chiramada.com   |
    |Radisson Blu|27 Cluj-Napoca |https://radissoncluj.com|

  @DeletePlace
  Scenario: Verify if Delete Place functionality is working

    Given DeletePlace Payload
    When user calls "deletePlaceAPI" with "POST" http request method
    Then the API call got success with status code 200
    And "status" in response body is "OK"



