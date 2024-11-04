Feature: Gym CRM System End-to-End Tests

  Scenario: Register a new trainee
    Given a new trainee with first name "John", last name "Doe", birthday "2000-01-01", and address "123 Main St"
    When the trainee registers
    Then the trainee should be created with a username "john.doe"

  Scenario: Authenticate trainee
    Given an existing trainee with username "angelina.jolie" and password "password1"
    When the trainee authenticates
    Then the access token should be available

  Scenario: Create training
    Given a new training with name "New Training", type ID 1, date "2024-10-05", duration 60, trainee username "john.doe", and trainer username "sandra.bullock"
    When the trainee with username "angelina.jolie" creates a training
    Then the trainer's monthly summary should match expected summary for "sandra.bullock"

  Scenario: Delete trainee
    When the trainee with username "angelina.jolie" deletes trainee with username "john.doe"
    Then an exception should be thrown when retrieving summary for trainee "john.doe"

  Scenario: Authenticate with invalid credentials
    Given a trainee with username "john.doe" and wrong password "wrongPassword"
    When the trainee attempts to authenticate with invalid credentials
    Then an exception should be thrown

  Scenario: Create training for nonexistent trainee
    Given a new training with name "New Training", type ID 1, date "2024-10-05", duration 60, trainee username "john.doe", and trainer username "sandra.bullock"
    When the authenticated trainee with username "angelina.jolie" attempts to create a training for nonexistent trainee
    Then an exception should be thrown

  Scenario: Delete nonexistent trainee
    When the authenticated trainee with username "angelina.jolie" attempts to delete nonexistent trainee with username "NonExistentUser"
    Then an exception should be thrown
