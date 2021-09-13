package nl.vu.dynamicplugins.productdetails;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator implements BundleActivator {

    private final static Logger LOGGER = LoggerFactory.getLogger(Activator.class);

    public void start(BundleContext arg0) throws Exception {
        LOGGER.info("Starting Products Details Bundle...");
    }

    public void stop(BundleContext arg0) throws Exception {
        LOGGER.info("Stopping Products Details Bundle...");
    }

}

