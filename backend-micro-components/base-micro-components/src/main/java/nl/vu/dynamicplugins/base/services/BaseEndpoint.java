package nl.vu.dynamicplugins.base.services;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import nl.vu.dynamicplugins.base.utils.MicroComponentUtils;
import org.apache.cxf.dosgi.common.api.IntentsProvider;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

public class BaseEndpoint implements IntentsProvider {

    protected MicroComponentUtils componentUtils;

    public BaseEndpoint() {
        componentUtils = new MicroComponentUtils();
    }

    @GET
    @Produces(value = "text/javascript")
    @Path("view")
    public Response view() {
        InputStream inputStream = componentUtils.getViewFile(getClass().getClassLoader());
        return componentUtils.buildInputStreamResponseResponse(inputStream);
    }

    @GET
    @Produces(value = "text/javascript")
    @Path("health")
    public Response health() {
        return Response
                .ok("The micro-component is up and running!")
                .build();
    }

    @Override
    public List<?> getIntents() {
        return Collections.singletonList(new JacksonJaxbJsonProvider());
    }
}
