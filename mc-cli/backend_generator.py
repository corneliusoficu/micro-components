from jinja2 import Template
import pathlib
import os
import sys


class BackendGenerator:
    GROUP_ID = "nl.vu.dynamicplugins"

    def __init__(self, name, description, group):
        self._name = name
        self._description = description
        self._current_work_directory = os.getcwd()
        self._scripts_path = pathlib.Path(__file__).parent.resolve()
        self._location_of_component_directory = f"{self._current_work_directory}/{self._name}"
        self._artifact_id = self._name.lower()
        self._bundle_name = f"{BackendGenerator.GROUP_ID}.{self._artifact_id}"
        self._description = self._description
        self._main_package_name = self._artifact_id.replace("-", "")

    def generate(self):
        self._create_folder_for_component()
        self._generate_pom_xml_file()
        self._generate_file_structure_for_java_project()

    def _create_folder_for_component(self):
        if os.path.exists(self._location_of_component_directory):
            print(f"Directory for component already exists: {self._location_of_component_directory}\nAborting!")
            sys.exit(-1)

        print(f"Creating component directory: {self._location_of_component_directory}")
        os.mkdir(self._location_of_component_directory)

    def _generate_pom_xml_file(self):

        with open(f'{self._scripts_path}/templates/pom.xml.jinja') as file_:
            pom_xml_template = Template(file_.read())

            activator_location = f"{BackendGenerator.GROUP_ID}.{self._main_package_name}.Activator"
            export_package = f"{BackendGenerator.GROUP_ID}.{self._main_package_name}.*"

            generated_pom_xml = pom_xml_template.render(
                group_id=BackendGenerator.GROUP_ID,
                artifact_id=self._artifact_id,
                bundle_name=self._bundle_name,
                description=self._description,
                activator_location=activator_location,
                export_package=export_package
            )

        with open(f"{self._location_of_component_directory}/pom.xml", "w") as f:
            f.write(generated_pom_xml)

        print("Created pom.xml file in the component directory")

    def _generate_file_structure_for_java_project(self):
        group_id_dirs = BackendGenerator.GROUP_ID.split('.')
        file_structure_java_project = 'src/main/java/' + '/'.join(group_id_dirs) + '/' + self._main_package_name
        file_structure_resources = 'src/main/resources'
        file_structure_test = 'src/test/java'

        entire_project_structure = self._location_of_component_directory + '/' + file_structure_java_project
        entire_services_structure = entire_project_structure + '/services'
        entire_resources_structure = self._location_of_component_directory + '/' + file_structure_resources
        entire_test_structure = self._location_of_component_directory + '/' + file_structure_test

        # Generating File Structure for the created micro-component
        os.makedirs(entire_project_structure)
        os.mkdir(entire_services_structure)
        os.mkdir(entire_resources_structure)
        os.makedirs(entire_test_structure)

        print("Generated entire file structure for micro-component")

        class_name_components = [c.capitalize() for c in self._name.split("-")]
        class_name = "".join(class_name_components) + "Service"
        endpoint_name = "-".join(c.lower() for c in self._name.split("-"))

        # Generating the main Service Java Class
        with open(f'{self._scripts_path}/templates/Service.java.jinja') as file_:
            service_java = Template(file_.read())
            generated_service_java = service_java.render(
                group_id=BackendGenerator.GROUP_ID,
                main_package_name=self._main_package_name,
                class_name=class_name,
                endpoint_name=endpoint_name
            )

        with open(f"{entire_services_structure}/{class_name}.java", "w") as f:
            f.write(generated_service_java)

        print(f"Generated {class_name}.java file")

        # Generating the Activator Java Class
        with open(f'{self._scripts_path}/templates/Activator.java.jinja') as file_:
            activator_java = Template(file_.read())
            generated_activator_java = activator_java.render(
                group_id=BackendGenerator.GROUP_ID,
                main_package_name=self._main_package_name,
                endpoint_name=endpoint_name
            )

        with open(f"{entire_project_structure}/Activator.java", "w") as f:
            f.write(generated_activator_java)

        print("Generated Activator Java Class")

        # Generating the default view.js script file
        with open(f'{self._scripts_path}/templates/view.js.jinja') as file_:
            view_js = Template(file_.read())
            generated_view_js = view_js.render(
                endpoint_name=endpoint_name
            )

        with open(f"{entire_resources_structure}/view.js", "w") as f:
            f.write(generated_view_js)

        print("Generated view.js script")











