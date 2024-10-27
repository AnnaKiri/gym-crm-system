Feature: Trainee Management

  Scenario: Register a new trainee
    Given a new trainee with first name "John", last name "Doe", birthday "1990-01-01" and address "123 Main St"
    When the trainee registers
    Then the trainee should be created with a username "john.doe"

  Scenario: Change password
    Given an existing trainee with username "angelina.jolie"
    When the trainee changes password to "newPassword123"
    Then the trainee password change should succeed

  Scenario: Retrieve trainee details
    Given an existing trainee with username "ryan.reynolds"
    When the trainee retrieves their details
    Then the trainee details should be returned

  Scenario: Update trainee details
    Given an existing trainee with username "john.doe"
    When the trainee updates their details
    Then the trainee details should be updated successfully

  Scenario: Retrieve free trainers
    Given an existing trainee with username "tom.hardy"
    When the trainee retrieves free trainers
    Then the list of free trainers should be returned

  Scenario: Retrieve trainings
    Given an existing trainee with username "tom.hardy"
    When the trainee retrieves their trainings
    Then the list of trainings for trainee should be returned

  Scenario: Set account status to active
    Given an existing trainee with username "john.doe"
    When the trainee sets their account status to "true"
    Then the trainee account status should be updated successfully

  Scenario: Set account status to inactive
    Given an existing trainee with username "john.doe"
    When the trainee sets their account status to "false"
    Then the trainee account status should be updated successfully

  Scenario: Retrieve nonexistent trainee
    Given an existing trainee with username "tom.hardy"
    When the trainee attempts to retrieve a nonexistent trainee
    Then the request should return trainee not found status

  Scenario: Update trainee details with invalid data
    Given an existing trainee with username "tom.hardy"
    When the trainee attempts to update their details with invalid data
    Then the trainee update request should return bad request status

  Scenario: Update trainer list
    Given an existing trainee with username "tom.hardy"
    When the trainee updates their trainer list
    Then the trainer list should be updated successfully

  Scenario: Attempt to set account status again to inactive
    Given an existing trainee with username "keanu.reeves"
    When the trainee attempts to set their account status again to "true"
    Then the trainee set active request should return conflict status

  Scenario: Delete trainee account
    Given an existing trainee with username "john.doe"
    When the trainee attempts to delete their account
    Then the trainee should be deleted successfully
