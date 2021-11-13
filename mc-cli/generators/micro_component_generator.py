import os
import sys

from generators.backend.java_jax_rs_generator import JavaJaxRSGenerator
from generators.frontend.angular_generator import AngularGenerator
from generators.generator import Generator
from helpers.file_helpers import copy_to_location


class MicroComponentGenerator(Generator):
    def __init__(self, name, description, group_id="nl.vu.dynamicplugins", ui="angular"):
        self._name = name
        self._description = description
        self._group_id = group_id
        self._ui = ui

        # TODO: Replace hardcoded backend type
        self._backend_generator = MicroComponentGenerator.backend_generator_by_type(name, description,group_id, "JAX-RS")
        self._frontend_generator = MicroComponentGenerator.frontend_generator_by_ui_type(ui, self._name)

    def generate(self, mc_directory):
        self._create_folder_for_component(mc_directory)
        self._backend_generator.generate(mc_directory)
        self._frontend_generator.generate(mc_directory)

        print("Updating the view.js file")
        view_js_location = self._backend_generator.get_view_location()
        exported_frontend_location = self._frontend_generator.get_view_location()

        copy_to_location(view_js_location, exported_frontend_location)

    def _create_folder_for_component(self, mc_directory):
        if os.path.exists(mc_directory):
            print(f"Directory for component already exists: {mc_directory}\nAborting!")
            sys.exit(-1)

        print(f"Creating component directory: {mc_directory}")
        os.mkdir(mc_directory)

    @staticmethod
    def format_project_name(name):
        lowered_name = name.lower()
        replaced_name = lowered_name.replace("_", "-")
        return replaced_name

    @staticmethod
    def frontend_generator_by_ui_type(ui_type, name):
        if ui_type == "angular":
            return AngularGenerator(name)

    @staticmethod
    def backend_generator_by_type(name, description, group_id, type_of_backend):
        if type_of_backend == "JAX-RS":
            return JavaJaxRSGenerator(name, description, group_id)













