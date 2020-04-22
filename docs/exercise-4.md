[Home](index.md) 
[Previous exercise](exercise-3.md) 
[Next exercise](exercise-5.md)  

# exercise 4: Inject data into the script

In this exercise we will add a Feeder to the script to inject user data into the script.


## Feeders

[Feeders](https://gatling.io/docs/2.3/session/feeder/) are used to inject variables in a Gatling script session. In this assigment we will create a list of user data to be used in the Sign up user action (and later on in the Sign in user action).

* First create a file "users.csv" in /src/test/resources/data with the following contents:


```scala
user
demo1
demo2
demo3
demo4
demo5
demo6
demo7
demo8
demo9
demo10
demo11
demo12
demo13
demo14
demo15
demo16
demo17
demo18
demo19
demo20
  
```

* Next, create a file name UserFeeder.scala in /src/test/scala/qa/perfana/mean/gatling/feeders with the following contents:

```scala
package qa.perfana.mean.gatling.feeders  
  

import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
  
object UserFeeder {

  val user = csv("data/users.csv").circular


}

```
> This feeder will take users from the csv file we just created and will assign the value to a session variable named "user" (the column header of the csv)

* To inject the user variable we have to "feed" the script. You can do this anywhere in your script but in our case we only want to do this once at the start. In order to do so we will add it in _Scenarios.scala_

```scala
 /**
   * These are the scenarios run in 'normal' mode.
   */
   
  val acceptanceTestScenario = scenario("acceptance")
    .feed(UserFeeder.user)
    .exec(Home.userAction)
    .exec(SignUp.userAction)
    

  /**
    * These are the scenarios run in 'debug' mode.
    */
     

  val debugScenario = scenario("debug")
    .feed(UserFeeder.user)
    .exec(Home.userAction)
    .exec(SignUp.userAction)


``` 

* To use the injected data in the actual calls we have to put some place holders for the variable "user" in the request body of the Sign up call:

```json
{
  "username": "${user}",
  "password": "${user}Password!",
  "firstName": "${user}",
  "lastName": "${user}",
  "email": "${user}@demo.com"
}

```
* Finally we need to tell Gatling to replace the placeholders with the session variable values, by changing RawFileBody to ElFileBody:

```scala
    .exec(http("SignUp - Submit")
    .post("/api/auth/signup")
    .headers(headers_5)
    .body(ElFileBody("request-bodies/signUp.txt")))
    .pause(11)

```

* Let's run the script again, and this time let it iterate a few times.

```scala

  /**
    * These are the scenarios run in 'debug' mode.
    */
     

  val debugScenario = scenario("debug")
    .repeat(5){
        feed(UserFeeder.user)
        .exec(Home.userAction)
        .exec(SignUp.userAction)
    }   

``` 

> Make sure the feed is inside the repeat loop, so it updates the user randomly each iteration!

* To run the script execute this command in your terminal

```  
mvn clean perfana-gatling:test -Ptest-env-local,debug
 ```
 
If all is well you should see the last four Sign up calls succeeded. That means for the first five users there is no use signing up again. So let's make our script a little bit more fancy by only signing up for users in our list that have not signed up yet. How do we find out if a user has signed up: right, by trying to sign in!

In the next exercise we will use "checks" and "conditional execution" to accomplish this.

## Result

If you were not able to succeed with the steps described above you find the end result [here](https://github.com/perfana/perfana-gatling-workshop/tree/workshop/exercise-4)  



[Home](index.md) 
[Previous exercise](exercise-3.md) 
[Next exercise](exercise-5.md)  
  
