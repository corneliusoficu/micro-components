package nl.vu.dynamicplugins.pluginhandler.services;

import nl.vu.dynamicplugins.pluginhandler.controllers.BundleController;
import nl.vu.dynamicplugins.pluginhandler.exceptions.BundleDeploymentException;
import nl.vu.dynamicplugins.pluginhandler.models.AddBundleRequest;
import nl.vu.dynamicplugins.pluginhandler.properties.DeploymentProperties;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class LocalBundleDeploymentService implements IBundleDeploymentService {

    private final static Logger LOGGER = LoggerFactory.getLogger(BundleController.class);

    Properties properties = DeploymentProperties.get();

    @Override
    public void deployBundle(AddBundleRequest request) throws BundleDeploymentException, IOException {
        File sourceFolder = new File(properties.getProperty("source-folder"));
        File targetFolder = new File(properties.getProperty("deploy-folder"));

        LOGGER.info("Using the following folder as source folder: {}", sourceFolder.getAbsolutePath());
        LOGGER.info("Using the following folder as target deploy folder: {}", targetFolder.getAbsolutePath());

        if(!sourceFolder.exists() || !targetFolder.exists()) {
            throw new BundleDeploymentException("Could not find the source or target folder for deployment!");
        }

        String bundleName = String.format("%s-%s.%s",
                request.getName(),
                request.getVersion(),
                request.getVersion());

        File bundle = new File(bundleName);

        if(!bundle.exists()) {
            throw new BundleDeploymentException("Could not find the requested bundle in the local source folder!");
        }

        LOGGER.info("Executing deployment of bundle: {} to deploy directory: {}",
                bundle.getAbsolutePath(),
                targetFolder.getAbsolutePath());

        FileUtils.copyFileToDirectory(bundle, targetFolder);
    }
}
