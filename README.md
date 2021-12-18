# Local Setup
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
