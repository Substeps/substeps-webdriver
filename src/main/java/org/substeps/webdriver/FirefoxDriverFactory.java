package org.substeps.webdriver;

import com.typesafe.config.Config;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.FirefoxDriverManager;
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

    private static final String FIREFOXDRIVER_VERSION_KEY = "firefoxdriver.version";


    @Override
    public DriverFactoryKey getKey() {
        return KEY;
    }

    @Override
    protected WebDriver createInternal(Config cfg) {

        log.debug("creating firefox driver");

        if (cfg.hasPath(FIREFOXDRIVER_VERSION_KEY)) {
            FirefoxDriverManager.getInstance().setup(cfg.getString(FIREFOXDRIVER_VERSION_KEY));
        }
        else {
            FirefoxDriverManager.getInstance().setup();
        }

        FirefoxProfile fp = new FirefoxProfile();
        fp.setPreference("browser.startup.homepage", "about:blank");
        fp.setPreference("startup.homepage_welcome_url", "about:blank");
        fp.setPreference("startup.homepage_welcome_url.additional", "about:blank");
        fp.setPreference("browser.startup.homepage_override.mston‌​e", "ignore");

        fp.setPreference("gecko.mstone", "ignore");


        final DesiredCapabilities firefoxCapabilities = DesiredCapabilities.firefox();

        WebDriverFactoryUtils.setNetworkCapabilities(firefoxCapabilities, cfg);

        WebDriverFactoryUtils.setLoggingPreferences(firefoxCapabilities, cfg);

        firefoxCapabilities.setCapability(FirefoxDriver.PROFILE, fp);

        return new FirefoxDriver(firefoxCapabilities);
    }
}
