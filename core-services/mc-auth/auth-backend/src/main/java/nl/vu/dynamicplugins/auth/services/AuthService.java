package nl.vu.dynamicplugins.auth.services;


import nl.vu.dynamicplugins.core.base.services.BaseEndpoint;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.io.InputStream;

@Component(service = AuthService.class, immediate = true, property = //
        { //
                "service.exported.interfaces=*", //
                "service.exported.configs=org.apache.cxf.rs", //
                "org.apache.cxf.rs.address=/auth", "cxf.bus.prop.skip.default.json.provider.registration=true" } //
)
public class AuthService extends BaseEndpoint {

    private final static Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

    @GET
    @Produces({"text/javascript"})
    @Path("app-auth.js")
    @Override
    public Response view() {
        LOGGER.info("Requesting view for auth!");
        InputStream viewFileInputStream = getViewFile(getClass().getClassLoader());
        return buildInputStreamResponseResponse(viewFileInputStream);
    }

    @Override
    public Response health() {
        LOGGER.info("Requesting health for auth");
        return super.health();
    }
}