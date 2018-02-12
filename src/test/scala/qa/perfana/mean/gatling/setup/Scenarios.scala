package qa.perfana.mean.gatling.setup

import qa.perfana.mean.gatling.useCases._
import qa.perfana.mean.gatling.feeders._
import io.gatling.core.Predef._
import scala.concurrent.duration._

/**
 * This object collects the Scenarios in the project for use in the Simulation. There are two
 * main properties in this object: acceptanceTestScenario and debugScenario. These two are
 * used in the Simulation class to setup the actual tests to run. If you wish to add
 * scenarios to either run, add them here. 
 */
object Scenarios {

  /**
   * These are the scenarios run in 'normal' mode.
   */
  val acceptanceTestScenario = scenario("Acceptance test")
    .feed(UserFeeder.user)
    .exec(Home.userAction)
    .exec(SignUp.userAction)

  /**
    * These are the scenarios run in 'debug' mode.
    */
  val debugScenario = scenario("debug")
       .feed(UserFeeder.user)
       .exec(Home.userAction)
       .exec(SignIn.userAction)
       .exec(CreateArticle.userAction)
}