package nl.vu.dynamicplugins.base.services;

import nl.vu.dynamicplugins.base.utils.FileUtils;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

public class BaseEndpoint {

    @GET
    @Produces(value = "text/javascript")
    @Path("view")
    default Response view() {
        FileUtils fileUtils = new FileUtils();
        Response.ResponseBuilder rb = Response
                .ok(fileUtils.getViewFile())
                .header("Access-Control-Allow-Origin", "*");
        return rb.build();
    }

    @GET
    @Produces(value = "text/javascript")
    @Path("health")
    default Response health() {
        return Response
                .ok("The micro-component is up and running!")
                .build();
    }
}
