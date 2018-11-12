package qa.perfana.mean.gatling.feeders


import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

object UserFeeder {

  val user = csv("data/users.csv").circular


}
