import os
import pathlib
import re

MC_CLI_HOME_PATH = pathlib.Path(__file__).parent.resolve()
MC_PREFIX = "mc-"
MC_BACKEND_SUFFIX = "-backend"
MC_FRONTEND_SUFFIX = "-frontend"

CURRENT_WORKING_DIR = os.getcwd()

# TODO: Change this to point to correct location or read from PATH
KARAF_BIN_LOCATION = '/Users/corneliudumitru.sofi/Downloads/apache-karaf-4.3.2/bin'
# TODO: Change this to point to correct location or read from PATH
KARAF_USER_PRIVATE_KEY_LOCATION = f"{MC_CLI_HOME_PATH}/karaf.id_rsa"

ANGULAR_REGEX_CLI_VERSION = re.compile(r"Angular CLI: *((\d+)\.?(\d+)?\.?(\d+)?)")
ANGULAR_DEFAULT_STYLE = "css"
ANGULAR_DEFAULT_ROUTING = "false"
ANGULAR_ES_VERSION = "es5"
