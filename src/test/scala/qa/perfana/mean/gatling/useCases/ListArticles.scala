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
        .get("/api/articles")
        .check(jsonPath("$[*]._id").findAll.saveAs("articleIds"))
      )

    )
    // View article
    .exec(http("View article")
    .get("/api/articles/${articleIds.random()}")
    .check(jsonPath("$.isCurrentUserOwner").saveAs("isCurrentUserOwner"))
    .check(jsonPath("$._id").saveAs("articleId"))
    .check(jsonPath("$.user._id").saveAs("articleUserId"))

  )

    // Print session variable

    .exec(session =>{
      println("isCurrentUserOwner: " + session("isCurrentUserOwner").as[String])
      session
    })

    // Bonus
//    .exec(session =>{
//      println("user_id: " + session("user_id").as[String] + " , articleUserId: " + session("articleUserId").as[String])
//      session
//    })


    .pause(5)

    // Delete article
      .doIfEquals("${user_id}", "${articleUserId}") {  // Bonus
      //    .doIfEquals("${isCurrentUserOwner}", "true"){
       exec(http("Delete article")
      .delete("/api/articles/${articleId}?$$state=%7B%22status%22:0%7D")
      .headers(headers_3)
      .resources(http("List articles")
        .get("/api/articles")
      ))
    }
}