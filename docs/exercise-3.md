[Home](index.md) 
[Previous exercise](exercise-2.md) 
[Next exercise](exercise-4.md)  

# exercise 3: Integrate recorded script in template script

In this exercise we will integrate the recorded code from exercise 2 in the template script.
 
## Modify recorded scripts

First, let's have a look at the recorded requests in SignUp.scala

```scala
// Home
.exec(http("request_0")
    .get("/")
    .headers(headers_0)
    .resources(http("request_1")
    .get("/modules/core/client/views/header.client.view.html")
    .headers(headers_1),
    http("request_2")
    .get("/modules/core/client/views/home.client.view.html")))
.pause(10)
// Signup
.exec(http("request_3")
    .get("/modules/users/client/views/authentication/authentication.client.view.html")
    .resources(http("request_4")
    .get("/modules/users/client/views/authentication/signup.client.view.html")))
.pause(20)
.exec(http("request_5")
    .post("/api/auth/signup")
    .headers(headers_5)
    .body(RawFileBody("request-bodies/SignUp_0005_request.txt")))
```

* First let's give the requests some meaningful names
* Also, change the request body filename for the "SignUp - Submit request" to "SignUp.txt" 

```scala
// Home
.exec(http("Home")
    .get("/")
    .headers(headers_0)
    .resources(http("Home - header view")
    .get("/modules/core/client/views/header.client.view.html")
    .headers(headers_1),
    http("Home - client view")
    .get("/modules/core/client/views/home.client.view.html")))
.pause(10)
// Signup
.exec(http("SignUp - Authentication html")
    .get("/modules/users/client/views/authentication/authentication.client.view.html")
    .resources(http("SignUp - Signup html")
    .get("/modules/users/client/views/authentication/signup.client.view.html")))
.pause(20)
.exec(http("SignUp - Submit")
    .post("/api/auth/signup")
    .headers(headers_5)
        .body(RawFileBody("request-bodies/SignUp.txt")))
```

## Create modular script

Since we want to create a modular script to be able to run these two user actions independently in any scenario we will create a module for each of the user actions next.
Let's create a module called Home.scala in the useCases directory. 
 
Home.scala 

```scala
package qa.perfana.mean.gatling.useCases 
 

import io.gatling.core.Predef._
import io.gatling.http.Predef._  
 
object  Home{   
 
  val headers_0 = Map(
  		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
  		"Upgrade-Insecure-Requests" -> "1")
  
  val headers_1 = Map("Accept" -> "application/json, text/plain, */*")
  
  	


  val userAction = 
    exec(http("Home")
        .get("/")
        .headers(headers_0)
        .resources(http("Home - header view")
        .get("/modules/core/client/views/header.client.view.html")
        .headers(headers_1),
          http("Home - client view")
        .get("/modules/core/client/views/home.client.view.html"))
        )
    .pause(10)
}
```
Do the same for the sign up user action:

SignUp.scala 

```scala
package qa.perfana.mean.gatling.useCases 
 

import io.gatling.core.Predef._
import io.gatling.http.Predef._  
 
object  SignUp{   
 
  val headers_5 = Map(
  		"Accept" -> "application/json, text/plain, */*",
  		"Content-Type" -> "application/json;charset=UTF-8",
  		"Origin" -> "http://localhost:3333")
  	


  val userAction = 
    
    exec(http("SignUp - Authentication html")
        .get("/modules/users/client/views/authentication/authentication.client.view.html")
        .resources(http("SignUp - Signup html")
        .get("/modules/users/client/views/authentication/signup.client.view.html")))
    .pause(74)
    .exec(http("SignUp - Submit")
        .post("/api/auth/signup")
        .headers(headers_5)
        .body(RawFileBody("request-bodies/SignUp.txt")))
}
```

Now we have two modules, let's see how to put them together in a scenario.

In setup/Scenarios.scala replace the modules in the "acceptanceTestScenario" and "debugScenario" with the modules we have just created.

```scala
 /**
   * These are the scenarios run in 'normal' mode.
   */
  val acceptanceTestScenario = scenario("Acceptance test")
    .exec(Home.userAction)
    .exec(SignUp.userAction)
  
  /**
    * These are the scenarios run in 'debug' mode.
    */
  val debugScenario = scenario("debug")
    .exec(Home.userAction)
    .exec(SignUp.userAction)
    
```

## Http protocol

