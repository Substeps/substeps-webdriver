package org.substeps.webdriver;

import com.google.common.base.Strings;
import com.technophobia.webdriver.util.WebDriverContext;
import com.typesafe.config.Config;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class that sets up webdriver manager for the creation of webdrivers, implements shutdown and reset.
 */
public abstract class BaseDriverFactory implements DriverFactory, WebdriverSubstepsConfigurationKeys {

    private static final Logger log = LoggerFactory.getLogger(BaseDriverFactory.class);

    /**
     * Sub-classes should implement this method to construct the appropriate webdriver.  Config is passed in should the implementing class need to look anything up.
     * NB. this method simply needs to create the webdriver, binding into the ExecutionContext is managed elsewhere
     *
     * @param cfg the configuration in scope
     * @return the constructed webdriver
     */
    protected abstract WebDriver createInternal(Config cfg);


    /**
     * {@inheritDoc}
     */
    @Override
    public WebDriver create(Config cfg) {
        if (cfg.hasPath(WEBDRIVER_MANAGER_PROPERTIES_KEY) && !Strings.isNullOrEmpty(cfg.getString(WEBDRIVER_MANAGER_PROPERTIES_KEY))) {

            // see https://github.com/bonigarcia/webdrivermanager/blob/master/src/main/resources/webdrivermanager.properties

//            System.setProperty("wdm.properties", cfg.getString(WEBDRIVER_MANAGER_PROPERTIES_KEY));

            WebDriverManager.globalConfig().setProperties(cfg.getString(WEBDRIVER_MANAGER_PROPERTIES_KEY));
        }

        WebDriver webdriver = createInternal(cfg);

        WebDriverFactoryUtils.setScreensize(webdriver, cfg);
        return webdriver;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shutdownWebDriver(WebDriverContext webDriverContext) {
        log.debug("Shutting WebDriver down");
        WebDriver webDriver = webDriverContext.getWebDriver();
        if (webDriver != null) {
            webDriver.manage().deleteAllCookies();

            try {
                ((JavascriptExecutor) webDriver).executeScript("window.localStorage.clear();");
                ((JavascriptExecutor) webDriver).executeScript("window.sessionStorage.clear();");

            } catch (Exception e) {
                // best efforts
                log.error("Exception thrown executing js, closing webdriver down", e);
            }


            webDriver.quit();
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean resetWebDriver(WebDriverContext webDriverContext) {
        log.debug("Resetting WebDriver");
        WebDriver webDriver = webDriverContext.getWebDriver();
        if (webDriver != null) {
            webDriver.manage().deleteAllCookies();
        }

        return true;
    }
}
