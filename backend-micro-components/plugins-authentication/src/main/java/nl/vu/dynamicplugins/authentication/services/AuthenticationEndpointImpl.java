package nl.vu.dynamicplugins.authentication.services;

import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.io.InputStream;

@Component(service = IAuthenticationEndpoint.class, immediate = true, property = //
        { //
                "service.exported.interfaces=*", //
                "service.exported.configs=org.apache.cxf.rs", //
                "org.apache.cxf.rs.address=/plugins-authentication", "cxf.bus.prop.skip.default.json.provider.registration=true" } //
)
public class AuthenticationEndpointImpl implements IAuthenticationEndpoint {

    static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationEndpointImpl.class);

    @Override
    public Response view() {
        LOGGER.info("Requesting view for products-list!");

        Response.ResponseBuilder rb = Response
                .ok(getViewFile())
                .header("Access-Control-Allow-Origin", "*");
        return rb.build();
    }

    @Override
    public Response health() {
        return null;
    }

    private InputStream getViewFile() {
        return getClass().getClassLoader().getResourceAsStream("view.js");
    }
}
