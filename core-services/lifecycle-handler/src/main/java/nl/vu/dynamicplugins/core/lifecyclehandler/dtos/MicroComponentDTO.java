package nl.vu.dynamicplugins.core.lifecyclehandler.dtos;

public class MicroComponentDTO {
    private String name;
    private String location;

    public MicroComponentDTO() {
    }

    public MicroComponentDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
