# Perfana workshop environment

A `docker-compose` setup to run a `Perfana` environment to be used in the Perfana Gatling Workshop.

## Edit hosts file  

Add these lines to your hosts file

```
127.0.0.1       grafana
127.0.0.1       jenkins
```

## Start

* Start all the containers with: `docker-compose up -d`

The end result will be an environment with the following started docker containers:

| Container  	| Description                                            	| Exposed port|
|------------	|--------------------------------------------------------	|-------	|
| Perfana 	  | Performance dashboard application                      	| 4000    	|
| Grafana 	  | Monitoring dashboard application                      	| 3000    	|
| mongodb    	| Database to store dashboard configurations           		 | 27017 	|
| jenkins    	| CI server to start demo scripts     	                   | 8080  	|
| mean       	| Demo application to run performance tests against 	     | 3001  	|
| influxdb      | Time based metrics store                          	| 8086 / 2003      |
| telegraf    	|    Metric collection agent 	| -   |

* Additional step for Windows users: run bootstrap-windows.sh (requires WSL or Cygwin)

## Instructions
* Open Perfana dashboard at ```http://localhost:4000```
* Sign in with user/password admin@example.com/admin
* Click "Grafana configuration" in the sidebar
* Click  "Sync all Grafana instance dashboards" (refresh icon). This should sync Perfana with the Grafana container running at ```localhost:3000```. 


## Windows users 
* Don't forget to share your drive when Docker asks for it during start up of the containers!
