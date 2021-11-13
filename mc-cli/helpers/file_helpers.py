import json

from jinja2 import Template


def create_template_file(template_location, file_location, **template_ags):
    with open(template_location) as file_:
        view_js = Template(file_.read())
        generated_view_js = view_js.render(template_ags)

    with open(file_location, "w") as f:
        f.write(generated_view_js)

    print(f"Rendered template: {template_location} to location: {file_location}")


def read_json_file(json_location):
    with open(json_location, "r") as json_file:
        content = json.load(json_file)
        return content


def write_json_file(json_location, content):
    with open(json_location, "w") as json_file:
        json.dump(content, json_file)
