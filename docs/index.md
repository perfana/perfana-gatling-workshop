# Workshop performance testing with Gatling and Perfana

## Goals

This workshop is aiming to get you acquainted with the Perfana performance test framework. You will learn how to create a load test script using Gatling and how to create a continuous performance validation setup that can be integrated in your CI/CD pipelines.

## Scope

The workshop covers the following subjects:

* Create a Gatling script using the Perfana-Gatling template script
* Setting up your aplication in Perfana
* Setting up continuous performance validation by integrating your load test in your Jenkins CI pipeline

It does NOT cover:

* How to create Grafana dashboards. Please refer to the [Grafana documentation](http://docs.grafana.org/guides/getting_started/) to get up to speed with Grafana. 



## Prerequisites
* [Docker](https://www.docker.com/) and [Docker Compose](https://docs.docker.com/compose/) installed
* JDK8+ installed
* Maven installed
* An IDE with Scala support, for instance [IntelliJ IDEA](https://www.jetbrains.com/idea/) or [Visual Studio Code](https://code.visualstudio.com/)
* Gatling bundle installed, get instructions [here](https://gatling.io/docs/current/quickstart/)



## Setting up your environment

For your convenience a complete Perfana test / demo environment is available in this repository: [https://github.com/perfana/perfana-gatling-workshop](https://github.com/perfana/perfana-gatling-workshop)  
Please clone this repository and follow the instructions in the readme file in the "test-environment" folder.


## Exercises

### Gatling

[exercise 1: Maven set up](exercise-1.md) 

[exercise 2: Recording script with Gatling recorder](exercise-2.md)

[exercise 3: Integrate recorded script in template script](exercise-3.md)

[exercise 4: Inject data into the script](exercise-4.md)

[exercise 5: Checks & conditional execution](exercise-5.md)

[exercise 6: Data correlation](exercise-6.md)

[exercise 7: Add logic to the script](exercise-7.md)

[exercise 8: Configure the workload](exercise-8.md)

### Perfana

[exercise 9: View test results in Perfana](exercise-9.md)

[exercise 10: Adding Key Performance Indicators](exercise-10.md)

[exercise 11: Test run comparison & reports](exercise-11.md)

## Continuous performance validation

[exercise 12: Continuous performance validation](exercise-12.md)

