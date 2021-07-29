package nl.vu.dynamicplugins.pluginhandler.models;

public class AddBundleRequest {
    String name;
    String version;
    String extension;

    public String getName() {
        return name;
    }

    public void setName(String bundleName) {
        this.name = bundleName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
