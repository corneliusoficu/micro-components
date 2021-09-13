package nl.vu.dynamicplugins.pluginhandler.controllers;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import nl.vu.dynamicplugins.pluginhandler.exceptions.BundleDeploymentException;
import nl.vu.dynamicplugins.pluginhandler.models.AddBundleRequest;
import nl.vu.dynamicplugins.pluginhandler.models.AddBundleResponse;
import nl.vu.dynamicplugins.pluginhandler.services.IBundleDeploymentService;
import nl.vu.dynamicplugins.pluginhandler.services.LocalBundleDeploymentService;
import nl.vu.dynamicplugins.pluginhandler.services.RemoteSourceDeploymentService;
import org.apache.cxf.dosgi.common.api.IntentsProvider;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component(service = IBundleController.class, immediate = true, property =
        {
                "service.exported.interfaces=*",
                "service.exported.configs=org.apache.cxf.rs",
                "org.apache.cxf.rs.address=/api",
                "cxf.bus.prop.skip.default.json.provider.registration=true"
    }
)
public class BundleController implements IBundleController, IntentsProvider {

    private final static Logger LOGGER = LoggerFactory.getLogger(BundleController.class);
    private IBundleDeploymentService bundleDeploymentService;

    public BundleController() throws IOException {
        this.bundleDeploymentService = new RemoteSourceDeploymentService();
    }

    @Override
    public Response addBundle(AddBundleRequest request) {
        AddBundleResponse response = new AddBundleResponse();
        Response.ResponseBuilder responseBuilder;

        try {
            this.bundleDeploymentService.deployBundle(request);
            response.setMessage("Deployed bundle successfully!");
            responseBuilder = Response.status(Response.Status.OK).entity(response);
        } catch (IOException e) {
            LOGGER.warn("Got exception when deploying bundle", e);
            response.setMessage(e.getMessage());
            responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response);
        } catch (BundleDeploymentException e) {
            LOGGER.warn("Got exception when deploying bundle", e);
            response.setMessage(e.getMessage());
            responseBuilder = Response.status(Response.Status.BAD_REQUEST).entity(response);
        }

        return responseBuilder.build();
    }

    @Override
    public List<?> getIntents() {
        return Collections.singletonList(new JacksonJaxbJsonProvider());
    }

}
