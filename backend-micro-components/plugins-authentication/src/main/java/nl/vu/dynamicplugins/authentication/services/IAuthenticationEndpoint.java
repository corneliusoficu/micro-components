package nl.vu.dynamicplugins.authentication.services;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

public interface IAuthenticationEndpoint {

    @GET
    @Produces(value = "text/javascript")
    @Path("view")
    public Response view();

    @GET
    @Produces(value = "text/javascript")
    @Path("health")
    public Response health();
}
