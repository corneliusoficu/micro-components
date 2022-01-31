import os
import subprocess
import sys
import xml.etree.ElementTree as ET

from deployers.deployer import Deployer
from karaf_instance import KarafInstance


class JarDeployer(Deployer):
    def deploy(self, location, force_update=False):
        target_dir = location + "/target"
        pom_xml_location = location + "/pom.xml"

        print("Building Micro-Component JAR")

        os.chdir(location)
        process = subprocess.Popen("mvn clean package", shell=True)
        process.wait()

        if process.returncode != 0:
            print("Failed to build maven package. Exiting")
            sys.exit()

        tree = ET.parse(pom_xml_location)
        root = tree.getroot()
        xml_namespace, _ = self._tag_uri_and_name(root)

        group_id = root.find(f"{{{xml_namespace}}}groupId").text
        artifactId = root.find(f"{{{xml_namespace}}}artifactId").text
        version = root.find(f"{{{xml_namespace}}}version").text

        jar_name = f"{artifactId}-{version}.jar"
        jar_location_for_deploy = target_dir + f"/{jar_name}"

        bundle_to_start = f"{group_id}.{artifactId}"

        if not force_update:
            self._start_bundle(bundle_to_start, jar_location_for_deploy)
        else:
            self._force_start_bundle(bundle_to_start, jar_location_for_deploy)

    def _tag_uri_and_name(self, elem):
        if elem.tag[0] == "{":
            uri, ignore, tag = elem.tag[1:].partition("}")
        else:
            uri = None
            tag = elem.tag
        return uri, tag

    def _start_bundle(self, bundle_to_start, jar_location_for_deploy):
        KarafInstance().install_bundle_jar(jar_location_for_deploy)
        print("Installed Jar!")

        started = KarafInstance().start_bundle_by_name(bundle_to_start)

        if started:
            print(f"Started Bundle {bundle_to_start}")
        else:
            print(f"Failed to start the Bundle {bundle_to_start}")

    def _force_start_bundle(self, bundle_to_start, jar_location_for_deploy):
        karaf = KarafInstance()

        if karaf.bundle_already_runs(bundle_to_start):
            print(f"Bundle {bundle_to_start} is already running. Restarting...")
            karaf.uninstall_bundle_by_name(bundle_to_start)
        else:
            print(f"Bundle {bundle_to_start} is not running...")

        self._start_bundle(bundle_to_start, jar_location_for_deploy)