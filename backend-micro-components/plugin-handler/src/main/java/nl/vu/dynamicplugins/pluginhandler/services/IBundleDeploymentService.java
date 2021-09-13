package nl.vu.dynamicplugins.pluginhandler.services;

import nl.vu.dynamicplugins.pluginhandler.exceptions.BundleDeploymentException;
import nl.vu.dynamicplugins.pluginhandler.models.AddBundleRequest;

import java.io.IOException;

public interface IBundleDeploymentService {
    void deployBundle(AddBundleRequest request) throws BundleDeploymentException, IOException;
}
