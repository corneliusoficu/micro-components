import os
import sys

from constants import MC_BACKEND_SUFFIX
from constants import MC_CLI_HOME_PATH
from helpers import file_helpers
from generators.generator import Generator


class JavaJaxRSGenerator(Generator):

    def __init__(self, name, description, group_id):
        self._name = self._artifact_id = f"{name.lower()}{MC_BACKEND_SUFFIX}"
        self._description = description
        self._group_id = group_id

        self._bundle_name = f"{self._group_id}.{self._artifact_id}"
        self._description = self._description
        self._main_package_name = self._artifact_id.replace("-", "")

    def generate(self, mc_path):
        backend_path = self._generate_folder_for_backend(mc_path)
        self._generate_pom_xml_file(backend_path)
        self._generate_file_structure_for_java_project(backend_path)

    def _generate_folder_for_backend(self, mc_path):
        backend_folder_path = f"{mc_path}/{self._name}"

        if os.path.exists(backend_folder_path):
            print(f"Cannot generate backend file structure because of already existing directory {backend_folder_path}")
            sys.exit(-1)

        os.mkdir(backend_folder_path)
        print(f"Created directory {self._name}")
        return backend_folder_path

    def _generate_file_structure_for_java_project(self, backend_path):
        group_id_dirs = self._group_id.split('.')
        file_structure_java_project = 'src/main/java/' + '/'.join(group_id_dirs) + '/' + self._main_package_name
        file_structure_resources = 'src/main/resources'
        file_structure_test = 'src/test/java'

        entire_project_structure = backend_path + '/' + file_structure_java_project
        entire_services_structure = entire_project_structure + '/services'
        entire_resources_structure = backend_path + '/' + file_structure_resources
        entire_test_structure = backend_path + '/' + file_structure_test

        # Generating File Structure for the created micro-component
        os.makedirs(entire_project_structure)
        os.mkdir(entire_services_structure)
        os.mkdir(entire_resources_structure)
        os.makedirs(entire_test_structure)

        endpoint_name = "-".join(c.lower() for c in self._name.split("-"))

        print("Generated entire file structure for micro-component")

        self._generate_main_class(entire_services_structure, endpoint_name)
        self._generate_activator_class(entire_project_structure, endpoint_name)
        self._generate_view_js_file(entire_resources_structure, endpoint_name)

    def _generate_pom_xml_file(self, backend_path):
        activator_location = f"{self._group_id}.{self._main_package_name}.Activator"
        export_package = f"{self._group_id}.{self._main_package_name}.*"

        pom_xml_template = f'{MC_CLI_HOME_PATH}/templates/java_jax_rs/pom.xml.jinja'
        pom_xml_location = f"{backend_path}/pom.xml"
        file_helpers.create_template_file(pom_xml_template, pom_xml_location,
                                          group_id=self._group_id,
                                          artifact_id=self._artifact_id,
                                          bundle_name=self._bundle_name,
                                          description=self._description,
                                          activator_location=activator_location,
                                          export_package=export_package)

    def _generate_main_class(self, path_to_services_package, endpoint_name):
        class_name_components = [c.capitalize() for c in self._name.split("-")]
        class_name = "".join(class_name_components) + "Service"

        main_class_template = f'{MC_CLI_HOME_PATH}/templates/java_jax_rs/Service.java.jinja'
        main_class_location = f"{path_to_services_package}/{class_name}.java"

        file_helpers.create_template_file(main_class_template, main_class_location,
                                          group_id=self._group_id,
                                          main_package_name=self._main_package_name,
                                          class_name=class_name,
                                          endpoint_name=endpoint_name)

    def _generate_activator_class(self, project_structure, endpoint_name):
        # Generating the Activator Java Class
        activator_class_template = f'{MC_CLI_HOME_PATH}/templates/java_jax_rs/Activator.java.jinja'
        activator_class_location = f'{project_structure}/Activator.java'

        file_helpers.create_template_file(activator_class_template, activator_class_location,
                                          group_id=self._group_id,
                                          main_package_name=self._main_package_name,
                                          endpoint_name=endpoint_name)

    def _generate_view_js_file(self, path_to_resources_folder, endpoint_name):
        # Generating the default view.js script file

        view_js_template = f'{MC_CLI_HOME_PATH}/templates/java_jax_rs/view.js.jinja'
        view_js_location = f'{path_to_resources_folder}/view.js'

        file_helpers.create_template_file(view_js_template, view_js_location,
                                          endpoint_name=endpoint_name)
