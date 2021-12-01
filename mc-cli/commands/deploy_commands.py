import os

import click

from deployers.backend.jar_deployer import JarDeployer
from deployers.mc_deployer import MicroComponentDeployer


@click.group(invoke_without_command=True)
@click.option("--update", is_flag=True)
def deploy(update):
    MicroComponentDeployer().deploy(os.getcwd(), force_update=update)


@deploy.command("jar")
@click.option("--update", is_flag=True)
def deploy_local_jar(update):
    JarDeployer().deploy(os.getcwd(), force_update=update)


@deploy.command("micro-component")
@click.option("--update", is_flag=True)
def deploy_micro_component(update):
    MicroComponentDeployer().deploy(os.getcwd(), force_update=update)