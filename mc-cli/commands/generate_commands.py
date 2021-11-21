import os

import click

import constants
from generators.backend.java_jax_rs_generator import JavaJaxRSGenerator
from generators.micro_component_generator import MicroComponentGenerator


@click.group()
def generate():
    pass


@generate.command("micro-component")
@click.argument("name")
@click.option("--description", "description", default="Micro-Component generated using mc-cli")
@click.option("--group", "group", default="nl.vu.dynamicplugins")
@click.option("--ui", "ui", type=click.Choice(['angular', 'react', 'vanilla']), default="angular")
def generate_micro_component(name, description, group, ui):
    print(f"Generating a micro-component with the name: {name}")
    name = MicroComponentGenerator.format_project_name(name)
    mc_directory = f"{os.getcwd()}/{constants.MC_PREFIX}{name}"
    MicroComponentGenerator(name, description, group_id=group, ui=ui).generate(mc_directory)


@generate.command("jax_rs_backend")
@click.argument("name")
@click.option("--description", "description", default="Backend generator for JAX-RS Project using mc-cli")
@click.option("--group", "group", default="nl.vu.dynamicplugins")
def generate_jax_rs_backend(name, description, group):
    name = MicroComponentGenerator.format_project_name(name)
    print(f"Generating a JAX-RS micro-component backend with name: {name}")
    JavaJaxRSGenerator(name, description,group).generate(os.getcwd())