package org.substeps.webdriver;

import com.technophobia.webdriver.substeps.runner.DriverType;
import com.technophobia.webdriver.util.WebDriverContext;
import com.typesafe.config.Config;
import org.openqa.selenium.WebDriver;

/**
 * Created by ian on 12/12/16.
 */
public interface DriverFactory {
    static final String DRIVER_FACTORY_KEY = "driverFactory";

    WebDriver create(Config cfg);

    DriverFactoryKey getKey();

    void shutdownWebDriver(WebDriverContext webDriverContext);

    boolean resetWebDriver(WebDriverContext webDriverContext);

}
