import click

from karaf_instance import KarafInstance


@click.group()
def karaf():
    pass


@karaf.command("start")
def start_karaf():
    KarafInstance().start()


@karaf.command("stop")
def stop_karaf():
    KarafInstance().stop()


@karaf.command("list")
def list_karaf_bundles():
    KarafInstance().list_bundles()


@karaf.command("logs")
def tail_karaf_logs():
    KarafInstance().tail_logs()


@karaf.command("install")
@click.argument("--location")
def install_karaf_bundle(__location):
    KarafInstance().install_bundle_jar(__location)


@karaf.command("start-bundle")
@click.argument("name")
def start_bundle(name):
    KarafInstance().start_bundle_by_name(name)


@karaf.command("stop-bundle")
@click.argument("name")
def start_bundle(name):
    KarafInstance().stop_bundle_by_name(name)


@karaf.command("uninstall-bundle")
@click.argument("name")
def start_bundle(name):
    KarafInstance().uninstall_bundle_by_name(name)