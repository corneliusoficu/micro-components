package vu.nl.dynamicplugins.bundlestore.models;

import java.util.List;

public class AvailableBundlesResponse {
    String count;
    List<Bundle> bundles;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<Bundle> getBundles() {
        return bundles;
    }

    public void setBundles(List<Bundle> bundles) {
        this.bundles = bundles;
    }
}
