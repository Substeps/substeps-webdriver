package org.substeps.webdriver;

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

    protected final void setupWebdriverManagerGithubAuth(Config cfg){

        // this is to allow the injection of github username and access token
        // see https://github.com/settings/tokens

        System.setProperty("wdm.properties", "substeps-webdrivermanager.properties");
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
