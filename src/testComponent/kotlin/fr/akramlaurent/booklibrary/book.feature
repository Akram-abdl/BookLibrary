Feature: store, reserve ane get book
  Scenario: the user create two entries and retrieve both
    Given the user creates a book with title "Les misérables" and author "Victor Hugo"
    And the user creates a book with title "La légende des siècles" and author "Victor Hugo"
    When the user get all books
    Then the user should see the following books
      | title                  | author       | reserved |
      | Les misérables         | Victor Hugo  | false    |
      | La légende des siècles | Victor Hugo  | false    |

  Scenario: the user reserve a book
    Given the user creates a book with title "Les misérables" and author "Victor Hugo"
    When the user reserve a book with title "Les misérables" and author "Victor Hugo"
    And the user get all books
    Then the user should see the following books
      | title                  | author       | reserved |
      | Les misérables         | Victor Hugo  | true     |

  Scenario: the user reserve a book that does not exist
    When the user reserve a book with title "Les misérables" and author "Victor Hugo"
    Then the user should see the following error message 404 "Book not found"

  Scenario: the user reserve a book that is already reserved
    Given the user creates a book with title "Les misérables" and author "Victor Hugo"
    And the user reserve a book with title "Les misérables" and author "Victor Hugo"
    When the user reserve a book with title "Les misérables" and author "Victor Hugo"
    Then the user should see the following error message 409 "Book already reserved"
