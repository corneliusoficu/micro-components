import os
import subprocess
import sys

from constants import KARAF_BIN_LOCATION
from constants import KARAF_USER_PRIVATE_KEY_LOCATION


class KarafInstance:
    def __init__(self) -> None:
        if not os.path.exists(KARAF_BIN_LOCATION):
            print("Could not find Karaf instance!, Exiting!")
            sys.exit(-1)

    def list_bundles(self):
        os.system(f"{KARAF_BIN_LOCATION}/client -k {KARAF_USER_PRIVATE_KEY_LOCATION} list")

    def tail_logs(self):
        os.system(f"{KARAF_BIN_LOCATION}/client -k {KARAF_USER_PRIVATE_KEY_LOCATION} log:tail")

    def start(self):
        print("Starting Apache Karaf...")
        os.system(f"{KARAF_BIN_LOCATION}/start")

    def stop(self):
        print("Stopping Apache Karaf...")
        os.system(f"{KARAF_BIN_LOCATION}/stop")

    def install_bundle_jar(self, jar_location):
        os.system(f"{KARAF_BIN_LOCATION}/client -k {KARAF_USER_PRIVATE_KEY_LOCATION} install file:{jar_location}")

    def start_bundle_by_name(self, name):
        print(f"Starting bundle with name {name}")
        if self.bundle_already_runs(name):
            print(f"The bundle {name} is already installed!")
            return False
        os.system(f"{KARAF_BIN_LOCATION}/client -k {KARAF_USER_PRIVATE_KEY_LOCATION} start {name}")
        return True

    def stop_bundle_by_name(self, name):
        print(f"Stopping bundle with name {name}")
        subprocess.run([f"{KARAF_BIN_LOCATION}/client", '-k',
                        KARAF_USER_PRIVATE_KEY_LOCATION, 'stop', name])

    def uninstall_bundle_by_name(self, name):
        print(f"Uninstalling bundle with name {name}")
        subprocess.run([f"{KARAF_BIN_LOCATION}/client", '-k',
                        KARAF_USER_PRIVATE_KEY_LOCATION, 'uninstall', name])

    def bundle_already_runs(self, name):
        result = subprocess.run([f"{KARAF_BIN_LOCATION}/client", '-k',
                                 KARAF_USER_PRIVATE_KEY_LOCATION, 'list --no-format', name],
                                stdout=subprocess.PIPE)
        result = result.stdout.decode('UTF-8')
        result_lines = result.splitlines()

        running_bundle_info = result_lines[-1].split('\t')
        running_bundle_status = running_bundle_info[1]
        running_bundle_name = running_bundle_info[4]

        if running_bundle_name != name:
            return False

        return running_bundle_status in ['Active', 'Waiting']
