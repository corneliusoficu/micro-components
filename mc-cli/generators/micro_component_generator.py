import os
import sys

from generators.backend_generator import BackendGenerator
from constants import MC_PREFIX


class MicroComponentGenerator:
    def __init__(self, name, description, group_id="nl.vu.dynamicplugins", ui="angular"):
        self._name = name.lower()
        self._description = description
        self._group_id = group_id
        self._ui = ui
        self._backend_generator = BackendGenerator(self._name, description, group_id)
        self._current_work_directory = os.getcwd()
        self._mc_directory = f"{self._current_work_directory}/{MC_PREFIX}{self._name}"

    def generate(self):
        self._create_folder_for_component()
        self._backend_generator.generate(self._mc_directory)

    def _create_folder_for_component(self):
        if os.path.exists(self._mc_directory):
            print(f"Directory for component already exists: {self._mc_directory}\nAborting!")
            sys.exit(-1)

        print(f"Creating component directory: {self._mc_directory}")
        os.mkdir(self._mc_directory)












