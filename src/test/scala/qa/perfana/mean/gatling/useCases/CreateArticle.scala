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
      .body(RawFileBody("request-bodies/CreateArticle.txt"))
      .check(jsonPath("$._id").saveAs("articleId"))
      .resources(http("View article")
        .get("/api/articles/${articleId}"),
        http("View article HTML")
          .get("/modules/articles/client/views/view-article.client.view.html")
          .headers(headers_0)
      ))
}
