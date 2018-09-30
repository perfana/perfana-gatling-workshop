[Home](index.md) 
[Next exercise](exercise-2.md)  


# exercise 1: Maven setup

In this exercise we will generate a template Gatling script using a Maven archetype.

## Prerequisites 

* Maven installation

## Install archetype

* Clone the perfana-gatling-maven-archetype from [Github](https://github.com/perfana/perfana-gatling-maven-archetype)
* Install the perfana-gatling-maven-archetype

```
mvn clean install
```

## Generate template script

In a terminal, execute the following command from a directory where you would like to store your script:


```  
   mvn org.apache.maven.plugins:maven-archetype-plugin:2.4:generate -B \
  -DarchetypeGroupId=qa.perfana \
  -DarchetypeArtifactId=perfana-gatling-maven-archetype \
  -DarchetypeVersion=0.0.3 \
  -DgroupId=qa.perfana \
  -DartifactId=gatling-mean \
  -Dversion=1.0-SNAPSHOT \
  -Dpackage=qa.perfana.mean \
  -DsimulationClassName=Mean \
  -Dapplication=Mean \
  -DapplicationRelease=1.0   
```

## Install perfana-gatling-maven-plugin

To communicate with Perfana the script will use the perfana-gatling-maven-plugin, a fork from the [gatling-maven-plugin](https://github.com/gatling/gatling-maven-plugin). For our workshop we need to install this plugin locally:

* Clone the perfana-gatling-maven-plugin from [Github](https://github.com/perfana/gatling-maven-plugin)
* Install the perfana-gatling-maven-plugin

```
mvn clean install
```


## Result

The result of this exercise is a template Gatling script. If you were not able to generate the template script it can be found [here](https://github.com/perfana/perfana-gatling-workshop/tree/workshop/exercise-1)  
  
[Home](index.md) 
[Next exercise](exercise-2.md)  
  
