package nl.vu.dynamicplugins.base.services;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.apache.cxf.dosgi.common.api.IntentsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.List;

public class BaseEndpoint implements IntentsProvider {
    Logger logger = LoggerFactory.getLogger(BaseEndpoint.class);

    @GET
    @Produces(value = "text/javascript")
    @Path("view")
    public Response view() {
        InputStream inputStream = getViewFile(getClass().getClassLoader());
        return buildInputStreamResponseResponse(inputStream);
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

    public InputStream getViewFile(ClassLoader classLoader) {
        URL url = classLoader.getResource("view.js");
        logger.info("Loading view.js file from location: {}", url);
        return classLoader.getResourceAsStream("view.js");
    }

    public Response buildInputStreamResponseResponse(InputStream inputStream) {
        Response.ResponseBuilder rb = Response
                .ok(inputStream)
                .header("Access-Control-Allow-Origin", "*");
        return rb.build();
    }
}
