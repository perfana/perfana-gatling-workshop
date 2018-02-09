
import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class SignIn extends Simulation {

	val httpProtocol = http
		.baseURL("http://localhost:3333")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.(t|o)tf""", """.*\.png""", """.*\.woff2"""), WhiteList())
		.acceptHeader("text/html")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("en-US,en;q=0.9,nl;q=0.8,de;q=0.7")
		.userAgentHeader("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.119 Safari/537.36")

	val headers_2 = Map(
		"Accept" -> "application/json, text/plain, */*",
		"Content-Type" -> "application/json;charset=UTF-8",
		"Origin" -> "http://localhost:3333")



	val scn = scenario("SignIn")
		// Sign In
		.exec(http("request_0")
			.get("/modules/users/client/views/authentication/authentication.client.view.html")
			.resources(http("request_1")
			.get("/modules/users/client/views/authentication/signin.client.view.html")))
		.pause(19)
		.exec(http("request_2")
			.post("/api/auth/signin")
			.headers(headers_2)
			.body(RawFileBody("SignIn_0002_request.txt")))

	setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}