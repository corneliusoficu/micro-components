import os
import pathlib
import subprocess
import sys




class KarafInstance:
    KARAF_BIN_LOCATION = '/Users/corneliudumitru.sofi/Downloads/apache-karaf-4.3.2/bin'
    KARAF_USER_PRIVATE_KEY_LOCATION = f"{pathlib.Path(__file__).parent.resolve()}/karaf.id_rsa"
    def __init__(self) -> None:
        if not os.path.exists(KarafInstance.KARAF_BIN_LOCATION):
            print("Could not find Karaf instance!, Exiting!")
            sys.exit(-1)

    def list_bundles(self):
        os.system(f"{KarafInstance.KARAF_BIN_LOCATION}/client -k {KarafInstance.KARAF_USER_PRIVATE_KEY_LOCATION} list")

    def tail_logs(self):
        os.system(f"{KarafInstance.KARAF_BIN_LOCATION}/client -k {KarafInstance.KARAF_USER_PRIVATE_KEY_LOCATION} log:tail")

    def start(self):
        os.system(f"{KarafInstance.KARAF_BIN_LOCATION}/start")

    def stop(self):
        os.system(f"{KarafInstance.KARAF_BIN_LOCATION}/stop")

    def install_bundle_jar(self, jar_location):
        os.system(f"{KarafInstance.KARAF_BIN_LOCATION}/client -k {KarafInstance.KARAF_USER_PRIVATE_KEY_LOCATION} install file:{jar_location}")

    def start_bundle_by_name(self, name):
        if self._bundle_already_runs(name):
            print(f"The bundle {name} is already installed!")
            return False
        os.system(f"{KarafInstance.KARAF_BIN_LOCATION}/client -k {KarafInstance.KARAF_USER_PRIVATE_KEY_LOCATION} start {name}")
        return True

    def _bundle_already_runs(self, name):
        result = subprocess.run([f"{KarafInstance.KARAF_BIN_LOCATION}/client", '-k',
                                 KarafInstance.KARAF_USER_PRIVATE_KEY_LOCATION, 'list --no-format', name],
                                stdout=subprocess.PIPE)
        result = result.stdout.decode('UTF-8')
        result_lines = result.splitlines()
        if len(result_lines) != 2:
            return False

        running_bundle = result_lines[1].split('\t')[4]
        return running_bundle == name

