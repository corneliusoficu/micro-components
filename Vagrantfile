Vagrant.configure("2") do |config|
  config.vm.box = "ubuntu/bionic64"
  config.vm.network "forwarded_port", guest: 8181, host: 8181 # Karaf
  config.vm.network "forwarded_port", guest: 4200, host: 4200 # Host Application Dev server
  config.vm.provision "file", source: "karaf/org.apache.karaf.features.cfg", destination: "/tmp/org.apache.karaf.features.cfg"
  config.vm.provision :shell, path: "provision/provision.sh"
end
