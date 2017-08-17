package org.substeps.webdriver;

import com.technophobia.substeps.model.exception.SubstepsConfigurationException;
import com.typesafe.config.Config;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ian on 12/12/16.
 */
public enum DriverFactoryRegistry implements WebdriverSubstepsConfigurationKeys{

    INSTANCE;

    private static final Logger log = LoggerFactory.getLogger(DriverFactoryRegistry.class);

    private Map<String, DriverFactory> registry = new HashMap<>();

    public void register(DriverFactoryKey key) {

        log.debug("registering " + key.getFactory() + " to key: "  + key.getKey() );

        if (registry.containsKey(key.getKey())){
            log.info("replacing default driver factory implementation: " + key.getFactory()  + " key: " + key);
        }
        try {
            DriverFactory factory = key.getFactory().newInstance();
            registry.put(key.getKey(), factory);

        } catch (Exception e) {
            throw new SubstepsConfigurationException("Failed to create Webdriver factory: " + key.getFactory().toString(), e);
        }

    }

    public DriverFactory getDriverFactory(Config cfg){

        String key = cfg.getString(DRIVER_TYPE_KEY).toUpperCase();

        DriverFactory factory = registry.get(key);
        if (factory == null){
            throw new SubstepsConfigurationException("No factory registered / initialised for key: " + key);
        }
        return factory;
    }

    public WebDriver create(String key, Config cfg){

        log.info("creating webdriver: " + key);

        DriverFactory factory = registry.get(key);
        if (factory == null){
            throw new SubstepsConfigurationException("No factory registered / initialised for key: " + key);
        }
        WebDriver webDriver = factory.create(cfg);

        webDriver.manage().window().maximize();

        return webDriver;
    }
}
