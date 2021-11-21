package nl.vu.dynamicplugins.core.lifecyclehandler.osgi;

import nl.vu.dynamicplugins.core.lifecyclehandler.Activator;
import nl.vu.dynamicplugins.core.lifecyclehandler.models.MicroComponent;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OSGIBundlesHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(OSGIBundlesHandler.class);
    private final static String MICRO_COMPONENT_PACKAGE_PREFIX = "nl.vu.dynamicplugins";
    private final static String MICRO_COMPONENT_PACKAGE_CORE_SUFFIX = ".core";

    public List<MicroComponent> getActiveMicroComponentBundleNames() {
        if(Activator.bundleContext == null) {
            LOGGER.error("Cannot retrieve list of installed bundles because of empty Bundle Context");
            return new ArrayList<>();
        }

        List<Bundle> bundlesList = Arrays.asList(Activator.bundleContext.getBundles());

        return bundlesList.stream()
                .filter(this::bundleIsActiveAndIsMicroComponent)
                .map(this::toMicroComponent)
                .collect(Collectors.toList());
    }

    private MicroComponent toMicroComponent(Bundle bundle) {
        MicroComponent microComponent = new MicroComponent();
        microComponent.setName(bundle.getSymbolicName());
        microComponent.setLocation(bundle.getLocation());
        return microComponent;
    }

    private boolean bundleIsActiveAndIsMicroComponent(Bundle bundle) {
        if(bundle.getState() != Bundle.ACTIVE) {
            return false;
        }

        String bundleName = bundle.getSymbolicName();
        if(!bundleName.startsWith(MICRO_COMPONENT_PACKAGE_PREFIX)) {
            return false;
        }

        String coreServicesPackageName = MICRO_COMPONENT_PACKAGE_PREFIX + MICRO_COMPONENT_PACKAGE_CORE_SUFFIX;
        return !bundleName.startsWith(coreServicesPackageName);
    }
}
