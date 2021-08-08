package vu.nl.dynamicplugins.bundlestore.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vu.nl.dynamicplugins.bundlestore.models.Bundle;
import vu.nl.dynamicplugins.bundlestore.properties.BundlesProperties;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BundlesDownloadService {
    private final BundlesProperties bundlesProperties;
    private final String bundleInformationRegex = "(.*)-(((\\d\\.?)+)(-SNAPSHOT)?)\\.(.*)";

    public BundlesDownloadService(@Autowired BundlesProperties bundlesProperties) {
        this.bundlesProperties = bundlesProperties;
    }

    public List<Bundle> getStoredBundles() {
        String bundleStoragePath = bundlesProperties.getStorePath();
        File bundleStorageFolder = new File(bundleStoragePath);
        List<Bundle> bundlesList = new ArrayList<>();

        for(String pathName: bundleStorageFolder.list()) {
            Bundle bundle = this.extractBundleInformationFromFileName(pathName);
            if(bundle != null) {
                bundlesList.add(bundle);
            }
        }

        return bundlesList;
    }

    private Bundle extractBundleInformationFromFileName(String pathName) {
        Pattern pattern = Pattern.compile(bundleInformationRegex);
        Matcher matcher = pattern.matcher(pathName);
        Bundle bundle = null;


        if(matcher.matches()) {
            String name = matcher.group(1);
            String version = matcher.group(2);
            String extension = matcher.group(6);
            bundle = new Bundle();
            bundle.setName(name);
            bundle.setVersion(version);
            bundle.setExtension(extension);
        }

        return bundle;
    }

}
