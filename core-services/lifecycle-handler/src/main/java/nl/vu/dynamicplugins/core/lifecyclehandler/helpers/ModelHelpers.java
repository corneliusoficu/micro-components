package nl.vu.dynamicplugins.core.lifecyclehandler.helpers;

import nl.vu.dynamicplugins.core.lifecyclehandler.dtos.MicroComponentDTO;
import nl.vu.dynamicplugins.core.lifecyclehandler.models.MicroComponent;

public class ModelHelpers {
    public static MicroComponentDTO microComponentToMicroComponentDTO(MicroComponent microComponent) {
        MicroComponentDTO microComponentDTO = new MicroComponentDTO();
        microComponentDTO.setLocation(microComponent.getLocation());
        microComponentDTO.setName(microComponent.getName());
        return microComponentDTO;

    }
}
