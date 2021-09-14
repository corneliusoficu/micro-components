package nl.vu.dynamicplugins.base.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

public class FileUtils {

    Logger logger = LoggerFactory.getLogger(FileUtils.class);

    public InputStream getViewFile() {
        ClassLoader classLoader = getClass().getClassLoader();
        URL url = classLoader.getResource("view.js");
        logger.info("Loading view.js file from location: {}", url);
        return classLoader.getResourceAsStream("view.js");
    }
}