In Gatling you use [httpProtocol](https://gatling.io/docs/current/http/http_protocol/) to bootstrap your test script. Any property you configure in httpPrototcol will apply to all requests in the scenario. In the template script, the httpProtocol is configured in /gatling-mean/src/test/scala/qa/perfana/mean/gatling/configuration/Configuration.scala. There is a separate httpProtocol for running the script in debug mode, in case you want to use a different setup while debugging. We will take the configuration created by the Gatling recorder and make some modifications:
* We replace the baseUrl, the root for all relative urls used in the script, to a variable that is set via a Maven profile. This is used to easily switch between environments  when running the script. As you can see, this has been done in the generated template script already.
* We remove the Blacklist from inferHTMLResources, this will make the script download any static resources found in the application HTML. To prevent Gatling from reporting response times for each static resource, we'll add ".silentResources". We add a whitelist section to prevent the script from downloading any resources not coming from our host/domain.  
* In the baseHttpDebugProtocol we will add a extraInfoExtractor that can help us debug issues.  

```scala
import io.gatling.commons.stats.KO

  private val baseHttpProtocol = http
     .baseURL(Configuration.targetBaseUrl)
     .acceptHeader("text/html")
     .acceptEncodingHeader("gzip, deflate")
     .acceptLanguageHeader("en-US,en;q=0.9,nl;q=0.8,de;q=0.7")
     .userAgentHeader("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.119 Safari/537.36")
     .inferHtmlResources(WhiteList(""".*""" + Configuration.targetBaseUrl + """.*"""))
     .silentResources
 
   private val baseHttpDebugProtocol = http
     .baseURL(Configuration.targetBaseUrl)
     .acceptHeader("text/html")
     .acceptEncodingHeader("gzip, deflate")
     .acceptLanguageHeader("en-US,en;q=0.9,nl;q=0.8,de;q=0.7")
     .userAgentHeader("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.119 Safari/537.36")
     .inferHtmlResources(WhiteList(""".*""" + Configuration.targetBaseUrl + """.*"""))
     .silentResources
     .extraInfoExtractor(ExtraInfo => {
       if(ExtraInfo.status == KO)
         println("httpCode: " + ExtraInfo.response.statusCode + ", body: "+ ExtraInfo.response.body)
       Nil
     })
```   

## Test environment profile
Next, we add a test environment profile for testing the Mean app in our localhost. This profile contains the targetBaseUrl variable.

```xml
  <!-- Test environment profiles -->

    <profile>
        <id>test-env-local</id>
        <activation>
            <activeByDefault>false</activeByDefault>
        </activation>
        <properties>
            <targetBaseUrl>http://localhost:3333</targetBaseUrl>
            <testEnvironment>local</testEnvironment>
        </properties>
    </profile>
``` 

## Run the script

It's time now to give it a spin! The template script uses a fork of the [gatling-maven-plugin](https://gatling.io/docs/current/extensions/maven_plugin/) to start and configure tests using Maven profiles.

A short explanation:

The script can be configured with three types of profiles:
* Workload profiles: to configure the workload of the test
* Test environment profiles: to configure the environment to use for the test
* "special profiles": to trigger some specific script behavior, e.g. to run in debug mode, use a proxy.

In a terminal, run the following command to test the script against the Mean app running on your local machine, in debug mode:

```  

mvn clean install perfana-gatling:integration-test -Ptest-env-local,debug
 ```
 
Unfortunately the Sign up fails, as the logs indicate:

```  
Request 'SignUp - Submit' failed: status.find.in(200,304,201,202,203,204,205,206,207,208,209), but actually found 400
   httpCode: Some(400), request url: http://localhost:3333/api/auth/signup, response: 
```

## Debug

To debug a failing script there are a number of options available:

* use an [extraInfoExtractor](https://gatling.io/docs/2.3/http/http_protocol/?highlight=extrainfo). For your convenience the extraInfoExtractor is already configured on the httpDebugProtocol in configuration/Configuration.scala:

```scala    
    .extraInfoExtractor(ExtraInfo => {
      if(ExtraInfo.status == KO)
        println("httpCode: " + ExtraInfo.response.statusCode + ", request url: " +  ExtraInfo.request.getUri.toUrl()  + /*", response: "+ ExtraInfo.response.body.string +*/ ", User: " + ExtraInfo.session("user").as[String])
      Nil
    })
```
* Change the loglevel of Gatling to DEBUG or TRACE in logback.xml
* Pro tip: install a web proxy like Fiddler or Charles proxy and use the "proxy" profile to route the Gatling traffic through a proxy. This will speed up your debugging significantly compared to sifting through huge logs!


For now we will try setting the loglevel to DEBUG:

```  
<!-- Uncomment for logging ALL HTTP request and responses -->
	 	<!--<logger name="io.gatling.http" level="TRACE" />-->
	<!-- Uncomment for logging ONLY FAILED HTTP request and responses -->
	 	<logger name="io.gatling.http" level="DEBUG" /> 
```

That should help you find the root cause of the script failing! We will fix the script in the next exercise.

  
## Result

If you were not able to succeed with the steps described above you find the end result [here](https://github.com/perfana/perfana-gatling-workshop/tree/workshop/exercise-3)  
   
 [Home](index.md) 
 [Previous exercise](exercise-2.md) 
 [Next exercise](exercise-4.md)  

 