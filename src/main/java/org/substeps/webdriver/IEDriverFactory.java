package org.substeps.webdriver;

import com.typesafe.config.Config;
import io.github.bonigarcia.wdm.FirefoxDriverManager;
import io.github.bonigarcia.wdm.InternetExplorerDriverManager;
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

    private static final String IEDRIVER_VERSION_KEY = "iedriver.version";

    @Override
    public DriverFactoryKey getKey() {
        return KEY;
    }

    @Override
    protected WebDriver createInternal(Config cfg) {

        log.debug("creating IE driver");

        if (cfg.hasPath(IEDRIVER_VERSION_KEY)) {
            InternetExplorerDriverManager.getInstance().setup(cfg.getString(IEDRIVER_VERSION_KEY));
        }
        else {
            InternetExplorerDriverManager.getInstance().setup();
        }


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
