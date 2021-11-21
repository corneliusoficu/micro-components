package nl.vu.dynamicplugins.core.lifecyclehandler.services;


import nl.vu.dynamicplugins.core.base.services.BaseEndpoint;
import nl.vu.dynamicplugins.core.lifecyclehandler.dtos.MicroComponentsNamesListDTO;
import nl.vu.dynamicplugins.core.lifecyclehandler.osgi.OSGIBundlesHandler;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.List;

@Component(service = LifecycleHandlerService.class, immediate = true, property = //
        { //
                "service.exported.interfaces=*", //
                "service.exported.configs=org.apache.cxf.rs", //
                "org.apache.cxf.rs.address=/lifecycle-handler", "cxf.bus.prop.skip.default.json.provider.registration=true" } //
)
public class LifecycleHandlerService extends BaseEndpoint {

    private final static Logger LOGGER = LoggerFactory.getLogger(LifecycleHandlerService.class);
    private final OSGIBundlesHandler osgiBundlesHandler;

    public LifecycleHandlerService() {
        osgiBundlesHandler = new OSGIBundlesHandler();
    }

    @GET
    @Produces({"application/json"})
    @Path("micro-components")
    public Response retrieveListOfAvailableMicroComponents() {
        List<String> microComponentsNames = osgiBundlesHandler.getActiveMicroComponentBundleNames();
        MicroComponentsNamesListDTO microComponentsNamesListDTO = new MicroComponentsNamesListDTO();
        microComponentsNamesListDTO.setMicro_components_names(microComponentsNames);
        LOGGER.info("Returning the list of micro-components: {}", microComponentsNames);
        return Response.ok(microComponentsNamesListDTO).build();
    }
}