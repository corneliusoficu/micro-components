package nl.vu.dynamicplugins.uiauthentication.services;


import nl.vu.dynamicplugins.base.services.BaseEndpoint;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.io.InputStream;

@Component(service = UiAuthenticationService.class, immediate = true, property = //
        { //
                "service.exported.interfaces=*", //
                "service.exported.configs=org.apache.cxf.rs", //
                "org.apache.cxf.rs.address=/ui-authentication", "cxf.bus.prop.skip.default.json.provider.registration=true" } //
)
public class UiAuthenticationService extends BaseEndpoint {

    private final static Logger LOGGER = LoggerFactory.getLogger(UiAuthenticationService.class);

    @Override
    public Response view() {
        LOGGER.info("Requesting view for ui-authentication!");
        InputStream viewFileInputStream = getViewFile(getClass().getClassLoader());
        return buildInputStreamResponseResponse(viewFileInputStream);
    }

    @Override
    public Response health() {
        LOGGER.info("Requesting health for ui-authentication");
        return super.health();
    }
}