package qa.perfana.mean.gatling.configuration

import java.util.concurrent.TimeUnit

import io.gatling.commons.stats.KO
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import qa.perfana.mean.gatling.setup._


/**
 * Contains the configuration needed to build the Scenarios to run. All configuration is read from
 * the resources/application.conf file. 
 */
object Configuration {


  val isDebugActive = System.getProperty("debug") == "true"
  val useProxy = System.getProperty("useProxy") == "true"
  val graphitePrefix = System.getProperty("gatling.data.graphite.rootPathPrefix")


  System.out.println("Script settings:")
  System.out.println("useProxy: " + useProxy)
  System.out.println("graphitePrefix: " + graphitePrefix)
  System.out.println("debug: " + isDebugActive)

  //  get targetBaseUrl
  val targetBaseUrl = System.getProperty("targetBaseUrl")
  System.out.println("targetBaseUrl: " + targetBaseUrl)

  // get testRunId
  val testRunId = System.getProperty("testRunId")
  System.out.println("testRunId: " + testRunId)

  // get load figures
  val initialUsersPerSecond  = System.getProperty("initialUsersPerSecond").toDouble
  val targetUsersPerSecond  = System.getProperty("targetUsersPerSecond").toDouble
  val rampupTimeInSeconds = ( System.getProperty("rampupTimeInSeconds").toLong, TimeUnit.SECONDS )
  val constantLoadTimeInSeconds = ( System.getProperty("constantLoadTimeInSeconds").toLong, TimeUnit.SECONDS )

  System.out.println("initialUsersPerSecond: " + initialUsersPerSecond)
  System.out.println("targetUsersPerSecond: " + targetUsersPerSecond)
  System.out.println("rampupTimeInSeconds: " + rampupTimeInSeconds)
  System.out.println("constantLoadTimeInSeconds: " + constantLoadTimeInSeconds)


  /**
    * This determines what scenario to use as baseScenario based on the profile
    * selected when starting the test
    */

  val scenario = System.getProperty("scenario")

  val baseScenario = scenario match {

    case "debug" => Scenarios.debugScenario
    case "acceptance"  => Scenarios.acceptanceTestScenario

    case _ => Scenarios.acceptanceTestScenario


  }

  private val baseHttpProtocol = http
    .baseURL(Configuration.targetBaseUrl)
    .acceptHeader("text/html")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.9,nl;q=0.8,de;q=0.7")
    .userAgentHeader("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.119 Safari/537.36")
    .inferHtmlResources(WhiteList(""".*""" + Configuration.targetBaseUrl + """.*"""))
    .silentResources

  private val baseHttpDebugProtocol = http
    .baseURL(Configuration.targetBaseUrl)
    .acceptHeader("text/html")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.9,nl;q=0.8,de;q=0.7")
    .userAgentHeader("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.119 Safari/537.36")
    .inferHtmlResources(WhiteList(""".*""" + Configuration.targetBaseUrl + """.*"""))
    .silentResources
    .extraInfoExtractor(ExtraInfo => {
      if(ExtraInfo.status == KO)
        println("httpCode: " + ExtraInfo.response.statusCode + ", body: "+ ExtraInfo.response.body)
      Nil
    })

  def httpDebugProtocol ={
    /* Add proxy if specified */
    if(Configuration.useProxy) {
      System.out.println("Using proxy at localhost port 8888!")
      baseHttpDebugProtocol.proxy(Proxy("localhost", 8888).httpsPort(8888))
    }else{
      baseHttpDebugProtocol
    }
  }

  def httpProtocol ={
    (baseHttpDebugProtocol)
  }



}
