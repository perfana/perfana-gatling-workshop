[Home](index.md) 
[Previous exercise](exercise-11.md) 
  

# Continuous performance validation 

To setup your continuous performance validation you can include a load test in your continuous integration pipeline. The perfana-gatling-maven-plugin has an option to assert the consolidated KPI results in Perfana and pass or fail a build based on this assertion.
  
This adds feedback on performance of an application to the feedback loop, allowing developers to act immediately if a Key Performance Indicator is not up to par for a build due to the latest code changes. On the other hand, if a build passes, the team knows that the application still adheres to the configured KPI's and can continue developing without the need to look into the load test results.          

## Perfana-gatling-maven-plugin configuration

By using the "assert-results" profile the perfana-gatling-maven-plugin will get the consolidated results for a test run after the test run has finished.

```  
mvn clean install perfana-gatling:integration-test -Ptest-env-local,test-type-load,assert-results
```
The consolidated test run results are exposed via a REST API by Perfana. The response will look something like this

```json
{
	"requirements": {
		"result": false,
		"deeplink": "http://localhost:4000/testrun/Mean-1.0-loadTest-local-20180214-112643/requirements?application=Mean&testType=loadTest&testEnvironment=local"
	},
	"benchmarkPreviousTestRun": {
		"result": true,
		"deeplink": "http://localhost:4000/testrun/Mean-1.0-loadTest-local-20180214-112643/benchmarks/compared-to-previous-test-run?application=Mean&testType=loadTest&testEnvironment=local"
	},
	"benchmarkBaselineTestRun": {
		"result": true,
		"deeplink": "http://localhost:4000/testrun/Mean-1.0-loadTest-local-20180214-112643/benchmarks/compared-to-baseline-test-run?application=Mean&testType=loadTest&testEnvironment=local"
	}
}
```
If one the assertions has a "result: false", the perfana-gatling-maven-plugin will fail the build and print the reason for the failure to the build log:

```
[ERROR] Failed to execute goal qa.perfana:perfana-gatling-maven-plugin:0.0.12:integration-test (default-cli) on project gatling-mean: One or more Perfana assertions are failing: 
[ERROR] Requirements failed: http://localhost:4000/testrun/Mean-1.0-loadTest-local-20180214-143119/requirements?application=Mean&testType=loadTest&testEnvironment=local

```

## Jenkins setup

The demo environment includes a Jenkins instance containing a demo pipeline job. This job can be found at

```
http://localhost:8080/job/PERFANA-GATLING-DEMO/
```

Click "configure" to see a simple example of how to trigger a Gatling script from a Jenkins pipeline groovy script.


To check out the Jenkins-Perfana integration run the build a few times. The tests will show up in Perfana under test environment "acc" and the Jenkins build ID will be used as test run ID. 

When the run has finished you will see a new property in the test run summary view: "CI build result"
You can use this url to deeplink from Perfana to the Jenkins build results page.  


[Home](index.md) 
[Previous exercise](exercise-11.md) 
