# Description
This project showcases the architecture of a system for dynamic loading of Micro-Components for Java Based Modular Web Application. It represents the technical part of the Dissertation Thesis created as part of the Master in Computer Science - Internet & Web Technologies Track at the Vrije Universiteit Amsterdam, The Netherlands.

A general overview of the entire project can be visualized in the Use-Case diagram below:

![general architecture](/img/diagram-build-micro-component.png "General Use Case Diagram of creating and using Micro-Components")

As can be seen in the diagram above, the following elements are the main parts of the entire system:
- **The CLI Application**: Built using Python3, it provides commands that allow you to interact and execute the most important commands for developing, building, deploying and running Micro-Components.
- **The Apache Karaf Runtime**: It provides an OSGi runtime that is used to dynamically install and run Micro-Components. 
- **The Frontend Host Application**: Represents an UI application built using Angular that allows end-users to load Micro-Components and interact with them. The main purpose of this application is to handle the Micro-Components frontends UIs.
- **Micro-Component Development and Build Workfllow**: This workflow is managed using the CLI application and controls the way in which Micro-Components are created built and deployed. More concretely, it is composed of an automatically created Java + OSGi + Maven project for the backend of the Micro-Component and an automatically created UI project (Angular) for the frontend of the Micro-Component. These projects are built using the CLI Application and then bundled together into a JAR file, that represents the actual Micro-Component.





# Local Setup
We strongly recommend to use [Vagrant](https://www.vagrantup.com/) for bringing up the entire environment for developing Micro-Components. Doing this, all the required dependencies will be made available to you into an Ubuntu VM.

You can easily setup the local environment for developing micro-components using Vagrant:
Just type the command from the `micro-components` folder:
```shell
vagrant up
```
Doing this, you will create an Ubuntu VM that will be provisioned using the script: `provision/provision.sh`

This will install and configure: 
- Python 3
- pip
- venv
- node
- npm
- angular cli
- apache-karaf

This Vagrant configuration was created to run on **MacOS** based systems, so if you run this on other systems, be mindful that minor issues might arise.

# Using the CLI 
If you used Vagrant, once you have the VM provisiined you can start working on Micro-Components, by ssh-ing into the VM. Again, from the `micro-components` folder, you can issue the following:

```shell
vagrant ssh
```

Once you are in, you should have the Python3 CLI application available in the path. You can test this by issuing the following:
```shell
mc-cli --help
```

This should display you all the available commands.

## Starting Apache Karaf

To start Apache Karaf, you can issue the  following command using the mc-cli:
```shell
mc-cli karaf start
```

To verify that Apache Karaf is running:
```shell
mc-cli karaf list
```
This will display you a list of running OSGi Bundles. At this point there are no Micro-Components running so the list you see is for all the OSGi bundles that are required for the Micro-Components to be run. You can always use this command to verify if any of your Micro-Components are installed and running.

## Installing the Core Micro-Components
For the development and running of Micro-Components, we developed some Core Micro-Components that provide a series of functionalities that can help you in this process. These are: 
- **base-micro-components**: Provides common code that simplies the development of Micro-Components
- **lifecycle-handler**: Provides lifecycle functions for Micro-Components. Also contains code for Micro-Component discovery.
- **auth-handler**: Provides authentication features for the frontend host application.

The commands for installing these core Micro-Components are the following:
```shell
# Install base-micro-components 
cd /vagrant/core-services/base-micro-components
mc-cli deploy jar

# Install lifecycle-handler Micro-Component
cd /vagrant/core-services/lifecycle-handler
mc-cli deploy jar

# Install auth-handler Micro-Component
cd /vagrant/core-services/auth-handler
mc-cli deploy jar
```




