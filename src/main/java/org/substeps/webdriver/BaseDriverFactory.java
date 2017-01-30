package org.substeps.webdriver;

import com.technophobia.webdriver.util.WebDriverContext;
import com.typesafe.config.Config;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ian on 19/12/16.
 */
public abstract class BaseDriverFactory implements DriverFactory{

    private static final Logger log = LoggerFactory.getLogger(BaseDriverFactory.class);

    protected final void setupWebdriverManagerGithubAuth(Config cfg){

        // see https://github.com/settings/tokens
        if (cfg.hasPath("github.auth")){

            System.setProperty("wdm.gitHubTokenName", cfg.getString("github.auth.username"));
            System.setProperty("wdm.gitHubTokenSecret", cfg.getString("github.auth.token"));
        }
        else {
            log.info("webdriver manager may be unable to download drivers");
        }
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
