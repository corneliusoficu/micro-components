import subprocess
import sys

from deployers.deployer import Deployer

import os


class AngularDeployer(Deployer):
    def deploy(self, location):
        files = os.listdir(location)
        package_json_files = list(filter(lambda f: f == "package.json", files))

        if len(package_json_files) != 1:
            print(f"Could not identify package.json in location: {location} to build Angular project, exiting....")
            sys.exit(-1)

        self._run_npm_build()
        self._run_npm_package()

    def _run_npm_build(self):
        print("Running Angular build command ")
        subprocess.run(['npm', 'run', 'build'])

    def _run_npm_package(self):
        print("Running angular package command")
        subprocess.run(["npm", "run", "package"])


if __name__ == '__main__':
    AngularDeployer().deploy(os.getcwd())
