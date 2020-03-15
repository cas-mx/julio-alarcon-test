# julio-alarcon-test
REST test API
API to create, get, list, sum and report transactions.
Each transaction will consist of User Id, Amount, Description and date.

The endpoints to perform actions are:
## To create a transaction:
``` POST http://<server>/rest/trans/create```
```
  Body:
  {
	"userId": 9,
	"amount":12.89,
	"description":"tacos",
	"date":"2019-08-13"
  }
  ```
  
  ## To get the details of a specific transaction:
 ```GET http://<server>/rest/trans/get/{userId}/{transId}```
  
  ## To list all the transactions for a specific user:
```GET http://<server>/rest/trans/list/{userId}```

 ##  To sum all the transactions for a user:
	GET http://<server>/rest/trans/sum/{userId}
 
## This option will generate a report with all the transactions for a specific user:
	GET http://<server>/rest/trans/report/{userId}
  output report:
```  
Id        Week start         Week finish   Quantity Amount Total Amount
9 2019-08-09   FRIDAY 2019-08-15 THURSDAY   1       2.89             0
9 2019-09-01   SUNDAY 2019-09-05 THURSDAY   2      17.78          2.89
9 2019-09-06   FRIDAY 2019-09-12 THURSDAY   1       8.89         20.67
9 2019-09-13   FRIDAY 2019-09-19 THURSDAY   1       8.89         29.56
```

This application will generate a txt file called Transactions.txt, it will be stored in the same 
folder as the war file
of the application.
Make sure that the folder has the right permisions to write files.

Running application for testing:
```
http://julio-alarcon-test.herokuapp.com/rest/trans/create
http://julio-alarcon-test.herokuapp.com/rest/trans/report/{userId}
http://julio-alarcon-test.herokuapp.com/rest/trans/list/{userId}
http://julio-alarcon-test.herokuapp.com/rest/trans/get/{userId}/{transUUID}
```

