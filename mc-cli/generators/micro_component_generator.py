import os
import sys

from generators.backend.java_jax_rs_generator import JavaJaxRSGenerator
from generators.frontend.angular_generator import AngularGenerator
from constants import MC_PREFIX


class MicroComponentGenerator:
    def __init__(self, name, description, group_id="nl.vu.dynamicplugins", ui="angular"):
        self._name = MicroComponentGenerator.format_project_name(name)
        self._description = description
        self._group_id = group_id
        self._ui = ui
        # TODO: Replace hardcoded backend type
        self._backend_generator = MicroComponentGenerator.backend_generator_by_type(name, description,
                                                                                    group_id, "JAX-RS")
        self._frontend_generator = MicroComponentGenerator.frontend_generator_by_ui_type(ui, self._name)
        self._current_work_directory = os.getcwd()
        self._mc_directory = f"{self._current_work_directory}/{MC_PREFIX}{self._name}"

    def generate(self):
        self._create_folder_for_component()
        self._backend_generator.generate(self._mc_directory)
        self._frontend_generator.generate(self._mc_directory)

    def _create_folder_for_component(self):
        if os.path.exists(self._mc_directory):
            print(f"Directory for component already exists: {self._mc_directory}\nAborting!")
            sys.exit(-1)

        print(f"Creating component directory: {self._mc_directory}")
        os.mkdir(self._mc_directory)

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













