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
        .body(RawFileBody("SignUp.txt")))
}