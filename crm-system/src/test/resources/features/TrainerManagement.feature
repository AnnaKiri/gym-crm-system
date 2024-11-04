Feature: Trainer Management

  Scenario: Register a new trainer
    Given a new trainer with first name "Jackie", last name "Chan", specialization ID 1
    When the trainer registers
    Then the trainer should be created with a username "jackie.chan"

  Scenario: Change password
    Given an existing trainer with username "tom.cruise"
    When the trainer changes password to "newPassword123"
    Then the trainer password change should succeed

  Scenario: Retrieve trainer details
    Given an existing trainer with username "brad.pitt"
    When the trainer retrieves their details
    Then the trainer details should be returned

  Scenario: Update trainer details
    Given an existing trainer with username "jackie.chan"
    When the trainer updates their details
    Then the trainer details should be updated successfully

  Scenario: Retrieve trainings
    Given an existing trainer with username "jennifer.aniston"
    When the trainer retrieves their trainings
    Then the list of trainings for trainer should be returned

  Scenario: Set account status to active
    Given an existing trainer with username "jackie.chan"
    When the trainer sets their account status to "true"
    Then the trainer account status should be updated successfully

  Scenario: Set account status to inactive
    Given an existing trainer with username "jackie.chan"
    When the trainer sets their account status to "false"
    Then the trainer account status should be updated successfully

  Scenario: Retrieve nonexistent trainer
    Given an existing trainer with username "jennifer.aniston"
    When the trainer attempts to retrieve a nonexistent trainer
    Then the request should return trainer not found status

  Scenario: Update trainer details with invalid data
    Given an existing trainer with username "jennifer.aniston"
    When the trainer attempts to update their details with invalid data
    Then the trainer update request should return bad request status

  Scenario: Attempt to set account status again to inactive
    Given an existing trainer with username "sandra.bullock"
    When the trainer attempts to set their account status again to "true"
    Then the trainer set active request should return conflict status
