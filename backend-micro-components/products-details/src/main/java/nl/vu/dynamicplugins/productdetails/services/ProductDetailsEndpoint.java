package nl.vu.dynamicplugins.productdetails.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

public interface ProductDetailsEndpoint {

    @GET
    @Produces(value = "text/javascript")
    @Path("view")
    public Response view();

    @GET
    @Produces(value = "text/javascript")
    @Path("health")
    public Response health();
}
