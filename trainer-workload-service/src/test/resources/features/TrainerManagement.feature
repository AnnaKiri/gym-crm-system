Feature: Trainer Management

  Scenario: Retrieve monthly summary for a trainer
    Given a trainer with username "brad.pitt" and summary data is saved
    When the user "angelina.jolie" retrieves the monthly summary for trainer "brad.pitt"
    Then the monthly summary should be returned

  Scenario: Attempt to retrieve monthly summary for a nonexistent trainer
    Given a trainer with username "brad.pitt" and summary data is saved
    When the user "angelina.jolie" attempts to retrieve the monthly summary for nonexistent trainer "NoName"
    Then the request should return not found status

  Scenario: Add training
    Given a trainer with username "brad.pitt" and summary data is saved
    And a new training with name "New Training", type ID 1, date "2024-01-05", duration 40, trainee username "john.doe", and trainer username "brad.pitt"
    When somebody "ADD" the training
    And the user "angelina.jolie" retrieves the monthly summary for trainer "brad.pitt"
    Then the monthly summary for date "2024-01-05" should be "increased" by 40 minutes

  Scenario: Delete training
    Given a trainer with username "brad.pitt" and summary data is saved
    And a new training with name "New Training", type ID 1, date "2024-01-05", duration 20, trainee username "john.doe", and trainer username "brad.pitt"
    When somebody "DELETE" the training
    And the user "angelina.jolie" retrieves the monthly summary for trainer "brad.pitt"
    Then the monthly summary for date "2024-01-05" should be "decreased" by 20 minutes
