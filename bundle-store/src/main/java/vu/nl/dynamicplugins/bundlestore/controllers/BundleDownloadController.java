package vu.nl.dynamicplugins.bundlestore.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vu.nl.dynamicplugins.bundlestore.properties.BundlesProperties;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
@RequestMapping(value = "/bundles")
public class BundleDownloadController {

    BundlesProperties bundlesProperties;

    public BundleDownloadController(@Autowired BundlesProperties bundlesProperties) {
        this.bundlesProperties = bundlesProperties;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(BundleDownloadController.class);

    @GetMapping("/download")
    public void downloadBundle(@RequestParam("name") String bundleName, HttpServletResponse response) throws IOException {
        try {
            LOGGER.info("Got request to download bundle: {}", bundleName);
            String fullBundlePath = String.format("%s/%s", bundlesProperties.getStorePath(), bundleName);

            File bundle = new File(fullBundlePath);
            if(!bundle.exists()) {
                throw new IOException(String.format("The specified bundle does not exist: %s", fullBundlePath));
            }

            InputStream is = new FileInputStream(new File(fullBundlePath));
            org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException exception) {
            LOGGER.error("Error writing file to output stream. Filename was '{}'", bundleName, exception);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
