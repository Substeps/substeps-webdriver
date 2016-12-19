package org.substeps.webdriver;

/**
 * Created by ian on 12/12/16.
 */
public enum FactoryInitialiser {
    INSTANCE;

    private FactoryInitialiser() {
        DriverFactoryRegistry.INSTANCE.register(ChromeDriverFactory.KEY);
        DriverFactoryRegistry.INSTANCE.register(FirefoxDriverFactory.KEY);
        DriverFactoryRegistry.INSTANCE.register(HTMLUnitDriverFactory.KEY);
        DriverFactoryRegistry.INSTANCE.register(IEDriverFactory.KEY);
        DriverFactoryRegistry.INSTANCE.register(RemoteDriverFactory.KEY);

    }
}
