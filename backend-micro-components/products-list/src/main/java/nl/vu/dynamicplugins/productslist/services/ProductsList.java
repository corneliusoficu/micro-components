package nl.vu.dynamicplugins.productslist.services;


import nl.vu.dynamicplugins.base.services.BaseEndpoint;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.io.InputStream;

@Component(service = ProductsList.class, immediate = true, property = //
        { //
                "service.exported.interfaces=*", //
                "service.exported.configs=org.apache.cxf.rs", //
                "org.apache.cxf.rs.address=/products-list", "cxf.bus.prop.skip.default.json.provider.registration=true" } //
)
public class ProductsList extends BaseEndpoint {

    private final static Logger LOGGER = LoggerFactory.getLogger(ProductsList.class);

    @Override
    public Response view() {
        LOGGER.info("Requesting view for products-list!");
        InputStream viewFileInputStream = componentUtils.getViewFile(getClass().getClassLoader());
        return componentUtils.buildInputStreamResponseResponse(viewFileInputStream);
    }

    @Override
    public Response health() {
        LOGGER.info("Requesting health for products-list");
        return super.health();
    }
}
