# **JavaEE website**

[![Build Status](https://travis-ci.org/JeremyPansier/JavaEE_website.svg?branch=master)](https://travis-ci.org/JeremyPansier/JavaEE_website)
[![Quality Gate](https://sonarcloud.io/api/badges/gate?key=com.website%3Awebsite)](https://sonarcloud.io/dashboard/index/com.website:website)
[![Quality Gate](https://sonarcloud.io/api/badges/measure?key=com.website%3Awebsite&metric=lines)](https://sonarcloud.io/component_measures?id=com.website%3Awebsite&metric=lines)
[![Quality Gate](https://sonarcloud.io/api/badges/measure?key=com.website%3Awebsite&metric=comment_lines_density)](https://sonarcloud.io/component_measures?id=com.website%3Awebsite&metric=comment_lines_density)
[![Quality Gate](https://sonarcloud.io/api/badges/measure?key=com.website%3Awebsite&metric=sqale_rating)](https://sonarcloud.io/component_measures?id=com.website%3Awebsite&metric=sqale_rating)
[![Quality Gate](https://sonarcloud.io/api/badges/measure?key=com.website%3Awebsite&metric=security_rating)](https://sonarcloud.io/component_measures?id=com.website%3Awebsite&metric=security_rating)

## Table of Contents

* [Overview](#overview)
	* [General Purpose](#general-purpose)
	* [Use case](#use-case)
	* [Data model](#data-model)
* [Getting Started](#getting-started)
* [Prerequisites](#prerequisites)
	* [The java development kit](#the-java-development-kit)
	* [The build automation tool](#the-build-automation-tool)
	* [The application server](#the-application-server)
* [Installing](#installing)
	* [Starting the server](#starting-the-server)
	* [Connection to the database](#connection-to-the-database)
	* [Connection to the mail box](#connection-to-the-mail-box)
* [Running](#running)
	* [Starting the database](#starting-the-database)
	* [Deployment](#deployment)
* [Testing](#testing)
	* [Manual test](#manual-test)
* [Built With](#built-with)
* [Supported browsers](#supported-browsers)
* [Authors](#authors)
* [Acknowledgments](#acknowledgments)
* [License](#license)

## Overview

### General Purpose

The aim of this project is to provide a hand-coded JavaEE website.<br/>
This website lets an author to create events and to invite some guests to subscribe to those events. Each author can register or login to the website with a password which will be hashed before being stored in a database.

### Use case
![use case](modelling/useCase.png "The general use case diagram")

### Data model
![data model](modelling/dataModel.png "The data model class diagram")

## Getting Started

Clone the project into your workspace.

## Prerequisites

### The java development kit

Download the java development kit: jdk 1.8.0_111.<br/>
Create an environment variable called JAVA_HOME pointing on the bin folder of the jdk.<br/>
Add the variable to the Path.

### The build automation tool

Download the build automation tool: apache-maven-3.3.9-bin.<br/>
Create an environment variable called MAVEN_HOME pointing on the bin folder of maven.<br/>
Add the variable to the Path.

### The application server

Download the application server: wildfly-10.0.0.Final.<br/>
Create an environment variable called JBOSS_HOME pointing on the bin folder of WildFly.<br/>
Go to the bin folder of WildFly.<br/>
To add a new user, run:

For Linux   | For Windows
----------- | ---
add-user.sh | add-user.bat

## Installing

### Starting the server

Go to the bin folder of WildFly and run:

For Linux     | For Windows    | For a remote Linux server
------------- | -------------- | ---
standalone.sh | standalone.bat | standalone.sh -b 0.0.0.0

### Connection to the database

**create the database:**

Find the database source file ([website.sql](https://github.com/JeremyPansier/JavaEE_website/tree/master/database/website.sql)) containing the tables creation script. It will be called $FILE and its path will be called $PATH in the following table.

For Linux                                               | For Windows
------------------------------------------------------- | ---
Download MySQL: <code>sudo apt install mysql-server</code>.     | Download easyPHP and run it.
Start MySQL: <code>sudo service mysql start</code>.     | [Open the administration](http://127.0.0.1/home/ "easyPHP administration interface").
Start the MySQL console: <code>mysql -u root -p</code>. | Then, open the administration module MySQL : PhpMyAdmin 4.1.4.
mysql> CREATE DATABASE website;                         | Create a new database named website.
mysql> USE website;                                     | Select the database: website.
mysql> SOURCE $PATH$FILE;                               | Go to the 'import' tab.
mysql> quit;                                            | Import $FILE file located into $PATH.

**setup WildFly to connect to the database:**

Download the driver [mysql-connector-java-5.1.4](https://dev.mysql.com/downloads/file/?id=465644 "MySQL connector download page").<br/>
<br/>**1.** [Open the administration of WildFly](http://localhost:9990 "The WildFly administration interface").<br/>
<br/>**2.** Go to "Deployments" and click on "Add".<br/>
Upload a new deployment.<br/>
Find mysql-connector-java-5.1.40-bin.jar.<br/>
<br/>**3.** Go to "Configuration", then "Subsystems", then "DataSources", then "Non-XA". Click on "Add".<br/>
Choose DataSource: MySQL Datasource<br/>
On step 1/3, set:

	Name: website
	JNDI Name: java:jboss/DataSources/website (name used in persistence.xml <jta-data-source>)
On step 2/3 go to the tab "Detected Driver" and choose: mysql-connector-java-5.1.40-bin.jar_com.mysql.jdbc.Driver_5_1<br/>
On step 3/3, set:

	Connection URL: jdbc:mysql://localhost:3306/website
	Username: (yourDatabaseUsername)
	Password: (yourDatabasePassword)

### Connection to the mail box

**setup WildFly to connect to the mail box:**

[Open the administration of WildFly](http://localhost:9990 "WildFly administration interface").<br/>
<br/>**1.** Go to "Configuration", then "Socket Binding" and click on "View".<br/>
Click on "View>", then on "OutBound Remote", then on "Add" and set:

	Name: (what ever name you want, for ex: mail-smtp-gmail)
	Host: smtp.gmail.com
	Port: 465 (587 should work too)
*Save*<br/>
<br/>**2.** Go to "Configuration", then "Subsystems", then"Mail", click on "Add" and set:

	Name: java:jboss/mail/gmail
	JNDI Name: java:jboss/mail/gmail (name of the resource used in 'EmailObserver.java')
*Save*<br/>
<br/>Click on your Mail Session, then click on "View", then on "Add" and set:

	Socket Binding: (Socket Binding name, in our ex: mail-smtp-gmail)
	Type: smtp
	Username: (youremail@gmail.com)
	Password: (yourEmailPassword)
	Use SSL (tick)
*Save*<br/>
<br/>**3.** Restart WildFly (in the shell: "Ctrl+C", then [start the server](#starting-the-server "go to the 'starting the server' section")).

**setup your mail box to accept external applications:**

**1.** Authorize less secure applications to access to your email service (for Gmail, go [there](https://myaccount.google.com/lesssecureapps "Gmail webpage letting to activate/deactivate less secure application authorization")).

**2.** Check that the outgoing mails analysis is disable in your anti-virus.

## Running

### Starting the database

For Linux                                          | For Windows
-------------------------------------------------- | ---
Start MySQL: <code>sudo service mysql start</code> | Run easyPHP

### Deployment

[Starting the server](#starting-the-server "go to the 'starting the server' section")
In the [CLI](https://en.wikipedia.org/wiki/Command-line_interface "Command Line Interface definition on Wikipedia"), go to the root folder of the java project, containing the pom.xml.<br/>
Type the command: `mvn clean wildfly:deploy`

## Testing

### Manual test

After [running](#running "go to the 'running' chapter") the project, go to http://localhost:8080.

## Built With

### Structure

Build automation tool: [Maven](https://maven.apache.org "Apache Maven website")

### Front-end

Layer | Style | Control    | Graphical User Interface
----- | ----- | ---------- | ---
html5 | css3  | javascript | [Primefaces](http://www.primefaces.org "Primefaces website")

### Back-end

Template | View
-------- | ---
Facelets | [JSF (Java Server Faces)](http://www.oracle.com/technetwork/java/javaee/javaserverfaces-139869.html "JSF documentation on Oracle website")

### Persistence

Application programming interface: [JPA (Java Persistence API)](http://www.oracle.com/technetwork/java/javaee/tech/persistence-jsp-140049.html "JPA documentation on Oracle website")

## Supported browsers

This project have been manually tested with the following browsers:
* Google Chrome 65, Chromium 51
* Mozilla Firefox 59
* Internet Explorer 11
* Opera 51
* Safari 5.1.7

## Authors

* [Jeremy Pansier](https://github.com/JeremyPansier "Jérémy's profile on GitHub") - *Initial work*

See also the list of [contributors](https://github.com/JeremyPansier/JavaEE_website/contributors "Contributors of the project") who participated in this project.

## Acknowledgments

* Special thanks to my brother [Gwenall Pansier](https://github.com/Gwenall "Gwenall's profile on GitHub") who helped me from my early days as developer.
* Thanks to Thomas Gros, the trainer who teached me basics in JavaEE.
* Thanks to Pascal Cunin, my schoolmate, with whom I started this project.

## License
![copyleft](http://unlicense.org/pd-icon.png)
[http://unlicense.org/](http://unlicense.org/)
