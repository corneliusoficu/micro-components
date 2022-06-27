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
```

The auth handler requires the mongo-java-driver as a runtime dependency, so we will install that using the Apache Karaf CLI directly (we currently do not support functionality in the CLI application to install OSGi bundles directly), as follows:

```shell
# Connect to Apache Karaf Directly:
/opt/apache-karaf-4.3.3/bin/client -k ~/.ssh/karaf.id_dsa

# Within the Karaf CLI:
bundle:install mvn:org.mongodb/mongo-java-driver/3.12.2
bundle:start <mongo-java-driver bundle id>

# Install auth-handler Micro-Component
cd /vagrant/core-services/auth-handler
mc-cli deploy jar
```

## Starting the Frontend Host Application
The Host Application that is able to load Micro-Components is build as a typical Angular Web Application. The Source code for this is in the `/vagrant/core-services/host-application/host-application-frontend` folder. Currently this host application is still under development so we did not deployed this as a Micro-Component, but this should be an easy task to achieve. 

To start the development server for the Host Application you can simply run:
```shell
# Go to Host Application folder
cd /vagrant/core-services/host-application/host-application-frontend

# Start the development server:
ng serve --proxy-config proxy.conf.json --host 0.0.0.0
```

The proxy file is only for develoment environment and instructs that the Micro-Components should be accessed through the **CXF** layer of Apache Karaf. 

# Using the Host Application

The host application is a basic web application that allows registration/login and loading/unloading Micro-Components for demonstration purposes

To register and account for yourself, you will need to submit a curl request to the auth-handler Micro-Component. Currently there is no webpage built for registration:

```shell
curl --location --request POST 'http://localhost:8181/cxf/auth-handler/register' --header 'Content-Type: application/json' --data-raw '{
    "email": "jane.doe@gmail.com",
    "firstName": "Jane",
    "lastName": "Doe",
    "password": "12345678",
    "repeatPassword": "12345678"
}'
```

This will create a mock user for you to test the web application.

Now you can head to `http://localhost:4200`, login using the credentials you created in the register request and then start playing with the application.

In the **Settings** tab, you control how the layout should look like, you provide how many columns and how many rows the layout of the page should have, (Currently only columns works)

In the **Layout** tab, you can load the actual Micro-Components. Every Micro-Component that you build and deploy using the CLI will be displayed here. If you click on *Install Micro-Component* nothing should show up because we haven't installed yet any Micro-Components, except for the core ones which are not supposed to show up here. 

In the **Home** tab, you should view the UIs of the Micro-Components. Once you deploy and install available Micro-Components you should be able to see them here.

# Installing the Test Micro-Components

We provided some Micro-Components for testing purposes which you can install for testing the system. These are available as a sub-repository in this project, under the [micro-components-demo-app](https://github.com/corneliusoficu/micro-components-demo-app/tree/5f403e25a7c2a6e9f20bc40c159539664e8b43fb) folder.

To install them, simply navigate to each of the Micro-Component folder inside the Vagrant VM and issue the install commands, ex:
```shell
# Navigate to Micro-Component folder
cd /vagrant/micro-components-demo-app/mc-stocks-actions

# Deploy Micro-Component
mc-cli deploy
```

After doing this for the Test Micro-Components you want to install, you can navigate back to the host-application and under **Layout** can click on **Install Micro-Component**, in whatever position you wish to install and then the Micro-Components should be available for you to install.

After installing them, you can go back in the **Home** tab and see that the Micro-Components are running and are also able to interact with each other.





