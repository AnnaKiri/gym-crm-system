Feature: Trainer Management

  Scenario: Retrieve monthly summary for a trainer
    Given a trainer with username "brad.pitt" and summary data is saved
    When the user "angelina.jolie" retrieves the monthly summary for trainer "brad.pitt"
    Then the monthly summary should be returned

  Scenario: Attempt to retrieve monthly summary for a nonexistent trainer
    Given a trainer with username "brad.pitt" and summary data is saved
    When the user "angelina.jolie" attempts to retrieve the monthly summary for nonexistent trainer "NoName"
    Then the request should return not found status
