package vu.nl.dynamicplugins.bundlestore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages={"vu.nl.dynamicplugins.bundlestore"})
public class BundleStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(BundleStoreApplication.class, args);
	}

}
