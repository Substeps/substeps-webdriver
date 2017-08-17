package org.substeps.webdriver;

import com.google.common.base.Strings;
import com.technophobia.webdriver.util.WebDriverContext;
import com.typesafe.config.Config;
import io.github.bonigarcia.wdm.WdmConfig;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ian on 19/12/16.
 */
public abstract class BaseDriverFactory implements DriverFactory, WebdriverSubstepsConfigurationKeys{

    private static final Logger log = LoggerFactory.getLogger(BaseDriverFactory.class);

    protected abstract WebDriver createInternal(Config cfg);

    @Override
    public WebDriver create(Config cfg){
        if (cfg.hasPath(WEBDRIVER_MANAGER_PROPERTIES_KEY) && !Strings.isNullOrEmpty(cfg.getString(WEBDRIVER_MANAGER_PROPERTIES_KEY))) {

            // see https://github.com/bonigarcia/webdrivermanager/blob/master/src/main/resources/webdrivermanager.properties

            System.setProperty("wdm.properties", cfg.getString(WEBDRIVER_MANAGER_PROPERTIES_KEY));
        }

        WebDriver webdriver = createInternal(cfg);

        try {
            WebDriverFactoryUtils.setScreensize(webdriver, cfg);
        }
        catch (WebDriverException e) {

        }
        return webdriver;
    }

    @Override
    public void shutdownWebDriver(WebDriverContext webDriverContext) {
        log.debug("Shutting WebDriver down");
        WebDriver webDriver = webDriverContext.getWebDriver();
        if(webDriver != null) {
            webDriver.manage().deleteAllCookies();

            try {
                ((JavascriptExecutor) webDriver).executeScript("window.localStorage.clear();");
                ((JavascriptExecutor) webDriver).executeScript("window.sessionStorage.clear();");

            }
            catch (Exception e){
                // best efforts
                log.error("Exception thrown executing js, closing webdriver down", e);
            }


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
