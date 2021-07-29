package nl.vu.dynamicplugins.pluginhandler.controllers;

import nl.vu.dynamicplugins.pluginhandler.models.AddBundleRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("bundles")
public interface IBundleController {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addBundle(AddBundleRequest request);
}

