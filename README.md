# **JavaEE website**

### Goal

The aim of this project is to provide a hand-coded JavaEE website.<br/>
This website lets an author to create events and to invite some gests to subscribe to those events. Each author can register or login to the website with a password which will be hashed before beeing stored in a database.

### Front-end

The front-end uses html, css and javascript. A lot of components like buttons or tables use PrimeFaces (a UI framework).

### Back-end

The back-end is managed by JSF.

### Persistence

The data persistence is managed by JPA.

# Getting Started

Clone the project into your workspace.

# Prerequisites

## The java development kit

Download the java development kit: jdk 1.8.0_111.<br/>
Create an environment variable called JAVA_HOME pointing on the bin folder of the jdk.<br/>
Add the variable to the Path.

## The build automation tool

Download the build automation tool: apache-maven-3.3.9-bin.<br/>
Create an environment variable called MAVEN_HOME pointing on the bin folder of maven.<br/>
Add the variable to the Path.

## The application server

Download the application server: wildfly-10.0.0.Final.<br/>
Create an environment variable called JBOSS_HOME pointing on the bin folder of wildfly.<br/>
Go to the bin folder of wildfly.<br/>
Add a new user:<br/>
* For linux, run: `add-user.sh`
* For windows, run: `add-user.bat`

# Installing

## Starting the server

Go to the bin folder of wildfly.<br/>
* Launcher for linux: `standalone.sh`
* Launcher for windows: `standalone.bat`

## Connection to the database

### Create the database:

<ul><liFor Linux:<br/>
In the shell, go to the folder /src/dataBase.<br/>
Start MySQL: `sudo service mysql start`.<br/>
Start the MySQL console: `mysql -u root -p`.<br/>
Type the following commands:</li></ul>

	mysql> CREATE DATABASE eventsdb;
	mysql> SOURCE eventsdb.sql;
	mysql> quit;
<ul><li>For Windows:<br/>
Download easyPHP and run it.<br/>
Open the administration: http://127.0.0.1/home/.<br/>
Then, open the administration module MySQL : PhpMyAdmin 4.1.4.<br/>
Create a new database named eventsdb.<br/>
Import the .sql file located into /src/dataBase.</li></ul>

### Setup wildfly to connect to the database:

Download the driver mysql-connector-java-5.1.4: https://dev.mysql.com/downloads/file/?id=465644.<br/>
<br/>**1.** Open the administration of wildfly: http://localhost:9990.<br/>
<br/>**2.** Go to "Deployments" and click on "Add".<br/>
Upload a new deployment.<br/>
Find mysql-connector-java-5.1.40-bin.jar.<br/>
<br/>**3.** Go to "Configuration", then "Subsystems", then "DataSources", then "Non-XA". Click on "Add".<br/>
Choose DataSource: MySQL Datasource<br/>
On step 1/3, set:

	Name: eventsdb
	JNDI Name: java:jboss/DataSources/eventsdb
On step 2/3 go to the tab "Detected Driver" and choose: mysql-connector-java-5.1.40-bin.jar_com.mysql.jdbc.Driver_5_1

## Connection to the mail box

### Setup wildfly to connect to the mail box:

Open the administration of wildfly: http://localhost:9990.<br/>
<br/>**1.** Go to "Configuration", then "Socket Binding" and click on "View".<br/>
Click on "View>", then on "OutBound Remote", then on "Add" and set:

	Name: (what ever name you want, for ex: mail-smtp-gmail)
	Host: smtp.gmail.com
	Port: 465 (587 should work too)
*Save*<br/>
<br/>**2.** Go to "Configuration", then "Subsystems", then"Mail", click on "Add" and set:

	Name: java:jboss/mail/Gmail
	JNDI Name: java:jboss/mail/Gmail (name of the resource used in 'EmailObserver.java')
*Save*<br/>
<br/>Click on your Mail Session, then click on "View", then on "Edit" and set:

	Socket Binding: (Socket Binding name, in our ex: mail-smtp-gmail)
	Type: smtp
	Username: (youremail@gmail.com)
	Password: (yourEmailPassword)
	Use SSL (tick)
*Save*<br/>
<br/>**3.** Restart wildfly (in the shell: "Ctrl+C", then "O", then <a href="#starting-the-server" title="go to the 'Starting the server' chapter">start the server</a>).

### Setup your mail box to accept external applications:

Go to https://myaccount.google.com/lesssecureapps and authorize less secure applications.</br>
Check that the outgoing mails analysis is disable in your antivirus.

# Running

## Starting the database

* For Linux:<br/>
Start MySQL: `sudo service mysql start`.
* For Windows:<br/>
Run easyPHP.

## Starting the server

Go to the bin folder of wildfly.<br/>
* Launcher for linux: `standalone.sh`
* Launcher for windows: `standalone.bat`

## Deployment

In the shell, go to the root folder of the java project, containing the pom.xml.<br/>
Type the command: `mvn clean wildfly:deploy`.

## Testing

In a web browser, go to http://localhost:8080.

# Built With

* [Primefaces](http://www.primefaces.org "link to http://www.primefaces.org") - The web framework used.
* [Maven](https://maven.apache.org "link to https://maven.apache.org") - Dependency Management.

# Authors

* **Jérémy Pansier** - *Initial work* - [JeremyPansier](https://github.com/JeremyPansier "link to Jérémy's profile")

See also the list of [contributors](https://github.com/JeremyPansier/JavaEE_website/contributors "link to the contributors of the project") who participated in this project.

# Acknowledgments

* Special thanks to my brother Gwenall Pansier who helped me from my early days as developer.
* Thanks to Thomas gros, the trainer who teached me basics in JavaEE.
* Thanks to Pascal Cunin, my schoolmate, with whom I started this project.
* Thanks to [PurpleBooth](https://github.com/PurpleBooth "link to PurpleBooth's profile") for the template that I used to write this readme.
