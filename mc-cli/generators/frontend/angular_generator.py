import os
import subprocess
import sys

import constants
from constants import MC_CLI_HOME_PATH
from generators.helpers import file_helpers
from generators.models.generator import Generator


class AngularGenerator(Generator):
    def __init__(self, name):
        self._name = name

    def generate(self, mc_directory):
        print("Generating UI using Angular framework")
        if not self._check_angular_cli_present():
            print("Could not identify angular CLI, exiting...")
            sys.exit(-1)
        angular_project_name = f"{self._name}-frontend"
        angular_project_location = os.path.join(mc_directory, angular_project_name)
        self._generate_new_angular_project(mc_directory, angular_project_name)
        self._install_angular_elements_npm_package(angular_project_location)
        self._create_main_component(angular_project_location, angular_project_name)

    def _check_angular_cli_present(self):
        print("Checking if Angular CLI is present")

        process = subprocess.Popen(['ng', 'v'], stdout=subprocess.PIPE, stderr=subprocess.PIPE)
        stdout, _ = process.communicate()
        match = constants.ANGULAR_REGEX_CLI_VERSION.search(str(stdout))

        if not match:
            return False

        print(f"Identified Angular CLI with version: {match.group(1)}")
        return True

    def _generate_new_angular_project(self, mc_directory, angular_project_name):
        print(f"Generating a new Angular Project with the name: {angular_project_name}")
        os.chdir(mc_directory)
        subprocess.run(['ng', 'new', '--style', constants.ANGULAR_DEFAULT_STYLE, '--routing',
                        constants.ANGULAR_DEFAULT_ROUTING, angular_project_name])

    def _install_angular_elements_npm_package(self, project_location):
        os.chdir(project_location)
        print("Installing Angular Elements extension...")
        subprocess.run(['ng', 'add', '@angular/elements'])

    def _create_main_component(self, project_location, project_name):
        print("Generating the main component")
        main_component_location = f"{project_location}/src/app/main"
        print(f"Creating main component folder: {main_component_location}")
        os.mkdir(main_component_location)

        template_main_component_html = f'{MC_CLI_HOME_PATH}/templates/angular_custom_elements/main.component.html.jinja'
        location_main_component_html = f"{main_component_location}/main.component.html"
        file_helpers.create_template_file(template_main_component_html, location_main_component_html, project_name=project_name)


if __name__ == '__main__':
    AngularGenerator("test").generate("./")


