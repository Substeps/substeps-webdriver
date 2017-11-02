package org.substeps.webdriver;

import com.technophobia.webdriver.util.WebDriverContext;
import com.typesafe.config.Config;
import org.openqa.selenium.WebDriver;

/**
 * Interface that Driver factories need to implement.
 *
 * @see BaseDriverFactory can also be extended instead that provides some default shutdown and reset functionality
 * <p>
 * Created by ian on 12/12/16.
 */
public interface DriverFactory {
    String DRIVER_FACTORY_KEY = "driverFactory";

    /**
     * Instantiate the webdriver.
     *
     * @param cfg the configuration in scope, implementations can access this in order to be able to set flags etc.
     * @return the created Driver
     */
    WebDriver create(Config cfg);

    /**
     * return a driver factory key that is placed in the context and accessible to steps to determine if this driver is visual
     *
     * @return the DriverFactoryKey associated with this DriverFactory
     */
    DriverFactoryKey getKey();

    /**
     * shut the webdriver down.
     * <p>
     * the driver can be obtained from the context:
     * <code>
     * WebDriver webDriver = webDriverContext.getWebDriver();
     * </code>
     *
     * @param webDriverContext the context containing the webdriver
     */
    void shutdownWebDriver(WebDriverContext webDriverContext);

    /**
     * reset the webdriver (between scenarios) to eliminate any leakage of state between test scenarios whilst removing the overhead of creating the driver every time
     *
     * @param webDriverContext the context containing the webdriver
     * @return true / false to indicate whether resetting was successful or not
     */
    boolean resetWebDriver(WebDriverContext webDriverContext);

}
