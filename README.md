# MessageApp Documentation
Demo Link: http://messageapp.elasticbeanstalk.com/

**INSTRUCTIONS**
* Require a valid email address
* Register user: http://messageapp.elasticbeanstalk.com/register/register.html
* Activate account from received email
* Login (Demo Link) to access MessageApp Interface
* Repeat to create another user (use separate browser to maintain session of first user)
* Send messages between users

**DESCRIPTION**

MessageApp is a small web application for users to create/retrieve/delete messages. Additionally, the service also evaluates whether the content of a new message is a Palindrome. 
To use this application, the users first need to register then activate their account. Only active users within the application can send messages to one another.

**ARCHITECTURE**

MessageApp is Java web application that runs on tomcat. It has web resources which contain all the resources used for the client front-end. The CRUD requests are handled by Java Servlets. A simple architecture of the application is shown below:


![architecture](https://cloud.githubusercontent.com/assets/15851901/12544027/d402c790-c303-11e5-9938-535c2d758ac9.png)

All requests coming to the Servlets are sent the appropriate business logic component. The business logic components use the Morphia to communicate with the database (i.e. MongoDB).


**SEQUENCE DIAGRAMS**
![sequence diagrams](https://cloud.githubusercontent.com/assets/15851901/12543973/3c87b2ae-c303-11e5-9c9c-f7efbf4c3f14.png)

**DEPLOYMENT**

The application is deployed on Amazon's Elastic Beanstalk (with Amazon VPC). The application is hosted on Amazon Linux machine with Tomcat 7 and Java 7. The application is deployed from the Amazon's Elastic Beanstalk interface as a .war file which is generated from Eclipse IDE. All dependencies are handled by Maven for the project (in Eclipse).
MongoDB needs to installed on the same server (though it can be installed on a separate machine). The project settings are set inside web.xml and these parameters are used by the application to start-up. The following are the important parameters:

DB_URL: url to connect with mongoDB (e.g. mongodb://localhost:27017)

DB_NAME: name of the database where all the data will be stored (e.g. messageapp)

SMTP_HOST, SMTP_PORT, FROM_EMAIL, FROM_EMAIL_PASSWORD: these parameters are required to send emails to users (Future: email and password could moved to server.xml)

PROTOCOL: http or https (currently server is setup for http only)

HOST: domain name where application is being hosted (e.g. messageapp.elasticbeanstalk.com)


**REST API**

{{VARIABLE_NAME}}: identifies a variable

**_User Registration:_** 

•	POST /register?type=invite

•	Request data: {email : "abc@gmail.ca", username: "abc", password: "***"}

•	Status Codes: 200 OK, success or 530, if user already exists

**_User Activation:_**

•	GET /register?type=activate&id={{USER_ID}}

•	USER_ID is assigned by the database when user created as part of the User Registration

•	Status Codes: 200 OK, success

**_User Login:_**

•	POST /authenticator?type=login&username={{USERNAME}}&password={{PASSWORD}}

•	USERNAME and PASSWORD from User Registration

•	Status Codes: 200 OK, success

**_User Logout:_**

•	POST /authenticator?type=logout

•	Status Codes: 200 OK, success

**_NOTE:_** Remaining API calls require user to be logged in, otherwise server will return status: 401 Unauthorized

**_Get User Profile:_**

•	GET /message/data?context=user&type=profile

•	Return: { id: "56a596dd2ac78d55ce04c21d", email : "abc@gmail.ca", username: "abc",  secret: "***",  state: "ACTIVE", Role: "User", createdDate: 2016-01-25 03:32:16.783Z, updatedDate: 2016-01-25 03:32:16.783Z }

•	Status Codes: 200 OK, success

**_Find User:_**

•	GET /message/data?context=user&type=find&email={{EMAIL}}

•	EMAIL: encoded email

•	Return: { id: "56a596dd2ac78d55ce04c21d",  email : "abc@gmail.ca" }

•	Status Codes: 200 OK, success

**_Invite User:_**

•	POST /message/data?context=user&type=invite

•	Request data: { email : "abc@gmail.ca" }

•	Status Codes: 200 OK, success

**_Create Message:_**

•	POST /message/data?context=message&type=send

•	Request data: { message: {subject : "subject", content: "content", recipientIDs: {{RECIPIENT_USERIDS}}, recipientEmails: {{RECIPIENT_EMAILS}} } }

•	RECIPIENT_USERIDS: ["56a596dd2ac78d55ce04c21d "]

•	RECIPIENT_EMAILS: ["abc@gmail.ca"]

•	Return: request data updated with id, senderID, senderEmail, isPalindrome, createdDate and updatedDate

•	Status Codes: 200 OK, success

**_Open Message:_**

•	GET /message/data?context=message&type=open&id={{MESSAGE_ID}}

•	MESSAGE_ID is assigned by the database when the message is created

•	Return: { subject : "subject", content: "content", senderID: "56a596dd2ac78d55ce04c21d", senderEmail: " abc@gmail.ca", recipientIDs: ["56a596dd2ac78d55ce04c21d "], recipientEmails: ["abc@gmail.ca"], isPalindrome: false, createdDate: 2016-01-25 03:32:16.783Z, updatedDate: 2016-01-25 03:32:16.783Z }

•	Status Codes: 200 OK, success

**_Get Received Messages:_**

•	GET /message/data?context=message&type=received

•	Return: [ { subject : "subject", content: "content", senderID: "56a596dd2ac78d55ce04c21d", senderEmail: " abc@gmail.ca", recipientIDs: ["56a596dd2ac78d55ce04c21d "], recipientEmails: ["abc@gmail.ca"], isPalindrome: false, createdDate: 2016-01-25 03:32:16.783Z, updatedDate: 2016-01-25 03:32:16.783Z } ]

•	Status Codes: 200 OK, success

**_Get Sent Messages:_**

•	GET /message/data?context=message&type=sent

•	Return: [ { subject : "subject", content: "content", senderID: "56a596dd2ac78d55ce04c21d", senderEmail: " abc@gmail.ca", recipientIDs: ["56a596dd2ac78d55ce04c21d "], recipientEmails: ["abc@gmail.ca"], isPalindrome: false, createdDate: 2016-01-25 03:32:16.783Z, updatedDate: 2016-01-25 03:32:16.783Z } ]

**_Delete Received Messages:_**

•	POST /message/data?context=message&type=delete&mode=recipient

•	Request data: { selectedMessageIDs: [{{MESSAGE_IDS}}] }

•	MESSAGE_IDS: ["56a596dd2ac78d55ce04c21d "]

•	Status Codes: 200 OK, success

**_Delete Sent Messages:_**

•	POST /message/data?context=message&type=delete&mode=sent

•	Request data: { selectedMessageIDs: [{{MESSAGE_IDS}}] }

•	MESSAGE_IDS: ["56a596dd2ac78d55ce04c21d "]

•	Status Codes: 200 OK, success
