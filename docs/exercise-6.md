[Home](index.md) 
[Previous exercise](exercise-5.md) 
[Next exercise](exercise-7.md)  

# exercise 6: Data correlation

In this exercise we will learn about the concept of data correlation.


## Data correlation

In the previous exercise we learned about [Checks](https://gatling.io/docs/current/http/http_check/). Checks can also be used to parse certain values from response data and to store them in a session variable to be used later on in the script. We call this data correlation.

In our use case we will add an article, capture the article ID from the response and use this to open the article.

## Steps
* First we will record the user action AddArticle with the Gatling recorder

```
/bin/recorder.sh
```

* Set Class name to "CreateArticle"
* In the black list section, click "No static resources"
* Open the Mean demo app and sign in with user demo1, password demo1Password! 
* Click "Start!" in the Gatling recorder
* Configure your browser to use a proxy at localhost:8000
* In the Gatling recorder add a tag named "Create article"
* In the Mean demo app chose "create article" from the articles menu
* Create an article in the article form
* In the Gatling recorder add a tag named "Submit article"
* Click "create" in the create article form
* In the Gatling recorder, click "Stop &#38; Save"

Now let's have a look at the recorded code.

```scala
// Create article
.exec(http("request_0")
.get("/modules/articles/client/views/form-article.client.view.html")
.headers(headers_0))
.pause(20)
// Submit article
.exec(http("request_1")
.post("/api/articles")
.headers(headers_1)
.body(RawFileBody("request-bodies/AddArticle_0001_request.txt"))
.resources(http("request_2")
.get("/api/articles/5a7432d506b2c92500707542"),
http("request_3")
.get("/modules/articles/client/views/view-article.client.view.html")
.headers(headers_0),
http("request_4")
.get("/lib/bootstrap/dist/fonts/glyphicons-halflings-regular.woff2")
.headers(headers_4)))
  
  ```
 
> Request 4 made it through the static resources blacklist. To prevent this form happening next time, add ".*\.woff2" to the blacklist section of the Gatling recorder.
 
Like we did earlier, we will add meaningful names to the requests and create a module for the user action, named CreateArticle. Don't forget to copy (and rename) the recorded request body to the /src/test/resources/request-bodies directory of your script

```scala
  
  package qa.perfana.mean.gatling.useCases
  
  
  import io.gatling.core.Predef._
  import io.gatling.http.Predef._
  
  object CreateArticle{
  
	val headers_0 = Map("Accept" -> "text/html")
  
	val headers_1 = Map(
		"Content-Type" -> "application/json;charset=UTF-8",
		"Origin" -> "http://localhost:3333")
  
  
    val userAction =
// Create article
    exec(http("Open create article form")
    .get("/modules/articles/client/views/form-article.client.view.html")
    .headers(headers_0))
    .pause(20)
      
// Submit article
    .exec(http("Submit article")
    .post("/api/articles")
    .headers(headers_1)
    .body(RawFileBody("request-bodies/AddArticle.txt"))
    .resources(http("View article")
    .get("/api/articles/5a7432d506b2c92500707542"),
    http("View article HTML")
    .get("/modules/articles/client/views/view-article.client.view.html")
    .headers(headers_0)
    ))
}
  
  
```

The "View article" request seems to have a dynamic parameter in the url. Taking a quick look in the meanJs [source](https://github.com/meanjs/mean/blob/master/modules/articles/server/routes/articles.server.routes.js) tells us this is the articleId. We have to capture this id from the response of the preceding POST request. To find out what this request looks like, we take a different approach this time using the Chrome DevTools!

* Open the Mean demo app in Chrome.
* Open Chrome developer tools (hit F12 or open via menu)
* Open the "Network" tab
* Create and submit an article 
* In Chrome developer tool, click on the request named "articles". 

In the "Headers" tab you can confirm this is the POST request to submit the article. In the "Response" tab you can see what the response looks like:

```json
{
  "__v": 0,
  "user": {
    "_id": "5a6b2e17dbf6202400a3c0e6",
    "displayName": "demo1 demo1",
    "provider": "local",
    "username": "demo1",
    "__v": 0,
    "created": "2018-01-26T13:33:11.615Z",
    "roles": [
      "user"
    ],
    "profileImageURL": "modules\/users\/client\/img\/profile\/default.png",
    "email": "demo1@demo.com",
    "lastName": "demo1",
    "firstName": "demo1"
  },
  "_id": "5a745fc506b2c92500707544",
  "content": "A great, great story",
  "title": "This is a great post",
  "created": "2018-02-02T12:55:33.482Z"
}  

```

* Now we correlate the articleId by capturing and saving it in a session variable via jsonPath. We then use a placeholder "${articleId}" in the "View article" url.

```scala
// Submit article
    .exec(http("Submit article")
    .post("/api/articles")
    .headers(headers_1)
    .body(RawFileBody("request-bodies/AddArticle.txt"))
    .check(jsonPath("$._id").saveAs("articleId"))
    .resources(http("View article")
    .get("/api/articles/${articleId}"),
    http("View article HTML")
    .get("/modules/articles/client/views/view-article.client.view.html")
    .headers(headers_0)
    ))


```


Now add the user action to Scenarios.scala, set loglevel to TRACE and see if your correlation works!

  
```scala

  /**
    * These are the scenarios run in 'debug' mode.
    */
     

  val debugScenario = scenario("debug")
    .feed(UserFeeder.user)
    .exec(Home.userAction)
    .exec(SignIn.userAction)
    .exec(CreateArticle.userAction)

``` 




## Result

If you were not able to succeed with the steps described above you find the end result [here](https://github.com/perfana/perfana-gatling-workshop/tree/workshop/exercise-6)  


[Home](index.md) 
[Previous exercise](exercise-5.md) 
[Next exercise](exercise-7.md)  


  
