import os

import click

from deployers.backend.jar_deployer import JarDeployer


@click.group()
def deploy():
    pass


@deploy.command("jar")
def deploy_local_jar():
    JarDeployer().deploy(os.getcwd())