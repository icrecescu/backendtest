# backendtest
A RESTful API for transferin money between 2 accounts

### How to run
mvn clean package </br>
java -jar target/backend-test-1.0.0-SNAPSHOT-jar-with-dependencies.jar

### Available Services

| HTTP METHOD | PATH | USAGE |
| -----------| ------ | ------ |
| POST | /api/v1/account/create | Creates an account | 
| GET | /api/v1/account/{accountId} | Get account by account id | 
| GET | /api/v1/account/user/{userId} | Get all accounts related to a specific user | 
| DELETE | /api/v1/account/{accountId}/delete | Deletes an account by id | 
| PUT | /api/v1/account/{accountId}/balance/{amount} | Updates an account by a specified positive or negative amount | 
| POST | /api/v1/transaction/ | Create a transaction for transfering funds between 2 accounts | 


### Sample Json for creating an account

```sh
{
	"userId" : 34578,
	"balance" : 0,
	"limit" : 0,
	"currencyCode" : "EUR"
}
```

### Sample Json for creating a transaction

```sh
{
	"source" : "USD0YP9XKQMEDW",
	"destination" : "EUR0Y8GJTULMIK",
	"amount" : "7"
}
```

### P.S. Not all edge cases are unit/functional tested :(  (tight schedule in this period)
### P.S.S Also no logging
