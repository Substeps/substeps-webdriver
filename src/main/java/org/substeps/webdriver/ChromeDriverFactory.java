package org.substeps.webdriver;

import com.typesafe.config.Config;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ian on 12/12/16.
 */
public class ChromeDriverFactory extends BaseDriverFactory implements DriverFactory{

    private static final Logger log = LoggerFactory.getLogger(ChromeDriverFactory.class);

    public static DriverFactoryKey KEY = new DriverFactoryKey("CHROME", true, ChromeDriverFactory.class);

    @Override
    public WebDriver create(Config cfg) {

        log.debug("creating chrome driver");

        String preset = System.getProperty("webdriver.chrome.driver");

        if (preset == null) {
            String driverPath = cfg.getString("chromedriver.path");
            Assert.assertNotNull("Chromedriver path not set as a -Dwebdriver.chrome.driver parameter or in config", driverPath);
            System.setProperty("webdriver.chrome.driver", driverPath);
        }

        final DesiredCapabilities chromeCapabilities = DesiredCapabilities.chrome();
        WebDriverFactoryUtils.setNetworkCapabilities(chromeCapabilities, cfg);

        WebDriverFactoryUtils.setLoggingPreferences(chromeCapabilities, cfg);

        return new ChromeDriver(chromeCapabilities);
    }

    @Override
    public DriverFactoryKey getKey() {
        return KEY;
    }
}
