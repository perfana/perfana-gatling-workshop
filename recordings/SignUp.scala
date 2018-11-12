
import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class SignUp extends Simulation {

	val httpProtocol = http
		.baseURL("http://localhost:3333")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.(t|o)tf""", """.*\.png""", """.*\.woff2"""), WhiteList())
		.acceptHeader("text/html")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("en-US,en;q=0.9,nl;q=0.8,de;q=0.7")
		.userAgentHeader("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.119 Safari/537.36")

	val headers_0 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_1 = Map("Accept" -> "application/json, text/plain, */*")

	val headers_5 = Map(
		"Accept" -> "application/json, text/plain, */*",
		"Content-Type" -> "application/json;charset=UTF-8",
		"Origin" -> "http://localhost:3333")



	val scn = scenario("SignUp")
		// Home
		.exec(http("request_0")
			.get("/")
			.headers(headers_0)
			.resources(http("request_1")
			.get("/modules/core/client/views/header.client.view.html")
			.headers(headers_1),
            http("request_2")
			.get("/modules/core/client/views/home.client.view.html")))
		.pause(38)
		// Sign up
		.exec(http("request_3")
			.get("/modules/users/client/views/authentication/authentication.client.view.html")
			.resources(http("request_4")
			.get("/modules/users/client/views/authentication/signup.client.view.html")))
		.pause(48)
		.exec(http("request_5")
			.post("/api/auth/signup")
			.headers(headers_5)
			.body(RawFileBody("request-bodies/SignUp_0005_request.txt")))

	setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}