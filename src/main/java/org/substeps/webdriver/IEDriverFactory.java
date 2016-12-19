package org.substeps.webdriver;

import com.typesafe.config.Config;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ian on 12/12/16.
 */
public class IEDriverFactory extends BaseDriverFactory implements DriverFactory{

    private static final Logger log = LoggerFactory.getLogger(IEDriverFactory.class);


    public static DriverFactoryKey KEY = new DriverFactoryKey("IE", true, IEDriverFactory.class);

    @Override
    public DriverFactoryKey getKey() {
        return KEY;
    }

    @Override
    public WebDriver create(Config cfg) {

        log.debug("creating IE driver");

        // apparently this is required to get around some IE security
        // restriction.
        final DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
        ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
                true);

        log.warn("Using IE Webdriver with IGNORING SECURITY DOMAIN");

        WebDriverFactoryUtils.setNetworkCapabilities(ieCapabilities, cfg);

        return new InternetExplorerDriver(ieCapabilities);

    }
}
