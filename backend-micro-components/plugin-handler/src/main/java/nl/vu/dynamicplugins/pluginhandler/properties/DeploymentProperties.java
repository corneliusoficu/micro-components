package nl.vu.dynamicplugins.pluginhandler.properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DeploymentProperties extends Properties {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeploymentProperties.class);
    private static DeploymentProperties instance = null;

    private DeploymentProperties() {
        super();
        try {
            InputStream resourceStream = getClass().getClassLoader().getResourceAsStream("deployment.properties");
            this.load(resourceStream);
        } catch (IOException e) {
            LOGGER.error("Failed to load properties from file", e);
        }
    }

    public static DeploymentProperties get() {
        if(instance == null) {
            instance = new DeploymentProperties();
        }
        return instance;
    }
}
