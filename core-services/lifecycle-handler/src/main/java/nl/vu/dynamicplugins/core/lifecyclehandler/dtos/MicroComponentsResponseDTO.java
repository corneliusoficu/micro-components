package nl.vu.dynamicplugins.core.lifecyclehandler.dtos;

import java.util.List;

public class MicroComponentsResponseDTO {
    private List<MicroComponentDTO> micro_components;

    public List<MicroComponentDTO> getMicro_components() {
        return micro_components;
    }

    public void setMicro_components(List<MicroComponentDTO> micro_components) {
        this.micro_components = micro_components;
    }
}
