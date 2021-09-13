package nl.vu.dynamicplugins.productslist.services;


import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.apache.cxf.dosgi.common.api.IntentsProvider;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.io.*;
import java.util.Arrays;
import java.util.List;

@Component(service = ProductsListEndpoint.class, immediate = true, property = //
        { //
                "service.exported.interfaces=*", //
                "service.exported.configs=org.apache.cxf.rs", //
                "org.apache.cxf.rs.address=/products-list", "cxf.bus.prop.skip.default.json.provider.registration=true" } //
)
public class ProductsList implements ProductsListEndpoint, IntentsProvider {

    private final static Logger LOGGER = LoggerFactory.getLogger(ProductsList.class);

    public Response view() {

        LOGGER.info("Requesting view for products-list!");

        Response.ResponseBuilder rb = Response
                .ok(getViewFile())
                .header("Access-Control-Allow-Origin", "*");
        return rb.build();
    }

    @Override
    public Response health() {
        return Response
                .ok("Products list bundle is up and running!")
                .build();
    }

    private InputStream getViewFile() {
        return getClass().getClassLoader().getResourceAsStream("view.js");
    }

    @Override
    public List<?> getIntents() {
        return Arrays.asList(new JacksonJaxbJsonProvider());
    }
}
