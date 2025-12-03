Feature: Login
  As an Admin,
  I want to login and logout,
  so that I can access the system securely

  Scenario: Admin-Konto einloggen
    Given I am not logged in
    When I login as admin with username "admin" and password "admin123"
    Then I am successfully logged in as admin
    And I can access the admin dashboard

  Scenario: Admin-Konto ausloggen
    Given I am logged in as admin
    When I logout as admin
    Then I am successfully logged out
    And I cannot access the admin dashboard anymore
