# MessageApp
Demo Link: http://messageapp.elasticbeanstalk.com/

Description

MessageApp is a small web application for users to create/retrieve/delete messages. Additionally, the service also evaluates whether the content of a new message is a Palindrome. 
To use this application, the users first need to register then activate their account. Only active users within the application can send messages to one another.

Architecture

MessageApp is Java web application that runs on tomcat. It has web resources which contain all the resources used for the client front-end. The CRUD requests are handled by Java Servlets. A simple architecture of the application is shown below:


![architecture](https://cloud.githubusercontent.com/assets/15851901/12544027/d402c790-c303-11e5-9938-535c2d758ac9.png)

All requests coming to the Servlets are sent the appropriate business logic component. The business logic components use the Morphia to communicate the database (i.e. MongoDB).


Sequence Diagrams
![sequence diagrams](https://cloud.githubusercontent.com/assets/15851901/12543973/3c87b2ae-c303-11e5-9c9c-f7efbf4c3f14.png)

Deployment

The application is deployed on Amazon's Elastic Beanstalk (with Amazon VPC). The application is hosted on Amazon Linux machine with Tomcat 7 and Java 7. The application is deployed from the Amazon's Elastic Beanstalk interface as a .war file which is generated from Eclipse IDE. All dependencies are handled by Maven for the project (in Eclipse).
MongoDB needs to installed on the server (though it can be installed on a separate machine). The project settings are set inside web.xml and these parameters are used the application to start-up. The following are the important parameters:

DB_URL: url to connect with mongoDB (e.g. mongodb://localhost:27017)

DB_NAME: name of the database where all the data will be stored (e.g. messageapp)

SMTP_HOST, SMTP_PORT, FROM_EMAIL, FROM_EMAIL_PASSWORD: these parameters are required to send emails to users (Future: email and password could moved to server.xml)

PROTOCOL: http or https (currently server is setup for http only)

HOST: domain name where application is being hosted (e.g. messageapp.elasticbeanstalk.com)


