[Home](index.md) 
[Previous exercise](exercise-6.md) 
[Next exercise](exercise-8.md)  

# exercise 7: Add logic to the script

In this exercise we will add some logic to the script.


## Use case

Our goal in this exercise is to add the following user actions to the script:
* User opens the article list
* User open a random article from the list
* If that random article was posted by the signed in user, delete the article.

## Steps
* First we will record the user actions with the Gatling recorder

```
/bin/recorder.sh
```

* Set Class name to "ListArticles"
* In the black list section, click "No static resources"
* Open the Mean demo app and sign in with user demo1, password demo1Password! 
* Click "Start!" in the Gatling recorder
* Configure your browser to use a proxy at localhost:8000
* In the Gatling recorder add a tag named "List articles"
* In the Mean demo app chose "List articles" from the articles menu
* In the Gatling recorder add a tag named "View article"
* In the Mean demo app open an article that was posted by user demo1
* In the Gatling recorder add a tag named "Delete article"
* Click the "trash" icon article view and confirm the article deletion
* In the Gatling recorder, click "Stop &#38; Save"

Now let's have a look at the recorded code.

```scala
// List articles
.exec(http("request_0")
.get("/modules/articles/client/views/list-articles.client.view.html")
.headers(headers_0)
.resources(http("request_1")
.get("/api/articles")))
.pause(10)
// View article
.exec(http("request_2")
.get("/api/articles/5a745fc506b2c92500707544"))
.pause(5)
// Delete article
.exec(http("request_3")
.delete("/api/articles/5a745fc506b2c92500707544?$$state=%7B%22status%22:0%7D")
.headers(headers_3)
.resources(http("request_4")
.get("/api/articles")))  
  ```
 
Like we did earlier, we will add meaningful names to the requests and create a module for the user action, named ListArticles.

```scala
  
  package qa.perfana.mean.gatling.useCases
  
  
  import io.gatling.core.Predef._
  import io.gatling.http.Predef._
  
  object ListArticles{
  
    val headers_0 = Map("Accept" -> "text/html")
    
    val headers_3 = Map("Origin" -> "http://localhost:3333")
  
  
    val userAction =
     
    // List articles
    exec(http("List articles HTML")
    .get("/modules/articles/client/views/list-articles.client.view.html")
    .headers(headers_0)
    .resources(http("List articles")
    .get("/api/articles")))
    .pause(10)  
      
    // View article
    .exec(http("View article")
    .get("/api/articles/5a745fc506b2c92500707544"))
    .pause(5)
      
    // Delete article
    .exec(http("Delete article")
    .delete("/api/articles/5a745fc506b2c92500707544?$$state=%7B%22status%22:0%7D")
    .headers(headers_3)
    .resources(http("List articles")
    .get("/api/articles")))

}
  
  
```

* Use the Chrome developers tool to investigate the "List articles" response

```json
[
  {
    "_id": "5a74597f06b2c92500707543",
    "user": {
      "_id": "5a6b2e17dbf6202400a3c0e6",
      "displayName": "demo1 demo1"
    },
    "__v": 0,
    "content": "A great, great story",
    "title": "This is a great post",
    "created": "2018-02-02T12:28:47.568Z"
  },
  {
    "_id": "5a731ec347f99a2400cf4751",
    "user": {
      "_id": "5a68e83ea0fbf81a00cbeb0b",
      "displayName": "demo2 demo2"
    },
    "__v": 0,
    "content": "A great, great story",
    "title": "This is a great post",
    "created": "2018-02-01T14:05:55.347Z"
  },

  ...
 ]
```

* This time we want to capture all article ID's and open a random article from the list.

```scala
// List articles
    exec(http("List articles HTML")
    .get("/modules/articles/client/views/list-articles.client.view.html")
    .headers(headers_0)
    .resources(http("List articles")
    .get("/api/articles"))
    .check(jsonPath("$[*]._id").findAll.saveAs("articleIds")))
)
    .pause(10)  
      
    // View article
    .exec(http("View article")
    .get("/api/articles/${articleIds.random()}"))
    .pause(5)
    
```
> The jsonPath query "$[*]._id" selects the _id for all items in the array. The "findAll" tells Gatling you want to store the result as an list. 
  
> Gatling comes with [built-in functions for session variables](https://gatling.io/docs/2.3/session/expression_el/). One of these, random(), can be used to select a random item from a list.

The next part of our goal is to delete the article, if the current user is the owner. Let's see what the response of the "View article" request looks like:

```json

{
  "_id": "5a74597f06b2c92500707543",
  "user": {
    "_id": "5a6b2e17dbf6202400a3c0e6",
    "displayName": "demo100 demo100"
  },
  "__v": 0,
  "content": "Bla",
  "title": "Bla",
  "created": "2018-02-02T12:28:47.568Z",
  "isCurrentUserOwner": true
}

```

Well that is convenient, there is a article property stating if the current user is owner of the article :-) Let's capture it and use it!

```scala

    // View article
    .exec(http("View article")
    .get("/api/articles/${articleIds.random()}"))
    .check(jsonPath("$.isCurrentUserOwner").saveAs("isCurrentUserOwner")))
    .check(jsonPath("$._id").saveAs("articleId"))
    .pause(5)
    
  // Delete article
     .doIf("${isCurrentUserOwner}"){
         exec(http("Delete article")
         .delete("/api/articles/${articleId}?$$state=%7B%22status%22:0%7D")
         .headers(headers_3)
        .resources(http("List articles")
        .get("/api/articles")))
     }   


```

Now add the user action to Scenarios.scala, set logging to debug and see if your logic works!

> Tip: to print [session variables](https://gatling.io/docs/2.3/session/session_api/) values add a line like this.

```scala

.exec(session =>{
      println("isCurrentUserOwner: " + session("isCurrentUserOwner").as[String])
      session
    })

```


  
```scala

  /**
    * These are the scenarios run in 'debug' mode.
    */
     

  val debugScenario = scenario("debug")
    .feed(UserFeeder.user)
    .exec(Home.userAction)
    .exec(SignIn.userAction)
//    .exec(CreateArticle.userAction)
    .exec(ListArticles.userAction)

``` 

## Bonus

Now let's try to accomplish the same without using the oh so convenient "isCurrentUserOwner" property. As you may remember, in the "Sign in" user action we captured the user id and saved it in the session variable "user_id". We now want to compare this to the user id in the article response.

First we slightly change the jsonPath:

```scala
//View article
  
.exec(http("View article")
.get("/api/articles/${articleIds.random()}"))
.check(jsonPath("$._id").saveAs("articleId"))
.check(jsonPath("$.user._id").saveAs("articleUserId")))

```  

Next, we change the [conditional statement](https://gatling.io/docs/2.3/general/scenario/#conditional-statements).

```scala
// Delete article
 
   .doIfEquals("${user_id}", "${articleUserId}") {
     exec(http("Delete article")
     .delete("/api/articles/${articleId}?$$state=%7B%22status%22:0%7D")
     .headers(headers_3)
    .resources(http("List articles")
    .get("/api/articles")))
 }   

```
Log the session variables:

```scala

.exec(session =>{
        println("user_id: " + session("user_id").as[String] + " , articleUserId: " + session("articleUserId").as[String])
        session
  })
```

## Result

If you were not able to succeed with the steps described above you find the end result [here](https://github.com/perfana/perfana-gatling-workshop/tree/workshop/exercise-7)  


[Home](index.md) 
[Previous exercise](exercise-6.md) 
[Next exercise](exercise-8.md)  


  
