# Wallet Optimizer

## Overview

Wallet Optimizer is a full-stack credit card rewards application built with Spring Boot and JavaScript.

The application allows users to store credit cards and their cashback reward rules, recommend the best card for a spending category, and analyze a monthly budget to estimate which cards maximize cashback rewards.

Credit card data is persisted in a MySQL database using Spring Data JPA and Hibernate.

## Features

* Create and store credit cards
* Update and delete existing cards
* Add, update, and delete category-specific reward rules
* View cards and their reward rules
* Search for cards by name
* Support default cashback rates when no category-specific reward exists
* Recommend the card or cards with the highest cashback for a spending category
* Ignore inactive cards when calculating recommendations
* Prevent duplicate cards with the same name and issuer
* Analyze multiple monthly expenses with the Budgeting Tool
* Recommend the best card for each budget category
* Calculate estimated monthly and annual cashback rewards
* Validate incoming requests
* Handle errors with custom exception responses
* Persist card and reward data in MySQL
* Web frontend built with HTML, CSS, and JavaScript
* Automated controller and service tests

## Tech Stack

### Backend

* Java 21
* Spring Boot
* Spring Web
* Spring Data JPA
* Hibernate
* Jakarta Bean Validation
* Maven

### Database

* MySQL

### Frontend

* HTML
* CSS
* JavaScript

### Testing

* JUnit
* Mockito
* MockMvc
* H2
* Postman

## Data Model

A `Card` can have multiple `RewardRule`s.

### Card

* `id`
* `name`
* `issuer`
* `network`
* `annualFee`
* `defaultCashbackPercent`
* `active`
* `rewardRules`

### RewardRule

* `id`
* `category`
* `cashbackPercent`
* `card`

`Card` and `RewardRule` have a one-to-many / many-to-one relationship. Card and reward rule IDs are generated automatically by the database.

Cashback percentages are represented using `BigDecimal`.

## Recommendation Logic

When a spending category is provided, Wallet Optimizer evaluates all active cards.

For each card:

1. The application checks whether the card has a reward rule matching the requested category.
2. If a matching rule exists, its cashback percentage is used.
3. Otherwise, the card's default cashback percentage is used.
4. The application determines the highest available cashback rate.
5. If multiple cards have the same highest rate, all tied cards are returned.

This allows recommendations to work even for categories that do not have a specific reward rule.

## Budgeting Tool

The Budgeting Tool allows users to enter multiple monthly expenses and determine the best card to use for each one.

For every expense, Wallet Optimizer:

1. Finds the card or cards with the highest cashback rate.
2. Calculates the expected monthly cashback.
3. Displays the recommended cards and cashback percentage.

The application also calculates:

* Total monthly expenses
* Total estimated monthly rewards
* Total estimated annual rewards

Example request:

```json
{
  "expenses": [
    {
      "category": "Gas",
      "monthlyAmount": 100
    },
    {
      "category": "Restaurants",
      "monthlyAmount": 300
    },
    {
      "category": "Groceries",
      "monthlyAmount": 400
    }
  ]
}
```

## API Endpoints

### GET Endpoints

`GET /cards`

Returns all stored credit cards.

---

`GET /cards/{cardId}`

Returns the card with the specified ID.

---

`GET /cards/{cardId}/reward-rules`

Returns the reward rules associated with the specified card.

---

`GET /cards/search?name=Costco Anywhere`

Returns cards matching the provided name.

---

`GET /recommend?category=Gas`

Returns the card or cards that provide the highest cashback for the given spending category.

### POST Endpoints

`POST /cards`

Creates a new credit card.

---

`POST /cards/{cardId}/reward-rules`

Adds a reward rule to the specified card.

---

`POST /budget/recommendation`

Analyzes a list of monthly expenses and returns card recommendations together with estimated monthly and annual cashback rewards.

### PUT Endpoints

`PUT /cards/{cardId}`

Updates an existing credit card.

---

`PUT /cards/{cardId}/reward-rules/{rewardId}`

Updates an existing reward rule.

### DELETE Endpoints

`DELETE /cards/{cardId}`

Deletes the specified credit card.

---

`DELETE /cards/{cardId}/reward-rules/{rewardId}`

Deletes the specified reward rule.

## Frontend

The project includes a frontend built with HTML, CSS, and JavaScript.

The main interface allows users to:

* View stored cards
* Add new cards
* Enter a default cashback percentage
* Request a recommendation for a spending category

The separate Budgeting Tool allows users to:

* Add multiple monthly expenses
* Remove expenses before submitting the budget
* Request optimized card recommendations
* View recommendations in a table
* View total monthly expenses
* View estimated monthly rewards
* View estimated annual rewards

The frontend communicates with the Spring Boot backend using REST API requests.

## Validation and Error Handling

The application uses Jakarta Bean Validation to validate incoming requests.

Validation includes checks for fields such as:

* Required card information
* Positive cashback percentages
* Valid monthly spending amounts
* Non-empty spending categories
* Non-empty budget requests

The application also includes custom exception handling for cases such as:

* Card not found
* Reward rule not found
* Duplicate cards
* Invalid request data

## Persistence

Credit cards and reward rules are persisted in a MySQL database using Spring Data JPA and Hibernate.

The application uses:

```properties
spring.jpa.hibernate.ddl-auto=update
```

to keep the database schema synchronized with the entity model during development.

## Testing

The project contains automated tests for controller and service behavior.

Tests cover areas including:

* Input validation
* Card recommendation logic
* Default cashback behavior
* Tied card recommendations
* Budget recommendation calculations
* Invalid budget requests

H2 is used as a test database where database-backed testing is needed.

## Running the Project

1. Clone the repository.
2. Create a MySQL database named `wallet_optimizer`.
3. Configure the environment variables `DB_USERNAME` and `DB_PASSWORD` with your MySQL credentials.
4. Run the Spring Boot application.
5. Run the frontend using a local web server such as VS Code Live Server.
6. Open `frontend/index.html` to use the card management and recommendation interface.
7. Open `frontend/budget.html` to use the Budgeting Tool.

The backend runs locally at:

```text
http://localhost:8080
```

The REST API can also be tested directly using Postman.

## Future Improvements

* Deploy the application to a cloud platform
* Implement authentication and authorization with Spring Security
* Add user accounts so each user can manage their own wallet
* Support more advanced reward structures such as rotating categories
* Improve the frontend navigation and user experience
* Convert remaining monetary fields such as `annualFee` from `double` to `BigDecimal`
