package qa.perfana.mean.gatling.useCases


import io.gatling.core.Predef._
import io.gatling.http.Predef._

object SignIn {

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
        .body(ElFileBody("SignIn.txt"))
        .check(status.in(200,400))
        .check(jsonPath("$._id").optional.saveAs("user_id")))

      .doIf(!_.contains("user_id")) {
        exec(SignUp.userAction)

      }
}