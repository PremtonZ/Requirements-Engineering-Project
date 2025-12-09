Feature: Admin Login
  As an Owner,
  I want to login and logout,
  so that I can access the system securely

  Scenario: Login Admin Account
    Given I am not logged in
    When I login as admin with username "admin" and password "admin123"
    Then I am successfully logged in as admin
    And I can access the admin dashboard

  Scenario: Logout Admin Account
    Given I am logged in as admin
    When I logout as admin
    Then I am successfully logged out
    And I cannot access the admin dashboard anymore
