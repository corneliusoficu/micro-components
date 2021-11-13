import sys


class Generator:
    def generate(self, directory):
        print("Fatal error, the base generate function should not be called!")
        sys.exit(-1)


class ViewProvidingGenerator(Generator):

    def generate(self, directory):
        print("This method should not be executed!")
        sys.exit(-1)

    def get_view_location(self):
        print("This method should not be executed!")
        sys.exit(-1)