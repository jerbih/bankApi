
# Bank Account API

### Description ###

This spring boot REST API application allows to manage customer bank account.


### How do I get set up? ###
Preerquisites:
* Java 8  
* Maven 3  
* Git  

### API web services

* GET /accounts/ => return all banks accounts
* GET /accounts/${identifier} => return the bank account of the given bank account identifier
* GET /accounts/${identifier}/transactions=> return the transactions history of the given bank account identifier
* GET /accounts/${identifier}/balance  => return the balance of the given bank account identifier
* GET /accounts/${identifier}/withdraw => withdraw funds from the given bank account
* GET /accounts/${identifier}/deposit  => deposit funds into the given bank account

### User Stories ###

Requirements :

* Deposit and Withdrawal
* Account statement (date, amount, balance)
* Statement printing


#US 1:

* In order to save money
* As a bank client
* I want to make a deposit in my account

#US 2:
* In order to retrieve some or all of my savings
* As a bank client
* I want to make a withdrawal from my account

#US 3:
* In order to check my operations
* As a bank client
* I want to see the history (operation, date, amount, balance) of my operations


Use this link to test the API http://localhost:8080/swagger-ui.html 

 

