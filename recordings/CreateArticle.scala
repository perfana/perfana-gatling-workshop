
import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class AddArticle extends Simulation {

	val httpProtocol = http
		.baseURL("http://localhost:3333")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.(t|o)tf""", """.*\.png"""), WhiteList())
		.acceptHeader("text/html")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("en-US,en;q=0.9,nl;q=0.8,de;q=0.7")
		.userAgentHeader("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.119 Safari/537.36")

	val headers_1 = Map(
		"Accept" -> "application/json, text/plain, */*",
		"Content-Type" -> "application/json;charset=UTF-8",
		"Origin" -> "http://localhost:3333")

	val headers_2 = Map("Accept" -> "application/json, text/plain, */*")

	val headers_4 = Map(
		"Accept" -> "*/*",
		"Origin" -> "http://localhost:3333")



	val scn = scenario("AddArticle")
		// Create Article
		.exec(http("request_0")
			.get("/modules/articles/client/views/form-article.client.view.html"))
		.pause(42)
		// Submit article
		.exec(http("request_1")
			.post("/api/articles")
			.headers(headers_1)
			.body(RawFileBody("AddArticle_0001_request.txt"))
			.resources(http("request_2")
			.get("/api/articles/5a818407290572240079992a")
			.headers(headers_2),
            http("request_3")
			.get("/modules/articles/client/views/view-article.client.view.html"),
            http("request_4")
			.get("/lib/bootstrap/dist/fonts/glyphicons-halflings-regular.woff2")
			.headers(headers_4)))

	setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}