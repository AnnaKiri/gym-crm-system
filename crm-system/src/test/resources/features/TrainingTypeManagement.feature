Feature: Training Type Management

  Scenario: Retrieve the list of training types
    Given some existing user with username "brad.pitt"
    When the user retrieves the list of training types
    Then the list of training types should be returned
