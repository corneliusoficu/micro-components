package vu.nl.dynamicplugins.bundlestore.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import vu.nl.dynamicplugins.bundlestore.models.Bundle;
import vu.nl.dynamicplugins.bundlestore.properties.BundlesProperties;
import vu.nl.dynamicplugins.bundlestore.services.BundlesDownloadService;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@Controller
public class BundleDownloadController {

    private final BundlesProperties bundlesProperties;
    private BundlesDownloadService downloadService;

    public BundleDownloadController(
            @Autowired BundlesProperties bundlesProperties,
            @Autowired BundlesDownloadService downloadService) {
        this.bundlesProperties = bundlesProperties;
        this.downloadService = downloadService;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(BundleDownloadController.class);

   @GetMapping("/bundles")
    public @ResponseBody
   List<Bundle> getAllAvailableDownloads() throws InterruptedException {
        LOGGER.info("Got request to obtain all available downloads!");
        List<Bundle> bundles = this.downloadService.getStoredBundles();
        return bundles;
    }

    @GetMapping("/bundles/download")
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
