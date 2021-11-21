package nl.vu.dynamicplugins.core.lifecyclehandler;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator implements BundleActivator {

    private final static Logger LOGGER = LoggerFactory.getLogger(Activator.class);

    public static BundleContext bundleContext = null;

    public void start(BundleContext bundleContext) throws Exception {
        LOGGER.info("Starting lifecycle-handler Micro-Component...");
        if(Activator.bundleContext == null) {
            Activator.bundleContext = bundleContext;
        } else {
            LOGGER.warn("Cannot assign bundle context as it was already assigned!");
        }
    }

    public void stop(BundleContext arg0) throws Exception {
        LOGGER.info("Stopping lifecycle-handler Micro-Component...");
    }

}