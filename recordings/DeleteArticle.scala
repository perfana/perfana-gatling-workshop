
import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class DeleteArticle extends Simulation {

	val httpProtocol = http
		.baseURL("http://localhost:3333")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.(t|o)tf""", """.*\.png""", """.*\.woff2"""), WhiteList())
		.acceptHeader("application/json, text/plain, */*")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("en-US,en;q=0.9,nl;q=0.8,de;q=0.7")
		.userAgentHeader("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36")

	val headers_0 = Map("Accept" -> "text/html")

	val headers_3 = Map("Origin" -> "http://localhost:3333")



	val scn = scenario("DeleteArticle")
		// List articles
		.exec(http("request_0")
			.get("/modules/articles/client/views/list-articles.client.view.html")
			.headers(headers_0)
			.resources(http("request_1")
			.get("/api/articles")))
		.pause(22)
		// Open article
		.exec(http("request_2")
			.get("/api/articles/5a745fc506b2c92500707544"))
		.pause(18)
		// Delete article
		.exec(http("request_3")
			.delete("/api/articles/5a745fc506b2c92500707544?$$state=%7B%22status%22:0%7D")
			.headers(headers_3)
			.resources(http("request_4")
			.get("/api/articles")))

	setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}