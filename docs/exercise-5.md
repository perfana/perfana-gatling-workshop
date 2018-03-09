[Home](index.md) 
[Previous exercise](exercise-4.md) 
[Next exercise](exercise-6.md)  

# exercise 5: Checks & conditional execution

In this exercise we will learn about how to check responses and based on the result decide what call to do next.


## Checks

[Checks](https://gatling.io/docs/current/http/http_check/) are used to verify that a response matches the expectations. Gatling will report a request as failed when a check fails. Checks can also be used to parse certain values from the response and to store them in session variable to be used later on in the script.

In our use case we will check if user signs in successfully, and if not, do a sign up for the user.


## Steps
* First we have to create a SignIn user action module and hook up the "user" session variable:
 

```scala

//SignIn.scala
 
  package qa.perfana.mean.gatling.useCases
 

  import io.gatling.core.Predef._
  import io.gatling.http.Predef._
 
  object SignIn{
 
    val headers_5 = Map(
    "Accept" -> "application/json, text/plain, */*",
    "Content-Type" -> "application/json;charset=UTF-8",
    "Origin" -> "http://localhost:3333")
 
    val userAction = 
     exec(http("SignIn - Authentication html")
    .get("/modules/users/client/views/authentication/authentication.client.view.html")
    .resources(http("SignIn - SignIn html")
    .get("/modules/users/client/views/authentication/signin.client.view.html")))
    .pause(5)
    .exec(http("SignIn - Submit")
    .post("/api/auth/signin")
    .headers(headers_5)
    .body(ElFileBody("SignIn.txt")))
 }
```
SignIn.txt

```json
{
  "username": "${user}",
  "password": "${user}Password!"
}
```

* Now let's find out what a failed and a successful sign in response looks like. Set the log level to TRACE in logback.xml and add the Sign in user action to Scenarios.scala.
  
```scala

  /**
    * These are the scenarios run in 'debug' mode.
    */
     

  val debugScenario = scenario("debug")
    .feed(UserFeeder.user)
    .exec(Home.userAction)
    .exec(SignIn.userAction)

``` 

* To run the script execute this command in your terminal

```  
mvn clean install perfana-gatling:integration-test -Ptest-env-local,debug
 ```

The logs show that the app returns the user object when the sign in succeeds. 

```
body=
{"_id":"5a68e839a0fbf81a00cbeb06","displayName":"demo1 demo1","provider":"local","username":"demo1","__v":0,"created":"2018-01-24T20:10:33.600Z","roles":["user"],"profileImageURL":"modules/users/client/img/profile/default.png","email":"demo1@demo.com","lastName":"demo1","firstName":"demo1"}

```

Now let's try to sign in a user that was not signed up before. In users.csv file change user "demo1" to "demo100" and observe the log: 


```
HTTP response:
status=
400 Bad Request
headers= 
Vary: X-HTTP-Method-Override, Accept-Encoding
X-FRAME-OPTIONS: SAMEORIGIN
X-XSS-Protection: 1; mode=block
X-Content-Type-Options: nosniff
X-Download-Options: noopen
Strict-Transport-Security: max-age=15778476; includeSubDomains
P3P: ABCDEF
Content-Security-Policy: 
Content-Type: application/json; charset=utf-8
Content-Length: 42
ETag: W/"2a-dpOd07GkhA43GlNv7/dl4g"
Date: Thu, 01 Feb 2018 14:26:17 GMT
Connection: keep-alive

body=
{"message":"Invalid username or password"}

```

There are now several ways to make the script do what we want. Since we will need the "user_id" later on in the script we will go for this approach:

```
.exec(http("SignIn - Submit")
.post("/api/auth/signin")
.headers(headers_5)
.body(ElFileBody("SignIn.txt")
.check(status.in(200,400))
.check(jsonPath("$._id").optional.saveAs("user_id"))) 
```

The first check verifies if the http status code is either 200 or 400. If we do not tell Gatling that a 400 status code is in our use case not a failed request (the application or script are not "failing" when the Mean app returns a http 400 request) it will mark the request as failed automatically.
The next check is parsing the response body using jsonPath and saving the value for key "_id" to a session variable named "user_id". A useful tool for debugging jsonPath is [https://jsonpath.curiousconcept.com/](https://jsonpath.curiousconcept.com/). The "optional" tells Gatling not to fail the request when the jsonPath doesn't return anything. (in case the user still needs to sign up)

# Conditional execution

Next we'll implement what we aimed to achieve: if a user's sign in fails, the script should run the sign up user action for that user. We will use a [conditional statement](https://gatling.io/docs/current/general/scenario/#conditional-statements) to accomplish this.

In SignIn.scala add

```
  .doIf(!_.contains("user_id")) {
    exec(SignUp.userAction)

  } 
```
> The doIf statement checks if the session does NOT contain a variable "user_id" ( _ is Gatling shorthand for the session). If the condition resolves ot true the SignUp.useAction is executed.

Now check what happens when you run the script some iterations with both users that have signed up already and users that have not signed up yet!
## Result

If you were not able to succeed with the steps described above you find the end result [here](https://github.com/perfana/perfana-gatling-workshop/tree/workshop/exercise-5)  


[Home](index.md) 
[Previous exercise](exercise-4.md) 
[Next exercise](exercise-6.md)  


  
