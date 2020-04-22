[Home](index.md) 
[Previous exercise](exercise-1.md) 
[Next exercise](exercise-3.md)  

# exercise 2: Recording script with Gatling recorder

In this exercise we will use the Gatling recorder to record some user actions. We will then integrate the recorded code in the template script.

## Prerequisites 

* Gatling bundle installed, get instructions [here](https://gatling.io/docs/current/quickstart/)

## Steps

* Start the Gatling recorder from your Gatling installation folder

```
/bin/recorder.sh
```

* Set Class name to "SignUp"
* In the black list section, click "No static resources"
* Click "Start!"
* Configure your browser to use a proxy at localhost:8000

> For Windows users: disable you automatic proxy configuration if enabled and use manual configuration to set proxy to "localhost", port 8000. Uncheck the option to bypass local addresses.
 
* In the Gatling recorder add a tag named "Home"
* In your browser, open the homepage of the Mean demo app

``` 
http://localhost:3333 
```
* In the Gatling recorder you should see some recorded events now
* Add a tag "SignUp"
* In the Mean app, click "Sign up"
* Use the following information to sign up

```  
   First Name: demo1
   Last Name: demo1
   Email: demo1@demo.com
   username: demo1
   password: demo1Password!
```
* After signing up, click "Stop &#38; Save"

The recorded script can be found in the /user-files folder in your Gatling installation directory. This directory contains three subdirectories:
* bodies
* data
* simulations

In the simulations directory a file called SignUp.scala can be found. Create a directory "recordings" in the root of your project and copy the file there. We will use this file in the next exercise. 
The Gatling recorder also created a file in the "bodies" directory, "SignUp_000n_request.txt". Copy this file to the /src/test/resources/request-bodies directory in your template script and rename it to "SignUp.txt".

Now repeat these steps for the user action "Sign in", where you sign in with the user you just created. 

> Don't forget to copy the SignIn request body to the bodies directory. 

> Recording using the Gatling recorder proxy can be challenging on some OSes and proxy configurations.  
The Gatling recorder also supports [HAR](https://en.wikipedia.org/wiki/HAR_(file_format)) file importing to generate a Gatling script from. You can use a tool to your liking to create a HAR file. Tool examples: [Chrome DevTools](https://developers.google.com/web/tools/chrome-devtools), [Fiddler](https://www.telerik.com/fiddler) or [Charles Proxy](http://www.charlesproxy.com/)

## Result

If you were not able to succeed with the steps described above you find the end result [here](https://github.com/perfana/perfana-gatling-workshop/tree/workshop/exercise-2)  

[Home](index.md) 
[Previous exercise](exercise-1.md) 
[Next exercise](exercise-3.md)  
   