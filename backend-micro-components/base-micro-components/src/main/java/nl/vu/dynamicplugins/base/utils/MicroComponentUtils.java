package nl.vu.dynamicplugins.base.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.net.URL;

public class MicroComponentUtils {

    Logger logger = LoggerFactory.getLogger(MicroComponentUtils.class);

    public InputStream getViewFile(ClassLoader classLoader) {
        URL url = classLoader.getResource("view.js");
        logger.info("Loading view.js file from location: {}", url);
        return classLoader.getResourceAsStream("view.js");
    }

    public Response buildInputStreamResponseResponse(InputStream inputStream) {
        Response.ResponseBuilder rb = Response
                .ok(inputStream)
                .header("Access-Control-Allow-Origin", "*");
        return rb.build();
    }
}
