Feature: Training Management

  Scenario: Create a new training
    Given an existing user with username "brad.pitt"
    When the user creates the training
    Then the training should be created successfully

  Scenario: Retrieve training details
    Given an existing user with username "brad.pitt"
    When the user retrieves the training details with ID 1
    Then the training details for ID 1 should be returned

  Scenario: Attempt to retrieve a nonexistent training
    Given an existing user with username "brad.pitt"
    When the user attempts to retrieve a nonexistent training
    Then the training request should return not found status
