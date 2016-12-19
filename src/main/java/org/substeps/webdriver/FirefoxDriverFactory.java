package org.substeps.webdriver;

import com.typesafe.config.Config;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ian on 12/12/16.
 */
public class FirefoxDriverFactory extends BaseDriverFactory implements DriverFactory{

    private static final Logger log = LoggerFactory.getLogger(FirefoxDriverFactory.class);

    public static DriverFactoryKey KEY = new DriverFactoryKey("FIREFOX", true, FirefoxDriverFactory.class);

    @Override
    public DriverFactoryKey getKey() {
        return KEY;
    }

    @Override
    public WebDriver create(Config cfg) {

        log.debug("creating firefox driver");

        FirefoxProfile fp = new FirefoxProfile();
        fp.setPreference("browser.startup.homepage", "about:blank");
        fp.setPreference("startup.homepage_welcome_url", "about:blank");
        fp.setPreference("startup.homepage_welcome_url.additional", "about:blank");
        fp.setPreference("browser.startup.homepage_override.mston‌​e", "ignore");

        fp.setPreference("gecko.mstone", "ignore");

        String preset = System.getProperty("webdriver.gecko.driver");

        if (preset == null) {
            String driverPath = cfg.getString("geckodriver.path");
            Assert.assertNotNull("Geckodriver path not set as a -Dwebdriver.gecko.driver parameter or in config", driverPath);
            System.setProperty("webdriver.gecko.driver", driverPath);
        }

        final DesiredCapabilities firefoxCapabilities = DesiredCapabilities.firefox();

        WebDriverFactoryUtils.setNetworkCapabilities(firefoxCapabilities, cfg);

        WebDriverFactoryUtils.setLoggingPreferences(firefoxCapabilities, cfg);

        firefoxCapabilities.setCapability(FirefoxDriver.PROFILE, fp);

        return new FirefoxDriver(firefoxCapabilities);
    }
}
