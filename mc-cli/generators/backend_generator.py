import os
import sys

from jinja2 import Template
from constants import MC_CLI_HOME_PATH
from constants import MC_BACKEND_SUFFIX


class BackendGenerator:
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

    def _generate_pom_xml_file(self, backend_path):

        with open(f'{MC_CLI_HOME_PATH}/templates/pom.xml.jinja') as file_:
            pom_xml_template = Template(file_.read())

            activator_location = f"{self._group_id}.{self._main_package_name}.Activator"
            export_package = f"{self._group_id}.{self._main_package_name}.*"

            generated_pom_xml = pom_xml_template.render(
                group_id=self._group_id,
                artifact_id=self._artifact_id,
                bundle_name=self._bundle_name,
                description=self._description,
                activator_location=activator_location,
                export_package=export_package
            )

        with open(f"{backend_path}/pom.xml", "w") as f:
            f.write(generated_pom_xml)

        print("Created pom.xml file in the component directory")

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

    def _generate_main_class(self, path_to_services_package, endpoint_name):
        class_name_components = [c.capitalize() for c in self._name.split("-")]
        class_name = "".join(class_name_components) + "Service"

        # Generating the main Service Java Class
        with open(f'{MC_CLI_HOME_PATH}/templates/Service.java.jinja') as file_:
            service_java = Template(file_.read())
            generated_service_java = service_java.render(
                group_id=self._group_id,
                main_package_name=self._main_package_name,
                class_name=class_name,
                endpoint_name=endpoint_name
            )

        with open(f"{path_to_services_package}/{class_name}.java", "w") as f:
            f.write(generated_service_java)

        print(f"Generated {class_name}.java file")

    def _generate_activator_class(self, project_structure, endpoint_name):
        # Generating the Activator Java Class
        with open(f'{MC_CLI_HOME_PATH}/templates/Activator.java.jinja') as file_:
            activator_java = Template(file_.read())
            generated_activator_java = activator_java.render(
                group_id=self._group_id,
                main_package_name=self._main_package_name,
                endpoint_name=endpoint_name
            )

        with open(f"{project_structure}/Activator.java", "w") as f:
            f.write(generated_activator_java)

        print("Generated Activator Java Class")

    def _generate_view_js_file(self, path_to_resources_folder, endpoint_name):
        # Generating the default view.js script file
        with open(f'{MC_CLI_HOME_PATH}/templates/view.js.jinja') as file_:
            view_js = Template(file_.read())
            generated_view_js = view_js.render(
                endpoint_name=endpoint_name
            )

        with open(f"{path_to_resources_folder}/view.js", "w") as f:
            f.write(generated_view_js)

        print("Generated view.js script")

    def _generate_folder_for_backend(self, mc_path):
        backend_folder_path = f"{mc_path}/{self._name}"

        if os.path.exists(backend_folder_path):
            print(f"Cannot generate backend file structure because of already existing directory {backend_folder_path}")
            sys.exit(-1)

        os.mkdir(backend_folder_path)
        print(f"Created directory {self._name}")
        return backend_folder_path



