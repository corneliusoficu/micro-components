import os
import subprocess
import sys
import re


class AngularGenerator:
    REGEX_ANGULAR_CLI_VERSION = re.compile(r"Angular CLI: *((\d+)\.?(\d+)?\.?(\d+)?)")

    def __init__(self, name):
        self._name = name

    def generate(self, mc_directory):
        print("Generating UI using Angular framework")
        if not self._check_angular_cli_present():
            print("Could not identify angular CLI, exiting...")
            sys.exit(-1)
        angular_project_name = f"{self._name}-frontend"
        self._generate_new_angular_project(mc_directory, angular_project_name)

    def _check_angular_cli_present(self):
        print("Checking if Angular CLI is present")

        process = subprocess.Popen(['ng', 'v'], stdout=subprocess.PIPE, stderr=subprocess.PIPE)
        stdout, _ = process.communicate()
        match = AngularGenerator.REGEX_ANGULAR_CLI_VERSION.search(str(stdout))

        if not match:
            return False

        print(f"Identified Angular CLI with version: {match.group(1)}")
        return True

    def _generate_new_angular_project(self, mc_directory, angular_project_name):
        print(f"Generating a new Angular Project with the name: {angular_project_name}")
        os.chdir(mc_directory)
        subprocess.run(['ng', 'new', angular_project_name])


if __name__ == '__main__':
    AngularGenerator("test").generate("./")


