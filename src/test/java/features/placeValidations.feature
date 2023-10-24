Feature: Validating Place API's
  @CreateGetUpdateDeletePlace
  Scenario Outline: Verify if place is being successfully added, update and deleted
    Given add place payload with the following:"<name>","<address>","<website>"
    When user calls "addPlaceAPI" with "POST" http request method
    Then the API call got success with status code 200
    And "status" in response body is "OK"
    And "scope" in response body is "APP"
    Then using "getPlaceAPI" verify "<name>" with the help of place_id
    And using "updatePlaceAPI" change payload
    Then using "deletePlaceAPI" delete place

  Examples:
    |name        |address        |website                 |
    |Marriott    |777 New Work   |https://nycmarriott.com |
    |Ramada      |1678 Chicago   |https://chiramada.com   |
    |Radisson Blu|27 Cluj-Napoca |https://radissoncluj.com|



