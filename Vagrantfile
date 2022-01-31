Vagrant.configure("2") do |config|
  config.vm.box = "ubuntu/bionic64"
  config.vm.provider "virtualbox" do |v|
    v.memory = 2048
  end
  config.vm.synced_folder ".", "/vagrant", 
    type: "nfs",
    mount_options: ["rw", "vers=3", "tcp"],
    linux__nfs_options: ["rw","no_subtree_check","all_squash","async"]

  config.vm.network "private_network", type: "dhcp", ip: "192.168.56.4"
  config.vm.network "forwarded_port", guest: 8181, host: 8181 # Karaf
  config.vm.network "forwarded_port", guest: 4200, host: 4200 # Host Application Dev server
  config.vm.network "forwarded_port", guest: 4201, host: 4201 # Dev server for testing frontend apps
  config.vm.network "forwarded_port", guest: 27017, host: 27017 # MongoDB
  config.vm.provision "file", source: "conf/org.apache.karaf.features.cfg", destination: "/tmp/org.apache.karaf.features.cfg" # Startup Config for Karaf
  config.vm.provision "file", source: "conf/karaf.config.properties", destination: "/tmp/config.properties" # Startup Config for Karaf
  config.vm.provision "file", source: "conf/mongod.conf", destination: "/tmp/mongod.conf" # Startup Config for Karaf

  config.vm.provision :shell, path: "provision/provision.sh"
end
