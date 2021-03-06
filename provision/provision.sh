# VERSIONS

ANGULAR_CLI_VERSION=9.0.3
APACHE_KARAF_VERSION=4.3.3
MONGO_DB_VERSION=4.4

# UPDATE PACKAGE LISTS FOR NEW VM

echo "Executing apt-get update"
sudo apt-get update

echo "Executing provisioning script for micro-components"

install_pkg_if_not_present () {
    PKG_OK=$(dpkg-query -W --showformat='${Status}\n' $1|grep "install ok installed")
    echo Checking for $1: $PKG_OK
    if [ "" = "$PKG_OK" ]; then
    echo "No $1. Setting up $1."
    sudo apt-get --yes install $1
    fi
}

# PYTHON3

echo "Ensuring python3 exists"
install_pkg_if_not_present python3

# PYTHON3 PIP

echo "Ensuring pip3 exists"
install_pkg_if_not_present python3-pip

# PYTHON3 VENV

echo "Ensuring venv exists"
install_pkg_if_not_present python3-venv

# JAVA 11

echo "Ensuring openjdk-11-jdk exists"
install_pkg_if_not_present openjdk-11-jdk

# MAVEN

echo "Ensuring maven exists"
install_pkg_if_not_present maven

# NPM

if which node > /dev/null
    then
        echo "node is installed, skipping..."
        echo "node version: : $(node --version)"
        echo "npm version: $(npm --version)"
    else
        echo "Installing npm"
        curl -sL https://deb.nodesource.com/setup_12.x | sudo -E bash -
        sudo apt-get --yes install nodejs
        echo "node version: : $(node --version)"
        echo "npm version: $(npm --version)"
fi

# ANGULAR CLI

if which ng > /dev/null
    then
        echo "Angular is installed, skipping..."
        echo "Angular version: : $(ng --version)"
    else
        echo "Installing Angular CLI Version: $ANGULAR_CLI_VERSION"
        npm install -g @angular/cli@$ANGULAR_CLI_VERSION
        echo "Angular version: : $(ng --version)"
fi

# MC-CLI DEPENDENCIES

echo "Installing dependencies for mc-cli"
python3 -m  pip install -r /vagrant/mc-cli/requirements.txt
if [ ! -f "/bin/mc-cli" ]; then
    ln -s /vagrant/mc-cli/mc-cli /bin/mc-cli 
fi

# APACHE KARAF

if [ ! -d "/opt/apache-karaf-${APACHE_KARAF_VERSION}" ]; then
    echo "Installing Apache Karaf"
    APACHE_KARAF_DOWNLOAD_LINK="https://archive.apache.org/dist/karaf/${APACHE_KARAF_VERSION}/apache-karaf-${APACHE_KARAF_VERSION}.tar.gz"
    cd /opt 
    echo "Downloading Aapache KARAF tar.gz from ${APACHE_KARAF_DOWNLOAD_LINK}"
    wget -q "${APACHE_KARAF_DOWNLOAD_LINK}"
    echo "Extracting Apache Karaf archive"
    tar -xzf apache-karaf-"${APACHE_KARAF_VERSION}".tar.gz 
    rm -f apache-karaf-"${APACHE_KARAF_VERSION}".tar.gz
    chown vagrant:vagrant -R /opt/apache-karaf-"${APACHE_KARAF_VERSION}"/
    
    # GENERATE KARAF USER PUBLIC PRIVATE KEY PAIR FOR THE MC-CLI
    
    echo "Generating public private key pair for karaf user to be used by MC-CLI"
    cd /home/vagrant/.ssh
    ssh-keygen -t dsa -f karaf.id_dsa -N karaf
    PUBLIC_KEY_CONTENTS="$(cat /home/vagrant/.ssh/karaf.id_dsa.pub)"
    REGEX_PUB_KEY="ssh-dss\s+(.*)\s+root@ubuntu-bionic"
    [[ $PUBLIC_KEY_CONTENTS =~ $REGEX_PUB_KEY ]]
    PUBLIC_KEY_CONTENTS="${BASH_REMATCH[1]}"
    echo "karaf=${PUBLIC_KEY_CONTENTS},_g_:admingroup" > /opt/apache-karaf-"${APACHE_KARAF_VERSION}"/etc/keys.properties
    echo "_g_\:admingroup = group,admin,manager,viewer,systembundles,ssh" >> /opt/apache-karaf-"${APACHE_KARAF_VERSION}"/etc/keys.properties
    
    echo "karaf = karaf,_g_:admingroup" > /opt/apache-karaf-"${APACHE_KARAF_VERSION}"/etc/users.properties
    echo "_g_\:admingroup = group,admin,manager,viewer,systembundles,ssh" >> /opt/apache-karaf-"${APACHE_KARAF_VERSION}"/etc/users.properties
    
    # SETTING KARAF CONFIGURATION
    cp /tmp/org.apache.karaf.features.cfg /opt/apache-karaf-"${APACHE_KARAF_VERSION}"/etc/
    rm /tmp/org.apache.karaf.features.cfg

    cp /tmp/config.properties /opt/apache-karaf-"${APACHE_KARAF_VERSION}"/etc/
    rm /tmp/config.properties
fi

# INSTALLING MONGODB

echo "Installing MongoDB"
systemctl is-active --quiet mongod
retVal=$?

if [ $retVal -ne 0 ]; then
    echo "MongoDB is not running..."
    sudo mkdir /vagrant/.db-data
    sudo chown -R mongodb:mongodb /vagrant/.db-data
    cp /tmp/mongod.conf /etc/mongod.conf
    rm /tmp/mongod.cong
    cd /vagrant
    curl -fsSL https://www.mongodb.org/static/pgp/server-"${MONGO_DB_VERSION}".asc | sudo apt-key add -
    echo "deb [ arch=amd64,arm64 ] https://repo.mongodb.org/apt/ubuntu bionic/mongodb-org/${MONGO_DB_VERSION} multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-"${MONGO_DB_VERSION}".list
    sudo apt update
    echo "Installing MongoDB"
    sudo apt install --yes mongodb-org
    sudo systemctl start mongod.service
    sudo systemctl enable mongod
fi

# ENVIRONMENT VARIABLES

echo "export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64" >> /home/vagrant/.bashrc
echo "export PATH=$PATH:$JAVA_HOME/bin" >> /home/vagrant/.bashrc
echo "export KARAF_HOME=/opt/apache-karaf-${APACHE_KARAF_VERSION}" >> /home/vagrant/.bashrc
echo "export KARAF_USER_PRIVATE_KEY_LOCATION=/home/vagrant/.ssh/karaf.id_dsa" >> /home/vagrant/.bashrc

# INSTALL BASE MICRO-COMPONENTS

echo "Installing base-micro-components..."
cd /vagrant/core-services/base-micro-components
mvn clean install

# TO START SSH SESSIONS IN /vagrant FOLDER

echo "cd /vagrant" >> /home/vagrant/.bashrc

