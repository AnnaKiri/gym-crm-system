Feature: Authentication

  Scenario: Successful Authentication
    Given a user with username "angelina.jolie" and password "password1"
    When the user attempts to log in
    Then the authentication should succeed and return a token

  Scenario: Authentication with Wrong Credentials
    Given a user with wrong credentials
    When the user attempts to log in
    Then the authentication should fail with an unauthorized status with reason wrong credentials

  Scenario: Brute-force protection. User is blocked after 3 login attempts
    Given a user with username "tom.hardy" and password "password1"
    When the user attempts to log in
    Then the authentication should fail with an unauthorized status with reason wrong credentials
    When the user attempts to log in
    Then the authentication should fail with an unauthorized status with reason wrong credentials
    When the user attempts to log in
    Then the authentication should fail with an unauthorized status with reason wrong credentials
    When the user attempts to log in
    Then the authentication should fail with an unauthorized status with reason you're blocked

  Scenario: Successful Logout
    Given a logged-in user with a valid token
    When the user attempts to log out
    Then the logout should succeed with a success message

  Scenario: Logout with Invalid Access Token
    Given a user with an invalid access token
    When the user attempts to log out
    Then the logout should fail with an unauthorized status

  Scenario: Logout with Invalid Refresh Token
    Given a user with an invalid refresh token
    When the user attempts to log out
    Then the logout should fail with a bad request status

