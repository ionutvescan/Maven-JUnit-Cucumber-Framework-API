Feature: Validating Ecommerce API's
  Scenario Outline: Verify if order is created
    Given user is logging on the website with email "<email>" and password "<password>"
    Then product is added by the user
    And "message" in body is "Product Added Successfully"
    Then user create order
    And "Order Placed Successfully" is the "message" in the body response
    Then user delete product ordered
    And  "Product Deleted Successfully" is the "message" displayed

  Examples:
    |email               |password|
    |i.vescan96@yahoo.com|dinamo  |
