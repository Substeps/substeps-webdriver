package org.substeps.webdriver;

import com.google.common.base.Strings;
import com.technophobia.webdriver.util.WebDriverContext;
import com.typesafe.config.Config;
import io.github.bonigarcia.wdm.WdmConfig;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ian on 19/12/16.
 */
public abstract class BaseDriverFactory implements DriverFactory{

    private static final Logger log = LoggerFactory.getLogger(BaseDriverFactory.class);

    private static final String WEBDRIVER_MANAGER_PROPS_KEY = "webdriver.manager.properties";

    protected abstract WebDriver createInternal(Config cfg);

    @Override
    public WebDriver create(Config cfg){
        if (cfg.hasPath(WEBDRIVER_MANAGER_PROPS_KEY) && !Strings.isNullOrEmpty(cfg.getString(WEBDRIVER_MANAGER_PROPS_KEY))) {

            // see https://github.com/bonigarcia/webdrivermanager/blob/master/src/main/resources/webdrivermanager.properties

            System.setProperty("wdm.properties", cfg.getString(WEBDRIVER_MANAGER_PROPS_KEY));
        }
        return createInternal(cfg);
    }

    @Override
    public void shutdownWebDriver(WebDriverContext webDriverContext) {
        log.debug("Shutting WebDriver down");
        WebDriver webDriver = webDriverContext.getWebDriver();
        if(webDriver != null) {
            webDriver.manage().deleteAllCookies();
            webDriver.quit();
        }

    }

    @Override
    public boolean resetWebDriver(WebDriverContext webDriverContext) {
        log.debug("Resetting WebDriver");
        WebDriver webDriver = webDriverContext.getWebDriver();
        if(webDriver != null) {
            webDriver.manage().deleteAllCookies();
        }

        return true;
    }
}
