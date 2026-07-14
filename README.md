# Wallet Optimizer

## Overview
Wallet Optimizer is a Spring Boot REST API that allows users to store their credit cards and recommends the best credit card to use for a given spending category based on its cashback rewards.


## Features
- Create credit cards
- Add reward rules to each credit card
- View reward rules for each card
- Recommend the best card based on spending category
- Validate user input
- Handle errors with custom exception responses



## Tech Stack
- Java 21
- Spring Boot
- Spring Data JPA
- Hibernate
- MySQL
- Maven

The project uses Spring Data JPA and Hibernate to persist entities in a MySQL database.
## Data Model

A `Card` can have multiple `RewardRule`s.

###Card
    - id
    - name
    - issuer
    - network
    - annualFee
    - rewardRules

###RewardRule
    - id
    - category
    - cashbackPercent
    - card
  
## API Endpoints
### GET endpoints: 
`GET /cards`
Returns all the cards stored.

`GET /cards/{cardId}`
Returns the card with the specified ID.

`GET /cards/{cardId}/reward-rules`
Returns the reward rules of the card with the specified ID.

`GET /recommend?category=Gas`
Returns the card with highest cashback in the given spending category.

`GET /cards/search?name=Costco Anywhere `
Returns the card(s) with a given name.

### POST endpoints:
`POST /cards`
Creates a new card.

`POST /cards/{cardId}/reward-rules`
Adds reward rules to the card with the specified ID.

## Running the Project

1. Create a MySQL database named `wallet_optimizer`.
2.  Configure the environment variables `DB_USERNAME` and `DB_PASSWORD` with your MySQL credentials.
3. Run the Spring Boot application.
4. Use Postman to test the available REST endpoints.


## Future Improvements
- Build a web frontend for interacting with the API.
- Implement authentication with Spring Security.
- Support flat cashback and rotating rewards.
- Replace `double` with `BigDecimal` for monetary calculations.