[Home](index.md) 
[Previous exercise](exercise-7.md) 
[Next exercise](exercise-9.md)  

# exercise 8: Configure the workload 

We are now ready to put some load on our test environment! In this exercise we will specify the workload for the test and add some additional configuration to run in our demo test environment.  

## Workload distribution

The workload distribution of a test is specified by a combination of a scenario and a [injection](https://gatling.io/docs/2.3/general/simulation_setup/?highlight=injection) setup. In the template script, the scenario configuration is done in Scenarios.scala. When load testing an application, you try to emulate the real-live behavior of users as close a possible in your script. Gatling provides a number of building blocks to accomplish this. In our case we will use a [loop statement](https://gatling.io/docs/2.3/general/scenario/#loop-statements) and a [conditional statement](https://gatling.io/docs/2.3/general/scenario/#conditional-statements)  

 

```scala

 /**
    * These are the scenarios run in 'normal' mode.
    */
      
  val acceptanceTestScenario = scenario("acceptanceTestScenario")
    .feed(UserFeeder.user)
    .exec(Home.userAction)
    .exec(SignIn.userAction)
    .repeat(3){
          randomSwitch(
            50.0 -> exec(CreateArticle.userAction)
                        .exec(ListArticles.userAction),      
            50.0 -> exec(ListArticles.userAction)
          )
    }
    
      

```
> The randomSwitch statement is used to, based on specified percentages, let the script execute parts of the script.

> The repeat statement repeats whatever code it is wrapping. 

## Injection rate

The template script provides an (opinionated) setup to provide a load profile to a test via a Maven profile. For our load test we will use the "test-type-load" profile, that is configured in the pom.xml: 

```xml
<profile>
    <id>test-type-load</id>
    <activation>
        <activeByDefault>false</activeByDefault>
    </activation>
    <properties>
        <testType>loadTest</testType>
        <initialUsersPerSecond>0.1</initialUsersPerSecond>
        <targetUsersPerSecond>0.2</targetUsersPerSecond>
        <rampupTimeInSeconds>60</rampupTimeInSeconds>
        <constantLoadTimeInSeconds>300</constantLoadTimeInSeconds>
        <perfanaEnabled>true</perfanaEnabled>
        <testRunId>${application}-${applicationRelease}-${testType}-${testEnvironment}-${build.time}</testRunId>
        <graphitePrefix>gatling2</graphitePrefix>
    </properties>
</profile>
```

> In this profile we tell Gatling to start the test injecting 1 user per 10 seconds (initialUsersPerSecond), and ramp this load up to 2 users per 10 seconds (targetUsersPerSecond) in 60 seconds (rampupTimeInSeconds). The  load is then kept constant at 2 started users per seconds for 300 seconds (constantLoadTimeInSeconds)
  
The values from the Maven profile are transferred to the Gatling inject API in the Setup.scala file:

```scala
/**
  * Injects the required settings into a single ScenarioBuilder.
  * @param scn the Scenario to initialize
  * @return the initialized PopulationBuilder
  */
def setupSingleScenario(scn: ScenarioBuilder): PopulationBuilder = scn.inject(
rampUsersPerSec(Configuration.initialUsersPerSecond) to Configuration.targetUsersPerSecond during (Configuration.rampupTimeInSeconds),
constantUsersPerSec(Configuration.targetUsersPerSecond) during(Configuration.constantLoadTimeInSeconds)
``` 
> If you would like use a different injection profile, make the necessary changes here.

## Additional configuration

The perfana-gatling-maven-plugin will inform the Perfana dashboard via a REST API when a test is running and when it's finished. In the properties section of the script's pom.xml some configuration needs to be done to enable this communication.


```xml
   <!-- Perfana url  -->
   <perfanaUrl>http://localhost:4000</perfanaUrl>
``` 
> In our test setup, Perfana runs at localhost, port 3000

```xml
  <!-- Settings for the Perfana Dashboard -->
    <application>Mean</application>
    <applicationRelease>1.0</applicationRelease>
```
> The configured application name will be used in Perfana to store all test runs. The applicationRelease can also be set dynamically by Maven or overridden via Jenkins. 

In the gatling.conf file (/src/test/resources) we need to configure the Graphite writer to send data to our Graphite instance, listening on localhost, port 2004.

```
 graphite {
      #light = false              # only send the all* stats
      host = "localhost"
      port = 2004
      protocol = "tcp"           # The protocol used to send data to Carbon (currently supported : "tcp", "udp")
      rootPathPrefix = "gatling2" # The common prefix of all metrics sent to Graphite
      #bufferSize = 8192          # GraphiteDataWriter's internal data buffer size, in bytes
      writeInterval = 10          # GraphiteDataWriter's write interval, in seconds
 }

```

Now let's start a load test!

> Make sure to disable DEBUG/TRACE in logback.xml when running a test under load!

```  
mvn clean install perfana-gatling:integration-test -Ptest-env-local,test-type-load
```

In our next exercise we will have a look at the test results in Perfana!

## Result

If you were not able to succeed with the steps described above you find the end result [here](https://github.com/perfana/perfana-gatling-workshop/tree/workshop/exercise-8)  


[Home](index.md) 
[Previous exercise](exercise-7.md) 
[Next exercise](exercise-9.md)  


  
