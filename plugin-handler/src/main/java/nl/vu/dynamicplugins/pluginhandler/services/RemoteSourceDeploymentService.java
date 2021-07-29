package nl.vu.dynamicplugins.pluginhandler.services;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

import nl.vu.dynamicplugins.pluginhandler.clients.RemoteBundleClient;
import nl.vu.dynamicplugins.pluginhandler.exceptions.BundleDeploymentException;
import nl.vu.dynamicplugins.pluginhandler.models.AddBundleRequest;
import nl.vu.dynamicplugins.pluginhandler.properties.DeploymentProperties;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Properties;

public class RemoteSourceDeploymentService implements IBundleDeploymentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteSourceDeploymentService.class);

    Properties deploymentProperties = DeploymentProperties.get();
    RemoteBundleClient remoteService;

    public RemoteSourceDeploymentService() {
        remoteService = setupRemoteBundleClient();
    }

    @Override
    public void deployBundle(AddBundleRequest request) throws IOException, BundleDeploymentException {
        String bundleName = String.format("%s-%s.%s",
                request.getName(),
                request.getVersion(),
                request.getExtension());

        LOGGER.info("Attempting to deploy bundle: {} by downloading bundle from remote store!", bundleName);

        Response response = remoteService.download(bundleName);
        LOGGER.info("Got status code: {} when downloading bundle: {}", response.getStatus(), bundleName);
        if(response.getStatus() != Response.Status.OK.getStatusCode()) {
            throw new BundleDeploymentException("The remote store does not contain the requested bundle!");
        }
        InputStream in = response.readEntity(InputStream.class);
        File deployFolder = new File(
                System.getProperty("karaf.home") +
                deploymentProperties.getProperty("deploy.folder"));

        if(!deployFolder.exists()) {
            throw new IOException(String.format(
                    "Cannot find deployment dir! Location: %s",
                    deployFolder.getAbsolutePath()));
        }

        Path path = Paths.get(deployFolder.getAbsolutePath(), bundleName);
        Files.copy(in, path);
    }

    private RemoteBundleClient setupRemoteBundleClient() {
        return JAXRSClientFactory.create(
                "http://localhost:2000",
                RemoteBundleClient.class,
                Collections.singletonList(new JacksonJaxbJsonProvider()));
    }
}
