package nl.vu.dynamicplugins.productslist.services;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

public interface ProductsListEndpoint {

    @GET
    @Produces(value = "text/javascript")
    @Path("view")
    public Response view();

    @GET
    @Produces(value = "text/javascript")
    @Path("health")
    public Response health();
}
