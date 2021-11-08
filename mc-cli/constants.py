import os
import pathlib

MC_CLI_HOME_PATH = pathlib.Path(__file__).parent.resolve()
MC_PREFIX = "mc-"
MC_BACKEND_SUFFIX = "-backend"

CURRENT_WORKING_DIR = os.getcwd()

KARAF_BIN_LOCATION = '/Users/corneliudumitru.sofi/Downloads/apache-karaf-4.3.2/bin'
KARAF_USER_PRIVATE_KEY_LOCATION = f"{MC_CLI_HOME_PATH}/karaf.id_rsa"