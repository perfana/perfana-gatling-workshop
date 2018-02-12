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