# MessageApp

Description

MessageApp is a small web application for users to create/retrieve/delete messages. Additionally, the service also evaluates whether the content of a new message is a Palindrome. 
To use this application, the users first need to register then activate their account. Only active users within the application can send messages to one another.

Architecture

MessageApp is Java web application that runs on tomcat. It has web resources which contain all the resources used for the client front-end. The CRUD requests are handled by Java Servlets.

A simple architecture of the application is shown below:
![architecture](https://cloud.githubusercontent.com/assets/15851901/12544027/d402c790-c303-11e5-9938-535c2d758ac9.png)

Sequence Diagrams
![sequence diagrams](https://cloud.githubusercontent.com/assets/15851901/12543973/3c87b2ae-c303-11e5-9c9c-f7efbf4c3f14.png)
