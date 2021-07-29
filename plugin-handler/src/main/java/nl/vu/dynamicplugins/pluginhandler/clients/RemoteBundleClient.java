package nl.vu.dynamicplugins.pluginhandler.clients;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/bundles")
public interface RemoteBundleClient {

    @GET
    @Path("/download")
    Response download(@QueryParam("name") String bundleName);
}
