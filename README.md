Please first read the whole documentation.
The documentation is based on software engineering documentation, e.g., ARC42, Test-Scripts, etc..

The program can run when:

1. A stable MySQL connection on port:3306 should exist.

2. If there is a Database already initialized, please clear it by removing all tables in it or you can completely remove the Database schema. 

3. All JUnit5 tests have been run completely and successfully. 

4. Starting the web application with the following commands: 

 <code>mvn spring-boot:run</code> will start the web-application 
 using the local MySQL database. MySQL connection with port :3306 must exist. The Database skeletondb will be 
 initialized automatically, you don’t have to initialize it by yourself. Application has to start on localhost:8080. 
 Please make sure, that you set yours database connection password correctly under the application.properties 
 file (spring.datasource.password=${ΜYSQL_PASSWORD: < your password>}. 

 <code>docker compose up --build</code> to create the 
 image and to run the database and the web app containers. Application has to start on localhost:8081. 
 In both cases above, the database then will automatically be initialized using the data.sql file under the 
 package /src/main/resources/data.sql

5. For further instructions like usernames, roles, password, etc... please check our Test Script file.

6. Good luck and have fun! :)
