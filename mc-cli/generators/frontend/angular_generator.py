import json
import os
import subprocess
import sys

import constants
from constants import MC_CLI_HOME_PATH
from deployers.frontend.angular_deployer import AngularDeployer
from helpers import file_helpers
from generators.generator import ViewProvidingGenerator


class AngularGenerator(ViewProvidingGenerator):
    def __init__(self, name):
        self._name = name
        self._deployer = AngularDeployer()
        self.export_file = f"{self._name}.js"

    def generate(self, mc_directory):
        print("Generating UI using Angular framework")
        if not self._check_angular_cli_present():
            print("Could not identify angular CLI, exiting...")
            sys.exit(-1)

        angular_project_location_name = f"{self._name}{constants.MC_FRONTEND_SUFFIX}"
        self._angular_project_location = os.path.join(mc_directory, angular_project_location_name)

        self._generate_new_angular_project(mc_directory, angular_project_location_name)
        self._install_angular_elements_npm_package(self._angular_project_location)
        self._install_custom_webpack_dependency(self._angular_project_location)
        self._update_es_version(self._angular_project_location, constants.ANGULAR_ES_VERSION)
        self._create_extra_webpack_config(self._angular_project_location, self._name)

        if constants.ANGULAR_ES_VERSION == "es5":
            self._create_custom_elements_es5_adapter(self._angular_project_location)

        self._update_angular_json_file(self._angular_project_location, self._name)
        self._create_main_component(self._angular_project_location, self._name)
        self._create_app_module(self._angular_project_location, self._name)
        self._create_index_html(self._angular_project_location, self._name)
        self._update_build_config(self._angular_project_location, angular_project_location_name)
        self._deployer.deploy(self._angular_project_location)

    def get_view_location(self):
        if self._angular_project_location is None:
            print("The view was not generated yet!")
            sys.exit(-1)

        return f"{self._angular_project_location}/{self.export_file}"

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

    def _install_custom_webpack_dependency(self, project_location):
        os.chdir(project_location)
        print("Installing Custom Webpack package...")
        subprocess.run(['npm', 'i', '@angular-builders/custom-webpack@9.0.0'])

    def _update_es_version(self, project_location, es_version="es5"):
        tsconfig_file_location = project_location + "/tsconfig.json"
        with open(tsconfig_file_location) as ts_config_file:
            ts_config = json.load(ts_config_file)
            ts_config["compilerOptions"]["target"] = es_version
        with open(tsconfig_file_location, "w") as ts_config_file:
            json.dump(ts_config, ts_config_file)

    def _update_angular_json_file(self, project_location, project_name):
        print("Updating the angular.json file with custom webpack information")

        builder = "@angular-builders/custom-webpack:browser"
        customWebpackConfig = {
            "path": "extra-webpack.config.js",
            "mergeStrategies": {"externals": "replace"}
        }

        os.chdir(project_location)
        print("Updating the angular.json file to include the extra-webpack build config")
        angular_json_file = f"{project_location}/angular.json"
        with open(angular_json_file, "r") as angular_json_fp:
            angular_json = json.load(angular_json_fp)
            angular_json["projects"][f"{project_name}{constants.MC_FRONTEND_SUFFIX}"]["architect"]["build"]["builder"] = builder
            angular_json["projects"][f"{project_name}{constants.MC_FRONTEND_SUFFIX}"]["architect"]["build"]["options"]["customWebpackConfig"] = customWebpackConfig
        with open(angular_json_file, "w") as angular_json_fp:
            json.dump(angular_json, angular_json_fp)

    def _create_extra_webpack_config(self, project_location, project_name):
        print("Generating the extra webpack configuration file")
        extra_webpack_config_template = f"{MC_CLI_HOME_PATH}/templates/angular_custom_elements/extra-webpack.config.js.jinja"
        extra_webpack_config_location = f"{project_location}/extra-webpack.config.js"

        class_name_components = [c.capitalize() for c in project_name.split("-")]
        class_name = "".join(class_name_components)
        library_name = class_name.lower()

        file_helpers.create_template_file(
            extra_webpack_config_template,
            extra_webpack_config_location,
            class_name=class_name,
            library_name=library_name)

    def _create_custom_elements_es5_adapter(self, project_location):
        custom_elements_es5_adapter_template = f"{MC_CLI_HOME_PATH}/templates/angular_custom_elements/custom-elements-es5-adapter.js.jinja"
        custom_elements_es5_adapter_location = f"{project_location}/custom-elements-es5-adapter.js"

        file_helpers.create_template_file(
            custom_elements_es5_adapter_template,
            custom_elements_es5_adapter_location)

    def _create_main_component(self, project_location, project_name):
        print("Generating the main component")
        main_component_location = f"{project_location}/src/app/main"
        print(f"Creating main component folder: {main_component_location}")
        os.mkdir(main_component_location)

        main_component_files = {
                    "main.component.css": {},
                    "main.component.html": {"project_name": project_name},
                    "main.component.spec.ts": {},
                    "main.component.ts": {}
        }

        for file, template_args in main_component_files.items():
            template_main_component_html = f'{MC_CLI_HOME_PATH}/templates/angular_custom_elements/{file}.jinja'
            location_main_component_html = f"{main_component_location}/{file}"

            file_helpers.create_template_file(
                template_main_component_html,
                location_main_component_html,
                **template_args)

    def _create_app_module(self, project_location, project_name):
        print("Generating the app module")
        app_module_template = f"{MC_CLI_HOME_PATH}/templates/angular_custom_elements/app.module.ts.jinja"
        app_module_location = f"{project_location}/src/app/app.module.ts"

        file_helpers.create_template_file(
            app_module_template,
            app_module_location,
            project_name=project_name)

    def _create_index_html(self, project_location, project_name):
        print("Generating the index.html")
        index_html_template = f"{MC_CLI_HOME_PATH}/templates/angular_custom_elements/index.html.jinja"
        index_html_location = f"{project_location}/src/index.html"
        file_helpers.create_template_file(
            index_html_template,
            index_html_location,
            project_name=project_name)

    def _update_build_config(self, project_location, project_location_name):
        print("Updating the package.json file")

        if constants.ANGULAR_ES_VERSION == "es5":
            es5_adapter_js = f"{project_location}/custom-elements-es5-adapter.js"
            runtime_js = f"{project_location}/dist/{project_location_name}/runtime.js"
            polyfills_js = f"{project_location}/dist/{project_location_name}/polyfills.js"
            main_js = f"{project_location}/dist/{project_location_name}/main.js"
        else:
            es5_adapter_js = ""
            runtime_js = f"{project_location}/dist/{project_location_name}/runtime-es2015.js"
            polyfills_js = f"{project_location}/dist/{project_location_name}/polyfills-es2015.js"
            main_js = f"{project_location}/dist/{project_location_name}/main-es2015.js"

        package_json_file = f"{project_location}/package.json"
        package_json = file_helpers.read_json_file(package_json_file)
        package_json['scripts']['build'] = "ng build --prod --output-hashing=none"
        package_json['scripts']['package'] = f"cat {es5_adapter_js} {runtime_js} {polyfills_js} {main_js} > {self.export_file}"

        file_helpers.write_json_file(package_json_file, package_json)


if __name__ == '__main__':
    AngularGenerator("test").generate("./")


