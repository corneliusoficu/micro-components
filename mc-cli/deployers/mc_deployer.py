import os
import sys

import constants
from deployers.backend.jar_deployer import JarDeployer

from deployers.deployer import Deployer
from deployers.frontend.angular_deployer import AngularDeployer
from shutil import copyfile


class MicroComponentDeployer(Deployer):
    def deploy(self, location, force_update=False):
        project_name = self._get_micro_component_name(location)
        print(f"Micro component name: {project_name}")
        backend_location, frontend_location = self._identify_microcomponent_folders(location)
        print("Deploying Frontend component")
        AngularDeployer().deploy(frontend_location)
        self._move_fronted_js_file_to_backend_project(frontend_location, backend_location, project_name)
        JarDeployer().deploy(backend_location, force_update=force_update)

    def _identify_microcomponent_folders(self, location):
        directories = [f for f in os.listdir(location) if os.path.isdir(os.path.join(location, f))]
        frontend_and_backend_dirs = list(filter(self._filter_mc_elements, directories))

        if len(frontend_and_backend_dirs) != 2:
            print("Ambiguos number of Micro-Component folder elements!")
            sys.exit(-1)

        backend_project_location = frontend_and_backend_dirs[0]
        frontend_project_location = frontend_and_backend_dirs[1]

        if frontend_and_backend_dirs[0].endswith(constants.MC_FRONTEND_SUFFIX):
            frontend_project_location = frontend_and_backend_dirs[0]
            backend_project_location = frontend_and_backend_dirs[1]

        backend_project_location = os.path.join(location, backend_project_location)
        frontend_project_location = os.path.join(location, frontend_project_location)

        return backend_project_location, frontend_project_location

    def _filter_mc_elements(self, folder_name):
        return folder_name.endswith((constants.MC_BACKEND_SUFFIX, constants.MC_FRONTEND_SUFFIX))

    def _get_micro_component_name(self, location):
        realpath = os.path.realpath(location)
        _, filename = os.path.split(realpath)

        if not filename.startswith(constants.MC_PREFIX):
            print("Folder name is not a valid micro-component name!")
            sys.exit(-1)

        return filename.replace(constants.MC_PREFIX, "")

    def _move_fronted_js_file_to_backend_project(self, frontend_location, backend_location, project_name):
        frontend_js_filename = f"{project_name}.js"
        frontend_js_location = f"{frontend_location}/{frontend_js_filename}"
        backend_location = f"{backend_location}/src/main/resources/view.js"
        print(f"Moving js file from {frontend_js_location} to {backend_location}")
        copyfile(frontend_js_location, backend_location)
