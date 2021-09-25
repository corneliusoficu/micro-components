import os
import subprocess
import sys
import xml.etree.ElementTree as ET

from karaf_instance import KarafInstance

class Deployer:

    CURRENT_WORKING_DIR = os.getcwd()

    def deploy_jar(self):
        target_dir = Deployer.CURRENT_WORKING_DIR + "/target"
        pom_xml_location = Deployer.CURRENT_WORKING_DIR + "/pom.xml"

        print("Building Micro-Component JAR")

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

        KarafInstance().install_bundle_jar(jar_location_for_deploy)
        print("Installed Jar!")

        bundle_to_start = f"{group_id}.{artifactId}"
        started = KarafInstance().start_bundle_by_name(bundle_to_start)

        if started:
            print(f"Started Bundle {bundle_to_start}")
        else:
            print(f"Failed to start the Bundle {bundle_to_start}")

    def _tag_uri_and_name(self, elem):
        if elem.tag[0] == "{":
            uri, ignore, tag = elem.tag[1:].partition("}")
        else:
            uri = None
            tag = elem.tag
        return uri, tag


